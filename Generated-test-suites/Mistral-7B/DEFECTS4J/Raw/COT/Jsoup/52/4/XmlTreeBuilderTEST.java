import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.jsoup.parser.XmlTreeBuilder;
import java.util.ArrayList;
import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 @Test
 public void testInitialiseParse() {
 Document doc = Jsoup.parse("<xml/>", "", xmlTreeBuilder);
 Assert.assertNotNull(doc);
 }

 @Test
 public void testProcess_StartTag() {
 String xml = "<root><child tag=\"test\"/></root>";
 Document doc = Jsoup.parse(xml, "", xmlTreeBuilder);
 Element root = doc.rootElement();
 Element child = root.child(0);
 Assert.assertEquals("root", root.tagName());
 Assert.assertEquals("child", child.tagName());
 Assert.assertEquals("test", child.attr("tag"));
 }

 @Test
 public void testProcess_EndTag() {
 String xml = "<root><child tag=\"test\"/></root>";
 Document doc = Jsoup.parse(xml, "", xmlTreeBuilder);
 Element root = doc.rootElement();
 Element child = root.child(0);
 int index = root.indexOf(child);
 root.remove(index);
 }

 @Test(expected = AssertionError.class)
 public void testProcess_UnexpectedTokenType() {
 String xml = "<root><child tag=\"test\"/><!invalid>";
 Document doc = Jsoup.parse(xml, "", xmlTreeBuilder);
 }

 @Test
 public void testInsertNode() {
 String xml = "<root><child tag=\"test\"/></root>";
 Document doc = Jsoup.parse(xml, "", xmlTreeBuilder);
 Element root = doc.rootElement();
 Element child = root.child(0);
 Node node = new Element("grandChild", root.baseUri());
 xmlTreeBuilder.insertNode(node);
 root.appendChild(node);
 }

 @Test
 public void testInsert_Comment() {
 String xml = "<!--comment-->";
 Document doc = Jsoup.parse(xml, "", xmlTreeBuilder);
 List<Node> nodes = doc.childNodes();
 Node node = nodes.get(0);
 Assert.assertTrue(node instanceof Comment);
 }

 @Test
 public void testInsert_Character() {
 String xml = "<root>text</root>";
 Document doc = Jsoup.parse(xml, "", xmlTreeBuilder);
 List<Node> nodes = doc.childNodes();
 Node node = nodes.get(2);
 Assert.assertTrue(node instanceof TextNode);
 Assert.assertEquals("text", node.text());
 }

 @Test
 public void testInsert_Doctype() {
 String xml = "<!DOCTYPE xml PUBLIC \"-//W3C//DTD XML 1.0//EN\" \"http://www.w3.org/TR/xml\">";
 Document doc = Jsoup.parse(xml, "", xmlTreeBuilder);
 Node node = doc.childNodes().get(0);
 Assert.assertTrue(node instanceof XmlDeclaration);
 }

 @Test
 public void testPopStackToClose_Found() {
 String xml = "<root><child tag=\"test\"/><child tag=\"test\"/></root>";
 Document doc = Jsoup.parse(xml, "", xmlTreeBuilder);
 Element root = doc.rootElement();
 Element child1 = root.child(0);
 Element child2 = root.child(1);
 xmlTreeBuilder.popStackToClose((Token.EndTag) Tokenizer.tokenize("<child tag=\"test\"/>").next());
 Assert.assertEquals(1, root.childSize());
 Assert.assertEquals(child1, root.child(0));
 }

 @Test
 public void testPopStackToClose_NotFound() {
 String xml = "<root><child tag=\"test\"/><child tag=\"test\"/></root>";
 Document doc = Jsoup.parse(xml, "", xmlTreeBuilder);
 xmlTreeBuilder.popStackToClose((Token.EndTag) Tokenizer.tokenize("<unknownTag/>").next());
 Assert.assertEquals(2, doc.childSize());
 }

 @Test
 public void testParseFragment() {
 String xml = "<root><child tag=\"test\"/><child tag=\"test\"/></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xml, "", new ParseErrorList());
 Assert.assertEquals(2, nodes.size());
 }
}