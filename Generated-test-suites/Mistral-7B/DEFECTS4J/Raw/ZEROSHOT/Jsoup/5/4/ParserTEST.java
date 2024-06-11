package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.LinkedList;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * ParserTest.java
 *
 * Test cases for Parser class.
 *
 * @author [Your Name]
 */
public class ParserTest {

 private static final String HTML_EMPTY = "<html></html>";
 private static final String HTML_SIMPLE = "<html><head><title>Title</title></head><body><h1>Heading</h1></body></html>";
 private static final String HTML_COMPLEX = "<html>"
 + "<head><title>Title</title>"
 + "<meta charset=\"UTF-8\">"
 + "</head>"
 + "<body>"
 + "<base href=\"test.html\">"
 + "<div id=\"container\">"
 + "<p>Hello world!</p>"
 + "<ul>"
 + "<li><a href=\"link.html\">Link</a></li>"
 + "</ul>"
 + "</div>"
 + "</body>"
 + "</html>";

 @Test
 public void testParseEmpty() throws Exception {
 Parser parser = Parser.parse(HTML_EMPTY, "test.html");
 Document doc = parser.parse();
 assertEquals("", doc.body().text());
 assertNotNull(doc.body());
 assertNotNull(doc.head());
 assertNull(doc.title());
 }

 @Test
 public void testParseSimple() throws Exception {
 Parser parser = Parser.parse(HTML_SIMPLE, "test.html");
 Document doc = parser.parse();
 Element title = doc.title();
 assertEquals("Title", title.text());
 assertNotNull(title);
 assertEquals("Heading", doc.body().getElementsByTag("h1").text());
 }

 @Test
 public void testParseComplex() throws Exception {
 Parser parser = Parser.parse(HTML_COMPLEX, "test.html");
 Document doc = parser.parse();

 Element container = doc.getElementById("container");
 assertEquals("Hello world!", container.getElementsByTag("p").text());

 Element link = container.getElementsByTag("a").get(0);
 assertEquals("link.html", link.absUrl("href"));
 }

 @Test
 public void testParseBodyFragment() throws Exception {
 String bodyFragment = "<div><p>Hello world!</p></div>";
 Parser parser = Parser.parseBodyFragment(bodyFragment, "test.html");
 Document doc = parser.parse();
 assertEquals("Hello world!", doc.body().getElementsByTag("p").text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() throws Exception {
 String bodyFragment = "<div><p>Hello world!</p></div>"; // no closing tag
 Parser parser = Parser.parseBodyFragmentRelaxed(bodyFragment, "test.html");
 Document doc = parser.parse();
 assertEquals("Hello world!", doc.body().text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidHtml() throws Exception {
 Parser parser = new Parser("<", "test.html", false); // invalid start tag
 parser.parse();
 }

 @Test
 public void testParseXmlDeclaration() throws Exception {
 String xmlDeclaration = "<xml version=\"1.0\" encoding=\"UTF-8\">";
 Parser parser = new Parser(xmlDeclaration, "test.html", false);
 Document doc = parser.parse();
 XmlDeclaration xmlDecl = (XmlDeclaration) doc.getRootElement();
 assertEquals("UTF-8", xmlDecl.encoding());
 assertEquals("1.0", xmlDecl.version());
 }
}