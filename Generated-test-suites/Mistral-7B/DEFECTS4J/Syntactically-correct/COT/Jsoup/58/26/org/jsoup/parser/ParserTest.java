import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.ParseError;
import java.util.ArrayList;
import java.util.List;

public class ParserTest {

private Parser parser;
private TreeBuilder treeBuilder;

@Before
public void setup() {
treeBuilder = new HtmlTreeBuilder();
parser = new Parser(treeBuilder);
}

@Test
public void testParseInput() {
String html = "<html><body><h1>Hello World</h1></body></html>";
Document doc = parser.parseInput(html, null);
assert(doc != null);
assert(doc.tagName().equals("html"));
}

@Test
public void testParseInputWithBaseUri() {
String html = "<img src=\"image.jpg\" alt=\"Image\">";
String baseUri = "https://example.com";
Document doc = parser.parseInput(html, baseUri);
Element img = doc.select("img").first();
assert(img != null);
assert(img.attr("abs:src").equals("https://example.com/image.jpg"));
}

@Test
public void testGetTreeBuilder() {
assert(parser.getTreeBuilder() == treeBuilder);
}

@Test
public void testSetTreeBuilder() {
TreeBuilder newTreeBuilder = new XmlTreeBuilder();
parser.setTreeBuilder(newTreeBuilder);
assert(parser.getTreeBuilder() == newTreeBuilder);
}

@Test
public void testIsTrackErrors() {
parser.setTrackErrors(1);
assertTrue(parser.isTrackErrors());
parser.setTrackErrors(0);
assertFalse(parser.isTrackErrors());
}

@Test
public void testGetErrors() {
parseErrorWithTracking();
List<ParseError> errors = parser.getErrors();
assertFalse(errors.isEmpty());
}

@Test
public void testSettings() {
ParseSettings newSettings = new ParseSettings.Builder().build();
parser.settings(newSettings);
assert(parser.settings() == newSettings);
}

@Test
public void testParse() {
Document doc = Parser.parse("<html><body><h1>Hello World</h1></body></html>", null);
assert(doc != null);
assert(doc.tagName().equals("html"));
}

@Test
public void testParseFragment() {
String fragmentHtml = "<div><p>Hello World</p></div>";
List<Node> nodes = Parser.parseFragment(fragmentHtml, null, null);
assertFalse(nodes.isEmpty());
}

@Test
public void testParseXmlFragment() {
String fragmentXml = "<xml><data>Hello World</data></xml>";
List<Node> nodes = Parser.parseXmlFragment(fragmentXml, null);
assertFalse(nodes.isEmpty());
}

@Test
public void testParseBodyFragment() {
String bodyHtml = "<body><h1>Hello World</h1></body>";
Document doc = Parser.parseBodyFragment(bodyHtml, null);
assert(doc != null);
assert(doc.body().tagName().equals("body"));
}

@Test(expected = IllegalArgumentException.class)
public void testParseInputNullHtml() {
parser.parseInput(null, null);
}

@Test(expected = IllegalArgumentException.class)
public void testParseInputNullBaseUri() {
parser.parseInput("<html></html>", null);
}

@Test(expected = IllegalArgumentException.class)
public void testParseFragmentNullFragmentHtml() {
Parser.parseFragment(null, null, null);
}

@Test(expected = IllegalArgumentException.class)
public void testParseFragmentNullContext() {
Parser.parseFragment("<html></html>", null, null);
}

@Test(expected = IllegalArgumentException.class)
public void testParseXmlFragmentNullFragmentXml() {
Parser.parseXmlFragment(null, null);
}

@Test
public void testUnescapeEntities() {
String string = "&lt;Hello&gt; &amp; World &amp;amp; &apos;Quotation Mark&apos; &quot;Double Quotation Mark&quot;";
String unescapedString = Parser.unescapeEntities(string, false);
assert(unescapedString.equals("<Hello> &amp; World &amp;amp; 'Quotation Mark' \"Double Quotation Mark\""));
}

private void parseErrorWithTracking() {
String html = "<script><script>";
parser.parseInput(html, null);
}
}