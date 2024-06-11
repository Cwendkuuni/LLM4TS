import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Token;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParse() {
 String xmlInput = "<root><element1 attr1=\"value1\">Content 1</element1><element2 attr2=\"value2\"/></root>";
 Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlInput), "baseUri");

 Element root = xmlDocument.rootElement();
 Element element1 = root.getElementsByTagName("element1").get(0);
 Element element2 = root.getElementsByTagName("element2").get(0);

 assertEquals("root", root.tagName());
 assertEquals("element1", element1.tagName());
 assertEquals("element2", element2.tagName());
 assertEquals("value1", element1.attribute("attr1"));
 assertEquals("value2", element2.attribute("attr2"));
 assertEquals("Content 1", element1.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullInput() {
 xmlTreeBuilder.parse(null, "baseUri");
 }

 @Test
 public void testParseWithEmptyInput() {
 Document xmlDocument = xmlTreeBuilder.parse(new StringReader(""), "baseUri");
 assertEquals("", xmlDocument.body().text());
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<element attr=\"value\">Content</element>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Element element = (Element) nodes.get(0);
 assertEquals("element", element.tagName());
 assertEquals("value", element.attribute("attr"));
 assertEquals("Content", element.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithNullInput() {
 xmlTreeBuilder.parseFragment(null, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 }
}