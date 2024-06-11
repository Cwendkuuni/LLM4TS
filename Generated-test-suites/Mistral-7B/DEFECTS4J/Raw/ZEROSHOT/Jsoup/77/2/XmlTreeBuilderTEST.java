package org.jsoup.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.helper.Validate;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder builder;
 private Document document;

 @Before
 public void setUp() {
 builder = new XmlTreeBuilder();
 document = new Document();
 }

 @Test
 public void testParse() {
 String xml = "<xml><tag attr='value'><subtag/></tag></xml>";
 document = builder.parse(new StringReader(xml), "http://test.com");
 Element root = document.rootElement();
 assertNotNull(root);
 assertEquals("xml", root.tagName());
 assertEquals("http://test.com", document.baseUri());
 assertNull(document.outputSettings().getDeclaredTagForms());

 Element tag = root.element("tag");
 assertNotNull(tag);
 assertEquals("tag", tag.tagName());
 assertEquals("value", tag.attr("attr"));
 assertNull(tag.child(0));

 Element subtag = tag.child(0);
 assertNull(subtag);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullBaseUri() {
 builder.parse(new StringReader("<xml/>"), null);
 }

 @Test
 public void testParseFragment() {
 String xml = "<xml><tag attr='value'><subtag/></tag></xml>";
 List<Node> nodes = builder.parseFragment(xml, "http://test.com", ParseErrorList.noTracking(), XmlTreeBuilder.ParseSettings.preserveCase);
 assertNotNull(nodes);
 assertEquals(4, nodes.size());

 Element root = (Element) nodes.get(0);
 assertEquals("xml", root.tagName());

 Element tag = (Element) nodes.get(1);
 assertEquals("tag", tag.tagName());
 assertEquals("value", tag.attr("attr"));

 Element subtag = (Element) nodes.get(3);
 assertNull(subtag.child(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithNullInput() {
 builder.parseFragment(null, "http://test.com", ParseErrorList.noTracking(), XmlTreeBuilder.ParseSettings.preserveCase);
 }

 @Test
 public void testProcessStartTag() {
 String xml = "<xml><tag attr='value'/></xml>";
 builder = new XmlTreeBuilder();
 document = new Document();
 builder.initialiseParse(new StringReader(xml), "http://test.com", ParseErrorList.noTracking(), XmlTreeBuilder.ParseSettings.preserveCase);
 Token token = builder.nextToken();
 assertEquals(Token.StartTag.class, token.getClass());
 Element tag = builder.insert(token.asStartTag());
 assertEquals("tag", tag.tagName());
 assertEquals("value", tag.attr("attr"));
 }

 @Test
 public void testProcessEndTag() {
 String xml = "<xml><tag><subtag/></tag></xml>";
 builder = new XmlTreeBuilder();
 document = new Document();
 builder.initialiseParse(new StringReader(xml), "http://test.com", ParseErrorList.noTracking(), XmlTreeBuilder.ParseSettings.preserveCase);
 Token token = builder.nextToken();
 assertEquals(Token.StartTag.class, token.getClass());
 Element tag = builder.insert(token.asStartTag());
 tag.appendChild(new Element("subtag"));
 token = builder.nextToken();
 assertEquals(Token.EndTag.class, token.getClass());
 builder.popStackToClose(token.asEndTag());
 assertNull(tag.child(0));
 }

 @Test
 public void testProcessComment() {
 String xml = "<xml><!-- comment --></xml>";
 builder = new XmlTreeBuilder();
 document = new Document();
 builder.initialiseParse(new StringReader(xml), "http://test.com", ParseErrorList.noTracking(), XmlTreeBuilder.ParseSettings.preserveCase);
 Token token = builder.nextToken();
 assertEquals(Token.Comment.class, token.getClass());
 Comment comment = (Comment) builder.insert(token);
 assertEquals("comment", comment.data());
 }

 @Test
 public void testProcessCharacter() {
 String xml = "<xml>&#123;abc</xml>";
 builder = new XmlTreeBuilder();
 document = new Document();
 builder.initialiseParse(new StringReader(xml), "http://test.com", ParseErrorList.noTracking(), XmlTreeBuilder.ParseSettings.preserveCase);
 Token token = builder.nextToken();
 assertEquals(Token.Character.class, token.getClass());
 TextNode text = (TextNode) builder.insert(token);
 assertEquals("&#123;abc", text.text());
 }

 @Test
 public void testProcessDoctype() {
 String xml = "<xml><!DOCTYPE xml SYSTEM \"xml.dtd\">";
 builder = new XmlTreeBuilder();
 document = new Document();
 try {
 builder.initialiseParse(new StringReader(xml), "http://test.com", ParseErrorList.noTracking(), XmlTreeBuilder.ParseSettings.preserveCase);
 Token token = builder.nextToken();
 assertEquals(Token.Doctype.class, token.getClass());
 DocumentType doctype = (DocumentType) builder.insert(token);
 assertEquals("xml", doctype.nodeName());
 assertEquals("xml.dtd", doctype.systemId());
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessUnexpectedTokenType() {
 String xml = "<xml><tag><!-- comment --></tag></xml>";
 builder = new XmlTreeBuilder();
 document = new Document();
 builder.initialiseParse(new StringReader(xml), "http://test.com", ParseErrorList.noTracking(), XmlTreeBuilder.ParseSettings.preserveCase);
 builder.nextToken(); // start tag
 builder.nextToken(); // comment
 Validate.expect(IllegalArgumentException.class);
 builder.nextToken(); // end tag
 }

 @After
 public void tearDown() {
 builder = null;
 document = null;
 }
}