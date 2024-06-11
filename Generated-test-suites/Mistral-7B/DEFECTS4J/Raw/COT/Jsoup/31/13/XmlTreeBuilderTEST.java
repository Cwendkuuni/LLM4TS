import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.helper.Validate;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;
 private ParseErrorList errors;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 document = new Document();
 errors = new ParseErrorList();
 }

 @Test
 public void testInitialiseParse() {
 xmlTreeBuilder.initialiseParse("", "", errors);
 assertEquals(document, xmlTreeBuilder.getDocument());
 assertTrue(xmlTreeBuilder.getStack().contains(document));
 }

 @Test
 public void testProcess_StartTag() {
 Token startTagToken = new Token(Token.TokenType.StartTag, "<test>", null, null, null, null, null, null, null);
 xmlTreeBuilder.process(startTagToken);
 Element testElement = new Element("test", "", new Attributes());
 assertEquals(testElement, xmlTreeBuilder.getCurrentElement());
 }

 @Test
 public void testProcess_EndTag() {
 Token endTagToken = new Token(Token.TokenType.EndTag, "</test>", null, null, null, null, null, null, null);
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertNull(xmlTreeBuilder.getCurrentElement());
 }

 @Test
 public void testProcess_Comment() {
 Token commentToken = new Token(Token.TokenType.Comment, "--test--", null, null, null, null, null, null, null);
 xmlTreeBuilder.process(commentToken);
 assertEquals(document.body(), xmlTreeBuilder.getCurrentElement());
 }

 @Test
 public void testProcess_Character() {
 Token characterToken = new Token(Token.TokenType.Character, "test", null, null, null, null, null, null, null);
 xmlTreeBuilder.process(characterToken);
 TextNode textNode = new TextNode("test", "");
 assertEquals(textNode, xmlTreeBuilder.getCurrentElement());
 }

 @Test
 public void testProcess_Doctype() {
 Token doctypeToken = new Token(Token.TokenType.Doctype, "<!DOCTYPE test [>", null, null, null, null, null, null, null);
 xmlTreeBuilder.process(doctypeToken);
 DocumentType doctypeNode = new DocumentType("test", null, null, "");
 assertEquals(doctypeNode, xmlTreeBUILDer.getCurrentElement());
 }

 @Test(expected = AssertionError.class)
 public void testProcess_UnexpectedTokenType() {
 Token unexpectedToken = new Token(123, null, null, null, null, null, null, null, null);
 xmlTreeBuilder.process(unexpectedToken);
 }

 @Test
 public void testInsertNode() {
 Element testElement = new Element("test", "", new Attributes());
 xmlTreeBuilder.insertNode(testElement);
 assertEquals(testElement, xmlTreeBuilder.getCurrentElement().getChildNodes().get(0));
 }

 @Test
 public void testPopStackToClose_NotFound() {
 Token endTagToken = new Token(Token.TokenType.EndTag, "</test>", null, null, null, null, null, null, null);
 xmlTreeBuilder.popStackToClose(endTagToken);
 }

 @Test
 public void testPopStackToClose_Found() {
 Token startTagToken = new Token(Token.TokenType.StartTag, "<test>", null, null, null, null, null, null, null);
 Token endTagToken = new Token(Token.TokenType.EndTag, "</test>", null, null, null, null, null, null, null);
 xmlTreeBuilder.process(startTagToken);
 xmlTreeBuilder.process(endTagToken);
 Element testElement = new Element("test", "", new Attributes());
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertEquals(document, xmlTreeBuilder.getCurrentElement());
 }
}