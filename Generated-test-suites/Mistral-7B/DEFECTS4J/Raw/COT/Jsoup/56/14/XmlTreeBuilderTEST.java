package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder builder;

 @Before
 public void setUp() {
 builder = new XmlTreeBuilder();
 }

 // Test defaultSettings method
 @Test
 public void testDefaultSettings() {
 ParseSettings settings = builder.defaultSettings();
 assertEquals(ParseSettings.preserveCase, settings);
 }

 // Test parse method
 @Test
 public void testParse() {
 String xmlString = "<root><child><subchild/></subchild></child></root>";
 Document xmlDoc = builder.parse(xmlString, "");
 assertEquals("<root><child><subchild/></subchild></child></root>", xmlDoc.html());
 }

 // Test initialiseParse method
 @Test
 public void testInitialiseParse() {
 String xmlString = "<root><child><subchild/></subchild></child></root>";
 builder.parse(xmlString, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(XmlTreeBuilder.class, builder.stack.peek().getClass());
 assertSame(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());
 }

 // Test process method with StartTag
 @Test
 public void testProcessStartTag() {
 String xmlString = "<root><child><subchild/></subchild></child></root>";
 Token token = builder.tokenizer.next();
 builder.process(token);
 assertEquals("root", builder.stack.peek().nodeName());
 }

 // Test process method with EndTag
 @Test
 public void testProcessEndTag() {
 String xmlString = "<root><child><subchild/></subchild></child></root>";
 Token token = builder.tokenizer.next(); // StartTag
 builder.process(token);

 token = builder.tokenizer.next(); // EndTag
 builder.process(token);

 assertEquals("child", builder.stack.size() > 0 ? builder.stack.peek().nodeName() : null);
 assertFalse(builder.stack.isEmpty());
 }

 // Test process method with Comment
 @Test
 public void testProcessComment() {
 String xmlString = "<!-- Test Comment -->";
 Token token = builder.tokenizer.next();
 builder.process(token);
 assertEquals(Comment.class, builder.stack.peek().getClass());
 }

 // Test process method with Character
 @Test
 public void testProcessCharacter() {
 String xmlString = "<root><child>Test</child></root>";
 Token token = builder.tokenizer.next(); // StartTag
 builder.process(token);

 token = builder.tokenizer.next(); // Character
 builder.process(token);

 assertEquals("Test", builder.stack.peek().childNodes().get(0).text());
 }

 // Test process method with Doctype
 @Test
 public void testProcessDoctype() {
 String xmlString = "<!DOCTYPE root [ <!ATTLIST myatt Attribute ( " +
 "type CDATA #IMPLIED" +
 "value CDATA #IMPLIED) > ]><root></root>";
 Token token = builder.tokenizer.next(); // Doctype
 builder.process(token);
 Document doc = builder.doc;
 assertEquals("<!DOCTYPE root [ <!ATTLIST myatt Attribute ( type CDATA #IMPLIED" +
 " value CDATA #IMPLIED) > ]><root></root>", doc.html());
 }

 // Test insertNode method
 @Test
 public void testInsertNode() {
 String xmlString = "<root><child></child></root>";
 Token startTag = builder.tokenizer.next();
 Element child = builder.insert(startTag);
 Node node = new TextNode("Test", "");
 builder.insertNode(node);
 child.appendChild(node);
 assertEquals("Test", child.childNodes().get(0).text());
 }

 // Test popStackToClose method with no matching tag
 @Test(expected = AssertionError.class)
 public void testPopStackToCloseNoMatchingTag() {
 String xmlString = "<root><child></child></root>";
 builder.popStackToClose(new Token.EndTag("unknownTag"));
 }

 // Test popStackToClose method with matching tag
 @Test
 public void testPopStackToClose() {
 String xmlString = "<root><child><subchild/></subchild></child></root>";
 builder.parse(xmlString, "");
 builder.popStackToClose(new Token.EndTag("child"));
 assertEquals("root", builder.stack.peek().nodeName());
 }

 // Test parseFragment method
 @Test
 public void testParseFragment() {
 String xmlFragment = "<root><child><subchild/></subchild></child></root>";
 List<Node> nodes = builder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(3, nodes.size());
 }
}