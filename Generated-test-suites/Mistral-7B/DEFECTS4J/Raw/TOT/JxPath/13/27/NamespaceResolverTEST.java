import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.apache.commons.jxpath.ri.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

 @Test
 public void testRegisterNamespace() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("xpath", "http://www.w3.org/2005/XPath");
 String uri = resolver.getNamespaceURI("xpath");
 assertEquals("http://www.w3.org/2005/XPath", uri);
 }

 @Test
 public void testGetNamespaceURI() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("xpath", "http://www.w3.org/2005/XPath");
 String prefix = resolver.getNamespaceURI("xpath");
 assertEquals("http://www.w3.org/2005/XPath", prefix);

 NamespaceResolver resolver2 = new NamespaceResolver(resolver);
 String prefix2 = resolver2.getNamespaceURI("xpath");
 assertEquals("http://www.w3.org/2005/XPath", prefix2);
 }

 @Test
 public void testGetPrefix() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("xpath", "http://www.w3.org/2005/XPath");
 String uri = resolver.getNamespaceURI("xpath");
 String prefix = resolver.getPrefix(uri);
 assertEquals("xpath", prefix);

 NamespaceResolver resolver2 = new NamespaceResolver();
 resolver2.registerNamespace("xmlns", "http://www.w3.org/2000/xmlns/");
 String namespaceURI = resolver2.getNamespaceURI("xmlns");
 String prefix2 = resolver2.getPrefix(namespaceURI);
 assertEquals("xmlns", prefix2);
 }

 @Test
 public void testSetNamespaceContextPointer() {
 NamespaceResolver resolver = new NamespaceResolver();
 NodePointer pointer = new NodePointer();
 resolver.setNamespaceContextPointer(pointer);
 String prefix = resolver.getPrefix("http://www.w3.org/2005/XPath");
 assertNull(prefix);

 pointer.setNamespaceURI("http://www.w3.org/2005/XPath", "xpath");
 prefix = resolver.getPrefix("http://www.w3.org/2005/XPath");
 assertEquals("xpath", prefix);
 }

 @Test
 public void testIsSealed() {
 NamespaceResolver resolver = new NamespaceResolver();
 assertFalse(resolver.isSealed());

 resolver.seal();
 assertTrue(resolver.isSealed());
 }

 @Test(expected = IllegalStateException.class)
 public void testRegisterNamespaceOnSealedResolver() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("xpath", "http://www.w3.org/2005/XPath");
 resolver.seal();
 resolver.registerNamespace("xpath2", "http://www.w3.org/2005/XPath2");
 }
}