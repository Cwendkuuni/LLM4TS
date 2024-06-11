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
    resolver.registerNamespace("prefix1", "uri1");
    resolver.registerNamespace("prefix2", "uri2");
    
    assertEquals("uri1", resolver.getNamespaceURI("prefix1"));
    assertEquals("uri2", resolver.getNamespaceURI("prefix2"));
  }
  
  @Test(expected = IllegalStateException.class)
  public void testRegisterNamespaceSealed() {
    resolver.seal();
    resolver.registerNamespace("prefix3", "uri3");
  }
  
  @Test
  public void testGetNamespaceURI() {
    resolver.registerNamespace("prefix4", "uri4");
    resolver.registerNamespace("prefix5", "uri5");
    
    assertEquals("uri4", resolver.getNamespaceURI("prefix4"));
    assertEquals("uri5", resolver.getNamespaceURI("prefix5"));
    assertNull(resolver.getNamespaceURI("prefix6"));
  }
  
  @Test
  public void testGetPrefix() {
    resolver.registerNamespace("prefix7", "uri7");
    resolver.registerNamespace("prefix8", "uri8");
    
    assertEquals("prefix7", resolver.getPrefix("uri7"));
    assertEquals("prefix8", resolver.getPrefix("uri8"));
    assertNull(resolver.getPrefix("uri9"));
  }
  
  @Test
  public void testClone() {
    NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
    
    assertEquals(resolver.getNamespaceURI("prefix1"), clonedResolver.getNamespaceURI("prefix1"));
    assertEquals(resolver.getNamespaceURI("prefix2"), clonedResolver.getNamespaceURI("prefix2"));
    assertEquals(resolver.getNamespaceContextPointer(), clonedResolver.getNamespaceContextPointer());
    assertFalse(resolver.isSealed());
    assertFalse(clonedResolver.isSealed());
    
    resolver.seal();
    NamespaceResolver sealedClone = (NamespaceResolver) resolver.clone();
    
    assertTrue(sealedClone.isSealed());
  }
}