import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.ElementHelper;
import org.jsoup.helper.ParserCompatibilityParser;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.XmlTreeBuilder;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document xmlDocument;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlDocument = xmlTreeBuilder.parse(new StringReader("<root><element attr='value'><subElement /><comment>This is a comment</comment></element></root>"), "base-uri");
 }

 @Test
 public void testInitialiseParse() {
 // assert document is created and output settings are set to xml
 assertNotNull(xmlDocument);
 assertEquals(Document.OutputSettings.Syntax.xml, xmlDocument.outputSettings().syntax());
 // assert document is on the stack
 assertEquals(xmlDocument, stackTop());
 }

 @Test
 public void testProcessStartTag() {
 // test root element
 assertEquals("root", xmlDocument.tagName());
 // test element tag
 Element element = xmlDocument.selectFirst("element");
 assertEquals("element", element.tagName());
 // test attributes
 List<Attribute> attributes = element.attributes();
 assertEquals(1, attributes.size());
 Attribute attribute = attributes.get(0);
 assertEquals("attr", attribute.getKey());
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void testProcessEndTag() {
 // test root element is not closed since it's not in the stack
 assertFalse(xmlDocument.outputSettings().isClosed());
 // test element tag is closed
 Element element = xmlDocument.selectFirst("element");
 assertEquals(1, element.childNodes().size()); // there is a comment node
 Element closingElement = xmlDocument.selectFirst("element[1]/..");
 assertTrue(closingElement.hasMetaInformation()); // element has been closed
 }

 @Test
 public void testProcessComment() {
 // test comment node
 Comment comment = xmlDocument.selectFirst("comment");
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testProcessCharacter() {
 // test text node
 TextNode textNode = xmlDocument.selectText("root text()");
 assertEquals("", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 // test document type node
 DocumentType documentType = xmlDocument.documentType();
 assertEquals("root", documentType.name());
 }

 @Test
 public void testInsertNode() {
 // test inserting an element node
 Element element = new Element(new Tag("testTag"));
 xmlTreeBuilder.insertNode(element);
 assertEquals(element, xmlDocument.childNodes().get(0));
 }

 @Test
 public void testInsertComment() {
 // test inserting a comment node
 Comment comment = new Comment("This is a test comment");
 xmlTreeBuilder.insert(new Token.Comment("This is a test comment"));
 assertEquals(comment, xmlDocument.childNodes().get(0));
 }

 @Test
 public void testInsertCharacter() {
 // test inserting a text node
 TextNode textNode = new TextNode("This is a test text node");
 xmlTreeBuilder.insert(new Token.Character('T', "This is a test text node"));
 assertEquals(textNode, xmlDocument.childNodes().get(0));
 }

 @Test
 public void testInsertCData() {
 // test inserting a CDATA node
 CDataNode cdataNode = new CDataNode("<![CDATA[This is a test CDATA node]]>");
 xmlTreeBuilder.insert(new Token.Character('C', "<![CDATA[This is a test CDATA node]]>"));
 assertEquals(cdataNode, xmlDocument.childNodes().get(0));
 }

 @Test
 public void testPopStackToClose() {
 // test closing an element
 Element element = xmlDocument.selectFirst("element");
 assertEquals(2, element.childNodes().size()); // there is a comment and a closing tag
 Element closingElement = xmlDocument.selectFirst("element[1]/..");
 assertTrue(closingElement.hasMetaInformation()); // element has been closed
 // test failing to find a matching tag
 xmlDocument = xmlTreeBuilder.parse(new StringReader("<root><notMatchingTag /></root>"), "base-uri");
 assertFalse(xmlDocument.outputSettings().isClosed()); // root element is not closed
 }

 @Test
 public void testParseFragment() {
 // parse fragment with root element
 List<Node> fragment = xmlTreeBuilder.parseFragment("<root><element attr='value'><subElement /></element></root>", "base-uri", new ParserCompatibilityParser());
 assertEquals(3, fragment.size()); // there is a root node, an element node, and a sub-element node
 // parse fragment with context element
 Element contextElement = xmlDocument.selectFirst("root");
 List<Node> fragmentWithContext = xmlTreeBuilder.parseFragment("<element attr='value'><subElement /></element>", contextElement, "base-uri", new ParserCompatibilityParser());
 assertEquals(2, fragmentWithContext.size()); // there is an element node and a sub-element node
 }

 private Element stackTop() {
 Element stackTop = null;
 for (int pos = stack.size() - 1; pos >= 0; pos--) {
 stackTop = (Element) stack.get(pos);
 }
 return stackTop;
 }
}