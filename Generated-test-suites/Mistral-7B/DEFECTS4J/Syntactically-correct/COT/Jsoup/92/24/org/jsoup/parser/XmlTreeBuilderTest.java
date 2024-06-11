import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Parser;
import org.jsoup.helper.Validate;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;
 private Parser parser;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 doc = xmlTreeBuilder.parse(new StringReader("<root><tag attr='value'/><tag2/><comment>This is a comment</comment></root>"), "http://localhost");
 parser = new Parser(xmlTreeBuilder);
 }

 @Test
 public void testDefaultSettings() {
 Assert.assertEquals("root", doc.tagName());
 }

 @Test
 public void testInitialiseParse() {
 Assert.assertNotNull(xmlTreeBuilder.doc);
 }

 @Test
 public void testParse() {
 Assert.assertNotNull(doc);
 Assert.assertEquals(3, doc.childNodes().size());
 }

 @Test
 public void testProcessStartTag() {
 Element tag = (Element) doc.childNodes().get(0);
 Assert.assertEquals("tag", tag.tagName());
 Assert.assertEquals("value", tag.attr("attr"));
 }

 @Test
 public void testProcessEndTag() {
 Element tag = (Element) doc.childNodes().get(1);
 Assert.assertEquals("tag2", tag.tagName());
 }

 @Test
 public void testProcessComment() {
 Comment comment = (Comment) doc.childNodes().get(2);
 Assert.assertEquals("This is a comment", comment.data());
 }

 @Test
 public void testProcessCharacter() {
 TextNode textNode = (TextNode) doc.childNodes().get(3);
 Assert.assertEquals("", textNode.data()); // This would depend on the actual input
 }

 @Test
 public void testProcessDoctype() {
 // It's not possible to test doctype in a unit test without an actual reader
 // Validate.testInput("<?xml version='1.0' encoding='UTF-8'?>\n", XmlTreeBuilder.class);
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 xmlTreeBuilder.parse(new StringReader("UnexpectedToken"), "http://localhost");
 }

 @Test
 public void testInsertNode() {
 Element tag = new Element("tag");
 xmlTreeBuilder.insertNode(tag);
 Element root = doc.childNodes().get(0);
 Assert.assertEquals(tag, root.childNodes().get(0));
 }

 @Test
 public void testInsertComment() {
 Comment comment = new Comment("This is a comment");
 xmlTreeBuilder.insert(new Token.Comment("This is a comment"));
 Node root = doc.childNodes().get(0);
 Assert.assertEquals(comment, root.childNodes().get(3));
 }

 @Test
 public void testInsertCharacter() {
 TextNode textNode = new TextNode("Character data");
 xmlTreeBuilder.insert(new Token.Character('C', "Character data"));
 Node root = doc.childNodes().get(0);
 Assert.assertEquals(textNode, root.childNodes().get(4));
 }

 @Test
 public void testInsertDoctype() {
 DocumentType doctypeNode = new DocumentType("root", null, null);
 xmlTreeBuilder.insert(new Token.Doctype("root"));
 Node root = doc.childNodes().get(0);
 Assert.assertEquals(doctypeNode, root.childNodes().get(0));
 }

 @Test
 public void testPopStackToClose() {
 Element tag1 = new Element("tag1");
 Element tag2 = new Element("tag2");
 stack.push(tag1);
 stack.push(tag2);
 xmlTreeBuilder.popStackToClose(new Token.EndTag("tag1"));
 stack.push(tag1);
 Assert.assertEquals(2, stack.size());
 Assert.assertEquals(tag2, stack.peek());
 }

 @Test(expected = AssertionError.class)
 public void testPopStackToCloseUnknownTag() {
 Element tag1 = new Element("tag1");
 Element tag2 = new Element("tag2");
 stack.push(tag1);
 stack.push(tag2);
 xmlTreeBuilder.popStackToClose(new Token.EndTag("tag3"));
 }

 @Test
 public void testParseFragment() {
 String inputFragment = "<tag attr='value'>Test</tag>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, null, null, parser);
 Assert.assertEquals(1, nodes.size());
 Assert.assertEquals(inputFragment, nodes.get(0).outerHtml());
 }

 @Test(expected = AssertionError.class)
 public void testParseFragmentInvalidInput() {
 xmlTreeBuilder.parseFragment("InvalidInput", null, null, parser);
 }

}