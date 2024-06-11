import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 document = xmlTreeBuilder.parse("<root><child><grandChild/></child></root>", "http://localhost");
 }

 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
 }

 @Test
 public void testInitialiseParse() {
 Document doc = xmlTreeBuilder.parse(new StringReader("<root><child><grandChild/></child></root>"), "http://localhost");
 assertNotNull(doc);
 assertEquals(doc, xmlTreeBuilder.doc);
 }

 @Test
 public void testProcessStartTag() {
 Element root = document.rootElement();
 assertEquals("root", root.tagName());
 }

 @Test
 public void testProcessEndTag() {
 Element child = document.select("child").first();
 assertEquals("child", child.tagName());
 assertNull(child.parent());
 }

 @Test
 public void testProcessComment() {
 List<Node> comments = document.select("comment()");
 assertEquals(0, comments.size());
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 xmlTreeBuilder.parse("<root><child>", new Parser(xmlTreeBuilder)); // missing end tag
 }

 @Test
 public void testInsertNode() {
 Element root = document.rootElement();
 TextNode textNode = new TextNode("hello world");
 xmlTreeBuilder.insertNode(textNode);
 root.appendChild(textNode);
 assertEquals("hello world", root.text());
 }

 @Test
 public void testInsertComment() {
 TextNode comment = new TextNode("<!-- this is a comment -->");
 xmlTreeBuilder.insert(new Token.Comment(comment.text(), 0, 0, 0, 0));
 assertEquals(1, document.childNodes().size());
 assertEquals("<!-- this is a comment -->", document.childNodes().get(0).toString());
 }

 @Test
 public void testInsertCharacter() {
 TextNode textNode = new TextNode("hello");
 xmlTreeBuilder.insert(new Token.Character('h', 0, 0, 0, 0));
 xmlTreeBuilder.insert(new Token.Character('e', 0, 0, 0, 0));
 xmlTreeBuilder.insert(new Token.Character('l', 0, 0, 0, 0));
 xmlTreeBuilder.insert(new Token.Character('l', 0, 0, 0, 0));
 xmlTreeBuilder.insert(new Token.Character('o', 0, 0, 0, 0));
 Element root = document.rootElement();
 assertEquals("hello", root.child(0).toString());
 }

 @Test
 public void testInsertDoctype() {
 Token.Doctype doctype = new Token.Doctype("xml", null, null, null);
 XmlDeclaration declaration = new XmlDeclaration("1.0", null, null, null);
 xmlTreeBuilder.insert(doctype);
 xmlTreeBuilder.insert(declaration);
 assertEquals(1, document.childNodes().size());
 assertEquals("<?xml version=\"1.0\"?>", document.childNodes().get(0).toString());
 }

 @Test
 public void testPopStackToClose() {
 Element root = document.rootElement();
 Element child = root.add(new Element("child"));
 Element grandChild = child.add(new Element("grandChild"));
 assertEquals(3, stack.size());
 xmlTreeBuilder.popStackToClose(grandChild.tag());
 assertEquals(2, stack.size());
 xmlTreeBuilder.popStackToClose(child.tag());
 assertEquals(1, stack.size());
 xmlTreeBuilder.popStackToClose(root.tag());
 assertEquals(0, stack.size());
 }

 @Test
 public void testParseFragment() {
 List<Node> nodes = xmlTreeBuilder.parseFragment("<root><child><grandChild/></child></root>", "http://localhost");
 assertEquals(3, nodes.size());
 assertEquals("root", nodes.get(0).tagName());
 assertEquals("child", nodes.get(1).tagName());
 assertEquals("grandChild", nodes.get(2).tagName());
 }

 @Test(expected = AssertionError.class)
 public void testParseFragmentWithInvalidInput() {
 xmlTreeBuilder.parseFragment("<root><child>", new Parser(xmlTreeBuilder)); // missing end tag
 }
}