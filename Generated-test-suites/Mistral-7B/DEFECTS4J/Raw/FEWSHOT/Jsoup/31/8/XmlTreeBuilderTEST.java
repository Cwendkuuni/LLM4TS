package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.*;
import org.junit.Test;

public class XmlTreeBuilderTest {

 XmlTreeBuilder builder = new XmlTreeBuilder();

 @Test
 public void testInitialiseParse() {
 String input = "<root><child></child></root>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();

 builder.initialiseParse(input, baseUri, errors);
 assertNotNull(builder.doc);
 assertSame(input, builder.doc().body().html());
 }

 @Test
 public void testProcessStartTag() {
 String input = "<root><child><grandchild/></child></root>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();

 builder.initialiseParse(input, baseUri, errors);

 Token.StartTag startTag = new Token.StartTag("child");
 Element el = builder.insert(startTag);
 assertNotNull(el);
 assertSame("child", el.nodeName());
 }

 @Test
 public void testProcessEndTag() {
 String input = "<root><child></child></root>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();

 builder.initialiseParse(input, baseUri, errors);

 Token.EndTag endTag = new Token.EndTag("child");
 builder.popStackToClose(endTag);
 assertNull(builder.peekStack());
 }

 @Test
 public void testProcessComment() {
 String input = "<root><!--comment--></root>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();

 builder.initialiseParse(input, baseUri, errors);

 Token.Comment comment = new Token.Comment("comment");
 builder.insert(comment);
 assertNotNull(builder.peekStack());
 }

 @Test
 public void testProcessCharacter() {
 String input = "<root>text</root>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();

 builder.initialiseParse(input, baseUri, errors);

 Token.Character character = new Token.Character('t');
 builder.insert(character);
 assertNotNull(builder.peekStack());
 }

 @Test
 public void testProcessDoctype() {
 String input = "<!DOCTYPE root [ <!ATTLIST root attr CDATA #REQUIRED> ]>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();

 builder.initialiseParse(input, baseUri, errors);

 Token.Doctype doctype = new Token.Doctype("root", null, null, null);
 builder.insert(doctype);
 assertNotNull(builder.peekStack());
 }

 @Test
 public void testProcessUnexpectedTokenType() {
 String input = "<root>"; // no closing tag
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();

 builder.initialiseParse(input, baseUri, errors);

 Token invalidToken = new Token(-1, null);
 assertNull(builder.insert(invalidToken));
 }

 @Test
 public void testPopStackToCloseMultipleOccurrences() {
 String input = "<root><child><grandchild/><child></child></root>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();

 builder.initialiseParse(input, baseUri, errors);

 Token.EndTag endTagChild = new Token.EndTag("child");
 Token.EndTag endTagGrandchild = new Token.EndTag("grandchild");
 builder.popStackToClose(endTagGrandchild);
 builder.popStackToClose(endTagChild);
 assertNull(builder.peekStack());
 }

 @Test
 public void testPopStackToCloseNoMatchingTag() {
 String input = "<root><child></child></root>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();

 builder.initialiseParse(input, baseUri, errors);

 Token.EndTag endTagUnknown = new Token.EndTag("unknown");
 builder.popStackToClose(endTagUnknown);
 assertSame(builder.peekStack().size(), 1);
 }
}