import org.apache.commons.jxpath.NamespaceResolver;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
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

 /**
 * Test constructor.
 */
 @Test
 public void testConstructor() {
 NamespaceResolver resolver = new NamespaceResolver();
 assertNull(resolver.getParent());
 assertNull(resolver.getNamespaceMap());
 assertNull(resolver.getReverseMap());
 assertNull(resolver.getPointer());
 assertFalse(resolver.isSealed());
 }

 /**
 * Test constructor with parent.
 */
 @Test
 public void testConstructorWithParent() {
 NamespaceResolver parentResolver = new NamespaceResolver();
 NamespaceResolver resolver = new NamespaceResolver(parentResolver);
 assertEquals(parentResolver, resolver.getParent());
 assertNull(resolver.getNamespaceMap());
 assertNull(resolver.getReverseMap());
 assertNull(resolver.getPointer());
 assertFalse(resolver.isSealed());
 }

 /**
 * Test registerNamespace method.
 */
 @Test
 public void testRegisterNamespace() {
 resolver.registerNamespace("x", "http://example.com/x");
 Map namespaceMap = resolver.getNamespaceMap();
 assertEquals("http://example.com/x", namespaceMap.get("x"));
 assertEquals(1, namespaceMap.size());
 }

 /**
 * Test registerNamespace method with duplicate prefix.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testRegisterNamespaceWithDuplicatePrefix() {
 resolver.registerNamespace("x", "http://example.com/x");
 resolver.registerNamespace("x", "http://example.com/y");
 }

 /**
 * Test getNamespaceURI method.
 */
 @Test
 public void testGetNamespaceURI() {
 resolver.registerNamespace("x", "http://example.com/x");
 assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
 assertEquals(null, resolver.getNamespaceURI("y"));
 }

 /**
 * Test getPrefix method.
 */
 @Test
 public void testGetPrefix() {
 resolver.registerNamespace("x", "http://example.com/x");
 assertEquals("x", resolver.getPrefix("http://example.com/x"));
 assertEquals(null, resolver.getPrefix("http://example.com/y"));
 }

 /**
 * Test isSealed method.
 */
 @Test
 public void testIsSealed() {
 assertFalse(resolver.isSealed());
 }

 /**
 * Test seal method.
 */
 @Test
 public void testSeal() {
 resolver.registerNamespace("x", "http://example.com/x");
 resolver.seal();
 assertTrue(resolver.isSealed());
 }

 /**
 * Test clone method.
 */
 @Test
 public void testClone() {
 NamespaceResolver originalResolver = new NamespaceResolver();
 NamespaceResolver clonedResolver = (NamespaceResolver) originalResolver.clone();
 assertNotSame(originalResolver, clonedResolver);
 assertNull(clonedResolver.getParent());
 assertNull(clonedResolver.getNamespaceMap());
 assertNull(clonedResolver.getReverseMap());
 assertNull(clonedResolver.getPointer());
 assertFalse(clonedResolver.isSealed());
 }

}