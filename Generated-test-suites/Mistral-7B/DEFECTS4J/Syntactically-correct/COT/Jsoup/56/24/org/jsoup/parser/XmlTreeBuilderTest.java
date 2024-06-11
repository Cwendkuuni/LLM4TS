package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private ParseSettings parseSettings;
 private String inputXml;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 parseSettings = xmlTreeBuilder.defaultSettings();
 inputXml = "<root><child tag=\"test\"/><anotherChild/><comment><![CDATA[Hello World]]></comment></root>";
 }

 @Test
 public void testDefaultSettings() {
 assertNotNull(xmlTreeBuilder);
 assertNotNull(parseSettings);
 }

 @Test
 public void testParse() {
 Document document = xmlTreeBuilder.parse(inputXml, null);
 assertNotNull(document);
 assertNotNull(document.childNodes());
 assertEquals(4, document.childNodes().size());
 }

 @Test
 public void testInitialiseParse() {
 xmlTreeBuilder = new XmlTreeBuilder();
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();
 ParseSettings settings = parseSettings();
 xmlTreeBuilder.initialiseParse(inputXml, baseUri, errors, settings);
 assertNotNull(xmlTreeBuilder.doc);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessUnexpectedTokenType() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String input = "<xml>";
 xmlTreeBuilder.initialiseParse(input, null, null, parseSettings());
 xmlTreeBuilder.tokeniser.next(); // simulate next token
 Validate.fail("Unexpected test case passed"); // this line should never be reached
 }

 @Test
 public void testInsertStartTag() {
 Element rootElement = xmlTreeBuilder.insert(tokenizer.getStartTag("root"));
 assertNotNull(rootElement);
 assertEquals("root", rootElement.nodeName());
 }

 @Test
 public void testInsertEndTag() {
 xmlTreeBuilder.push(new Element("root"));
 xmlTreeBuilder.insert(tokenizer.getEndTag("root"));
 assertNull(xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1));
 }

 @Test
 public void testInsertComment() {
 xmlTreeBuilder.push(new Element("root"));
 Token commentToken = tokenizer.getComment("Hello World");
 Comment comment = (Comment) xmlTreeBuilder.insert(commentToken);
 Node insert = comment;
 assertNotNull(comment);
 assertEquals("Hello World", comment.data());
 }

 @Test
 public void testInsertCharacter() {
 xmlTreeBuilder.push(new Element("root"));
 Token characterToken = tokenizer.getCharacter('A');
 TextNode textNode = (TextNode) xmlTreeBuilder.insert(characterToken);
 assertNotNull(textNode);
 assertEquals("A", textNode.text());
 }

 @Test
 public void testInsertDocumentType() {
 xmlTreeBuilder.push(new Element("root"));
 DocumentType documentType = (DocumentType) xmlTreeBuilder.insert(tokenizer.getDoctype("html", null, null, null));
 assertNotNull(documentType);
 }

 @Test
 public void testPopStackToClose() {
 xmlTreeBuilder.push(new Element("root"));
 xmlTreeBuilder.push(new Element("child"));
 xmlTreeBuilder.popStackToClose(tokenizer.getEndTag("child"));
 assertEquals(1, xmlTreeBuilder.stack.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPopStackToCloseUnknownTag() {
 xmlTreeBuilder.push(new Element("root"));
 xmlTreeBuilder.push(new Element("child"));
 xmlTreeBuilder.popStackToClose(tokenizer.getEndTag("unknownTag"));
 }

 @Test
 public void testParseFragment() {
 List<Node> nodes = xmlTreeBuilder.parseFragment("<root><child tag=\"test\"/><anotherChild/> <comment><![CDATA[Hello World]]></comment></root>", null, new ParseErrorList(), parseSettings());
 assertNotNull(nodes);
 assertEquals(4, nodes.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentInvalidInput() {
 List<Node> nodes = xmlTreeBuilder.parseFragment("Invalid Input", null, new ParseErrorList(), parseSettings());
 // this test case should fail with an IllegalArgumentException
 }

}