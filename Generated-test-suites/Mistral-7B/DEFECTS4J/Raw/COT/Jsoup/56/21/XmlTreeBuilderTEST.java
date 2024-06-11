import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 // Test method for defaultSettings()
 @Test
 public void testDefaultSettings() {
 ParseSettings defaultSettings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.PreserveCase.class, defaultSettings.getClass());
 }

 // Test method for parse()
 @Test
 public void testParse() {
 String xmlInput = "<root><child>Hello World</child></root>";
 Document xmlDocument = xmlTreeBuilder.parse(xmlInput, "");
 assertNotNull(xmlDocument);
 assertNotNull(xmlDocument.childNodes());
 assertNotNull(xmlDocument.childNodes().get(0));
 assertEquals("root", xmlDocument.tagName());
 }

 // Test method for initialiseParse()
 @Test(expected = IllegalArgumentException.class)
 public void testInitialiseParseInvalidInput() {
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", null, null);
 }

 @Test
 public void testInitialiseParseValid() {
 String xmlInput = "<root><child>Hello World</child></root>";
 xmlTreeBuilder.initialiseParse(xmlInput, "", null, null);
 assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.peek());
 }

 // Test method for process()
 @Test
 public void testProcessStartTag() {
 Token startTagToken = new Token(Token.StartTag.class, "<root>", 1, 1, null);
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", null, null);
 xmlTreeBuilder.tokeniser = new Tokenizer();
 xmlTreeBuilder.tokeniser.add(startTagToken);
 xmlTreeBuilder.process(startTagToken);
 assertEquals(new Element("root", "", null), xmlTreeBuilder.stack.peek());
 }

 @Test
 public void testProcessEndTag() {
 Token endTagToken = new Token(Token.EndTag.class, "</root>", 1, 1, null);
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", null, null);
 xmlTreeBuilder.tokeniser = new Tokenizer();
 xmlTreeBuilder.tokeniser.add(new Token(Token.StartTag.class, "<root>", 1, 1, null));
 xmlTreeBuilder.tokeniser.add(endTagToken);
 xmlTreeBuilder.process(endTagToken);
 assertEquals(null, xmlTreeBuilder.stack.peek());
 }

 // Test method for insertNode()
 @Test
 public void testInsertNode() {
 Element rootElement = new Element("root", "", null);
 Node nodeToInsert = new TextNode("Hello World", "");
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", null, null);
 xmlTreeBuilder.currentElement = rootElement;
 xmlTreeBuilder.insertNode(nodeToInsert);
 assertEquals(nodeToInsert, rootElement.childNodes().get(0));
 }

 // Test method for insert() - StartTag
 @Test
 public void testInsertStartTag() {
 Token startTagToken = new Token(Token.StartTag.class, "<root>", 1, 1, null);
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", null, null);
 xmlTreeBuilder.tokeniser = new Tokenizer();
 xmlTreeBuilder.tokeniser.add(startTagToken);
 xmlTreeBuilder.process(startTagToken);
 Element rootElement = (Element) xmlTreeBuilder.stack.peek();
 assertEquals("root", rootElement.tagName());
 }

 // Test method for insert() - Comment
 @Test
 public void testInsertComment() {
 Token commentToken = new Token(Token.Comment.class, "-- This is a comment --", 1, 1, null);
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", null, null);
 xmlTreeBuilder.tokeniser = new Tokenizer();
 xmlTreeBuilder.tokeniser.add(commentToken);
 xmlTreeBuilder.process(commentToken);
 assertNotNull(xmlTreeBuilder.stack.peek());
 }

 // Test method for insert() - Character
 @Test
 public void testInsertCharacter() {
 Token characterToken = new Token(Token.Character.class, "H", 1, 1, null);
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", null, null);
 xmlTreeBuilder.tokeniser = new Tokenizer();
 xmlTreeBuilder.tokeniser.add(characterToken);
 xmlTreeBuilder.process(characterToken);
 assertEquals("H", xmlTreeBuilder.stack.peek().childNodes().get(0).text());
 }

 // Test method for insert() - Doctype
 @Test
 public void testInsertDoctype() {
 Token doctypeToken = new Token(Token.Doctype.class, "<!DOCTYPE html SYSTEM \"about:legacy-compat\">", 1, 1, null);
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", null, null);
 xmlTreeBuilder.tokeniser = new Tokenizer();
 xmlTreeBuilder.tokeniser.add(doctypeToken);
 xmlTreeBuilder.process(doctypeToken);
 DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.stack.peek();
 assertEquals("html", doctypeNode.nodeName());
 }

 // Test method for popStackToClose()
 @Test
 public void testPopStackToClose() {
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", null, null);
 xmlTreeBuilder.tokeniser = new Tokenizer();
 xmlTreeBuilder.tokeniser.add(new Token(Token.StartTag.class, "<root>", 1, 1, null));
 xmlTreeBuilder.tokeniser.add(new Token(Token.EndTag.class, "</root>", 1, 1, null));
 xmlTreeBuilder.process(new Token(Token.EndTag.class, "</root>", 1, 1, null));
 assertEquals(null, xmlTreeBuilder.stack.peek());
 }

 // Test method for parseFragment()
 @Test
 public void testParseFragment() {
 String xmlFragment = "<root><child>Hello World</child></root>";
 List<Node> fragmentNodes = xmlTreeBuilder.parseFragment(xmlFragment, "", null, null);
 assertNotNull(fragmentNodes);
 assertNotNull(fragmentNodes.get(0));
 assertEquals("root", fragmentNodes.get(0).tagName());
 }

}