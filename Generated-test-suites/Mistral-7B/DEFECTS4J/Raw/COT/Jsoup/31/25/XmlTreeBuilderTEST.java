import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DocumentType;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.attributes.Attributes;

import java.util.Stack;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Stack<Element> stack;
 private Document doc;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 stack = xmlTreeBuilder.stack;
 doc = xmlTreeBuilder.doc;
 }

 // Test for initialiseParse method
 @Test
 public void testInitialiseParse() {
 xmlTreeBuilder.initialiseParse("xmlData", "baseUri", new ParseErrorList());
 assertEquals("xmlData", xmlTreeBuilder.input);
 assertEquals("baseUri", xmlTreeBuilder.baseUri);
 assertNotNull(xmlTreeBuilder.errors);
 assertNotNull(stack);
 assertNotNull(doc);
 }

 // Test for process method with StartTag
 @Test
 public void testProcessStartTag() {
 Token startTagToken = new Token(Token.TokenType.StartTag, "<tag>", 0, 0, 0);
 xmlTreeBuilder.tokeniser = new Tokeniser();
 xmlTreeBuilder.tokeniser.setInput("testData");
 xmlTreeBuilder.tokeniser.next();
 xmlTreeBuilder.process(startTagToken);

 Element el = new Element("tag", "baseUri", null);
 assertEquals(el, xmlTreeBuilder.currentElement());
 assertTrue(stack.contains(el));
 }

 // Test for process method with EndTag
 @Test
 public void testProcessEndTag() {
 Token endTagToken = new Token(Token.TokenType.EndTag, "</tag>", 0, 0, 0);
 xmlTreeBuilder.tokeniser = new Tokeniser();
 xmlTreeBuilder.tokeniser.setInput("testData");
 xmlTreeBuilder.tokeniser.next();
 xmlTreeBuilder.tokeniser.next();
 xmlTreeBuilder.process(endTagToken);

 assertFalse(stack.contains(xmlTreeBuilder.currentElement()));
 }

 // Test for process method with Comment
 @Test
 public void testProcessComment() {
 Token commentToken = new Token(Token.TokenType.Comment, "--comment--", 0, 0, 0);
 xmlTreeBuilder.tokeniser = new Tokeniser();
 xmlTreeBuilder.tokeniser.setInput("testData");
 xmlTreeBuilder.tokeniser.next();
 xmlTreeBuilder.tokeniser.next();
 xmlTreeBuilder.tokeniser.next();
 xmlTreeBuilder.process(commentToken);

 Comment comment = (Comment) doc.childNodes().get(0);
 assertEquals("--comment--", comment.data());
 }

 // Test for process method with Character
 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token(Token.TokenType.Character, "testCharacter", 0, 0, 0);
 xmlTreeBuilder.tokeniser = new Tokeniser();
 xmlTreeBuilder.tokeniser.setInput("testData");
 xmlTreeBuilder.tokeniser.next();
 xmlTreeBuilder.tokeniser.next();
 xmlTreeBuilder.tokeniser.next();
 xmlTreeBuilder.tokeniser.next();
 xmlTreeBuilder.process(characterToken);

 TextNode textNode = (TextNode) doc.childNodes().get(1);
 assertEquals("testCharacter", textNode.text());
 }

 // Test for process method with Doctype
 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Token(Token.TokenType.Doctype, "<!DOCTYPE doc [>\n", 0, 0, 0);
 xmlTreeBuilder.tokeniser = new Tokeniser();
 xmlTreeBuilder.tokeniser.setInput("testData");
 xmlTreeBuilder.tokeniser.next();
 xmlTreeBuilder.tokeniser.next();
 xmlTreeBuilder.tokeniser.next();
 xmlTreeBuilder.tokeniser.next();
 xmlTreeBuilder.process(doctypeToken);

 DocumentType doctypeNode = (DocumentType) doc.childNodes().get(0);
 assertEquals("doc", doctypeNode.nodeName());
 assertNull(doctypeNode.publicIdentifier());
 assertNull(doctypeNode.systemIdentifier());
 }

 // Test for popStackToClose method with existing tag on stack
 @Test
 public void testPopStackToCloseExistingTag() {
 Element tag1 = new Element("tag1", "baseUri", null);
 Element tag2 = new Element("tag2", "baseUri", null);
 stack.push(tag1);
 stack.push(tag2);
 xmlTreeBuilder.popStackToClose(new Token.EndTag("tag1"));
 assertEquals(tag2, xmlTreeBuilder.currentElement());
 assertEquals(1, stack.size());
 }
 // Test for popStackToClose method with non-existing tag on stack
 @Test (expected = AssertionError.class)
 public void testPopStackToCloseNonExistingTag() {
 xmlTreeBuilder.popStackToClose(new Token.EndTag("tag3"));
 fail("Expected AssertionError but no exception was thrown.");
 }
}