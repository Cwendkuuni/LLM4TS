import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import static org.junit.Assert.*;

public class ParserTest {

private static final String HTML_WITH_COMMENT = "<html><!-- This is a comment --></html>";
private static final String HTML_WITH_CDATA = "<html><![CDATA[This is CDATA]]></html>";
private static final String XML_DECLARATION = "<html xmlns:foo=\"bar\"></html>";
private static final String EMPTY_TAG = "<img />";
private static final String SELF_CLOSED_TAG = "<input type=\"text\" name=\"username\" />";
private static final String EMPTY_BODY_FRAGMENT = "<body></body>";
private static final String HTML_WITH_BASE = "<html><base href=\"/test\"></html>";

@Test
public void testParse() {
Document doc = Parser.parse(HTML_WITH_COMMENT, "http://test.com");
Element html = doc.body();
assertEquals(htmlTag, html.tag());
assertEquals("http://test.com", html.baseUri());

Comment comment = html.child(0);
assertEquals("This is a comment", comment.text());
}

@Test
public void testParseCdata() {
Document doc = Parser.parse(HTML_WITH_CDATA, "http://test.com");
Element html = doc.body();
assertEquals(htmlTag, html.tag());
 assertEquals("http://test.com", html.baseUri());

TextNode cdata = html.child(0).child(0);
assertEquals("This is CDATA", cdata.text());
}

@Test
public void testParseXmlDeclaration() {
Document doc = Parser.parse(XML_DECLARATION, "http://test.com");
Element html = doc.body();
assertEquals(htmlTag, html.tag());
assertEquals("http://test.com", html.baseUri());

XmlDeclaration xmlDeclaration = html.child(0);
assertEquals("xmlns:foo=\"bar\"", xmlDeclaration.xmlDecl());
}

@Test
public void testParseEmptyTag() {
Document doc = Parser.parse(EMPTY_TAG, "http://test.com");
Element html = doc.body();
assertEquals(htmlTag, html.tag());
assertEquals("http://test.com", html.baseUri());

Element img = html.child(0);
assertTrue(img.tag().isEmpty());
}

@Test
public void testParseSelfClosedTag() {
Document doc = Parser.parse(SELF_CLOSED_TAG, "http://test.com");
Element html = doc.body();
assertEquals(htmlTag, html.tag());
assertEquals("http://test.com", html.baseUri());

Element input = html.child(0);
assertTrue(input.tag().isEmpty());
}

@Test
public void testParseEmptyBodyFragment() {
Document doc = Parser.parseBodyFragment(EMPTY_BODY_FRAGMENT, "http://test.com");
Element html = doc.body();
assertEquals(htmlTag, html.tag());
assertEquals("http://test.com", html.baseUri());
assertEquals(0, html.childSize());
}

@Test
public void testParseWithBaseTag() {
Document doc = Parser.parse(HTML_WITH_BASE, "http://test.com");
Element html = doc.body();
assertEquals(htmlTag, html.tag());
assertEquals("http://test.com/test", html.baseUri());
}

@Test(expected = IllegalArgumentException.class)
public void testNullHtml() {
Parser.parse(null, "http://test.com");
fail("Expected IllegalArgumentException");
}

@Test(expected = IllegalArgumentException.class)
public void testNullBaseUri() {
Parser.parse("", null);
fail("Expected IllegalArgumentException");
}

@Test
public void testParseInvalidTag() {
try {
Parser.parse("<invalid>", "http://test.com");
fail("Expected IllegalStateException");
} catch (IllegalStateException e) {
// pass
}
}
}