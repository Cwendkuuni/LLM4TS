import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.Attributes;

public class XmlDeclarationTest {

 private XmlDeclaration createXmlDeclaration(String name, String baseUri, boolean isProcessingInstruction, String version, String encoding) {
 Attributes attributes = new Attributes();
 if (version != null) {
 attributes.put("version", version);
 }
 if (encoding != null) {
 attributes.put("encoding", encoding);
 }
 return new XmlDeclaration(name, baseUri, isProcessingInstruction, attributes);
 }

 @Test
 public void testXmlDeclarationConstructor() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "", false);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 assertNull(xmlDeclaration.getWholeDeclaration());

 xmlDeclaration = new XmlDeclaration("xml", "", true);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 assertNotNull(xmlDeclaration.getWholeDeclaration());
 }

 @Test(expected = NullPointerException.class)
 public void testXmlDeclarationConstructorNullName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(null, "", false);
 }

 @Test
 public void testGetWholeDeclaration() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "", false);
 assertEquals("xml", xmlDeclaration.getWholeDeclaration());

 xmlDeclaration = new XmlDeclaration("xml", "", true);
 assertEquals("<!xml>", xmlDeclaration.getWholeDeclaration());

 xmlDeclaration = new XmlDeclaration("xml", "", false, "1.1", "UTF-8");
 assertEquals("<!xml version=\"1.1\" encoding=\"UTF-8\">", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testOuterHtmlHead() throws IOException {
 Appendable accumulator = new StringBuffer();
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "", false);
 xmlDeclaration.outerHtmlHead(accumulator, 0, null);
 assertEquals("<xml>", accumulator.toString());

 xmlDeclaration = new XmlDeclaration("xml", "", true);
 xmlDeclaration.outerHtmlHead(accumulator, 0, null);
 assertEquals("<?xml>", accumulator.toString());
}

 @Test
 public void testOuterHtmlTail() throws IOException {
 Appendable accumulator = new StringBuffer();
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "", false);
 xmlDeclaration.outerHtmlTail(accumulator, 0, null);
 assertTrue(accumulator.toString().isEmpty());

 xmlDeclaration = new XmlDeclaration("xml", "", true);
 xmlDeclaration.outerHtmlTail(accumulator, 0, null);
 assertTrue(accumulator.toString().isEmpty());
}

 @Test
 public void testToString() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "", false);
 assertEquals("<xml>", xmlDeclaration.toString());

 xmlDeclaration = new XmlDeclaration("xml", "", true);
 assertEquals("<?xml>", xmlDeclaration.toString());

 xmlDeclaration = new XmlDeclaration("xml", "", false, "1.1", "UTF-8");
 assertEquals("<!xml version=\"1.1\" encoding=\"UTF-8\">", xmlDeclaration.toString());
}
}