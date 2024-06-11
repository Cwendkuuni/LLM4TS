import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jdom2.*;
import java.util.*;

public class NamespaceStackTest {

  private NamespaceStack namespaceStack;
  private Element element;
  private Attribute attribute;

  @Before
  public void setUp() {
    namespaceStack = new NamespaceStack();
    element = new Element("element", Namespace.getNamespace("prefix", "uri"));
    attribute = new Attribute("attribute", "value", Namespace.getNamespace("prefix", "uri"));
  }

  @Test
  public void testPush_Element() {
    // Test push with element that has no additional namespaces or attributes
    namespaceStack.push(element);
    Namespace[] scope = namespaceStack.getScope();
    assertEquals(2, scope.length);
    assertEquals(Namespace.NO_NAMESPACE, scope[0]);
    assertEquals(Namespace.XML_NAMESPACE, scope[1]);

    // Test push with element that has additional namespaces and attributes
    element.addNamespaceDeclaration(Namespace.getNamespace("prefix2", "uri2"));
    element.setAttribute(attribute);
    namespaceStack.push(element);
    scope = namespaceStack.getScope();
    assertEquals(4, scope.length);
    assertEquals(Namespace.NO_NAMESPACE, scope[0]);
    assertEquals(Namespace.XML_NAMESPACE, scope[1]);
    assertEquals(Namespace.getNamespace("prefix", "uri"), scope[2]);
    assertEquals(Namespace.getNamespace("prefix2", "uri2"), scope[3]);
  }

  @Test
  public void testPush_Attribute() {
    // Test push with attribute
    namespaceStack.push(attribute);
    Namespace[] scope = namespaceStack.getScope();
    assertEquals(2, scope.length);
    assertEquals(Namespace.NO_NAMESPACE, scope[0]);
    assertEquals(Namespace.XML_NAMESPACE, scope[1]);
    assertTrue(namespaceStack.isInScope(Namespace.getNamespace("prefix", "uri")));
  }

  @Test(expected = IllegalStateException.class)
  public void testPop() {
    // Test pop with an empty stack
    namespaceStack.pop();

    // Test pop with a non-empty stack
    namespaceStack.push(attribute);
    Namespace[] scope = namespaceStack.getScope();
    assertEquals(2, scope.length);
    assertEquals(Namespace.NO_NAMESPACE, scope[0]);
    assertEquals(Namespace.XML_NAMESPACE, scope[1]);
    namespaceStack.pop();
    scope = namespaceStack.getScope();
    assertEquals(2, scope.length);
    assertEquals(Namespace.NO_NAMESPACE, scope[0]);
    assertEquals(Namespace.XML_NAMESPACE, scope[1]);
  }

  @Test
  public void testAddedForward() {
    // Test addedForward with an empty stack
    Iterable<Namespace> iter = namespaceStack.addedForward();
    assertFalse(iter.iterator().hasNext());

    // Test addedForward with a non-empty stack
    namespaceStack.push(element);
    iter = namespaceStack.addedForward();
    Iterator<Namespace> iterator = iter.iterator();
    assertTrue(iterator.hasNext());
    assertEquals(Namespace.getNamespace("prefix", "uri"), iterator.next());
    assertFalse(iterator.hasNext());
  }

  @Test
  public void testAddedReverse() {
    // Test addedReverse with an empty stack
    Iterable<Namespace> iter = namespaceStack.addedReverse();
    assertFalse(iter.iterator().hasNext());

    // Test addedReverse with a non-empty stack
    namespaceStack.push(element);
    iter = namespaceStack.addedReverse();
    Iterator<Namespace> iterator = iter.iterator();
    assertTrue(iterator.hasNext());
    assertEquals(Namespace.getNamespace("prefix", "uri"), iterator.next());
    assertFalse(iterator.hasNext());
  }

  @Test
  public void testIterator() {
    // Test iterator with an empty stack
    Iterator<Namespace> iterator = namespaceStack.iterator();
    assertFalse(iterator.hasNext());

    // Test iterator with a non-empty stack
    namespaceStack.push(element);
    iterator = namespaceStack.iterator();
    assertTrue(iterator.hasNext());
    assertEquals(Namespace.NO_NAMESPACE, iterator.next());
    assertEquals(Namespace.XML_NAMESPACE, iterator.next());
    assertEquals(Namespace.getNamespace("prefix", "uri"), iterator.next());
    assertFalse(iterator.hasNext());
  }

  @Test
  public void testIsInScope() {
    // Test isInScope with a namespace in scope
    namespaceStack.push(element);
    assertTrue(namespaceStack.isInScope(Namespace.NO_NAMESPACE));
    assertTrue(namespaceStack.isInScope(Namespace.XML_NAMESPACE));
    assertTrue(namespaceStack.isInScope(Namespace.getNamespace("prefix", "uri")));

    // Test isInScope with a namespace not in scope
    assertFalse(namespaceStack.isInScope(Namespace.getNamespace("prefix2", "uri2")));
  }
}