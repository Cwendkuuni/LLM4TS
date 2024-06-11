import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class XmlTreeBuilderTest {

 @Test
 public void testParseXMLWithValidInput() {
 String xmlInput = "<root><child element='value' /><anotherChild /></root>";
 XmlTreeBuilder xmlBuilder = new XmlTreeBuilder();
 Document xmlDocument = xmlBuilder.parse(new StringReader(xmlInput), "base-uri");

 Element root = xmlDocument.getRootElement();
 Assertions.assertEquals("root", root.tagName());
 Element child = root.child(0);
 Assertions.assertEquals("child", child.tagName());
 Assertions.assertEquals("value", child.attribute("element"));
 }

 @Test
 public void testParseXMLWithEmptyInput() {
 XmlTreeBuilder xmlBuilder = new XmlTreeBuilder();
 Document xmlDocument = xmlBuilder.parse(new StringReader(""), "base-uri");
 Assertions.assertNull(xmlDocument);
 }

 @Test
 public void testParseXMLWithInvalidInput() {
 XmlTreeBuilder xmlBuilder = new XmlTreeBuilder();
 String invalidXmlInput = "<root></root><!-- comment --><script>alert('Hello World')</script>";
 ParseErrorList errorList = ParseErrorList.empty();
 Document xmlDocument = xmlBuilder.parse(new StringReader(invalidXmlInput), "base-uri", errorList, XmlTreeBuilder.ParseSettings.preserveCase);

 Assertions.assertEquals(3, errorList.size());
 Assertions.assertEquals("StartTag: script, line: 1, column: 40: syntax error", errorList.get(0).getMessage());
 Assertions.assertEquals("EndTag: root, line: 1, column: 1: not well-formed (missing document type)\n at line 1 column 16: <root>", errorList.get(1).getMessage());
 Assertions.assertEquals("EndTag: root, line: 1, column: 1: not well-formed (missing document type)", errorList.get(2).getMessage());
 }

 @Test
 public void testParseXMLFragment() {
 String xmlFragment = "<child element='value' /><anotherChild />";
 XmlTreeBuilder xmlBuilder = new XmlTreeBuilder();
 List<Node> fragmentNodes = xmlBuilder.parseFragment(xmlFragment, "base-uri", ParseErrorList.noTracking(), XmlTreeBuilder.ParseSettings.preserveCase);

 Element child = (Element) fragmentNodes.get(0);
 Assertions.assertEquals("child", child.tagName());
 Assertions.assertEquals("value", child.attribute("element"));
 Element anotherChild = (Element) fragmentNodes.get(1);
 Assertions.assertEquals("anotherChild", anotherChild.tagName());
 Assertions.assertNull(anotherChild.attributes());
 }
}