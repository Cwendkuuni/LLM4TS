import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.ParseSettings;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 document = xmlTreeBuilder.parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element attr=\"value\">Content</element>\n</root>", "baseUrl");
 }

 @Test
 public void testParseValidXml() {
 // Test valid XML parsing
 assertNotNull(document);
 assertEquals("root", document.tagName());
 List<Node> nodes = document.childNodes();
 assertEquals(2, nodes.size());
 Element root = (Element) nodes.get(0);
 assertEquals("root", root.tagName());
 List<Node> children = root.childNodes();
 assertEquals(1, children.size());
 Element element = (Element) children.get(0);
 assertEquals("element", element.tagName());
 assertEquals("value", element.attr("attr"));
 assertEquals("Content", element.text());
 }

 @Test
 public void testParseInvalidXml() {
 // Test invalid XML parsing
 // Malformed XML with an unclosed tag
 String invalidXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element attr=\"value\">Content\n";
 try {
 xmlTreeBuilder.parse(new StringReader(invalidXml), "baseUrl");
 } catch (IllegalArgumentException e) {
 // Expected exception
 assertEquals("Error parsing input: start tag named 'element' was never closed", e.getMessage());
 }
 }

 @Test
 public void testParseXmlWithSelfClosingTag() {
 // Test XML parsing with self-closing tag
 String xmlWithSelfClosingTag = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<image src=\"image.png\" />\n</root>";
 Document documentWithSelfClosingTag = xmlTreeBuilder.parse(new StringReader(xmlWithSelfClosingTag), "baseUrl");
 List<Node> nodesWithSelfClosingTag = documentWithSelfClosingTag.childNodes();
 assertEquals(2, nodesWithSelfClosingTag.size());
 Element rootWithSelfClosingTag = (Element) nodesWithSelfClosingTag.get(0);
 assertEquals("root", rootWithSelfClosingTag.tagName());
 List<Node> childrenWithSelfClosingTag = rootWithSelfClosingTag.childNodes();
 assertEquals(1, childrenWithSelfClosingTag.size());
 Element image = (Element) childrenWithSelfClosingTag.get(0);
 assertEquals("image", image.tagName());
 assertNull(image.text());
 }

 @Test
 public void testParseXmlFragment() {
 // Test XML parsing with a fragment
 String xmlFragment = "<element attr=\"value\">Content</element>";
 List<Node> nodesFromFragment = xmlTreeBuilder.parseFragment(xmlFragment, document, "baseUrl").subList(0, 2);
 assertEquals(2, nodesFromFragment.size());
 Element elementFromFragment = (Element) nodesFromFragment.get(0);
 assertEquals("element", elementFromFragment.tagName());
 assertEquals("value", elementFromFragment.attr("attr"));
 assertEquals("Content", elementFromFragment.text());
 }
}