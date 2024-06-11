import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Doctype;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;
import org.jsoup.helper.ElementFilter;

import java.util.Stack;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 doc = xmlTreeBuilder.parse("<root><tag attr='value'><subtag/><comment>This is a comment</comment></tag></root>");
 }

 @Test
 public void testStartTag() {
 Element root = doc.rootElement();
 Element tag = root.getElementsByTagName("tag").first();
 Element subtag = tag.getElementsByTagName("subtag").first();
 assertEquals("tag", tag.tagName());
 assertEquals("value", tag.attributes().get("attr"));
 assertTrue(subtag.isEmpty());
 }

 @Test
 public void testEndTag() {
 Element root = doc.rootElement();
 Element tag = root.getElementsByTagName("tag").first();
 Element tagEnd = tag.getElementsByTagsName("tag").last();
 assertTrue(tagEnd.tagName().equals("tag"));
 assertTrue(tagEnd.parentNode() == tag);
 }

 @Test
 public void testComment() {
 Element root = doc.rootElement();
 Element comment = root.getElementsByTag("comment").first();
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testCharacter() {
 Element root = doc.rootElement();
 TextNode textNode = (TextNode) root.getElementsByTag("root").first().getElementsByTag("tag").first().getElementsByTagName("text")
 .first();
 assertEquals("", textNode.text());
 }

 @Test
 public void testDoctype() {
 Document doc1 = xmlTreeBuilder.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"DTD/xhtml1-strict.dtd\">");
 DocumentType doctypeNode = doc1.getDoctype();
 assertEquals("html", doctypeNode.name());
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.parse("Unexpected token type: 123");
 }

 @Test
 public void testPopStackToClose() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document doc = xmlTreeBuilder.parse("<root><tag><tag><tag></tag></tag></tag></root>");
 Element root = doc.rootElement();
 Element tag = root.getElementsByTagName("tag").first();
 Element tag1 = tag.getElementsByTagName("tag").first();
 Element tag2 = tag1.getElementsByTagName("tag").first();
 xmlTreeBuilder.popStackToClose(tag2.getTagName());
 assertEquals(2, tag1.children().size());
 assertEquals(1, tag.children().size());
 }

 @Test
 public void testUnknownTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document doc = xmlTreeBuilder.parse("<root><unknown/></root>");
 Element root = doc.rootElement();
 Element unknown = root.getElementsByTagName("unknown").first();
 assertTrue(unknown.isSelfClosing());
 }

 @Test
 public void testSelfClosingTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document doc = xmlTreeBuilder.parse("<root><img src='image.png'/></root>");
 Element root = doc.rootElement();
 Element img = root.getElementsByTagName("img").first();
 assertTrue(img.isSelfClosing());
 }

 @Test
 public void testBaseUri() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document doc = xmlTreeBuilder.parse("<base href='/test'><root><link rel='stylesheet' href='/styles.css'/></root></base>");
 Element base = doc.getElementsByTagName("base").first();
 Element link = doc.getElementsByTagName("link").first();
 assertEquals("/test", base.attr("href"));
 assertEquals("/styles.css", link.attr("href"));
 }

 @Test
 public void testUnknownTagWithBaseUri() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document doc = xmlTreeBuilder.parse("<base href='/test'><root><unknown attr='value'/></root></base>");
 Element base = doc.getElementsByTagName("base").first();
 Element unknown = doc.getElementsByTagName("unknown").first();
 assertEquals("/test/unknown", unknown.attr("abs:href"));
 }
}