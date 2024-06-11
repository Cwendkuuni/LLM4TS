package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParseEmptyXML() {
 String input = "<xml/>";
 Document doc = xmlTreeBuilder.parse(input, "baseUri");
 Element root = doc.rootElement();
 Assert.assertEquals("xml", root.tagName());
 Assert.assertTrue(doc.childNodes().isEmpty());
 }

 @Test
 public void testParseXMLWithElement() {
 String input = "<root><child>Hello World</child></root>";
 Document doc = xmlTreeBuilder.parse(input, "baseUri");
 Element root = doc.rootElement();
 Element child = root.child(0);
 Assert.assertEquals("root", root.tagName());
 Assert.assertEquals("Hello World", child.text());
 }

 @Test
 public void testParseXMLWithComment() {
 String input = "<root><!-- This is a comment --><child>Hello World</child></root>";
 Document doc = xmlTreeBuilder.parse(input, "baseUri");
 Element root = doc.rootElement();
 Element child = root.child(0);
 Comment comment = root.child(1);
 Assert.assertEquals("root", root.tagName());
 Assert.assertEquals("Hello World", child.text());
 Assert.assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseXMLWithDOCTYPE() {
 String input = "<!DOCTYPE root [<!ATTLIST root id ID #REQUIRED>]<root/>";
 Document doc = xmlTreeBuilder.parse(input, "baseUri");
 DocumentType doctype = doc.doctype();
 Assert.assertEquals("root", doctype.name());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseXMLWithInvalidToken() {
 String input = "<root>Hello<World";
 TryCatch.assertThrows(IllegalArgumentException.class, () -> xmlTreeBuilder.parse(input, "baseUri"));
 }

 @Test(expected = NullPointerException.class)
 public void testParseXMLWithNullBaseUri() {
 xmlTreeBuilder.parse(null, "baseUri");
 TryCatch.assertThrows(NullPointerException.class);
 }

 @Test
 public void testParseXMLWithFragment() {
 String input = "<root><child>Hello World</child></root>";
 List<Node> fragments = xmlTreeBuilder.parseFragment(input, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Element root = (Element) fragments.get(0);
 Element child = (Element) fragments.get(1);
 Assert.assertEquals("root", root.tagName());
 Assert.assertEquals("Hello World", child.text());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseXMLWithEmptyFragment() {
 List<Node> fragments = xmlTreeBuilder.parseFragment("", "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 TryCatch.assertThrows(IllegalArgumentException.class);
 }
}