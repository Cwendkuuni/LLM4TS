package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.util.Iterator;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder builder;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 builder = new XmlTreeBuilder();
 }

 @Test
 public void testInitialiseParse() {
 String input = "<xml></xml>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);
 assertEquals(Document.class, xmlTreeBuilder.getDocument());
 }

 @Test
 public void testProcessStartTag() {
 String input = "<tag attr1='val1' attr2='val2'/>";
 Token startTagToken = new Token.StartTag("tag", new AttributeSet());
 builder.initialiseParse(input, null, null);
 builder.process(startTagToken);
 Element tagElement = (Element) builder.getCurrentRootElement();
 assertEquals("tag", tagElement.nodeName());
 assertEquals("val1", tagElement.attr("attr1"));
 assertEquals("val2", tagElement.attr("attr2"));
 }

 @Test
 public void testProcessEndTag() {
 String input = "<tag><subtag/></tag>";
 Token startTagToken = new Token.StartTag("tag", new AttributeSet());
 Token endTagToken = new Token.EndTag("tag");
 builder.initialiseParse(input, null, null);
 builder.process(startTagToken);
 builder.process(endTagToken);
 assertEquals(0, builder.stack.size());
 }

 @Test
 public void testProcessComment() {
 String input = "<!-- comment -->";
 Token commentToken = new Token.Comment("comment");
 builder.initialiseParse(input, null, null);
 builder.process(commentToken);
 assertEquals(1, builder.getDocument().childNodes().size());
 assertTrue(builder.getDocument().childNodes().get(0) instanceof Comment);
 }

 @Test
 public void testProcessCharacter() {
 String input = "<tag>text</tag>";
 Token characterToken = new Token.Character('t');
 builder.initialiseParse(input, null, null);
 builder.process(characterToken);
 assertEquals(1, builder.getDocument().childNodes().size());
 assertTrue(builder.getDocument().childNodes().get(0) instanceof TextNode);
 assertEquals("text", builder.getDocument().childNodes().get(0).text());
 }

 @Test
 public void testProcessDoctype() {
 String input = "<!DOCTYPE xml SYSTEM 'xml.dtd' [ ]>";
 Token doctypeToken = new Token.Doctype("xml", null, null, null);
 builder.initialiseParse(input, null, null);
 builder.process(doctypeToken);
 assertEquals(1, builder.getDocument().childNodes().size());
 assertTrue(builder.getDocument().childNodes().get(0) instanceof DocumentType);
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 String input = "invalid";
 Token unexpectedToken = new Token(100, null);
 builder.initialiseParse(input, null, null);
 builder.process(unexpectedToken);
 }

 @Test
 public void testPopStackToClose() {
 String input = "<tag><subtag><subsubtag/></subtag></tag>";
 Token startTagToken = new Token.StartTag("tag", new AttributeSet());
 Token subTagStartToken = new Token.StartTag("subtag", new AttributeSet());
 Token subSubTagEndToken = new Token.EndTag("subsubtag");
 Token endTagToken = new Token.EndTag("tag");
 builder.initialiseParse(input, null, null);
 builder.process(startTagToken);
 builder.process(subTagStartToken);
 builder.process(subSubTagEndToken);
 builder.process(endTagToken);
 Iterator<Element> it = builder.stack.descendingIterator();
 Element tagElement = it.next();
 assertEquals("tag", tagElement.nodeName());
 it.remove();
 assertEquals(0, builder.stack.size());
 }

}