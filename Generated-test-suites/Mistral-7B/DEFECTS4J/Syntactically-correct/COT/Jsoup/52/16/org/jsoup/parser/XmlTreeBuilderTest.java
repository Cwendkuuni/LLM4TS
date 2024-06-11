package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder builder;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 builder = new XmlTreeBuilder();
 }

 @Test
 public void testInitialiseParse() {
 String input = "<root><child/></root>";
 String baseUri = "http://test.com";
 ParseErrorList errors = new ParseErrorList();
 builder.initialiseParse(input, baseUri, errors);
 assertEquals("http://test.com", builder.doc().baseUri());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitialiseParseNullInput() {
 String baseUri = "http://test.com";
 ParseErrorList errors = new ParseErrorList();
 builder.initialiseParse(null, baseUri, errors);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitialiseParseNullBaseUri() {
 String input = "<root><child/></root>";
 ParseErrorList errors = new ParseErrorList();
 builder.initialiseParse(input, null, errors);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitialiseParseNullErrors() {
 String input = "<root><child/></root>";
 String baseUri = "http://test.com";
 builder.initialiseParse(input, baseUri, null);
 }

 @Test
 public void testProcessStartTag() {
 String input = "<root><child/></root>";
 builder.initialiseParse(input, "http://test.com", new ParseErrorList());
 builder.parseFragment(input, "http://test.com", new ParseErrorList());
 Element root = (Element) builder.doc().childNodes().get(0);
 Element child = (Element) root.children().get(0);
 assertTrue(child.nodeName().equals("child"));
 }

 @Test
 public void testProcessEndTag() {
 String input = "<root><child/></root>";
 builder.initialiseParse(input, "http://test.com", new ParseErrorList());
 builder.parseFragment(input, "http://test.com", new ParseErrorList());
 builder.popStackToClose(new Token.EndTag("child"));
 assertEquals(0, builder.doc().childNodes().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessEndTagNullEndTag() {
 builder.popStackToClose(null);
 }

 @Test
 public void testProcessComment() {
 String input = "<!--comment-->";
 builder.initialiseParse(input, "http://test.com", new ParseErrorList());
 builder.parseFragment(input, "http://test.com", new ParseErrorList());
 Comment comment = (Comment) builder.doc().childNodes().get(0);
 String commentText = comment.data();
 assertTrue(commentText.equals("comment"));
 }

 @Test
 public void testProcessCharacter() {
 String input = "<root>&#x26;<child/></root>";
 builder.initialiseParse(input, "http://test.com", new ParseErrorList());
 builder.parseFragment(input, "http://test.com", new ParseErrorList());
 TextNode childText = (TextNode) ((Element) builder.doc().childNodes().get(1)).childNodes().get(0);
 String childTextData = childText.data();
 String expectedText = "&amp;";
 assertTrue(childTextData.equals(expectedText));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessCharacterNullCharacterToken() {
 builder.insert(null);
 }

 @Test
 public void testProcessDoctype() {
 String input = "<!DOCTYPE root SYSTEM \"file:test.xml\">";
 builder.initialiseParse(input, "http://test.com", new ParseErrorList());
 builder.parseFragment(input, "http://test.com", new ParseErrorList());
 DocumentType doctype = (DocumentType) builder.doc().childNodes().get(0);
 String doctypeName = doctype.name();
 String doctypePublicId = doctype.publicId();
 String doctypeSystemId = doctype.systemId();
 assertTrue(doctypeName.equals("root"));
 assertTrue(doctypePublicId.equals("file:test.xml"));
 assertTrue(doctypeSystemId.equals("file:test.xml"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessDoctypeNullDoctypeToken() {
 builder.insert(new Token.Doctype(null, null, null, null));
 }

 @Test
 public void testParseFragment() {
 String input = "<root><child><grandchild/></child></root>";
 List<Node> nodes = builder.parseFragment(input, "http://test.com", new ParseErrorList());
 Element root = (Element) nodes.get(0);
 Element child = (Element) root.children().get(0);
 Element grandchild = (Element) child.children().get(0);
 assertTrue(root.nodeName().equals("root"));
 assertTrue(child.nodeName().equals("child"));
 assertTrue(grandchild.nodeName().equals("grandchild"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullInput() {
 List<Node> nodes = builder.parseFragment(null, "http://test.com", new ParseErrorList());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullBaseUri() {
 List<Node> nodes = builder.parseFragment("<root><child/></root>", null, new ParseErrorList());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullErrors() {
 List<Node> nodes = builder.parseFragment("<root><child/></root>", "http://test.com", null);
 }
}