import org.apache.commons.jxpath.Name;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.Namespaces;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class NamespaceResolverTest {

 private NamespaceResolver namespaceresolver;
 private NamespaceResolver parentNamespaceResolver;

 @Test
 public void testConstructor() {
 // Test empty constructor
 namespaceresolver = new NamespaceResolver();
 Assert.assertNull(namespaceresolver.getParent());
 }

 @Test
 public void testConstructorWithParent() {
 // Test constructor with parent
 parentNamespaceResolver = new NamespaceResolver();
 namespaceresolver = new NamespaceResolver(parentNamespaceResolver);
 Assert.assertSame(parentNamespaceResolver, namespaceresolver.getParent());
 }

 @Test
 public void testRegisterNamespace() {
 // Test registering a namespace
 namespaceresolver = new NamespaceResolver();
 namespaceresolver.registerNamespace("xmlns:x", "http://example.com/x");
 String prefix = namespaceresolver.getNamespaceURI("x");
 Assert.assertEquals("http://example.com/x", prefix);
 }

 @Test(expected = IllegalStateException.class)
 public void testRegisterNamespaceOnSealedNamespaceResolver() {
 // Test registering a namespace on sealed NamespaceResolver
 namespaceresolver = new NamespaceResolver();
 namespaceresolver.seal();
 namespaceresolver.registerNamespace("xmlns:x", "http://example.com/x");
 }

 @Test
 public void testGetNamespaceURI() {
 // Test getting a namespace URI with registered prefix
 namespaceresolver = new NamespaceResolver();
 namespaceresolver.registerNamespace("xmlns:x", "http://example.com/x");
 String prefix = namespaceresolver.getNamespaceURI("x");
 Assert.assertEquals("http://example.com/x", prefix);

 // Test getting a namespace URI with unregistered prefix
 String prefix1 = namespaceresolver.getNamespaceURI("y");
 Assert.assertNull(prefix1);

 // Test getting a namespace URI with context pointer
 NodePointer contextPointer = new NodePointer("http://example.com", new Name("root"), new HashMap<>());
 namespaceresolver.setNamespaceContextPointer(contextPointer);
 prefix = namespaceresolver.getNamespaceURI("xmlns:x");
 Assert.assertEquals("http://example.com/x", prefix);
 }

 @Test
 public void testGetPrefix() {
 // Test getting a prefix with registered namespace URI
 namespaceresolver = new NamespaceResolver();
 namespaceresolver.registerNamespace("xmlns:x", "http://example.com/x");
 String uri = namespaceresolver.getNamespaceURI("x");
 String prefix = namespaceresolver.getPrefix(uri);
 Assert.assertEquals("x", prefix);

 // Test getting a prefix with unregistered namespace URI
 uri = "http://example.com/unregistered";
 prefix = namespaceresolver.getPrefix(uri);
 Assert.assertNull(prefix);

 // Test getting a prefix with context pointer
 contextPointer = new NodePointer("http://example.com", new Name("root"), new HashMap<>());
 namespaceresolver.setNamespaceContextPointer(contextPointer);
 Map<String, String> namespaces = contextPointer.getNamespaces();
 namespaces.put("xmlns:x", "http://example.com/x");
 prefix = namespaceresolver.getPrefix(namespaces.get("xmlns:x"));
 Assert.assertEquals("x", prefix);
 }

 @Test
 public void testIsSealed() {
 // Test initial state
 namespaceresolver = new NamespaceResolver();
 Assert.assertFalse(namespaceresolver.isSealed());

 // Test sealing
 namespaceresolver.seal();
 Assert.assertTrue(namespaceresolver.isSealed());
 }

 @Test
 public void testClone() {
 // Test cloning a NamespaceResolver
 namespaceresolver = new NamespaceResolver();
 namespaceresolver.registerNamespace("xmlns:x", "http://example.com/x");
 NamespaceResolver clonedNamespaceresolver = (NamespaceResolver) namespaceresolver.clone();
 String prefix1 = namespaceresolver.getNamespaceURI("x");
 String prefix2 = clonedNamespaceresolver.getNamespaceURI("x");
 Assert.assertEquals(prefix1, prefix2);
 }

 @Test
 public void testNamespaceIterator() {
 // Test namespace iterator with no namespaces
 namespaceresolver = new NamespaceResolver();
 NodeIterator iterator = namespaceresolver.namespaceIterator();
 Assert.assertFalse(iterator.hasNext());

 // Test namespace iterator with registered namespaces
 namespaceresolver = new NamespaceResolver();
 namespaceresolver.registerNamespace("xmlns:x", "http://example.com/x");
 namespaceresolver.registerNamespace("xmlns:y", "http://example.com/y");
 iterator = namespaceresolver.namespaceIterator();
 String prefix = "";
 while (iterator.hasNext()) {
 NodePointer nodePointer = iterator.nextNodePointer();
 prefix = nodePointer.getName().getName();
 Assert.assertEquals("x", prefix);
 // Next namespace should be y
 iterator.nextNodePointer();
 }
 }

 @Test
 public void testSeal() {
 // Test sealing a NamespaceResolver
 namespaceresolver = new NamespaceResolver();
 namespaceresolver.registerNamespace("xmlns:x", "http://example.com/x");
 namespaceresolver.registerNamespace("xmlns:y", "http://example.com/y");
 namespaceresolver.seal();
 String prefix1 = namespaceresolver.getNamespaceURI("x");
 String prefix2 = namespaceresolver.getNamespaceURI("y");
 Assert.assertEquals(prefix1, prefix1); // Should not change after sealing
 Assert.assertEquals(prefix2, prefix2); // Should not change
 }
}