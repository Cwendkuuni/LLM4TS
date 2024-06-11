import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XmlDeclarationTest {

 @Test
 public void testXmlDeclaration() {
 String xmlDeclarationString = "<declaration version=\"1.0\" encoding=\"UTF-8\">";
 Document document = Jsoup.parse(xmlDeclarationString);
 XmlDeclaration xmlDeclaration = (XmlDeclaration) document.head();

 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("declaration", xmlDeclaration.name());
 assertEquals("version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
 assertNotNull(xmlDeclaration.toString());

 String invalidXmlDeclarationString = "<declaration version=\"1.0\" encoding=\"UTF-8\" version2=\"2.0\">";
 try {
 Document invalidDocument = Jsoup.parse(invalidXmlDeclarationString);
 XmlDeclaration invalidXmlDeclaration = (XmlDeclaration) invalidDocument.head();
 fail("Expected an IOException to be thrown due to invalid XML declaration");
 } catch (IOException e) {
 // Expected
 }

 String emptyXmlDeclarationString = "<declaration>";
 Document emptyDocument = Jsoup.parse(emptyXmlDeclarationString);
 XmlDeclaration emptyXmlDeclaration = (XmlDeclaration) emptyDocument.head();
 assertNull(emptyXmlDeclaration);
 }

 @Test
 public void testXmlDeclarationOutput() throws IOException {
 Document document = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">" +
 "<xml version=\"1.0\" encoding=\"UTF-8\">" +
 "<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
 "<head>" +
 "<title>Test</title>" +
 "</head>" +
 "<body>" +
 "<p>Hello World!</p>" +
 "</body>" +
 "</html>");
 String expectedXmlDeclaration = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">" +
 "<xml version=\"1.0\" encoding=\"UTF-8\">";
 XmlDeclaration xmlDeclaration = (XmlDeclaration) document.head();
 String actualXmlDeclaration = xmlDeclaration.outerHtml();
 assertEquals(expectedXmlDeclaration, actualXmlDeclaration);
 }
}