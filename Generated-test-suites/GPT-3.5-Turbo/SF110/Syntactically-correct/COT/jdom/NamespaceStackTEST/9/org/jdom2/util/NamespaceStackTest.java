
import static org.junit.Assert.*;
import org.junit.Test;
import org.jdom2.*;
import java.util.*;

public class NamespaceStackTest {
  
  /**
   * Test method for {@link org.jdom2.util.NamespaceStack#push(org.jdom2.Element)}.
   */
  @Test
  public void testPushElement() {
    NamespaceStack stack = new NamespaceStack();
    Element element = new Element("test", Namespace.getNamespace("http://www.example.com/test"));
    
    // Push element to the stack
    stack.push(element);
    
    // The pushed element should be in the topmost scope
    assertEquals(Namespace.getNamespace("http://www.example.com/test"), stack.getScope()[0]);
  }
  
  /**
   * Test method for {@link org.jdom2.util.NamespaceStack#push(org.jdom2.Attribute)}.
   */
  @Test
  public void testPushAttribute() {
    NamespaceStack stack = new NamespaceStack();
    Attribute attribute = new Attribute("test", "value", Namespace.getNamespace("http://www.example.com/test"));
    
    // Push attribute to the stack
    stack.push(attribute);
    
    // The pushed attribute's namespace should be in the topmost scope
    assertEquals(Namespace.getNamespace("http://www.example.com/test"), stack.getScope()[0]);
  }
  
  /**
   * Test method for {@link org.jdom2.util.NamespaceStack#pop()}.
   */
  @Test
  public void testPop() {
    NamespaceStack stack = new NamespaceStack();
    Element element = new Element("test", Namespace.getNamespace("http://www.example.com/test"));
    
    // Push element to the stack
    stack.push(element);
    
    // Pop the element from the stack
    stack.pop();
    
    // The stack should be empty after popping the element
    assertTrue(stack.getScope().length == 0);
  }
  
  /**
   * Test method for {@link org.jdom2.util.NamespaceStack#addedForward()}.
   */
  @Test
  public void testAddedForward() {
    NamespaceStack stack = new NamespaceStack();
    Element element = new Element("test", Namespace.getNamespace("http://www.example.com/test"));
    
    // Push element to the stack
    stack.push(element);
    
    // Iterate over the added namespaces in forward order
    for (Namespace ns : stack.addedForward()) {
      assertEquals(Namespace.getNamespace("http://www.example.com/test"), ns);
    }
  }
  
  /**
   * Test method for {@link org.jdom2.util.NamespaceStack#addedReverse()}.
   */
  @Test
  public void testAddedReverse() {
    NamespaceStack stack = new NamespaceStack();
    Element element = new Element("test", Namespace.getNamespace("http://www.example.com/test"));
    
    // Push element to the stack
    stack.push(element);
    
    // Iterate over the added namespaces in reverse order
    for (Namespace ns : stack.addedReverse()) {
      assertEquals(Namespace.getNamespace("http://www.example.com/test"), ns);
    }
  }
  
  /**
   * Test method for {@link org.jdom2.util.NamespaceStack#iterator()}.
   */
  @Test
  public void testIterator() {
    NamespaceStack stack = new NamespaceStack();
    Element element = new Element("test", Namespace.getNamespace("http://www.example.com/test"));
    
    // Push element to the stack
    stack.push(element);
    
    // Iterate over the namespaces in the stack
    for (Namespace ns : stack) {
      assertEquals(Namespace.getNamespace("http://www.example.com/test"), ns);
    }
  }
  
  /**
   * Test method for {@link org.jdom2.util.NamespaceStack#getScope()}.
   */
  @Test
  public void testGetScope() {
    NamespaceStack stack = new NamespaceStack();
    Element element = new Element("test", Namespace.getNamespace("http://www.example.com/test"));
    
    // Push element to the stack
    stack.push(element);
    
    // The pushed element should be in the topmost scope
    assertEquals(Namespace.getNamespace("http://www.example.com/test"), stack.getScope()[0]);
  }
  
  /**
   * Test method for {@link org.jdom2.util.NamespaceStack#isInScope(org.jdom2.Namespace)}.
   */
  @Test
  public void testIsInScope() {
    NamespaceStack stack = new NamespaceStack();
    Element element = new Element("test", Namespace.getNamespace("http://www.example.com/test"));
    
    // Push element to the stack
    stack.push(element);
    
    // Check if the pushed element's namespace is in the stack's scope
    assertTrue(stack.isInScope(Namespace.getNamespace("http://www.example.com/test")));
  }
}
