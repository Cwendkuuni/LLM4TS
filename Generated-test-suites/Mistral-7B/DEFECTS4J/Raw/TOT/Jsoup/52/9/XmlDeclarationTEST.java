import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.*;

public class XmlDeclarationTest {

 @Test
 public void testXmlDeclarationWithNameAndBaseUri() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 assertEquals("<?xml>\n", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithNameProcessingInstructionAndAttributes() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", true);
 xmlDeclaration.attributes().put("version", "1.0");
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"\n", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithEmptyName() {
 try {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(null, "http://example.com", false);
 fail("Expected NullPointerException, but got none.");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testXmlDeclarationToString() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
 assertEquals("<?xml>\n", xmlDeclaration.toString());
 }

 @Test
 public void testXmlDeclarationOuterHtml() throws IOException {
 Document document = Jsoup.parse("<html><head><![CDATA[ [" +
 "  xmlDeclaration = new XmlDeclaration(\"xml\", \"http://example.com\", false);\n" +
 "  xmlDeclaration.outerHtmlHead(appendable, 0, null);\n" +
 "  xmlDeclaration.outerHtmlTail(appendable, 0, null);\n" +
 "  document.head().append(xmlDeclaration.toString());\n" +
 "  out.println(document.html());]\n]>]\n</head><body></body></html>", "UTF-8");
 String expected = "<html>\n<head>\n<![CDATA[\n<?xml>\n\n]]>\n</head>\n<body>\n</body>\n</html>";
 assertEquals(expected, document.html());
 }

 @Test(expected = NullPointerException.class)
 public void testXmlDeclarationOuterHtmlWithNullAppendable() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
 xmlDeclaration.outerHtmlHead(null, 0, null);
 }
}