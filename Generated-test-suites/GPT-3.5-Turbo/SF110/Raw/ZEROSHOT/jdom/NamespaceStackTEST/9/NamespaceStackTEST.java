import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceStackTest {
  
  private NamespaceStack nsStack;
  
  @Before
  public void setUp() {
    nsStack = new NamespaceStack();
  }
  
  @Test
  public void testPush() {
    // Test when element has no additional namespaces and attributes
    Element element1 = new Element("element1");
    nsStack.push(element1);
    assertEquals(1, nsStack.getScope().length);
    
    // Test when element has additional namespaces and attributes
    Element element2 = new Element("element2");
    element2.addNamespaceDeclaration(Namespace.getNamespace("ns1", "http://ns1"));
    Attribute attribute1 = new Attribute("attribute1", "value", Namespace.getNamespace("ns2", "http://ns2"));
    element2.setAttribute(attribute1);
    nsStack.push(element2);
    assertEquals(3, nsStack.getScope().length);
  }
  
  @Test
  public void testPushAttribute() {
    Attribute attribute1 = new Attribute("attribute1", "value1", Namespace.getNamespace("ns1", "http://ns1"));
    nsStack.push(attribute1);
    assertEquals(2, nsStack.getScope().length);
  }
  
  @Test
  public void testPop() {
    // Test when stack depth is greater than 0
    Element element1 = new Element("element1");
    nsStack.push(element1);
    nsStack.pop();
    assertEquals(1, nsStack.getScope().length);
    
    // Test when stack depth is 0
    nsStack.pop();
    assertEquals(0, nsStack.getScope().length);
  }
  
  @Test
  public void testAddedForward() {
    // Test when no namespaces have been added
    Iterable<Namespace> addedForward = nsStack.addedForward();
    assertFalse(addedForward.iterator().hasNext());
    
    // Test when namespaces have been added
    Element element1 = new Element("element1");
    element1.addNamespaceDeclaration(Namespace.getNamespace("ns1", "http://ns1"));
    element1.addNamespaceDeclaration(Namespace.getNamespace("ns2", "http://ns2"));
    nsStack.push(element1);
    addedForward = nsStack.addedForward();
    assertTrue(addedForward.iterator().hasNext());
    assertEquals(2, ((Object[]) addedForward.toArray()).length);
  }
  
  @Test
  public void testAddedReverse() {
    // Test when no namespaces have been added
    Iterable<Namespace> addedReverse = nsStack.addedReverse();
    assertFalse(addedReverse.iterator().hasNext());
    
    // Test when namespaces have been added
    Element element1 = new Element("element1");
    element1.addNamespaceDeclaration(Namespace.getNamespace("ns1", "http://ns1"));
    element1.addNamespaceDeclaration(Namespace.getNamespace("ns2", "http://ns2"));
    nsStack.push(element1);
    addedReverse = nsStack.addedReverse();
    assertTrue(addedReverse.iterator().hasNext());
    assertEquals(2, ((Object[]) addedReverse.toArray()).length);
  }
  
  @Test
  public void testIterator() {
    // Test when no namespaces are in scope
    Iterable<Namespace> iterable = nsStack;
    assertFalse(iterable.iterator().hasNext());
    
    // Test when namespaces are in scope
    Element element1 = new Element("element1");
    element1.addNamespaceDeclaration(Namespace.getNamespace("ns1", "http://ns1"));
    element1.addNamespaceDeclaration(Namespace.getNamespace("ns2", "http://ns2"));
    nsStack.push(element1);
    iterable = nsStack;
    assertTrue(iterable.iterator().hasNext());
    assertEquals(2, ((Object[]) iterable.iterator().toArray()).length);
  }
  
  @Test
  public void testGetScope() {
    // Test when no namespaces are in scope
    Namespace[] scope = nsStack.getScope();
    assertEquals(0, scope.length);
    
    // Test when namespaces are in scope
    Element element1 = new Element("element1");
    element1.addNamespaceDeclaration(Namespace.getNamespace("ns1", "http://ns1"));
    element1.addNamespaceDeclaration(Namespace.getNamespace("ns2", "http://ns2"));
    nsStack.push(element1);
    scope = nsStack.getScope();
    assertEquals(2, scope.length);
  }
  
  @Test
  public void testIsInScope() {
    // Test when namespace is in scope
    Namespace namespace1 = Namespace.getNamespace("ns1", "http://ns1");
    Element element1 = new Element("element1", namespace1);
    nsStack.push(element1);
    assertTrue(nsStack.isInScope(namespace1));
    
    // Test when namespace is not in scope
    Namespace namespace2 = Namespace.getNamespace("ns2", "http://ns2");
    assertFalse(nsStack.isInScope(namespace2));
  }
  
}