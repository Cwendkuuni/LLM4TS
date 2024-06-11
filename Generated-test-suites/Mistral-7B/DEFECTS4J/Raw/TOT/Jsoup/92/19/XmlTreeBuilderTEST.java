import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextDocument;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Parser;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testValidXmlParsing() {
 String xmlInput = "<root><child1>Hello</child1><child2><grandchild>World</grandchild></child2></root>";
 Document expectedDocument = new TextDocument("<root><child1>Hello</child1><child2><grandchild>World</grandchild></child2></root>");
 Document actualDocument = xmlTreeBuilder.parse(new StringReader(xmlInput));
 Validate.assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testInvalidXmlParsingWithMissingStartTag() {
 String xmlInput = "<child1>Hello<child2>World";
 Document actualDocument = xmlTreeBuilder.parse(new StringReader(xmlInput));
 Validate.assertNull(actualDocument);
 }

 @Test
 public void testInvalidXmlParsingWithMissingEndTag() {
 String xmlInput = "<root><child1>Hello<child2>World";
 Document actualDocument = xmlTreeBuilder.parse(new StringReader(xmlInput));
 Validate.assertNull(actualDocument);
 }

 @Test
 public void testInvalidXmlParsingWithEmptyElement() {
 String xmlInput = "<root><child/></root>";
 Document expectedDocument = new TextDocument("<root><child></child></root>");
 Document actualDocument = xmlTreeBuilder.parse(new StringReader(xmlInput));
 Validate.assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testInvalidXmlParsingWithSelfClosingTag() {
 String xmlInput = "<root><img src=\"image.jpg\" /> </root>";
 Document expectedDocument = new TextDocument("<root><img src=\"image.jpg\" /></root>");
 Document actualDocument = xmlTreeBuilder.parse(new StringReader(xmlInput));
 Validate.assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testXmlDeclarationParsing() {
 String xmlInput = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><child>Hello</child></root>";
 Document expectedDocument = new TextDocument("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><child>Hello</child></root>");
 Document actualDocument = xmlTreeBuilder.parse(new StringReader(xmlInput));
 Validate.assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testXmlParsingWithComments() {
 String xmlInput = "<root><!-- This is a comment --><child>Hello</child></root>";
 Document expectedDocument = new TextDocument("<root><!-- This is a comment --></root><child>Hello</child>");
 Document actualDocument = xmlTreeBuilder.parse(new StringReader(xmlInput));
 Validate.assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testXmlParsingWithDoctype() {
 String xmlInput = "<!DOCTYPE root SYSTEM \"root.dtd\">\n<root><child>Hello</child></root>";
 Document expectedDocument = new TextDocument("<!DOCTYPE root SYSTEM \"root.dtd\">\n<root><child>Hello</child></root>");
 Document actualDocument = xmlTreeBuilder.parse(new StringReader(xmlInput));
 Validate.assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testXmlParsingWithFragment() {
 String xmlFragment = "<child1><child2>Hello</child2></child1>";
 List<Node> expectedChildren = List.of(new Element("child1"), new Element("child2"), new TextNode("Hello"));
 List<Node> actualChildren = xmlTreeBuilder.parseFragment(xmlFragment, null, null, new Parser(xmlTreeBuilder)).childNodes();
 Validate.assertIterateEquals(expectedChildren, actualChildren);
 }
}