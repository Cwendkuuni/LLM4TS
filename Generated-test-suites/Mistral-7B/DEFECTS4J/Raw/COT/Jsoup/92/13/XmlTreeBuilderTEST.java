import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.helper.Validate;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Parser parser;

 @BeforeEach
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 parser = new Parser(xmlTreeBuilder);
 }

 // Basic test cases for public methods

 @Test
 public void testDefaultSettings() {
 Assertions.assertEquals(ParseSettings.preserveCase, xmlTreeBuilder.defaultSettings());
 }

 @Test
 public void testInitialiseParse() {
 Document document = xmlTreeBuilder.parse(new StringReader(""), "", parser);
 Assertions.assertNotNull(document);
 }

 @Test
 public void testParse() {
 String xmlString = "<root><child></child></root>";
 Document document = xmlTreeBuilder.parse(new StringReader(xmlString), "", parser);
 Assertions.assertNotNull(document);
 Assertions.assertEquals("root", document.tagName());
 }

 @Test
 public void testProcess() {
 // Basic test for token processing
 String xmlString = "<root><child></child></root>";
 StringReader input = new StringReader(xmlString);
 Document document = xmlTreeBuilder.parse(input, "", parser);
 // Check that the root element is present
 Assertions.assertEquals("root", document.tagName());
 // Check that the child element is present
 Element childElement = findElement(document, "child");
 Assertions.assertNotNull(childElement);
 }

 // Edge cases and exception handling test cases

 @Test
 public void testInvalidInput() {
 StringReader input = new StringReader("Invalid XML input");
 Assertions.assertThrows(RuntimeException.class, () -> xmlTreeBuilder.parse(input, "", parser));
 }

 @Test
 public void testUnknownTag() {
 String xmlString = "<unknownTag></unknownTag>";
 StringReader input = new StringReader(xmlString);
 Assertions.assertThrows(Validate.MalformedXmlException.class, () -> xmlTreeBuilder.parse(input, "", parser));
 }

 @Test
 public void testSelfClosingTag() {
 String xmlString = "<selfClosingTag />";
 StringReader input = new StringReader(xmlString);
 Document document = xmlTreeBuilder.parse(input, "", parser);
 Element rootElement = document.rootElement();
 Element selfClosingElement = rootElement.getChild(0);
 Assertions.assertEquals("selfClosingTag", selfClosingElement.tagName());
 Assertions.assertFalse(selfClosingElement.hasText());
 }

 @Test
 public void testNestedTags() {
 String xmlString = "<root><child1><child2></child2></child1></root>";
 StringReader input = new StringReader(xmlString);
 Document document = xmlTreeBuilder.parse(input, "", parser);
 Element rootElement = document.rootElement();
 Element child1Element = rootElement.getChild(0);
 Element child2Element = child1Element.getChild(0);
 Assertions.assertEquals("child2", child2Element.tagName());
 }

 @Test
 public void testEmptyDocument() {
 Document document = xmlTreeBuilder.parse(new StringReader(""), "", parser);
 Assertions.assertNotNull(document);
 Assertions.assertTrue(document.isEmpty());
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<root><child></child></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "", parser);
 Document document = (Document) nodes.get(0);
 Assertions.assertEquals("root", document.tagName());
 Element childElement = (Element) document.getChild(0);
 Assertions.assertEquals("child", childElement.tagName());
 }

 // Helper method to find an element in a document by tag name
 private Element findElement(Document document, String tagName) {
 List<Node> nodes = document.getElementsByTag(tagName);
 if (!nodes.isEmpty()) {
 return (Element) nodes.get(0);
 } else {
 throw new RuntimeException("Element not found: " + tagName);
 }
 }

}