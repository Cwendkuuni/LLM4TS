import org.apache.commons.jxpath.NamespaceResolver;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

 private NamespaceResolver nsResolver;
 private NamespaceResolver nsResolverWithParent;

 @Test
 public void testConstructor() {
 nsResolver = new NamespaceResolver();
 assertNull(nsResolver.getParent());
 }

 @Test
 public void testConstructorWithParent() {
 nsResolverWithParent = new NamespaceResolver(new NamespaceResolver());
 assertEquals(nsResolverWithParent.getParent(), nsResolverWithParent);
 }

 @Test
 public void testRegisterNamespace() {
 nsResolver = new NamespaceResolver();
 nsResolver.registerNamespace("x", "http://example.com/x");
 String namespaceURI = "http://example.com/x";
 String prefix = nsResolver.getPrefix(namespaceURI);
 assertEquals("x", prefix);
 }

 @Test(expected = IllegalStateException.class)
 public void testRegisterNamespaceOnSealedResolver() {
 nsResolver = new NamespaceResolver();
 nsResolver.seal();
 nsResolver.registerNamespace("x", "http://example.com/x");
 }

 @Test
 public void testGetNamespaceURI() {
 nsResolver = new NamespaceResolver();
 nsResolver.registerNamespace("x", "http://example.com/x");
 String prefix = "x";
 String namespaceURI = nsResolver.getNamespaceURI(prefix);
 assertEquals("http://example.com/x", namespaceURI);
 }

 @Test
 public void testGetNamespaceURIWithNullPrefix() {
 nsResolver = new NamespaceResolver();
 String namespaceURI = nsResolver.getNamespaceURI(null);
 assertNull(namespaceURI);
 }

 @Test
 public void testGetNamespaceURIWithUnregisteredPrefix() {
 nsResolver = new NamespaceResolver();
 String prefix = "y";
 String namespaceURI = nsResolver.getNamespaceURI(prefix);
 assertNull(namespaceURI);
 }

 @Test
 public void testGetNamespaceURIWithParentPrefix() {
 nsResolverWithParent = new NamespaceResolver(new NamespaceResolver());
 nsResolverWithParent.registerNamespace("x", "http://example.com/x");
 nsResolverWithParent.setNamespaceContextPointer(new NodePointer(""));
 NodePointer nodePointer = new NodePointer("{http://example.com/x}node");
 nsResolverWithParent.setNamespaceContextPointer(nodePointer);
 String prefix = "x";
 String namespaceURI = nsResolverWithParent.getNamespaceURI(prefix);
 assertEquals("http://example.com/x", namespaceURI);
 }

 @Test
 public void testGetNamespaceURIWithParentUnregisteredPrefix() {
 nsResolverWithParent = new NamespaceResolver(new NamespaceResolver());
 nsResolverWithParent.registerNamespace("x", "http://example.com/x");
 nsResolverWithParent.setNamespaceContextPointer(new NodePointer(""));
 NodePointer nodePointer = new NodePointer("{http://other.com/y}node");
 nsResolverWithParent.setNamespaceContextPointer(nodePointer);
 String prefix = "x";
 String namespaceURI = nsResolverWithParent.getNamespaceURI(prefix);
 assertNull(namespaceURI);
 }

 @Test
 public void testSetNamespaceContextPointer() {
 nsResolver = new NamespaceResolver();
 nsResolver.registerNamespace("x", "http://example.com/x");
 NodePointer nodePointer = new NodePointer("{http://example.com/x}node");
 nsResolver.setNamespaceContextPointer(nodePointer);
 String prefix = "x";
 String namespaceURI = nsResolver.getNamespaceURI(prefix);
 assertEquals("http://example.com/x", namespaceURI);
 }

 @Test
 public void testSetNamespaceContextPointerWithNullPointer() {
 nsResolver = new NamespaceResolver();
 nsResolver.registerNamespace("x", "http://example.com/x");
 nsResolver.setNamespaceContextPointer(null);
 String prefix = "x";
 String namespaceURI = nsResolver.getNamespaceURI(prefix);
 assertNull(namespaceURI);
 }

 @Test
 public void testGetPrefix() {
 nsResolver = new NamespaceResolver();
 nsResolver.registerNamespace("x", "http://example.com/x");
 String namespaceURI = "http://example.com/x";
 String prefix = nsResolver.getPrefix(namespaceURI);
 assertEquals("x", prefix);
 }

 @Test
 public void testGetPrefixWithNullNamespaceURI() {
 nsResolver = new NamespaceResolver();
 String prefix = nsResolver.getPrefix(null);
 assertNull(prefix);
 }

 @Test
 public void testGetPrefixWithUnregisteredNamespaceURI() {
 nsResolver = new NamespaceResolver();
 String namespaceURI = "http://other.com/y";
 String prefix = nsResolver.getPrefix(namespaceURI);
 assertNull(prefix);
 }

 @Test
 public void testGetPrefixWithParentPrefix() {
 nsResolverWithParent = new NamespaceResolver(new NamespaceResolver());
 nsResolverWithParent.registerNamespace("x", "http://example.com/x");
 nsResolverWithParent.setNamespaceContextPointer(new NodePointer(""));
 NodePointer nodePointer = new NodePointer("{http://example.com/x}node");
 nsResolverWithParent.setNamespaceContextPointer(nodePointer);
 String namespaceURI = "http://example.com/x";
 String prefix = nsResolverWithParent.getPrefix(namespaceURI);
 assertEquals("x", prefix);
 }

 @Test
 public void testGetPrefixWithParentUnregisteredNamespaceURI() {
 nsResolverWithParent = new NamespaceResolver(new NamespaceResolver());
 nsResolverWithParent.registerNamespace("x", "http://example.com/x");
 nsResolverWithParent.setNamespaceContextPointer(new NodePointer(""));
 NodePointer nodePointer = new NodePointer("{http://other.com/y}node");
 nsResolverWithParent.setNamespaceContextPointer(nodePointer);
 String namespaceURI = "http://example.com/x";
 String prefix = nsResolverWithParent.getPrefix(namespaceURI);
 assertNull(prefix);
 }

 @Test
 public void testIsSealed() {
 nsResolver = new NamespaceResolver();
 assertFalse(nsResolver.isSealed());
 }

 @Test
 public void testSeal() {
 nsResolver = new NamespaceResolver();
 nsResolver.registerNamespace("x", "http://example.com/x");
 nsResolver.seal();
 assertTrue(nsResolver.isSealed());
 }

 @Test(expected = IllegalStateException.class)
 public void testSealTwice() {
 nsResolver = new NamespaceResolver();
 nsResolver.registerNamespace("x", "http://example.com/x");
 nsResolver.seal();
 nsResolver.seal();
 }

 @Test
 public void testClone() {
 NamespaceResolver original = new NamespaceResolver();
 original.registerNamespace("x", "http://example.com/x");
 NamespaceResolver clone = (NamespaceResolver) original.clone();
 assertNotNull(clone);
 assertNotSame(original, clone);
 assertFalse(original.isSealed());
 assertFalse(clone.isSealed());
 String namespaceURI = "http://example.com/x";
 String prefix = original.getPrefix(namespaceURI);
 assertEquals("x", prefix);
 String prefixCloned = clone.getPrefix(namespaceURI);
 assertEquals("x", prefixCloned);
 }

}