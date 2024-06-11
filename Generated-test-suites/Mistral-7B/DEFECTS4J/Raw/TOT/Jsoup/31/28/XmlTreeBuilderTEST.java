import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.XmlTreeBuilder.Token;
import org.jsoup.parser.XmlTreeBuilder.Token.Attribute;
import org.jsoup.parser.XmlTreeBuilder.Token.Comment;
import org.jsoup.parser.XmlTreeBuilder.Token.Doctype;
import org.jsoup.parser.XmlTreeBuilder.Token.EndTag;
import org.jsoup.parser.XmlTreeBuilder.Token.StartTag;
import org.jsoup.parser.XmlTreeBuilder.Token.Character;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class XmlTreeBuilderTest {

 @BeforeAll
 public void setup() {
 // Initialize the XMLTreeBuilder with an empty Document
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document document = new Document();
 xmlTreeBuilder.initialiseParse("", "", new ParseErrorList());
 xmlTreeBuilder.setInput(document);
 }

 @Test
 public void testStartTag() {
 // Test a simple start tag
 Token startTagToken = new Token.StartTag("root", null);
 XmlTreeBuilder.Element rootElement = xmlTreeBuilder.insert(startTagToken);
 assertEquals("root", rootElement.nodeName());
 }

 @Test
 public void testEndTag() {
 // Test a simple end tag
 Token endTagToken = new Token.EndTag("root");
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertTrue(xmlTreeBuilder.stack.isEmpty());
 }

 @Test
 public void testComment() {
 // Test a simple comment
 Token commentToken = new Token.Comment("-- this is a comment");
 xmlTreeBuilder.insert(commentToken);
 assertTrue(xmlTreeBuilder.currentElement().child(0) instanceof Comment);
 }

 @Test
 public void testCharacter() {
 // Test inserting a character
 Token characterToken = new Token.Character('a');
 xmlTreeBuilder.insert(characterToken);
 assertTrue(xmlTreeBuilder.currentElement().child(0) instanceof TextNode);
 assertEquals("a", xmlTreeBuilder.currentElement().child(0).text());
 }

 @Test
 public void testDoctype() {
 // Test inserting a doctype
 Token doctypeToken = new Token.Doctype("xml", null, null, "");
 xmlTreeBuilder.insert(doctypeToken);
 assertTrue(xmlTreeBuilder.currentElement() instanceof Document);
 assertEquals("xml", xmlTreeBuilder.currentElement().nodeName());
 }

 @Test
 public void testSelfClosingTag() {
 // Test inserting a self-closing tag
 Token startTagToken = new Token.StartTag("img", null, null, true);
 XmlTreeBuilder.Element imgElement = xmlTreeBuilder.insert(startTagToken);
 assertTrue(imgElement.isSelfClosing());
 }

 @Test
 public void testNestedTags() {
 // Test inserting nested tags
 Token startTagToken1 = new Token.StartTag("root", null);
 XmlTreeBuilder.Element rootElement = xmlTreeBuilder.insert(startTagToken1);

 Token startTagToken2 = new Token.StartTag("child", null);
 XmlTreeBuilder.Element childElement = xmlTreeBuilder.insert(startTagToken2);
 rootElement.appendChild(childElement);

 Token endTagToken1 = new Token.EndTag("root");
 xmlTreeBuilder.popStackToClose(endTagToken1);
 assertTrue(xmlTreeBuilder.stack.isEmpty());
 }

 @Test
 public void testInvalidTokenType() {
 // Test handling an unexpected token type
 Token invalidToken = new Token(-1, "");
 xmlTreeBuilder.process(invalidToken);
 fail("Unexpected token type: " + invalidToken.type);
 }
}