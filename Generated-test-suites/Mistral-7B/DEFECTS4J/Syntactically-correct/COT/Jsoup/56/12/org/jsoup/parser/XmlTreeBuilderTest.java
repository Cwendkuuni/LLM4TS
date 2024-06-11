import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Assert;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 @Test
 public void testDefaultSettings() {
 Document document = xmlTreeBuilder.parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><element attr=\"value\"></element></root>", "baseUri");
 Assert.assertEquals("baseUri", document.baseUri());
 }

 @Test
 public void testParse() {
 Document document = xmlTreeBuilder.parse("<root><element attr=\"value\"></element></root>", "baseUri");
 Assert.assertEquals(1, document.childNodes().size());
 Assert.assertEquals("element", document.childNodes().get(0).nodeName());
 }

 @Test
 public void testInitialiseParse() {
 String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><element attr=\"value\"></element></root>";
 ParseErrorList errors = new ParseErrorList();
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse(input, "baseUri", errors, settings);
 Assert.assertNotNull(xmlTreeBuilder.doc);
 }

 @Test(expected = RuntimeException.class)
 public void testUnexpectedTokenType() {
 String input = "<html><body>";
 Token token = new Token(1, 1, 1, HttpParser.TokenType.START_TAG, "<html", null, null, null, null, null);
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.process(token);
 }

 @Test
 public void testProcessStartTag() {
 String input = "<root><element attr=\"value\"></element></root>";
 Token startTag = new Token(1, 1, 1, HttpParser.TokenType.START_TAG, "root", null, null, null, null, null);
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.insert(startTag);
 Document document = xmlTreeBuilder.doc;
 Assert.assertEquals(1, document.childNodes().size());
 Assert.assertEquals("root", document.childNodes().get(0).nodeName());
 }

 @Test
 public void testProcessEndTag() {
 String input = "<root><element attr=\"value\"><element2/></element></root>";
 Token startTag = new Token(1, 1, 1, HttpParser.TokenType.START_TAG, "root", null, null, null, null, null);
 Token endTag = new Token(1, 1, 1, HttpParser.TokenType.END_TAG, "element", null, null, null, null, null);
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.insert(startTag);
 xmlTreeBuilder.process(endTag);
 Document document = xmlTreeBuilder.doc;
 Assert.assertEquals(1, document.childNodes().size());
 Assert.assertEquals("element", document.childNodes().get(0).nodeName());
 Assert.assertEquals(0, document.childNodes().get(0).childNodes().size());
 }

 @Test
 public void testProcessComment() {
 String input = "<!--This is a comment-->\n<root></root>";
 Token comment = new Token(1, 1, 1, HttpParser.TokenType.COMMENT, "--This is a comment--", null, null, null, null, null);
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.insert(comment);
 Document document = xmlTreeBuilder.doc;
 Assert.assertEquals(1, document.childNodes().size());
 Assert.assertEquals("root", document.childNodes().get(0).nodeName());
 Assert.assertEquals(1, document.childNodes().get(0).childNodes().size());
 Assert.assertEquals("!--This is a comment--", document.childNodes().get(0).childNodes().get(0).nodeValue());
 }

 @Test
 public void testProcessCharacter() {
 String input = "<root>Text<element attr=\"value\"></element></root>";
 Token character = new Token(1, 1, 1, HttpParser.TokenType.CHARACTER, "Text", null, null, null, null, null);
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.insert(character);
 Document document = xmlTreeBuilder.doc;
 Assert.assertEquals(1, document.childNodes().size());
 Assert.assertEquals("root", document.childNodes().get(0).nodeName());
 Assert.assertEquals(1, document.childNodes().get(0).childNodes().size());
 Assert.assertEquals("Text", document.childNodes().get(0).childNodes().get(0).nodeValue());
 }

 @Test
 public void testProcessDoctype() {
 String input = "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED> ]>\n<root><element attr=\"value\"></element></root>";
 Token doctype = new Token(1, 1, 1, HttpParser.TokenType.DOCTYPE, "root", null, null, null, null, null);
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.insert(doctype);
 Document document = xmlTreeBuilder.doc;
 Assert.assertEquals(1, document.childNodes().size());
 Assert.assertEquals("root", document.childNodes().get(0).nodeName());
 Assert.assertEquals(1, document.childNodes().get(0).childNodes().size());
 Assert.assertEquals("root", document.childNodes().get(0).childNodes().get(0).nodeName());
 Assert.assertTrue(document.childNodes().get(0).childNodes().get(0).hasAttr("ATTLIST"));
 Assert.assertEquals(1, document.childNodes().get(0).childNodes().get(0).attributes().size());
 Assert.assertEquals("attr", document.childNodes().get(0).childNodes().get(0).attributes().keySet().iterator().next());
 Assert.assertEquals("CDATA", document.childNodes().get(0).childNodes().get(0).attributes().get("CDATA").getType());
 Assert.assertEquals("#REQUIRED", document.childNodes().get(0).childNodes().get(0).attributes().get("CDATA").getValue());
 }

 @Test
 public void testInsertNode() {
 String input = "<root><element attr=\"value\"></element></root>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Token startTag = new Token(1, 1, 1, HttpParser.TokenType.START_TAG, "root", null, null, null, null, null);
 Element root = xmlTreeBuilder.insert(startTag);
 Element element = new Element("element", "baseUri", null);
 xmlTreeBuilder.insertNode(element);
 root.appendChild(element);
 Document document = xmlTreeBuilder.doc;
 Assert.assertEquals(1, document.childNodes().size());
 Assert.assertEquals("root", document.childNodes().get(0).nodeName());
 Assert.assertEquals(1, document.childNodes().get(0).childNodes().size());
 Assert.assertEquals("element", document.childNodes().get(0).childNodes().get(0).nodeName());
 }

 @Test
 public void testPopStackToClose() {
 String input = "<root><a><b/><c/></a></root>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Token startTagA = new Token(1, 1, 1, HttpParser.TokenType.START_TAG, "root", null, null, null, null, null);
 Element root = xmlTreeBuilder.insert(startTagA);
 Token startTagB = new Token(1, 1, 1, HttpParser.TokenType.START_TAG, "a", null, null, null, null, null);
 Element a = xmlTreeBuilder.insert(startTagB);
 root.appendChild(a);
 Token endTagB = new Token(1, 1, 1, HttpParser.TokenType.END_TAG, "b", null, null, null, null, null);
 xmlTreeBuilder.popStackToClose(endTagB);
 Document document = xmlTreeBuilder.doc;
 Assert.assertEquals(1, document.childNodes().size());
 Assert.assertEquals("root", document.childNodes().get(0).nodeName());
 Assert.assertEquals(1, document.childNodes().get(0).childNodes().size());
 Assert.assertEquals("a", document.childNodes().get(0).childNodes().get(0).nodeName());
 Assert.assertEquals(1, document.childNodes().get(0).childNodes().get(0).childNodes().size());
 Assert.assertEquals("b", document.childNodes().get(0).childNodes().get(0).childNodes().get(0).nodeName());
 }

 @Test
 public void testParseFragment() {
 String input = "<root><element attr=\"value\"></element></root>";
 List<Node> parsedNodes = xmlTreeBuilder.parseFragment(input, "baseUri", new ParseErrorList(), xmlTreeBuilder.defaultSettings());
 Assert.assertEquals(1, parsedNodes.size());
 Assert.assertEquals(1, parsedNodes.get(0).childNodes().size());
 Assert.assertEquals("root", parsedNodes.get(0).nodeName());
 Assert.assertEquals("element", parsedNodes.get(0).childNodes().get(0).nodeName());
 }

}