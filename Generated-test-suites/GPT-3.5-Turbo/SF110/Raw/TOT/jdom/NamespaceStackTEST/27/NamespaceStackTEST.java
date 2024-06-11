
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceStackTest {
  
  @Test
  public void testPush_ElementWithNoAdditionalNamespacesAndNoAttributes_PushesNamespaceToStack() {
    Element element = new Element("test");
    NamespaceStack namespaceStack = new NamespaceStack();
    
    namespaceStack.push(element);
    
    assertTrue(namespaceStack.isInScope(element.getNamespace()));
  }
  
  @Test
  public void testPush_ElementWithAdditionalNamespaces_PushesNamespacesToStack() {
    Namespace testNamespace = Namespace.getNamespace("test", "http://www.test.com");
    NamespaceStack namespaceStack = new NamespaceStack();
    Element element = new Element("test", testNamespace);
    Namespace additionalNamespace = Namespace.getNamespace("additional", "http://www.additional.com");
    element.addNamespaceDeclaration(additionalNamespace);
    
    namespaceStack.push(element);
    
    assertTrue(namespaceStack.isInScope(testNamespace));
    assertTrue(namespaceStack.isInScope(additionalNamespace));
  }
  
  @Test
  public void testPush_ElementWithAttributes_PushesNamespacesInAttributesToStack() {
    Namespace testNamespace = Namespace.getNamespace("test", "http://www.test.com");
    NamespaceStack namespaceStack = new NamespaceStack();
    Element element = new Element("test", testNamespace);
    Namespace attributeNamespace = Namespace.getNamespace("attribute", "http://www.attribute.com");
    Attribute attribute = new Attribute("attribute", "value", attributeNamespace);
    element.setAttribute(attribute);
    
    namespaceStack.push(element);
    
    assertTrue(namespaceStack.isInScope(testNamespace));
    assertTrue(namespaceStack.isInScope(attributeNamespace));
  }
  
  @Test
  public void testPush_ElementWithAttributesAndAdditionalNamespaces_PushesAllNamespacesToStack() {
    Namespace testNamespace = Namespace.getNamespace("test", "http://www.test.com");
    NamespaceStack namespaceStack = new NamespaceStack();
    Element element = new Element("test", testNamespace);
    Namespace additionalNamespace = Namespace.getNamespace("additional", "http://www.additional.com");
    element.addNamespaceDeclaration(additionalNamespace);
    Namespace attributeNamespace = Namespace.getNamespace("attribute", "http://www.attribute.com");
    Attribute attribute = new Attribute("attribute", "value", attributeNamespace);
    element.setAttribute(attribute);
    
    namespaceStack.push(element);
    
    assertTrue(namespaceStack.isInScope(testNamespace));
    assertTrue(namespaceStack.isInScope(additionalNamespace));
    assertTrue(namespaceStack.isInScope(attributeNamespace));
  }
  
  @Test
  public void testPush_AttributeWithNoNamespace_PushesNoNamespaceToStack() {
    Attribute attribute = new Attribute("attribute", "value");
    NamespaceStack namespaceStack = new NamespaceStack();
    
    namespaceStack.push(attribute);
    
    assertFalse(namespaceStack.isInScope(Namespace.NO_NAMESPACE));
  }
  
  @Test
  public void testPush_AttributeWithNamespace_PushesNamespaceToStack() {
    Namespace attributeNamespace = Namespace.getNamespace("attribute", "http://www.attribute.com");
    Attribute attribute = new Attribute("attribute", "value", attributeNamespace);
    NamespaceStack namespaceStack = new NamespaceStack();
    
    namespaceStack.push(attribute);
    
    assertTrue(namespaceStack.isInScope(attributeNamespace));
  }
  
  @Test(expected=IllegalStateException.class)
  public void testPop_PopWhenStackIsEmpty_ThrowsIllegalStateException() {
    NamespaceStack namespaceStack = new NamespaceStack();
    
    namespaceStack.pop();
  }
  
  @Test
  public void testPop_PopWhenStackHasElements_RemovesTopElementFromStack() {
    NamespaceStack namespaceStack = new NamespaceStack();
    Element element = new Element("test");
    namespaceStack.push(element);
    
    namespaceStack.pop();
    
    assertFalse(namespaceStack.isInScope(element.getNamespace()));
  }
  
  @Test
  public void testAddedForward_StackIsEmpty_ReturnsEmptyIterable() {
    NamespaceStack namespaceStack = new NamespaceStack();
    
    Iterable<Namespace> iterable = namespaceStack.addedForward();
    
    assertFalse(iterable.iterator().hasNext());
  }
  
  @Test
  public void testAddedForward_StackIsNotEmpty_ReturnsForwardIterable() {
    NamespaceStack namespaceStack = new NamespaceStack();
    Element element = new Element("test");
    Namespace namespace = element.getNamespace();
    namespaceStack.push(element);
    
    Iterable<Namespace> iterable = namespaceStack.addedForward();
    
    assertTrue(iterable.iterator().hasNext());
    assertEquals(namespace, iterable.iterator().next());
  }
  
  @Test
  public void testAddedReverse_StackIsEmpty_ReturnsEmptyIterable() {
    NamespaceStack namespaceStack = new NamespaceStack();
    
    Iterable<Namespace> iterable = namespaceStack.addedReverse();
    
    assertFalse(iterable.iterator().hasNext());
  }
  
  @Test
  public void testAddedReverse_StackIsNotEmpty_ReturnsBackwardIterable() {
    NamespaceStack namespaceStack = new NamespaceStack();
    Element element = new Element("test");
    Namespace namespace = element.getNamespace();
    namespaceStack.push(element);
    
    Iterable<Namespace> iterable = namespaceStack.addedReverse();
    
    assertTrue(iterable.iterator().hasNext());
    assertEquals(namespace, iterable.iterator().next());
  }
  
  @Test
  public void testIterator_StackIsEmpty_ReturnsEmptyIterator() {
    NamespaceStack namespaceStack = new NamespaceStack();
    
    assertFalse(namespaceStack.iterator().hasNext());
  }
  
  @Test
  public void testIterator_StackIsNotEmpty_ReturnsIteratorWithNamespacesInStack() {
    NamespaceStack namespaceStack = new NamespaceStack();
    Element element = new Element("test");
    Namespace namespace = element.getNamespace();
    namespaceStack.push(element);
    
    assertTrue(namespaceStack.iterator().hasNext());
    assertEquals(namespace, namespaceStack.iterator().next());
  }
  
  @Test
  public void testGetScope_StackIsEmpty_ReturnsEmptyArray() {
    NamespaceStack namespaceStack = new NamespaceStack();
    
    assertEquals(0, namespaceStack.getScope().length);
  }
  
  @Test
  public void testGetScope_StackIsNotEmpty_ReturnsArrayWithNamespacesInStack() {
    NamespaceStack namespaceStack = new NamespaceStack();
    Element element = new Element("test");
    Namespace namespace = element.getNamespace();
    namespaceStack.push(element);
    
    assertEquals(namespace, namespaceStack.getScope()[0]);
  }
  
  @Test
  public void testIsInScope_NamespaceInStack_ReturnsTrue() {
    NamespaceStack namespaceStack = new NamespaceStack();
    Element element = new Element("test");
    Namespace namespace = element.getNamespace();
    namespaceStack.push(element);
    
    assertTrue(namespaceStack.isInScope(namespace));
  }
  
  @Test
  public void testIsInScope_NamespaceNotInStack_ReturnsFalse() {
    NamespaceStack namespaceStack = new NamespaceStack();
    Namespace namespace = Namespace.getNamespace("test", "http://www.test.com");
    
    assertFalse(namespaceStack.isInScope(namespace));
  }
}
