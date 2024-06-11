import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testInitialiseParse() {
 String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n" +
 " <root>\n" +
 " <element>\n" +
 " <subElement/>\n" +
 " </element>\n" +
 " </root>\n";
 String baseUri = "http://example.com/";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);
 assertTrue(xmlTreeBuilder.doc.hasDocType());
 assertEquals("xml", xmlTreeBuilder.doc.doctype().getName());
 assertEquals("http://example.com/", xmlTreeBuilder.doc.baseUri());
 assertEquals(3, xmlTreeBuilder.doc.childNodes().size());
 }

 @Test(expected = AssertionError.class)
 public void testInvalidTokenType() {
 String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n" +
 " <root>\n" +
 " <element>\n" +
 " <subElement>\n" + // missing '>' for tag
 " </subElement>\n" +
 " </element>\n" +
 " </root>\n";
 String baseUri = "http://example.com/";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);
 fail("Should have thrown an AssertionError with unexpected token type");
 }

 @Test
 public void testProcessStartTag() {
 String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n" +
 " <root>\n" +
 " <element>\n" +
 " <subElement/>\n" +
 " </element>\n" +
 " </root>\n";
 String baseUri = "http://example.com/";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);
 xmlTreeBuilder.runParser();
 Element root = (Element) xmlTreeBuilder.doc.childNodes().get(0);
 Element element = (Element) root.childNodes().get(0);
 assertEquals("root", root.nodeName());
 assertEquals("element", element.nodeName());
 assertEquals(1, element.childNodes().size());
 }

 @Test
 public void testProcessSelfClosingTag() {
 String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n" +
 " <root>\n" +
 " <element>\n" +
 " <subElement/>\n" +
 " </element>\n" +
 " </root>\n";
 String baseUri = "http://example.com/";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);
 xmlTreeBuilder.runParser();
 Element root = (Element) xmlTreeBuilder.doc.childNodes().get(0);
 Element element = (Element) root.childNodes().get(0);
 assertEquals("root", root.nodeName());
 assertEquals("element", element.nodeName());
 assertEquals(1, element.childNodes().size());
 assertTrue(element.getTag().isSelfClosing());
 }

 @Test
 public void testProcessEndTag() {
 String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n" +
 " <root>\n" +
 " <element>\n" +
 " <subElement>\n" +
 " <anotherElement/>\n" +
 " </subElement>\n" +
 " </element>\n" +
 " </root>\n";
 String baseUri = "http://example.com/";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);
 xmlTreeBuilder.runParser();
 Element root = (Element) xmlTreeBuilder.doc.childNodes().get(0);
 Element element = (Element) root.childNodes().get(0);
 Element subElement = (Element) element.childNodes().get(0);
 assertEquals("root", root.nodeName());
 assertEquals("element", element.nodeName());
 assertEquals("subElement", subElement.nodeName());
 assertEquals(0, subElement.childNodes().size());
 assertEquals("anotherElement", element.childNodes().get(1).nodeName());
 }

 @Test
 public void testProcessComment() {
 String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n" +
 " <!-- This is a comment -->\n" +
 " <root>\n" +
 " <element>\n" +
 " <subElement>\n" +
 " <anotherElement/>\n" +
 " </subElement>\n" +
 " </element>\n" +
 " </root>\n";
 String baseUri = "http://example.com/";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);
 xmlTreeBuilder.runParser();
 Element root = (Element) xmlTreeBuilder.doc.childNodes().get(0);
 Element element = (Element) root.childNodes().get(0);
 Element subElement = (Element) element.childNodes().get(0);
 Node comment = subElement.childNodes().get(0);
 assertEquals("root", root.nodeName());
 assertEquals("element", element.nodeName());
 assertEquals("subElement", subElement.nodeName());
 assertTrue(comment instanceof Comment);
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testProcessCharacter() {
 String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n" +
 " <root>\n" +
 " <element>\n" +
 " <subElement>\n" +
 " <anotherElement>Hello World</anotherElement>\n" +
 " </subElement>\n" +
 " </element>\n" +
 " </root>\n";
 String baseUri = "http://example.com/";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);
 xmlTreeBuilder.runParser();
 Element root = (Element) xmlTreeBuilder.doc.childNodes().get(0);
 Element element = (Element) root.childNodes().get(0);
 Element subElement = (Element) element.childNodes().get(0);
 Element anotherElement = (Element) subElement.childNodes().get(0);
 Text textNode = (Text) anotherElement.childNodes().get(0);
 assertEquals("root", root.nodeName());
 assertEquals("element", element.nodeName());
 assertEquals("subElement", subElement.nodeName());
 assertEquals("anotherElement", anotherElement.nodeName());
 assertEquals("Hello World", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 String input = "<!DOCTYPE xml SYSTEM \"xml.dtd\">\n" +
 " <root>\n" +
 " <element>\n" +
 " <subElement>\n" +
 " <anotherElement/>\n" +
 " </subElement>\n" +
 " </element>\n" +
 " </root>\n";
 String baseUri = "http://example.com/";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);
 xmlTreeBuilder.runParser();
 DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("xml", doctypeNode.getName());
 assertEquals("xml.dtd", doctypeNode.getSystemIdentifier());
 assertEquals("http://example.com/", doctypeNode.baseUri());
 }

 @Test
 public void testParseFragment() {
 String input = "<root>\n" +
 " <element>\n" +
 " <subElement/>\n" +
 " </element>\n" +
 " </root>";
 String baseUri = "http://example.com/";
 ParseErrorList errors = new ParseErrorList();
 List<Node> parsedNodes = xmlTreeBuilder.parseFragment(input, baseUri, errors);
 assertEquals(3, parsedNodes.size());
 Element root = (Element) parsedNodes.get(0);
 Element element = (Element) root.childNodes().get(0);
 assertEquals("root", root.nodeName());
 assertEquals("element", element.nodeName());
 assertEquals(1, element.childNodes().size());
 }

 @Test
 public void testParseFragmentWithDeclaration() {
 String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
 " <root>\n" +
 " <element>\n" +
 " <subElement/>\n" +
 " </element>\n" +
 " </root>";
 String baseUri = "http://example.com/";
 ParseErrorList errors = new ParseErrorList();
 List<Node> parsedNodes = xmlTreeBuilder.parseFragment(input, baseUri, errors);
 assertEquals(4, parsedNodes.size());
 Document doc = (Document) parsedNodes.get(0);
 assertEquals("xml", doc.doctype().getName());
 assertEquals("UTF-8", doc.doctype().getPublicIdentifier());
 Element root = (Element) parsedNodes.get(1);
 Element element = (Element) root.childNodes().get(0);
 assertEquals("root", root.nodeName());
 assertEquals("element", element.nodeName());
 assertEquals(1, element.childNodes().size());
 }
}