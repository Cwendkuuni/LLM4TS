import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlDeclarationTest {

 @Test
 public void testXmlDeclarationCreation() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertNotNull(xmlDeclaration);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 }

 @Test
 public void testXmlDeclarationWithVersionAndEncoding() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 Attributes attributes = new Attributes();
 attributes.put("version", "1.0");
 attributes.put("encoding", "UTF-8");
 xmlDeclaration.attributes = attributes;
 String expectedXmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 assertEquals(expectedXmlDeclaration, xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationEmptyName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(null, null, false);
 assertNull(xmlDeclaration.name());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testXmlDeclarationNullName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(null, null, false);
 }

 @Test
 public void testXmlDeclarationProcessingInstruction() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, true);
 String expectedXmlDeclaration = "<?xml?>";
 assertEquals(expectedXmlDeclaration, xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationToString() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertEquals("<xml/>", xmlDeclaration.toString());
 }

 @Test
 public void testXmlDeclarationOuterHtml() throws IOException {
 Document doc = Jsoup.parse("<root><declaration version=\"1.0\" encoding=\"UTF-8\"/></root>");
 XmlDeclaration xmlDeclaration = (XmlDeclaration) doc.getElementsByTag("declaration").first();
 StringBuilder expectedOutput = new StringBuilder();
 xmlDeclaration.outerHtmlHead(expectedOutput, 0, null);
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", expectedOutput.toString());
 }
}