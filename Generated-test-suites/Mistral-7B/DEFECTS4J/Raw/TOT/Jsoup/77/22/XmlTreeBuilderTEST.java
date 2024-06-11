import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Parser;

public class XmlTreeBuilderTest {

 @Test
 public void testParseXmlWithEmptyInput() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document xmlDoc = xmlTreeBuilder.parse("", "");
 Assert.assertNotNull(xmlDoc);
 Assert.assertEquals(0, xmlDoc.childNodeSize());
 }

 @Test
 public void testParseXmlWithValidInput() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlInput = "<root><element1>Hello World</element1><element2><subelement1>Nested text</subelement1></element2></root>";
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "");

 Assert.assertEquals("root", xmlDoc.tagName());
 Element root = xmlDoc.child(0);
 Assert.assertEquals("element1", root.child(0).tagName());
 Assert.assertEquals("Hello World", root.child(0).text());
 Element element2 = root.child(1);
 Assert.assertEquals("element2", element2.tagName());
 Element subelement1 = element2.child(0);
 Assert.assertEquals("subelement1", subelement1.tagName());
 Assert.assertEquals("Nested text", subelement1.text());
 }

 @Test
 public void testParseXmlWithInvalidInput() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String invalidXmlInput = "<root><element1>Hello <script>World</script></element1>";
 ParseErrorList errors = new ParseErrorList();
 Document xmlDoc = xmlTreeBuilder.parse(new StringReader(invalidXmlInput), "", errors, XmlTreeBuilder.ParseSettings.preserveCase);

 Assert.assertEquals(1, errors.size());
 Assert.assertTrue(errors.get(0) instanceof ElementParseError);
 Assert.assertEquals("Start tag expected, '<' not found", errors.get(0).getMessage());
 }

 @Test
 public void testParseXmlWithEmptyElement() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlInput = "<root></root>";
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "");

 Assert.assertEquals("root", xmlDoc.tagName());
 Assert.assertEquals(0, xmlDoc.childNodeSize());
 }

 @Test
 public void testParseXmlWithSelfClosingTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlInput = "<img src='image.png' />";
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "");

 Assert.assertEquals("img", xmlDoc.tagName());
 Assert.assertEquals(0, xmlDoc.childNodeSize());
 }

 @Test
 public void testParseXmlWithComment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlInput = "<!-- This is a comment --> <root><element>Hello World</element></root>";
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "");

 Element root = xmlDoc.child(0);
 Assert.assertEquals("root", root.tagName());
 Element element = root.child(0);
 Assert.assertEquals("element", element.tagName());
 Assert.assertEquals("Hello World", element.text());
 Element comment = root.child(1);
 Assert.assertEquals("#comment", comment.tagName());
 Assert.assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseXmlWithCDataSection() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlInput = "<root><![CDATA[ <script lang='javascript'>alert('Hello World');</script> ]]> </root>";
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "");

 Element root = xmlDoc.child(0);
 Assert.assertEquals("root", root.tagName());
 Element cdata = root.child(0);
 Assert.assertEquals("CDATA[", cdata.tagName());
 Assert.assertEquals("<script lang='javascript'>alert('Hello World');</script>", cdata.text());
 }

 @Test
 public void testParseXmlWithXmlDeclaration() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlInput = "<?xml version='1.0' encoding='UTF-8'?><root><element>Hello World</element></root>";
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "");

 Assert.assertEquals("<?xml version='1.0' encoding='UTF-8'", xmlDoc.head().outerHtml());
 Element root = xmlDoc.child(0);
 Assert.assertEquals("root", root.tagName());
 Element element = root.child(0);
 Assert.assertEquals("element", element.tagName());
 Assert.assertEquals("Hello World", element.text());
 }

 @Test
 public void testParseXmlWithFragment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlFragment = "<root><element1>Hello World</element1></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "", new ParseErrorList(), XmlTreeBuilder.ParseSettings.preserveCase);
 Document xmlDoc = (Document) nodes.get(0);

 Assert.assertEquals("root", xmlDoc.tagName());
 Element root = xmlDoc.child(0);
 Assert.assertEquals("element1", root.tagName());
 Assert.assertEquals("Hello World", root.text());
 }
}