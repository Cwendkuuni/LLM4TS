import static org.junit.Assert.*;
import org.junit.Test;

public class NamespaceResolverTest {
  
  @Test
  public void testConstructor() {
    NamespaceResolver resolver = new NamespaceResolver();
    assertNotNull(resolver);
  }
  
  @Test
  public void testRegisterNamespace() {
    NamespaceResolver resolver = new NamespaceResolver();
    resolver.registerNamespace("test", "http://example.com");
    assertEquals("http://example.com", resolver.getNamespaceURI("test"));
  }
  
  @Test
  public void testGetNamespaceURI() {
    NamespaceResolver resolver = new NamespaceResolver();
    resolver.registerNamespace("test", "http://example.com");
    assertEquals("http://example.com", resolver.getNamespaceURI("test"));
  }
  
  @Test
  public void testGetPrefix() {
    NamespaceResolver resolver = new NamespaceResolver();
    resolver.registerNamespace("test", "http://example.com");
    assertEquals("test", resolver.getPrefix("http://example.com"));
  }
  
  @Test
  public void testSetNamespaceContextPointer() {
    NamespaceResolver resolver = new NamespaceResolver();
    resolver.setNamespaceContextPointer(new NodePointer());
    assertNotNull(resolver.getNamespaceContextPointer());
  }
  
  @Test
  public void testSeal() {
    NamespaceResolver resolver = new NamespaceResolver();
    resolver.seal();
    assertTrue(resolver.isSealed());
  }
  
  @Test
  public void testClone() {
    NamespaceResolver resolver = new NamespaceResolver();
    resolver.registerNamespace("test", "http://example.com");
    NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
    assertNotNull(clonedResolver);
  }
  
  @Test
  public void testInvalidNamespace() {
    NamespaceResolver resolver = new NamespaceResolver();
    assertNull(resolver.getNamespaceURI("invalid"));
  }
  
  @Test(expected = IllegalStateException.class)
  public void testSealedRegisterNamespace() {
    NamespaceResolver resolver = new NamespaceResolver();
    resolver.seal();
    resolver.registerNamespace("test", "http://example.com");
  }
}