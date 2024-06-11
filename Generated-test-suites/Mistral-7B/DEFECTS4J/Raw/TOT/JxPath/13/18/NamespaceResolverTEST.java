import org.apache.commons.jxpath.Name;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.Namespaces;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NamespaceResolverTest {

 private NamespaceResolver resolver;

 @Test
 public void testRegisterNamespace() {
 resolver = new NamespaceResolver();
 resolver.registerNamespace("x", "http://example.com/x");
 assertEquals("x", resolver.getPrefix("http://example.com/x"));
 }

 @Test
 public void testRegisterNamespaceWithContext() {
 NamespaceResolver parentResolver = new NamespaceResolver();
 parentResolver.registerNamespace("d", "http://example.com/d");
 NamespaceResolver resolver = new NamespaceResolver(parentResolver);
 resolver.registerNamespace("x", "http://example.com/x");
 assertEquals("x", resolver.getPrefix("http://example.com/x"));
 assertEquals("d", resolver.getPrefix("http://example.com/d"));
 }

 @Test
 public void testGetNamespaceURI() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("x", "http://example.com/x");
 assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
 }

 @Test
 public void testGetNamespaceURIWithContext() {
 NamespaceResolver parentResolver = new NamespaceResolver();
 parentResolver.registerNamespace("d", "http://example.com/d");
 NamespaceResolver resolver = new NamespaceResolver(parentResolver);
 resolver.registerNamespace("x", "http://example.com/x");
 assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
 assertEquals("http://example.com/d", resolver.getNamespaceURI("d"));
 }

 @Test
 public void testGetNamespaceURIEmpty() {
 NamespaceResolver resolver = new NamespaceResolver();
 assertNull(resolver.getNamespaceURI("x"));
 }

 @Test
 public void testGetNamespaceURIWithContextEmpty() {
 NamespaceResolver parentResolver = new NamespaceResolver();
 assertNull(parentResolver.getNamespaceURI("x"));
 }

 @Test
 public void testGetPrefix() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("x", "http://example.com/x");
 assertEquals("x", resolver.getPrefix("http://example.com/x"));
 }

 @Test
 public void testGetPrefixWithContext() {
 NamespaceResolver parentResolver = new NamespaceResolver();
 parentResolver.registerNamespace("d", "http://example.com/d");
 NamespaceResolver resolver = new NamespaceResolver(parentResolver);
 resolver.registerNamespace("x", "http://example.com/x");
 assertEquals("x", resolver.getPrefix("http://example.com/x"));
 assertEquals("d", resolver.getPrefix("http://example.com/d"));
 }

 @Test
 public void testGetPrefixEmpty() {
 NamespaceResolver resolver = new NamespaceResolver();
 assertNull(resolver.getPrefix("http://example.com/x"));
 }

 @Test
 public void testGetPrefixWithContextEmpty() {
 NamespaceResolver parentResolver = new NamespaceResolver();
 assertNull(parentResolver.getPrefix("http://example.com/x"));
 }

 @Test
 public void testGetNamespaceContextPointer() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.setNamespaceContextPointer(new NodePointer(new Object(), Namespaces.EMPTY_NAMESPACE_URI, "root", 1, new Object[0], null));
 assertEquals("root", resolver.getNamespaceContextPointer().getLocalName());
 }

 @Test
 public void testGetNamespaceContextPointerWithContext() {
 NamespaceResolver parentResolver = new NamespaceResolver();
 parentResolver.setNamespaceContextPointer(new NodePointer(new Object(), Namespaces.EMPTY_NAMESPACE_URI, "parent", 1, new Object[0], null));
 NamespaceResolver resolver = new NamespaceResolver(parentResolver);
 resolver.setNamespaceContextPointer(new NodePointer(new Object(), Namespaces.EMPTY_NAMESPACE_URI, "child", 1, new Object[0], null));
 assertEquals("child", resolver.getNamespaceContextPointer().getLocalName());
 assertEquals("parent", resolver.getNamespaceContextPointer().getParent().getLocalName());
 }

 @Test
 public void testSeal() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("x", "http://example.com/x");
 resolver.seal();
 try {
 resolver.registerNamespace("y", "http://example.com/y");
 fail();
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testClone() {
 NamespaceResolver original = new NamespaceResolver();
 original.registerNamespace("x", "http://example.com/x");
 NamespaceResolver clone = (NamespaceResolver) original.clone();
 assertEquals("x", clone.getPrefix("http://example.com/x"));
 }

 @Test
 public void testCloneWithContext() {
 NamespaceResolver parentResolver = new NamespaceResolver();
 parentResolver.registerNamespace("d", "http://example.com/d");
 NamespaceResolver original = new NamespaceResolver(parentResolver);
 original.registerNamespace("x", "http://example.com/x");
 NamespaceResolver clone = (NamespaceResolver) original.clone();
 assertEquals("x", clone.getPrefix("http://example.com/x"));
 assertEquals("d", clone.getPrefix("http://example.com/d"));
 }

 @Test
 public void testSetNamespaceContextPointer() {
 NamespaceResolver resolver = new NamespaceResolver();
 NodePointer pointer = new NodePointer(new Object(), Namespaces.EMPTY_NAMESPACE_URI, "root", 1, new Object[0], null);
 resolver.setNamespaceContextPointer(pointer);
 assertEquals(pointer, resolver.getNamespaceContextPointer());
 }

 @Test
 public void testSetNamespaceContextPointerWithContext() {
 NamespaceResolver parentResolver = new NamespaceResolver();
 NodePointer pointer = new NodePointer(new Object(), Namespaces.EMPTY_NAMESPACE_URI, "parent", 1, new Object[0], null);
 parentResolver.setNamespaceContextPointer(pointer);
 NamespaceResolver resolver = new NamespaceResolver(parentResolver);
 NodePointer newPointer = new NodePointer(new Object(), Namespaces.EMPTY_NAMESPACE_URI, "child", 1, new Object[0], null);
 resolver.setNamespaceContextPointer(newPointer);
 assertEquals(newPointer, resolver.getNamespaceContextPointer());
 assertEquals(pointer, resolver.getNamespaceContextPointer().getParent());
 }
}