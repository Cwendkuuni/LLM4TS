import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;
import java.io.IOException;

public class XmlDeclarationTest {

 private XmlDeclaration xmlDeclaration;

 @Test
 public void testXmlDeclarationConstructor() {
 xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertNull(xmlDeclaration.name());
 assertTrue(xmlDeclaration.isProcessingInstruction());
 }

 @Test
 public void testXmlDeclarationNameConstructor() {
 xmlDeclaration = new XmlDeclaration(" XML Declaration", "baseUri", false);
 assertEquals(" XML Declaration", xmlDeclaration.name());
 }

 @Test
 public void testXmlDeclarationWithAttributesConstructor() {
 xmlDeclaration = new XmlDeclaration("xml", "baseUri", true, new Attributes().put("version", "1.0").put("encoding", "UTF-8"));
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationEmptyNameConstructor() {
 xmlDeclaration = new XmlDeclaration("", "baseUri", false);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertNull(xmlDeclaration.name());
 }

 @Test(expected = NullPointerException.class)
 public void testXmlDeclarationNullNameConstructor() {
 Validate.isNull("name is null", () -> new XmlDeclaration(null, "baseUri", false));
 }

 @Test
 public void testXmlDeclarationGetWholeDeclarationNullVersionAttribute() {
 xmlDeclaration = new XmlDeclaration("xml", "baseUri", true, new Attributes());
 assertEquals("xml", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationGetWholeDeclarationWithVersionAttribute() {
 xmlDeclaration = new XmlDeclaration("xml", "baseUri", true, new Attributes().put("version", "1.1"));
 assertEquals("xml version=\"1.1\"", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationGetWholeDeclarationWithEncodingAttribute() {
 xmlDeclaration = new XmlDeclaration("xml", "baseUri", true, new Attributes().put("encoding", "UTF-16"));
 assertEquals("xml version=\"1.0\" encoding=\"UTF-16\"", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationGetWholeDeclarationWithVersionAndEncodingAttributes() {
 xmlDeclaration = new XmlDeclaration("xml", "baseUri", true, new Attributes().put("version", "1.2").put("encoding", "UTF-32"));
 assertEquals("xml version=\"1.2\" encoding=\"UTF-32\"", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationToString() {
 xmlDeclaration = new XmlDeclaration("xml", "baseUri", true, new Attributes().put("version", "1.2").put("encoding", "UTF-32"));
 assertEquals("<?xml version=\"1.2\" encoding=\"UTF-32\"?>", xmlDeclaration.toString());
 }

 @Test(expected = IOException.class)
 public void testOuterHtmlHeadThrowsIOException() throws IOException {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", true);
 Appendable appendable = new StringBuilder();
 xmlDeclaration.outerHtmlHead(appendable, 0, null);
 }

 @Test
 public void testOuterHtmlHead() throws IOException {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", true, new Attributes().put("version", "1.2").put("encoding", "UTF-32"));
 StringBuilder appendable = new StringBuilder();
 xmlDeclaration.outerHtmlHead(appendable, 0, null);
 assertEquals("<?xml version=\"1.2\" encoding=\"UTF-32\"?>", appendable.toString());
 }

 @Test
 public void testOuterHtmlTail() throws IOException {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", true);
 StringBuilder appendable = new StringBuilder();
 xmlDeclaration.outerHtmlTail(appendable, 0, null);
 assertEquals("", appendable.toString());
 }
}