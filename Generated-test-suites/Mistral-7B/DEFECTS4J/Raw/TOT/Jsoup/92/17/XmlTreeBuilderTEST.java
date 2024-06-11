import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextDocument;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.XmlTreeBuilderTest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParseXmlWithValidInput() throws IOException {
 String xmlInput = "<root><element1>Element 1</element1><element2>Element 2</element2></root>";
 Documents.overwriteXmlDeclaration(xmlInput, "UTF-8"); // ensure UTF-8 encoding for test
 doc = xmlTreeBuilder.parse(new InputStreamReader(new ByteArrayInputStream(xmlInput.getBytes())));

 Element root = doc.getRootElement();
 Element element1 = root.getElementsByTagName("element1").get(0);
 Element element2 = root.getElementsByTagName("element2").get(0);

 assertNotNull(root);
 assertNotNull(element1);
 assertNotNull(element2);
 assertNotNull(element1.text());
 assertNotNull(element2.text());
 assertNotNull(element1.attributes());
 assertNotNull(element2.attributes());
 assertEquals("Element 1", element1.text());
 assertEquals("Element 2", element2.text());
 }

 @Test
 public void testParseXmlWithSelfClosingTag() throws IOException {
 String xmlInput = "<root><img src=\"image.jpg\" /> </root>";
 doc = xmlTreeBuilder.parse(new InputStreamReader(new ByteArrayInputStream(xmlInput.getBytes())));

 Element root = doc.getRootElement();
 Element img = root.getElementsByTagName("img").get(0);

 assertNotNull(root);
 assertNotNull(img);
 assertFalse(img.hasText());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseXmlWithInvalidInput() throws IOException {
 String xmlInput = "<root><element1>Element 1</element1><element2>Element 2</element2>";
 doc = xmlTreeBuilder.parse(new InputStreamReader(new ByteArrayInputStream(xmlInput.getBytes())));

 // Test for missing closing tag
 doc.getElementsByTagName("element3").get(0); // non-existent tag
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseXmlWithInvalidXmlDeclaration() throws IOException {
 String xmlInput = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><element1>Element 1</element1></root>";
 doc = xmlTreeBuilder.parse(new InputStreamReader(new ByteArrayInputStream(xmlInput.getBytes())));

 // Test for a missing XML declaration
 String xmlInputWithoutDeclaration = xmlInput.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
 doc = xmlTreeBuilder.parse(new InputStreamReader(new ByteArrayInputStream(xmlInputWithoutDeclaration.getBytes())));
 }

 @Test
 public void testParseXmlFragment() throws IOException {
 String xmlFragment = "<element1>Element 1</element1>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, null, new Parser(xmlTreeBuilder));

 Element element1 = (Element) nodes.get(0);

 assertNotNull(element1);
 assertNotNull(element1.text());
 assertEquals("Element 1", element1.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseXmlFragmentWithInvalidInput() throws IOException {
 String xmlFragment = "<root><element1>Element 1</element1><element2"; // missing closing tag
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, null, new Parser(xmlTreeBuilder));
 }
}