import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

  @Test
  public void testRegisterNamespace() {
    NamespaceResolver resolver = new NamespaceResolver();
    resolver.registerNamespace("prefix", "http://example.com");
    assertEquals("http://example.com", resolver.getNamespaceURI("prefix"));
  }

  @Test
  public void testSetNamespaceContextPointer() {
    NamespaceResolver resolver = new NamespaceResolver();
    NodePointer pointer = new NodePointer();
    resolver.setNamespaceContextPointer(pointer);
    assertEquals(pointer, resolver.getNamespaceContextPointer());
  }

  @Test
  public void testGetNamespaceURI() {
    NamespaceResolver resolver = new NamespaceResolver();
    resolver.registerNamespace("prefix", "http://example.com");
    assertEquals("http://example.com", resolver.getNamespaceURI("prefix"));
  }

  @Test
  public void testGetPrefix() {
    NamespaceResolver resolver = new NamespaceResolver();
    resolver.registerNamespace("prefix", "http://example.com");
    assertEquals("prefix", resolver.getPrefix("http://example.com"));
  }

  @Test
  public void testIsSealed() {
    NamespaceResolver resolver = new NamespaceResolver();
    assertFalse(resolver.isSealed());
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
    NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
    assertNotNull(clonedResolver);
  }
  
  // Additional test cases for edge cases and exceptions
  
  @Test(expected = IllegalStateException.class)
  public void testRegisterNamespaceSealed() {
    NamespaceResolver resolver = new NamespaceResolver();
    resolver.seal();
    resolver.registerNamespace("prefix", "http://example.com");
  }
  
  @Test
  public void testGetNamespaceURINotFound() {
    NamespaceResolver resolver = new NamespaceResolver();
    assertNull(resolver.getNamespaceURI("prefix"));
  }

  @Test
  public void testGetPrefixNotFound() {
    NamespaceResolver resolver = new NamespaceResolver();
    assertNull(resolver.getPrefix("http://example.com"));
  }

}