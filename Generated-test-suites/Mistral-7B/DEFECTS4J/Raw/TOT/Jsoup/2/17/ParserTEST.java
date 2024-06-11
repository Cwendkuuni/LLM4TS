import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Tag;
import static org.junit.Assert.*;

public class ParserTest {

 private Parser parser;
 private Document doc;

 @Before
 public void setUp() {
 parser = new Parser("", "", false);
 doc = parser.parse("<html><head></head><body></body></html>");
 }

 @Test
 public void testParseEmptyHtml() {
 assertNotNull(doc);
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 assertTrue(doc.head().children().isEmpty());
 assertTrue(doc.body().children().isEmpty());
 }

 @Test
 public void testParseHtmlWithTextNode() {
 parser = new Parser("<p>Hello World</p>", "", false);
 doc = parser.parse();
 Element pElement = doc.body().firstChild();
 assertEquals("Hello World", pElement.text());
 }

 @Test
 public void testParseHtmlWithComment() {
 parser = new Parser("<!DOCTYPE html>\n<!-- This is a comment-->\n<html><head></head><body></body></html>");
 doc = parser.parse();
 Element comment = doc.body().get(0).getFirstChild();
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseHtmlWithAttributes() {
 parser = new Parser("<a href=\"https://www.google.com\" target=\"_blank\">Google</a>", "", false);
 doc = parser.parse();
 Element aElement = doc.body().firstChild();
 assertEquals("https://www.google.com", aElement.absUrl("href"));
 assertEquals("_blank", aElement.attr("target"));
 }

 @Test
 public void testParseHtmlWithEmptyElement() {
 parser = new Parser("<img src=\"image.jpg\" alt=\"Image\"/>", "", false);
 doc = parser.parse();
 Element imgElement = doc.body().firstChild();
 assertTrue(imgElement.tags().isEmpty());
 assertNotNull(imgElement.attributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseHtmlWithInvalidTagName() {
 parser = new Parser("<invalidTag name=\"value\">", "", false);
 parser.parse();
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseHtmlWithMissingEndTag() {
 parser = new Parser("<p>Hello World<script>alert('Hello')</p>");
 doc = parser.parse();
 Element scriptElement = doc.body().get(1);
 assertNotNull(scriptElement);
 assertEquals("script", scriptElement.tagName());
 }

 @Test
 public void testParseHtmlWithNestedTags() {
 parser = new Parser("<html><body><p>Hello <b>World!</b></p></body></html>");
 doc = parser.parse();
 Element pElement = doc.body().firstChild();
 Element bElement = pElement.firstChild();
 assertEquals("Hello ", pElement.text().substring(0, pElement.text().length() - 2));
 assertEquals("World!", bElement.text());
 }

 @Test
 public void testParseHtmlWithCdataSection() {
 parser = new Parser("<p><![CDATA[Hello World]]></p>");
 doc = parser.parse();
 Element pElement = doc.body().firstChild();
 TextNode textNode = (TextNode) pElement.child(0);
 assertEquals("Hello World", textNode.text());
 }
}