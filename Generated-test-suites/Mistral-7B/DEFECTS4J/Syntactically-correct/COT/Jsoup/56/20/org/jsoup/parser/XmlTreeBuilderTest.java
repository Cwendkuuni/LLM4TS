package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Tokenizer;

import java.util.List;

/**
 * Test class for XmlTreeBuilder
 */
public class XmlTreeBuilderTest {

 private XmlTreeBuilder builder;
 private Tokenizer tokeniser;

 @Before
 public void setUp() {
 builder = new XmlTreeBuilder();
 tokeniser = new Tokenizer("<?xml version=\"1.0\"?>\n<root><tag1 attr1=\"val1\"><tag2 attr2=\"val2\"/><tag3 attr3=\"val3\"/></tag1></root>", builder);
 }

 @Test
 public void testDefaultSettings() {
 ParseSettings settings = builder.defaultSettings();
 assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
 }

 @Test
 public void testParse() {
 Document doc = builder.parse("<root><tag1 attr1=\"val1\"><tag2 attr2=\"val2\"/><tag3 attr3=\"val3\"/></tag1></root>", "http://test.com");
 assertEquals("http://test.com", doc.baseUri());
 assertEquals("root", doc.tagName());

 List<Node> nodes = doc.childNodes();
 assertEquals(3, nodes.size());

 Element root = (Element) nodes.get(0);
 assertEquals("root", root.tagName());

 List<Node> rootChildren = root.childNodes();
 assertEquals(1, rootChildren.size());

 Element tag1 = (Element) rootChildren.get(0);
 assertEquals("tag1", tag1.tagName());

 List<Node> tag1Children = tag1.childNodes();
 assertEquals(3, tag1Children.size());

 Element tag2 = (Element) tag1Children.get(0);
 assertEquals("tag2", tag2.tagName());

 Element tag3 = (Element) tag1Children.get(1);
 assertEquals("tag3", tag3.tagName());
 }

 @Test
 public void testProcessStartTag() {
 Token startTagToken = tokeniser.nextToken();
 assertEquals(StartTag.class, startTagToken.getClass());
 Element el = builder.insert(startTagToken.asStartTag());
 assertEquals("tag1", el.tagName());
 }

 @Test
 public void testProcessEndTag() {
 Token endTagToken = tokeniser.nextToken();
 assertEquals(EndTag.class, endTagToken.getClass());
 builder.popStackToClose(endTagToken.asEndTag());
 assertTrue(builder.stack.isEmpty());
 }

 @Test
 public void testProcessComment() {
 Token commentToken = tokeniser.nextToken();
 assertEquals(Comment.class, commentToken.getClass());
 Comment comment = (Comment) builder.insert(commentToken);
 assertEquals("", comment.getData());
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = tokeniser.nextToken();
 assertEquals(Character.class, characterToken.getClass());
 TextNode textNode = (TextNode) builder.insert(characterToken);
 assertEquals("", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 Token doctypeToken = tokeniser.nextToken();
 assertEquals(Doctype.class, doctypeToken.getClass());
 DocumentType documentTypeNode = (DocumentType) builder.insert(doctypeToken);
 assertEquals("root", documentTypeNode.parentNode().tagName());
 }

 @Test(expected = AssertionError.class)
 public void testInvalidTokenType() {
 Token invalidToken = tokeniser.nextToken();
 Validate.fail("Unexpected token type: " + invalidToken.type);
 }

 @Test
 public void testInsertNode() {
 Element el = new Element("tag", "", null);
 builder.insertNode(el);
 assertEquals(el, builder.currentElement().lastChild());
 }

 @Test
 public void testPopStackWhenTagNotFound() {
 tokeniser.nextToken(); // consume start tag
 tokeniser.nextToken(); // consume tag content
 tokeniser.nextToken(); // consume end tag
 builder.popStackToClose(new Token.EndTag("tag")); // should not throw an exception
 }

 @Test
 public void testParseFragment() {
 String inputFragment = "<root><tag1 attr1=\"val1\"><tag2 attr2=\"val2\"/><tag3 attr3=\"val3\"/></tag1></root>";
 List<Node> nodes = builder.parseFragment(inputFragment, "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase).childNodes();
 assertEquals(3, nodes.size());
 }

}