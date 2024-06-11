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
    resolver.registerNamespace("test", "http://www.test.com");
    assertEquals("http://www.test.com", resolver.getNamespaceURI("test"));
  }
  
  @Test
  public void testSetNamespaceContextPointer() {
    NodePointer pointer = new NodePointer();
    resolver.setNamespaceContextPointer(pointer);
    assertEquals(pointer, resolver.getNamespaceContextPointer());
  }
  
  @Test
  public void testGetNamespaceURI() {
    resolver.registerNamespace("test", "http://www.test.com");
    assertEquals("http://www.test.com", resolver.getNamespaceURI("test"));
  }
  
  @Test
  public void testGetPrefix() {
    resolver.registerNamespace("test", "http://www.test.com");
    assertEquals("test", resolver.getPrefix("http://www.test.com"));
  }
  
  @Test
  public void testIsSealed() {
    assertFalse(resolver.isSealed());
  }
  
  @Test
  public void testSeal() {
    resolver.seal();
    assertTrue(resolver.isSealed());
  }
  
  @Test
  public void testClone() {
    NamespaceResolver cloneResolver = (NamespaceResolver) resolver.clone();
    assertEquals(cloneResolver, resolver);
  }
  
}