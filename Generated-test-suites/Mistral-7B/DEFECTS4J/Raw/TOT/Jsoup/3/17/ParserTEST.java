import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Attribute;
import static org.junit.Assert.*;

public class ParserTest {

 private Document doc;
 private Parser parser;

 @Before
 public void setup() {
 doc = Document.createEmpty();
 parser = new Parser("", "", false);
 parser.setDocument(doc);
 }

 @Test
 public void testParseEmptyHtml() {
 String html = "<html/>";
 Document parsed = Parser.parse(html, "http://example.com");
 assertEquals("http://example.com", parsed.baseUri());
 assertTrue(parsed.body().childNodes().isEmpty());
 }

 @Test
 public void testParseSingleTag() {
 String html = "<tag attr='val'/>";
 Document parsed = Parser.parse(html, "http://example.com");
 Element tag = parsed.body().child(0);
 assertEquals("tag", tag.tagName());
 assertEquals("val", tag.attr("attr"));
 assertTrue(tag.childNodes().isEmpty());
 }

 @Test
 public void testParseMultipleTags() {
 String html = "<tag1 attr1='val1'><tag2 attr2='val2'>content</tag2></tag1>";
 Document parsed = Parser.parse(html, "http://example.com");
 Element tag1 = parsed.body().child(0);
 assertEquals("tag1", tag1.tagName());
 Element tag2 = tag1.child(0);
 assertEquals("tag2", tag2.tagName());
 assertEquals("val1", tag1.attr("attr1"));
 assertEquals("val2", tag2.attr("attr2"));
 assertEquals("content", tag2.childNodes().get(0).text());
 }

 @Test
 public void testParseSelfClosingTag() {
 String html = "<tag/>";
 Document parsed = Parser.parse(html, "http://example.com");
 Element tag = parsed.body().child(0);
 assertEquals("tag", tag.tagName());
 assertTrue(tag.childNodes().isEmpty());
 }

 @Test
 public void testParseComment() {
 String html = "<!-- This is a comment -->";
 Document parsed = Parser.parse(html, "http://example.com");
 Element comment = parsed.body().child(0);
 assertEquals("Comment", comment.tagName());
 assertEquals("This is a comment", comment.attr("data"));
 }

 @Test
 public void testParseCdata() {
 String html = "<![CDATA[ This is CDATA ]]>";
 Document parsed = Parser.parse(html, "http://example.com");
 Element cdata = parsed.body().child(0);
 assertEquals("TextNode", cdata.tagName());
 assertEquals("This is CDATA", cdata.text());
 }

 @Test
 public void testParseScriptTag() {
 String html = "<script type='text/javascript'>function test(){}</script>";
 Document parsed = Parser.parse(html, "http://example.com");
 Element script = parsed.body().child(0);
 assertTrue(script.tagName().equalsOrNull("script"));
 assertFalse(script.text().isEmpty());
 }

 @Test
 public void testParseImplicitParent() {
 String html = "<implicit-tag attr='val'>Content</implicit-tag>";
 Document parsed = Parser.parse(html, "http://example.com");
 Element implicitParent = parsed.body().child(0);
 assertNotNull(implicitParent);
 assertEquals("div", implicitParent.tagName()); // assuming implicit-tag has div as its implicit parent
 Element content = implicitParent.child(0);
 assertEquals("Content", content.text());
 }

 @Test
 public void testParseInvalidHtml() {
 String html = "<html<tag>";
 Document parsed = Parser.parse(html, "http://example.com");
 assertEquals(0, parsed.body().childNodes().size());
 }

 @Test
 public void testParseMissingTagEnd() {
 String html = "<tag>";
 Document parsed = Parser.parse(html, "http://example.com");
 assertEquals(0, parsed.body().childNodes().size());
 }

 @Test
 public void testParseMalformedAttributeValue() { 
 String html = "<tag attr='val val=";
 Document parsed = Parser.parse(html, "http://example.com");
 Element tag = parsed.body().child(0);
 assertEquals("val val=", tag.attr("attr"));
 }

 @Test
 public void testParseQuotedAttributeValue() {
 String html = "<tag attr='val \"val\"'/>";
 Document parsed = Parser.parse(html, "http://example.com");
 Element tag = parsed.body().child(0);
 assertEquals("val \"val\"", tag.attr("attr"));
 }
}