import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.ElementFilter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.select.Elements;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.jsoup.helper.Validate.isNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<root><element attr1=\"value1\">Hello World</element></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, new ParseErrorList());

 Document doc = nodes.get(0);
 Element root = (Element) doc.childNodes().get(0);
 Element element = (Element) root.childNodes().get(0);

 assertEquals("root", root.nodeName());
 assertEquals("element", element.nodeName());
 assertEquals("value1", element.attr("attr1"));
 assertEquals("Hello World", element.childNodes().get(0).text());
 }

 @Test
 public void testParseInvalidXmlFragment() {
 String invalidXmlFragment = "<root<element";
 List<Node> nodes = xmlTreeBuilder.parseFragment(invalidXmlFragment, null, new ParseErrorList());

 assertEquals(0, nodes.size());
 }

 @Test
 public void testParseFragmentWithComments() {
 String xmlFragment = "<!-- This is a comment --><root><element attr1=\"value1\">Hello World</element><!-- This is another comment --></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, new ParseErrorList());

 Document doc = nodes.get(0);
 Element root = (Element) doc.childNodes().get(0);
 Element element = (Element) root.childNodes().get(0);

 assertEquals("root", root.nodeName());
 assertEquals("element", element.nodeName());
 assertEquals("value1", element.attr("attr1"));
 assertEquals("Hello World", element.childNodes().get(0).text());

 Element comment1 = (Element) ElementFilter.filter(nodes, "_:comment").get(0);
 assertEquals("This is a comment", comment1.text());
 Element comment2 = (Element) ElementFilter.filter(nodes, "_:comment").get(1);
 assertEquals("This is another comment", comment2.text());
 }

 @Test(expected = RuntimeException.class)
 public void testParseFragmentWithInvalidTokenType() {
 String xmlFragment = "<!-- This is a comment --><root<element";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, new ParseErrorList());
 }
}