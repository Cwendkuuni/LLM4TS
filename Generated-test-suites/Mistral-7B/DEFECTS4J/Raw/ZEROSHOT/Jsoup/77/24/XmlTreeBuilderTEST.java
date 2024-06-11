package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.StringReader;
import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

@RunWith(JUnit4.class)
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 doc = xmlTreeBuilder.parse(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
 "<root>\n" +
 "<element attr1=\"value1\" attr2=\"value2\"/>\n" +
 "<element2>\n" +
 "<subElement attr3=\"value3\"/>\n" +
 "</element2>\n" +
 "</root>"), "baseUri");
 }

 @Test
 public void testParse() {
 // Testing the parse method
 Document parseDoc = xmlTreeBuilder.parse(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
 "<root>\n" +
 "<element attr1=\"value1\" attr2=\"value2\"/>\n" +
 "<element2>\n" +
 "<subElement attr3=\"value3\"/>\n" +
 "</element2>\n" +
 "</root>"), "baseUri");
 assertEquals(doc, parseDoc);
 }

 @Test
 public void testDefaultSettings() {
 // Testing the defaultSettings method
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.preserveCase, settings);
 }

 @Test
 public void testInitialiseParse() {
 // Testing the initialiseParse method
 XmlTreeBuilder xmlTreeBuilderSpy = new XmlTreeBuilder() {
 @Override
 protected void initialiseParse(Reader input, String baseUri, ParseErrorList errors, ParseSettings settings) {
 super.initialiseParse(input, baseUri, errors, settings);
 System.out.println("Initialise Parse called");
 }
 };
 xmlTreeBuilderSpy.parse(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
 "<root>\n" +
 "<element attr1=\"value1\" attr2=\"value2\"/>\n" +
 "<element2>\n" +
 "<subElement attr3=\"value3\"/>\n" +
 "</element2>\n" +
 "</root>"), "baseUri");
 // Since this method is protected, we cannot directly test its behavior, but we can check if it is being called
 }

 @Test
 public void testProcessTokenStartTag() {
 // Testing the process method with start tag
 xmlTreeBuilder.parse(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
 "<root>\n" +
 "<element attr1=\"value1\" attr2=\"value2\"/>\n" +
 "</root>"), "baseUri");
 Element element = doc.select("element").first();
 assertEquals("root", element.parent().tagName());
 assertEquals("element", element.tagName());
 assertEquals("value1", element.attr("attr1"));
 assertEquals("value2", element.attr("attr2"));
 }

 @Test
 public void testProcessTokenEndTag() {
 // Testing the process method with end tag
 Element root = doc.select("root").first();
 root.child(1).remove();
 assertEquals(1, doc.outputSettings().omitXmlDeclaration());
 }

 @Test
 public void testProcessTokenCharacter() {
 // Testing the process method with character
 Element element = doc.select("element").first();
 assertEquals("value1", element.child(0).text());
 }

 @Test
 public void testProcessTokenComment() {
 // Testing the process method with comment
 Comment comment = doc.select("comment").first();
 assertEquals("Comment text", comment.text());
 }

 @Test
 public void testProcessTokenDoctype() {
 // Testing the process method with doctype
 DocumentType documentType = doc.doctype();
 assertEquals("root", documentType.getName());
 assertEquals("XML 1.0", documentType.getPublicIdentifier());
 assertEquals("UTF-8", documentType.getSystemIdentifier());
 }

 @Test
 public void testProcessTokenEof() {
 // Testing the process method with eof
 }

 @Test
 public void testInsertNode() {
 // Testing the insertNode method
 Element newElement = new Element("newElement", "baseUri");
 xmlTreeBuilder.insertNode(newElement);
 Element root = doc.select("root").first();
 assertEquals(1, root.childNodes().size());
 assertEquals("newElement", root.child(0).tagName());
 }

 @Test
 public void testInsert() {
 // Testing the insert method with start tag
 Token.StartTag startTag = new Token.StartTag("element", null, null, null, null, false);
 Element element = xmlTreeBuilder.insert(startTag);
 assertEquals("element", element.tagName());
 assertEquals("baseUri", element.baseUri());
 assertEquals("value1", element.attr("attr1"));
 assertEquals("value2", element.attr("attr2"));

 // Testing the insert method with comment
 Token.Comment commentToken = new Token.Comment("Comment text");
 xmlTreeBuilder.insert(commentToken);
 assertEquals(1, doc.childNodes().size());
 assertEquals("comment", doc.child(0).tagName());
 assertEquals("Comment text", doc.child(0).text());

 // Testing the insert method with character
 Token.Character characterToken = new Token.Character('A');
 xmlTreeBuilder.insert(characterToken);
 assertEquals(1, doc.childNodes().size());
 assertEquals("text", doc.child(0).tagName());
 assertEquals("A", doc.child(0).text());

 // Testing the insert method with doctype
 Token.Doctype doctypeToken = new Token.Doctype("root", "", "");
 xmlTreeBuilder.insert(doctypeToken);
 assertEquals(1, doc.childNodes().size());
 assertEquals("document-type-node", doc.child(0).nodeName());
 }

 @Test
 public void testPopStackToClose() {
 // Testing the popStackToClose method
 Element element1 = doc.select("element").first();
 xmlTreeBuilder.popStackToClose(new Token.EndTag("element2"));
 assertEquals(null, element1.parent());
 }

 @Test
 public void testParseFragment() {
 // Testing the parseFragment method
 List<Node> fragmentNodes = xmlTreeBuilder.parseFragment("<root><element attr1='value1' attr2='value2'/></root>", "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(2, fragmentNodes.size());
 assertEquals("root", fragmentNodes.get(0).tagName());
 assertEquals("element", fragmentNodes.get(1).tagName());
 }
}