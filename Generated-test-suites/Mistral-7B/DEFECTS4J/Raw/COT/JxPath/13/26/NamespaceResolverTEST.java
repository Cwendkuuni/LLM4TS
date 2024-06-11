import org.apache.commons.jxpath.NamespaceResolver;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

public class NamespaceResolverTest {

 private NamespaceResolver resolver;
 private NamespaceResolver parentResolver;
 private Pointer pointer;

 @Test
 public void testConstructor() {
 resolver = new NamespaceResolver();
 Assert.assertNull(resolver.getParent());
 }

 @Test
 public void testConstructorWithParent() {
 parentResolver = new NamespaceResolver();
 resolver = new NamespaceResolver(parentResolver);
 Assert.assertSame(parentResolver, resolver.getParent());
 }

 @Test
 public void testRegisterNamespace() {
 resolver = new NamespaceResolver();
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 String uri = resolver.getNamespaceURI("xsd");
 Assert.assertEquals("http://www.w3.org/2001/XMLSchema", uri);
 }

 @Test(expected = IllegalStateException.class)
 public void testRegisterNamespaceOnSealedResolver() {
 resolver = new NamespaceResolver();
 resolver.seal();
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 }

 @Test
 public void testGetNamespaceURI() {
 resolver = new NamespaceResolver();
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 String uri = resolver.getNamespaceURI("xsd");
 Assert.assertEquals("http://www.w3.org/2001/XMLSchema", uri);

 pointer = new NodePointer("", null);
 uri = resolver.getNamespaceURI("xsd");
 Assert.assertEquals("http://www.w3.org/2001/XMLSchema", uri);

 resolver.setNamespaceContextPointer(pointer);
 uri = resolver.getNamespaceURI("xsd");
 Assert.assertEquals("http://www.w3.org/2001/XMLSchema", uri);
 }

 @Test
 public void testGetNamespaceURIWithParent() {
 parentResolver = new NamespaceResolver();
 parentResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 resolver = new NamespaceResolver(parentResolver);
 resolver.registerNamespace("jxpath", "http://www.example.com/jxpath");
 String uri = resolver.getNamespaceURI("xsd");
 Assert.assertEquals("http://www.w3.org/2001/XMLSchema", uri);

 uri = resolver.getNamespaceURI("jxpath");
 Assert.assertEquals("http://www.example.com/jxpath", uri);

 uri = resolver.getNamespaceURI("xsd");
 Assert.assertEquals("http://www.w3.org/2001/XMLSchema", uri);
 }

 @Test
 public void testGetPrefix() {
 resolver = new NamespaceResolver();
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 String prefix = resolver.getPrefix("http://www.w3.org/2001/XMLSchema");
 Assert.assertEquals("xsd", prefix);

 pointer = new NodePointer("", null);
 prefix = resolver.getPrefix("http://www.w3.org/2001/XMLSchema");
 Assert.assertEquals("xsd", prefix);

 resolver.setNamespaceContextPointer(pointer);
 prefix = resolver.getPrefix("http://www.w3.org/2001/XMLSchema");
 Assert.assertEquals("xsd", prefix);
 }

 @Test
 public void testGetPrefixWithParent() {
 parentResolver = new NamespaceResolver();
 parentResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 parentResolver.registerNamespace("jxpath", "http://www.example.com/jxpath");
 resolver = new NamespaceResolver(parentResolver);
 resolver.registerNamespace("foo", "http://www.example.com/foo");
 String prefix = resolver.getPrefix("http://www.w3.org/2001/XMLSchema");
 Assert.assertEquals("xsd", prefix);

 prefix = resolver.getPrefix("http://www.example.com/foo");
 Assert.assertEquals("foo", prefix);

 prefix = resolver.getPrefix("http://www.w3.org/2001/XMLSchema");
 Assert.assertEquals("xsd", prefix);
 }

 @Test
 public void testIsSealed() {
 resolver = new NamespaceResolver();
 Assert.assertFalse(resolver.isSealed());

 resolver.seal();
 Assert.assertTrue(resolver.isSealed());
 }

 @Test
 public void testClone() {
 resolver = new NamespaceResolver();
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 resolver.seal();
 NamespaceResolver cloned = (NamespaceResolver) resolver.clone();
 Assert.assertFalse(cloned.isSealed());
 resolver.registerNamespace("jxpath", "http://www.example.com/jxpath");
 Assert.assertFalse(cloned.isSealed());
 Assert.assertEquals(resolver.getNamespaceMap(), cloned.getNamespaceMap());
 }

 @Test
 public void testGetNamespaceMap() {
 resolver = new NamespaceResolver();
 Map<String, String> map = new HashMap();
 map.put("xsd", "http://www.w3.org/2001/XMLSchema");
 resolver.setNamespaceMap(map);
 Map<String, String> resolvedMap = resolver.getNamespaceMap();
 Assert.assertEquals(map, resolvedMap);
 }

 @Test
 public void testGetReverseMap() {
 resolver = new NamespaceResolver();
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 Map<String, String> reverseMap = resolver.getReverseMap();
 Assert.assertNull(reverseMap);

 pointer = new NodePointer("", null);
 resolver.setNamespaceContextPointer(pointer);
 reverseMap = resolver.getReverseMap();
 Assert.assertEquals("http://www.w3.org/2001/XMLSchema", reverseMap.get("xsd"));
 }

 @Test
 public void testGetNamespaceContextPointer() {
 resolver = new NamespaceResolver();
 Assert.assertNull(resolver.getNamespaceContextPointer());

 pointer = new NodePointer("", null);
 resolver.setNamespaceContextPointer(pointer);
 Assert.assertEquals(pointer, resolver.getNamespaceContextPointer());
 }

 @Test
 public void testGetNamespaceContextPointerWithParent() {
 parentResolver = new NamespaceResolver();
 parentResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 parentResolver.registerNamespace("jxpath", "http://www.example.com/jxpath");
 resolver = new NamespaceResolver(parentResolver);
 NodePointer pointer = new NodePointer("", null);
 resolver.setNamespaceContextPointer(pointer);
 Assert.assertEquals(pointer, resolver.getNamespaceContextPointer());
 NodePointer parentPointer = new NodePointer("", null);
 parentResolver.setNamespaceContextPointer(parentPointer);
 Assert.assertEquals(parentPointer, resolver.getNamespaceContextPointer());
 }
}