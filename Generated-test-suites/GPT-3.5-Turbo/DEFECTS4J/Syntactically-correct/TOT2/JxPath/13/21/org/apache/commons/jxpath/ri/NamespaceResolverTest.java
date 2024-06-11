import static org.junit.Assert.*;
import org.junit.Test;

public class NamespaceResolverTest {
  
  @Test
  public void testRegisterNamespace() {
    NamespaceResolver resolver = new NamespaceResolver();
    resolver.registerNamespace("prefix", "http://www.test.com");
    assertEquals("http://www.test.com", resolver.getNamespaceURI("prefix"));
  }
  
  @Test
  public void testGetPrefix() {
    NamespaceResolver resolver = new NamespaceResolver();
    resolver.registerNamespace("prefix", "http://www.test.com");
    assertEquals("prefix", resolver.getPrefix("http://www.test.com"));
  }
  
  @Test
  public void testGetNamespaceURINoMatch() {
    NamespaceResolver resolver = new NamespaceResolver();
    assertNull(resolver.getNamespaceURI("nonexistent"));
  }
  
  @Test
  public void testGetPrefixNoMatch() {
    NamespaceResolver resolver = new NamespaceResolver();
    assertNull(resolver.getPrefix("http://www.nonexistent.com"));
  }
  
  @Test
  public void testSeal() {
    NamespaceResolver resolver = new NamespaceResolver();
    resolver.seal();
    assertTrue(resolver.isSealed());
  }
  
  @Test
  public void testClone() {
    NamespaceResolver parent = new NamespaceResolver();
    parent.registerNamespace("prefix", "http://www.test.com");
    
    NamespaceResolver resolver = new NamespaceResolver(parent);
    resolver.seal();
    NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
    
    assertEquals("http://www.test.com", clonedResolver.getNamespaceURI("prefix"));
    assertTrue(clonedResolver.isSealed());
  }
}