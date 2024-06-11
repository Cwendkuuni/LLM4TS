import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;

public class XmlTreeBuilderTest {

 // Test case for defaultSettings method
 @Test
 public void testDefaultSettings() {
 ParseSettings settings = XmlTreeBuilder.defaultSettings();
 Assert.assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
 }

 // Test case for parse method with valid XML input
 @Test
 public void testParseValidXml() {
 String xmlInput = "<root><child>Hello</child></root>";
 Document xmlDoc = new XmlTreeBuilder().parse(xmlInput, "http://example.com");
 Assert.assertNotNull(xmlDoc);
 Assert.assertEquals(2, xmlDoc.childNodeSize());
 Element root = (Element) xmlDoc.child(0);
 Element child = (Element) root.child(0);
 Assert.assertEquals("Hello", child.text());
 }

 // Test case for process method with StartTag token
 @Test
 public void testProcessStartTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document inputDoc = Jsoup.parse("<root><child></child></root>", "http://example.com");
 Tokenizer tokenizer = new Tokenizer(inputDoc.outerHtml());
 Token startTag = tokenizer.nextToken();
 Assert.assertEquals(Token.StartTag.class, startTag.getClass());
 xmlTreeBuilder.process(startTag);
 }

 // Test case for process method with EndTag token
 @Test
 public void testProcessEndTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document inputDoc = Jsoup.parse("<root><child><sub></sub></child></root>", "http://example.com");
 Tokenizer tokenizer = new Tokenizer(inputDoc.outerHtml());
 Token endTag = tokenizer.nextToken();
 Assert.assertEquals(Token.EndTag.class, endTag.getClass());
 xmlTreeBuilder.process(endTag);
 }

 // Test case for process method with Comment token
 @Test
 public void testProcessComment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document inputDoc = Jsoup.parse("<root><!-- Hello world --></root>", "http://example.com");
 Tokenizer tokenizer = new Tokenizer(inputDoc.outerHtml());
 Token commentToken = tokenizer.nextToken();
 Assert.assertEquals(Token.Comment.class, commentToken.getClass());
 xmlTreeBuilder.process(commentToken);
 }

 // Test case for process method with Character token
 @Test
 public void testProcessCharacter() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document inputDoc = Jsoup.parse("<root>Hello world</root>", "http://example.com");
 Tokenizer tokenizer = new Tokenizer(inputDoc.outerHtml());
 Token characterToken = tokenizer.nextToken();
 Assert.assertEquals(Token.Character.class, characterToken.getClass());
 xmlTreeBuilder.process(characterToken);
 }

 // Test case for process method with Doctype token
 @Test
 public void testProcessDoctype() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document inputDoc = Jsoup.parse("<!DOCTYPE root SYSTEM \"http://example.com/root.dtd\">", "http://example.com");
 Tokenizer tokenizer = new Tokenizer(inputDoc.outerHtml());
 Token doctypeToken = tokenizer.nextToken();
 Assert.assertEquals(Token.Doctype.class, doctypeToken.getClass());
 xmlTreeBuilder.process(doctypeToken);
 }

 // Test case for insertNode method with Element node
 @Test
 public void testInsertNodeElement() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Element element = new Element("child", "http://example.com");
 xmlTreeBuilder.insertNode(element);
 }

 // Test case for parseFragment method with valid XML fragment input
 @Test
 public void testParseFragmentValidXml() {
 String xmlFragment = "<root><child>Hello</child></root>";
 List<Node> nodes = new XmlTreeBuilder().parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Assert.assertNotNull(nodes);
 Assert.assertEquals(2, nodes.size());
 Element root = (Element) nodes.get(0);
 Element child = (Element) root.child(0);
 Assert.assertEquals("Hello", child.text());
 }

 // Test case for popStackToClose method with unknown tag name
 @Test(expected = AssertionError.class)
 public void testPopStackToCloseUnknownTagName() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.popStackToClose(new Token.EndTag("unknown"));
 }

 // Test case for popStackToClose method with known tag name already on stack
 @Test
 public void testPopStackToCloseKnownTagName() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Element root = new Element("root", "http://example.com");
 xmlTreeBuilder.insertNode(root);
 xmlTreeBuilder.popStackToClose(new Token.EndTag("root"));
 Assert.assertEquals(0, xmlTreeBuilder.stack.size());
 }

 // Test case for popStackToClose method with known tag name not on stack
 @Test(expected = AssertionError.class)
 public void testPopStackToCloseKnownTagNameNotOnStack() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.popStackToClose(new Token.EndTag("child"));
 }

}