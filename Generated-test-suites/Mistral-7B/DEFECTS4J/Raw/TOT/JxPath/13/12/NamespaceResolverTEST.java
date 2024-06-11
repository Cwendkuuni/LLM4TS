import org.apache.commons.jxpath.NamespaceResolver;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for NamespaceResolver.
 */
public class NamespaceResolverTest {

 @Test
 public void testRegisterNamespace() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 String uri = resolver.getNamespaceURI("xsd");
 assertEquals("http://www.w3.org/2001/XMLSchema", uri);
 }

 @Test(expected = IllegalStateException.class)
 public void testRegisterNamespaceOnSealedResolver() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 resolver.seal();
 resolver.registerNamespace("foo", "bar");
 }

 @Test
 public void testGetNamespaceURI() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 resolver.registerNamespace("xml", "http://www.w3.org/XML/1998/namespace");
 String prefix = "xml";
 String uri = resolver.getNamespaceURI(prefix);
 assertEquals("http://www.w3.org/XML/1998/namespace", uri);

 resolver.setNamespaceContextPointer(new NodePointer(""));
 NodePointer nodePointer = new NodePointer("ns0:root", null);
 resolver.setNamespaceContextPointer(nodePointer);
 uri = resolver.getNamespaceURI(prefix);
 assertEquals("http://www.w3.org/XML/1998/namespace", uri);

 NodeIterator ni = NodeIterator.getEmptyIterator();
 NodePointer nsPointer = new NodePointer("ns1:test", ni);
 resolver.setNamespaceContextPointer(nsPointer);
 uri = resolver.getNamespaceURI(prefix);
 assertNull(uri);
 }

 @Test
 public void testGetPrefix() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 String uri = "http://www.w3.org/2001/XMLSchema";
 String prefix = resolver.getPrefix(uri);
 assertEquals("xsd", prefix);

 resolver.setNamespaceContextPointer(new NodePointer(""));
 NodePointer nodePointer = new NodePointer("xml:root", null);
 resolver.setNamespaceContextPointer(nodePointer);
 prefix = resolver.getPrefix(uri);
 assertEquals("xml", prefix);

 resolver.setNamespaceContextPointer(new NodePointer("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"", null));
 prefix = resolver.getPrefix(uri);
 assertEquals("xsd", prefix);
 }

 @Test
 public void testIsSealed() {
 NamespaceResolver resolver = new NamespaceResolver();
 assertFalse(resolver.isSealed());

 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 assertFalse(resolver.isSealed());

 resolver.seal();
 assertTrue(resolver.isSealed());
 }

 @Test
 public void testClone() {
 NamespaceResolver original = new NamespaceResolver();
 original.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 NamespaceResolver clone = (NamespaceResolver) original.clone();
 assertEquals(original.getNamespaceURI("xsd"), clone.getNamespaceURI("xsd"));
 }

 @Test
 public void testGetNamespaceURIAndGetPrefixWithSameUri() {
 NamespaceResolver resolver = new NamespaceResolver();
 String uri = "http://example.com/mynamespace";
 String prefix = resolver.getPrefix(uri);
 assertEquals("", prefix);

 resolver.registerNamespace(uri, uri);
 prefix = resolver.getPrefix(uri);
 assertEquals(uri, prefix);
 }

 @Test(expected = NullPointerException.class)
 public void testGetNamespaceURIWithNullPrefix() {
 NamespaceResolver resolver = new NamespaceResolver();
 String prefix = null;
 String uri = resolver.getNamespaceURI(prefix);
 }

 @Test(expected = NullPointerException.class)
 public void testGetPrefixWithNullUri() {
 NamespaceResolver resolver = new NamespaceResolver();
 String uri = null;
 String prefix = resolver.getPrefix(uri);
 }
}