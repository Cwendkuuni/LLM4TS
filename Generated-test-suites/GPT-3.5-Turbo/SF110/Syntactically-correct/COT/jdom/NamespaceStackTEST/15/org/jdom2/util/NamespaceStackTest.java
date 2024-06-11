import static org.junit.Assert.*;
import org.junit.Test;
import org.jdom2.*;
import java.util.*;

public class NamespaceStackTest {

  @Test
  public void testPush_Element() {
    NamespaceStack stack = new NamespaceStack();
    Element element = new Element("test");
    stack.push(element);
    Namespace[] scope = stack.getScope();
    assertEquals(3, scope.length);
    assertEquals(Namespace.NO_NAMESPACE, scope[0]);
    assertEquals(Namespace.XML_NAMESPACE, scope[1]);
    assertEquals(element.getNamespace(), scope[2]);
  }

  @Test
  public void testPush_Attribute() {
    NamespaceStack stack = new NamespaceStack();
    Attribute att = new Attribute("name", "value", Namespace.XML_NAMESPACE);
    stack.push(att);
    Namespace[] scope = stack.getScope();
    assertEquals(3, scope.length);
    assertEquals(Namespace.NO_NAMESPACE, scope[0]);
    assertEquals(Namespace.XML_NAMESPACE, scope[1]);
    assertEquals(att.getNamespace(), scope[2]);
  }

  @Test
  public void testPop() {
    NamespaceStack stack = new NamespaceStack();
    Element element = new Element("test");
    stack.push(element);
    stack.pop();
    Namespace[] scope = stack.getScope();
    assertEquals(2, scope.length);
    assertEquals(Namespace.NO_NAMESPACE, scope[0]);
    assertEquals(Namespace.XML_NAMESPACE, scope[1]);
  }

  @Test(expected = IllegalStateException.class)
  public void testPop_Exception() {
    NamespaceStack stack = new NamespaceStack();
    stack.pop();
  }
  
  @Test
  public void testAddedForward_Empty() {
    NamespaceStack stack = new NamespaceStack();
    Iterable<Namespace> added = stack.addedForward();
    assertFalse(added.iterator().hasNext());
  }
  
  @Test
  public void testAddedForward() {
    NamespaceStack stack = new NamespaceStack();
    Element element = new Element("test");
    stack.push(element);
    Iterable<Namespace> added = stack.addedForward();
    Iterator<Namespace> it = added.iterator();
    assertTrue(it.hasNext());
    assertEquals(element.getNamespace(), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void testAddedReverse_Empty() {
    NamespaceStack stack = new NamespaceStack();
    Iterable<Namespace> added = stack.addedReverse();
    assertFalse(added.iterator().hasNext());
  }
  
  @Test
  public void testAddedReverse() {
    NamespaceStack stack = new NamespaceStack();
    Element element = new Element("test");
    stack.push(element);
    Iterable<Namespace> added = stack.addedReverse();
    Iterator<Namespace> it = added.iterator();
    assertTrue(it.hasNext());
    assertEquals(element.getNamespace(), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void testIterator() {
    NamespaceStack stack = new NamespaceStack();
    Element element = new Element("test");
    stack.push(element);
    Iterator<Namespace> it = stack.iterator();
    assertTrue(it.hasNext());
    assertEquals(Namespace.NO_NAMESPACE, it.next());
    assertEquals(Namespace.XML_NAMESPACE, it.next());
    assertEquals(element.getNamespace(), it.next());
    assertFalse(it.hasNext());
  }
  
  @Test
  public void testGetScope() {
    NamespaceStack stack = new NamespaceStack();
    Element element = new Element("test");
    stack.push(element);
    Namespace[] scope = stack.getScope();
    assertEquals(3, scope.length);
    assertEquals(Namespace.NO_NAMESPACE, scope[0]);
    assertEquals(Namespace.XML_NAMESPACE, scope[1]);
    assertEquals(element.getNamespace(), scope[2]);
  }

  @Test
  public void testIsInScope_True() {
    NamespaceStack stack = new NamespaceStack();
    Element element = new Element("test");
    stack.push(element);
    assertTrue(stack.isInScope(element.getNamespace()));
  }
  
  @Test
  public void testIsInScope_False() {
    NamespaceStack stack = new NamespaceStack();
    Element element = new Element("test");
    assertFalse(stack.isInScope(element.getNamespace()));
  }
}