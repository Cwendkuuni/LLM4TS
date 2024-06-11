import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder parser;

 @Before
 public void setup() {
 parser = new XmlTreeBuilder();
 }

 @Test
 public void testParseEmptyXml() {
 Document doc = parser.parse("", "");
 Element root = doc.getRootElement();
 assertEquals(Element.VOID_ELEMENT_TYPE, root.elementType());
 assertTrue(doc.childNodes().isEmpty());
 }

 @Test
 public void testParseSimpleXml() {
 String xml = "<root><child>Hello World</child></root>";
 Document doc = parser.parse(new StringReader(xml), "");
 Element root = doc.getRootElement();
 assertEquals("root", root.tagName());
 Element child = root.child(0);
 assertEquals("child", child.tagName());
 assertEquals("Hello World", child.text());
 }

 @Test
 public void testParseXmlWithSelfClosingTag() {
 String xml = "<root><img src=\"image.jpg\" /></root>";
 Document doc = parser.parse(new StringReader(xml), "");
 Element root = doc.getRootElement();
 Element img = root.child(0);
 assertEquals("img", img.tagName());
 assertTrue(img.hasAttrs());
 assertEquals("image.jpg", img.attr("src"));
 }

 @Test
 public void testParseXmlWithComment() {
 String xml = "<root><!-- This is a comment --></root>";
 Document doc = parser.parse(new StringReader(xml), "");
 Element root = doc.getRootElement();
 List<Node> comments = doc.body().childNodes();
 assertEquals(1, comments.size());
 assertInstanceOf(Comment.class, comments.get(0));
 }

 @Test
 public void testParseXmlWithCDataSection() {
 String xml = "<root><![CDATA[This is a CData section]]></root>";
 Document doc = parser.parse(new StringReader(xml), "");
 Element root = doc.getRootElement();
 List<Node> cdataSections = doc.body().childNodes();
 assertEquals(1, cdataSections.size());
 assertInstanceOf(CDataNode.class, cdataSections.get(0));
 }

 @Test
 public void testParseXmlWithDoctype() {
 String xml = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";
 Document doc = parser.parse(new StringReader(xml), "");
 DocumentType doctype = doc.doctype();
 assertEquals("html", doctype.name());
 assertNull(doctype.publicId());
 assertNull(doctype.systemId());
 }

 @Test
 public void testParseXmlWithError() {
 String xml = "<root><child<ERROR>";
 ParseErrorList errors = new ParseErrorList();
 Document doc = parser.parse(new StringReader(xml), "", errors, XmlTreeBuilder.ParseSettings.preserveCase);
 assertEquals(1, errors.size());
 assertEquals("unclosed-tag", errors.get(0).getType());
 assertEquals("child", errors.get(0).getTagName());
 }
}