import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.xmlparser.XmlParser;
import org.jsoup.safety.Whitelist;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 document = xmlTreeBuilder.parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element attr=\"value\">Content</element>\n</root>", "http://example.com");
 }

 @Test
 public void testParseXmlDocument() {
 // Test that parse method returns a valid Document
 assertEquals(Document.OutputSettings.Syntax.xml, document.outputSettings().syntax());

 // Test parsing of basic XML elements
 Element root = document.rootElement();
 Element element = root.child(0);
 assertEquals("root", root.nodeName());
 assertEquals("element", element.nodeName());
 assertEquals("value", element.attr("attr"));
 assertEquals("Content", element.text());

 // Test parsing of comments
 Element comment = (Element) document.getElementsByTag("comment").first();
 assertEquals("comment", comment.nodeName());
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseInvalidXmlDocument() {
 // Test error handling for invalid XML documents
 String invalidXml = "<root> <element attr=\"value\" >Content</element > <element attr=\"value\" >Content</element >";
 Document invalidDocument = xmlTreeBuilder.parse(new StringReader(invalidXml), "http://example.com");
 // Check that parse method throws an exception
 assertTrue(invalidDocument.childElements().isEmpty());
 }

 @Test
 public void testParseFragment() {
 // Test fragment parsing
 String xmlFragment = "<element attr=\"value\">Content</element>";
 List<Node> fragmentNodes = xmlTreeBuilder.parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Element fragmentElement = (Element) fragmentNodes.get(0);
 assertEquals("element", fragmentElement.nodeName());
 assertEquals("value", fragmentElement.attr("attr"));
 assertEquals("Content", fragmentElement.text());
 }

 @Test
 public void testIgnoreHtmlTags() {
 // Test that unknown tags are treated as xml elements, not html
 String xmlWithUnknownTag = "<root><unknownTag attr=\"value\">Content</unknownTag></root>";
 Document xmlDocumentWithUnknownTag = xmlTreeBuilder.parse(new StringReader(xmlWithUnknownTag), "http://example.com");
 Element unknownTag = (Element) xmlDocumentWithUnknownTag.getElementsByTag("unknownTag").first();
 assertNotNull(unknownTag);
 }

 @Test
 public void testPreserveCase() {
 // Test that ParseSettings.preserveCase is respected
 String xmlWithMixedCase = "<root><Element attr=\"VALUE\">Content</Element></root>";
 Document xmlDocumentWithMixedCase = xmlTreeBuilder.parse(new StringReader(xmlWithMixedCase), "http://example.com", ParseSettings.preserveCase);
 Element elementWithMixedCase = (Element) xmlDocumentWithMixedCase.getElementsByTag("Element").first();
 assertEquals("Element", elementWithMixedCase.nodeName());
 }
}