import org.junit.jupitertest.BeforeEach;
import org.junit.jupitertest.Test;
import org.jsoup.helper.ElementUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.jupitertest.Assertions;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;
 private Tokenizer tokenizer;

 @BeforeEach
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 document = new Document();
 tokenizer = new Tokenizer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><child></child></root>");
 xmlTreeBuilder.initialiseParse(tokenizer.input(), "", null);
 }

 @Test
 public void testStartTag() {
 // Test inserting a start tag
 Token startTagToken = new Token(Token.StartTag.class, "<tag name='test' attr='value' />");
 xmlTreeBuilder.process(startTagToken);
 Element tagElement = xmlTreeBuilder.currentElement();
 Assertions.assertEquals("tag", tagElement.nodeName());
 Assertions.assertEquals("value", tagElement.attr("attr"));
 }

 @Test
 public void testEndTag() {
 // Test inserting an end tag and popping the stack
 Token endTagToken = new Token(Token.EndTag.class, "</tag>");
 xmlTreeBuilder.process(endTagToken);
 Assertions.assertNull(xmlTreeBuilder.currentElement());
 }

 @Test
 public void testCharacter() {
 // Test inserting a character
 Token characterToken = new Token(Token.Character.class, "test");
 xmlTreeBuilder.process(characterToken);
 TextNode characterNode = (TextNode) xmlTreeBuilder.currentElement().childNodes().get(0);
 Assertions.assertEquals("test", characterNode.text());
 }

 @Test
 public void testComment() {
 // Test inserting a comment
 Token commentToken = new Token(Token.Comment.class, "-- test comment --");
 xmlTreeBuilder.process(commentToken);
 Comment commentNode = (Comment) xmlTreeBuilder.currentElement().childNodes().get(0);
 Assertions.assertEquals("test comment", commentNode.text());
 }

 @Test
 public void testDoctype() {
 // Test inserting a doctype
 Token doctypeToken = new Token(Token.Doctype.class, "<!DOCTYPE html SYSTEM \"about:legacy-compat\">");
 xmlTreeBuilder.process(doctypeToken);
 DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.currentElement();
 Assertions.assertEquals("html", doctypeNode.name());
 Assertions.assertEquals("about:legacy-compat", doctypeNode.publicIdentifier());
 Assertions.assertNull(doctypeNode.systemIdentifier());
 }

 @Test
 public void testUnknownStartTag() {
 // Test inserting an unknown start tag
 Token startTagToken = new Token(Token.StartTag.class, "<unknown>");
 xmlTreeBuilder.process(startTagToken);
 Assertions.assertNull(xmlTreeBuilder.currentElement());
 }

 @Test
 public void testMissingEndTag() {
 // Test missing end tag
 tokenizer = new Tokenizer("<tag/><child></child>");
 xmlTreeBuilder.initialiseParse(tokenizer.input(), "", null);
 xmlTreeBuilder.process(tokenizer.nextToken()); // <tag>
 xmlTreeBuilder.process(tokenizer.nextToken()); // </child>
 Assertions.assertThrows(AssertionError.class, () -> xmlTreeBuilder.popStackToClose(tokenizer.nextToken()));
 }

 @Test
 public void testSelfClosingTag() {
 // Test self-closing tag
 Token selfClosingStartTagToken = new Token(Token.StartTag.class, "<tag name='test' selfClosing='true' />");
 xmlTreeBuilder.process(selfClosingStartTagToken);
 Element selfClosingTagElement = xmlTreeBuilder.currentElement();
 Assertions.assertEquals("test", selfClosingTagElement.nodeName());
 Assertions.assertFalse(selfClosingTagElement.hasChildren());
 }

 @Test
 public void testProcessEOF() {
 // Test processing EOF
 Token eofToken = new Token(Token.EOF.class, null);
 xmlTreeBuilder.process(eofToken);
 Assertions.assertNull(xmlTreeBuilder.currentElement());
 }
}