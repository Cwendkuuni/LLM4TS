import org.junit.Test;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Doctype;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.BaseElement;
import org.jsoup.nodes.AttributeSelector;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ElementSelector;
import org.jsoup.nodes.NodeFilter;
import org.jsoup.nodes.ElementParent;
import org.jsoup.nodes.NodeTraversor;
import org.jsoup.nodes.Traverser;
import org.jsoup.nodes.Whitespace;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.XmlTree;
import org.jsoup.nodes.Xml;
import org.jsoup.nodes.ElementParent;
import org.jsoup.nodes.ElementParent.NamespacedName;
import org.jsoup.nodes.Node;
import org.junit.Assert;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

 @Test
 public void testParse() {
 Document doc = new XmlTreeBuilder().parse("<test xmlns:my=\"http://example.com/\"><my:name>John</my:names></test>", null);
 Assert.assertNotNull(doc);
 Assert.assertEquals("test", doc.tagName());
 Element nameElement = doc.selectFirst("my:name");
 Assert.assertNotNull(nameElement);
 Assert.assertEquals("John", nameElement.text());
 }

 @Test
 public void testParseWithErrors() {
 ParseErrorList errors = new ParseErrorList();
 Document doc = new XmlTreeBuilder().parse("<test xmlns:my=\"http://example.com/\"><my:name>John", null, errors);
 Assert.assertNotNull(doc);
 Assert.assertFalse(errors.size() > 0);
 }

 @Test
 public void testParseFragment() {
 List<Node> nodes = new XmlTreeBuilder().parseFragment("<test xmlns:my=\"http://example.com/\"><my:name>John</my:name><my:age>30</my:age></test>", null, null, XmlTreeBuilder.ParseSettings.preserveCase);
 Assert.assertNotNull(nodes);
 Assert.assertEquals(3, nodes.size());
 Node nameNode = nodes.get(0);
 Assert.assertTrue(nameNode instanceof Element);
 Element nameElement = (Element) nameNode;
 Assert.assertEquals("name", nameElement.tagName());
 Assert.assertEquals("John", nameElement.text());
 Node ageNode = nodes.get(1);
 Assert.assertTrue(ageNode instanceof Element);
 Element ageElement = (Element) ageNode;
 Assert.assertEquals("age", ageElement.tagName());
 Assert.assertEquals("30", ageElement.text());
 Node textNode = nodes.get(2);
 Assert.assertTrue(textNode instanceof TextNode);
 Assert.assertEquals("", textNode.text());
 }

 @Test
 public void testParseWithDoctype() {
 Document doc = new XmlTreeBuilder().parse("<!DOCTYPE html SYSTEM \"html.dtd\">", null);
 Assert.assertNotNull(doc);
 Assert.assertEquals("html", doc.tagName());
 DocumentType doctypeNode = doc.doctype();
 Assert.assertNotNull(doctypeNode);
 Assert.assertEquals("html", doctypeNode.name());
 Assert.assertNull(doctypeNode.systemIdentifier());
 Assert.assertNull(doctypeNode.publicIdentifier());
 }

 @Test
 public void testParseWithEmptyInput() {
 Document doc = new XmlTreeBuilder().parse("", null);
 Assert.assertNotNull(doc);
 Assert.assertEquals("", doc.outerHtml());
 }

 @Test
 public void testParseWithComments() {
 Document doc = new XmlTreeBuilder().parse("<test><!-- comment1 --><my:name>John</my:name><!-- comment2 --></test>", null);
 Assert.assertNotNull(doc);
 Element myNameElement = doc.selectFirst("my:name");
 Assert.assertNotNull(myNameElement);
 Assert.assertEquals("John", myNameElement.text());
 List<Node> comments = doc.selectNodes("--*");
 Assert.assertEquals(2, comments.size());
 Comment comment1 = (Comment) comments.get(0);
 Assert.assertEquals("comment1", comment1.data());
 Comment comment2 = (Comment) comments.get(1);
 Assert.assertEquals("comment2", comment2.data());
 }

 @Test
 public void testParseWithCData() {
 Document doc = new XmlTreeBuilder().parse("<test><![CDATA[This is some CData]]></test>", null);
 Assert.assertNotNull(doc);
 List<Node> nodes = doc.childNodes();
 Assert.assertEquals(1, nodes.size());
 Node cdataNode = nodes.get(0);
 Assert.assertTrue(cdataNode instanceof CDataNode);
 CDataNode cdata = (CDataNode) cdataNode;
 Assert.assertEquals("This is some CData", cdata.getData());
 }

 @Test
 public void testParseWithXmlDeclaration() {
 Document doc = new XmlTreeBuilder().parse("<!DOCTYPE html SYSTEM \"html.dtd\">\n< xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>This is a test</p>\n</body>\n</html>", null);
 Assert.assertNotNull(doc);
 DocumentType doctypeNode = doc.doctype();
 Assert.assertNotNull(doctypeNode);
 Assert.assertEquals("html", doctypeNode.name());
 XmlDeclaration xmlDeclaration = doc.selectFirst("xml");
 Assert.assertNotNull(xmlDeclaration);
 Assert.assertTrue(xmlDeclaration.isDeclared());
 Assert.assertEquals("1.0", xmlDeclaration.version());
 Assert.assertEquals("UTF-8", xmlDeclaration.encoding());
 Assert.assertTrue(xmlDeclaration.isStandalone());
 }

 @Test
 public void testSelectFirst() {
 Document doc = new XmlTreeBuilder().parse("<test><my:name xmlns:my=\"http://example.com/\">John</my:name></test>", null);
 Element nameElement = doc.selectFirst("my:name");
 Assert.assertNotNull(nameElement);
 Assert.assertEquals("John", nameElement.text());
 }

 @Test
 public void testSelect() {
 Document doc = new XmlTreeBuilder().parse("<test><my:name xmlns:my=\"http://example.com/\">John</my:name><my:age>30</my:age></test>", null);
 List<Element> nameElements = doc.select("my:name");
 Assert.assertNotNull(nameElements);
 Assert.assertEquals(1, nameElements.size());
 Element nameElement = nameElements.get(0);
 Assert.assertEquals("name", nameElement.tagName());
 Assert.assertEquals("John", nameElement.text());
 List<Element> ageElements = doc.select("my:age");
 Assert.assertNotNull(ageElements);
 Assert.assertEquals(1, ageElements.size());
 Element ageElement = ageElements.get(0);
 Assert.assertEquals("age", ageElement.tagName());
 Assert.assertEquals("30", ageElement.text());
 }

 @Test
 public void testSelectWithNamespacePrefix() {
 Document doc = new XmlTreeBuilder().parse("<test xmlns:my=\"http://example.com/\"><my:name xmlns:x=\"http://example.com/\">John</my:name></test>", null);
 List<Element> nameElements = doc.select("my:name");
 Assert.assertNotNull(nameElements);
 Assert.assertEquals(1, nameElements.size());
 Element nameElement = nameElements.get(0);
 Assert.assertEquals("name", nameElement.tagName());
 Assert.assertEquals("John", nameElement.text());
 List<Element> nameElementsWithXPrefix = doc.select("x:my:name");
 Assert.assertNotNull(nameElementsWithXPrefix);
 Assert.assertEquals(1, nameElementsWithXPrefix.size());
 Element nameElementWithXPrefix = nameElementsWithXPrefix.get(0);
 Assert.assertEquals("name", nameElementWithXPrefix.tagName());
 Assert.assertEquals("John", nameElementWithXPrefix.text());
 }

 @Test
 public void testSelectWithAttribute() {
 Document doc = new XmlTreeBuilder().parse("<test><my:name xml:id=\"testId\" xmlns:my=\"http://example.com/\">John</my:name></test>", null);
 Element nameElement = doc.selectFirst("#testId");
 Assert.assertNotNull(nameElement);
 Assert.assertEquals("name", nameElement.tagName());
 Assert.assertEquals("John", nameElement.text());
 }

 @Test
 public void testSelectWithAttributeAndNamespace() {
 Document doc = new XmlTreeBuilder().parse("<test xmlns:my=\"http://example.com/\"><my:name xml:id=\"testId\" xmlns:x=\"http://example.com/\">John</my:name></test>", null);
 Element nameElement = doc.selectFirst("x:my:name[@xml:id='testId']");
 Assert.assertNotNull(nameElement);
 Assert.assertEquals("name", nameElement.tagName());
 Assert.assertEquals("John", nameElement.text());
 }

 @Test
 public void testSelectWithAttributeValue() {
 Document doc = new XmlTreeBuilder().parse("<test><my:name xml:id=\"testId\" xmlns:my=\"http://example.com/\">John</my:name></test>", null);
 List<Element> nameElementsWithValue = doc.select("my:name[.='John']");
 Assert.assertNotNull(nameElementsWithValue);
 Assert.assertEquals(1, nameElementsWithValue.size());
 Element nameElementWithValue = nameElementsWithValue.get(0);
 Assert.assertEquals("name", nameElementWithValue.tagName());
 Assert.assertEquals("John", nameElementWithValue.text());
 }

 @Test
 public void testSelectWithAttributeValueAndNamespace() {
 Document doc = new XmlTreeBuilder().parse("<test xmlns:my=\"http://example.com/\"><my:name xml:id=\"testId\" xmlns:x=\"http://example.com/\">John</my:name></test>", null);
 List<Element> nameElementsWithValue = doc.select("x:my:name[.='John']");
 Assert.assertNotNull(nameElementsWithValue);
 Assert.assertEquals(1, nameElementsWithValue.size());
 Element nameElementWithValue = nameElementsWithValue.get(0);
 Assert.assertEquals("name", nameElementWithValue.tagName());
 Assert.assertEquals("John", nameElementWithValue.text());
 }

 @Test
 public void testSelectWithAttributeValueAndNamespaceAndPrefix() {
 Document doc = new XmlTreeBuilder().parse("<test xmlns:my=\"http://example.com/\"><my:name xml:id=\"testId\" xmlns:x=\"http://example.com/\">John</my:name></test>", null);
 List<Element> nameElementsWithValue = doc.select("*:my:name[.='John']");
 Assert.assertNotNull(nameElementsWithValue);
 Assert.assertEquals(1, nameElementsWithValue.size());
 Element nameElementWithValue = nameElementsWithValue.get(0);
 Assert.assertEquals("name", nameElementWithValue.tagName());
 Assert.assertEquals("John", nameElementWithValue.text());
 }

 @Test
 public void testTraverse() {
 Document doc = new XmlTreeBuilder().parse("<test><my:name xmlns:my=\"http://example.com/\">John</my:name><my:age>30</my:age></test>", null);
 NodeTraverser traverser = traverse(doc);
 List<Node> elements = new ArrayList<>();
 traverser.traverse(new ElementFilter() {
 @Override
 public boolean accept(Node node) {
 elements.add(node);
 return true;
 }
 });
 Assert.assertEquals(3, elements.size());
 Assert.assertTrue(elements.contains(doc));
 Assert.assertTrue(elements.contains(doc.selectFirst("my:name")));
 Assert.assertTrue(elements.contains(doc.selectFirst("my:age")));
 }
}