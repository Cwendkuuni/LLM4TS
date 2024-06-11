package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.CharacterData;
import org.jsoup.parser.Token.Comment.Bogus;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder builder;
 private Document doc;

 @Before
 public void setUp() {
 builder = new XmlTreeBuilder();
 doc = builder.parse(new StringReader(""), "");
 }

 @Test
 public void testDefaultSettings() {
 ParseSettings settings = builder.defaultSettings();
 assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
 }

 @Test
 public void testParseWithReaderAndBaseUri() {
 String xml = "<xml><tag attr=\"value\"></tag></xml>";
 Document doc = builder.parse(new StringReader(xml), "");
 Element root = doc.getRootElement();
 assertEquals("xml", root.tagName());
 assertEquals(1, root.childNodes().size());
 assertEquals("tag", root.childNodes().get(0).tagName());
 assertEquals("value", root.childNodes().get(0).attributes().get("attr").getValue());
 }

 @Test
 public void testParseWithStringInputAndBaseUri() {
 String xml = "<xml><tag attr=\"value\"></tag></xml>";
 Document doc = builder.parse(xml, "");
 Element root = doc.getRootElement();
 assertEquals("xml", root.tagName());
 assertEquals(1, root.childNodes().size());
 assertEquals("tag", root.childNodes().get(0).tagName());
 assertEquals("value", root.childNodes().get(0).attributes().get("attr").getValue());
 }

 @Test
 public void testInitialiseParse() {
 String xml = "<xml><tag attr=\"value\"></tag></xml>";
 StringReader reader = new StringReader(xml);
 Document doc = builder.parse(reader, "");
 Element root = doc.getRootElement();
 assertNotNull(doc.getRootElement());
 assertEquals("xml", root.tagName());
 assertEquals(1, root.childNodes().size());
 assertEquals("tag", root.childNodes().get(0).tagName());
 assertEquals("value", root.childNodes().get(0).attributes().get("attr").getValue());

 assertEquals(1, doc.childNodes().size());
 assertEquals(doc, doc.childNodes().get(0));
 }

 @Test
 public void testProcessStartTag() {
 String xml = "<xml><tag attr=\"value\"></tag></xml>";
 StringReader reader = new StringReader(xml);
 Document doc = builder.parse(reader, "");
 Element root = doc.getRootElement();
 Element tag = root.childNodes().get(0).cast();
 assertEquals("tag", tag.tagName());
 assertEquals(0, tag.childNodes().size());
 assertEquals("value", tag.attributes().get("attr").getValue());
 }

 @Test
 public void testProcessEndTag() {
 String xml = "<xml><tag><tag2></tag2></tag></xml>";
 StringReader reader = new StringReader(xml);
 Document doc = builder.parse(reader, "");
 Element root = doc.getRootElement();
 Element tag = root.childNodes().get(0).cast();
 assertEquals("tag", tag.tagName());
 assertEquals(1, tag.childNodes().size());
 Element tag2 = tag.childNodes().get(0).cast();
 assertEquals("tag2", tag2.tagName());
 assertEquals(0, tag2.childNodes().size());
 }

 @Test
 public void testProcessComment() {
 String xml = "<!-- This is a comment -->";
 StringReader reader = new StringReader(xml);
 Document doc = builder.parse(reader, "");
 List<Node> comments = doc.childNodes();
 assertEquals(1, comments.size());
 assertTrue(comments.get(0) instanceof Comment);
 assertEquals("This is a comment", ((Comment) comments.get(0)).getData());
 }

 @Test
 public void testProcessCharacter() {
 String xml = "<xml>abc</xml>";
 StringReader reader = new StringReader(xml);
 Document doc = builder.parse(reader, "");
 List<Node> textNodes = doc.getElementsByTag("text");
 assertEquals(1, textNodes.size());
 assertEquals("abc", textNodes.get(0).text());
 }

 @Test(expected = IllegalStateException.class)
 public void testProcessUnknownTagAsSelfClosing() {
 String xml = "<unknown/>";
 StringReader reader = new StringReader(xml);
 Document doc = builder.parse(reader, "");
 List<Node> nodes = doc.childNodes();
 assertEquals(1, nodes.size());
 assertTrue(nodes.get(0) instanceof Element);
 Element element = nodes.get(0).cast();
 assertFalse(element.isSelfClosing());
 }

 @Test
 public void testInsertNode() {
 String xml = "<xml><tag><subtag></subtag></tag></xml>";
 StringReader reader = new StringReader(xml);
 Document doc = builder.parse(reader, "");
 Element tag = doc.getElementsByTag("tag").get(0).cast();
 Element subtag = doc.getElementsByTag("subtag").get(0).cast();
 assertEquals(1, tag.childNodes().size());
 assertEquals(subtag, tag.childNodes().get(0));
 }

 @Test
 public void testPopStackToClose() {
 String xml = "<xml><tag><tag2></tag2></tag></xml>";
 StringReader reader = new StringReader(xml);
 Document doc = builder.parse(reader, "");
 Element tag = doc.getElementsByTag("tag").get(0).cast();
 Element tag2 = doc.getElementsByTag("tag2").get(0).cast();
 assertEquals(1, tag.childNodes().size());
 assertEquals(tag2, tag.childNodes().get(0));

 tag2 = doc.getElementsByTag("tag2").get(1).cast(); // there is another tag2, but this is not relevant here
 assertEquals(0, tag.childNodes().size());
 }

 @Test
 public void testParseFragment() {
 String xml = "<xml><tag attr=\"value\"><subtag></subtag></tag></xml>";
 List<Node> nodes = builder.parseFragment("<xml><tag attr=\"value\"><subtag></subtag></tag></xml>", "", ParseErrorList.noTracking(), ParseSettings.preserveCase).get(0).childNodes();
 Element tag = nodes.get(0).cast();
 assertEquals("tag", tag.tagName());
 assertEquals(1, tag.childNodes().size());
 Element subtag = tag.childNodes().get(0).cast();
 assertEquals("subtag", subtag.tagName());
 assertEquals(0, subtag.childNodes().size());
 assertEquals("value", tag.attributes().get("attr").getValue());
 }

 @Test
 public void testProcessDocumentType() {
 String xml = "<!DOCTYPE xml PUBLIC \"-//W3C//DTD XML 1.0//EN\" \"xml.dtd\">";
 StringReader reader = new StringReader(xml);
 Document doc = builder.parse(reader, "");
 DocumentType doctype = doc.getDoctype();
 assertEquals("xml", doctype.getName());
 assertEquals("xml.dtd", doctype.getSystemIdentifier());
 }

 @Test
 public void testBogusCommentHandling() {
 String xml = "<!DOCTYPE xml PUBLIC \"-//W3C//DTD XML 1.0//EN\" \"xml.dtd\">" +
               "<!-- This is a comment -->" +
               "<xml><tag attr=\"value\"></tag></xml>";
 StringReader reader = new StringReader(xml);
 Document doc = builder.parse(reader, "");
 List<Node> nodes = doc.childNodes();
 assertEquals(3, nodes.size());
 assertTrue(nodes.get(0) instanceof DocumentType);
 assertTrue(nodes.get(1) instanceof Comment);
 Element tag = nodes.get(2).cast();
 assertEquals("xml", tag.tagName());
 assertEquals(1, tag.childNodes().size());
 assertEquals("tag", tag.childNodes().get(0).tagName());
 assertEquals("value", tag.childNodes().get(0).attributes().get("attr").getValue());
 }
}