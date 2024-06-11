import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.jsoup.helper.Validate;

import java.util.Stack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder builder;
 private Document doc;
 private Tokenizer tokenizer;

 @Before
 public void setup() {
 builder = new XmlTreeBuilder();
 doc = new Document();
 tokenizer = builder.tokenizer();
 }

 @Test
 public void testStartTag() {
 // Given: A start tag token
 Token startTag = new Token.StartTag("tag", null, null);

 // When: Invoke insert method with the start tag
 Element tagElement = builder.insert(startTag);

 // Then: The new Element should be added to the document and stack
 assertEquals("tag", tagElement.nodeName());
 assertTrue(doc.body().children().hasSize(1));
 assertTrue(stack.peek().equals(tagElement));
 }

 @Test
 public void testEndTag() {
 // Given: A document with an Element on the stack
 Element root = new Element("root", "http://example.com");
 doc.body().appendChild(root);
 stack.push(root);

 // Given: An end tag token for the root Element
 Token endTag = new Token.EndTag("root");

 // When: Invoke popStackToClose method
 builder.popStackToClose(endTag);

 // Then: The root Element should be removed from the stack
 assertFalse(stack.isEmpty());
 assertNull(stack.peek());
 // The document should still contain the root Element
 assertEquals(1, doc.body().children().size());
 }

 @Test
 public void testComment() {
 // Given: A comment token
 Token commentToken = new Token.Comment("-- This is a comment");

 // When: Invoke insert method with the comment token
 builder.insert(commentToken);

 // Then: A new Comment node should be added to the document
 assertTrue(doc.body().child(0) instanceof Comment);
 }

 @Test
 public void testCharacter() {
 // Given: A character token
 Token characterToken = new Token.Character('\'');

 // When: Invoke insert method with the character token
 Node characterNode = builder.insert(characterToken);

 // Then: A new TextNode should be added to the document
 assertTrue(characterNode instanceof TextNode);
 assertEquals("'", characterNode.text());
 }

 @Test
 public void testDoctype() {
 // Given: A doctype token
 Token doctypeToken = new Token.Doctype("html", null, null, null);

 // When: Invoke insert method with the doctype token
 DocumentType doctypeNode = (DocumentType) builder.insert(doctypeToken);

 // Then: A new DocumentType node should be added to the document
 assertEquals("html", doctypeNode.name());
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 // Given: An unexpected token type
 Token unexpectedToken = new Token(-1, "Unexpected token type");

 // When: Invoke process method with the unexpected token
 builder.process(unexpectedToken);
 }
}