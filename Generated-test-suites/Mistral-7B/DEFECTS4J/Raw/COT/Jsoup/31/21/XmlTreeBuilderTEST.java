package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.*;
import org.junit.Test;

/**
 * XmlTreeBuilderTest.java
 */
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testInitialiseParse() {
 String input = "<xml><tag/></xml>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);
 assertNotNull(xmlTreeBuilder.doc);
 }

 @Test(expected = AssertionError.class)
 public void testInvalidTokenType() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Token invalidToken = new Token(-1, 0, "invalid token");
 xmlTreeBuilder.process(invalidToken);
 }

 @Test
 public void testProcessStartTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String input = "<tag/><tag/><tag attr='value'/><tag attr='value' attr2='value2'/>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);

 Token startTag1 = new Token.StartTag("tag", null, null);
 Token startTag2 = new Token.StartTag("tag", null, null);
 Token startTag3 = new Token.StartTag("tag", null, new AttributeSet("attr", "value"));
 Token startTag4 = new Token.StartTag("tag", null, new AttributeSet("attr", "value", "attr2", "value2"));

 xmlTreeBuilder.process(startTag1);
 assertNotNull(xmlTreeBuilder.currentElement());
 xmlTreeBuilder.process(startTag2);
 assertNotNull(xmlTreeBuilder.currentElement().tagName(), "Expected tag name: tag, but was: " + xmlTreeBuilder.currentElement().tagName());
 xmlTreeBuilder.process(startTag3);
 assertNotNull(xmlTreeBuilder.currentElement().tagName(), "Expected tag name: tag, but was: " + xmlTreeBuilder.currentElement().tagName());
 assertNotNull(xmlTreeBuilder.currentElement().attr("attr"), "Expected attr value: value, but was: " + xmlTreeBuilder.currentElement().attr("attr"));
 xmlTreeBuilder.process(startTag4);
 assertNotNull(xmlTreeBuilder.currentElement().tagName(), "Expected tag name: tag, but was: " + xmlTreeBuilder.currentElement().tagName());
 assertNotNull(xmlTreeBuilder.currentElement().attr("attr"), "Expected attr value: value, but was: " + xmlTreeBuilder.currentElement().attr("attr"));
 assertNotNull(xmlTreeBuilder.currentElement().attr("attr2"), "Expected attr value: value2, but was: " + xmlTreeBuilder.currentElement().attr("attr2"));
 }

 @Test
 public void testProcessEndTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String input = "<tag><subtag/></tag>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);

 Token startTag = new Token.StartTag("tag", null, null);
 xmlTreeBuilder.process(startTag);
 assertNotNull(xmlTreeBuilder.currentElement());
 Token endTag = new Token.EndTag("tag");
 xmlTreeBuilder.process(endTag);
 assertNull(xmlTreeBuilder.currentElement());
 }

 @Test
 public void testProcessEmptyTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String input = "<tag/>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);

 Token startTag = new Token.StartTag("tag", null, null);
 xmlTreeBuilder.process(startTag);
 xmlTreeBuilder.tokeniser.acknowledgeSelfClosingFlag();
 assertNull(xmlTreeBuilder.currentElement());
 }

 @Test
 public void testProcessCommentTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String input = "<!-- comment -->";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);

 Token commentToken = new Token.Comment("-- comment --");
 xmlTreeBuilder.process(commentToken);
 assertNotNull(xmlTreeBuilder.lastChild());
 assertEquals("comment", xmlTreeBuilder.lastChild().nodeName());
 }

 @Test
 public void testProcessCharacterTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String input = "<tag>&#x1;\u00A9</tag>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);

 Token characterToken = new Token.Character("&#x1;");
 xmlTreeBuilder.process(characterToken);
 xmlTreeBuilder.process(new Token.Character("\u00A9"));
 assertNotNull(xmlTreeBuilder.lastChild());
 assertEquals("&#x1;", xmlTreeBuilder.lastChild().nodeName());
 }

 @Test
 public void testProcessDoctypeTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String input = "<!DOCTYPE tag SYSTEM \"file.dtd\">";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);

 Token doctypeToken = new Token.Doctype("tag", null, null, null);
 xmlTreeBuilder.process(doctypeToken);
 assertNotNull(xmlTreeBuilder.lastChild());
 assertEquals("tag", xmlTreeBuilder.lastChild().nodeName());
 assertEquals("file.dtd", xmlTreeBuilder.lastChild().systemId());
 }

 @Test(expected = AssertionError.class)
 public void testUnknownTagName() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String input = "<unknown_tag/>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);

 Token startTag = new Token.StartTag("unknown_tag", null, null);
 xmlTreeBuilder.process(startTag);
 }

 @Test(expected = AssertionError.class)
 public void testUnknownAttributeName() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String input = "<tag attr_unknown='value'/>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);

 Token startTag = new Token.StartTag("tag", null, new AttributeSet("attr_unknown", "value"));
 xmlTreeBuilder.process(startTag);
 }

 @Test
 public void testPopStackToClose() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String input = "<tag><tag/><tag/><tag/></tag>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);

 Token startTag = new Token.StartTag("tag", null, null);
 xmlTreeBuilder.process(startTag);
 assertNotNull(xmlTreeBuilder.currentElement());

 Token endTag1 = new Token.EndTag("tag");
 xmlTreeBuilder.process(endTag1);
 assertNotNull(xmlTreeBuilder.currentElement());

 Token endTag2 = new Token.EndTag("tag");
 xmlTreeBuilder.process(endTag2);
 assertNull(xmlTreeBuilder.currentElement());
 }

 @Test(expected = AssertionError.class)
 public void testPopStackToCloseEmpty() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String input = "<tag></tag>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);

 Token startTag = new Token.StartTag("tag", null, null);
 xmlTreeBuilder.process(startTag);
 assertNotNull(xmlTreeBuilder.currentElement());

 Token endTag = new Token.EndTag("tag");
 xmlTreeBuilder.process(endTag);
 xmlTreeBuilder.popStackToClose(new Token.EndTag("tag"));
 }

 @Test(expected = AssertionError.class)
 public void testPopStackToCloseUnmatchedTagName() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String input = "<tag><tag/><unknown_tag/></tag>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);

 Token startTag = new Token.StartTag("tag", null, null);
 xmlTreeBuilder.process(startTag);
 assertNotNull(xmlTreeBuilder.currentElement());

 Token endTag = new Token.EndTag("tag");
 xmlTreeBuilder.process(endTag);
 assertNotNull(xmlTreeBuilder.currentElement());

 Token endTagWrong = new Token.EndTag("unknown_tag");
 xmlTreeBuilder.popStackToClose(endTagWrong);
 }
}