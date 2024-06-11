import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.ParseSettings;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private ParseSettings parseSettings;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 parseSettings = xmlTreeBuilder.defaultSettings();
 }

 @Test
 public void testParseXmlFragmentWithValidInput() {
 String xmlFragment = "<root><name>John</name><age>30</age></root>";
 List<Node> parsedNodes = xmlTreeBuilder.parseFragment(xmlFragment, null, ParseErrorList.noTracking(), parseSettings);

 Document parsedDocument = parsedNodes.get(0).asDocument();
 Element rootElement = parsedDocument.getElementById("root");
 assertEquals("root", rootElement.nodeName());
 Element nameElement = rootElement.getElementsByTagName("name").get(0);
 assertEquals("name", nameElement.nodeName());
 assertEquals("John", nameElement.getText());
 Element ageElement = rootElement.getElementsByTagName("age").get(0);
 assertEquals("age", ageElement.nodeName());
 assertEquals("30", ageElement.getText());
 }

 @Test
 public void testParseXmlFragmentWithInvalidInput() {
 String invalidInput = "<root><name>John<age";
 List<Node> parsedNodes = xmlTreeBuilder.parseFragment(invalidInput, null, ParseErrorList.noTracking(), parseSettings);
 assertFalse("Expected parse to fail with error due to invalid input", parsedNodes.isEmpty());
 Element rootElement = parsedNodes.get(0).asDocument().getRootElement();
 assertNotNull("Expected root element to not be null", rootElement);
 assertNull("Expected 'name' element to be null due to parse error", rootElement.getElementById("name"));
 }

 @Test
 public void testParseXmlFragmentWithEmptyInput() {
 String emptyInput = "";
 List<Node> parsedNodes = xmlTreeBuilder.parseFragment(emptyInput, null, ParseErrorList.noTracking(), parseSettings);
 assertTrue("Expected empty list due to empty input", parsedNodes.isEmpty());
 }

 @Test
 public void testProcessStartTag() {
 Element expectedElement = new Element("tag", null, null);
 Token.StartTag startTag = new Token.StartTag("tag", null, null, null, null, null, null, null, null);
 xmlTreeBuilder.insert(startTag);
 Element actualElement = xmlTreeBuilder.currentElement();
 assertSame("Expected the same element", expectedElement, actualElement);
 }

 @Test
 public void testProcessEndTag() {
 String baseUri = "http://example.com";
 Element parentElement = new Element("parent", baseUri, null);
 Element childElement = new Element("child", baseUri, null);
 parentElement.appendChild(childElement);
 xmlTreeBuilder.stack.add(parentElement);
 xmlTreeBuilder.stack.add(childElement);
 Token.EndTag endTag = new Token.EndTag("child", null);
 xmlTreeBuilder.popStackToClose(endTag);
 Element actualElement = xmlTreeBuilder.currentElement();
 assertNull("Expected current element to be null after popping stack", actualElement);
 }

 @Test(expected = AssertionError.class)
 public void testProcessInvalidTokenType() {
 Token invalidToken = new Token(255, "InvalidToken");
 xmlTreeBuilder.process(invalidToken);
 }
}