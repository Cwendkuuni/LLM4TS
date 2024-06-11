import org.apache.commons.jxpath.Node;
import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

 private NamespaceResolver resolver;
 private Node rootNode;

 @Test
 public void testRegisterNamespace() {
 resolver = new NamespaceResolver();
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 assertEquals("http://www.w3.org/2001/XMLSchema", resolver.getNamespaceURI("xsd"));
 }

 @Test(expected = IllegalStateException.class)
 public void testRegisterNamespaceAfterSeal() {
 resolver = new NamespaceResolver();
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 resolver.seal();
 resolver.registerNamespace("xsd2", "http://www.w3.org/2001/XMLSchema");
 fail("Expected IllegalStateException");
 }

 @Test
 public void testGetNamespaceURI() {
 resolver = new NamespaceResolver();
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 resolver.registerNamespace("foo", "http://example.com/foo");
 assertEquals("http://example.com/foo", resolver.getNamespaceURI("foo"));
 assertEquals("http://www.w3.org/2001/XMLSchema", resolver.getNamespaceURI("xsd"));
 }

 @Test
 public void testGetNamespaceURIUnknown() {
 resolver = new NamespaceResolver();
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 assertEquals(null, resolver.getNamespaceURI("unknown"));
 }

 @Test
 public void testGetPrefix() {
 resolver = new NamespaceResolver();
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 resolver.registerNamespace("foo", "http://example.com/foo");
 assertEquals("foo", resolver.getPrefix("http://example.com/foo"));
 assertEquals("xsd", resolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
 assertEquals(null, resolver.getPrefix("http://example.com/unknown"));
 }

 @Test
 public void testGetPrefixUnknown() {
 resolver = new NamespaceResolver();
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 resolver.registerNamespace("foo", "http://example.com/foo");
 assertEquals("foo", resolver.getPrefix("http://example.com/foo"));
 assertEquals(null, resolver.getPrefix("http://example.com/unknown"));
 }

 @Test
 public void testSeal() {
 resolver = new NamespaceResolver();
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 resolver.registerNamespace("foo", "http://example.com/foo");
 assertEquals(false, resolver.isSealed());
 resolver.seal();
 assertEquals(true, resolver.isSealed());
 }

 @Test
 public void testClone() {
 NamespaceResolver resolver1 = new NamespaceResolver();
 NamespaceResolver resolver2 = (NamespaceResolver) resolver1.clone();
 resolver1.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 assertEquals(false, resolver1.isSealed());
 assertEquals(false, resolver2.isSealed());
 resolver1.seal();
 assertEquals(false, resolver1.isSealed());
 assertEquals(false, resolver2.isSealed());
 resolver2.registerNamespace("xsd2", "http://www.w3.org/2001/XMLSchema");
 assertEquals("http://www.w3.org/2001/XMLSchema", resolver1.getNamespaceURI("xsd"));
 assertEquals(null, resolver2.getNamespaceURI("xsd"));
 }
}