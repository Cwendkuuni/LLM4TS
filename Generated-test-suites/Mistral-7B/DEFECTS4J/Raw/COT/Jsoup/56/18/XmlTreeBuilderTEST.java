package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private String input = "<xml><tag attr=\"value\">Content</tag></xml>";

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
 }

 @Test
 public void testParse() {
 Document document = xmlTreeBuilder.parse(input, "baseUri");
 assertNotNull(document);
 assertEquals("xml", document.rootElement().tagName());
 }

 @Test
 public void testInitialiseParse() {
 String baseUri = "baseUri";
 ParseErrorList errors = ParseErrorList.noTracking();
 ParseSettings settings = ParseSettings.preserveCase;
 xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
 assertNotNull(xmlTreeBuilder.doc);
 assertTrue(xmlTreeBuilder.doc.outputSettings().syntax() == Document.OutputSettings.Syntax.xml);
 }

 @Test
 public void testProcessStartTag() {
 Token startTag = new Token.StartTag("tag", null, null, null, null, null, null, null, null, null, null);
 Element actual = xmlTreeBuilder.insert(startTag);
 Element expected = new Element("tag", "baseUri", null);
 assertEquals(expected.tagName(), actual.tagName());
 assertEquals(expected.baseUri(), actual.baseUri());
 }

 @Test
 public void testProcessEndTag() {
 Token endTag = new Token.EndTag("tag");
 xmlTreeBuilder.popStackToClose(endTag);
 assertTrue(xmlTreeBuilder.stack.isEmpty());
 }

 @Test
 public void testProcessComment() {
 Token commentToken = new Token.Comment("comment");
 xmlTreeBuilder.insert(commentToken);
 Comment actual = (Comment) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("comment", actual.getData());
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token.Character('a');
 xmlTreeBuilder.insert(characterToken);
 TextNode actual = (TextNode) xmlTreeBuilder.doc.childNodes().get(1);
 assertEquals("a", actual.text());
 }

 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Token.Doctype("xml", null, null);
 DocumentType actual = (DocumentType) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("xml", actual.nodeName());
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 Token token = new Token(-1);
 xmlTreeBuilder.process(token);
 }

 @Test
 public void testInsertNode() {
 Element element = new Element("tag", "baseUri", null);
 xmlTreeBuilder.insertNode(element);
 assertEquals(xmlTreeBuilder.doc.childNodes().get(0), element);
 }

 @Test
 public void testParseFragment() {
 String inputFragment = "<tag attr=\"value\"/>";
 List<Node> actualNodes = xmlTreeBuilder.parseFragment(inputFragment, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(1, actualNodes.size());
 assertEquals(Element.class, actualNodes.get(0).getClass());
 }

}