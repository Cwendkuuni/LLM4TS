import org.apache.commons.lang3.validate.Validate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.*;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParserTest {

 private Parser parser;
 private Document doc;

 @Before
 public void setUp() {
 String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
 parser = new Parser(html, "http://test.com");
 doc = parser.parse();
 }

 @Test
 public void testParse() {
 assertNotNull(doc);
 assertEquals("Test", doc.title().text());
 assertEquals("Hello World", doc.body().text());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><h1>Hello World</h1></body>";
 Document fragmentDoc = Parser.parseBodyFragment(bodyHtml, "http://test.com");
 assertNotNull(fragmentDoc);
 assertEquals("Hello World", fragmentDoc.body().text());
 assertEquals("", fragmentDoc.head().text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullHtml() {
 Parser.parse(null, "http://test.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 Parser.parse("", null);
 }

 @Test
 public void testEmptyHtml() {
 String html = "";
 Document emptyDoc = Parser.parse(html, "http://test.com");
 assertNotNull(emptyDoc);
 assertEquals("", emptyDoc.body().text());
 }

 @Test
 public void testParseAttributes() {
 String htmlWithAttributes = "<p id='test' class='testClass' title='testTitle'>Test</p>";
 Element pElement = doc.selectFirst("p");
 List<Attribute> attributes = pElement.attributes();
 assertEquals(3, attributes.size());
 assertEquals("test", attributes.get(0).getKey());
 assertEquals("Test", attributes.get(0).getValue());
 assertEquals("testClass", attributes.get(1).getKey());
 assertEquals("", attributes.get(1).getValue());
 assertEquals("testTitle", attributes.get(2).getKey());
 assertEquals("testTitle", attributes.get(2).getValue());
 }

 @Test
 public void testParseTextNodeWithQuotes() {
 String htmlWithTextQuotes = "<p>Test <i>quote</i> 'quote' \"quote\"</p>";
 Element pElement = doc.selectFirst("p");
 TextNode textNode = (TextNode) pElement.child(0);
 assertEquals("Test <i>quote</i> 'quote' \"quote\"", textNode.text());
 }

 @Test
 public void testParseTextNodeWithoutQuotes() {
 String htmlWithTextWithoutQuotes = "<p>Test <i>quote</i> quote</p>";
 Element pElement = doc.selectFirst("p");
 TextNode textNode = (TextNode) pElement.child(0);
 assertEquals("Test <i>quote</i> quote", textNode.text());
 }

 @Test
 public void testParseCdata() {
 String htmlWithCdata = "<html><![CDATA[<p>Test CDATA</p>]]><body></body></html>";
 Document cdataDoc = Parser.parse(htmlWithCdata, "http://test.com");
 Element pElement = cdataDoc.selectFirst("p");
 TextNode textNode = (TextNode) pElement.child(0);
 assertEquals("Test CDATA", textNode.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidStartTag() {
 String htmlWithInvalidStartTag = "<html<p>Test</p>";
 Parser parser = new Parser(htmlWithInvalidStartTag, "http://test.com");
 parser.parse(); // this should throw an exception
 }

 @Test
 public void testParseInvalidEndTag() {
 String htmlWithInvalidEndTag = "<html><body><h1>Test</h1></body></body>";
 Parser parser = new Parser(htmlWithInvalidEndTag, "http://test.com");
 parser.parse(); // this should not throw an exception, but the resulting Document will be invalid
 }

 @Test
 public void testParseInvalidEndTagWithException() {
 String htmlWithInvalidEndTagWithException = "<html><body><h1>Test</h1></body></html><p>";
 Parser parser = new Parser(htmlWithInvalidEndTagWithException, "http://test.com");
 try {
 parser.parse();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseUnknownTag() {
 String htmlWithUnknownTag = "<html><body><z></z></body></html>";
 Parser parser = new Parser(htmlWithUnknownTag, "http://test.com");
 parser.parse(); // this should not throw an exception, but the resulting Document will not contain the <z> tag
 }

 @Test
 public void testParseEmptyTag() {
 String htmlWithEmptyTag = "<html><body><img src='' alt=''/></body></html>";
 Parser parser = new Parser(htmlWithEmptyTag, "http://test.com");
 parser.parse();
 Element imgElement = doc.selectFirst("img");
 assertNotNull(imgElement);
 assertEquals("", imgElement.attr("src"));
 assertEquals("", imgElement.attr("alt"));
 }

 @Test
 public void testParseScriptTag() {
 String htmlWithScriptTag = "<html><body><script src='script.js'></script></body></html>";
 Parser parser = new Parser(htmlWithScriptTag, "http://test.com");
 parser.parse();
 Element scriptElement = doc.selectFirst("script");
 assertNotNull(scriptElement);
 assertEquals("script.js", scriptElement.attr("src"));
 }

 @Test
 public void testParseComment() {
 String htmlWithComment = "<html><!-- This is a comment --><body></body></html>";
 Parser parser = new Parser(htmlWithComment, "http://test.com");
 parser.parse();
 Element commentElement = doc.selectFirst("!--");
 assertNotNull(commentElement);
 assertEquals("This is a comment", commentElement.text());
 }

 @Test
 public void testParseXmlDeclaration() {
 String htmlWithXmlDeclaration = "<html xmlns='http://www.w3.org/1999/xhtml' xml:lang='en'>";
 Parser parser = new Parser(htmlWithXmlDeclaration, "http://test.com");
 parser.parse();
 XmlDeclaration xmlDeclarationElement = doc.selectFirst("xml");
 assertNotNull(xmlDeclarationElement);
 assertEquals("http://www.w3.org/1999/xhtml", xmlDeclarationElement.namespaceURI());
 assertEquals("en", xmlDeclarationElement.xmlLang());
 assertEquals(true, xmlDeclarationElement.isStandalone());
 }
}