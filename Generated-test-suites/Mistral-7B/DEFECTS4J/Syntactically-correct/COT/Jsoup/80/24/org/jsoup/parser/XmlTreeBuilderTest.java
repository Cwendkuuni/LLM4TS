package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.List;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;

/**
 * Test class for {@link XmlTreeBuilder}
 *
 * @author John Doe
 */
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 // Test for defaultSettings() method
 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertTrue(settings.isPreserveCase());
 }

 // Test for parse(Reader) method
 @Test
 public void testParseWithReader() throws Exception {
 String xmlString = "<root><element>Test</element></root>";
 Reader reader = new StringReader(xmlString);
 Document doc = xmlTreeBuilder.parse(reader, "http://example.com");
 assertNotNull(doc);
 Element root = doc.getElementById("root");
 Element element = root.getElementsByTag("element").first();
 assertNotNull(element);
 assertTrue(element.text().equals("Test"));
 }

 // Test for parse(String) method
 @Test
 public void testParseWithString() throws Exception {
 String xmlString = "<root><element>Test</element></root>";
 Document doc = xmlTreeBuilder.parse(xmlString, "http://example.com");
 assertNotNull(doc);
 Element root = doc.getElementById("root");
 Element element = root.getElementsByTag("element").first();
 assertNotNull(element);
 assertTrue(element.text().equals("Test"));
 }

 // Test for initialiseParse(Reader, String, ParseErrorList, ParseSettings) method
 @Test(expected = Exception.class)
 public void testInitialiseParseWithNullInput() {
 xmlTreeBuilder.initialiseParse(null, "http://example.com", null, xmlTreeBuilder.defaultSettings());
 }

 // Test for initialiseParse(Reader, String, ParseErrorList, ParseSettings) method with empty input
 @Test
 public void testInitialiseParseWithEmptyInput() {
 ParseErrorList errorList = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(new StringReader(""), "http://example.com", errorList, xmlTreeBuilder.defaultSettings());
 assertNotNull(xmlTreeBuilder.doc);
 assertTrue(xmlTreeBuilder.doc.childNodes().isEmpty());
 }

 // Test for process(Token) method with null token
 @Test(expected = NullPointerException.class)
 public void testProcessWithNullToken() {
 xmlTreeBuilder.initialiseParse(new StringReader("<root></root>"), "http://example.com", null, xmlTreeBuilder.defaultSettings());
 xmlTreeBuilder.process(null);
 }

 // Test for process(Token) method with unknown tag
 @Test
 public void testProcessWithUnknownTag() throws Exception {
 String xmlString = "<unknownTag>Test</unknownTag>";
 xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "http://example.com", null, xmlTreeBuilder.defaultSettings());
 xmlTreeBuilder.process(new Token.StartTag("<unknownTag>", null, null, 1, 1));
 Element unknownTag = xmlTreeBuilder.doc.getElementsByTag("unknownTag").first();
 assertNotNull(unknownTag);
 }

 // Test for process(Token) method with self-closing tag
 @Test
 public void testProcessWithSelfClosingTag() throws Exception {
 String xmlString = "<selfClosingTag />";
 xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "http://example.com", null, xmlTreeBuilder.defaultSettings());
 xmlTreeBuilder.process(new Token.StartTag("<selfClosingTag>", null, null, 1, 1));
 Element selfClosingTag = xmlTreeBuilder.doc.getElementsByTag("selfClosingTag").first();
 assertNotNull(selfClosingTag);
 assertTrue(selfClosingTag.tagName().equals("#selfClosingTag"));
 }

 // Test for insertNode(Node) method with null node
 @Test(expected = NullPointerException.class)
 public void testInsertNodeWithNullNode() {
 Element root = new Element("root");
 xmlTreeBuilder.initialiseParse(new StringReader("<root></root>"), "http://example.com", null, xmlTreeBuilder.defaultSettings());
 xmlTreeBuilder.setDoc(root);
 xmlTreeBuilder.insertNode(null);
 }

 // Test for insert(Token.StartTag) method with null startTag
 @Test(expected = NullPointerException.class)
 public void testInsertWithNullStartTag() {
 xmlTreeBuilder.initialiseParse(new StringReader("<root><element>Test</element></root>"), "http://example.com", null, xmlTreeBuilder.defaultSettings());
 xmlTreeBuilder.insert(null);
 }

 // Test for insert(Token.Comment) method with null commentToken
 @Test
 public void testInsertWithNullCommentToken() {
 xmlTreeBuilder.initialiseParse(new StringReader("<root><!--Test--></root>"), "http://example.com", null, xmlTreeBuilder.defaultSettings());
 xmlTreeBuilder.insert(null);
 assertTrue(xmlTreeBuilder.doc.childNodes().isEmpty());
 }

 // Test for insert(Token.Character) method with null characterToken
 @Test
 public void testInsertWithNullCharacterToken() {
 xmlTreeBuilder.initialiseParse(new StringReader("<root></root>"), "http://example.com", null, xmlTreeBuilder.defaultSettings());
 xmlTreeBuilder.insert(null);
 assertTrue(xmlTreeBuilder.doc.childNodes().isEmpty());
 }

 // Test for insert(Token.Doctype) method with null d
 @Test
 public void testInsertWithNullDoctype() {
 xmlTreeBuilder.initialiseParse(new StringReader("<!DOCTYPE root [<!ATTLIST element attr CDATA #REQUIRED>]><root></root>"), "http://example.com", null, xmlTreeBuilder.defaultSettings());
 xmlTreeBuilder.insert(null);
 assertTrue(xmlTreeBuilder.doc.childNodes().isEmpty());
 }

 // Test for popStackToClose(Token.EndTag) method with null endTag
 @Test(expected = NullPointerException.class)
 public void testPopStackToCloseWithNullEndTag() {
 xmlTreeBuilder.initialiseParse(new StringReader("<root><element></element></root>"), "http://example.com", null, xmlTreeBuilder.defaultSettings());
 xmlTreeBuilder.popStackToClose(null);
 }

 // Test for popStackToClose(Token.EndTag) method with no matching tag on stack
 @Test
 public void testPopStackToCloseWithNoMatchingTag() {
 xmlTreeBuilder.initialiseParse(new StringReader("<root><element></element><unmatchedTag></unmatchedTag></root>"), "http://example.com", null, xmlTreeBuilder.defaultSettings());
 xmlTreeBuilder.popStackToClose(new Token.EndTag("unmatchedTag", null, null, 1, 1));
 assertTrue(xmlTreeBuilder.doc.childNodes().size() > 2);
 }

 // Test for parseFragment(String, String, ParseErrorList, ParseSettings) method with null inputFragment
 @Test(expected = NullPointerException.class)
 public void testParseFragmentWithNullInputFragment() {
 xmlTreeBuilder.parseFragment(null, "http://example.com", null, xmlTreeBuilder.defaultSettings());
 }

 // Test for parseFragment(String, String, ParseErrorList, ParseSettings) method with empty inputFragment
 @Test
 public void testParseFragmentWithEmptyInputFragment() {
 List<Node> nodes = xmlTreeBuilder.parseFragment("", "http://example.com", null, xmlTreeBuilder.defaultSettings());
 assertTrue(nodes.isEmpty());
 }

}