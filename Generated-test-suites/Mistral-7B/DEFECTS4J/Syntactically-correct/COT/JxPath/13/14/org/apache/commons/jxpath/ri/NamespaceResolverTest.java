import org.apache.commons.jxpath.NamespaceResolver;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

public class NamespaceResolverTest {

 private NamespaceResolver namespaceResolver;
 private NamespaceResolver nestedNamespaceResolver;

 @Test
 public void testConstructor() {
 NamespaceResolver namespaceResolver = new NamespaceResolver();
 Assert.assertNull(namespaceResolver.getParent());
 Assert.assertNull(namespaceResolver.getNamespaceMap());
 Assert.assertNull(namespaceResolver.getReverseMap());
 Assert.assertNull(namespaceResolver.getPointer());
 Assert.assertFalse(namespaceResolver.isSealed());
 }

 @Test
 public void testConstructorWithParent() {
 NamespaceResolver parentResolver = new NamespaceResolver();
 NamespaceResolver namespaceResolver = new NamespaceResolver(parentResolver);
 Assert.assertSame(parentResolver, namespaceResolver.getParent());
 Assert.assertNull(namespaceResolver.getNamespaceMap());
 Assert.assertNull(namespaceResolver.getReverseMap());
 Assert.assertNull(namespaceResolver.getPointer());
 Assert.assertFalse(namespaceResolver.isSealed());
 }

 @Test
 public void testRegisterNamespace() {
 namespaceResolver = new NamespaceResolver();
 namespaceResolver.registerNamespace("x", "http://example.com/x");
 Map<String, String> namespaceMap = namespaceResolver.getNamespaceMap();
 Assert.assertEquals("http://example.com/x", namespaceMap.get("x"));
 }

 @Test(expected = IllegalStateException.class)
 public void testRegisterNamespaceOnSealedResolver() {
 NamespaceResolver resolved = new NamespaceResolver();
 resolved.registerNamespace("x", "http://example.com/x");
 resolved.seal();
 resolved.registerNamespace("y", "http://example.com/y");
 }

 @Test
 public void testGetNamespaceURI() {
 namespaceResolver = new NamespaceResolver();
 namespaceResolver.registerNamespace("x", "http://example.com/x");
 Assert.assertEquals("http://example.com/x", namespaceResolver.getNamespaceURI("x"));
 }

 @Test
 public void testGetNamespaceURINotFound() {
 namespaceResolver = new NamespaceResolver();
 Assert.assertNull(namespaceResolver.getNamespaceURI("y"));
 }

 @Test
 public void testGetNamespaceURIWithPointer() {
 NodePointer pointer = new NodePointer(new Object(), null);
 namespaceResolver = new NamespaceResolver();
 namespaceResolver.setNamespaceContextPointer(pointer);
 namespaceResolver.registerNamespace("x", "http://example.com/x");
 Assert.assertEquals("http://example.com/x", namespaceResolver.getNamespaceURI("x"));
 }

 @Test
 public void testGetNamespaceURIWithParentPointer() {
 NodePointer parentPointer = new NodePointer(new Object(), null);
 NodePointer pointer = new NodePointer(new Object(), parentPointer);
 namespaceResolver = new NamespaceResolver();
 NamespaceResolver parentNamespaceResolver = new NamespaceResolver();
 parentNamespaceResolver.registerNamespace("x", "http://example.com/x");
 namespaceResolver.setNamespaceContextPointer(pointer);
 namespaceResolver.setParent(parentNamespaceResolver);
 Assert.assertEquals("http://example.com/x", namespaceResolver.getNamespaceURI("x"));
 }

 @Test
 public void testGetPrefix() {
 namespaceResolver = new NamespaceResolver();
 namespaceResolver.registerNamespace("x", "http://example.com/x");
 Assert.assertNull(namespaceResolver.getPrefix("http://example.com/y"));
 Assert.assertEquals("x", namespaceResolver.getPrefix("http://example.com/x"));
 }

