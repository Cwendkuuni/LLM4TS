import org.junit.Test;
import org.jsoup.parser.Parser;
import static org.junit.Assert.*;
import org.jsoup.nodes.*;

public class ParserTest {

 @Test
 public void testParseDocument() {
 Parser parser = new Parser("<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>", "http://test.com");
 Document doc = parser.parse();
 Element root = doc.rootElement();

 assertEquals("html", root.tag().name());
 assertEquals("Test Title", root.head().title().text());
 assertEquals("Test Heading", root.body().head().text());
 }

 @Test
 public void testParseBodyFragment() {
 Parser parser = new Parser("<body><h1>Test Heading</h1></body>", "http://test.com");
 Document doc = parser.parse();
 Element root = doc.rootElement();

 assertEquals("html", root.tag().name());
 assertEquals(Element.Void.class, root.head().tag().getClass());
 assertEquals("Test Heading", root.body().head().text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 Parser parser = new Parser("<body><h1>Test Heading</h1>", "http://test.com");
 parser.relaxed = true;
 Document doc = parser.parse();
 Element root = doc.rootElement();

 assertEquals("html", root.tag().name());
 assertEquals(Element.Void.class, root.head().tag().getClass());
 assertEquals("Test Heading", root.body().head().text());
 }

 @Test(expected = NullPointerException.class)
 public void testNullHtml() {
 Parser parser = new Parser(null, "http://test.com");
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
 Parser parser = new Parser("<html></html>", null);
 }

 @Test
 public void testParseComment() {
 Parser parser = new Parser("<html><!-- This is a comment --></html>", "http://test.com");
 Document doc = parser.parse();
 Comment comment = doc.body().firstChild().nextSibling();
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseXmlDecl() {
 Parser parser = new Parser("<html xmlns:foo=\"bar\"><![CDATA[<!-- This is a comment --><xml>]]>", "http://test.com");
 Document doc = parser.parse();
 XmlDeclaration decl = doc.body().firstChild();
 assertEquals("http://test.com", decl.baseUri());
 assertTrue(decl.procIsInstructionSet());
 }

 @Test
 public void testParseEndTag() {
 Parser parser = new Parser("<html><body><p><div></div></p></body></html>", "http://test.com");
 Document doc = parser.parse();
 assertEquals(3, doc.body().childNodeSize());
 Element div = doc.body().get(1);
 assertEquals("div", div.tag().name());
 assertEquals(Element.Void.class, div.tag().getClass());
 }

 @Test
 public void testParseStartTag() {
 Parser parser = new Parser("<html><body><h1 id=\"test\"></h1></body></html>", "http://test.com");
 Document doc = parser.parse();
 Element h1 = doc.body().get(0);
 assertEquals("h1", h1.tag().name());
 assertEquals("test", h1.id());
 }

 @Test
 public void testParseAttribute() {
 Parser parser = new Parser("<html><body><a href=\"http://test.com/link\" class=\"testClass\"></a></body></html>", "http://test.com");
 Document doc = parser.parse();
 Element a = doc.body().get(0);
 assertEquals("http://test.com/link", a.attr("href"));
 assertEquals("testClass", a.attr("class"));
 }

 @Test(expected = IllegalStateException.class)
 public void testUnknownTagAsSelfClosing() {
 Parser parser = new Parser("<html><body><x></x></body></html>", "http://test.com");
 parser.relaxed = true;
 Document doc = parser.parse();
 Element x = doc.body().get(0);
 fail("Unexpectedly found a non-empty element node for tag 'x'");
 }

 @Test(expected = IllegalStateException.class)
 public void testUnknownTagWithContent() {
 Parser parser = new Parser("<html><body><x>Content</x></body></html>", "http://test.com");
 parser.relaxed = true;
 Document doc = parser.parse();
 Element x = doc.body().get(0);
 fail("Unexpectedly found a non-empty element node for tag 'x' with content 'Content'");
 }

 @Test
 public void testParseBaseTag() {
 Parser parser = new Parser("<html><head><base href=\"http://test2.com\"></base><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>", "http://test.com");
 Document doc = parser.parse();
 assertEquals("http://test2.com", doc.baseUri());
 }

 @Test
 public void testParseEmptyElement() {
 Parser parser = new Parser("<html><body><img src=\"test.png\"/></body></html>", "http://test.com");
 Document doc = parser.parse();
 Element img = doc.body().get(0);
 assertEquals("img", img.tag().name());
 assertEquals(Element.Void.class, img.tag().getClass());
 }

 @Test
 public void testParseImplicitParent() {
 Parser parser = new Parser("<html><body><script src=\"test.js\"></script></body></html>", "http://test.com");
 Document doc = parser.parse();
 Element script = doc.body().get(0);
 assertEquals("script", script.tag().name());
 Element parent = script.parent();
 assertEquals("body", parent.tag().name());
 }

 @Test
 public void testParseInvalidAncestor() {
 Parser parser = new Parser("<html><body><h1><div></div></h1></body></html>", "http://test.com");
 Document doc = parser.parse();
 Element h1 = doc.body().get(0);
 Element div = h1.get(0);
 assertTrue(false); // should fail because <div> is not a valid ancestor of <h1>
 }

 @Test
 public void testParseInvalidParentTag() {
 Parser parser = new Parser("<html><body><h1 parent=\"invalid\"></h1></body></html>", "http://test.com");
 Document doc = parser.parse();
 Element h1 = doc.body().get(0);
 assertTrue(false); // should fail because 'parent' is not a valid tag name
 }

 @Test
 public void testParseEmptyTagWithContent() {
 Parser parser = new Parser("<html><body><input type=\"text\" value=\"Test\"/></body></html>", "http://test.com");
 Document doc = parser.parse();
 Element input = doc.body().get(0);
 assertEquals("input", input.tag().name());
 assertEquals("text", input.attr("type"));
 String content = input.text();
 assertEquals("Test", content);
 }

 @Test
 public void testParseTextNodeWithSpecialCharacter() {
 Parser parser = new Parser("<html><body><p>This is &lt; a test &gt;</p></body></html>", "http://test.com");
 Document doc = parser.parse();
 TextNode textNode = doc.body().get(0).child(0);
 assertEquals("This is < a test >", textNode.text());
 }
}