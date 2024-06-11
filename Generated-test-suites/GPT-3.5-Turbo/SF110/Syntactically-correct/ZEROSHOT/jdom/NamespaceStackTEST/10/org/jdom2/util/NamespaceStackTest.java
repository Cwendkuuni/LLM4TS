import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class NamespaceStackTest {
  
  @Test
  public void testPushWithElement() {
    Element element = new Element("test");
    NamespaceStack namespaceStack = new NamespaceStack();
    namespaceStack.push(element);
    Namespace[] expectedScope = { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, element.getNamespace() };
    assertArrayEquals(expectedScope, namespaceStack.getScope());
  }
  
  @Test
  public void testPushWithAttribute() {
    Attribute attribute = new Attribute("test", "value", Namespace.NO_NAMESPACE);
    NamespaceStack namespaceStack = new NamespaceStack();
    namespaceStack.push(attribute);
    Namespace[] expectedScope = { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, attribute.getNamespace() };
    assertArrayEquals(expectedScope, namespaceStack.getScope());
  }
  
  @Test
  public void testPop() {
    Element element = new Element("test");
    NamespaceStack namespaceStack = new NamespaceStack();
    namespaceStack.push(element);
    assertEquals(true, namespaceStack.isInScope(element.getNamespace()));
    namespaceStack.pop();
    assertEquals(false, namespaceStack.isInScope(element.getNamespace()));
  }
  
  @Test
  public void testAddedForwardWithNamespaces() {
    Element element = new Element("test");
    NamespaceStack namespaceStack = new NamespaceStack();
    namespaceStack.push(element);
    Iterable<Namespace> addedForward = namespaceStack.addedForward();
    List<Namespace> addedForwardList = new ArrayList<>();
    Iterator<Namespace> iterator = addedForward.iterator();
    while(iterator.hasNext()) {
      addedForwardList.add(iterator.next());
    }
    assertEquals(2, addedForwardList.size());
  }
  
  @Test
  public void testAddedForwardWithoutNamespaces() {
    NamespaceStack namespaceStack = new NamespaceStack();
    Iterable<Namespace> addedForward = namespaceStack.addedForward();
    List<Namespace> addedForwardList = new ArrayList<>();
    Iterator<Namespace> iterator = addedForward.iterator();
    while(iterator.hasNext()) {
      addedForwardList.add(iterator.next());
    }
    assertEquals(0, addedForwardList.size());
  }
  
  @Test
  public void testAddedReverseWithNamespaces() {
    Element element = new Element("test");
    NamespaceStack namespaceStack = new NamespaceStack();
    namespaceStack.push(element);
    Iterable<Namespace> addedReverse = namespaceStack.addedReverse();
    List<Namespace> addedReverseList = new ArrayList<>();
    Iterator<Namespace> iterator = addedReverse.iterator();
    while(iterator.hasNext()) {
      addedReverseList.add(iterator.next());
    }
    assertEquals(2, addedReverseList.size());
  }
  
  @Test
  public void testAddedReverseWithoutNamespaces() {
    NamespaceStack namespaceStack = new NamespaceStack();
    Iterable<Namespace> addedReverse = namespaceStack.addedReverse();
    List<Namespace> addedReverseList = new ArrayList<>();
    Iterator<Namespace> iterator = addedReverse.iterator();
    while(iterator.hasNext()) {
      addedReverseList.add(iterator.next());
    }
    assertEquals(0, addedReverseList.size());
  }
  
  @Test
  public void testIterator() {
    Element element = new Element("test");
    Attribute attribute = new Attribute("test", "value", Namespace.NO_NAMESPACE);
    NamespaceStack namespaceStack = new NamespaceStack();
    namespaceStack.push(element);
    namespaceStack.push(attribute);
    Iterable<Namespace> iterable = namespaceStack;
    List<Namespace> iterableList = new ArrayList<>();
    for(Namespace namespace : iterable) {
      iterableList.add(namespace);
    }
    assertEquals(3, iterableList.size());
  }
  
  @Test
  public void testGetScope() {
    Element element = new Element("test");
    NamespaceStack namespaceStack = new NamespaceStack();
    namespaceStack.push(element);
    Namespace[] expectedScope = { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, element.getNamespace() };
    assertArrayEquals(expectedScope, namespaceStack.getScope());
  }
  
  @Test
  public void testIsInScopeWithNamespaceInScope() {
    Element element = new Element("test");
    NamespaceStack namespaceStack = new NamespaceStack();
    namespaceStack.push(element);
    assertEquals(true, namespaceStack.isInScope(element.getNamespace()));
  }
  
  @Test
  public void testIsInScopeWithNamespaceNotInScope() {
    Element element = new Element("test");
    NamespaceStack namespaceStack = new NamespaceStack();
    Namespace namespace = Namespace.getNamespace("http://www.example.com");
    assertEquals(false, namespaceStack.isInScope(namespace));
  }
}