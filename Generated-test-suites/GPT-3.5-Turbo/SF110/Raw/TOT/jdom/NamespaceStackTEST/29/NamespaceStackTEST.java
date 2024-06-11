import static org.junit.Assert.*;
import org.junit.Test;
import org.jdom2.Namespace;

public class NamespaceStackTest {

  @Test
  public void testPush_WithElement_AddsElementNamespace() {
    NamespaceStack stack = new NamespaceStack();
    Element element = new Element("test", "http://www.example.com");
    stack.push(element);
    Namespace[] result = stack.getScope();
    assertEquals(2, result.length);
    assertEquals(Namespace.NO_NAMESPACE, result[0]);
    assertEquals("http://www.example.com", result[1].getURI());
  }

  @Test
  public void testPush_WithAttribute_AddsAttributeNamespace() {
    NamespaceStack stack = new NamespaceStack();
    Attribute attribute = new Attribute("key", "value", Namespace.getNamespace("http://www.example.com"));
    stack.push(attribute);
    Namespace[] result = stack.getScope();
    assertEquals(2, result.length);
    assertEquals(Namespace.NO_NAMESPACE, result[0]);
    assertEquals("http://www.example.com", result[1].getURI());
  }
  
  @Test
  public void testPush_WithElement_HasAdditionalNamespaces_AddsNamespaces() {
    NamespaceStack stack = new NamespaceStack();
    Element element = new Element("test", "http://www.example.com");
    Namespace ns1 = Namespace.getNamespace("prefix1", "http://www.namespace1.com");
    Namespace ns2 = Namespace.getNamespace("prefix2", "http://www.namespace2.com");
    element.addNamespaceDeclaration(ns1);
    element.addNamespaceDeclaration(ns2);
    stack.push(element);
    Namespace[] result = stack.getScope();
    assertEquals(4, result.length);
    assertEquals(Namespace.NO_NAMESPACE, result[0]);
    assertEquals("http://www.example.com", result[1].getURI());
    assertEquals("http://www.namespace1.com", result[2].getURI());
    assertEquals("http://www.namespace2.com", result[3].getURI());
  }
  
  @Test
  public void testPush_WithElement_HasAttributes_AddsAttributeNamespaces() {
    NamespaceStack stack = new NamespaceStack();
    Element element = new Element("test", "http://www.example.com");
    Attribute attr1 = new Attribute("key1", "value1", Namespace.getNamespace("prefix1", "http://www.namespace1.com"));
    Attribute attr2 = new Attribute("key2", "value2", Namespace.getNamespace("prefix2", "http://www.namespace2.com"));
    Attribute attr3 = new Attribute("key3", "value3", Namespace.getNamespace("prefix1", "http://www.namespace1.com"));
    element.setAttribute(attr1);
    element.setAttribute(attr2);
    element.setAttribute(attr3);
    stack.push(element);
    Namespace[] result = stack.getScope();
    assertEquals(4, result.length);
    assertEquals(Namespace.NO_NAMESPACE, result[0]);
    assertEquals("http://www.example.com", result[1].getURI());
    assertEquals("http://www.namespace1.com", result[2].getURI());
    assertEquals("http://www.namespace2.com", result[3].getURI());
  }

  @Test
  public void testPop_WithNonEmptyStack_RemovesTopEntry() {
    NamespaceStack stack = new NamespaceStack();
    stack.push(new Element("test", "http://www.example.com"));
    stack.push(new Element("test2", "http://www.example2.com"));
    stack.pop();
    Namespace[] result = stack.getScope();
    assertEquals(2, result.length);
    assertEquals(Namespace.NO_NAMESPACE, result[0]);
    assertEquals("http://www.example.com", result[1].getURI());
  }

  @Test(expected = IllegalStateException.class)
  public void testPop_WithEmptyStack_ThrowsIllegalStateException() {
    NamespaceStack stack = new NamespaceStack();
    stack.pop();
  }
  
  @Test
  public void testIsInScope_WithNamespaceInScope_ReturnsTrue() {
    NamespaceStack stack = new NamespaceStack();
    stack.push(new Element("test", "http://www.example.com"));
    assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
  }
  
  @Test
  public void testIsInScope_WithNamespaceNotInScope_ReturnsFalse() {
    NamespaceStack stack = new NamespaceStack();
    stack.push(new Element("test", "http://www.example.com"));
    assertFalse(stack.isInScope(Namespace.getNamespace("http://www.google.com")));
  }

}