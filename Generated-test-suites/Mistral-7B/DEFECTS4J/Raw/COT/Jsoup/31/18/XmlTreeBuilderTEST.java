import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Tokenizer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

 private Document doc;
 private Tokenizer tokeniser;

 @Before
 public void setUp() {
 doc = new Document();
 tokeniser = new Tokenizer();
 }

 // Test for all public methods in XmlTreeBuilder

 // Test method: initialiseParse
 @Test
 public void testInitialiseParse() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 builder.initialiseParse("input", "baseUri", new ParseErrorList());
 assertEquals(doc, builder.stack.peek());
 }

 // Test method: process
 @Test
 public void testProcess_StartTag() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 builder.stack = new Stack<Element>();
 builder.currentElement = doc;
 Token startTag = new Token(StartTag.class, "div", null, null, null, null, null);
 builder.process(startTag);
 Element element = new Element("div", "baseUri", null);
 assertEquals(element, builder.stack.peek());
 }

 @Test
 public void testProcess_EndTag() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 builder.stack = new Stack<>();
 builder.stack.add(doc);
 builder.stack.add(new Element("div", "baseUri", null));
 Token endTag = new Token(EndTag.class, "div");
 builder.process(endTag);
 assertEquals(doc, builder.stack.peek());
 }

 @Test
 public void testProcess_Comment() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 builder.stack = new Stack<>();
 builder.stack.add(doc);
 Token comment = new Token(Comment.class, "This is a comment", null);
 builder.process(comment);
 assertNotNull(builder.stack.peek().childNodes());
 assertEquals(1, builder.stack.peek().childNodes().size());
 }

 @Test
 public void testProcess_Character() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 builder.stack = new Stack<>();
 builder.stack.add(doc);
 Token character = new Token(Character.class, 'A');
 builder.process(character);
 assertNotNull(builder.stack.peek().childNodes());
 assertEquals(1, builder.stack.peek().childNodes().size());
 }

 @Test
 public void testProcess_Doctype() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 builder.stack = new Stack<>();
 builder.stack.add(doc);
 Token doctype = new Token(Doctype.class, "html", "PublicID", "SystemID", null);
 builder.process(doctype);
 assertEquals(doc.doctype(), builder.stack.peek());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcess_UnexpectedTokenType() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Token unexpectedToken = new Token(123, null, null, null, null, null, null);
 builder.process(unexpectedToken);
 }

 // Test method: insertNode
 @Test
 public void testInsertNode() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Element element = new Element("div", "baseUri", null);
 builder.insertNode(element);
 assertEquals(element, builder.stack.peek());
 }

 // Test method: insert
 @Test
 public void testInsert_StartTag() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Token startTag = new Token(StartTag.class, "div", null, null, null, null, null);
 Element element = builder.insert(startTag);
 assertEquals(element, builder.stack.peek());
 }

 @Test
 public void testInsert_Comment() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Token comment = new Token(Comment.class, "This is a comment", null);
 builder.insert(comment);
 assertNotNull(builder.stack.peek().childNodes());
 assertEquals(1, builder.stack.peek().childNodes().size());
 }

 @Test
 public void testInsert_Character() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Token character = new Token(Character.class, 'A');
 builder.insert(character);
 assertNotNull(builder.stack.peek().childNodes());
 assertEquals(1, builder.stack.peek().childNodes().size());
 }

 @Test
 public void testInsert_Doctype() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Token doctype = new Token(Doctype.class, "html", "PublicID", "SystemID", null);
 builder.insert(doctype);
 assertEquals(builder.stack.peek().doctype(), doctype);
 }

 // Test method: popStackToClose
 @Test
 public void testPopStackToClose_Found() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 builder.stack = new Stack<>();
 builder.stack.add(doc);
 builder.stack.add(new Element("div", "baseUri", null));
 builder.stack.add(new Element("p", "baseUri", null));
 Token endTag = new Token(EndTag.class, "p");
 builder.popStackToClose(endTag);
 assertEquals(doc, builder.stack.peek());
 }

 @Test
 public void testPopStackToClose_NotFound() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 builder.stack = new Stack<>();
 builder.stack.add(doc);
 builder.stack.add(new Element("div", "baseUri", null));
 Token endTag = new Token(EndTag.class, "p");
 builder.popStackToClose(endTag);
 assertEquals(builder.stack.peek(), doc);
 }
}