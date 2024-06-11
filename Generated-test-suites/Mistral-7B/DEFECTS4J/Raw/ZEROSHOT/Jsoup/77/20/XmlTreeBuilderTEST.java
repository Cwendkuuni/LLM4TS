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
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParse() {
 String xmlString = "<root><element attr='value'></element></root>";
 Document document = xmlTreeBuilder.parse(new StringReader(xmlString), "");
 Element rootElement = document.getRootElement();
 assertEquals("root", rootElement.tagName());
 Element element = rootElement.child(0);
 assertEquals("element", element.tagName());
 assertNull(element.classObj()); // no specific tag class for XML parsed elements
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<element attr='value'/>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(1, nodes.size());
 Element fragmentElement = (Element) nodes.get(0);
 assertEquals("element", fragmentElement.tagName());
 }

 @Test
 public void testProcessStartTag() {
 String xmlString = "<root><element attr='value'/></root>";
 Document document = Jsoup.parse(xmlString, "", Parser.xmlParser());
 Element rootElement = document.getRootElement();
 Element element = rootElement.child(0);
 Token startTag = document.body().tokenize(0, document.body().children().size()).get(0);
 assertEquals("element", startTag.name());
 }

 @Test(expected = AssertionError.class)
 public void testProcessUnexpectedToken() {
 String xmlString = "<root><element attr='value'><html><head></head></html></element></root>";
 Document document = Jsoup.parse(xmlString, "", Parser.xmlParser());
 // this token should not be present in an XML document
 Token unexpectedToken = document.body().tokenize(0, document.body().children().size()).get(1);
 assertEquals(Token.Type.StartTag, unexpectedToken.type());
 Validate.fail("Unexpected token type: " + unexpectedToken.type());
 }
}