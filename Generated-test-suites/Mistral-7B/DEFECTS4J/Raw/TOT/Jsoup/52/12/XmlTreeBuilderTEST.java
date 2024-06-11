import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XmlTreeBuilderTest {

 @Test
 public void testParseXmlFragment() {
 Document xmlDoc = Jsoup.parse("<test xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:x='http://www.example.com'><root><child1>Hello World</child1><child2><grandchild>Element with attributes <x:att1 xsi:type='x:String'>Attribute Value</x:att1></grandchild></child2></root>", "http://www.example.com/test.xml", Parser.xmlParser());

 Element root = xmlDoc.rootElement();
 assertEquals("root", root.tagName());
 assertEquals(2, root.childNodeSize());

 Element child1 = root.child(0);
 assertEquals("child1", child1.tagName());
 assertEquals("Hello World", child1.text());

 Element child2 = root.child(1);
 assertEquals("child2", child2.tagName());
 assertEquals(1, child2.childNodeSize());

 Element grandchild = child2.child(0);
 assertEquals("grandchild", grandchild.tagName());
 assertEquals("Element with attributes Attribute Value", grandchild.text());

 Element att1 = grandchild.getElementsByTag("att1").first();
 assertEquals("att1", att1.tagName());
 assertEquals("Attribute Value", att1.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 try {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Document xmlDoc = builder.parseFragment("</test>", "", new ParseErrorList());
 } catch (IllegalArgumentException e) {
 // expected
 assertEquals("Unexpected token type: -1", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInputWithComment() {
 try {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Document xmlDoc = builder.parseFragment("<!-- Comment --><test xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:x='http://www.example.com'><root><child1>Hello World</child1><child2><grandchild>Element with attributes <x:att1 xsi:type='x:String'>Attribute Value</x:att1></grandchild></child2></root>", "", new ParseErrorList());
 } catch (IllegalArgumentException e) {
 // expected
 assertEquals("Unexpected token type: -2", e.getMessage());
 }
 }
}