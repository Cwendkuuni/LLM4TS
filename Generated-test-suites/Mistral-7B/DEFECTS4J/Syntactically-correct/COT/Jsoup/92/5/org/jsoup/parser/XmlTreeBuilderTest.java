import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private ParseSettings parseSettings;
 private Parser parser;
 private Document document;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 parseSettings = xmlTreeBuilder.defaultSettings();
 parser = new Parser(xmlTreeBuilder);
 document = null;
 }

 // Testing the defaultSettings() method
 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
 }

 // Testing the initialiseParse() method
 @Test
 public void testInitialiseParse() {
 String input = "<root><child></child></root>";
 xmlTreeBuilder.initialiseParse(new StringReader(input), null, parser);
 assertNotNull(xmlTreeBuilder.doc);
 }

 // Testing the parse() method with valid input
 @Test
 public void testParseValidInput() {
 String input = "<root><child></child></root>";
 document = xmlTreeBuilder.parse(new StringReader(input), null);
 assertNotNull(document);
 assertEquals(2, document.childNodes().size());
 }

 // Testing the process() method with start tag
 @Test
 public void testProcessStartTag() {
 Token startTagToken = new Token.StartTag("root", null, null, null, -1, null);
 xmlTreeBuilder.process(startTagToken);
 Element rootElement = (Element) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
 assertEquals("root", rootElement.nodeName());
 }

 // Testing the process() method with end tag
 @Test
 public void testProcessEndTag() {
 Token endTagToken = new Token.EndTag("root");
 xmlTreeBuilder.process(endTagToken);
 assertEquals(1, xmlTreeBuilder.stack.size());
 }

 // Testing the process() method with comment
 @Test
 public void testProcessComment() {
 Token commentToken = new Token.Comment("This is a comment.");
 xmlTreeBuilder.process(commentToken);
 Comment comment = (Comment) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
 assertEquals("This is a comment.", comment.data());
 }

 // Testing the process() method with character
 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token.Character('A');
 xmlTreeBuilder.process(characterToken);
 TextNode textNode = (TextNode) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
 assertEquals("A", textNode.text());
 }

 // Testing the process() method with doctype
 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Token.Doctype("html", null, null);
 xmlTreeBuilder.process(doctypeToken);
 DocumentType documentType = (DocumentType) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
 assertEquals("html", documentType.nodeName());
 }

 // Testing the popStackToClose() method with valid input
 @Test
 public void testPopStackToCloseValidInput() {
 String input = "<root><child><grandchild></grandchild></child></root>";
 xmlTreeBuilder.initialiseParse(new StringReader(input), null, parser);
 xmlTreeBuilder.process(new Token.StartTag("root"));
 xmlTreeBuilder.process(new Token.StartTag("child"));
 xmlTreeBuilder.process(new Token.StartTag("grandchild"));
 xmlTreeBuilder.popStackToClose(new Token.EndTag("grandchild"));
 assertEquals(2, xmlTreeBuilder.stack.size());
 }

 // Testing the popStackToClose() method with invalid input (no matching tag)
 @Test (expected = AssertionError.class)
 public void testPopStackToCloseInvalidInput() {
 String input = "<root><child></root>";
 xmlTreeBuilder.initialiseParse(new StringReader(input), null, parser);
 xmlTreeBuilder.process(new Token.StartTag("root"));
 xmlTreeBuilder.process(new Token.StartTag("child"));
 xmlTreeBuilder.popStackToClose(new Token.EndTag("grandchild"));
 }

 // Testing the parseFragment() method with valid input
 @Test
 public void testParseFragmentValidInput() {
 String inputFragment = "<root><child></child></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, null, null, parser);
 assertEquals(2, nodes.size());
 }

 // Testing the parseFragment() method with self-closing tag
 @Test
 public void testParseFragmentSelfClosingTag() {
 String inputFragment = "<root><img src=\"image.jpg\" /></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, null, null, parser);
 assertEquals(2, nodes.size());
 }

 // Testing the parseFragment() method with improperly nested tags
 @Test (expected = AssertionError.class)
 public void testParseFragmentImproperlyNestedTags() {
 String inputFragment = "<root><child><grandchild></grandchild></child></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, null, null, parser);
 }
}