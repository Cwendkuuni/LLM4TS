import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class NamespaceResolverTest {
  
  @Test
  public void testRegisterNamespace() {
    NamespaceResolver resolver = new NamespaceResolver();
    resolver.registerNamespace("prefix1", "uri1");
    resolver.registerNamespace("prefix2", "uri2");
    assertEquals("uri1", resolver.getNamespaceURI("prefix1"));
    assertEquals("uri2", resolver.getNamespaceURI("prefix2"));
  }
  
  @Test(expected = IllegalStateException.class)
  public void testSealedRegisterNamespace() {
    NamespaceResolver resolver = new NamespaceResolver();
    resolver.seal();
    resolver.registerNamespace("prefix", "uri");
  }
  
  @Test
  public void testGetNamespaceURI() {
    NamespaceResolver resolver1 = new NamespaceResolver();
    assertNull(resolver1.getNamespaceURI("prefix"));
    
    NamespaceResolver resolver2 = new NamespaceResolver();
    resolver2.registerNamespace("prefix", "uri");
    assertEquals("uri", resolver2.getNamespaceURI("prefix"));
  }
  
  @Test
  public void testGetNamespaceURINotDefined() {
    NamespaceResolver resolver = new NamespaceResolver();
    assertNull(resolver.getNamespaceURI("prefix"));
  }
  
  @Test
  public void testGetPrefix() {
    NamespaceResolver resolver1 = new NamespaceResolver();
    assertNull(resolver1.getPrefix("uri"));
    
    NamespaceResolver resolver2 = new NamespaceResolver();
    resolver2.registerNamespace("prefix", "uri");
    assertEquals("prefix", resolver2.getPrefix("uri"));
  }
  
  @Test
  public void testGetPrefixNotDefined() {
    NamespaceResolver resolver = new NamespaceResolver();
    assertNull(resolver.getPrefix("uri"));
  }
  
  @Test
  public void testGetPrefixWithParentResolver() {
    NamespaceResolver parentResolver = new NamespaceResolver();
    parentResolver.registerNamespace("prefix", "uri");
    NamespaceResolver childResolver = new NamespaceResolver(parentResolver);
    assertEquals("prefix", childResolver.getPrefix("uri"));
  }
  
  @Test
  public void testClone() {
    NamespaceResolver resolver1 = new NamespaceResolver();
    resolver1.registerNamespace("prefix", "uri");
    NamespaceResolver resolver2 = (NamespaceResolver) resolver1.clone();
    assertEquals("uri", resolver2.getNamespaceURI("prefix"));
  }
  
  @Test
  public void testIsSealed() {
    NamespaceResolver resolver = new NamespaceResolver();
    assertFalse(resolver.isSealed());
    resolver.seal();
    assertTrue(resolver.isSealed());
  }
  
  @Test
  public void testSetNamespaceContextPointer() {
    NamespaceResolver resolver = new NamespaceResolver();
    NodePointer pointer = new NodePointer();
    resolver.setNamespaceContextPointer(pointer);
    assertEquals(pointer, resolver.getNamespaceContextPointer());
  }
  
  @Test
  public void testGetNamespaceContextPointer() {
    NamespaceResolver parentResolver = new NamespaceResolver();
    NamespaceResolver childResolver = new NamespaceResolver(parentResolver);
    NodePointer pointer = new NodePointer();
    childResolver.setNamespaceContextPointer(pointer);
    assertEquals(pointer, childResolver.getNamespaceContextPointer());
    assertNull(parentResolver.getNamespaceContextPointer());
  }
  
  @Test
  public void testGetPrefixWithNamespaceIterator() {
    NamespaceResolver resolver = new NamespaceResolver();
    NodePointer pointer = new NodePointer();
    NamespaceResolver pointerResolver = new NamespaceResolver();
    pointerResolver.registerNamespace("prefix", "uri");
    pointer.setNamespaceResolver(pointerResolver);
    resolver.setNamespaceContextPointer(pointer);
    assertEquals("prefix", resolver.getPrefix("uri"));
  }
  
  @Test
  public void testGetPrefixWithParentResolverBinding() {
    NamespaceResolver parentResolver = new NamespaceResolver();
    parentResolver.registerNamespace("prefix", "uri");
    NamespaceResolver childResolver = new NamespaceResolver(parentResolver);
    NodePointer pointer = new NodePointer();
    NamespaceResolver pointerResolver = new NamespaceResolver();
    pointerResolver.registerNamespace("prefix", "uri");
    pointer.setNamespaceResolver(pointerResolver);
    childResolver.setNamespaceContextPointer(pointer);
    assertEquals("prefix", childResolver.getPrefix("uri"));
  }
  
}