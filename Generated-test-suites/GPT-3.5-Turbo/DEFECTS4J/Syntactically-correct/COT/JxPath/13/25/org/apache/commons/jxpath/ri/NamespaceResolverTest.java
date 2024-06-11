import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import org.junit.Assert;

public class NamespaceResolverTest {

  @Test
  public void testRegisterNamespace() {
    NamespaceResolver resolver = new NamespaceResolver();
    resolver.registerNamespace("prefix", "http://example.com");
    String uri = resolver.getNamespaceURI("prefix");
    Assert.assertEquals("http://example.com", uri);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testRegisterNamespaceWhenSealed() {
    NamespaceResolver resolver = new NamespaceResolver();
    resolver.seal();
    resolver.registerNamespace("prefix", "http://example.com");
  }
  
  @Test
  public void testGetNamespaceURI() {
    NamespaceResolver resolver = new NamespaceResolver();
    resolver.registerNamespace("prefix", "http://example.com");
    String uri = resolver.getNamespaceURI("prefix");
    Assert.assertEquals("http://example.com", uri);
  }
  
  @Test
  public void testGetNamespaceURINoPrefix() {
    NamespaceResolver resolver = new NamespaceResolver();
    String uri = resolver.getNamespaceURI("http://example.com");
    Assert.assertNull(uri);
  }
  
  @Test
  public void testGetNamespaceURIShouldReturnParent() {
    NamespaceResolver parentResolver = new NamespaceResolver();
    parentResolver.registerNamespace("prefix", "http://example.com");
    
    NamespaceResolver childResolver = new NamespaceResolver(parentResolver);
    String uri = childResolver.getNamespaceURI("prefix");
    Assert.assertEquals("http://example.com", uri);
  }
  
  @Test
  public void testGetPrefix() {
    NamespaceResolver resolver = new NamespaceResolver();
    resolver.registerNamespace("prefix", "http://example.com");
    String prefix = resolver.getPrefix("http://example.com");
    Assert.assertEquals("prefix", prefix);
  }
  
  @Test
  public void testGetPrefixNoURI() {
    NamespaceResolver resolver = new NamespaceResolver();
    String prefix = resolver.getPrefix("http://example.com");
    Assert.assertNull(prefix);
  }
  
  @Test
  public void testGetPrefixShouldReturnParent() {
    NamespaceResolver parentResolver = new NamespaceResolver();
    parentResolver.registerNamespace("prefix", "http://example.com");
    
    NamespaceResolver childResolver = new NamespaceResolver(parentResolver);
    String prefix = childResolver.getPrefix("http://example.com");
    Assert.assertEquals("prefix", prefix);
  }
  
  @Test
  public void testIsSealed() {
    NamespaceResolver resolver = new NamespaceResolver();
    resolver.seal();
    boolean sealed = resolver.isSealed();
    Assert.assertTrue(sealed);
  }
  
  @Test
  public void testClone() {
    NamespaceResolver resolver = new NamespaceResolver();
    NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
    boolean sealed = clonedResolver.isSealed();
    Assert.assertFalse(sealed);
  }
  
}