import org.apache.commons.jxpath.ri.NamespacesResolver;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class NamespaceResolverTest {

 private NamespacesResolver resolver;

 @Before
 public void setUp() {
 resolver = new NamespacesResolver();
 }

 @Test
 public void testEmptyResolver() {
 // Test getting a prefix for an undefined namespace
 assertNull(resolver.getNamespaceURI("foo"));
 // Test getting a namespace URI for an undefined prefix
 assertNull(resolver.getPrefix("http://example.com"));
 }

 @Test
 public void testRegisteredNamespace() {
 // Register a prefix
 resolver.registerNamespace("x", "http://example.com/foo");
 // Get the namespace URI for the prefix
 assertEquals("http://example.com/foo", resolver.getNamespaceURI("x"));
 // Get the prefix for the namespace URI
 assertEquals("x", resolver.getPrefix("http://example.com/foo"));
 }

 @Test
 public void testRegisteredNamespaceWithPointer() {
 // Register a pointer
 NodePointer pointer = new NodePointer("root");
 resolver.setNamespaceContextPointer(pointer);
 resolver.registerNamespace("x", "http://example.com/foo");
 // Get the namespace URI for the prefix
 assertEquals("http://example.com/foo", resolver.getNamespaceURI("x"));
 // Get the prefix for the namespace URI
 assertEquals("x", resolver.getPrefix("http://example.com/foo"));
 // Get the namespace URI for the prefix from the pointer
 assertEquals("http://example.com/foo", pointer.getNamespaceURI("x"));
 }

 @Test
 public void testRegisteredNamespaceWithParent() {
 // Register a parent resolver
 NamespacesResolver parent = new NamespacesResolver();
 parent.registerNamespace("x", "http://example.com/foo");
 // Register a child resolver with the parent
 NamespacesResolver child = new NamespacesResolver(parent);
 // Register a prefix
 child.registerNamespace("y", "http://example.com/bar");
 // Get the namespace URI for the prefix
 assertEquals("http://example.com/bar", child.getNamespaceURI("y"));
 // Get the prefix for the namespace URI
 assertEquals("y", child.getPrefix("http://example.com/bar"));
 // Get the namespace URI for the prefix from the parent
 assertSame(parent, child.getParent());
 assertNull(parent.getNamespaceURI("y"));
 // Get the prefix for the namespace URI from the parent
 assertNull(parent.getPrefix("http://example.com/bar"));
 }

 @Test(expected = IllegalStateException.class)
 public void testSealedResolver() {
 // Seal the resolver
 resolver.seal();
 // Try to register a new namespace
 resolver.registerNamespace("x", "http://example.com/foo");
 }

 @Test
 public void testNamespaceIterator() {
 // Register some namespaces
 resolver.registerNamespace("x", "http://example.com/foo");
 resolver.registerNamespace("y", "http://example.com/bar");
 // Create a test node pointer with some namespaces
 NodePointer pointer = new NodePointer("root");
 pointer.addNamespace("xml", "http://www.w3.org/XML/1998/namespace");
 pointer.addNamespace("x", "http://example.com/foo");
 pointer.addNamespace("y", "http://example.com/bar");
 // Set the pointer as the context for the resolver
 resolver.setNamespaceContextPointer(pointer);
 // Get the iterator
 NodeIterator ni = resolver.namespaceIterator();
 // Check the order of the namespaces
 for (int i = 1; ni.hasNext(); i++) {
 NodePointer nsPointer = ni.getNodePointer();
 assertEquals("xml", nsPointer.getName().getName());
 assertEquals("http://www.w3.org/XML/1998/namespace", nsPointer.getNamespaceURI());
 assertEquals(i, ni.getPosition());
 }
 }
}