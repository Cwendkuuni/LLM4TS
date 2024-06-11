import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Parser parser;
 private Document document;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 parser = Mockito.spy(new Parser(xmlTreeBuilder));
 document = Mockito.spy(new Document());
 }

 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.preserveCase, settings);
 }

 @Test
 public void testInitialiseParse() {
 xmlTreeBuilder.initialiseParse(new StringReader(""), "baseUri", parser);
 assertEquals(document, xmlTreeBuilder.doc);
 }

 @Test
 public void testParse() {
 String xmlString = "<root><child></child></root>";
 Document parsedDocument = xmlTreeBuilder.parse(new StringReader(xmlString), "baseUri");
 assertNotNull(parsedDocument);
 assertNotNull(parsedDocument.childNodes().get(0));
 assertEquals("root", parsedDocument.childNodes().get(0).nodeName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessNullInput() {
 xmlTreeBuilder.process(null);
 }

 @Test
 public void testProcessStartTag() {
 String xmlString = "<root><child></child></root>";
 xmlTreeBuilder.parse(new StringReader(xmlString), "baseUri");
 Element rootElement = (Element) xmlTreeBuilder.doc.childNodes().get(0);
 Token startTag = new Token.StartTag("child");
 xmlTreeBuilder.process(startTag);
 Element childElement = (Element) rootElement.childNodes().get(0);
 assertEquals("child", childElement.nodeName());
 }

 @Test
 public void testProcessEndTag() {
 String xmlString = "<root><child><grandchild></grandchild></child></root>";
 xmlTreeBuilder.parse(new StringReader(xmlString), "baseUri");
 Element rootElement = (Element) xmlTreeBuilder.doc.childNodes().get(0);
 Element childElement = (Element) rootElement.childNodes().get(0);
 Token endTag = new Token.EndTag("child");
 xmlTreeBuilder.process(endTag);
 assertEquals(1, rootElement.childNodes().size());
 }

 @Test
 public void testProcessComment() {
 String xmlString = "<!-- This is a comment -->";
 xmlTreeBuilder.parse(new StringReader(xmlString), "baseUri");
 Comment comment = (Comment) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("This is a comment", comment.getData());
 }

 @Test
 public void testProcessCharacter() {
 String xmlString = "<root>&#x304;extit</root>";
 xmlTreeBuilder.parse(new StringReader(xmlString), "baseUri");
 TextNode textNode = (TextNode) xmlTreeBuilder.doc.childNodes().get(1);
 assertEquals("extit\u0304", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 String xmlString = "<!DOCTYPE root SYSTEM \"src/test/resources/test.dtd\">";
 xmlTreeBuilder.parse(new StringReader(xmlString), "baseUri");
 DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("root", doctypeNode.nodeName());
 }

 @Test
 public void testPopStackToClose() {
 String xmlString = "<root><child><grandchild></grandchild></child></root>";
 xmlTreeBuilder.parse(new StringReader(xmlString), "baseUri");
 Element rootElement = (Element) xmlTreeBuilder.doc.childNodes().get(0);
 Element childElement = (Element) rootElement.childNodes().get(0);
 Element grandchildElement = (Element) childElement.childNodes().get(0);
 Token endTag = new Token.EndTag("grandchild");
 xmlTreeBuilder.popStackToClose(endTag);
 assertEquals(1, childElement.childNodes().size());
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<child><grandchild></grandchild></child>";
 List<Node> parsedNodes = xmlTreeBuilder.parseFragment(xmlFragment, document, "baseUri", parser);
 assertEquals(2, parsedNodes.size());
 }
}