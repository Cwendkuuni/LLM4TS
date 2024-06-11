import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.commons.jxpath.Namespaces;
import org.apache.commons.jxpath.NodeContext;
import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class NamespaceResolverTest extends TestCase {

 private NamespaceResolver resolver;
 private NodePointer pointer;

 @Before
 public void setUp() {
 resolver = new NamespaceResolver();
 pointer = new NodePointer(Namespaces. getStandardModel(), "root", null);
 resolver.setNamespaceContextPointer(pointer);
 }

 // Test method for constructor NamespaceResolver()
 public void testConstructor() {
 NamespaceResolver nsResolver = new NamespaceResolver();
 assertNull(nsResolver.getParent());
 }

 // Test method for constructor NamespaceResolver(NamespaceResolver parent)
 public void testConstructorWithParent() {
 NamespaceResolver parentResolver = new NamespaceResolver();
 NamespaceResolver nsResolver = new NamespaceResolver(parentResolver);
 assertEquals(parentResolver, nsResolver.getParent());
 }

 // Test method for void registerNamespace(String prefix, String namespaceURI)
 public void testRegisterNamespace() {
 resolver.registerNamespace("x", "http://example.com/x");
 assertEquals("x", resolver.getPrefix("http://example.com/x"));
 }

 // Test method for void registerNamespace(String prefix, String namespaceURI) when prefix is empty
 public void testRegisterNamespaceWithEmptyPrefix() {
 resolver.registerNamespace("", "http://example.com/x");
 assertNull(resolver.getPrefix("http://example.com/x"));
 }

 // Test method for void registerNamespace(String prefix, String namespaceURI) when namespaceURI is null
 public void testRegisterNamespaceWithNullNamespaceURI() {
 try {
 resolver.registerNamespace("x", null);
 fail("Should not allow null namespaceURI");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 // Test method for void registerNamespace(String prefix, String namespaceURI) when namespaceURI is empty
 public void testRegisterNamespaceWithEmptyNamespaceURI() {
 try {
 resolver.registerNamespace("x", "");
 fail("Should not allow empty namespaceURI");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 // Test method for void registerNamespace(String prefix, String namespaceURI) when prefix is null
 public void testRegisterNamespaceWithNullPrefix() {
 try {
 resolver.registerNamespace(null, "http://example.com/x");
 fail("Should not allow null prefix");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 // Test method for void registerNamespace(String prefix, String namespaceURI) when prefix is empty and namespaceURI is empty
 public void testRegisterNamespaceWithEmptyPrefixAndEmptyNamespaceURI() {
 try {
 resolver.registerNamespace("", "");
 fail("Should not allow empty prefix and empty namespaceURI");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 // Test method for void setNamespaceContextPointer(NodePointer pointer)
 public void testSetNamespaceContextPointer() {
 String namespaceURI = "http://example.com/x";
 NodeContext nodeContext = new NodeContext();
 nodeContext.setNode(new NodePointer(nodeContext, "root", null));
 nodeContext.declareNamespace("x", namespaceURI);
 NodePointer nodePointer = nodeContext.getPointer("x:child");
 resolver.setNamespaceContextPointer(nodePointer);
 assertEquals(namespaceURI, resolver.getNamespaceURI("x"));
 }

 // Test method for String getNamespaceURI(String prefix)
 public void testGetNamespaceURI() {
 String namespaceURI = "http://example.com/x";
 resolver.registerNamespace("x", namespaceURI);
 assertEquals(namespaceURI, resolver.getNamespaceURI("x"));
 }

 // Test method for String getNamespaceURI(String prefix) when prefix is null
 public void testGetNamespaceURIWithNullPrefix() {
 assertNull(resolver.getNamespaceURI(null));
 }

 // Test method for String getNamespaceURI(String prefix) when prefix is empty
 public void testGetNamespaceURIWithEmptyPrefix() {
 assertNull(resolver.getNamespaceURI(""));
 }

 // Test method for String getNamespaceURI(String prefix) when prefix is not registered
 public void testGetNamespaceURIWithUnregisteredPrefix() {
 assertNull(resolver.getNamespaceURI("y"));
 }

 // Test method for String getPrefix(String namespaceURI)
 public void testGetPrefix() {
 String namespaceURI = "http://example.com/x";
 resolver.registerNamespace("x", namespaceURI);
 assertEquals("x", resolver.getPrefix(namespaceURI));
 }

 // Test method for String getPrefix(String namespaceURI) when namespaceURI is null
 public void testGetPrefixWithNullNamespaceURI() {
 assertNull(resolver.getPrefix(null));
 }

 // Test method for String getPrefix(String namespaceURI) when namespaceURI is empty
 public void testGetPrefixWithEmptyNamespaceURI() {
 assertNull(resolver.getPrefix(""));
 }

 // Test method for String getPrefix(String namespaceURI) when namespaceURI is not registered
 public void testGetPrefixWithUnregisteredNamespaceURI() {
 assertNull(resolver.getPrefix("http://example.com/y"));
 }

 // Test method for boolean isSealed()
 public void testIsSealed() {
 assertFalse(resolver.isSealed());
 }

 // Test method for void seal()
 public void testSeal() {
 resolver.seal();
 assertTrue(resolver.isSealed());
 }

 // Test method for Object clone()
 public void testClone() {
 NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
 assertNotSame(resolver, clonedResolver);
 assertFalse(clonedResolver.isSealed());
 }
}