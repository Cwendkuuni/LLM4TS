import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.XmlTreeBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private String xmlInput;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlInput = "<root><element attr=\"value\"><subElement/></element></root>";
 }

 @Test
 public void testParseValidXml() {
 Document doc = xmlTreeBuilder.parse(xmlInput, null);
 assertNotNull(doc);
 Element root = doc.rootElement();
 Element element = root.child(0);
 assertEquals("element", element.tagName());
 assertEquals("value", element.attributes().get("attr"));
 assertNotNull(element.child(0));
 }

 @Test(expected = ParseError.class)
 public void testParseInvalidXml() {
 xmlInput = "<root><element<subElement/>"; // missing '>' in tag
 Document doc = null;
 try {
 doc = xmlTreeBuilder.parse(xmlInput, null);
 } catch (ParseError e) {
 // expected
 assertEquals("Error parsing tag <element<subElement/>>", e.getMessage());
 }
 assertNull(doc);
 }

 @Test
 public void testParseFragment() {
 List<Node> fragments = xmlTreeBuilder.parseFragment("<fragment/>", null, null, XmlTreeBuilder.ParseSettings.preserveCase);
 Element fragment = (Element) fragments.get(0);
 assertEquals("fragment", fragment.tagName());
 assertNotNull(fragment.childNodes());
 }

 @Test
 public void testProcessStartTag() {
 Token token = new Token.StartTag("element", null, null);
 Element element = xmlTreeBuilder.insert(token);
 assertEquals("element", element.tagName());
 }

 @Test
 public void testProcessEndTag() {
 Token token = new Token.EndTag("element");
 xmlTreeBuilder.popStackToClose(token);
 assertEquals(0, xmlTreeBuilder.stack.size());
 }
}