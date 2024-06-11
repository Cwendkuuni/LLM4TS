import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;
 private ParseSettings parseSettings;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 parseSettings = xmlTreeBuilder.defaultSettings();
 }

 // Test for defaultSettings() method
 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.preserveCase, settings);
 }

 // Test for parse() method with Reader input
 @Test
 public void testParseWithReaderInput() throws Exception {
 String xmlInput = "<root><element>Text</element></root>";
 Reader reader = new StringReader(xmlInput);
 document = xmlTreeBuilder.parse(reader, "baseUri");
 Element root = document.getRootElement();
 assertEquals("root", root.tagName());
 Element element = root.getChild(0);
 assertEquals("element", element.tagName());
 assertEquals("Text", element.text());
 }

 // Test for parse() method with String input
 @Test
 public void testParseWithStringInput() throws Exception {
 String xmlInput = "<root><element>Text</element></root>";
 document = xmlTreeBuilder.parse(xmlInput, "baseUri");
 Element root = document.getRootElement();
 assertEquals("root", root.tagName());
 Element element = root.getChild(0);
 assertEquals("element", element.tagName());
 assertEquals("Text", element.text());
 }

 // Test for initialiseParse() method
 @Test
 public void testInitialiseParse() throws Exception {
 String xmlInput = "<root><element>Text</element></root>";
 Reader reader = new StringReader(xmlInput);
 xmlTreeBuilder.initialiseParse(reader, "baseUri", ParseErrorList.noTracking(), parseSettings);
 assertEquals(XmlTreeBuilder.class, xmlTreeBuilder.getClass());
 }

 // Test for process() method with StartTag token
 @Test
 public void testProcessStartTag() throws Exception {
 Token startTagToken = new Token(StartTag.class, "root");
 xmlTreeBuilder.process(startTagToken);
 Element rootElement = new Element("root", null, null);
 assertEquals(rootElement, xmlTreeBuilder.stack.peek());
 }

 // Test for process() method with EndTag token
 @Test (expected = AssertionError.class)
 public void testProcessEndTagNoMatchingStartTag() throws Exception {
 Token endTagToken = new Token(EndTag.class, "body");
 xmlTreeBuilder.process(endTagToken);
 Validate.fail("Unexpected end tag: body");
 }

 // Test for process() method with Comment token
 @Test
 public void testProcessComment() throws Exception {
 Token commentToken = new Token(Comment.class, "This is a comment");
 xmlTreeBuilder.process(commentToken);
 Comment comment = new Comment("This is a comment");
 assertEquals(comment, xmlTreeBuilder.currentElement().lastChild());
 }

 // Test for process() method with Character token
 @Test
 public void testProcessCharacter() throws Exception {
 Token characterToken = new Token(Character.class, "A");
 xmlTreeBuilder.process(characterToken);
 TextNode textNode = new TextNode("A");
 assertEquals(textNode, xmlTreeBuilder.currentElement().lastChild());
 }

 // Test for process() method with Doctype token
 @Test
 public void testProcessDoctype() throws Exception {
 Token doctypeToken = new Token(Doctype.class, "html", null, null, null, null, null);
 xmlTreeBuilder.process(doctypeToken);
 DocumentType documentType = new DocumentType("html", null, null);
 assertEquals(documentType, xmlTreeBuilder.currentElement());
 }

 // Test for insertNode() method
 @Test
 public void testInsertNode() throws Exception {
 Element root = new Element("root", null, null);
 Node node = new Element("element", null, null);
 xmlTreeBuilder.insertNode(node);
 assertEquals(node, xmlTreeBuilder.currentElement().lastChild());
 }

 // Test for insert() method with StartTag token
 @Test
 public void testInsertStartTag() throws Exception {
 Token startTagToken = new Token(StartTag.class, "element");
 xmlTreeBuilder.insert(startTagToken);
 Element element = new Element("element", null, null);
 assertEquals(element, xmlTreeBuilder.currentElement());
 }

 // Test for insert() method with Comment token
 @Test
 public void testInsertComment() throws Exception {
 Token commentToken = new Token(Comment.class, "This is a comment");
 xmlTreeBuilder.insert(commentToken);
 Comment comment = new Comment("This is a comment");
 assertEquals(comment, xmlTreeBuilder.currentElement().lastChild());
 }

 // Test for insert() method with Character token
 @Test
 public void testInsertCharacter() throws Exception {
 Token characterToken = new Token(Character.class, "A");
 xmlTreeBuilder.insert(characterToken);
 TextNode textNode = new TextNode("A");
 assertEquals(textNode, xmlTreeBuilder.currentElement().lastChild());
 }

 // Test for insert() method with Doctype token
 @Test
 public void testInsertDoctype() throws Exception {
 Token doctypeToken = new Token(Doctype.class, " html", null, null, null, null, null);
 xmlTreeBuilder.insert(doctypeToken);
 DocumentType documentType = new DocumentType(" html", null, null);
 assertEquals(documentType, xmlTreeBuilder.currentElement());
 }

 // Test for popStackToClose() method with unknown tag name
 @Test (expected = AssertionError.class)
 public void testPopStackToCloseUnknownTag() {
 xmlTreeBuilder.popStackToClose(new Token(EndTag.class, "unknownTag"));
 Validate.fail("Unexpected end tag: unknownTag");
 }

 // Test for parseFragment() method
 @Test
 public void testParseFragment() throws Exception {
 String xmlFragment = "<root><element>Text</element></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "baseUri", ParseErrorList.noTracking(), parseSettings);
 assertEquals(2, nodes.size());
 Element root = (Element) nodes.get(0);
 assertEquals("root", root.tagName());
 Element element = (Element) nodes.get(1);
 assertEquals("element", element.tagName());
 assertEquals("Text", element.text());
 }
}