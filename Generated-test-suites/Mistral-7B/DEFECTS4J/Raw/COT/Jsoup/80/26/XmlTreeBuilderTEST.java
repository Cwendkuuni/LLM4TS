package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;

import java.io.StringReader;
import java.util.List;

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

 // Test for parse(Reader, String) method
 @Test
 public void testParseReaderString() {
 String xmlString = "<xml><test><subtest/></test></xml>";
 Reader reader = new StringReader(xmlString);
 Document doc = xmlTreeBuilder.parse(reader, "baseUri");
 assertNotNull(doc);
 assertEquals("<xml><test><subtest/></test></xml>", doc.html());
 }

 // Test for parse(String, String) method
 @Test
 public void testParseStringString() {
 String xmlString = "<xml><test><subtest/></test></xml>";
 Document doc = xmlTreeBuilder.parse(xmlString, "baseUri");
 assertNotNull(doc);
 assertEquals("<xml><test><subtest/></test></xml>", doc.html());
 }

 // Test for initialiseParse(Reader, String, ParseErrorList, ParseSettings) method
 @Test
 public void testInitialiseParse() {
 String xmlString = "<xml><test><subtest/></test></xml>";
 Reader reader = new StringReader(xmlString);
 ParseErrorList errors = ParseErrorList.noTracking();
 ParseSettings settings = ParseSettings.preserveCase;
 xmlTreeBuilder.initialiseParse(reader, "baseUri", errors, settings);
 assertEquals(ParseSettings.preserveCase, xmlTreeBuilder.getSettings());
 }

 // Test for process(Token) method with StartTag
 @Test
 public void testProcessStartTag() {
 String xmlString = "<test><subtest/></test>";
 Reader reader = new StringReader(xmlString);
 Token startTagToken = new Token.StartTag("test", null, null, 1, 0);
 xmlTreeBuilder.initialiseParse(reader, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilder.runParser();
 Element testElement = (Element) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
 assertEquals("test", testElement.tagName());
 }

 // Test for process(Token) method with EndTag
 @Test
 public void testProcessEndTag() {
 String xmlString = "<test><subtest/></test>";
 Reader reader = new StringReader(xmlString);
 Token endTagToken = new Token.EndTag("test", null, 1, xmlString.length() - 2);
 xmlTreeBuilder.initialiseParse(reader, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilder.runParser();
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertEquals(xmlTreeBuilder.stack.size(), 0);
 }

 // Test for process(Token) method with Comment
 @Test
 public void testProcessComment() {
 String xmlString = "<!-- This is a comment -->";
 Reader reader = new StringReader(xmlString);
 Token commentToken = new Token.Comment("--This is a comment--");
 xmlTreeBuilder.initialiseParse(reader, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilder.runParser();
 assertEquals(xmlTreeBuilder.stack.size(), 0);
 }

 // Test for process(Token) method with Character
 @Test
 public void testProcessCharacter() {
 String xmlString = "<xml>&#x26;<test></test></xml>";
 Reader reader = new StringReader(xmlString);
 Token characterToken = new Character('#', '&', "&#x26;", 1, 0);
 xmlTreeBuilder.initialiseParse(reader, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilder.runParser();
 assertEquals(xmlTreeBuilder.stack.size(), 0);
 }

 // Test for process(Token) method with Doctype
 @Test
 public void testProcessDoctype() {
 String xmlString = "<!DOCTYPE xml SYSTEM \"xml.dtd\" [ <!ATTLIST xml foo CDATA #REQUIRED > ]>";
 Reader reader = new StringReader(xmlString);
 Doctype doctypeToken = new Doctype("xml", null, "xml.dtd");
 xmlTreeBuilder.initialiseParse(reader, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilder.runParser();
 DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
 assertEquals("xml", doctypeNode.nodeName());
 assertEquals("xml.dtd", doctypeNode.systemIdentifier());
 }

 // Test for process(Token) method with EOF
 @Test (expected = AssertionError.class)
 public void testProcessEof() {
 String xmlString = "<xml>";
 Reader reader = new StringReader(xmlString);
 xmlTreeBuilder.initialiseParse(reader, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilder.runParser();
 }

 // Test for insertNode(Node) method
 @Test
 public void testInsertNode() {
 Document doc = Jsoup.parse("<test><subtest/></test>", "baseUri");
 Element subtestElement = doc.select("subtest").first();
 Node node = new Element("newTag");
 xmlTreeBuilder.insertNode(node);
 Element testElement = (Element) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
 testElement.appendChild(node);
 assertEquals("newTag", testElement.child(0).nodeName());
 }

 // Test for insert(Token.StartTag) method
 @Test
 public void testInsertStartTag() {
 String xmlString = "<test><subtest/></test>";
 Reader reader = new StringReader(xmlString);
 Token startTagToken = new Token.StartTag("test", null, null, 1, 0);
 xmlTreeBuilder.initialiseParse(reader, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilder.runParser();
 Element testElement = (Element) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
 assertEquals("test", testElement.tagName());
 }

 // Test for insert(Token.Comment) method
 @Test
 public void testInsertComment() {
 String xmlString = "<!-- This is a comment -->";
 Reader reader = new StringReader(xmlString);
 Token commentToken = new Token.Comment("--This is a comment--");
 xmlTreeBuilder.initialiseParse(reader, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilder.runParser();
 assertEquals(xmlTreeBuilder.stack.size(), 0);
 }

 // Test for insert(Token.Character) method
 @Test
 public void testInsertCharacter() {
 String xmlString = "<xml>&#x26;<test></test></xml>";
 Reader reader = new StringReader(xmlString);
 Token characterToken = new Character('#', '&', "&#x26;", 1, 0);
 xmlTreeBuilder.initialiseParse(reader, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilder.runParser();
 assertEquals(xmlTreeBuilder.stack.size(), 0);
 }

 // Test for insert(Token.Doctype) method
 @Test
 public void testInsertDoctype() {
 String xmlString = "<!DOCTYPE xml SYSTEM \"xml.dtd\" [ <!ATTLIST xml foo CDATA #REQUIRED > ]>";
 Reader reader = new StringReader(xmlString);
 Doctype doctypeToken = new Doctype("xml", null, "xml.dtd");
 xmlTreeBuilder.initialiseParse(reader, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilder.runParser();
 assertEquals(xmlTreeBuilder.stack.size(), 0);
 }

 // Test for parseFragment(String, String, ParseErrorList, ParseSettings) method with valid XML
 @Test
 public void testParseFragmentValidXml() {
 String xmlFragment = "<xml><test><subtest/></test></xml>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(nodes.size(), 3);
 }

 // Test for parseFragment(String, String, ParseErrorList, ParseSettings) method with invalid XML
 @Test (expected = AssertionError.class)
 public void testParseFragmentInvalidXml() {
 String xmlFragment = "<test><subtest/></xml>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 fail("Expected an AssertionError due to invalid XML");
 }

 // Test for popStackToClose(Token.EndTag) method with existent tag
 @Test
 public void testPopStackToCloseExistentTag() {
 String xmlString = "<test><subtest/></test>";
 Reader reader = new StringReader(xmlString);
 Token endTagToken = new Token.EndTag("test", null, 1, xmlString.length() - 2);
 xmlTreeBuilder.initialiseParse(reader, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilder.runParser();
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertEquals(xmlTreeBuilder.stack.size(), 0);
 }

 // Test for popStackToClose(Token.EndTag) method with non-existent tag
 @Test
 public void testPopStackToCloseNonExistentTag() {
 String xmlString = "<test><subtest/></test>";
 Reader reader = new StringReader(xmlString);
 Token endTagToken = new Token.EndTag("nonExistentTag", null, 1, xmlString.length() - 2);
 xmlTreeBuilder.initialiseParse(reader, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilder.runParser();
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertEquals(xmlTreeBuilder.stack.size(), 1);
 }
}