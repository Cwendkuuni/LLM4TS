package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.parser.XmlTreeBuilder;

import java.util.List;

/**
 * Test class for XmlTreeBuilder.
 */
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private ParseSettings parseSettings;
 private ParseErrorList parseErrorList;
 private Document document;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 parseSettings = xmlTreeBuilder.defaultSettings();
 parseErrorList = ParseErrorList.noTracking();
 }

 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.preserveCase, settings);
 }

 @Test
 public void testParse() {
 String xmlString = "<root><child>Hello World</child></root>";
 document = xmlTreeBuilder.parse(xmlString, "");
 assertEquals("<root><child>Hello World</child></root>", document.html());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullInput() {
 xmlTreeBuilder.parse(null, "");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullBaseUri() {
 xmlTreeBuilder.parse("<root></root>", null);
 }

 @Test
 public void testProcessStartTag() {
 Token startTagToken = new Token.StartTag("root", null, null, null, null, null, null);
 Element rootElement = xmlTreeBuilder.insert(startTagToken);
 assertEquals("root", rootElement.tagName());
 }

 @Test
 public void testProcessEndTag() {
 Token endTagToken = new Token.EndTag("root");
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertTrue(xmlTreeBuilder.stack.isEmpty());
 }

 @Test
 public void testProcessComment() {
 Token commentToken = new Token.Comment("-- This is a comment.");
 xmlTreeBuilder.insert(commentToken);
 List<Node> comments = document.getElementsByTag("comment");
 assertEquals(1, comments.size());
 assertEquals("-- This is a comment.", comments.get(0).text());
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token.Character('A');
 xmlTreeBuilder.insert(characterToken);
 List<Node> children = document.getElementsByTag("text").get(0).childNodes();
 assertEquals(1, children.size());
 assertEquals("A", children.get(0).text());
 }

 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Token.Doctype("html", "public \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"");
 xmlTreeBuilder.insert(doctypeToken);
 List<Node> doctypes = document.getElementsByTag("document-type");
 assertEquals(1, doctypes.size());
 assertEquals("html", doctypes.get(0).tagName());
 }

 @Test
 public void testProcessEmptyInput() {
 document = xmlTreeBuilder.parse("", "");
 assertEquals("<document></document>", document.html());
 }

 @Test
 public void testProcessInvalidInput() {
 String invalidXmlString = "<root><child>Hello <script>World</script></child></root>";
 try {
 document = xmlTreeBuilder.parse(invalidXmlString, "");
 fail("Should have thrown XMLParseException");
 } catch (org.jsoup.parser.ParseError e) {
 // pass
 }
 List<Node> scriptTags = document.getElementsByTag("script");
 assertEquals(0, scriptTags.size());
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<root><child>Hello</child></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "", parseErrorList, parseSettings);
 assertEquals(2, nodes.size());
 assertEquals("root", nodes.get(0).tagName());
 assertEquals("child", nodes.get(1).tagName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithNullInput() {
 xmlTreeBuilder.parseFragment(null, "", parseErrorList, parseSettings);
 }

 @Test
 public void testParseFragmentWithEmptyInput() {
 List<Node> nodes = xmlTreeBuilder.parseFragment("", "", parseErrorList, parseSettings);
 assertEquals(0, nodes.size());
 }

}