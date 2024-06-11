import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ParserTest {

 private Parser parser;
 private Document doc;

 @Test
 public void testParserWithValidInput() {
 parser = new Parser(new HtmlTreeBuilder());
 doc = parser.parseInput("<html><head></head><body></body></html>", null);
 Assert.assertNotNull(doc);
 }

 @Test
 public void testGetTreeBuilder() {
 parser = new Parser(new HtmlTreeBuilder());
 Assert.assertEquals(new HtmlTreeBuilder(), parser.getTreeBuilder());
 }

 @Test
 public void testSetTreeBuilder() {
 parser = new Parser(new HtmlTreeBuilder());
 TreeBuilder newTreeBuilder = new XmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 Assert.assertEquals(newTreeBuilder, parser.getTreeBuilder());
 }

 @Test
 public void testIsTrackErrors() {
 parser = new Parser(new HtmlTreeBuilder());
 Assert.assertFalse(parser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrors() {
 parser = new Parser(new HtmlTreeBuilder());
 parser.setTrackErrors(5);
 Assert.assertTrue(parser.isTrackErrors());
 }

 @Test
 public void testGetErrors() {
 parser = new Parser(new HtmlTreeBuilder());
 parser.setTrackErrors(5);
 List<ParseError> errors = parser.parseInput("<invalid>", null);
 Assert.assertNotNull(errors);
 Assert.assertEquals(5, errors.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullInput() {
 parser = new Parser(new HtmlTreeBuilder());
 parser.parseInput(null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithEmptyInput() {
 parser = new Parser(new HtmlTreeBuilder());
 parser.parseInput("", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidInput() {
 parser = new Parser(new HtmlTreeBuilder());
 parser.parseInput("&%^@!", null);
 }

 @Test
 public void testParseInputWithTrackErrors() {
 parser = new Parser(new HtmlTreeBuilder());
 parser.setTrackErrors(2);
 List<ParseError> errors = parser.parseInput("<html><head></head><body></body><script>alert('xss')</script></html>", null);
 Assert.assertNotNull(errors);
 Assert.assertEquals(2, errors.size());
 }

 @Test
 public void testParseInputWithNoTrackErrors() {
 parser = new Parser(new HtmlTreeBuilder());
 List<ParseError> errors = parser.parseInput("<html><head></head><body></body></html>", null);
 Assert.assertEquals(0, errors.size());
 }

 @Test
 public void testStaticParseFunction() {
 doc = Jsoup.parse("<html><head></head><body></body></html>");
 Assert.assertNotNull(doc);
 }

 @Test
 public void testStaticParseFragment() {
 Element context = new Element("div");
 List<Node> nodes = Parser.parseFragment("<p>Hello World</p>", context, null);
 Assert.assertNotNull(nodes);
 Assert.assertEquals(1, nodes.size());
 }

 @Test
 public void testStaticParseBodyFragment() {
 doc = Parser.parseBodyFragment("<p>Hello World</p>", null);
 Assert.assertNotNull(doc);
 Element body = doc.body();
 Assert.assertNotNull(body);
 List<Node> nodes = body.childNodes();
 Assert.assertNotNull(nodes);
 Assert.assertEquals(1, nodes.size());
 }

 @Test
 public void testStaticParseBodyFragmentRelaxed() {
 doc = Jsoup.parseBodyFragment("<p>Hello World</p>");
 Assert.assertNotNull(doc);
 Element body = doc.body();
 Assert.assertNotNull(body);
 List<Node> nodes = body.childNodes();
 Assert.assertNotNull(nodes);
 Assert.assertEquals(1, nodes.size());
 }

 @Test
 public void testHtmlParser() {
 parser = Parser.htmlParser();
 doc = parser.parseInput("<html><head></head><body></body></html>", null);
 Assert.assertNotNull(doc);
 }

 @Test
 public void testXmlParser() {
 parser = Parser.xmlParser();
 doc = parser.parseInput("<xml></xml>", null);
 Assert.assertNotNull(doc);
 }
}