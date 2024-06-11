import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private ParseSettings parseSettings;
 private Parser parser;
 private Document document;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 parseSettings = xmlTreeBuilder.defaultSettings();
 parser = new Parser(xmlTreeBuilder);
 }

 // Testing constructor
 @Test
 public void testXmlTreeBuilderConstruction() {
 assertNotNull(xmlTreeBuilder);
 }

 // Testing defaultSettings method
 @Test
 public void testDefaultSettings() {
 assertEquals(ParseSettings.PreserveCase.class, xmlTreeBuilder.defaultSettings().getClass());
 }

 // Testing parse method with reader input
 @Test
 public void testParseWithReaderInput() throws Exception {
 String xmlString = "<root><child>Hello</child></root>";
 document = xmlTreeBuilder.parse(new StringReader(xmlString), null);
 assertNotNull(document);
 Element root = document.elementSiblings().get(0);
 Element child = root.children().get(0);
 assertEquals("Hello", child.text());
 }

 // Testing parse method with string input
 @Test
 public void testParseWithStringInput() throws Exception {
 String xmlString = "<root><child>Hello</child></root>";
 document = xmlTreeBuilder.parse(xmlString, null);
 assertNotNull(document);
 Element root = document.elementSiblings().get(0);
 Element child = root.children().get(0);
 assertEquals("Hello", child.text());
 }

 // Testing process method with start tag
 @Test
 public void testProcessStartTag() {
 Token startTag = new Token(Token.StartTag.class, "<root>", null, null);
 xmlTreeBuilder.process(startTag);
 Element root = (Element) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
 assertEquals("root", root.tagName());
 }

 // Testing process method with end tag
 @Test
 public void testProcessEndTag() {
 Token endTag = new Token(Token.EndTag.class, "</root>", null, null);
 xmlTreeBuilder.stack.add(new Element("root"));
 xmlTreeBuilder.process(endTag);
 assertEquals(xmlTreeBuilder.stack.size() - 1, xmlTreeBuilder.stack.size());
 }

 // Testing process method with comment
 @Test
 public void testProcessComment() {
 Token comment = new Token(Token.Comment.class, "-- This is a comment --", null, null);
 xmlTreeBuilder.process(comment);
 assertEquals(xmlTreeBuilder.stack.size(), xmlTreeBuilder.stack.size() + 1);
 assertEquals("-- This is a comment --", xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1).nodeName());
 }

 // Testing process method with character
 @Test
 public void testProcessCharacter() {
 Token character = new Token(Token.Character.class, "Hello", null, null);
 xmlTreeBuilder.process(character);
 assertEquals(xmlTreeBuilder.stack.size(), xmlTreeBuilder.stack.size() + 1);
 assertEquals("Hello", xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1).toString());
 }

 // Testing process method with doctype
 @Test
 public void testProcessDoctype() {
 Token doctype = new Token(Token.Doctype.class, "<!DOCTYPE root [ <!ATTLIST child attr CDATA #REQUIRED > ]>", null, null);
 xmlTreeBuilder.process(doctype);
 assertEquals(xmlTreeBuilder.stack.size(), xmlTreeBuilder.stack.size() + 1);
 assertEquals("root", ((DocumentType) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1)).name());
 }

 // Testing parseFragment method with string input
 @Test
 public void testParseFragmentStringInput() {
 String xmlFragment = "<root><child>Hello</child></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, null);
 assertEquals(2, nodes.size());
 }

 // Testing parseFragment method with context and string input
 @Test
 public void testParseFragmentContextStringInput() {
 String xmlFragment = "<root><child>Hello</child></root>";
 Element context = new Element("outer");
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, context, null, null);
 assertEquals(2, nodes.size());
 }

 // Edge cases
 // Testing parse method with empty string
 @Test(expected = IllegalArgumentException.class)
 public void testParseEmptyString() throws Exception {
 xmlTreeBuilder.parse(new StringReader(""), null);
 }

 // Testing parse method with null input
 @Test(expected = NullPointerException.class)
 public void testParseNullInput() throws Exception {
 xmlTreeBuilder.parse(null, null);
 }

 // Testing parseFragment method with empty string
 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentEmptyString() {
 xmlTreeBuilder.parseFragment("", null, null);
 }

 // Testing parseFragment method with null input
 @Test(expected = NullPointerException.class)
 public void testParseFragmentNullInput() {
 xmlTreeBuilder.parseFragment(null, null, null, null);
 }
}