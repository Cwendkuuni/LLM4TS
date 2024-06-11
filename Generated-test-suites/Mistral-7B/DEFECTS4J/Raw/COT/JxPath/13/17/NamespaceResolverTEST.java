import org.apache.commons.jxpath.NamespaceResolver;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
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
 NamespaceResolver resolver = new NamespaceResolver();
 assertNull(resolver.getParent());
 }

 /**
 * Test constructor with parent.
 */
 @Test
 public void testConstructorWithParent() {
 NamespaceResolver resolver = new NamespaceResolver(parentResolver);
 assertEquals(parentResolver, resolver.getParent());
 }

 /**
 * Test registerNamespace method.
 */
 @Test
 public void testRegisterNamespace() {
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 assertEquals("http://www.w3.org/2001/XMLSchema", resolver.getNamespaceURI("xsd"));
 }

 /**
 * Test registerNamespace method with sealed parent.
 */
 @Test(expected = IllegalStateException.class)
 public void testRegisterNamespaceWithSealedParent() {
 parentResolver.seal();
 resolver = new NamespaceResolver(parentResolver);
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 }

 /**
 * Test getNamespaceURI method with undefined prefix.
 */
 @Test
 public void testGetNamespaceURIWithUndefinedPrefix() {
 assertNull(resolver.getNamespaceURI("xsd"));
 }

 /**
 * Test getNamespaceURI method with defined prefix.
 */
 @Test
 public void testGetNamespaceURIWithDefinedPrefix() {
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 assertEquals("http://www.w3.org/2001/XMLSchema", resolver.getNamespaceURI("xsd"));
 }

 /**
 * Test getNamespaceURI method with defined prefix on parent.
 */
 @Test
 public void testGetNamespaceURIWithDefinedPrefixOnParent() {
 parentResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 resolver = new NamespaceResolver(parentResolver);
 assertEquals("http://www.w3.org/2001/XMLSchema", resolver.getNamespaceURI("xsd"));
 }

 /**
 * Test setNamespaceContextPointer method.
 */
 @Test
 public void testSetNamespaceContextPointer() {
 NodePointer pointer = new NodePointer("", null);
 resolver.setNamespaceContextPointer(pointer);
 assertEquals(pointer, resolver.getNamespaceContextPointer());
 }

 /**
 * Test getNamespaceContextPointer method when not set.
 */
 @Test
 public void testGetNamespaceContextPointerWhenNotSet() {
 assertNull(resolver.getNamespaceContextPointer());
 }

 /**
 * Test getNamespaceContextPointer method when set.
 */
 @Test
 public void testGetNamespaceContextPointerWhenSet() {
 NodePointer pointer = new NodePointer("", null);
 resolver.setNamespaceContextPointer(pointer);
 assertEquals(pointer, resolver.getNamespaceContextPointer());
 }

 /**
 * Test getNamespaceContextPointer method with parent set.
 */
 @Test
 public void testGetNamespaceContextPointerWithParentSet() {
 NodePointer parentPointer = new NodePointer("", null);
 resolver = new NamespaceResolver(parentResolver);
 NodePointer pointer = new NodePointer("", null);
 resolver.setNamespaceContextPointer(pointer);
 assertEquals(pointer, resolver.getNamespaceContextPointer());
 assertEquals(parentPointer, resolver.getParent().getNamespaceContextPointer());
 }

 /**
 * Test getPrefix method with undefined namespaceURI.
 */
 @Test
 public void testGetPrefixWithUndefinedNamespaceURI() {
 assertNull(resolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
 }

 /**
 * Test getPrefix method with defined namespaceURI.
 */
 @Test
 public void testGetPrefixWithDefinedNamespaceURI() {
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 assertEquals("xsd", resolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
 }

 /**
 * Test getPrefix method with defined namespaceURI on parent.
 */
 @Test
 public void testGetPrefixWithDefinedNamespaceURIOnParent() {
 parentResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 resolver = new NamespaceResolver(parentResolver);
 assertEquals("xsd", resolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
 }

 /**
 * Test isSealed method when not sealed.
 */
 @Test
 public void testIsSealedWhenNotSealed() {
 assertFalse(resolver.isSealed());
 }

 /**
 * Test isSealed method when sealed.
 */
 @Test
 public void testIsSealedWhenSealed() {
 resolver.seal();
 assertTrue(resolver.isSealed());
 }

 /**
 * Test clone method.
 */
 @Test
 public void testClone() {
 NamespaceResolver cloned = (NamespaceResolver) resolver.clone();
 assertNotSame(resolver, cloned);
 assertEquals(resolver, cloned.getParent());
 }

 /**
 * Test clone method with sealed parent.
 */
 @Test
 public void testCloneWithSealedParent() {
 parentResolver.seal();
 NamespaceResolver cloned = (NamespaceResolver) resolver.clone();
 assertNotSame(resolver, cloned);
 assertEquals(parentResolver, cloned.getParent());
 }
}