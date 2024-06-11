package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.StringReader;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.TokenType;
import org.junit.Test;

/**
 * Test cases for XmlTreeBuilder class.
 *
 * @author JohnDoe
 */
public class XmlTreeBuilderTest {

 // Basic test cases for all methods

 @Test
 public void testDefaultSettings() {
     ParseSettings settings = XmlTreeBuilder.defaultSettings();
     assertNotNull(settings);
 }

 @Test
 public void testParseWithReader() {
     String xmlString = "<xml><tag attr='value'></tag></xml>";
     Reader input = new StringReader(xmlString);
     Document doc = new XmlTreeBuilder().parse(input, "baseUri");
     assertNotNull(doc);
     assertEquals(1, doc.childNodes().size());
 }

 @Test
 public void testParseWithString() {
     String xmlString = "<xml><tag attr='value'></tag></xml>";
     Document doc = new XmlTreeBuilder().parse(xmlString, "baseUri");
     assertNotNull(doc);
     assertEquals(1, doc.childNodes().size());
 }

 @Test
 public void testInitialiseParse() {
     String xmlString = "<xml><tag attr='value'></tag></xml>";
     Reader input = new StringReader(xmlString);
     ParseErrorList errors = ParseErrorList.noTracking();
     ParseSettings settings = ParseSettings.preserveCase;
     XmlTreeBuilder builder = new XmlTreeBuilder();
     builder.initialiseParse(input, "baseUri", errors, settings);
     assertNotNull(builder.doc);
 }

 @Test
 public void testProcess() {
     String xmlString = "<xml><tag attr='value'></tag></xml>";
     Reader input = new StringReader(xmlString);
     XmlTreeBuilder builder = new XmlTreeBuilder();
     Document doc = builder.parse(input, "baseUri");
     List<Node> nodes = doc.childNodes();
     for (Node node : nodes) {
         Token token = builder.tokenizer.next();
         builder.process(token);
         assertEquals(node, builder.currentElement());
     }
 }

 // Test cases for edge cases and exception handling

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
     String xmlString = "<xml><tag attr='value'></tag></xml>";
     Reader input = new StringReader(xmlString);
     new XmlTreeBuilder().parse(input, null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullInput() {
     new XmlTreeBuilder().parse(null, "baseUri");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyInput() {
     Reader input = new StringReader("");
     new XmlTreeBuilder().parse(input, "baseUri");
 }

 @Test
 public void testUnknownTag() {
     String xmlString = "<unknownTag></unknownTag>";
     Reader input = new StringReader(xmlString);
     Document doc = new XmlTreeBuilder().parse(input, "baseUri");
     assertNotNull(doc);
     Element element = (Element) doc.select("unknownTag").first();
     assertNotNull(element);
 }

 @Test
 public void testSelfClosingTag() {
     String xmlString = "<tag selfClosing/>";
     Reader input = new StringReader(xmlString);
     Document doc = new XmlTreeBuilder().parse(input, "baseUri");
     assertEquals(1, doc.childNodes().size());
     Element element = (Element) doc.select("tag").first();
     assertNotNull(element);
     assertTrue(element.hasAttrAttr("selfClosing"));
     assertEquals("selfClosing", element.attr("selfClosing"));
 }

 @Test
 public void testComment() {
     String xmlString = "<!-- This is a comment -->";
     Reader input = new StringReader(xmlString);
     Document doc = new XmlTreeBuilder().parse(input, "baseUri");
     assertEquals(1, doc.childNodes().size());
     Comment comment = (Comment) doc.childNodes().get(0);
     assertEquals("This is a comment", comment.getData());
 }

 @Test
 public void testCDataSection() {
     String xmlString = "<![CDATA[ <html><head><title>Test CDATA</title></head><body><p>Test CDATA</p></body> ]]>";
     Reader input = new StringReader(xmlString);
     Document doc = new XmlTreeBuilder().parse(input, "baseUri");
     assertEquals(1, doc.childNodes().size());
     Element html = (Element) doc.select("html").first();
     assertEquals(3, html.childNodes().size());
     Element head = (Element) html.select("head").first();
     assertEquals(1, head.childNodes().size());
     Element title = (Element) head.select("title").first();
     assertEquals("Test CDATA", title.text());
     Element body = (Element) html.select("body").first();
     assertEquals(1, body.childNodes().size());
     Element p = (Element) body.select("p").first();
     assertEquals("Test CDATA", p.text());
 }

 @Test
 public void testDoctype() {
     String xmlString = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";
     Reader input = new StringReader(xmlString);
     Document doc = new XmlTreeBuilder().parse(input, "baseUri");
     assertEquals(1, doc.childNodes().size());
     DocumentType doctype = (DocumentType) doc.childNodes().get(0);
     assertEquals("html", doctype.nodeName());
     assertEquals("about:legacy-compat", doctype.getPublicIdentifier());
     assertNull(doctype.getSystemIdentifier());
 }

 @Test
 public void testParseFragment() {
     String xmlFragment = "<tag attr='value'></tag>";
     String baseUri = "baseUri";
     ParseErrorList errors = ParseErrorList.noTracking();
     ParseSettings settings = ParseSettings.preserveCase;
     List<Node> nodes = new XmlTreeBuilder().parseFragment(xmlFragment, baseUri, errors, settings);
     assertEquals(1, nodes.size());
     Element element = (Element) nodes.get(0);
     assertEquals("tag", element.nodeName());
     assertEquals("value", element.attr("attr"));
 }

 @Test
 public void testPopStackToClose() {
     String xmlString = "<outerTag><innerTag></innerTag></outerTag>";
     Reader input = new StringReader(xmlString);
     XmlTreeBuilder builder = new XmlTreeBuilder();
     builder.parse(input, "baseUri");
     Element outerTag = (Element) builder.doc.select("outerTag").first();
     Element innerTag = (Element) outerTag.select("innerTag").first();
     builder.popStackToClose((Token.EndTag) parser.tokenizer.next());
     List<Node> nodes = outerTag.childNodes();
     assertEquals(0, nodes.size());
 }

}