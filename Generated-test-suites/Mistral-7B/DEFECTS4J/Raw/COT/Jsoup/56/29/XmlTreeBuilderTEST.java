package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder builder;
 private Document doc;

 @Before
 public void setUp() {
 builder = new XmlTreeBuilder();
 doc = Jsoup.parse("<xml rootElement='test'><tag attr='value'><subTag/></tag></xml>", "baseUri", Parser.xmlParser());
 }

 @Test
 public void testDefaultSettings() {
 ParseSettings settings = builder.defaultSettings();
 Assert.assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
 }

 @Test
 public void testParse() {
 Document parsedDoc = builder.parse("<xml rootElement='test'><tag attr='value'><subTag/></tag></xml>", "baseUri");
 Assert.assertEquals(doc, parsedDoc);
 }

 @Test
 public void testInitialiseParse() {
 String input = "<xml rootElement='test'><tag attr='value'><subTag/></tag></xml>";
 ParseErrorList errors = ParseErrorList.noTracking();
 ParseSettings settings = ParseSettings.preserveCase;
 builder.initialiseParse(input, "baseUri", errors, settings);
 Assert.assertNotNull(builder.doc);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessInvalidTokenType() {
 builder = new XmlTreeBuilder();
 Token token = new Token(1, "test", Token.TokenType.UNKNOWN);
 Assert.assertFalse(builder.process(token));
 }

 @Test
 public void testInsertNode() {
 Node node = new Element("test", "baseUri");
 builder.insertNode(node);
 Assert.assertNotNull(builder.currentElement().lastChild());
 }

 @Test
 public void testInsertStartTag() {
 Token startTag = new Token.StartTag("tag", null, null, null, true, null, null, null, null);
 Element el = builder.insert(startTag);
 Assert.assertEquals("tag", el.tagName());
 Assert.assertEquals("baseUri", el.baseUri());
 }

 @Test
 public void testInsertComment() {
 Token commentToken = new Token.Comment("<!-- test comment -->");
 builder.insert(commentToken);
 Node commentNode = builder.currentElement().lastChild();
 Assert.assertInstanceOf(Comment.class, commentNode);
 }

 @Test
 public void testInsertCharacter() {
 Token characterToken = new Token.Character('A');
 builder.insert(characterToken);
 Node characterNode = builder.currentElement().lastChild();
 Assert.assertInstanceOf(TextNode.class, characterNode);
 Assert.assertEquals("A", characterNode.text());
 }

 @Test
 public void testInsertDoctype() {
 Token doctype = new Token.Doctype("xml", null, null, null);
 builder.insert(doctype);
 Node doctypeNode = builder.currentElement().lastChild();
 Assert.assertInstanceOf(DocumentType.class, doctypeNode);
 }

 @Test
 public void testPopStackToClose() {
 String input = "<xml rootElement='test'><tag attr='value'><tag2/><tag3/></tag></xml>";
 builder = new XmlTreeBuilder();
 builder.parse(input, "baseUri");
 Element tag1 = builder.currentElement();
 Element tag2 = tag1.getChild(0);
 Element tag3 = tag1.getChild(1);
 Token tag2End = new Token.EndTag("tag2");
 Token tag3End = new Token.EndTag("tag3");
 builder.popStackToClose(tag2End);
 builder.popStackToClose(tag3End);
 Assert.assertEquals(0, tag1.childNodes().size());
 }

 @Test
 public void testParseFragment() {
 String input = "<tag attr='value'><subTag/></tag>";
 List<Node> parsedNodes = builder.parseFragment(input, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Assert.assertEquals(2, parsedNodes.size());
 }
}