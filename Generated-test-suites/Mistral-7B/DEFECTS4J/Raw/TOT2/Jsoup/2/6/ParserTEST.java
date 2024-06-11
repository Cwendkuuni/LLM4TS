import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;

public class ParserTest {

 private static final String HTML_WITH_COMMENT = "<html><!-- This is a comment --></html>";
 private static final String HTML_WITH_XML_DECL = "<html xmlns:html=\"http://www.w3.org/1999/xhtml\">";
 private static final String HTML_WITH_EMPTY_TAG = "<img src=\"image.jpg\" />";
 private static final String HTML_WITH_SELF_CLOSED_TAG = "<input type=\"text\" />";
 private static final String HTML_WITH_DATA_TAG = "<textarea>This is textarea content</textarea>";
 private static final String HTML_WITH_BASE_TAG = "<base href=\"/test/\">";
 private static final String HTML_WITH_IMPLICIT_PARENT = "<div><span>Nested span</span></div>";
 private static final String HTML_WITH_INVALID_PARENT = "<div><p><div></div></p>";
 private static final String HTML_WITH_MISSING_END_TAG = "<div><p>This is a paragraph</p>";

 @Test
 public void testParser_HTML_withComment() {
 Document doc = Parser.parse(HTML_WITH_COMMENT, "");
 assertEquals("This is a comment ", doc.body().child(0).text());
 }

 @Test
 public void testParser_HTML_withXMLDecl() {
 Document doc = Parser.parse(HTML_WITH_XML_DECL, "");
 assertTrue(doc.xml().isStandalone());
 }

 @Test
 public void testParser_HTML_withEmptyTag() {
 Document doc = Parser.parse(HTML_WITH_EMPTY_TAG, "");
 assertEquals(0, doc.body().children().size());
 }

 @Test
 public void testParser_HTML_withSelfClosedTag() {
 Document doc = Parser.parse(HTML_WITH_SELF_CLOSED_TAG, "");
 assertEquals("", doc.body().child(0).text());
 }

 @Test
 public void testParser_HTML_withDataTag_Text() {
 Document doc = Parser.parse(HTML_WITH_DATA_TAG, "");
 assertEquals("This is textarea content", doc.body().child(0).child(0).text());
 }

 @Test
 public void testParser_HTML_withDataTag_Data() {
 Document doc = Parser.parse(HTML_WITH_DATA_TAG, "");
 assertEquals("This is textarea content", doc.body().child(0).child(0).nodeValue());
 }

 @Test
 public void testParser_HTML_withBaseTag() {
 Document doc = Parser.parse(HTML_WITH_BASE_TAG, "");
 assertEquals("/test/", doc.baseUri());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParser_HTML_withInvalidParent() {
 Document doc = Parser.parse(HTML_WITH_INVALID_PARENT, "");
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testParser_HTML_withImplicitParent() {
 Document doc = Parser.parse(HTML_WITH_IMPLICIT_PARENT, "");
 assertEquals("Nested span", doc.body().child(0).child(0).text());
 }

 @Test(expected = IllegalStateException.class)
 public void testParser_HTML_withMissingEndTag() {
 Document doc = Parser.parse(HTML_WITH_MISSING_END_TAG, "");
 fail("Expected IllegalStateException");
 }

}