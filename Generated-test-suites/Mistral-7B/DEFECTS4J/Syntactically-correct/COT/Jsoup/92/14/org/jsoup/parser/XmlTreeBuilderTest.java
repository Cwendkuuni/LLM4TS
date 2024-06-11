import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Token;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Assert;
import org.junit.Test;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder builder;
 private Parser parser;
 private ParseSettings settings;
 private Document doc;

 @Before
 public void setup() {
 builder = new XmlTreeBuilder();
 parser = new Parser(builder);
 settings = builder.defaultSettings();
 }

 @Test
 public void testInitialiseParse() {
 builder.initialiseParse(new StringReader(""), "", parser);
 Assert.assertNotNull(builder.doc);
 }

 @Test
 public void testParse() {
 doc = builder.parse(new StringReader("<xml></xml>"), "");
 Assert.assertNotNull(doc);
 Assert.assertEquals(0, doc.body().childNodes().size());
 }

 @Test
 public void testParseWithBaseUri() {
 doc = builder.parse(new StringReader("<xml><a href='test.xml'></xml>"), "");
 Assert.assertNotNull(doc);
 Assert.assertEquals("test.xml", doc.select("a[href]").first().attr("href"));
 }

 @Test
 public void testProcessStartTag() {
 Token startTag = new Token(Token.StartTag.class, "<tag name='test' attr='value'/>");
 builder.insert(startTag);
 Element element = (Element) builder.stack.get(builder.stack.size() - 1);
 Assert.assertEquals("test", element.tagName());
 Assert.assertEquals("value", element.attr("attr"));
 }

 @Test
 public void testProcessEndTag() {
 Token endTag = new Token(Token.EndTag.class, "</tag>");
 builder.popStackToClose(endTag);
 Assert.assertFalse(builder.stack.isEmpty());
 }

 @Test
 public void testProcessComment() {
 Token comment = new Token(Token.Comment.class, "-- This is a comment --");
 builder.insert(comment);
 Assert.assertEquals(1, builder.doc.childNodes().size());
 Assert.assertTrue(builder.doc.childNodes().get(0) instanceof Comment);
 }

 @Test
 public void testProcessCharacter() {
 Token character = new Token(Token.Character.class, "Hello World");
 builder.insert(character);
 Assert.assertEquals(1, builder.doc.childNodes().size());
 Assert.assertTrue(builder.doc.childNodes().get(0) instanceof TextNode);
 }

 @Test
 public void testProcessDoctype() {
 Token doctype = new Token(Token.Doctype.class, "<!DOCTYPE xml [ <!ENTITY foo SYSTEM \"bar.xml\"> ]>");
 builder.insert(doctype);
 Assert.assertEquals(1, builder.doc.childNodes().size());
 Assert.assertTrue(builder.doc.childNodes().get(0) instanceof DocumentType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessUnexpectedTokenType() {
 Token invalidToken = new Token(-1, "Invalid Token");
 builder.process(invalidToken);
 }

 @Test
 public void testParseFragment() {
 List<Node> nodes = builder.parseFragment("<a><b>Hello</b></a>", "", parser);
 Assert.assertEquals(2, nodes.size());
 Assert.assertTrue(nodes.get(0) instanceof Element);
 Assert.assertTrue(nodes.get(1) instanceof TextNode);
 Assert.assertEquals("Hello", nodes.get(1).text());
 }

 @Test
 public void testParseFragmentWithContext() {
 Element context = new Element("body");
 List<Node> nodes = builder.parseFragment("<a><b>Hello</b></a>", context, "", parser);
 Assert.assertEquals(2, nodes.size());
 Assert.assertTrue(nodes.get(0) instanceof Element);
 Assert.assertTrue(nodes.get(1) instanceof TextNode);
 Assert.assertEquals("Hello", nodes.get(1).text());
 }

}