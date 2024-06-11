package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.jsoup.parser.*;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder builder;
 private ParseErrorList errors;
 private Document doc;

 @Before
 public void setup() {
 builder = new XmlTreeBuilder();
 errors = new ParseErrorList();
 doc = new Document();
 }

 // Test for the constructor
 @Test
 public void testXmlTreeBuilderConstructor() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 assertNotNull(xmlTreeBuilder);
 }

 // Test for the initialiseParse method
 @Test
 public void testInitialiseParse() {
 builder.initialiseParse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><child/><child/></root>", "", errors);
 assertEquals("root", doc.tagName());
 }

 // Test for the process method - StartTag
 @Test
 public void testProcessStartTag() {
 builder.initialiseParse("<root><child/></root>", "", errors);
 Token token = new Token.StartTag("root");
 builder.process(token);
 assertEquals("root", doc.tagName());
 }

 // Test for the process method - EndTag
 @Test
 public void testProcessEndTag() {
 builder.initialiseParse("<root><child><child/></child></root>", "", errors);
 Token token = new Token.EndTag("child");
 builder.process(token);
 assertEquals(1, stack.size());
 }

 // Test for the process method - Comment
 @Test
 public void testProcessComment() {
 builder.initialiseParse("<!-- This is a comment -->\n<root><child/></root>", "", errors);
 Token token = new Token.Comment("<!-- This is a comment -->");
 builder.process(token);
 assertEquals(1, doc.childNodes().size());
assertEquals("!-- This is a comment -->", doc.childNodes().get(0).toString());
 }

 // Test for the process method - Character
 @Test
 public void testProcessCharacter() {
 builder.initialiseParse("<root><child>Hello World</child></root>", "", errors);
 Token token = new Token.Character('H');
 builder.process(token);
 assertEquals(1, doc.childNodes().size());
 Element child = (Element) doc.childNodes().get(0);
 assertEquals("Hello World", child.text());
 }

 // Test for the process method - Doctype
 @Test
 public void testProcessDoctype() {
 builder.initialiseParse("<!DOCTYPE root SYSTEM \"root.dtd\">\n<root><child/></root>", "", errors);
 Token token = new Token.Doctype("root");
 builder.process(token);
 assertEquals(1, doc.childNodes().size());
 DocumentType doctype = (DocumentType) doc.childNodes().get(0);
 assertEquals("root", doctype.getName());
 }

 // Test for the popStackToClose method - No matching element in the stack
 @Test (expected = AssertionError.class)
 public void testPopStackToCloseNoMatchingElement() {
 builder.initialiseParse("<root><child><child/></child></root>", "", errors);
 Token token = new Token.EndTag("grandchild");
 builder.popStackToClose(token);
 }

 // Test for the popStackToClose method - Matching element in the stack
 @Test
 public void testPopStackToCloseMatchingElement() {
 builder.initialiseParse("<root><child><child><grandchild/></grandchild></child></root>", "", errors);
 Token token = new Token.EndTag("grandchild");
 builder.popStackToClose(token);
 assertEquals(2, stack.size());
 }

 // Test for the parseFragment method
 @Test
 public void testParseFragment() {
 List<Node> nodes = builder.parseFragment("<root><child/><child/></root>", "", errors);
 assertEquals(2, nodes.size());
 }

 // Test for the parseFragment method with errors
 @Test
 public void testParseFragmentWithErrors() {
 List<Node> nodes = builder.parseFragment("<root><child><child><grandchild/></grandchild></child>", "", errors);
 assertEquals(2, nodes.size());
 assertEquals(1, errors.size());
 assertEquals("Unexpected end tag: grandchild", errors.get(0).getMessage());
 }

 // Test for exception handling - Unknown token type
 @Test (expected = AssertionError.class)
 public void testUnknownTokenType() {
 builder.initialiseParse("<root>", "", errors);
 builder.process(new Token(-1, ""));
 }

 // Test for exception handling - Null input
 @Test (expected = NullPointerException.class)
 public void testNullInput() {
 builder.initialiseParse(null, "", errors);
 }

 // Test for exception handling - Null baseUri
 @Test (expected = NullPointerException.class)
 public void testNullBaseUri() {
 builder.initialiseParse("<root>", null, errors);
 }

 // Test for exception handling - Null errors
 @Test (expected = NullPointerException.class)
 public void testNullErrors() {
 builder.initialiseParse("<root>", "", null);
 }
}