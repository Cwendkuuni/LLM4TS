import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {
  
  private NamespaceResolver resolver;
  
  @Before
  public void setUp() {
    resolver = new NamespaceResolver();
  }
  
  @Test
  public void testRegisterNamespace() {
    resolver.registerNamespace("prefix1", "http://example.com/ns1");
    resolver.registerNamespace("prefix2", "http://example.com/ns2");
    
    String uri1 = resolver.getNamespaceURI("prefix1");
    String uri2 = resolver.getNamespaceURI("prefix2");
    
    assertEquals("http://example.com/ns1", uri1);
    assertEquals("http://example.com/ns2", uri2);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testRegisterNamespaceSealed() {
    resolver.seal();
    resolver.registerNamespace("prefix", "http://example.com/ns");
  }
  
  @Test
  public void testGetNamespaceURIWithParentResolver() {
    NamespaceResolver parentResolver = new NamespaceResolver();
    parentResolver.registerNamespace("prefix", "http://example.com/ns");
    resolver = new NamespaceResolver(parentResolver);
    
    String uri = resolver.getNamespaceURI("prefix");
    
    assertEquals("http://example.com/ns", uri);
  }
  
  @Test
  public void testGetNamespaceURIWithContextPointer() {
    MockNodePointer pointer = new MockNodePointer();
    pointer.setNamespaceURI("prefix", "http://example.com/ns");
    resolver.setNamespaceContextPointer(pointer);
    
    String uri = resolver.getNamespaceURI("prefix");
    
    assertEquals("http://example.com/ns", uri);
  }
  
  @Test
  public void testGetPrefix() {
    resolver.registerNamespace("prefix1", "http://example.com/ns1");
    resolver.registerNamespace("prefix2", "http://example.com/ns2");
    
    String prefix1 = resolver.getPrefix("http://example.com/ns1");
    String prefix2 = resolver.getPrefix("http://example.com/ns2");
    
    assertEquals("prefix1", prefix1);
    assertEquals("prefix2", prefix2);
  }
  
  @Test
  public void testGetPrefixWithParentResolver() {
    NamespaceResolver parentResolver = new NamespaceResolver();
    parentResolver.registerNamespace("prefix", "http://example.com/ns");
    resolver = new NamespaceResolver(parentResolver);
    
    String prefix = resolver.getPrefix("http://example.com/ns");
    
    assertEquals("prefix", prefix);
  }
  
  private static class MockNodePointer extends NodePointer {
    // Mock NodePointer implementation for testing purposes
  }
}