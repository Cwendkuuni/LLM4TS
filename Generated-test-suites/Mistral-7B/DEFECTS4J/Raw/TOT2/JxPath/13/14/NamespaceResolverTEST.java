import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

 @Test
 public void testNewInstance() {
 NamespaceResolver resolver = new NamespaceResolver();
 assertNull(resolver.getNamespaceURI("prefix"));
 assertNull(resolver.getPrefix("http://example.com"));
 }

 @Test
 public void testRegisterNamespace() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("prefix", "http://example.com");
 assertEquals("http://example.com", resolver.getNamespaceURI("prefix"));
 assertEquals("prefix", resolver.getPrefix("http://example.com"));
 }

 @Test(expected = IllegalStateException.class)
 public void testRegisterNamespaceOnSealedResolver() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("prefix", "http://example.com");
 resolver.seal();
 resolver.registerNamespace("prefix2", "http://another-example.com");
 }

 @Test
 public void testGetNamespaceURIWithPointer() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.setNamespaceContextPointer(new NodePointer("root", "http://example.com"));
 assertEquals("http://example.com", resolver.getNamespaceURI("x"));
 }

 @Test
 public void testGetNamespaceURIWithParent() {
 NamespaceResolver child = new NamespaceResolver();
 NamespaceResolver parent = new NamespaceResolver();
 child.registerNamespace("prefix", "http://example.com");
 child.setNamespaceContextPointer(new NodePointer("root", "http://test.com"));
 child.setParent(parent);
 parent.registerNamespace("prefix2", "http://another-example.com");
 assertEquals("http://example.com", child.getNamespaceURI("prefix"));
 assertEquals("http://another-example.com", child.getNamespaceURI("prefix2"));
 }

 @Test
 public void testGetPrefixWithURI() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("prefix", "http://example.com");
 assertEquals("prefix", resolver.getPrefix("http://example.com"));
 }

 @Test
 public void testGetPrefixWithParent() {
 NamespaceResolver child = new NamespaceResolver();
 NamespaceResolver parent = new NamespaceResolver();
 child.registerNamespace("prefix", "http://example.com");
 child.setNamespaceContextPointer(new NodePointer("root", "http://test.com"));
 child.setParent(parent);
 parent.registerNamespace("prefix2", "http://another-example.com");
 assertEquals("prefix", child.getPrefix("http://example.com"));
 assertEquals("prefix2", child.getPrefix("http://another-example.com"));
 }

 @Test
 public void testIsSealed() {
 NamespaceResolver resolver = new NamespaceResolver();
 assertFalse(resolver.isSealed());
 resolver.seal();
 assertTrue(resolver.isSealed());
 }
}