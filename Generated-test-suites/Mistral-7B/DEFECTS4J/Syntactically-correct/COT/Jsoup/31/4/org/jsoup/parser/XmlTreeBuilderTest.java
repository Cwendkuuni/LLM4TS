import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;

import java.util.Stack;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

 private Document doc;
 private XmlTreeBuilder xmlTreeBuilder;
 private Parser parser;
 private Tokenizer tokenizer;
 private Stack<Element> stack;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 parser = new Parser();
 tokenizer = parser.tokenizer();
 doc = new Document();
 xmlTreeBuilder.initialiseParse("", "", null);
 stack = xmlTreeBuilder.stack;
 stack.add(doc);
 }

 @Test
 public void testInitialiseParse() {
 assertNotNull(xmlTreeBuilder);
 assertEquals(doc, stack.peek());
 }

 @Test
 public void testProcessStartTag() {
 Token startTagToken = new Token(1, "<tag name='test' attr='test' />", 0, 0, 0, Tag.START_TAG);
 xmlTreeBuilder.process(startTagToken);
 Element expectedElement = new Element("tag", "", Attributes.fromMap(Map.of("attr", "test")));
 assertEquals(expectedElement, stack.peek());
 }

 @Test(expected = AssertionError.class)
 public void testProcessUnexpectedTokenType() {
 Token unexpectedToken = new Token(1, "unexpected_token", 0, 0, 0, -1);
 xmlTreeBuilder.process(unexpectedToken);
 }

 @Test
 public void testProcessEndTag() {
 Token endTagToken = new Token(1, "</tag>", 0, 0, 0, Tag.END_TAG);
 xmlTreeBuilder.process(endTagToken);
 stack.pop();
 assertNotNull(stack.peek());
 }

 @Test
 public void testProcessComment() {
 Token commentToken = new Token(1, "<!-- comment -->", 0, 0, 0, Tag.COMMENT);
 xmlTreeBuilder.process(commentToken);
 assertNotNull(stack.peek());
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token(1, " character ", 0, 0, 0, Tag.CHARACTER);
 xmlTreeBuilder.process(characterToken);
 assertNotNull(stack.peek());
 }

 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Token(1, "<!DOCTYPE doc [ <!ATTLIST doc id ID #REQUIRED > ]>", 0, 0, 0, Tag.DOCTYPE);
 xmlTreeBuilder.process(doctypeToken);
 assertNotNull(stack.peek());
 }

 @Test
 public void testPopStackToClose() {
 Element rootElement = new Element("root", "", Attributes.EMPTY);
 Element innerElement = new Element("inner", "", Attributes.EMPTY);
 Element outerElement = new Element("outer", "", Attributes.EMPTY);
 stack.push(outerElement);
 stack.push(rootElement);
 stack.push(innerElement);
 xmlTreeBuilder.popStackToClose(new Token.EndTag("<inner/>"));
 assertEquals(outerElement, stack.peek());
 }

 @Test(expected = AssertionError.class)
 public void testPopStackToCloseNotFound() {
 xmlTreeBuilder.popStackToClose(new Token.EndTag("<nonExistent/>"));
 }

 @Test
 public void testInsertNode() {
 Element element = new Element("test", "", Attributes.EMPTY);
 xmlTreeBuilder.insertNode(element);
 assertEquals(element, stack.peek().childNodes().get(0));
 }
}