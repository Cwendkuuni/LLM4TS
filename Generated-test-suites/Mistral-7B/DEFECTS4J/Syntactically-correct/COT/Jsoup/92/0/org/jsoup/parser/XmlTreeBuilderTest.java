import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Tokenizer;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;
 private ParseSettings parseSettings;
 private Parser parser;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 parseSettings = xmlTreeBuilder.defaultSettings();
 parser = new Parser(xmlTreeBuilder);
 }

 @Test
 public void testInitialiseParse() {
 xmlTreeBuilder.initialiseParse(new StringReader(""), "", parser);
 assertEquals(xmlTreeBuilder.doc, doc);
 }

 @Test
 public void testParseDocument() {
 String testXml = "<root><child>Hello</child></root>";
 doc = xmlTreeBuilder.parse(new StringReader(testXml), "");
 assertEquals("<root><child>Hello</child></root>", doc.html());
 }

 @Test
 public void testParseString() {
 String testXml = "<root><child>Hello</child></root>";
 doc = xmlTreeBuilder.parse(testXml, "");
 assertEquals("<root><child>Hello</child></root>", doc.html());
 }

 @Test
 public void testProcessStartTag() {
 Token startTagToken = new Token(Token.Type.StartTag, "<tag name='test'>");
 Element expectedElement = new Element("tag", null, null);
 xmlTreeBuilder.insert(startTagToken);
 assertEquals(expectedElement, currentElement());
 }

 @Test
 public void testProcessEndTag() {
 Token endTagToken = new Token(Token.Type.EndTag, "</tag>");
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertEquals(null, currentElement());
 }

 @Test
 public void testProcessComment() {
 Token commentToken = new Token(Token.Type.Comment, "<!-- comment -->");
 Comment expectedComment = new Comment("comment");
 xmlTreeBuilder.insert(commentToken);
 assertEquals(expectedComment, currentElement());
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token(Token.Type.Character, "test");
 TextNode expectedTextNode = new TextNode("test");
 xmlTreeBuilder.insert(characterToken);
 assertEquals(expectedTextNode, currentElement());
 }

 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Token(Token.Type.Doctype, "<!DOCTYPE html SYSTEM \"about:legacy-compat\">");
 DocumentType expectedDocumentType = new DocumentType("html", null, null);
 xmlTreeBuilder.insert(doctypeToken);
 assertEquals(expectedDocumentType, currentElement());
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 Token unexpectedToken = new Token(Token.Type.Unknown, "unexpectedToken");
 xmlTreeBuilder.process(unexpectedToken);
 }

 @Test
 public void testInsertNode() {
 Node nodeToInsert = new TextNode("test");
 xmlTreeBuilder.insertNode(nodeToInsert);
 assertEquals(currentElement().childNodes().get(0), nodeToInsert);
 }

 @Test
 public void testPopStackToClose() {
 Element openElement = new Element("openTag");
 stack.add(openElement);
 xmlTreeBuilder.popStackToClose(new Token(Token.Type.EndTag, "</openTag>"));
 assertEquals(null, currentElement());
 }

 @Test
 public void testParseFragment() {
 String testFragment = "<fragment><child>test</child></fragment>";
 List<Node> expectedNodes = doc.childNodes();
 List<Node> nodes = xmlTreeBuilder.parseFragment(testFragment, "", "");
 assertEquals(expectedNodes.size(), nodes.size());
 for (int i = 0; i < expectedNodes.size(); i++) {
 Node expectedNode = expectedNodes.get(i);
 Node actualNode = nodes.get(i);
 assertEquals(expectedNode.nodeName(), actualNode.nodeName());
 assertEquals(expectedNode.html(), actualNode.html());
 }
 }

 @Test
 public void testParseFragmentWithContext() {
 String testFragment = "<fragment><child>test</child></fragment>";
 Element context = new Element("context");
 List<Node> expectedNodes = doc.childNodes();
 List<Node> nodes = xmlTreeBuilder.parseFragment(testFragment, context, "", "");
 assertEquals(expectedNodes.size(), nodes.size());
 for (int i = 0; i < expectedNodes.size(); i++) {
 Node expectedNode = expectedNodes.get(i);
 Node actualNode = nodes.get(i);
 assertEquals(expectedNode.nodeName(), actualNode.nodeName());
 assertEquals(expectedNode.html(), actualNode.html());
 }
 }

 private Element currentElement() {
 return (Element) stack.get(stack.size() - 1);
 }
}