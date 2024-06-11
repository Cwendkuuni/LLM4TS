import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Token;
import org.jsoup.parser.XmlTreeBuilder;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 document = xmlTreeBuilder.parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element attr=\"value\">Hello World</element>\n</root>", "http://test.com");
 }

 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 Assert.assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
 }

 @Test
 public void testInitialiseParse() {
 // Cannot test this method directly as it's not public and relies on input, baseUri, and parser.
 }

 @Test
 public void testParse() {
 Assert.assertNotNull(xmlTreeBuilder.parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element attr=\"value\">Hello World</element>\n</root>", "http://test.com"));
 }

 @Test
 public void testParseString() {
 Assert.assertNotNull(xmlTreeBuilder.parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element attr=\"value\">Hello World</element>\n</root>", "http://test.com"));
 }

 @Test
 public void testProcessStartTag() {
 Token startTag = new Token(Token.StartTag.class, new Tag("element", null, new Attribute("attr", "value")));
 Element element = xmlTreeBuilder.insert(startTag);
 Assert.assertEquals("element", element.nodeName());
 Assert.assertEquals("value", element.attribute(0).getValue());
 }

 @Test
 public void testProcessEndTag() {
 Token endTag = new Token(Token.EndTag.class, new Tag("element"));
 xmlTreeBuilder.popStackToClose(endTag);
 Element lastElement = stack.peek();
 Assert.assertEquals("element", lastElement.nodeName());
 }

 @Test
 public void testProcessComment() {
 Token comment = new Token(Token.Comment.class, "This is a comment");
 xmlTreeBuilder.insert(comment);
 Comment commentNode = (Comment) document.childNodes().get(0);
 Assert.assertEquals("This is a comment", commentNode.text());
 }

 @Test
 public void testProcessCharacter() {
 Token character = new Token(Token.Character.class, "Hello World", false);
 xmlTreeBuilder.insert(character);
 TextNode textNode = (TextNode) document.childNodes().get(1);
 Assert.assertEquals("Hello World", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 Token doctype = new Token(Token.Doctype.class, "html", null, null, null);
 xmlTreeBuilder.insert(doctype);
 DocumentType documentType = (DocumentType) document.childNodes().get(0);
 Assert.assertEquals("html", documentType.nodeName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessInvalidTokenType() {
 Token invalidToken = new Token(123, "Invalid Token");
 xmlTreeBuilder.process(invalidToken);
 }

 @Test
 public void testProcessEOF() {
 // Cannot test this method directly as it doesn't return anything.
 }

 @Test
 public void testInsertNode() {
 Element element = new Element("element", "http://test.com");
 xmlTreeBuilder.insertNode(element);
 Assert.assertNotNull(document.childNodes().get(1));
 }

 @Test
 public void testInsertComment() {
 Comment comment = new Comment("This is a comment");
 xmlTreeBuilder.insert(comment);
 Assert.assertEquals(2, document.childNodes().size());
 }

 @Test
 public void testInsertCharacter() {
 TextNode textNode = new TextNode("Hello World");
 xmlTreeBuilder.insert(textNode);
 Assert.assertEquals(2, document.childNodes().size());
 }

 @Test
 public void testInsertDocumentType() {
 DocumentType documentType = new DocumentType("html", null, null);
 xmlTreeBuilder.insert(documentType);
 Assert.assertEquals(1, document.childNodes().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullNode() {
 xmlTreeBuilder.insertNode(null);
 }

 @Test
 public void testPopStackToClose() {
 Element element = new Element("element", "http://test.com");
 stack.add(element);
 xmlTreeBuilder.popStackToClose(new Token.EndTag(new Tag("element")));
 Assert.assertEquals(0, stack.size());
 }

 @Test
 public void testProcessUnknownSelfClosingTag() {
 Token startTag = new Token(Token.StartTag.class, new Tag("unknown", null, new Attribute("", "selfClosing")));
 Element element = xmlTreeBuilder.insert(startTag);
 Assert.assertTrue(element.isSelfClosing());
 }

 @Test
 public void testParseFragment() {
 String inputFragment = "<root><element attr=\"value\">Hello World</element></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, null, "http://test.com", new Parser(xmlTreeBuilder));
 Assert.assertEquals(3, nodes.size());
 }

 @Test
 public void testParseFragmentWithContext() {
 String inputFragment = "<root><element attr=\"value\">Hello World</element></root>";
 Element context = new Element("context");
 List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, context, "http://test.com", new Parser(xmlTreeBuilder));
 Assert.assertEquals(3, nodes.size());
 }
}