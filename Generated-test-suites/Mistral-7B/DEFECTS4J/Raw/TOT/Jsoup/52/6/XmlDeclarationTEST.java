import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XmlDeclarationTest {

 @Test
 public void testXmlDeclarationWithNameAndVersion() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().put("version", "1.0");

 Document document = new Document();
 try {
 document.outputSettings(false);
 xmlDeclaration.outerHtmlHead(document.bodyAppender(), 0, document.outputSettings());
 document.bodyAppender().append("</html>");
 document.output();
 String expected = "<declaration version=\"1.0\">";
 assertEquals(expected, xmlDeclaration.toString());
 } catch (IOException e) {
 // IOException should not be thrown in this test case
 }
 }

 @Test
 public void testXmlDeclarationWithNameAndEncoding() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().put("encoding", "UTF-8");

 Document document = new Document();
 try {
 document.outputSettings(false);
 xmlDeclaration.outerHtmlHead(document.bodyAppender(), 0, document.outputSettings());
 document.bodyAppender().append("</html>");
 document.output();
 String expected = "<declaration encoding=\"UTF-8\">";
 assertEquals(expected, xmlDeclaration.toString());
 } catch (IOException e) {
 // IOException should not be thrown in this test case
 }
 }

 @Test
 public void testXmlDeclarationWithNameProcessingInstruction() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, true);

 Document document = new Document();
 try {
 document.outputSettings(false);
 xmlDeclaration.outerHtmlHead(document.bodyAppender(), 0, document.outputSettings());
 document.bodyAppender().append("</html>");
 document.output();
 String expected = "<?xml?>";
 assertEquals(expected, xmlDeclaration.toString());
 } catch (IOException e) {
 // IOException should not be thrown in this test case
 }
 }

 @Test
 public void testXmlDeclarationWithEmptyName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(null, null, false);

 Document document = new Document();
 try {
 document.outputSettings(false);
 xmlDeclaration.outerHtmlHead(document.bodyAppender(), 0, document.outputSettings());
 document.bodyAppender().append("</html>");
 document.output();
 String expected = "<";
 assertEquals(expected, xmlDeclaration.toString().substring(0, 1));
 } catch (IOException e) {
 // IOException should not be thrown in this test case
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testXmlDeclarationWithNullName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(null, null, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testXmlDeclarationWithNullBaseUri() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 }
}