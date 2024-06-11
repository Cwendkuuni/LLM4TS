import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.parser.XmlTreeBuilder;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 document = Jsoup.parse("<root><element/><element><subElement/></element></root>", "", Parser.xmlParser());
 }

 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
 }

 @Test
 public void testParse() {
 String input = "<root><element/><element><subElement/></element></root>";
 document = xmlTreeBuilder.parse(input, "");
 assertEquals("<root><element/><element><subElement/></element></root>", document.html());
 }

 @Test
 public void testInitialiseParse() {
 String input = "<root><element/><element><subElement/></element></root>";
 String baseUri = "";
 ParseErrorList errors = new ParseErrorList();
 ParseSettings settings = ParseSettings.preserveCase();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
 assertEquals(document, xmlTreeBuilder.doc);
 }

 @Test
 public void testProcessStartTag() {
 Token token = new Token(Token.StartTag.class, "<root>", 1, 1, 6);
 Element element = xmlTreeBuilder.insert(token.asStartTag());
 assertEquals("root", element.nodeName());
 }

 @Test
 public void testProcessEndTag() {
 Token token = new Token(Token.EndTag.class, "</root>", 1, 1, 7);
 xmlTreeBuilder.popStackToClose(token.asEndTag());
 assertNull(xmlTreeBuilder.stack.peek());
 }

 @Test
 public void testProcessComment() {
 Token token = new Token(Token.Comment.class, "-- Adjust this comment --", 1, 1, 8);
 Comment comment = (Comment) xmlTreeBuilder.insert(token);
 assertEquals("-- Adjust this comment --", comment.text());
 }

 @Test
 public void testProcessCharacter() {
 Token token = new Token(Token.Character.class, 'a', 1, 1, 9);
 TextNode textNode = (TextNode) xmlTreeBuilder.insert(token);
 assertEquals("a", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 Token token = new Token(Token.Doctype.class, "<!DOCTYPE root SYSTEM \"about:blank\">", 1, 1, 10);
 DocumentType documentTypeNode = (DocumentType) xmlTreeBuilder.insert(token);
 assertEquals("root", documentTypeNode.nodeName());
 }

 @Test(expected = RuntimeException.class)
 public void testValidateFailure() {
 Token token = new Token(123, "", 1, 1, 11);
 xmlTreeBuilder.process(token);
 }

 @Test
 public void testInsertNode() {
 Element element = new Element("element", "", null);
 xmlTreeBuilder.insertNode(element);
 Element root = xmlTreeBuilder.currentElement();
 assertEquals(element, root.child(0));
 }

 @Test
 public void testPopStackToClose_ElementFound() {
 xmlTreeBuilder.stack.add(xmlTreeBuilder.doc);
 xmlTreeBuilder.stack.add(new Element("root", "", null));
 xmlTreeBuilder.stack.add(new Element("element", "", null));
 Token token = new Token(Token.EndTag.class, "</element>", 1, 1, 12);
 xmlTreeBuilder.popStackToClose(token.asEndTag());
 assertEquals("root", xmlTreeBuilder.doc.nodeName());
 assertEquals(1, xmlTreeBuilder.stack.size());
 }

 @Test
 public void testPopStackToClose_ElementNotFound() {
 xmlTreeBuilder.stack.add(xmlTreeBuilder.doc);
 xmlTreeBuilder.stack.add(new Element("root", "", null));
 Token token = new Token(Token.EndTag.class, "</nonExistentElement>", 1, 1, 13);
 xmlTreeBuilder.popStackToClose(token.asEndTag());
 assertEquals("root", xmlTreeBuilder.doc.nodeName());
 assertEquals(1, xmlTreeBuilder.stack.size());
 }

 @Test
 public void testParseFragment() {
 String inputFragment = "<fragment><element/><element/></fragment>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, "", new ParseErrorList(), ParseSettings.preserveCase());
 assertEquals(Arrays.asList(document.getElementsByTag("element").get(0), document.getElementsByTag("element").get(1)), nodes);
 }

 @Test(expected = ParseError.class)
 public void testParseFragment_WithError() {
 String inputFragment = "<fragment><element><!comment";
 List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, "", new ParseErrorList(), ParseSettings.preserveCase());
 assertTrue(nodes.isEmpty());
 }

}