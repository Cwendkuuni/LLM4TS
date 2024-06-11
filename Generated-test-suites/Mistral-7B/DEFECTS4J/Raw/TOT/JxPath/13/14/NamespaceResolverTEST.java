import org.apache.commons.jxpath.NamespaceContext;
import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class NamespaceResolverTest {

 private NamespaceResolver resolver;
 private Document document;

 @Before
 public void setUp() throws Exception {
 document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
 resolver = new NamespaceResolver();
 }

 @Test
 public void testRegisterNamespace() {
 resolver.registerNamespace("x", "http://example.com/x");
 assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
 assertNull(resolver.getNamespaceURI("y"));
 }

 @Test
 public void testGetNamespaceURIWithSetPointer() {
 resolver.registerNamespace("x", "http://example.com/x");
 Element root = document.createElement("root");
 NodePointer pointer = new NodePointer(document, root);
 resolver.setNamespaceContextPointer(pointer);
 assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
 assertNull(resolver.getNamespaceURI("y"));
 }

 @Test
 public void testGetNamespaceURIWithSetParent() {
 NamespaceResolver parent = new NamespaceResolver();
 parent.registerNamespace("x", "http://example.com/x");
 resolver = new NamespaceResolver(parent);
 NodePointer pointer = new NodePointer(document, document.createProcessingInstruction("xml", "version='1.0'"));
 resolver.setNamespaceContextPointer(pointer);
 assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
 assertNull(resolver.getNamespaceURI("y"));
 }

 @Test(expected = IllegalStateException.class)
 public void testRegisterNamespaceAfterSealed() {
 resolver.seal();
 resolver.registerNamespace("x", "http://example.com/x");
 }

 @Test
 public void testGetNamespaceURIWithParent() {
 NamespaceResolver parent = new NamespaceResolver();
 parent.registerNamespace("x", "http://example.com/x");
 resolver = new NamespaceResolver(parent);
 NodePointer pointer = new NodePointer(document, document.createElementNS("http://example.com/y", "y:element"));
 resolver.setNamespaceContextPointer(pointer);
 assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
 assertEquals("http://example.com/y", pointer.getNamespaceURI());
 }

 @Test
 public void testGetPrefix() {
 resolver.registerNamespace("x", "http://example.com/x");
 assertEquals("x", resolver.getPrefix("http://example.com/x"));
 assertNull(resolver.getPrefix("http://example.com/y"));
 }

 @Test
 public void testGetPrefixWithSetPointer() {
 resolver.registerNamespace("x", "http://example.com/x");
 Element root = document.createElementNS("http://example.com/y", "y:root");
 root.setAttributeNS("xmlns:x", "http://example.com/x");
 NodePointer pointer = new NodePointer(document, root);
 resolver.setNamespaceContextPointer(pointer);
 assertEquals("x", resolver.getPrefix("http://example.com/x"));
 assertNull(resolver.getPrefix("http://example.com/y"));
 }

 @Test
 public void testGetPrefixWithSetParent() {
 NamespaceResolver parent = new NamespaceResolver();
 parent.registerNamespace("x", "http://example.com/x");
 resolver = new NamespaceResolver(parent);
 NodePointer pointer = new NodePointer(document, document.createProcessingInstruction("xml", "version='1.0'"));
 pointer.setNamespaceContext(new NamespaceContext() {
 @Override
 public String getNamespaceURI(String prefix) {
 return prefix.equals("x") ? "http://example.com/x" : null;
 }

 @Override
 public String getPrefix(String namespaceURI) {
 return namespaceURI.equals("http://example.com/x") ? "x" : null;
 }
 });
 resolver.setNamespaceContextPointer(pointer);
 assertEquals("x", resolver.getPrefix("http://example.com/x"));
 assertNull(resolver.getPrefix("http://example.com/y"));
 }

 @Test
 public void testSeal() {
 resolver.registerNamespace("x", "http://example.com/x");
 resolver.seal();
 try {
 resolver.registerNamespace("x", "http://example.com/x");
 } catch (IllegalStateException e) {
 assertEquals("Cannot register namespaces on a sealed NamespaceResolver", e.getMessage());
 }
 }

 @Test
 public void testClone() {
 NamespaceResolver original = new NamespaceResolver();
 original.registerNamespace("x", "http://example.com/x");
 NamespaceResolver clone = (NamespaceResolver) original.clone();
 assertEquals("http://example.com/x", clone.getNamespaceURI("x"));
 assertNull(clone.getNamespaceURI("y"));
 }
}