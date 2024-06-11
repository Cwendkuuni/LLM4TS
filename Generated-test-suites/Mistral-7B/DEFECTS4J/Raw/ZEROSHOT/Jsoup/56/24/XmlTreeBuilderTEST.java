import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Token;
import org.jsoup.parser.XmlTreeBuilder;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParse() {
 String xmlString = "<root><element attr=\"value\">Text</element></root>";
 document = xmlTreeBuilder.parse(xmlString, "");
 assertEquals("root", document.tagName());
 Element root = document.getElementByTagName("root");
 assertNotNull(root);
 List<Node> childNodes = root.childNodes();
 assertEquals(1, childNodes.size());
 Element element = (Element) childNodes.get(0);
 assertEquals("element", element.tagName());
 assertEquals("value", element.attribute(0).getValue());
 assertEquals("Text", element.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullInput() {
 xmlTreeBuilder.parse(null, "");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullBaseUri() {
 xmlTreeBuilder.parse("<root/>", null);
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<element attr=\"value\">Text</element>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "", new ParseErrorList(), ParseSettings.defaultSettings());
 assertEquals(1, nodes.size());
 Element element = (Element) nodes.get(0);
 assertEquals("element", element.tagName());
 assertEquals("value", element.attribute(0).getValue());
 assertEquals("Text", element.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithNullInput() {
 xmlTreeBuilder.parseFragment(null, "", new ParseErrorList(), ParseSettings.defaultSettings());
 }

 @Test(expected = ParseError.class)
 public void testParseWithInvalidInput() {
 xmlTreeBuilder = new XmlTreeBuilder();
 String invalidXml = "<root><element><xml:foo xmlns:xml=\"http://example.com\" bar=\"value\"></xml:foo></element></root>";
 document = xmlTreeBuilder.parse(invalidXml, "");
 assertNotNull(document.childNodes());
 assertEquals(2, document.childNodes().size());
 Element root = document.getElementByTagName("root");
 Element element = (Element) document.getElementsByTagName("element").get(0);
 assertEquals("element", element.tagName());
 Element xmlFoo = (Element) document.getElementsByTagName("xml:foo").get(0);
 assertEquals("xml:foo", xmlFoo.tagName());
 assertEquals("http://example.com", xmlFoo.namespaceURI());
 List<Node> namespaceDeclarations = xmlFoo.getElementsByTag("{http://example.com}namespace-prefix:{http://example.com}namespace-uri");
 assertEquals(1, namespaceDeclarations.size());
 Element namespaceDeclaration = (Element) namespaceDeclarations.get(0);
 assertEquals("namespace-prefix", namespaceDeclaration.tagName());
 assertEquals("namespace-uri", namespaceDeclaration.attribute(0).getValue());
 }

 @Test(expected = ParseError.class)
 public void testParseWithCommentError() {
 xmlTreeBuilder = new XmlTreeBuilder();
 String input = "<!-- This is a comment --> <root><element attr=\"value\">Text</element></root>";
 document = xmlTreeBuilder.parse(input, "");
 assertTrue(document.body().childNodes().hasText());
 }

 @Test(expected = ParseError.class)
 public void testParseWithDoctypeError() {
 xmlTreeBuilder = new XmlTreeBuilder();
 String input = "<!DOCTYPE root []>\n<root></root>";
 document = xmlTreeBuilder.parse(input, "");
 assertTrue(document.body().childNodes().hasText());
 }

 @Test(expected = ParseError.class)
 public void testParseWithCharacterError() {
 xmlTreeBuilder = new XmlTreeBuilder();
 String input = "<root>&#65;</root>";
 document = xmlTreeBuilder.parse(input, "");
 assertTrue(document.body().childNodes().hasText());
 }
}