package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertSame(ParseSettings.preserveCase, settings);
 }

 @Test
 public void testParseWithReaderAndBaseUri() {
 String xmlString = "<xml><tag attr='value'><subTag/></tag></xml>";
 Reader input = new StringReader(xmlString);
 String baseUri = "http://example.com";
 Document doc = xmlTreeBuilder.parse(input, baseUri);
 Element root = doc.rootElement();
 assertEquals("xml", root.tagName());
 assertEquals("value", root.child(0).attribute("attr"));
 assertNotNull(root.child(0).child(0));
 }

 @Test
 public void testParseWithStringAndBaseUri() {
 String xmlString = "<xml><tag attr='value'><subTag/></tag></xml>";
 String baseUri = "http://example.com";
 Document doc = xmlTreeBuilder.parse(xmlString, baseUri);
 Element root = doc.rootElement();
 assertEquals("xml", root.tagName());
 assertEquals("value", root.child(0).attribute("attr"));
 assertNotNull(root.child(0).child(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitialiseParseWithNullBaseUri() {
 String xmlString = "<xml><tag attr='value'><subTag/></tag></xml>";
 String baseUri = null;
 xmlTreeBuilder.initialiseParse(new StringReader(xmlString), baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitialiseParseWithNullInput() {
 String baseUri = "http://example.com";
 xmlTreeBuilder.initialiseParse(null, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
 }

 @Test
 public void testProcessStartTag() {
 String xmlString = "<xml><tag attr='value'><subTag/></tag></xml>";
 Document doc = Jsoup.parse(xmlString, Parse.xmlParser());
 Element tag = doc.select("tag").first();
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 XmlTreeBuilder.Token startTag = new XmlTreeBuilder.Token(XmlTreeBuilder.Token.Type.StartTag, "<tag", null, null, null, null, null, null, null, null, null, null, null);
 assertEquals(tag, xmlTreeBuilder.insert(startTag));
 }

 @Test
 public void testProcessEndTag() {
 String xmlString = "<xml><tag><subTag/></tag></xml>";
 Document doc = Jsoup.parse(xmlString, Parse.xmlParser());
 Element tag = doc.select("tag").first();
 Element subTag = tag.child(0);
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 XmlTreeBuilder.Token endTag = new XmlTreeBuilder.Token(XmlTreeBuilder.Token.Type.EndTag, "\t/tag", null, null, null, null, null, null, null, null, null, null, null);
 xmlTreeBuilder.popStackToClose(endTag);
 assertEquals(subTag, xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessEndTagWithInvalidTagName() {
 String xmlString = "<xml><tag><subTag/></tag></xml>";
 Document doc = Jsoup.parse(xmlString, Parse.xmlParser());
 Element tag = doc.select("tag").first();
 Element subTag = tag.child(0);
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 XmlTreeBuilder.Token endTag = new XmlTreeBuilder.Token(XmlTreeBuilder.Token.Type.EndTag, "\t/invalidTag", null, null, null, null, null, null, null, null, null, null, null);
 xmlTreeBuilder.popStackToClose(endTag);
 }

 @Test
 public void testProcessComment() {
 String xmlString = "<xml><!-- comment --></xml>";
 Document doc = Jsoup.parse(xmlString, Parse.xmlParser());
 Comment comment = doc.select("xml\\*:comment").first();
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 XmlTreeBuilder.Token commentToken = new XmlTreeBuilder.Token(XmlTreeBuilder.Token.Type.Comment, null, null, "-- comment --", null, null, null, null, null, null, null, null, null);
 assertEquals(comment, xmlTreeBuilder.insert(commentToken));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessCommentWithNullData() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 XmlTreeBuilder.Token commentToken = new XmlTreeBuilder.Token(XmlTreeBuilder.Token.Type.Comment, null, null, null, null, null, null, null, null, null, null, null, null);
 xmlTreeBuilder.insert(commentToken);
 }

 @Test
 public void testProcessCharacter() {
 String xmlString = "<xml>&lt;value&gt;</xml>";
 Document doc = Jsoup.parse(xmlString, Parse.xmlParser());
 TextNode textNode = doc.select("xml\\*:text()[1]").first();
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 XmlTreeBuilder.Token characterToken = new XmlTreeBuilder.Token(XmlTreeBuilder.Token.Type.Character, "&lt;value&gt;", null, null, null, null, null, null, null, null, null, null, null);
 assertEquals(textNode, xmlTreeBuilder.insert(characterToken));
 }

 @Test
 public void testProcessDoctype() {
 String xmlString = "<!DOCTYPE xml SYSTEM \"xml.dtd\">";
 Document doc = Jsoup.parse(xmlString, Parse.xmlParser());
 DocumentType documentType = doc.select("!DOCTYPE").first();
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 XmlTreeBuilder.Token doctypeToken = new XmlTreeBuilder.Token(XmlTreeBuilder.Token.Type.Doctype, null, "xml", null, "xml.dtd", null, null, null, null, null, null, null, null);
 assertEquals(documentType, xmlTreeBuilder.insert(doctypeToken));
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<xml><tag attr='value'><subTag/></tag></xml>";
 String baseUri = "http://example.com";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(2, nodes.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithNullInputFragment() {
 String baseUri = "http://example.com";
 xmlTreeBuilder.parseFragment(null, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithNullBaseUri() {
 String xmlFragment = "<xml><tag attr='value'><subTag/></tag></xml>";
 ParseErrorList errors = ParseErrorList.noTracking();
 xmlTreeBuilder.parseFragment(xmlFragment, null, errors, ParseSettings.preserveCase);
 }

}