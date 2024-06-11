import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

 private NamespaceResolver namespaceResolver;
 private NamespaceResolver parentResolver;

 @Test
 public void testNewNamespaceResolver() {
 NamespaceResolver resolver = new NamespaceResolver();
 assertNull(resolver.getParent());
 assertFalse(resolver.isSealed());
 }

 @Test
 public void testNewNamespaceResolverWithParent() {
 NamespaceResolver parent = new NamespaceResolver();
 NamespaceResolver resolver = new NamespaceResolver(parent);
 assertEquals(parent, resolver.getParent());
 assertFalse(resolver.isSealed());
 }

 @Test
 public void testRegisterNamespace() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("x", "http://example.com/x");
 assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
 assertNull(resolver.getNamespaceURI("y"));
 }

 @Test(expected = IllegalStateException.class)
 public void testRegisterNamespaceOnSealedResolver() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("x", "http://example.com/x");
 resolver.seal();
 resolver.registerNamespace("y", "http://example.com/y");
 }

 @Test
 public void testSetNamespaceContextPointer() {
 NamespaceResolver resolver = new NamespaceResolver();
 NodePointer pointer = new NodePointer();
 resolver.setNamespaceContextPointer(pointer);
 NodeIterator ni = pointer.namespaceIterator();
 assertTrue(ni.hasNext());
 }

 @Test
 public void testGetNamespaceURI() {
 NamespaceResolver parentResolver = new NamespaceResolver();
 parentResolver.registerNamespace("x", "http://example.com/x");
 NamespaceResolver resolver = new NamespaceResolver(parentResolver);
 resolver.setNamespaceContextPointer(new NodePointer());
 resolver.registerNamespace("y", "http://example.com/y");

 assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
 assertEquals("http://example.com/y", resolver.getNamespaceURI("y"));
 assertEquals(null, resolver.getNamespaceURI("z"));
 }

 @Test
 public void testGetPrefix() {
 NamespaceResolver parentResolver = new NamespaceResolver();
 parentResolver.registerNamespace("x", "http://example.com/x");
 parentResolver.registerNamespace("y", "http://example.com/y");
 NamespaceResolver resolver = new NamespaceResolver(parentResolver);
 resolver.setNamespaceContextPointer(new NodePointer());
 resolver.registerNamespace("z", "http://example.com/z");

 assertEquals("x", resolver.getPrefix("http://example.com/x"));
 assertEquals("y", resolver.getPrefix("http://example.com/y"));
 assertEquals("z", resolver.getPrefix("http://example.com/z"));
 assertEquals(null, resolver.getPrefix("http://example.com/w"));
 }

 @Test
 public void testIsSealed() {
 NamespaceResolver resolver = new NamespaceResolver();
 assertFalse(resolver.isSealed());
 resolver.seal();
 assertTrue(resolver.isSealed());
 }

 @Test
 public void testClone() {
 NamespaceResolver parentResolver = new NamespaceResolver();
 parentResolver.registerNamespace("x", "http://example.com/x");
 NamespaceResolver resolver = new NamespaceResolver(parentResolver);
 resolver.registerNamespace("y", "http://example.com/y");
 resolver.seal();

 NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
 assertEquals(resolver.getNamespaceURI("x"), clonedResolver.getNamespaceURI("x"));
 assertNotEquals(resolver.getNamespaceURI("x"), clonedResolver.getNamespaceURI("y"));
 assertFalse(clonedResolver.isSealed());
 }

 @Test
 public void testGetParent() {
 NamespaceResolver resolver = new NamespaceResolver();
 NamespaceResolver parent = new NamespaceResolver();
 resolver.setParent(parent);
 assertEquals(parent, resolver.getParent());
 }

 @Test
 public void testSeal() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("x", "http://example.com/x");
 resolver.seal();
 assertFalse(resolver.isSealed());
 resolver.seal();
 assertTrue(resolver.isSealed());
 }

 @Test
 public void testCloneWithParent() {
 NamespaceResolver parentResolver = new NamespaceResolver();
 parentResolver.registerNamespace("x", "http://example.com/x");
 NamespaceResolver resolver = new NamespaceResolver(parentResolver);
 resolver.registerNamespace("y", "http://example.com/y");
 resolver.seal();
 NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
 assertEquals(resolver.getNamespaceURI("x"), clonedResolver.getNamespaceURI("x"));
 assertNotEquals(resolver.getNamespaceURI("x"), clonedResolver.getNamespaceURI("y"));
 assertFalse(clonedResolver.isSealed());
 }
}