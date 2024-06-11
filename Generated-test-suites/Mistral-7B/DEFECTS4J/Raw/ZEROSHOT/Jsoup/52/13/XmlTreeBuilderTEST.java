package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;

import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 doc = Jsoup.parse("<root><tag attr='value'/><comment>This is a comment</comment></root>", "", Parser.xmlParser());
 xmlTreeBuilder = (XmlTreeBuilder) doc.parser();
 }

 @Test
 public void testInitialiseParse() {
 assertEquals("root", xmlTreeBuilder.doc.nodeName());
 assertNotNull(xmlTreeBuilder.stack);
 }

 @Test
 public void testProcessStartTag() {
 StartTag startTag = new StartTag("tag", null, null);
 Element el = xmlTreeBuilder.insert(startTag);
 assertEquals("tag", el.nodeName());
 assertEquals("value", el.attr("attr"));
 }

 @Test
 public void testProcessEndTag() {
 EndTag endTag = new EndTag("tag");
 xmlTreeBuilder.popStackToClose(endTag);
 assertEquals(0, xmlTreeBuilder.stack.size());
 }

 @Test
 public void testProcessComment() {
 Comment comment = new Comment("This is a comment");
 xmlTreeBuilder.insert(comment);
 assertEquals(1, doc.childNodes().size());
 assertEquals("This is a comment", doc.childNodes().get(0).text());
 }

 @Test
 public void testProcessCharacter() {
 Character character = new Character('A');
 xmlTreeBuilder.insert(character);
 assertEquals(2, doc.childNodes().size());
 assertEquals("A", doc.childNodes().get(1).text());
 }

 @Test
 public void testProcessDoctype() {
 Doctype doctype = new Doctype("root", null, null, null);
 xmlTreeBuilder.insert(doctype);
 assertEquals(3, doc.childNodes().size());
 assertEquals("root", doc.childNodes().get(2).nodeName());
 }

 @Test
 public void testPopStackToClose() {
 StartTag startTag1 = new StartTag("tag1", null, null);
 Element tag1 = xmlTreeBuilder.insert(startTag1);
 xmlTreeBuilder.stack.add(tag1);
 EndTag endTag1 = new EndTag("tag1");
 xmlTreeBuilder.popStackToClose(endTag1);
 assertEquals(0, xmlTreeBuilder.stack.size());
 StartTag startTag2 = new StartTag("tag2", null, null);
 Element tag2 = xmlTreeBuilder.insert(startTag2);
 xmlTreeBuilder.stack.add(tag2);
 EndTag endTag2 = new EndTag("tag2");
 xmlTreeBuilder.popStackToClose(endTag2);
 assertEquals(0, xmlTreeBuilder.stack.size());
 }

 @Test
 public void testParseFragment() {
 String inputFragment = "<root><tag attr='value'/><comment>This is a comment</comment></root>";
 List<Node> parsedFragment = xmlTreeBuilder.parseFragment(inputFragment, "", new ParseErrorList());
 assertEquals(2, parsedFragment.size());
 assertEquals("tag", parsedFragment.get(0).nodeName());
 assertEquals("This is a comment", parsedFragment.get(1).nodeName());
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 Token unexpectedToken = new Token(-1, "Unexpected token type: 123");
 xmlTreeBuilder.process(unexpectedToken);
 }
}