 @Test
 public void testGetPrefixWithParent() {
 NamespaceResolver parentNamespaceResolver = new NamespaceResolver();
 parentNamespaceResolver.registerNamespace("x", "http://example.com/x");
 parentNamespaceResolver.registerNamespace("y", "http://example.com/y");
 namespaceResolver = new NamespaceResolver();
 namespaceResolver.setParent(parentNamespaceResolver);
 Assert.assertEquals("x", namespaceResolver.getPrefix("http://example.com/x"));
 Assert.assertEquals("y", namespaceResolver.getPrefix("http://example.com/y"));
 }

 @Test
 public void testIsSealed() {
 NamespaceResolver namespaceResolver = new NamespaceResolver();
 Assert.assertFalse(namespaceResolver.isSealed());
 namespaceResolver.seal();
 Assert.assertTrue(namespaceResolver.isSealed());
 }

 @Test
 public void testClone() {
 NamespaceResolver namespaceResolver = new NamespaceResolver();namespaceResolver.registerNamespace("x", "http://example.com/x");
 NamespaceResolver cloned = (NamespaceResolver) namespaceResolver.clone();
 Map<String, String> clonedNamespaceMap = cloned.getNamespaceMap();
 Map<String, String> originalNamespaceMap = namespaceResolver.getNamespaceMap();
 Assert.assertNotSame(clonedNamespaceMap, originalNamespaceMap);
 Assert.assertEquals("http://example.com/x", clonedNamespaceMap.get("x"));
 }

 @Test
 public void testSetNamespaceContextPointer() {
 namespaceResolver = new NamespaceResolver();
 NodePointer pointer = new NodePointer(new Object(), null);
 namespaceResolver.setNamespaceContextPointer(pointer);
 Assert.assertEquals(pointer, namespaceResolver.getNamespaceContextPointer());
 }

 @Test
 public void testSeal() {
 NamespaceResolver namespaceResolver = new NamespaceResolver();
 namespaceResolver.registerNamespace("x", "http://example.com/x");
 namespaceResolver.seal();
 }

 @Test(expected = IllegalStateException.class)
 public void testSetNamespaceContextPointerOnSealedResolver() {
 NamespaceResolver namespaceResolver = new NamespaceResolver();
 namespaceResolver.registerNamespace("x", "http://example.com/x");
 namespaceResolver.seal();
 namespaceResolver.setNamespaceContextPointer(new NodePointer(new Object(), null));
 }

 @Test
 public void testNestedNamespaceResolver() {
 nestedNamespaceResolver = new NamespaceResolver(namespaceResolver);
 nestedNamespaceResolver.registerNamespace("z", "http://example.com/z");
 Assert.assertSame(namespaceResolver, nestedNamespaceResolver.getParent());
 Assert.assertFalse(namespaceResolver.isSealed());
 Assert.assertFalse(nestedNamespaceResolver.isSealed());
 }

 @Test
 public void testGetNamespaceURIWithNestedResolver() {
 nestedNamespaceResolver.registerNamespace("w", "http://example.com/w");
 Assert.assertEquals("http://example.com/x", namespaceResolver.getNamespaceURI("x"));
 Assert.assertEquals("http://example.com/w", nestedNamespaceResolver.getNamespaceURI("w"));
 }

 @Test
 public void testGetPrefixWithNestedResolver() {
 nestedNamespaceResolver.registerNamespace("w", "http://example.com/w");
 Assert.assertNull(namespaceResolver.getPrefix("w"));
 Assert.assertEquals("w", nestedNamespaceResolver.getPrefix("w"));
 }

 @Test
 public void testIsSealedWithNestedResolver() {
 Assert.assertFalse(namespaceResolver.isSealed());
 Assert.assertFalse(nestedNamespaceResolver.isSealed());
 namespaceResolver.seal();
 Assert.assertFalse(namespaceResolver.isSealed());
 Assert.assertFalse(nestedNamespaceResolver.isSealed());
 }

}