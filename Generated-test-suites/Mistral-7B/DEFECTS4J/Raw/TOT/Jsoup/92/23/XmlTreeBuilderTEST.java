import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.TextDocument;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParseEmptyXML() throws IOException {
 String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
 document = xmlTreeBuilder.parse(new StringReader(xml), null);
 assertNotNull(document);
 assertEquals(1, document.childNodes().size());
 }

 @Test
 public void testParseBasicXML() throws IOException {
 String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element attr=\"value\" />\n<element2>\n<subelement />\n</element2>\n</root>";
 document = xmlTreeBuilder.parse(new StringReader(xml), null);

 Element root = document.body();
 assertNotNull(root);
 assertEquals("root", root.nodeName());

 Element element = (Element) document.selectFirst("#element");
 assertNotNull(element);
 assertEquals("element", element.nodeName());
 assertEquals("value", element.attr("attr").get());

 Element element2 = (Element) document.selectFirst("#element2");
 assertNotNull(element2);
 assertEquals("element2", element2.nodeName());

 Element subelement = (Element) document.selectFirst("#subelement");
 assertNotNull(subelement);
 assertEquals("subelement", subelement.nodeName());
 }

 @Test
 public void testProcessSelfClosingTag() throws IOException {
 String xml = "<tag />";
 document = xmlTreeBuilder.parse(new StringReader(xml), null);
 Element root = document.body();
 assertEquals(1, root.childNodes().size());
 Element tag = (Element) root.childNodes().get(0);
 assertEquals("tag", tag.nodeName());
 assertFalse(tag.hasText());
 }

 @Test
 public void testProcessComment() throws IOException {
 String xml = "<!-- This is a comment -->";
 document = xmlTreeBuilder.parse(new StringReader(xml), null);
 List<Node> comments = document.selectNodes("//comment()");
 assertEquals(1, comments.size());
 assertEquals("This is a comment", comments.get(0).text());
 }

 @Test
 public void testProcessDoctype() throws IOException {
 String xml = "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED > ]>\n<root><element attr=\"value\" /></root>";
 document = xmlTreeBuilder.parse(new StringReader(xml), null);
 DocumentType doctype = document.doctype();
 assertNotNull(doctype);
 assertEquals("root", doctype.nodeName());
 assertEquals("CDATA", doctype.attributes().get("ATTLIST").attributes().get("element").attributes().get("attr").type());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessInvalidXML() throws IOException {
 String xml = "<html>\n<body>\n<root>\n"; // missing end tag
 document = xmlTreeBuilder.parse(new StringReader(xml), null);
 // exception should be thrown here
 document.body();
 }
}