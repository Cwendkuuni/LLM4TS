import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenCollector;
import org.jsoup.parser.XmlTreeBuilder;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 document = xmlTreeBuilder.parse(new StringReader("<root><element attr=\"value\"><child></child></element></root>"), "http://test.com");
 }

 @Test
 public void givenParseXmlDocument_whenParsingDocument_thenReturnDocumentObject() {
 assertNotNull(document);
 }

 @Test
 public void givenParseXmlDocument_whenParsingDocumentWithRootElement_thenElementIsRootOfDocument() {
 Element rootElement = document.rootElement();
 assertEquals("root", rootElement.tagName());
 }

 @Test
 public void givenParseXmlDocumentWithAttributes_whenParsingAttributes_thenAttributesAreSetCorrectly() {
 Element rootElement = document.rootElement();
 Element element = rootElement.child(0);
 List<Attribute> attributes = element.attributes();

 assertEquals(1, attributes.size());
 Attribute attribute = attributes.get(0);
 assertEquals("attr", attribute.getKey());
 assertEquals("value", attribute.getValue());
 }

 @Test
 public void givenParseXmlDocumentWithSelfClosingTag_whenParsingSelfClosingTag_thenTagIsSelfClosing() {
 Element selfClosingElement = document.selectSingleTag("root > element selfClosingTag");
 assertNotNull(selfClosingElement);
 assertTrue(selfClosingElement.tag().isSelfClosing());
 }

 @Test
 public void givenParseXmlDocumentWithComment_whenParsingComment_thenCommentIsAddedToDocument() {
 List<Node> commentNodes = document.childNodes();
 assertEquals(3, commentNodes.size());

 Comment comment = (Comment) commentNodes.get(2);
 assertEquals("This is a comment", comment.data());
 }

 @Test
 public void givenParseXmlDocumentWithCDataSection_whenParsingCDataSection_thenCDataNodeIsAddedToDocument() {
 List<Node> cdataNodes = document.selectNodes("//[contains(node(), ']]>')]");
 assertEquals(1, cdataNodes.size());

 CDataNode cdataNode = (CDataNode) cdataNodes.get(0);
 assertEquals("This is a CData section", cdataNode.text());
 }

 @Test
 public void givenParseXmlFragment_whenParsingXmlFragment_thenDocumentIsCreatedWithParsedFragmentAsRoot() {
 List<Node> fragmentNodes = xmlTreeBuilder.parseFragment("<element><child>Hello</child></element>", document, "http://test.com").childNodes();

 assertEquals(2, fragmentNodes.size());
 Element element = (Element) fragmentNodes.get(0);
 assertEquals("element", element.tagName());
 TextNode textNode = (TextNode) fragmentNodes.get(1);
 assertEquals("Hello", textNode.text());
 }

 @Test
 public void givenParseXmlFragmentWithContextElement_whenParsingXmlFragment_thenDocumentIsCreatedWithParsedFragmentAsChildOfContextElement() {
 Element contextElement = new Element("root");
 List<Node> fragmentNodes = xmlTreeBuilder.parseFragment("<element><child>Hello</child></element>", contextElement, "http://test.com");

 assertEquals(1, contextElement.childNodes().size());
 Element parsedElement = (Element) contextElement.child(0);
 assertEquals("element", parsedElement.tagName());
 TextNode textNode = (TextNode) parsedElement.childNodes().get(0);
 assertEquals("Hello", textNode.text());
 }
}