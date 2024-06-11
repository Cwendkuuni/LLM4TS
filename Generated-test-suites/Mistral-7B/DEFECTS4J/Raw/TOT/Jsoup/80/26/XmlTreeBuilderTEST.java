import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import java.io.StringReader;
import java.util.List;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParseEmptyXML() {
 String xml = "<xml/>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "baseUri");
 Element root = doc.getRootElement();
 assertTrue(root.tagName().equals("xml"));
 assertEquals(0, root.childNodeSize());
 }

 @Test
 public void testParseXMLWithElements() {
 String xml = "<root><child1>Hello</child1><child2><grandchild>World</grandchild></child2></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "baseUri");
 Element root = doc.getRootElement();
 Element child1 = root.getChild(0);
 Element child2 = root.getChild(1);
 Element grandchild = child2.getChild(0);
 assertEquals("root", root.tagName());
 assertEquals("child1", child1.tagName());
 assertEquals("Hello", child1.text());
 assertEquals("child2", child2.tagName());
 assertEquals("grandchild", grandchild.tagName());
 assertEquals("World", grandchild.text());
 }

 @Test
 public void testParseXMLWithComments() {
 String xml = "<root><!-- This is a comment --><child>Hello</child></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "baseUri");
 Element root = doc.getRootElement();
 Element child = root.getChild(0);
 assertEquals(1, root.childNodeSize());
 assertTrue(root.getChild(0).getChildren().isEmpty());
 assertTrue(child.getChildren().isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseXMLWithInvalidInput() {
 String xml = "<root>"; // missing closing tag
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "baseUri");
 fail("Expected IllegalArgumentException but got: " + doc);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseXMLWithInvalidInput2() {
 String xml = "<root><child><!-- This is a comment --></child>"; // comment not closed
 Document doc = xmlTreeBuilder.parse(new StringReader(xml), "baseUri");
 fail("Expected IllegalArgumentException but got: " + doc);
 }
}