package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;

/**
 * JUnit 4 test file for XmlTreeBuilder class
 */
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testInitialiseParse() {
 String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n<root>\n<element attr1=\"value1\"/>\n<element attr2=\"value2\"/>\n</root>\n";
 String baseUri = "http://test.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);
 assertEquals("http://test.com", xmlTreeBuilder.doc().baseUri());
 assertTrue(xmlTreeBuilder.doc().childNodes().size() > 0);
 }

 @Test
 public void testProcessStartTag() {
 String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n<root>\n<element attr1=\"value1\"/>\n<element attr2=\"value2\">\n<subElement attr3=\"value3\"/>\n</element>\n</root>\n";
 String baseUri = "http://test.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);

 Token startTag1 = new Token.StartTag("root", null);
 Token startTag2 = new Token.StartTag("element", new Attribute("attr1", "value1"));
 Token startTag3 = new Token.StartTag("subElement", new Attribute("attr3", "value3"));
 xmlTreeBuilder.process(startTag1);
 xmlTreeBuilder.process(startTag2);
 xmlTreeBuilder.process(startTag3);

 Element root = xmlTreeBuilder.doc();
 Element element = (Element) root.child(0);
 Element subElement = (Element) element.child(0);

 assertEquals("root", root.nodeName());
 assertEquals("element", element.nodeName());
 assertEquals("subElement", subElement.nodeName());
 assertEquals("value1", element.attr("attr1"));
 assertEquals("value3", subElement.attr("attr3"));
 }

 @Test
 public void testProcessEndTag() {
 String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n<root>\n<element attr1=\"value1\">\n<subElement attr2=\"value2\"/>\n</element>\n</root>\n";
 String baseUri = "http://test.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeTreeBuilder.initialiseParse(input, baseUri, errors);

 Token startTag1 = new Token.StartTag("root", null);
 Token startTag2 = new Token.StartTag("element", new Attribute("attr1", "value1"));
 Token endTag1 = new Token.EndTag("element");
 xmlTreeBuilder.process(startTag1);
 xmlTreeBuilder.process(startTag2);
 xmlTreeBuilder.process(endTag1);

 Element root = xmlTreeBuilder.doc();
 Element element = (Element) root.child(0);

 assertEquals(0, element.childNodes().size());
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n<root>\n<unknownToken>\n</root>\n";
 String baseUri = "http://test.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);
 }
}