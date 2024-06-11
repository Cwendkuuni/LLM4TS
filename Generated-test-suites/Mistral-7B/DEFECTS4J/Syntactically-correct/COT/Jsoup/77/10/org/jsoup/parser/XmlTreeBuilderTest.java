import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.XmlTreeBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 // Test for defaultSettings() method
 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.preserveCase, settings);
 }

 // Test for parse() method with valid XML input
 @Test
 public void testParseValidXml() throws Exception {
 String xmlInput = "<root><Child1><Child2/><Child3>Text</Child3></Child1></root>";
 Document document = xmlTreeBuilder.parse(new StringReader(xmlInput), "");
 assertNotNull(document);
 Element rootElement = document.getRootElement();
 assertEquals("root", rootElement.tagName());
 assertEquals(3, rootElement.childElementCount());
 }

 // Test for parse() method with invalid XML input
 @Test(expected = org.jsoup.exceptions.MalformedXmlException.class)
 public void testParseInvalidXml() throws Exception {
 String invalidXmlInput = "<root><Child><Child";
 xmlTreeBuilder.parse(new StringReader(invalidXmlInput), "");
 }

 // Test for initialiseParse() method
 @Test
 public void testInitialiseParse() {
 String xmlInput = "<root><Child><Child/></Child></root>";
 xmlTreeBuilder.initialiseParse(new StringReader(xmlInput), "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(ParseSettings.preserveCase, xmlTreeBuilder.settings());
 }

 // Test for process() method with valid XML tokens
 @Test
 public void testProcessValidXmlTokens() {
 Token startTag = new Token.StartTag("root", null, null, null);
 Token endTag = new Token.EndTag("root");
 xmlTreeBuilder.process(startTag);
 assertEquals(1, xmlTreeBuilder.stack().size());
 Element rootElement = (Element) xmlTreeBuilder.stack().get(0);
 assertEquals("root", rootElement.tagName());
 xmlTreeBuilder.process(endTag);
 assertEquals(0, xmlTreeBuilder.stack().size());
 }

 // Test for process() method with invalid XML tokens
 @Test(expected = IllegalArgumentException.class)
 public void testProcessInvalidXmlTokens() {
 Token invalidToken = new Token(-1, "InvalidToken");
 xmlTreeBuilder.process(invalidToken);
 }

 // Test for insertNode() method
 @Test
 public void testInsertNode() {
 Element childElement = new Element("Child");
 xmlTreeBuilder.insertNode(childElement);
 Element parentElement = new Element("Parent");
 parentElement.appendChild(childElement);
 assertEquals(1, parentElement.childElementCount());
 }

 // Test for insert() method with valid XML tokens
 @Test
 public void testInsertValidXmlTokens() {
 Token startTag = new Token.StartTag("root", null, null, null);
 xmlTreeBuilder.insert(startTag);
 assertEquals(1, xmlTreeBuilder.stack().size());
 Element rootElement = (Element) xmlTreeBuilder.stack().get(0);
 assertEquals("root", rootElement.tagName());
 Token endTag = new Token.EndTag("root");
 xmlTreeBuilder.insert(endTag);
 assertEquals(0, xmlTreeBuilder.stack().size());
 }

 // Test for insert() method with invalid XML tokens
 @Test(expected = IllegalArgumentException.class)
 public void testInsertInvalidXmlTokens() {
 Token invalidToken = new Token(-1, "InvalidToken");
 xmlTreeBuilder.insert(invalidToken);
 }

 // Test for insert() method with comment token
 @Test
 public void testInsertCommentToken() {
 Token commentToken = new Token.Comment("-- This is a comment");
 xmlTreeBuilder.insert(commentToken);
 assertEquals(1, xmlTreeBuilder.stack().size());
 Comment comment = (Comment) xmlTreeBuilder.stack().get(0);
 assertEquals("-- This is a comment", comment.getData());
 }

 // Test for popStackToClose() method with valid tag name
 @Test
 public void testPopStackToCloseValidTagName() {
 xmlTreeBuilder.pushTag("root");
 xmlTreeBuilder.pushTag("Child");
 xmlTreeBuilder.popStackToClose(new Token.EndTag("Child"));
 assertEquals(1, xmlTreeBuilder.stack().size());
 Element rootElement = (Element) xmlTreeBuilder.stack().get(0);
 assertEquals("root", rootElement.tagName());
 }

 // Test for popStackToClose() method with invalid tag name
 @Test
 public void testPopStackToCloseInvalidTagName() {
 xmlTreeBuilder.pushTag("root");
 xmlTreeBuilder.pushTag("Child");
 xmlTreeBuilder.popStackToClose(new Token.EndTag("Parent"));
 assertEquals(1, xmlTreeBuilder.stack().size());
 Element rootElement = (Element) xmlTreeBuilder.stack().get(0);
 assertEquals("root", rootElement.tagName());
 }

 // Test for parseFragment() method with valid XML input
 @Test
 public void testParseFragmentValidXml() throws Exception {
 String xmlInput = "<root><Child><Child/><Child>Text</Child></Child></root>";
 List<Node> parsedNodes = xmlTreeBuilder.parseFragment(xmlInput, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(5, parsedNodes.size());
 Element rootElement = (Element) parsedNodes.get(0);
 assertEquals("root", rootElement.tagName());
 Element childElement = (Element) parsedNodes.get(1);
 assertEquals("Child", childElement.tagName());
 Text textNode = (Text) parsedNodes.get(4);
 assertEquals("Text", textNode.text());
 }

 // Test for parseFragment() method with invalid XML input
 @Test(expected = org.jsoup.exceptions.MalformedXmlException.class)
 public void testParseFragmentInvalidXml() throws Exception {
 String invalidXmlInput = "<root><Child><Child";
 xmlTreeBuilder.parseFragment(invalidXmlInput, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 }
}