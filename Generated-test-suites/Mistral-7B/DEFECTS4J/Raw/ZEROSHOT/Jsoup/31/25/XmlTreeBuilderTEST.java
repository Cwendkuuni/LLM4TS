import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Comment;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 document = new Document();
 xmlTreeBuilder.initialiseParse("", "", null);
 }

 @Test
 public void testProcessStartTag() {
 // Given a start tag token
 Token startTagToken = new Token.StartTag("tagName", null);

 // When the XmlTreeBuilder processes the start tag token
 xmlTreeBuilder.process(startTagToken);

 // Then an Element should be added to the document
 Element element = new Element("tagName", "", null);
 Assert.assertEquals(element, xmlTreeBuilder.doc);
 }

 @Test
 public void testProcessSelfClosingStartTag() {
 // Given a self-closing start tag token
 Token selfClosingStartTagToken = new Token.StartTag("tagName", null, true);

 // When the XmlTreeBuilder processes the start tag token
 xmlTreeBuilder.process(selfClosingStartTagToken);

 // Then no Element should be added to the document stack
 Assert.assertNull(xmlTreeBuilder.doc);

 // And the tag should be set as self-closing
 Tag tag = Tag.valueOf("tagName");
 Assert.assertTrue(tag.isSelfClosing());
 }

 @Test
 public void testProcessEndTag() {
 // Given an end tag token with a matching tag name on the document stack
 xmlTreeBuilder.insert(new Token.StartTag("tagName", null));
 xmlTreeBuilder.insert(new Token.EndTag("tagName"));

 // When the XmlTreeBuilder processes the end tag token
 xmlTreeBuilder.process(new Token.EndTag("tagName"));

 // Then the top Element on the document stack should be removed
 Element topElement = xmlTreeBuilder.stack.peek();
 Assert.assertEquals("tagName", topElement.nodeName());
 xmlTreeBuilder.stack.remove();
 }

 @Test(expected = AssertionError.class)
 public void testProcessUnexpectedToken() {
 // Given an unexpected token type
 Token unexpectedToken = new Token(Token.Type.OTHER, null);

 // When the XmlTreeBuilder processes the unexpected token
 xmlTreeBuilder.process(unexpectedToken);

 // Then an AssertionError should be thrown
 }

 @Test
 public void testProcessComment() {
 // Given a comment token
 Token commentToken = new Token.Comment("-- comment");

 // When the XmlTreeBuilder processes the comment token
 xmlTreeBuilder.process(commentToken);

 // Then a Comment node should be added to the document
 Comment comment = new Comment("-- comment", "");
 Assert.assertEquals(comment, xmlTreeBuilder.doc.childNodes().get(0));
 }

 @Test
 public void testProcessCharacter() {
 // Given a character token
 Token characterToken = new Token.Character('a');

 // When the XmlTreeBuilder processes the character token
 xmlTreeBuilder.process(characterToken);

 // Then a TextNode should be added to the document with the character data
 TextNode textNode = new TextNode("a", "");
 Assert.assertEquals(textNode, xmlTreeBuilder.doc.childNodes().get(0));
 }

 @Test
 public void testProcessDoctype() {
 // Given a doctype token
 Token doctypeToken = new Token.Doctype("doctypeName", "", "", "");

 // When the XmlTreeBuilder processes the doctype token
 xmlTreeBuilder.process(doctypeToken);

 // Then a DocumentType node should be added to the document
 DocumentType documentType = new DocumentType("doctypeName", "", "", "");
 Assert.assertEquals(documentType, xmlTreeBuilder.doc);
 }
}