import org.apache.commons.jxpath.NamespaceResolver;
import org.apache.commons.jxpath.Pointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

 private NamespaceResolver resolver;
 private NamespaceResolver parentResolver;

 @Before
 public void setUp() {
 resolver = new NamespaceResolver();
 parentResolver = new NamespaceResolver(null);
 }

 /**
 * Test constructor.
 */
 @Test
 public void testConstructor() {
 NamespaceResolver namespaceResolver = new NamespaceResolver();
 assertNull(namespaceResolver.getParent());
 }

 /**
 * Test constructor with parent.
 */
 @Test
 public void testConstructorWithParent() {
 NamespaceResolver namespaceResolver = new NamespaceResolver(parentResolver);
 assertEquals(parentResolver, namespaceResolver.getParent());
 }

 /**
 * Test registerNamespace method.
 */
 @Test
 public void testRegisterNamespace() {
 resolver.registerNamespace("x", "http://example.com/x");
 assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
 }

 /**
 * Test getNamespaceURI method with undefined prefix.
 */
 @Test
 public void testGetNamespaceURIWithUndefinedPrefix() {
 assertNull(resolver.getNamespaceURI("x"));
 }

 /**
 * Test getNamespaceURI method with defined prefix.
 */
 @Test
 public void testGetNamespaceURIWithDefinedPrefix() {
 resolver.registerNamespace("x", "http://example.com/x");
 assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
 }

 /**
 * Test getNamespaceURI method with pointer.
 */
 @Test
 public void testGetNamespaceURIWithPointer() {
 Pointer pointer = new Pointer();
 resolver.setNamespaceContextPointer(pointer);
 String prefix = "x";
 String uri = "http://example.com/x";
 pointer.setNamespaceURI(prefix, uri);
 assertEquals(uri, resolver.getNamespaceURI(prefix));
 }

 /**
 * Test getNamespaceURI method with parent pointer.
 */
 @Test
 public void testGetNamespaceURIWithParentPointer() {
 Pointer parentPointer = new Pointer();
 Pointer pointer = new Pointer();
 parentResolver.setNamespaceContextPointer(parentPointer);
 resolver.setNamespaceContextPointer(pointer);
 String prefix = "x";
 String uri = "http://example.com/x";
 parentPointer.setNamespaceURI(prefix, uri);
 assertEquals(uri, resolver.getNamespaceURI(prefix));
 }

 /**
 * Test setNamespaceContextPointer method.
 */
 @Test
 public void testSetNamespaceContextPointer() {
 Pointer pointer = new Pointer();
 resolver.setNamespaceContextPointer(pointer);
 assertSame(pointer, resolver.getNamespaceContextPointer());
 }

 /**
 * Test getNamespaceContextPointer method.
 */
 @Test
 public void testGetNamespaceContextPointer() {
 Pointer pointer = new Pointer();
 resolver.setNamespaceContextPointer(pointer);
 assertSame(pointer, resolver.getNamespaceContextPointer());
 }

 /**
 * Test getNamespaceContextPointer method with null pointer.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testGetNamespaceContextPointerWithNullPointer() {
 resolver.setNamespaceContextPointer(null);
 }

 /**
 * Test getNamespaceContextPointer method with parent.
 */
 @Test
 public void testGetNamespaceContextPointerWithParent() {
 Pointer parentPointer = new Pointer();
 Pointer pointer = new Pointer();
 parentResolver.setNamespaceContextPointer(parentPointer);
 resolver.setNamespaceContextPointer(pointer);
 assertSame(parentPointer, resolver.getNamespaceContextPointer());
 }

 /**
 * Test getPrefix method with undefined namespace URI.
 */
 @Test
 public void testGetPrefixWithUndefinedNamespaceURI() {
 assertNull(resolver.getPrefix("http://example.com/x"));
 }

 /**
 * Test getPrefix method with defined namespace URI.
 */
 @Test
 public void testGetPrefixWithDefinedNamespaceURI() {
 resolver.registerNamespace("x", "http://example.com/x");
 assertEquals("x", resolver.getPrefix("http://example.com/x"));
 }

 /**
 * Test getPrefix method with pointer.
 */
 @Test
 public void testGetPrefixWithPointer() {
 Pointer pointer = new Pointer();
 resolver.setNamespaceContextPointer(pointer);
 String prefix = "x";
 String uri = "http://example.com/x";
 pointer.setNamespaceURI(prefix, uri);
 assertEquals(prefix, resolver.getPrefix(uri));
 }

 /**
 * Test getPrefix method with parent pointer.
 */
 @Test
 public void testGetPrefixWithParentPointer() {
 Pointer parentPointer = new Pointer();
 Pointer pointer = new Pointer();
 parentResolver.setNamespaceContextPointer(parentPointer);
 resolver.setNamespaceContextPointer(pointer);
 String prefix = "x";
 String uri = "http://example.com/x";
 parentPointer.setNamespaceURI(prefix, uri);
 assertEquals(prefix, resolver.getPrefix(uri));
 }

 /**
 * Test isSealed method with unsealed resolver.
 */
 @Test
 public void testIsSealedWithUnsealedResolver() {
 assertFalse(resolver.isSealed());
 }

 /**
 * Test isSealed method with sealed resolver.
 */
 @Test
 public void testIsSealedWithSealedResolver() {
 resolver.seal();
 assertTrue(resolver.isSealed());
 }

 /**
 * Test isSealed method with sealed parent resolver.
 */
 @Test
 public void testIsSealedWithSealedParentResolver() {
 parentResolver.seal();
 resolver.seal();
 assertTrue(resolver.isSealed());
 }

 /**
 * Test clone method.
 */
 @Test
 public void testClone() {
 NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
 assertFalse(clonedResolver == resolver);
 assertFalse(clonedResolver.isSealed());
 }

}