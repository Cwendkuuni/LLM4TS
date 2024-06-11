import org.apache.commons.jxpath.NamespaceResolver;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class NamespaceResolverTest {

 private NamespaceResolver resolver;
 private NodePointer pointer;

 @BeforeEach
 public void setup() {
 resolver = new NamespaceResolver();
 pointer = new NodePointer("", new HashMap<>(), new HashMap<>());
 resolver.registerNamespace("xmlns:x", "http://example.com/x");
 resolver.setNamespaceContextPointer(pointer);
 }

 @Test
 public void testRegisterNamespace() {
 String prefix = "x";
 String uri = "http://example.com/x";
 resolver.registerNamespace(prefix, uri);
 Assertions.assertEquals(uri, resolver.getNamespaceURI(prefix));
 }

 @Test
 public void testGetNamespaceURIWithRegisteredPrefix() {
 String prefix = "x";
 String uri = "http://example.com/x";
 Assertions.assertEquals(uri, resolver.getNamespaceURI(prefix));
 }

 @Test
 public void testGetNamespaceURIWithoutRegisteredPrefix() {
 Assertions.assertNull(resolver.getNamespaceURI("y"));
 }

 @Test
 public void testGetNamespaceURIWithRegisteredPrefixAndContextNode() {
 pointer.setNamespaceURI("xmlns:x", "http://example.com/x");
 Assertions.assertEquals(uri, resolver.getNamespaceURI(prefix));
 }

 @Test
 public void testGetNamespaceURIWithoutContextNode() {
 resolver.setNamespaceContextPointer(null);
 Assertions.assertNull(resolver.getNamespaceURI(prefix));
 }

 @Test
 public void testGetNamespaceURIWithParentResolver() {
 NamespaceResolver parentResolver = new NamespaceResolver();
 parentResolver.registerNamespace("xmlns:y", "http://example.com/y");
 resolver = new NamespaceResolver(parentResolver);
 Assertions.assertEquals("http://example.com/y", resolver.getNamespaceURI("y"));
 }

 @Test
 public void testGetPrefix() {
 String uri = "http://example.com/x";
 String prefix = "x";
 resolver.registerNamespace(prefix, uri);
 Assertions.assertEquals(prefix, resolver.getPrefix(uri));
 }

 @Test
 public void testGetPrefixWithRegisteredPrefixAndContextNode() {
 pointer.setNamespaceURI("xmlns:x", "http://example.com/x");
 Assertions.assertEquals(prefix, resolver.getPrefix(uri));
 }

 @Test
 public void testGetPrefixWithRegisteredPrefixButDifferentCase() {
 String uri = "http://example.com/X";
 String prefix = "x";
 resolver.registerNamespace(prefix, uri);
 Assertions.assertEquals(prefix, resolver.getPrefix(uri));
 }

 @Test
 public void testGetPrefixWithoutRegisteredPrefix() {
 Assertions.assertNull(resolver.getPrefix("http://example.com/y"));
 }

 @Test
 public void testGetPrefixWithParentResolver() {
 NamespaceResolver parentResolver = new NamespaceResolver();
 parentResolver.registerNamespace("xmlns:y", "http://example.com/y");
 parentResolver.registerNamespace("xmlns:x", "http://example.com/x");
 resolver = new NamespaceResolver(parentResolver);
 Assertions.assertEquals("x", resolver.getPrefix("{http://example.com/x}"));
 }

 @Test
 public void testSealNamespaceResolver() {
 resolver.seal();
 try {
 resolver.registerNamespace("x", "http://example.com/x");
 Assertions.fail("Should not be able to register namespaces on a sealed NamespaceResolver");
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testClone() {
 NamespaceResolver clone = resolver.clone();
 clone.registerNamespace("y", "http://example.com/y");
 Assertions.assertNotEquals(resolver, clone);
 Assertions.assertEquals(uri, clone.getNamespaceURI(prefix));
 Assertions.assertEquals(prefix, clone.getPrefix(uri));
 }

 @Test
 public void testCloneWithParentResolver() {
 NamespaceResolver parentResolver = new NamespaceResolver();
 parentResolver.registerNamespace("xmlns:y", "http://example.com/y");
 NamespaceResolver clone = resolver.clone();
 NamespaceResolver clonedParent = (NamespaceResolver) clone.getParent();
 Assertions.assertEquals(parentResolver, clonedParent);
 }
}