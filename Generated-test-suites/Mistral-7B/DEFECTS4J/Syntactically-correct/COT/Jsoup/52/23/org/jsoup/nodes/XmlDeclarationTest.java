import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;
import org.jsoup.helper.ElementFilter;

public class XmlDeclarationTest {

 private XmlDeclaration xmlDeclaration;

 @Test
 public void testXmlDeclarationConstructor() {
 xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertNotNull(xmlDeclaration.name());
 assertEquals("#declaration", xmlDeclaration.nodeName());
 }

 @Test
 public void testXmlDeclarationConstructorWithProcessingInstruction() {
 xmlDeclaration = new XmlDeclaration("xml", null, true);
 assertNotNull(xmlDeclaration.name());
 assertEquals("#declaration", xmlDeclaration.nodeName());
 }

 @Test
 public void testXmlDeclarationConstructorWithNameAndProcessingInstruction() {
 xmlDeclaration = new XmlDeclaration("xml-ns", null, true);
 assertNotNull(xmlDeclaration.name());
 assertEquals("#declaration", xmlDeclaration.nodeName());
 }

 @Test
 public void testXmlDeclarationConstructorWithNameAndBaseUri() {
 xmlDeclaration = new XmlDeclaration("xml", "https://example.com", false);
 assertNotNull(xmlDeclaration.name());
 assertEquals("#declaration", xmlDeclaration.nodeName());
 }

 @Test
 public void testXmlDeclarationConstructorWithNameProcessingInstructionAndBaseUri() {
 xmlDeclaration = new XmlDeclaration("xml", "https://example.com", true);
 assertNotNull(xmlDeclaration.name());
 assertEquals("#declaration", xmlDeclaration.nodeName());
 }

 @Test
 public void testXmlDeclarationGetWholeDeclarationWithEmptyAttributes() {
 xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertEquals("xml", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationGetWholeDeclarationWithVersionAttribute() {
 xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().put("version", "1.0");
 assertEquals("xml version=\"1.0\"", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationGetWholeDeclarationWithEncodingAttribute() {
 xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 assertEquals("xml encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationGetWholeDeclarationWithVersionAndEncodingAttributes() {
 xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().put("version", "1.0");
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
 }

 @Test(expected = NullPointerException.class)
 public void testXmlDeclarationGetWholeDeclarationWithNullName() {
 Validate.expect(NullPointerException.class);
 xmlDeclaration = new XmlDeclaration(null, null, false);
 }

 @Test(expected = NullPointerException.class)
 public void testXmlDeclarationGetWholeDeclarationWithNullAttributes() {
 Attributes attributes = null;
 xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes = attributes;
 Validate.expect(NullPointerException.class);
 xmlDeclaration.getWholeDeclaration();
 }

 @Test
 public void testXmlDeclarationToString() {
 xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertEquals("<xml>", xmlDeclaration.toString());
 }

 @Test
 public void testXmlDeclarationOuterHtmlHead() throws IOException {
 Appendable appendable = new StringBuilder();
 xmlDeclaration = new XmlDeclaration("xml", null, false);
 Document document = new Document();
 xmlDeclaration.outerHtmlHead(appendable, 0, document.outputSettings());
 assertEquals("<xml>", appendable.toString());
 }

 @Test
 public void testXmlDeclarationOuterHtmlTail() throws IOException {
 Appendable appendable = new StringBuilder();
 xmlDeclaration = new XmlDeclaration("xml", null, false);
 Document document = new Document();
 xmlDeclaration.outerHtmlTail(appendable, 0, document.outputSettings());
 assertEquals("", appendable.toString());
 }

 @Test
 public void testXmlDeclarationOuterHtmlWithAttributes() throws IOException {
 Appendable appendable = new StringBuilder();
 xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().put("version", "1.0");
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 Document document = new Document();
 xmlDeclaration.outerHtml(appendable, 0, document.outputSettings());
 assertEquals("<xml version=\"1.0\" encoding=\"UTF-8\"/>", appendable.toString());
 }

 @Test
 public void testXmlDeclarationWithProcessingInstructionOuterHtml() throws IOException {
 Appendable appendable = new StringBuilder();
 xmlDeclaration = new XmlDeclaration("xml", null, true);
 Document document = new Document();
 xmlDeclaration.outerHtml(appendable, 0, document.outputSettings());
 assertEquals("<?xml>", appendable.toString());
 }

 @Test
 public void testParseXmlDeclaration() {
 Element xmlDeclarationElement = ElementFilter.filter("xml", Element.class, new org.jsoup.nodes.Document());
 assertEquals("#declaration", xmlDeclarationElement.nodeName());
 }
}