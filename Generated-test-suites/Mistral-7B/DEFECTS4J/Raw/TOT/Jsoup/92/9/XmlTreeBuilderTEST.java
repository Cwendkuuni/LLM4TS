import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 @Test
 public void testParseValidXml() {
 String xmlData = "<root><element attr='value'/></root>";
 Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlData), "http://test.com");
 Element root = xmlDoc.rootElement();
 assertEquals("root", root.tagName());
 Element element = root.child(0);
 assertEquals("element", element.tagName());
 assertEquals("value", element.attr("attr"));
 }

 @Test
 public void testParseSelfClosingTag() {
 String xmlData = "<img src='image.jpg'/>";
 Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlData), "http://test.com");
 Element img = xmlDoc.selectFirst("img");
 assertTrue(img.isSelfClosing());
 }

 @Test
 public void testParseComment() {
 String xmlData = "<!-- This is a comment -->";
 Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlData), "http://test.com");
 Element comment = xmlDoc.selectFirst("--comment");
 assertNotNull(comment);
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseCDataSection() {
 String xmlData = "<![CDATA[ This is some CDATA ]]>";
 Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlData), "http://test.com");
 Element cdata = xmlDoc.selectFirst("![CDATA[ This is some CDATA ]]");
 assertNotNull(cdata);
 assertEquals("This is some CDATA", cdata.text());
 }

 @Test
 public void testParseDoctype() {
 String xmlData = "<!DOCTYPE html SYSTEM 'about:legacy-compat'>\r\n<html>\r\n<head>\r\n<title>Title</title>\r\n</head>\r\n<body>\r\n<p>Body text</p>\r\n</body>\r\n</html>";
 Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlData), "http://test.com");
 DocumentType doctype = xmlDoc.doctype();
 assertNotNull(doctype);
 assertEquals("html", doctype.name());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidXml() {
 String xmlData = "<html<body>"; // missing end tag
 Document xmlDoc = null;
 try {
 xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlData), "http://test.com");
 } catch (Exception e) {
 // expected error
 }
 assertNull(xmlDoc);
 }
}