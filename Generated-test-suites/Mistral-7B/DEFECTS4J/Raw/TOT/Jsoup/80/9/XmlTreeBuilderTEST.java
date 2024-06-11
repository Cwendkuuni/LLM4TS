import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Doctype;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParseSimpleXml() {
 String xmlInput = "<root><child>Hello World</child></root>";
 Document document = xmlTreeBuilder.parse(new StringReader(xmlInput), "baseUri");
 Element root = document.getRootElement();
 assertEquals("root", root.tagName());
 Element child = root.child(0);
 assertEquals("child", child.tagName());
 TextNode textNode = (TextNode) child.child(0);
 assertEquals("Hello World", textNode.text());
 }

 @Test
 public void testParseXmlWithSelfClosingTag() {
 String xmlInput = "<root><img src='image.jpg' />;</root>";
 Document document = xmlTreeBuilder.parse(new StringReader(xmlInput), "baseUri");
 Element root = document.getRootElement();
 Element img = root.child(0);
 assertEquals("img", img.tagName());
 assertTrue(img.hasAttrs());
 assertEquals("image.jpg", img.attr("src"));
 }

 @Test
 public void testParseXmlWithComment() {
 String xmlInput = "<root><!-- This is a comment --></root>";
 Document document = xmlTreeBuilder.parse(new StringReader(xmlInput), "baseUri");
 Element root = document.getRootElement();
 List<Node> commentNodes = root.childNodes();
 assertTrue(commentNodes.get(0) instanceof Comment);
 Comment comment = (Comment) commentNodes.get(0);
 assertEquals("This is a comment", comment.data());
 }

 @Test
 public void testParseXmlWithCDataSection() {
 String xmlInput = "<root><![CDATA[ <p>This is a CData Section</p> ]]></root>";
 Document document = xmlTreeBuilder.parse(new StringReader(xmlInput), "baseUri");
 Element root = document.getRootElement();
 List<Node> cdataNodes = root.childNodes();
 assertTrue(cdataNodes.get(0) instanceof CDataNode);
 CDataNode cdataNode = (CDataNode) cdataNodes.get(0);
 assertEquals("<p>This is a CData Section</p>", cdataNode.text());
 }

 @Test
 public void testParseXmlWithDoctype() {
 String xmlInput = "<!DOCTYPE html SYSTEM 'about:legacy-compat'>";
 Document document = xmlTreeBuilder.parse(new StringReader(xmlInput), "baseUri");
 Doctype doctypeNode = document.doctype();
 assertEquals("html", doctypeNode.name());
 assertNull(doctypeNode.getPublicIdentifier());
 assertNull(doctypeNode.getSystemIdentifier());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseXmlWithInvalidInput() {
 xmlTreeBuilder = new XmlTreeBuilder();
 String invalidInput = "<html<tag"; // missing '>' tag
 Document document = xmlTreeBuilder.parse(new StringReader(invalidInput), "baseUri");
 fail("Expected IllegalArgumentException with message 'Unexpected end of the document'");
 }
}