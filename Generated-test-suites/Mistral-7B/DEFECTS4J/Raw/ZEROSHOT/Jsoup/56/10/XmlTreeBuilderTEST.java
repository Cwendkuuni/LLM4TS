import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.tokenizer.CharSequenceToken;
import org.jsoup.parser.tokenizer.CommentToken;
import org.jsoup.parser.tokenizer.DoctypeToken;
import org.jsoup.parser.tokenizer.EndTagToken;
import org.jsoup.parser.tokenizer.StartTagToken;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

 @Test
 public void testXmlTreeBuilderParse() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 String xmlInput = "<root><child xmlns:my=\"http://test.com\"><subchild/></child></root>";
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "http://test.com");

 assertEquals("root", xmlDoc.tagName());
 Element root = xmlDoc.rootElement();
 assertEquals(1, root.children().size());
 Element child = root.child(0);
 assertEquals("child", child.tagName());
 assertEquals(1, child.children().size());
 Element subchild = child.child(0);
 assertEquals("subchild", subchild.tagName());
 assertEquals(0, subchild.children().size());
 }

 @Test
 public void testXmlTreeBuilderParseFragment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 String xmlFragment = "<subchild/>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "http://test.com", ParseErrorList.noTracking(), XmlTreeBuilder.ParseSettings.preserveCase);

 Element subchild = (Element) nodes.get(0);
 assertEquals("subchild", subchild.tagName());
 assertEquals(0, subchild.children().size());
 }

 @Test
 public void testXmlTreeBuilderProcessStartTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 String xmlInput = "<root><child xmlns:my=\"http://test.com\"><subchild/></child></root>";
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "http://test.com");
 Element root = xmlDoc.rootElement();
 Element child = root.child(0);
 Element subchild = child.child(0);
 Token.StartTag startTag = new Token.StartTag("subchild", null, null, null, 0, 6, 11);

 xmlTreeBuilder.insert(startTag);
 List<Node> expectedNodes = xmlDoc.childNodes();
 Node subchildNode = expectedNodes.get(2);
 assertEquals("subchild", subchildNode.tagName());
 assertEquals(0, subchildNode.children().size());
 }

 @Test
 public void testXmlTreeBuilderProcessEndTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 String xmlInput = "<root><child xmlns:my=\"http://test.com\"><subchild/></child></root>";
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "http://test.com");
 Element root = xmlDoc.rootElement();
 Element child = root.child(0);
 Element subchild = child.child(0);
 Token.EndTag endTag = new Token.EndTag("subchild", null, 0, 7, 12);

 xmlTreeBuilder.popStackToClose(endTag);
 List<Node> expectedNodes = xmlDoc.childNodes();
 Node subchildNode = expectedNodes.get(1);
 assertEquals("subchild", subchildNode.tagName());
 assertEquals(0, subchildNode.children().size());
 }

 @Test
 public void testXmlTreeBuilderProcessComment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 String xmlInput = "<!-- This is a comment -->";
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "http://test.com");
 List<Node> nodes = xmlDoc.childNodes();
 Node commentNode = nodes.get(0);
 assertEquals("-->", commentNode.tagName());
 assertEquals("This is a comment", commentNode.text());
 }

 @Test
 public void testXmlTreeBuilderProcessCharacter() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 String xmlInput = "<root>Hello World</root>";
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "http://test.com");
 List<Node> nodes = xmlDoc.childNodes();
 Node textNode = nodes.get(1);
 assertEquals("Hello World", textNode.text());
 }

 @Test
 public void testXmlTreeBuilderProcessDoctype() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 String xmlInput = "<!DOCTYPE root SYSTEM \"root.dtd\">";
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "http://test.com");
 Node doctypeNode = xmlDoc.childNodes().get(0);
 assertEquals("root", doctypeNode.tagName());
 assertEquals("root.dtd", doctypeNode.attributes().get("SYSTEM").toString());
 }
}