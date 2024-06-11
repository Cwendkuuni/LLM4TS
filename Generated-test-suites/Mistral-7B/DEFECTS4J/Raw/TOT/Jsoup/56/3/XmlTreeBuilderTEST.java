import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.XmlTreeBuilder;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 @Test
 public void testParseValidXml() {
 String xmlInput = "<root><child>Hello World</child></root>";
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "http://test.com");
 Element rootElement = xmlDoc.getRootElement();
 Assert.assertEquals("root", rootElement.tagName());
 Element childElement = rootElement.child(0);
 Assert.assertEquals("child", childElement.tagName());
 Assert.assertEquals("Hello World", childElement.text());
 }

 @Test
 public void testParseInvalidXmlWithErrorHandling() {
 String invalidXmlInput = "<root><child></child><!--invalid-->";
 ParseErrorList errors = new ParseErrorList();
 Document xmlDoc = xmlTreeBuilder.parse(invalidXmlInput, "http://test.com", errors, ParseSettings.preserveCase);
 Assert.assertFalse("Document should be empty due to error", xmlDoc.childNodeSize() > 0);
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("Error parsing start tag : --invalid--", errors.getError(0).getMessage());
 }

 @Test
 public void testParseEmptyXml() {
 String emptyXmlInput = "";
 Document xmlDoc = xmlTreeBuilder.parse(emptyXmlInput, "http://test.com");
 Assert.assertNull("Document should be null for empty input", xmlDoc);
 }

 @Test
 public void testParseXmlWithSelfClosingTag() {
 String xmlInput = "<root><img src=\"image.jpg\" /> </root>";
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "http://test.com");
 Element rootElement = xmlDoc.getRootElement();
 Element imgElement = rootElement.child(0);
 Assert.assertEquals("img", imgElement.tagName());
 Assert.assertFalse("Image tag should not have any children", imgElement.childNodeSize() > 0);
 }

 @Test
 public void testParseXmlWithDoctype() {
 String xmlInput = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">\n<html>\n<head>\n<title>The Dude</title>\n</head>\n<body>\n<p>The Dude abides.</p>\n</body>\n</html>";
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "http://test.com");
 DocumentType doctypeNode = xmlDoc.child(0);
 Assert.assertEquals("html", doctypeNode.nodeName());
 Assert.assertNotNull("Public identifier should not be null", doctypeNode.getPublicIdentifier());
 Assert.assertNull("System identifier should be null", doctypeNode.getSystemIdentifier());
 }

 @Test
 public void testParseXmlFragment() {
 String xmlFragment = "<root><child>Hello</child></root>";
 List<Node> fragments = xmlTreeBuilder.parseFragment(xmlFragment, "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Element rootElement = (Element) fragments.get(0);
 Element childElement = (Element) fragments.get(1);
 Assert.assertEquals("root", rootElement.tagName());
 Assert.assertEquals("child", childElement.tagName());
 Assert.assertEquals("Hello", childElement.text());
 }

}