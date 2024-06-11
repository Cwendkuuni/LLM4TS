import org.apache.commons.jxpath.NamespaceResolver;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

 private NamespaceResolver resolver;
 private NodePointer pointer;

 @Before
 public void setup() {
 resolver = new NamespaceResolver();
 pointer = new NodePointer();
 }

 // Test constructor
 @Test
 public void testNamespaceResolverConstructor() {
 NamespaceResolver namespaceResolver = new NamespaceResolver();
 assertNull(namespaceResolver.getParent());
 }

 // Test constructor with parent
 @Test
 public void testNamespaceResolverConstructorWithParent() {
 NamespaceResolver namespaceResolver = new NamespaceResolver(null);
 NamespaceResolver parentResolver = new NamespaceResolver();
 namespaceResolver.setParent(parentResolver);
 assertEquals(parentResolver, namespaceResolver.getParent());
 }

 // Test registerNamespace
 @Test
 public void testRegisterNamespace() {
 resolver.registerNamespace("xxx", "http://example.com/xxx");
 String uri = resolver.getNamespaceURI("xxx");
 assertEquals("http://example.com/xxx", uri);
 }

 // Test registerNamespace with existing prefix
 @Test(expected = IllegalStateException.class)
 public void testRegisterNamespaceWithExistingPrefix() {
 resolver.registerNamespace("xxx", "http://example.com/xxx");
 resolver.registerNamespace("xxx", "http://example.com/yyy");
 }

 // Test getNamespaceURI
 @Test
 public void testGetNamespaceURI() {
 resolver.registerNamespace("xxx", "http://example.com/xxx");
 assertEquals("http://example.com/xxx", resolver.getNamespaceURI("xxx"));
 }

 // Test getNamespaceURI with null prefix
 @Test
 public void testGetNamespaceURIWithNullPrefix() {
 assertEquals(null, resolver.getNamespaceURI(null));
 }

 // Test getNamespaceURI with unknown prefix
 @Test
 public void testGetNamespaceURIWithUnknownPrefix() {
 assertEquals(null, resolver.getNamespaceURI("unknown"));
 }

 // Test getPrefix
 @Test
 public void testGetPrefix() {
 resolver.registerNamespace("xxx", "http://example.com/xxx");
 assertEquals("xxx", resolver.getPrefix("http://example.com/xxx"));
 }

 // Test getPrefix with null namespaceURI
 @Test
 public void testGetPrefixWithNullNamespaceURI() {
 assertEquals(null, resolver.getPrefix(null));
 }

 // Test getPrefix with unknown namespaceURI
 @Test
 public void testGetPrefixWithUnknownNamespaceURI() {
 assertEquals(null, resolver.getPrefix("http://example.com/unknown"));
 }

 // Test isSealed
 @Test
 public void testIsSealed() {
 assertFalse(resolver.isSealed());
 }

 // Test seal
 @Test
 public void testSeal() {
 resolver.seal();
 assertTrue(resolver.isSealed());
 }

 // Test clone
 @Test
 public void testClone() {
 NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
 assertNotSame(resolver, clonedResolver);
 assertNotNull(clonedResolver.getParent());
 assertNull(clonedResolver.getParent().getParent());
 }

 // Test setNamespaceContextPointer
 @Test
 public void testSetNamespaceContextPointer() {
 pointer.setNamespaceContextPointer(resolver);
 resolver.setNamespaceContextPointer(pointer);
 assertTrue(resolver.getNamespaceContextPointer() == pointer);
 }

 // Test getNamespaceContextPointer
 @Test
 public void testGetNamespaceContextPointer() {
 assertNull(resolver.getNamespaceContextPointer());
 pointer.setNamespaceContextPointer(resolver);
 assertEquals(pointer, resolver.getNamespaceContextPointer());
 }

}