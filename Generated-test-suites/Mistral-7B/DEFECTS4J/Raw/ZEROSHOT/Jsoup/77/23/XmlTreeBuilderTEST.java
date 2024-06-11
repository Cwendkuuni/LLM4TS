package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@RunWith(JUnit4.class)
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParse() {
 String xmlString = "<root><element attr=\"value\"><subElement/></element></root>";
 document = xmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com/");
 assertNotNull(document);
 assertNotNull(document.body());
 assertEquals("root", document.tagName());

 Element root = document.rootElement();
 Element element = root.child(0);
 assertEquals("element", element.tagName());
 assertEquals("value", element.attr("attr"));
 assertNull(element.child(0));

 Element subElement = element.child(0);
 assertEquals("subElement", subElement.tagName());
 assertNull(subElement.attr("attr"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullInput() {
 xmlTreeBuilder = new XmlTreeBuilder();
 try {
 xmlTreeBuilder.parse(null, "http://example.com/");
 } catch (IllegalArgumentException e) {
 assertEquals("Input can't be null", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithEmptyInput() {
 xmlTreeBuilder = new XmlTreeBuilder();
 try {
 xmlTreeBuilder.parse(new StringReader(""), "http://example.com/");
 } catch (IllegalArgumentException e) {
 assertEquals("Input can't be empty", e.getMessage());
 }
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<subElement/>";
 List<Node> fragmentNodes = xmlTreeBuilder.parseFragment(xmlFragment, "http://example.com/", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Element fragmentElement = (Element) fragmentNodes.get(0);
 assertEquals("subElement", fragmentElement.tagName());
 assertNull(fragmentElement.attr("attr"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithNullInput() {
 xmlTreeBuilder = new XmlTreeBuilder();
 String xmlFragment = null;
 try {
 xmlTreeBuilder.parseFragment(xmlFragment, "http://example.com/", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 } catch (IllegalArgumentException e) {
 assertEquals("Input can't be null", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithEmptyInput() {
 xmlTreeBuilder = new XmlTreeBuilder();
 String xmlFragment = "";
 try {
 xmlTreeBuilder.parseFragment(xmlFragment, "http://example.com/", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 } catch (IllegalArgumentException e) {
 assertEquals("Input can't be empty", e.getMessage());
 }
 }
}