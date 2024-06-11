
import static org.junit.Assert.*;
import org.junit.Test;

public class NamespaceStackTest {
  @Test
  public void testPush_Element() {
    NamespaceStack stack = new NamespaceStack();
    Element element = new Element("test", "http://www.example.com");
    
    stack.push(element);
    
    assertEquals("http://www.example.com", stack.getScope()[0].getURI());
    assertTrue(stack.isInScope(element.getNamespace()));
  }
  
  @Test
  public void testPush_Attribute() {
    NamespaceStack stack = new NamespaceStack();
    Attribute attribute = new Attribute("test", "example", "http://www.example.com");
    
    stack.push(attribute);
    
    assertEquals("http://www.example.com", stack.getScope()[0].getURI());
    assertTrue(stack.isInScope(attribute.getNamespace()));
  }
  
  @Test
  public void testPop() {
    NamespaceStack stack = new NamespaceStack();
    Element element = new Element("test", "http://www.example.com");
    stack.push(element);
    
    stack.pop();
    
    assertEquals(Namespace.NO_NAMESPACE, stack.getScope()[0]);
    assertFalse(stack.isInScope(element.getNamespace()));
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
    Element element = new Element("test", "http://www.example.com");
    stack.push(element);
    
    Iterable<Namespace> added = stack.addedForward();
    Iterator<Namespace> iterator = added.iterator();
    
    assertTrue(iterator.hasNext());
    Namespace namespace = iterator.next();
    assertEquals("http://www.example.com", namespace.getURI());
    assertFalse(iterator.hasNext());
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
    Element element = new Element("test", "http://www.example.com");
    stack.push(element);
    
    Iterable<Namespace> added = stack.addedReverse();
    Iterator<Namespace> iterator = added.iterator();
    
    assertTrue(iterator.hasNext());
    Namespace namespace = iterator.next();
    assertEquals("http://www.example.com", namespace.getURI());
    assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testGetScope() {
    NamespaceStack stack = new NamespaceStack();
    Element element = new Element("test", "http://www.example.com");
    stack.push(element);
    
    Namespace[] scope = stack.getScope();
    
    assertEquals("http://www.example.com", scope[0].getURI());
    assertTrue(stack.isInScope(scope[0]));
  }
  
  @Test
  public void testIsInScope_NotInScope() {
    NamespaceStack stack = new NamespaceStack();
    Element element = new Element("test", "http://www.example.com");
    
    boolean isInScope = stack.isInScope(element.getNamespace());
    
    assertFalse(isInScope);
  }
  
  @Test
  public void testIsInScope_InScope() {
    NamespaceStack stack = new NamespaceStack();
    Element element = new Element("test", "http://www.example.com");
    stack.push(element);
    
    boolean isInScope = stack.isInScope(element.getNamespace());
    
    assertTrue(isInScope);
  }
}
