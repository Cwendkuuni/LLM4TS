import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.XmlTreeBuilder.*;
import org.jsoup.parser.Parser.xmlParser;
import org.junit.Assert.*;

public class XmlTreeBuilderTest {

 @Test
 public void testParse() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<root><child1><subchild1/><subchild2/></child1><child2/></root>";
 Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlString), "");

 Element root = xmlDocument.rootElement();
 assertEquals("root", root.tagName());

 List<Node> childNodes = root.childNodes();
 assertEquals(2, childNodes.size());

 Element child1 = (Element) childNodes.get(0);
 assertEquals("child1", child1.tagName());

 List<Node> child1ChildNodes = child1.childNodes();
 assertEquals(2, child1ChildNodes.size());

 Element subchild1 = (Element) child1ChildNodes.get(0);
 assertEquals("subchild1", subchild1.tagName());

 Element subchild2 = (Element) child1ChildNodes.get(1);
 assertEquals("subchild2", subchild2.tagName());

 Element child2 = (Element) childNodes.get(1);
 assertEquals("child2", child2.tagName());
 }

 @Test
 public void testParseFragment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlFragment = "<child1><subchild1/><subchild2/></child1>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);

 Element child1 = (Element) nodes.get(0);
 assertEquals("child1", child1.tagName());

 List<Node> child1ChildNodes = child1.childNodes();
 assertEquals(2, child1ChildNodes.size());

 Element subchild1 = (Element) child1ChildNodes.get(0);
 assertEquals("subchild1", subchild1.tagName());

 Element subchild2 = (Element) child1ChildNodes.get(1);
 assertEquals("subchild2", subchild2.tagName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String invalidInput = "<html>";
 xmlTreeBuilder.parse(new StringReader(invalidInput), "");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBaseUriNull() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.parse(new StringReader("<root/>"), null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testErrorsNull() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.parse(new StringReader("<root/>"), "", null);
 }
}