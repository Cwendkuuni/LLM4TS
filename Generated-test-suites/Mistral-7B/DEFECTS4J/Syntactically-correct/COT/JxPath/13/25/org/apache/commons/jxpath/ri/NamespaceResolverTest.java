import org.apache.commons.jxpath.NamespaceResolver;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

 private NamespaceResolver namespaceresolver;
 private NamespaceResolver parentNamespaceResolver;
 private NodePointer pointer;

 @Before
 public void setUp() {
 namespaceresolver = new NamespaceResolver();
 parentNamespaceResolver = new NamespaceResolver(namespaceresolver);
 pointer = new NodePointer();
 }

 /**
 * Test method for {@link NamespaceResolver#NamespaceResolver()}.
 */
 @Test
 public void testNamespaceResolverConstructor() {
 NamespaceResolver instance = new NamespaceResolver();
 assertNull(instance.getParent());
 }

 /**
 * Test method for {@link NamespaceResolver#NamespaceResolver(NamespaceResolver)}.
 */
 @Test
 public void testNamespaceResolverConstructorWithParent() {
 NamespaceResolver instance = new NamespaceResolver(parentNamespaceResolver);
 assertEquals(parentNamespaceResolver, instance.getParent());
 }

 /**
 * Test method for {@link NamespaceResolver#registerNamespace(String, String)}.
 */
 @Test(expected = IllegalStateException.class)
 public void testRegisterNamespaceOnSealedNamespaceResolver() {
 namespaceresolver.registerNamespace("x", "http://example.com/x");
 namespaceresolver.seal();
 namespaceresolver.registerNamespace("x", "http://example.com/x");
 }

 /**
 * Test method for {@link NamespaceResolver#registerNamespace(String, String)}.
 */
 @Test
 public void testRegisterNamespace() {
 namespaceresolver.registerNamespace("x", "http://example.com/x");
 assertEquals("x", namespaceresolver.getPrefix("http://example.com/x"));
 }

 /**
 * Test method for {@link NamespaceResolver#registerNamespace(String, String)}.
 */
 @Test
 public void testRegisterNamespaceWithParent() {
 parentNamespaceResolver.registerNamespace("y", "http://example.com/y");
 namespaceresolver.registerNamespace("x", "http://example.com/x");
 assertEquals("x", namespaceresolver.getPrefix("http://example.com/x"));
 assertEquals("y", namespaceresolver.getPrefix("http://example.com/y"));
 }

 /**
 * Test method for {@link NamespaceResolver#getNamespaceURI(String)}.
 */
 @Test
 public void testGetNamespaceURI() {
 namespaceresolver.registerNamespace("x", "http://example.com/x");
 assertEquals("http://example.com/x", namespaceresolver.getNamespaceURI("x"));
 }

 /**
 * Test method for {@link NamespaceResolver#getNamespaceURI(String)}.
 */
 @Test
 public void testGetNamespaceURIWithParent() {
 parentNamespaceResolver.registerNamespace("y", "http://example.com/y");
 namespaceresolver.registerNamespace("x", "http://example.com/x");
 assertEquals("http://example.com/x", namespaceresolver.getNamespaceURI("x"));
 assertEquals("http://example.com/y", namespaceresolver.getNamespaceURI("y"));
 }

 /**
 * Test method for {@link NamespaceResolver#getPrefix(String)}.
 */
 @Test
 public void testGetPrefix() {
 namespaceresolver.registerNamespace("x", "http://example.com/x");
 assertEquals("x", namespaceresolver.getPrefix("http://example.com/x"));
 }

 /**
 * Test method for {@link NamespaceResolver#getPrefix(String)}.
 */
 @Test
 public void testGetPrefixWithParent() {
 parentNamespaceResolver.registerNamespace("y", "http://example.com/y");
 namespaceresolver.registerNamespace("x", "http://example.com/x");
 assertEquals("x", namespaceresolver.getPrefix("http://example.com/x"));
 assertEquals("y", namespaceresolver.getPrefix("http://example.com/y"));
 }

 /**
 * Test method for {@link NamespaceResolver#isSealed()}.
 */
 @Test
 public void testIsSealed() {
 NamespaceResolver instance = new NamespaceResolver();
 assertFalse(instance.isSealed());
 instance.seal();
 assertTrue(instance.isSealed());
 }

 /**
 * Test method for {@link NamespaceResolver#seal()}.
 */
 @Test
 public void testSeal() {
 NamespaceResolver instance = new NamespaceResolver();
 instance.registerNamespace("x", "http://example.com/x");
 instance.seal();
 try {
 instance.registerNamespace("x", "http://example.com/x");
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 /**
 * Test method for {@link NamespaceResolver#clone()}.
 */
 @Test
 public void testClone() {
 NamespaceResolver original = new NamespaceResolver();
 original.registerNamespace("x", "http://example.com/x");
 NamespaceResolver clone = (NamespaceResolver) original.clone();
 assertEquals(original.getNamespaces(), clone.getNamespaces());
 }

 /**
 * Test method for {@link NamespaceResolver#setNamespaceContextPointer(NodePointer)}.
 */
 @Test
 public void testSetNamespaceContextPointer() {
 namespaceresolver.setNamespaceContextPointer(pointer);
 String prefix = namespaceresolver.getPrefix("http://example.com/x");
 assertNotNull(prefix);
 }

 /**
 * Test method for {@link NamespaceResolver#getNamespaceContextPointer()}.
 */
 @Test
 public void testGetNamespaceContextPointer() {
 assertNull(namespaceresolver.getNamespaceContextPointer());
 namespaceresolver.setNamespaceContextPointer(pointer);
 Pointer result = namespaceresolver.getNamespaceContextPointer();
 assertEquals(pointer, result);
 }

 /**
 * Test method for {@link NamespaceResolver#getNamespaceContextPointer()}.
 */
 @Test
 public void testGetNamespaceContextPointerWithParent() {
 parentNamespaceResolver.setNamespaceContextPointer(pointer);
 namespaceresolver.setNamespaceContextPointer(pointer);
 Pointer result = namespaceresolver.getNamespaceContextPointer();
 assertEquals(pointer, result);
 }

 /**
 * Test method for {@link NamespaceResolver#getNamespaceURI(String)}.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testGetNamespaceURIWithNullPrefix() {
 namespaceresolver.getNamespaceURI(null);
 }

 /**
 * Test method for {@link NamespaceResolver#getPrefix(String)}.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testGetPrefixWithNullPrefix() {
 namespaceresolver.getPrefix(null);
 }

 /**
 * Test method for {@link NamespaceResolver#getPrefix(String)}.
 */
 @Test
 public void testGetPrefixWithEmptyPrefix() {
 namespaceresolver.registerNamespace("x", "http://example.com/x");
 assertEquals("", namespaceresolver.getPrefix(""));
 }

 /**
 * Test method for {@link NamespaceResolver#getPrefix(String)}.
 */
 @Test
 public void testGetPrefixWithUnknownPrefix() {
 namespaceresolver.registerNamespace("x", "http://example.com/x");
 assertNull(namespaceresolver.getPrefix("unknown"));
 }

 /**
 * Test method for {@link NamespaceResolver#getPrefix(String)}.
 */
 @Test
 public void testGetPrefixWithParentPrefix() {
 parentNamespaceResolver.registerNamespace("y", "http://example.com/y");
 namespaceresolver.registerNamespace("x", "http://example.com/x");
 assertEquals("x", namespaceresolver.getPrefix("x"));
 assertEquals("y", namespaceresolver.getPrefix("y"));
 }

 /**
 * Test method for {@link NamespaceResolver#getPrefix(String)}.
 */
 @Test
 public void testGetPrefixWithParentPrefixAndLocalName() {
 parentNamespaceResolver.registerNamespace("y", "http://example.com/y");
 namespaceresolver.registerNamespace("x", "http://example.com/x");
 assertEquals("x:localName", namespaceresolver.getPrefix("x:localName"));
 assertEquals("y", namespaceresolver.getPrefix("y"));
 }

 /**
 * Test method for {@link NamespaceResolver#getPrefix(String)}.
 */
 @Test
 public void testGetPrefixWithNamespaceURI() {
 parentNamespaceResolver.registerNamespace("y", "http://example.com/y");
 namespaceresolver.registerNamespace("x", "http://example.com/x");
 assertEquals("x", namespaceresolver.getPrefix("http://example.com/x"));
 assertEquals("y", namespaceresolver.getPrefix("http://example.com/y"));
 }

 /**
 * Test method for {@link NamespaceResolver#getPrefix(String)}.
 */
 @Test
 public void testGetPrefixWithNamespaceURIAndLocalName() {
 parentNamespaceResolver.registerNamespace("y", "http://example.com/y");
 namespaceresolver.registerNamespace("x", "http://example.com/x");
 assertEquals("x:localName", namespaceresolver.getPrefix("http://example.com/x:localName"));
 assertEquals("y", namespaceresolver.getPrefix("http://example.com/y"));
 }

}