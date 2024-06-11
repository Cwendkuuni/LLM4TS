import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NamespaceResolverTest {
  
  private NamespaceResolver namespaceResolver;
  
  @Before
  public void setUp() {
    namespaceResolver = new NamespaceResolver();
  }
  
  @Test
  public void testRegisterNamespace() {
    String prefix = "test";
    String namespaceURI = "http://www.example.com";
    namespaceResolver.registerNamespace(prefix, namespaceURI);
    
    String retrievedURI = namespaceResolver.getNamespaceURI(prefix);
    Assert.assertEquals(namespaceURI, retrievedURI);
  }
  
  @Test
  public void testGetNamespaceURI() {
    String prefix = "test";
    String namespaceURI = "http://www.example.com";
    
    String retrievedURI = namespaceResolver.getNamespaceURI(prefix);
    Assert.assertNull(retrievedURI);
    
    namespaceResolver.registerNamespace(prefix, namespaceURI);
    
    retrievedURI = namespaceResolver.getNamespaceURI(prefix);
    Assert.assertEquals(namespaceURI, retrievedURI);
  }
  
  @Test
  public void testGetPrefix() {
    String prefix = "test";
    String namespaceURI = "http://www.example.com";
    
    String retrievedPrefix = namespaceResolver.getPrefix(namespaceURI);
    Assert.assertNull(retrievedPrefix);
    
    namespaceResolver.registerNamespace(prefix, namespaceURI);
    
    retrievedPrefix = namespaceResolver.getPrefix(namespaceURI);
    Assert.assertEquals(prefix, retrievedPrefix);
  }
  
  @Test
  public void testIsSealed() {
    boolean isSealed = namespaceResolver.isSealed();
    Assert.assertFalse(isSealed);
    
    namespaceResolver.seal();
    
    isSealed = namespaceResolver.isSealed();
    Assert.assertTrue(isSealed);
  }
  
  @Test
  public void testClone() {
    NamespaceResolver clone = (NamespaceResolver) namespaceResolver.clone();
    Assert.assertNotSame(namespaceResolver, clone);
    Assert.assertEquals(namespaceResolver.isSealed(), clone.isSealed());
  }
}