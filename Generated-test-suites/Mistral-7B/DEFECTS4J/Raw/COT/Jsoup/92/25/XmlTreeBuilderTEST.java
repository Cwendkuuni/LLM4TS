import org.junit.Before;
import org.junit.Test;
import org.jsoup.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 doc = xmlTreeBuilder.parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
 "<root>\n" +
 "<element1 attr1=\"value1\">Content 1</element1>\n" +
 "<element2>\n" +
 "<subElement1 attr2=\"value2\">Content 2</subElement1>\n" +
 "</element2>\n" +
 "</root>", "http://test.com");
 }

 @Test
 public void testDefaultSettings() {
 ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.PreserveCase.class, parseSettings.getClass());
 }

 @Test
 public void testInitialiseParse() {
 assertNotNull(doc);
 assertEquals("http://test.com", doc.baseUri());
 }

 @Test
 public void testParse() {
 assertNotNull(xmlTreeBuilder.parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
 "<root>\n" +
 "<element1 attr1=\"value1\">Content 1</element1>\n" +
 "<element2>\n" +
 "<subElement1 attr2=\"value2\">Content 2</subElement1>\n" +
 "</element2>\n" +
 "</root>", "http://test.com"));
 }

 @Test
 public void testProcessStartTag() {
 Element root = doc.rootElement();
 Element element1 = root.child(0);
 Element element2 = root.child(1);
 EqualsVerifier.forClass(Element.class);
 EqualsVerifier.forClass(Tag.class);
 EqualsVerifier.verify(element1);
 EqualsVerifier.verify(element2);
 }

 @Test
 public void testProcessEndTag() {
 Element root = doc.rootElement();
 assertEquals(2, root.childElements().size());
 root.remove(1); // remove element2
 assertEquals(1, root.childElements().size());
 }

 @Test
 public void testProcessComment() {
 List<Node> commentNodes = doc.childNodes();
 assertEquals(1, commentNodes.size());
 assertEquals("<!--  -->", commentNodes.get(0).outerHtml());
 }

 @Test
 public void testProcessCharacter() {
 List<Node> textNodes = doc.textNodes();
 assertEquals(5, textNodes.size());
 assertEquals("Content 1", textNodes.get(0).text());
 assertEquals("", textNodes.get(1).text());
 assertEquals("", textNodes.get(2).text());
 assertEquals("Content 2", textNodes.get(3).text());
 assertEquals("", textNodes.get(4).text());
 }

 @Test
 public void testProcessDoctype() {
 DocumentType doctype = doc.doctype();
 assertEquals("root", doctype.name());
 assertEquals("http://test.com/xml.dtd", doctype.systemIdentifier());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessUnexpectedToken() {
 xmlTreeBuilder.parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
 "<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "http://test.com");
 }

 @Test(expected = NullPointerException.class)
 public void testNullInput() {
 xmlTreeBuilder.parse(null, "http://test.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 xmlTreeBuilder.parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
 "<root>\n" +
 "<element1 attr1=\"value1\">Content 1</element1>\n" +
 "</root>", null);
 }

 @Test
 public void testParseFragment() {
 List<Node> fragmentNodes = xmlTreeBuilder.parseFragment("<root><element1 attr1=\"value1\">Content 1</element1></root>", "http://test.com").get(0).childNodes();
 assertEquals(1, fragmentNodes.size());
 assertEquals("Content 1", fragmentNodes.get(0).text());
 }

 @Test(expected = NullPointerException.class)
 public void testParseFragmentNullInput() {
 xmlTreeBuilder.parseFragment(null, "http://test.com");
 }

 @Test
 public void testParseFragmentEmptyInput() {
 List<Node> fragmentNodes = xmlTreeBuilder.parseFragment("", "http://test.com").get(0).childNodes();
 assertEquals(0, fragmentNodes.size());
 }
}