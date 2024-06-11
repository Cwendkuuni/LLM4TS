import org.apache.commons.jxpath.NamespaceResolver;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

public class NamespaceResolverTest {

 private NamespaceResolver resolver;
 private NamespaceResolver parentResolver;

 @Test
 public void testConstructors() {
 resolver = new NamespaceResolver();
 parentResolver = new NamespaceResolver(null);
 Assert.assertNull(resolver.getParent());
 Assert.assertNull(parentResolver.getParent());
 }

 @Test
 public void testRegisterNamespace() {
 resolver = new NamespaceResolver();
 resolver.registerNamespace("x", "http://example.com/x");
 assertNamespaceMapEquals("x", "http://example.com/x", resolver);
 }

 @Test(expected = IllegalStateException.class)
 public void testRegisterNamespaceOnSealedResolver() {
 resolver = new NamespaceResolver();
 resolver.registerNamespace("x", "http://example.com/x");
 resolver.seal();
 resolver.registerNamespace("y", "http://example.com/y");
 }

 @Test
 public void testRegisterNamespaceOnParentResolver() {
 parentResolver = new NamespaceResolver();
 parentResolver.registerNamespace("x", "http://example.com/x");
 resolver = new NamespaceResolver(parentResolver);
 assertNamespaceMapEquals("x", "http://example.com/x", resolver);
 }

 @Test
 public void testGetNamespaceURI() {
 String uri = "http://example.com/";
 String prefix = "x";
 resolver = new NamespaceResolver();
 resolver.registerNamespace(prefix, uri);
 Assert.assertEquals(uri, resolver.getNamespaceURI(prefix));
 }

 @Test
 public void testGetNamespaceURIForNonExistentPrefix() {
 String uri = "http://example.com/";
 resolver = new NamespaceResolver();
 Assert.assertNull(resolver.getNamespaceURI(prefix));
 }

 @Test
 public void testGetNamespaceURIForExistingPrefixOnParent() {
 String uri = "http://example.com/";
 String prefix = "x";
 parentResolver = new NamespaceResolver();
 parentResolver.registerNamespace(prefix, uri);
 resolver = new NamespaceResolver(parentResolver);
 Assert.assertEquals(uri, resolver.getNamespaceURI(prefix));
 }

 @Test
 public void testGetNamespaceURIForExistingPrefix() {
 String uri = "http://example.com/";
 String prefix = "x";
 resolver = new NamespaceResolver();
 resolver.registerNamespace(prefix, uri);
 Assert.assertEquals(uri, resolver.getNamespaceURI(prefix));
 }

 @Test
 public void testGetNamespaceURIForNonExistentPrefixOnParent() {
 String uri = "http://example.com/";
 String prefix = "y";
 parentResolver = new NamespaceResolver();
 parentResolver.registerNamespace("x", uri);
 resolver = new NamespaceResolver(parentResolver);
 Assert.assertNull(resolver.getNamespaceURI(prefix));
 }

 @Test
 public void testGetPrefix() {
 String uri = "http://example.com/";
 String prefix = "x";
 resolver = new NamespaceResolver();
 resolver.registerNamespace(prefix, uri);
 Assert.assertEquals(prefix, resolver.getPrefix(uri));
 }

 @Test
 public void testGetPrefixForNonExistentNamespaceURI() {
 String uri = "http://example.com/";
 resolver = new NamespaceResolver();
 Assert.assertNull(resolver.getPrefix(uri));
 }

 @Test
 public void testGetPrefixForExistingNamespaceURIOnParent() {
 String uri = "http://example.com/";
 String prefix = "x";
 parentResolver = new NamespaceResolver();
 parentResolver.registerNamespace(prefix, uri);
 resolver = new NamespaceResolver(parentResolver);
 Assert.assertEquals(prefix, resolver.getPrefix(uri));
 }

 @Test
 public void testSetNamespaceContextPointer() {
 NodePointer pointer = new NodePointer();
 resolver = new NamespaceResolver();
 resolver.setNamespaceContextPointer(pointer);
 Assert.assertEquals(pointer, resolver.getNamespaceContextPointer());
 }

 @Test
 public void testGetNamespaceContextPointer() {
 NodePointer pointer = new NodePointer();
 resolver = new NamespaceResolver();
 resolver.setNamespaceContextPointer(pointer);
 Assert.assertEquals(pointer, resolver.getNamespaceContextPointer());
 }

 @Test
 public void testGetNamespaceContextPointerWhenNull() {
 resolver = new NamespaceResolver();
 Assert.assertNull(resolver.getNamespaceContextPointer());
 }

 @Test
 public void testGetNamespaceContextPointerWhenParentIsNull() {
 NodePointer pointer = new NodePointer();resolver = new NamespaceResolver(null); resolver.setNamespaceContextPointer(pointer); Assert.assertEquals(pointer, resolver.getNamespaceContextPointer()); }

 @Test
 public void testSeal() {
 resolver = new NamespaceResolver();
 resolver.registerNamespace("x", "http://example.com/x");
 resolver.seal();
 resolver.registerNamespace("y", "http://example.com/y"); Assert.assertFalse(resolver.isSealed()); }

 @Test
 public void testSealParent() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("x", "http://example.com/x");
 NamespaceResolver parentResolver = new NamespaceResolver(resolver); parentResolver.seal(); Assert.assertTrue(resolver.isSealed()); }

 @Test
 public void testClone() {
 NamespaceResolver original = new NamespaceResolver();
 original.registerNamespace("x", "http://example.com/x"); NamespaceResolver clone = (NamespaceResolver) original.clone(); Assert.assertNotSame(original, clone); Assert.assertEquals(original.getNamespaceMap(), clone.getNamespaceMap()); }

 private void assertNamespaceMapEquals(String prefix, String uri, NamespaceResolver resolver) {
 Map<String, String> map = resolver.getNamespaceMap();
 Assert.assertEquals(1, map.size());
 Assert.assertEquals(prefix, map.keySet().iterator().next());
 Assert.assertEquals(uri, map.values().iterator().next());
 }

}