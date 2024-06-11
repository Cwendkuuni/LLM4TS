import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Token;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder builder;
 private Parser parser;
 private Document doc;

 @Before
 public void setUp() {
 builder = new XmlTreeBuilder();
 parser = new Parser(builder);
 doc = builder.parse(new StringReader("<root><tag attr='val'></tag></root>"), "baseUrl");
 }

 @Test
 public void testDefaultSettings() {
 ParseSettings settings = builder.defaultSettings();
 assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
 }

 @Test
 public void testInitialiseParse() throws Exception {
 assertEquals(builder, builder.stack.get(builder.stack.size() - 1));
 assertEquals(Document.OutputSettings.Syntax.XML, doc.outputSettings().syntax());
 }

 @Test
 public void testParse() throws Exception {
 Document parsed = builder.parse(new StringReader("<root><tag attr='val'></tag></root>"), "baseUrl");
 assertEquals(doc, parsed);
 }

 @Test
 public void testParseString() throws Exception {
 Document parsed = builder.parse("<root><tag attr='val'></tag></root>", "baseUrl");
 assertEquals(doc, parsed);
 }

 @Test
 public void testProcessStartTag() throws Exception {
 Token startTagToken = new Token(Token.StartTag.class, "<tag attr='val'>");
 Element tagElement = builder.insert(startTagToken);
 Node expectedNode = new Element("tag", "baseUrl", null);
 assertEquals(expectedNode, tagElement);
 }

 @Test
 public void testProcessEndTag() throws Exception {
 Token endTagToken = new Token(Token.EndTag.class, "</tag>");
 builder.popStackToClose(endTagToken);
 assertEquals(builder.stack.size(), 0);
 }

 @Test(expected = AssertionError.class)
 public void testUnknownTokenType() throws Exception {
 Token unknownToken = new Token(123, "unknownToken");
 builder.process(unknownToken);
 }

 @Test
 public void testProcessComment() throws Exception {
 Token commentToken = new Token(Token.Comment.class, "<!-- comment -->");
 builder.insert(commentToken);
 Node expectedNode = new Comment("comment");
 Node actualNode = doc.childNodes().get(doc.childNodes().size() - 1);
 assertEquals(expectedNode.getClass(), actualNode.getClass());
 assertEquals("comment", actualNode.text());
 }

 @Test
 public void testProcessCharacter() throws Exception {
 Token characterToken = new Token(Token.Character.class, "char");
 builder.insert(characterToken);
 Node expectedNode = new TextNode("char");
 Node actualNode = doc.childNodes().get(doc.childNodes().size() - 1);
 assertEquals(expectedNode.getClass(), actualNode.getClass());
 assertEquals("char", actualNode.text());
 }

 @Test
 public void testProcessDoctype() throws Exception {
 Token doctypeToken = new Token(Token.Doctype.class, "<!DOCTYPE root SYSTEM 'sysId' PUBLIC 'pubId' 'pubSysId'>");
 Node expectedNode = new DocumentType("root", null, null);
 builder.insert(doctypeToken);
 Node actualNode = doc.childNodes().get(0);
 assertEquals(expectedNode.getClass(), actualNode.getClass());
 assertEquals("root", actualNode.nodeName());
 }

 @Test
 public void testPopStackToCloseNullEndTag() {
 Token nullEndTag = new Token(Token.EndTag.class, null);
 builder.popStackToClose(nullEndTag);
 assertEquals(builder.stack.size(), 0);
 }

 @Test
 public void testParseFragment() throws Exception {
 String inputFragment = "<tag attr='val'>content</tag>";
 List<Node> expectedNodes = List.of(new Element("tag", "baseUrl", null), new TextNode("content"));
 List<Node> actualNodes = builder.parseFragment(inputFragment, "baseUrl", parser).subList(0, 2);
 assertEquals(expectedNodes, actualNodes);
 }

 @Test
 public void testParseFragmentWithContext() throws Exception {
 Element context = new Element("root", "baseUrl", null);
 List<Node> expectedNodes = List.of(new Element("tag", "baseUrl", null), new TextNode("content"));
 List<Node> actualNodes = builder.parseFragment(inputFragment, context, "baseUrl", parser).subList(0, 2);
 assertEquals(expectedNodes, actualNodes);
 }

}