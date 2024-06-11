import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlDeclarationTest {

 private Document document;

 @Before
 public void setUp() {
 document = new Document();
 }

 @Test
 public void testXmlDeclarationCreation() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
 assertNotNull(xmlDeclaration);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 }

 @Test
 public void testProcessingInstructionXmlDeclarationCreation() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", true);
 assertNotNull(xmlDeclaration);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 }

 @Test
 public void testGetWholeDeclarationWithVersionAndEncoding() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
 xmlDeclaration.attributes().put("version", "1.0");
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 assertEquals("<declaration version=\"1.0\" encoding=\"UTF-8\" >", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testGetWholeDeclarationWithOnlyVersion() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
 xmlDeclaration.attributes().put("version", "1.0");
 assertEquals("<declaration version=\"1.0\" >", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testGetWholeDeclarationWithOnlyEncoding() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 assertEquals("<declaration encoding=\"UTF-8\" >", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testGetWholeDeclarationWithNoAttributes() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
 assertEquals("xml", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testOuterHtmlHead() throws IOException {
 Element element = document.createElement("html");
 element.appendChild(new XmlDeclaration("xml", null, false));
 String expected = "<declaration version=\"1.0\" encoding=\"UTF-8\" >";
 String actual = "";
 Appendable accum = new StringBuilder();
 element.outerHtmlHead(accum, 0, null);
 actual = accum.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testOuterHtmlTail() throws IOException {
 Element element = document.createElement("html");
 element.appendChild(new XmlDeclaration("xml", null, false));
 String expected = "";
 String actual = "";
 Appendable accum = new StringBuilder();
 element.outerHtmlTail(accum, 0, null);
 actual = accum.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testToString() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
 assertEquals("<declaration>", xmlDeclaration.toString());
 }
}