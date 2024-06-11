import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;
import java.util.Stack;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 doc = new Document();
 xmlTreeBuilder.initialiseParse("", "", null);
 xmlTreeBuilder.setInput(doc.bodyElement());
 }

 @Test
 public void testInitialiseParse() {
 xmlTreeBuilder.initialiseParse("", "", null);
 assertNotNull(xmlTreeBuilder.stack.peek());
 assertEquals(doc, xmlTreeBuilder.stack.peek());
 }

 @Test
 public void testProcessStartTag() {
 Token startTagToken = new Token.StartTag("root", null, null);
 xmlTreeBuilder.process(startTagToken);
 Element rootElement = xmlTreeBuilder.stack.peek();
 assertNotNull(rootElement);
 assertEquals("root", rootElement.nodeName());
 }

 @Test
 public void testProcessEndTag() {
 Token endTagToken = new Token.EndTag("root");
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertNull(xmlTreeBuilder.stack.peek());
 }

 @Test
 public void testProcessComment() {
 Token commentToken = new Token.Comment("This is a comment");
 xmlTreeBuilder.process(commentToken);
 Comment comment = (Comment) xmlTreeBuilder.stack.peek();
 assertNotNull(comment);
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token.Character('A');
 xmlTreeBuilder.process(characterToken);
 TextNode textNode = (TextNode) xmlTreeBuilder.stack.peek();
 assertNotNull(textNode);
 assertEquals("A", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Token.Doctype("html", null, null, null);
 xmlTreeBuilder.process(doctypeToken);
 DocumentType documentType = (DocumentType) xmlTreeBuilder.stack.peek();
 assertNotNull(documentType);
 assertEquals("html", documentType.name());
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 Token unexpectedToken = new Token(-1, "Unexpected token type");
 xmlTreeBuilder.process(unexpectedToken);
 }

 @Test
 public void testPopStackToCloseUnknownTag() {
 // given
 Token startTagToken = new Token.StartTag("unknown", null, null);
 xmlTreeBuilder.insert(startTagToken);
 Stack<Element> stack = xmlTreeBuilder.stack;
 Element unknownElement = stack.peek();

 // when
 Token endTagToken = new Token.EndTag("unknown");
 xmlTreeBuilder.popStackToClose(endTagToken);

 // then
 assertNull(xmlTreeBuilder.stack.peek());
 assertTrue(unknownElement.isDetached());
 }

 @Test
 public void testPopStackToCloseKnownTag() {
 // given
 Token startTagToken = new Token.StartTag("root", null, null);
 xmlTreeBuilder.insert(startTagToken);
 Stack<Element> stack = xmlTreeBuilder.stack;
 Element rootElement = stack.peek();

 // when
 Token endTagToken = new Token.EndTag("root");
 xmlTreeBuilder.popStackToClose(endTagToken);

 // then
 assertNull(xmlTreeBuilder.stack.peek());
 assertTrue(rootElement.isDetached());
 }
}