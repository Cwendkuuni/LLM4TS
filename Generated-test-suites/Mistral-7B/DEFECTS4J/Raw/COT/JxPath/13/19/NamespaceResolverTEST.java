import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class NamespaceResolverTest {

 private NamespaceResolver resolver;
 private NodePointer pointer;

 @Before
 public void setUp() {
 resolver = new NamespaceResolver();
 pointer = new NodePointer();
 }

 // Test constructor
 @Test
 public void testConstructor() {
 NamespaceResolver resolver = new NamespaceResolver();
 assertNull(resolver.getParent());
 assertNotNull(resolver.getNamespaceMap());
 }

 // Test registerNamespace method
 @Test
 public void testRegisterNamespace() {
 resolver.registerNamespace("test", "http://test.com");
 assertEquals("test", resolver.getNamespaceURI("test"));
 assertNull(resolver.getNamespaceURI("unknown"));
 }

 // Test setNamespaceContextPointer method
 @Test
 public void testSetNamespaceContextPointer() {
 NamespaceResolver resolver = new NamespaceResolver();
 NodePointer pointer = new NodePointer();
 resolver.setNamespaceContextPointer(pointer);
 assertEquals(pointer, resolver.getNamespaceContextPointer());
 }

 // Test getNamespaceURI method with registered prefix
 @Test
 public void testGetNamespaceURIWithRegisteredPrefix() {
 resolver.registerNamespace("test", "http://test.com");
 assertEquals("http://test.com", resolver.getNamespaceURI("test"));
 }

 // Test getNamespaceURI method with unregistered prefix
 @Test(expected = NullPointerException.class)
 public void testGetNamespaceURIWithUnregisteredPrefix() {
 assertEquals(null, resolver.getNamespaceURI("unknown"));
 }

 // Test getNamespaceURI method with pointer
 @Test
 public void testGetNamespaceURIWithPointer() {
 String uri = "http://test.com";
 pointer.setNamespaceURI(uri);
 assertEquals(uri, resolver.getNamespaceURI(null));
 }

 // Test getNamespaceURI method with parent
 @Test
 public void testGetNamespaceURIWithParent() {
 NamespaceResolver parentResolver = new NamespaceResolver();
 parentResolver.registerNamespace("test", "http://test.com");
 resolver = new NamespaceResolver(parentResolver);
 assertEquals(uri, resolver.getNamespaceURI("test"));
 }

 // Test getPrefix method with registered namespace URI
 @Test
 public void testGetPrefixWithRegisteredNamespaceURI() {
 resolver.registerNamespace("test", "http://test.com");
 assertEquals("test", resolver.getPrefix("http://test.com"));
 }

 // Test getPrefix method with unregistered namespace URI
 @Test
 public void testGetPrefixWithUnregisteredNamespaceURI() {
 assertNull(resolver.getPrefix("http://unknown.com"));
 }

 // Test getPrefix method with parent
 @Test
 public void testGetPrefixWithParent() {
 NamespaceResolver parentResolver = new NamespaceResolver();
 parentResolver.registerNamespace("test", "http://test.com");
 parentResolver.registerNamespace("other", "http://other.com");
 resolver = new NamespaceResolver(parentResolver);
 assertEquals("other", resolver.getPrefix("http://other.com"));
 }

 // Test isSealed method
 @Test
 public void testIsSealed() {
 assertFalse(resolver.isSealed());
 }

 // Test seal method
 @Test
 public void testSeal() {
 resolver.seal();
 assertTrue(resolver.isSealed());
 }

 // Test clone method
 @Test
 public void testClone() {
 NamespaceResolver original = resolver;
 NamespaceResolver clone = (NamespaceResolver) resolver.clone();
 assertNotSame(original, clone);
 }

 // Test exception when trying to register namespace on sealed NamespaceResolver
 @Test(expected = IllegalStateException.class)
 public void testExceptionWhenRegisteringNamespaceOnSealedNamespaceResolver() {
 resolver.seal();
 resolver.registerNamespace("test", "http://test.com");
 }

}