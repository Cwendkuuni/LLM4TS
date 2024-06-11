import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XmlDeclarationTest {

 @Test
 public void testXmlDeclarationWithValidAttributes() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "", true);
 xmlDeclaration.attributes().put("version", "1.0");
 xmlDeclaration.attributes().put("encoding", "UTF-8");

 Document document = new Document();
 Element root = document.createElement("#document");
 root.appendChild(xmlDeclaration);

 String expectedXml = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
         "<!--\n" +
         "Created with JSoup (http://jsoup.org)\n" +
         "-->\n" +
         "<xml version=\"1.0\" encoding=\"UTF-8\">";

 xmlDeclaration.outerHtmlHead(document.body().appendText(""), 0, null);
 root.outerHtml(document.body().appendText("\n"));
 xmlDeclaration.outerHtmlTail(document.body().appendText("\n"), 0, null);

 String actualXml = Jsoup.parse(document.body().html()).toString();

 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testXmlDeclarationWithoutAttributes() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "", true);

 Document document = new Document();
 Element root = document.createElement("#document");
 root.appendChild(xmlDeclaration);

 xmlDeclaration.outerHtmlHead(document.body().appendText(""), 0, null);
 root.outerHtml(document.body().appendText("\n"));
 xmlDeclaration.outerHtmlTail(document.body().appendText("\n"), 0, null);

 String expectedXml = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
         "<!--\n" +
         "Created with JSoup (http://jsoup.org)\n" +
         "-->\n" +
         "<xml>";

 String actualXml = Jsoup.parse(document.body().html()).toString();

 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testXmlDeclarationWithInvalidName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("abc", "", true);

 Document document = new Document();
 Element root = document.createElement("#document");
 root.appendChild(xmlDeclaration);

 xmlDeclaration.outerHtmlHead(document.body().appendText(""), 0, null);
 root.outerHtml(document.body().appendText("\n"));
 xmlDeclaration.outerHtmlTail(document.body().appendText("\n"), 0, null);

 String expectedXml = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
         "<!--\n" +
         "Created with JSoup (http://jsoup.org)\n" +
         "-->\n" +
         "<abc>"; // Throws an exception during Jsoup.parse() due to "<abc>" being an invalid XML tag

 // This test case checks if an exception is thrown
 try {
 String actualXml = Jsoup.parse(document.body().html()).toString();
 } catch (IllegalArgumentException e) {
 // Test passes if an IllegalArgumentException is thrown
 return;
 }

 fail("Expected an IllegalArgumentException to be thrown");
 }

 @Test
 public void testXmlDeclarationWithEmptyName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("", "", true);

 Document document = new Document();
 Element root = document.createElement("#document");
 root.appendChild(xmlDeclaration);

 xmlDeclaration.outerHtmlHead(document.body().appendText(""), 0, null);
 root.outerHtml(document.body().appendText("\n"));
 xmlDeclaration.outerHtmlTail(document.body().appendText("\n"), 0, null);

 String expectedXml = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
         "<!--\n" +
         "Created with JSoup (http://jsoup.org)\n" +
         "-->\n" +
         "<>"; // Throws an exception during Jsoup.parse() due to "<>" being an empty tag

 // This test case checks if an exception is thrown
 try {
 String actualXml = Jsoup.parse(document.body().html()).toString();
 } catch (IllegalArgumentException e) {
 // Test passes if an IllegalArgumentException is thrown
 return;
 }

 fail("Expected an IllegalArgumentException to be thrown");
 }

 @Test
 public void testXmlDeclarationWithNullName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(null, "", true);

 Document document = new Document();
 Element root = document.createElement("#document");
 root.appendChild(xmlDeclaration);

 xmlDeclaration.outerHtmlHead(document.body().appendText(""), 0, null);
 root.outerHtml(document.body().appendText("\n"));
 xmlDeclaration.outerHtmlTail(document.body().appendText("\n"), 0, null);

 String expectedXml = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
         "<!--\n" +
         "Created with JSoup (http://jsoup.org)\n" +
         "-->\n" +
         "<xml>"; // Throws an exception during Jsoup.parse() due to "<xml>" being an invalid start tag without a name

 // This test case checks if an exception is thrown
 try {
 String actualXml = Jsoup.parse(document.body().html()).toString();
 } catch (IllegalArgumentException e) {
 // Test passes if an IllegalArgumentException is thrown
 return;
 }

 fail("Expected an IllegalArgumentException to be thrown");
 }
}