import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class NamespaceResolverTest {
  
  // Test registerNamespace method
  @Test
  public void testRegisterNamespace() {
    NamespaceResolver resolver = new NamespaceResolver();
    resolver.registerNamespace("prefix", "http://www.example.com");
    assertEquals("http://www.example.com", resolver.getNamespaceURI("prefix"));
  }
  
  // Test setNamespaceContextPointer and getNamespaceContextPointer methods
  @Test
  public void testNamespaceContextPointer() {
    NamespaceResolver parentResolver = new NamespaceResolver();
    NamespaceResolver childResolver = new NamespaceResolver(parentResolver);
    NodePointer pointer = new NodePointer();
    childResolver.setNamespaceContextPointer(pointer);
    assertEquals(pointer, childResolver.getNamespaceContextPointer());
  }
  
  // Test getNamespaceURI method when prefix is explicitly registered
  @Test
  public void testGetNamespaceURIWithRegisteredPrefix() {
    NamespaceResolver resolver = new NamespaceResolver();
    resolver.registerNamespace("prefix", "http://www.example.com");
    assertEquals("http://www.example.com", resolver.getNamespaceURI("prefix"));
  }
  
  // Test getNamespaceURI method when prefix is not explicitly registered but defined in context node
  @Test
  public void testGetNamespaceURIWithContextNode() {
    NodePointer pointer = new NodePointer();
    pointer.setNamespaceURI("prefix", "http://www.example.com");
    NamespaceResolver parentResolver = new NamespaceResolver();
    NamespaceResolver childResolver = new NamespaceResolver(parentResolver);
    childResolver.setNamespaceContextPointer(pointer);
    assertEquals("http://www.example.com", childResolver.getNamespaceURI("prefix"));
  }
  
  // Test getNamespaceURI method when prefix is not explicitly registered and not defined in context node
  @Test
  public void testGetNamespaceURIWithoutPrefix() {
    NamespaceResolver resolver = new NamespaceResolver();
    assertNull(resolver.getNamespaceURI("prefix"));
  }
  
  // Test getPrefix method when prefix is explicitly registered
  @Test
  public void testGetPrefixWithRegisteredPrefix() {
    NamespaceResolver resolver = new NamespaceResolver();
    resolver.registerNamespace("prefix", "http://www.example.com");
    assertEquals("prefix", resolver.getPrefix("http://www.example.com"));
  }
  
  // Test getPrefix method when prefix is not explicitly registered but defined in context node
  @Test
  public void testGetPrefixWithContextNode() {
    NodePointer pointer = new NodePointer();
    pointer.setNamespaceURI("prefix", "http://www.example.com");
    NamespaceResolver parentResolver = new NamespaceResolver();
    NamespaceResolver childResolver = new NamespaceResolver(parentResolver);
    childResolver.setNamespaceContextPointer(pointer);
    assertEquals("prefix", childResolver.getPrefix("http://www.example.com"));
  }
  
  // Test getPrefix method when prefix is not explicitly registered and not defined in context node
  @Test
  public void testGetPrefixWithoutPrefix() {
    NamespaceResolver resolver = new NamespaceResolver();
    assertNull(resolver.getPrefix("http://www.example.com"));
  }
  
  // Test isSealed method when resolver is not sealed
  @Test
  public void testIsSealedFalse() {
    NamespaceResolver resolver = new NamespaceResolver();
    assertFalse(resolver.isSealed());
  }
  
  // Test isSealed method when resolver is sealed
  @Test
  public void testIsSealedTrue() {
    NamespaceResolver resolver = new NamespaceResolver();
    resolver.seal();
    assertTrue(resolver.isSealed());
  }
  
  // Test clone method
  @Test
  public void testClone() {
    NamespaceResolver resolver = new NamespaceResolver();
    NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
    assertFalse(clonedResolver.isSealed());
  }
  
}