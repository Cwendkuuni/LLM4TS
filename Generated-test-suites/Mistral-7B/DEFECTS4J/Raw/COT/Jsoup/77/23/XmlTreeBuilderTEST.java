package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

/**
 * XmlTreeBuilderTest
 */
public class XmlTreeBuilderTest {

 private static final String TEST_XML = "" +
         "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
         "<root>\n" +
         "<element attr1=\"value1\" attr2=\"value2\">Content1</element>\n" +
         "<element2 attr3=\"value3\">Content2</element2>\n" +
         "<comment>This is a comment</comment>\n" +
         "</root>";

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 // Test for all constructors
 @Test
 public void testXmlTreeBuilderConstructors() {
 xmlTreeBuilder = new XmlTreeBuilder(); // default constructor
 xmlTreeBuilder = new XmlTreeBuilder(ParseSettings.preserveCase); // constructor with settings
 }

 // Test for defaultSettings method
 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.preserveCase, settings);
 }

 // Test for parse method with Reader input
 @Test
 public void testParseWithReaderInput() throws Exception {
 Document document = xmlTreeBuilder.parse(new StringReader(TEST_XML), "baseUri");
 assertNotNull(document);
 assertNotNull(document.title()); // should not be null for XML
 assertEquals("root", document.tagName());

 Element root = document.getElementsByTagName("root").get(0);
 List<Node> children = root.childNodes();
 assertEquals(3, children.size());

 Element element = (Element) children.get(0);
 assertEquals("element", element.tagName());
 List<Attribute> attributes = element.attributes();
 assertEquals(2, attributes.size());
 assertEquals("value1", attributes.get(0).getValue());
 assertEquals("value2", attributes.get(1).getValue());
 assertEquals("Content1", element.text());

 Element element2 = (Element) children.get(1);
 assertEquals("element2", element2.tagName());
 assertEquals(0, element2.attributes().size());
 assertEquals("Content2", element2.text());

 Element comment = (Element) children.get(2);
 assertEquals("comment", comment.tagName());
 assertEquals(null, comment.text());
 }

 // Test for parse method with String input
 @Test
 public void testParseWithStringInput() throws Exception {
 Document document = xmlTreeBuilder.parse(TEST_XML, "baseUri");
 assertNotNull(document);
 assertNotNull(document.title()); // should not be null for XML
 assertEquals("root", document.tagName());

 Element root = document.getElementsByTagName("root").get(0);
 List<Node> children = root.childNodes();
 assertEquals(3, children.size());

 // Rest of the assertions are the same as testParseWithReaderInput
 }

 // Test for initialiseParse method
 @Test
 public void testInitialiseParse() throws Exception {
 Reader reader = new StringReader(TEST_XML);
 xmlTreeBuilder.initialiseParse(reader, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(ParseSettings.preserveCase, xmlTreeBuilder.settings());
 }

 // Test for process method with StartTag token
 @Test
 public void testProcessStartTag() throws Exception {
 Token startTag = new Token.StartTag("root");
 xmlTreeBuilder.process(startTag);
 assertEquals("root", xmlTreeBuilder.doc.tagName());
 }

 // Test for process method with EndTag token
 @Test
 public void testProcessEndTag() {
 Token endTag = new Token.EndTag("root");
 xmlTreeBuilder.process(endTag);
 assertEquals(0, xmlTreeBuilder.stack.size());
 }

 // Test for process method with Comment token
 @Test
 public void testProcessComment() {
 Token commentToken = new Token.Comment("This is a comment");
 xmlTreeBuilder.process(commentToken);
 assertEquals(1, xmlTreeBuilder.stack.size());
 assertEquals(commentToken.getData(), xmlTreeBuilder.stack.get(0).text());
 }

 // Test for process method with Character token
 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token.Character('A');
 xmlTreeBuilder.process(characterToken);
 assertEquals(1, xmlTreeBuilder.stack.size());
 assertEquals("#x41", xmlTreeBuilder.stack.get(0).text());
 }

 // Test for process method with Doctype token
 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Token.Doctype("html", null, null);
 xmlTreeBuilder.process(doctypeToken);
 assertEquals(1, xmlTreeBuilder.stack.size());
 assertEquals("html", xmlTreeTreeBuilder.doc.doctype().name());
 }

 // Test for insertNode method
 @Test
 public void testInsertNode() {
 Element element = new Element("element", "baseUri", null);
 xmlTreeBuilder.insertNode(element);
 assertEquals(1, xmlTreeBuilder.stack.size());
 assertEquals(element, xmlTreeBuilder.stack.get(0));
 }

 // Test for insert method with StartTag token
 @Test
 public void testInsertStartTag() throws Exception {
 Token startTag = new Token.StartTag("root");
 Element element = xmlTreeBuilder.insert(startTag);
 assertEquals("root", element.tagName());
 assertEquals(1, xmlTreeBuilder.stack.size());
 assertEquals(element, xmlTreeBuilder.stack.get(0));
 }

 // Test for insert method with Comment token
 @Test
 public void testInsertComment() throws Exception {
 Token commentToken = new Token.Comment("This is a comment");
 Comment comment = (Comment) xmlTreeBuilder.insert(commentToken);
 assertEquals(1, xmlTreeBuilder.stack.size());
 assertEquals(commentToken.getData(), comment.text());
 assertEquals(comment, xmlTreeBuilder.stack.get(0));
 }

 // Test for insert method with Character token
 @Test
 public void testInsertCharacter() throws Exception {
 Token characterToken = new Token.Character('A');
 TextNode textNode = (TextNode) xmlTreeBuilder.insert(characterToken);
 assertEquals(1, xmlTreeBuilder.stack.size());
 assertEquals("#x41", textNode.text());
 assertEquals(textNode, xmlTreeBuilder.stack.get(0));
 }

 // Test for insert method with Doctype token
 @Test
 public void testInsertDoctype() throws Exception {
 Token doctypeToken = new Token.Doctype("html", null, null);
 DocumentType documentType = (DocumentType) xmlTreeBuilder.insert(doctypeToken);
 assertEquals(1, xmlTreeBuilder.stack.size());
 assertEquals("html", documentType.name());
 assertEquals(documentType, xmlTreeBuilder.stack.get(0));
 }

 // Test for parseFragment method
 @Test
 public void testParseFragment() throws Exception {
 List<Node> fragmentNodes = xmlTreeBuilder.parseFragment(TEST_XML, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(3, fragmentNodes.size());
 Element root = (Element) fragmentNodes.get(0);
 assertEquals("root", root.tagName());
 List<Node> children = root.childNodes();
 assertEquals(3, children.size());

 Element element = (Element) children.get(0);
 assertEquals("element", element.tagName());
 List<Attribute> attributes = element.attributes();
 assertEquals(2, attributes.size());
 assertEquals("value1", attributes.get(0).getValue());
 assertEquals("value2", attributes.get(1).getValue());
 assertEquals("Content1", element.text());

 Element element2 = (Element) children.get(1);
 assertEquals("element2", element2.tagName());
 assertEquals(0, element2.attributes().size());
 assertEquals("Content2", element2.text());

 Element comment = (Element) children.get(2);
 assertEquals("comment", comment.tagName());
 assertEquals(null, comment.text());
 }

 // Test for popStackToClose method with non-existent tag
 @Test
 public void testPopStackToCloseNonExistentTag() {
 xmlTreeBuilder.popStackToClose(new Token.EndTag("nonExistentTag"));
 assertEquals(0, xmlTreeBuilder.stack.size());
 }

 // Test for popStackToClose method with existing tag
 @Test
 public void testPopStackToCloseExistingTag() throws Exception {
 xmlTreeBuilder.process(new Token.StartTag("root"));
 xmlTreeBuilder.process(new Token.EndTag("root"));
 xmlTreeBuilder.popStackToClose(new Token.EndTag("root"));
 assertEquals(0, xmlTreeBuilder.stack.size());
 }
}