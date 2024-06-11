import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.Attributes;

public class XmlDeclarationTest {

 private XmlDeclaration createXmlDeclaration(String name, String baseUri, boolean isProcessingInstruction, Attributes attributes) {
 return new XmlDeclaration(name, baseUri, isProcessingInstruction, attributes);
 }

 @Test
 public void testXmlDeclarationConstructor() {
 String name = "xml";
 String baseUri = "http://example.com";
 boolean isProcessingInstruction = false;
 XmlDeclaration xmlDeclaration = createXmlDeclaration(name, baseUri, isProcessingInstruction, null);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals(name, xmlDeclaration.name());
 assertEquals(baseUri, xmlDeclaration.baseUri());
 assertFalse(xmlDeclaration.isProcessingInstruction());
 }

 @Test
 public void testXmlDeclarationConstructorWithProcessingInstruction() {
 String name = "xml";
 String baseUri = "http://example.com";
 boolean isProcessingInstruction = true;
 XmlDeclaration xmlDeclaration = createXmlDeclaration(name, baseUri, isProcessingInstruction, null);
 assertTrue(xmlDeclaration.isProcessingInstruction());
 }

 @Test
 public void testXmlDeclarationConstructorWithAttributes() {
 String name = "xml";
 String baseUri = "http://example.com";
 boolean isProcessingInstruction = false;
 Attributes attributes = new Attributes();
 attributes.put("version", "1.0");
 attributes.put("encoding", "UTF-8");
 XmlDeclaration xmlDeclaration = createXmlDeclaration(name, baseUri, isProcessingInstruction, attributes);
 assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testXmlDeclarationConstructorWithNullName() {
 String baseUri = "http://example.com";
 boolean isProcessingInstruction = false;
 XmlDeclaration xmlDeclaration = assertThrows(IllegalArgumentException.class, () -> createXmlDeclaration(null, baseUri, isProcessingInstruction, null));
 }

 @Test
 public void testGetWholeDeclarationEmptyDeclaration() {
 String name = "xml";
 XmlDeclaration xmlDeclaration = new XmlDeclaration(name, null, false, null);
 assertEquals(name, xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testGetWholeDeclarationDeclarationWithAttributes() {
 String name = "xml";
 String baseUri = "http://example.com";
 boolean isProcessingInstruction = false;
 Attributes attributes = new Attributes();
 attributes.put("version", "1.0");
 attributes.put("encoding", "UTF-8");
 XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction, attributes);
 assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testGetWholeDeclarationProcessingInstruction() {
 String name = "";
 String baseUri = "http://example.com";
 boolean isProcessingInstruction = true;
 XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction, null);
 assertEquals("<?", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testNodeName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false, null);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 }

 @Test
 public void testToString() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false, null);
 assertEquals("<xml/>", xmlDeclaration.toString());
 }

 @Test
 public void testOuterHtmlHead() throws IOException {
 String name = "xml";
 String baseUri = "http://example.com";
 boolean isProcessingInstruction = false;
 Attributes attributes = new Attributes();
 attributes.put("version", "1.0");
 attributes.put("encoding", "UTF-8");
 XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction, attributes);
 Appendable accum = new StringBuilder();
 Document.OutputSettings out = null;
 xmlDeclaration.outerHtmlHead(accum, 0, out);
 assertEquals("<declaration version=\"1.0\" encoding=\"UTF-8\">", accum.toString());
 }

 @Test
 public void testOuterHtmlTail() throws IOException {
 String name = "xml";
 String baseUri = "http://example.com";
 boolean isProcessingInstruction = false;
 Attributes attributes = new Attributes();
 XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction, attributes);
 Appendable accum = new StringBuilder();
 Document.OutputSettings out = null;
 xmlDeclaration.outerHtmlTail(accum, 0, out);
 assertEquals("", accum.toString());
 }
}