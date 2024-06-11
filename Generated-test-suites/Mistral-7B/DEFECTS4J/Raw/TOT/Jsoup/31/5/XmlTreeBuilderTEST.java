import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder builder;

 @Before
 public void setup() {
 builder = new XmlTreeBuilder();
 }

 @Test
 public void testStartTag() {
 Token startTag = new Token.StartTag("root", null, null);
 Element rootElement = builder.insert(startTag);
 assertEquals("root", rootElement.nodeName());
 }

 @Test
 public void testEndTag() {
 Token endTag = new Token.EndTag("root");
 builder.popStackToClose(endTag);
 assertTrue(builder.stack.isEmpty());
 }

 @Test
 public void testComment() {
 Token commentToken = new Token.Comment("this is a comment");
 builder.insert(commentToken);
 assertEquals("this is a comment", builder.getDocument().childNodes().get(0).nodeValue());
 }

 @Test
 public void testCharacter() {
 Token characterToken = new Token.Character('A');
 Node node = builder.insert(characterToken);
 assertEquals("A", node.nodeValue());
 }

 @Test
 public void testDoctype() {
 Token doctypeToken = new Token.Doctype("html", null, null);
 DocumentType documentTypeNode = (DocumentType) builder.insert(doctypeToken);
 assertEquals("html", documentTypeNode.getName());
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 builder.process(new Token(1234)); // Unexpected token type: 1234
 }

 @Test
 public void testSelfClosingTag() {
 Token startTag = new Token.StartTag("img", null, null, true);
 Element imgElement = builder.insert(startTag);
 assertEquals("img", imgElement.nodeName());
 assertTrue(imgElement.hasAttr("src"));
 assertTrue(builder.stack.isEmpty());
 }

 @Test
 public void testNestedTags() {
 Token startTag1 = new Token.StartTag("root", null, null);
 Token startTag2 = new Token.StartTag("child", null, null);
 Element rootElement = builder.insert(startTag1);
 Element childElement = builder.insert(startTag2);
 rootElement.appendChild(childElement);
 assertEquals("child", childElement.nodeName());
 }

 @Test
 public void testPopMultipleElements() {
 Token endTag1 = new Token.EndTag("child");
 Token endTag2 = new Token.EndTag("root");
 builder.popStackToClose(endTag1);
 builder.popStackToClose(endTag2);
 assertTrue(builder.stack.isEmpty());
 }

 @Test
 public void testEmptyDocument() {
 builder.initialiseParse("", null, null);
 assertNotNull(builder.getDocument());
 assertTrue(builder.getDocument().childNodes().isEmpty());
 }
}