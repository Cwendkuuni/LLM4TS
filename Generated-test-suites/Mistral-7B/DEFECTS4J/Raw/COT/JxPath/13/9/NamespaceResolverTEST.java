import org.apache.commons.jxpath.NamespaceResolver;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.RIConstants;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

 private NamespaceResolver resolver;
 private NodePointer pointer;

 @Before
 public void setUp() {
 resolver = new NamespaceResolver();
 pointer = new NodePointer(RIConstants.EMPTY_NAMESPACE, RIConstants.EMPTY_NAME, null, -1, null);
 resolver.setNamespaceContextPointer(pointer);
 }

 /**
 * Test constructor.
 */
 @Test
 public void testConstructor() {
 NamespaceResolver instance = new NamespaceResolver();
 assertNull(instance.getNamespaceURI("prefix"));
 assertNull(instance.getPrefix("uri"));
 }

 /**
 * Test constructor with parent.
 */
 @Test
 public void testConstructorWithParent() {
 NamespaceResolver parent = new NamespaceResolver();
 NamespaceResolver instance = new NamespaceResolver(parent);
 assertSame(parent, instance.getParent());
 }

 /**
 * Test registerNamespace() method.
 */
 @Test
 public void testRegisterNamespace() {
 resolver.registerNamespace("prefix", "uri");
 assertEquals("uri", resolver.getNamespaceURI("prefix"));
 assertEquals("prefix", resolver.getPrefix("uri"));
 }

 /**
 * Test registerNamespace() with sealed NamespaceResolver.
 */
 @Test(expected = IllegalStateException.class)
 public void testRegisterNamespaceWithSealedResolver() {
 resolver.seal();
 resolver.registerNamespace("prefix", "uri");
 }

 /**
 * Test getNamespaceURI() method.
 */
 @Test
 public void testGetNamespaceURI() {
 String uri = "http://example.com";
 pointer.setNamespaceURI(uri);
 assertEquals(uri, resolver.getNamespaceURI("prefix:localName"));
 }

 /**
 * Test getNamespaceURI() method with no matching prefix.
 */
 @Test
 public void testGetNamespaceURIWithNoMatchingPrefix() {
 assertEquals(null, resolver.getNamespaceURI("prefix"));
 }

 /**
 * Test getNamespaceURI() method with sealed NamespaceResolver.
 */
 @Test
 public void testGetNamespaceURIWithSealedResolver() {
 resolver.seal();
 assertEquals(null, resolver.getNamespaceURI("prefix"));
 }

 /**
 * Test getPrefix() method.
 */
 @Test
 public void testGetPrefix() {
 String uri = "http://example.com";
 resolver.registerNamespace("prefix", uri);
 assertEquals("prefix", resolver.getPrefix(uri));
 }

 /**
 * Test getPrefix() method with no matching URI.
 */
 @Test
 public void testGetPrefixWithNoMatchingURI() {
 assertEquals(null, resolver.getPrefix("uri"));
 }

 /**
 * Test getPrefix() method with sealed NamespaceResolver.
 */
 @Test
 public void testGetPrefixWithSealedResolver() {
 resolver.seal();
 assertEquals(null, resolver.getPrefix("uri"));
 }

 /**
 * Test setNamespaceContextPointer() method.
 */
 @Test
 public void testSetNamespaceContextPointer() {
 String uri = "http://example.com";
 pointer.setNamespaceURI(uri);
 pointer.setLocalName("localName");
 resolver.setNamespaceContextPointer(pointer);
 assertEquals(uri, resolver.getNamespaceURI("prefix:localName"));
 }

 /**
 * Test setNamespaceContextPointer() method with null pointer.
 */
 @Test(expected = NullPointerException.class)
 public void testSetNamespaceContextPointerWithNullPointer() {
 resolver.setNamespaceContextPointer(null);
 }

 /**
 * Test isSealed() method.
 */
 @Test
 public void testIsSealed() {
 assertFalse(resolver.isSealed());
 }

 /**
 * Test isSealed() method with sealed NamespaceResolver.
 */
 @Test
 public void testIsSealedWithSealedResolver() {
 resolver.seal();
 assertTrue(resolver.isSealed());
 }

 /**
 * Test clone() method.
 */
 @Test
 public void testClone() {
 NamespaceResolver original = new NamespaceResolver();
 original.registerNamespace("prefix", "uri");
 NamespaceResolver cloned = (NamespaceResolver) original.clone();
 assertEquals("uri", cloned.getNamespaceURI("prefix"));
 }

}