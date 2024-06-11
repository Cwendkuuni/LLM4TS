import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

 @Test
 public void testParseSimpleXML() {
 Document xmlDocument = new XmlTreeBuilder().parse("<root><child>Hello</child></root>", "baseURI");
 Element rootElement = xmlDocument.getRootElement();
 assertEquals("root", rootElement.tagName());
 assertEquals("baseURI", rootElement.baseUri());
 Element childElement = rootElement.child(0);
 assertEquals("child", childElement.tagName());
 assertEquals("Hello", childElement.text().getWholeText());
 }

 @Test
 public void testParseXMLWithSelfClosingTag() {
 Document xmlDocument = new XmlTreeBuilder().parse("<root><tag /></root>", "baseURI");
 Element rootElement = xmlDocument.getRootElement();
 assertEquals("root", rootElement.tagName());
 List<Element> tagList = rootElement.select("tag");
 assertEquals(1, tagList.size());
 Element tagElement = tagList.get(0);
 assertTrue(tagElement.isSelfClosing());
 }

 @Test
 public void testParseXMLWithComment() {
 Document xmlDocument = new XmlTreeBuilder().parse("<root><!-- This is a comment --></root>", "baseURI");
 Element rootElement = xmlDocument.getRootElement();
 List<Comment> commentList = rootElement.getAllElementsContaining(Comment.class);
 assertEquals(1, commentList.size());
 assertEquals("This is a comment", commentList.get(0).getData());
 }

 @Test
 public void testParseXMLWithCDATA() {
 Document xmlDocument = new XmlTreeBuilder().parse("<root><![CDATA[this is CDATA]]></root>", "baseURI");
 Element rootElement = xmlDocument.getRootElement();
 List<Node> cdataList = rootElement.getAllElementsContaining(CDataNode.class);
 assertEquals(1, cdataList.size());
 assertEquals("this is CDATA", cdataList.get(0).text().getWholeText());
 }

 @Test
 public void testParseXMLWithDoctype() {
 Document xmlDocument = new XmlTreeBuilder().parse("<!DOCTYPE html SYSTEM \"about:legacy-compat\">", "baseURI");
 DocumentType doctypeNode = xmlDocument.doctype();
 assertEquals("html", doctypeNode.name());
 assertEquals("about:legacy-compat", doctypeNode.systemId());
 }

 @Test
 public void testParseXMLWithErrors() {
 Document xmlDocument = null;
 try {
 xmlDocument = new XmlTreeBuilder().parse("<root><invalidTag>", "baseURI");
 fail("Expected ParseException to be thrown");
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertEquals(0, xmlDocument.childNodes().size());
 }

 @Test
 public void testParseXMLFragment() {
 Document xmlDocument = new XmlTreeBuilder().parse("<root><child>Hello</child></root>", "baseURI");
 List<Node> fragmentNodes = new XmlTreeBuilder().parseFragment("<child/>", xmlDocument, "baseURI").childNodes();
 Element fragmentElement = (Element) fragmentNodes.get(0);
 assertEquals("child", fragmentElement.tagName());
 assertEquals("Hello", xmlDocument.getRootElement().child(0).child(0).text().getWholeText());
 }
}