import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Doctype;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Attributes;
import org.junit.Assert;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParseXMLWithValidInput() {
 String xmlInput = "<root><child><subchild>Hello World</subchild></child></root>";
 Document document = xmlTreeBuilder.parse(new StringReader(xmlInput), "");
 Element rootElement = document.rootElement();
 Assert.assertEquals("root", rootElement.tagName());
 Assert.assertEquals(1, rootElement.childElementCount());
 Element childElement = rootElement.child(0);
 Assert.assertEquals("child", childElement.tagName());
 Assert.assertEquals(1, childElement.childElementCount());
 Element subchildElement = childElement.child(0);
 Assert.assertEquals("subchild", subchildElement.tagName());
 Assert.assertEquals("Hello World", subchildElement.text());
 }

 @Test
 public void testParseXMLWithComments() {
 String xmlInput = "<root><!-- This is a comment --><child><!-- Another comment --><subchild>Hello World</subchild></child></root>";
 Document document = xmlTreeBuilder.parse(new StringReader(xmlInput), "");
 List<Node> commentNodes = document.childNodes();
 Assert.assertEquals(2, commentNodes.size());
 Comment rootComment = (Comment) commentNodes.get(0);
 Assert.assertEquals("This is a comment", rootComment.data());
 Comment childComment = (Comment) commentNodes.get(1);
 Assert.assertEquals("Another comment", childComment.data());
 }

 @Test
 public void testParseXMLWithDoctype() {
 String xmlInput = "<!DOCTYPE root [ <!ATTLIST child attr CDATA #REQUIRED > ]><root><child attr='test'></child></root>";
 Document document = xmlTreeBuilder.parse(new StringReader(xmlInput), "");
 List<Node> nodeList = document.childNodes();
 Doctype doctypeNode = (Doctype) nodeList.get(0);
 Assert.assertEquals("root", doctypeNode.getName());
 Attributes attributes = doctypeNode.attributes();
 Assert.assertEquals(1, attributes.size());
 Assert.assertEquals("child", attributes.getKey(0));
 Assert.assertEquals("CDATA", attributes.getValue(0));
 }

 @Test
 public void testParseXMLWithEmptyInput() {
 String xmlInput = "";
 Document document = xmlTreeBuilder.parse(new StringReader(xmlInput), "");
 Assert.assertNull(document);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseXMLWithInvalidInput() {
 String xmlInput = "<root><child><subchild>Hello <script>World</script>";
 Document document = null;
 try {
 document = xmlTreeBuilder.parse(new StringReader(xmlInput), "");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 Validate.notNull(document);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseXMLWithMissingEndTag() {
 String xmlInput = "<root><child><subchild>Hello World</subchild>";
 Document document = null;
 try {
 document = xmlTreeBuilder.parse(new StringReader(xmlInput), "");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 Validate.notNull(document);
 }
}