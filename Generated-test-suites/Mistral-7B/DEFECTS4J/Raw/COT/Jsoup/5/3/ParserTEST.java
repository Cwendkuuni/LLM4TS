import org.jsoup.Jsoup;
import org.jsoup.helper.ElementUtils;
import org.jsoup.nodes.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;

public class ParserTest {

 private LinkedList<Element> stack;
 private Document doc;

 @Test
 public void testParse() {
 Parser parser = new Parser("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", "http://test.com");
 doc = parser.parse();

 Element root = doc.getRootElement();
 Element title = root.selectFirst("title");
 Element h1 = root.selectFirst("h1");

 Assert.assertEquals("Test", title.text());
 Assert.assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseBodyFragment() {
 Parser parser = new Parser("<body><h1>Hello World</h1></body>", "http://test.com");
 doc = parser.parse();

 Element root = doc.getRootElement();
 Element h1 = root.selectFirst("h1");

 Assert.assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 Parser parser = new Parser("<html><body><h1>Hello World</h1></body></html>", "http://test.com");
 doc = parser.parse();

 Element root = doc.getRootElement();
 Element body = root.getElementsByTag("body").get(0);
 Element h1 = body.selectFirst("h1");

 Assert.assertEquals("Hello World", h1.text());
 }

 @Test(expected = NullPointerException.class)
 public void testNullHtml() {
 Parser parser = new Parser(null, "http://test.com", false);
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
 Parser parser = new Parser("<html></html>", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyHtml() {
 Parser parser = new Parser("", "http://test.com", false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidStartTag() {
 Parser parser = new Parser("<x>", "http://test.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidEndTag() {
 Parser parser = new Parser("<html><body><h1></h1></body></html>", "http://test.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidAttribute() {
 Parser parser = new Parser("<html><body><div foo='bar'></div></body></html>", "http://test.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidEndTagWithDataNode() {
 Parser parser = new Parser("<html><body><textarea></textarea></body></html>", "http://test.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidEmptyEndTag() {
 Parser parser = new Parser("<html><body><div></div></div></body></html>", "http://test.com");
 }

 @Test
 public void testComment() {
 Parser parser = new Parser("<html><!-- This is a comment --><body></body></html>", "http://test.com");
 doc = parser.parse();

 Element root = doc.getRootElement();
 Comment comment = root.getElementsByTag("!--").get(0);

 Assert.assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testXmlDeclaration() {
 Parser parser = new Parser("<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xml:base=\"http://test.com/\"><body></body></html>", "http://test.com");
 doc = parser.parse();

 Element root = doc.getRootElement();
 XmlDeclaration xmlDeclaration = root.getElementsByTag("xml").get(0);

 Assert.assertEquals("http://www.w3.org/2001/XMLSchema-instance", xmlDeclaration.xmlnsXsi());
 Assert.assertEquals("http://test.com/", xmlDeclaration.xmlBaseURI());
 }

 @Test
 public void testEmptyElement() {
 Parser parser = new Parser("<html><body><img src=\"image.png\"/><img src=\"image2.png\"/></body></html>", "http://test.com");
 doc = parser.parse();

 Element root = doc.getRootElement();
 Element img1 = root.selectFirst("img");
 Element img2 = root.selectFirst("img");

 Assert.assertEquals(0, img1.children().size());
 Assert.assertEquals(0, img2.children().size());
 }

 @Test
 public void testSelfClosingTag() {
 Parser parser = new Parser("<html><body><img src=\"image.png\" /> </body></html>", "http://test.com");
 doc = parser.parse();

 Element root = doc.getRootElement();
 Element img = root.selectFirst("img");

 Assert.assertEquals("image.png", img.attr("src"));
 Assert.assertEquals(0, img.children().size());
 }

 @Test
 public void testBaseTag() {
 Parser parser = new Parser("<html><head><base href=\"/subdir/\"><title>Test</title></head><body><a href=\"foo.html\">Link to foo.html</a></body></html>", "http://test.com");
 doc = parser.parse();

 Element root = doc.getRootElement();
 Element link = root.selectFirst("a");

 Assert.assertEquals("/subdir/foo.html", link.attr("href"));
 }

 @Test
 public void testParseTextNodeWithSpecialCharacters() {
 Parser parser = new Parser("<html><body><p>Hello &lt; World &amp; Me!</p></body></html>", "http://test.com");
 doc = parser.parse();

 Element root = doc.getRootElement();
 Element p = root.selectFirst("p");

 Assert.assertEquals("Hello < World & Me!", p.text());
 }

 @Test
 public void testParseCdata() {
 Parser parser = new Parser("<html><body><script><![CDATA[function test() { alert('Hello World'); }]]></script></body></html>", "http://test.com");
 doc = parser.parse();

 Element root = doc.getRootElement();
 Element script = root.selectFirst("script");

 Assert.assertNotNull(script.html());
 }
}