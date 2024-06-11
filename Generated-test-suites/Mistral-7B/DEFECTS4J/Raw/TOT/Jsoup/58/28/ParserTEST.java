import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseError;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.util.List;

public class ParserTest {

 private Parser parser;
 private Document doc;

 @Before
 public void setup() {
 parser = new Parser(new HtmlTreeBuilder());
 }

 @Test
 public void testParseInput() {
 String html = "<html><head><title>Test Page</title></head><body><p>Hello World</p></body></html>";
 doc = parser.parseInput(html, "http://example.com");

 Assert.assertEquals("Test Page", doc.title());
 Assert.assertEquals("Hello World", doc.body().text());
 }

 @Test
 public void testParseInputWithTrackErrors() {
 String html = "<html><head><title>Test Page</title></head><body><script>throw new Error('Test Error')</script></body></html>";
 Parser parserWithErrors = new Parser(new HtmlTreeBuilder());
 parserWithErrors.setTrackErrors(1);
 List<ParseError> errors = parserWithErrors.parseInput(html, "http://example.com").getErrors();

 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("Error parsing start tag : script", errors.get(0).toString());
 }

 @Test
 public void testParseInputWithMaxErrors() {
 String html = "<html><head><title>Test Page</title></head><body><script>throw new Error('Test Error 1')</script><script>throw new Error('Test Error 2')</script></body></html>";
 Parser parserWithErrors = new Parser(new HtmlTreeBuilder());
 parserWithErrors.setTrackErrors(2);
 List<ParseError> errors = parserWithErrors.parseInput(html, "http://example.com").getErrors();

 Assert.assertEquals(2, errors.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetErrorsEmptyList() {
 Parser parser = new Parser(new HtmlTreeBuilder());
 List<ParseError> errors = parser.parseInput("", "").getErrors();
 errors.get(0);
 }

 @Test
 public void testGetTreeBuilder() {
 Parser parser = new Parser(new HtmlTreeBuilder());
 TreeBuilder treeBuilder = parser.getTreeBuilder();
 Assert.assertEquals(HtmlTreeBuilder.class, treeBuilder.getClass());
 }

 @Test
 public void testSetTreeBuilder() {
 Parser parser = new Parser(new HtmlTreeBuilder());
 TreeBuilder newTreeBuilder = new XmlTreeBuilder();
 parser.setTreeBuilder(newTreeBuilder);
 Assert.assertEquals(XmlTreeBuilder.class, parser.getTreeBuilder().getClass());
 }
}