package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

/**
 * XmlTreeBuilderTest
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
 String input = "<xml>\n<tag attr='value'/>\n</xml>";
 String baseUri = "http://www.example.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);
 assertNotNull(xmlTreeBuilder.doc);
 }

 @Test(expected = AssertionError.class)
 public void testInvalidTokenType() {
 Token invalidToken = new Token(-1, "invalid_token");
 xmlTreeBuilder.process(invalidToken);
 }

 @Test
 public void testProcessStartTag() {
 String input = "<xml>\n<tag attr='value'/>\n</xml>";
 String baseUri = "http://www.example.com";
 Token startTag = new Token(Token.StartTag.class, "<tag attr='value'>", null, null, "tag", null, null, null, null);
 Element expectedTag = new Element("tag", baseUri, new NamedAttributeMap(){{
 put("attr", new Attribute("value", "value"));
 }});
 xmlTreeBuilder.initialiseParse(input, baseUri, null);
 assertEquals(expectedTag, xmlTreeBuilder.insert(startTag));
 }

 @Test
 public void testProcessEndTag() {
 String input = "<xml>\n<tag>\n<tag2/>\n</tag>\n</xml>";
 String baseUri = "http://www.example.com";
 Token startTag = new Token(Token.StartTag.class, "<tag>", null, null, "tag", null, null, null, null);
 Token endTag = new Token(Token.EndTag.class, "</tag2>", null, null, "tag2", null, null, null, null);
 xmlTreeTreeBuilder.initialiseParse(input, baseUri, null);
 xmlTreeBuilder.process(startTag);
 xmlTreeBuilder.process(startTag); // for tag
 xmlTreeBuilder.process(endTag);
 xmlTreeBuilder.process(endTag); // for tag2
 assertFalse(xmlTreeBuilder.stack.isEmpty());
 }

 @Test(expected = AssertionError.class)
 public void testUnknownTagWithEndTag() {
 String input = "<xml>\n<unknown>\n</unknown>\n</xml>";
 String baseUri = "http://www.example.com";
 Token startTag = new Token(Token.StartTag.class, "<unknown>", null, null, "unknown", null, null, null, null);
 Token endTag = new Token(Token.EndTag.class, "</unknown>", null, null, "unknown", null, null, null, null);
 xmlTreeTreeBuilder.initialiseParse(input, baseUri, null);
 xmlTreeBuilder.process(startTag);
 xmlTreeBuilder.process(endTag);
 }

 @Test
 public void testProcessComment() {
 String input = "<xml>\n<!-- comment -->\n</xml>";
 String baseUri = "http://www.example.com";
 Token commentToken = new Token(Token.Comment.class, " --comment-- ", null, null);
 xmlTreeTreeBuilder.initialiseParse(input, baseUri, null);
 xmlTreeBuilder.insert(commentToken);
 assertFalse(xmlTreeBuilder.doc.childNodes().isEmpty());
 }

 @Test
 public void testProcessCharacter() {
 String input = "<xml>\n<tag>\nchar\n</tag>\n</xml>";
 String baseUri = "http://www.example.com";
 Token characterToken = new Token(Token.Character.class, "\nchar", 0, null);
 xmlTreeTreeBuilder.initialiseParse(input, baseUri, null);
 xmlTreeBuilder.insert(characterToken);
 assertFalse(xmlTreeBuilder.doc.childNodes().isEmpty());
 }

 @Test
 public void testProcessDoctype() {
 String input = "<xml>\n<!DOCTYPE xml SYSTEM \"xml.dtd\">\n</xml>";
 String baseUri = "http://www.example.com";
 Token doctypeToken = new Token(Token.Doctype.class, "<!DOCTYPE xml SYSTEM \"xml.dtd\">", null, null, null, "xml.dtd", null, null, null);
 xmlTreeTreeBuilder.initialiseParse(input, baseUri, null);
 xmlTreeBuilder.insert(doctypeToken);
 assertFalse(xmlTreeBuilder.doc.childNodes().isEmpty());
 }

 @Test
 public void testPopStackToClose() {
 String input = "<xml>\n<tag>\n<tag2/>\n</tag>\n</xml>";
 String baseUri = "http://www.example.com";
 Token startTag = new Token(Token.StartTag.class, "<tag>", null, null, "tag", null, null, null, null);
 Token endTag = new Token(Token.EndTag.class, "</tag2>", null, null, "tag2", null, null, null, null);
 xmlTreeTreeBuilder.initialiseParse(input, baseUri, null);
 xmlTreeBuilder.process(startTag);
 xmlTreeBuilder.process(startTag); // for tag
 xmlTreeBuilder.process(endTag);
 xmlTreeBuilder.process(endTag); // for tag2
 xmlTreeBuilder.popStackToClose(new Token.EndTag(null, "</tag2>", null, null, "tag2", null, null, null, null));
 assertFalse(xmlTreeBuilder.stack.isEmpty());
 }

 @Test
 public void testPopStackToCloseWithUnknownTag() {
 String input = "<xml>\n<tag>\n<unknown>\n</tag>\n</xml>";
 String baseUri = "http://www.example.com";
 Token startTag = new Token(Token.StartTag.class, "<tag>", null, null, "tag", null, null, null, null);
 xmlTreeTreeBuilder.initialiseParse(input, baseUri, null);
 xmlTreeBuilder.process(startTag);
 xmlTreeBuilder.popStackToClose(new Token.EndTag(null, "</unknown>", null, null, "unknown", null, null, null, null));
 assertFalse(xmlTreeBuilder.stack.isEmpty());
 }

 @Test(expected = AssertionError.class)
 public void testPopStackToCloseWithEmptyStack() {
 String baseUri = "http://www.example.com";
 xmlTreeTreeBuilder.popStackToClose(new Token.EndTag(null, "</tag>", null, null, "tag", null, null, null, null));
 }

 @Test
 public void testInsertNode() {
 String baseUri = "http://www.example.com";
 Document doc = new Document();
 Element tag = new Element("tag", baseUri);
 Comment comment = new Comment("comment", baseUri);
 Text text = new TextNode("text", baseUri);
 DocumentType doctype = new DocumentType("xml", null, null, baseUri);
 xmlTreeBuilder.insertNode(tag);
 xmlTreeBuilder.insertNode(comment);
 xmlTreeBuilder.insertNode(text);
 xmlTreeBuilder.insertNode(doctype);
 Element root = xmlTreeBuilder.doc;
 assertEquals(tag, root.child(0));
 assertEquals(comment, root.child(1));
 assertEquals(text, root.child(2));
 assertEquals(doctype, root.child(3));
 }

}