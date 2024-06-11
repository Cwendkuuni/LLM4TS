package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document xmlDocument;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParse() throws IOException {
 String xmlInput = "<root><element attr=\"value\"/><anotherElement /></root>";
 xmlDocument = xmlTreeBuilder.parse(xmlInput, null);
 assertNotNull(xmlDocument);
 assertNotNull(xmlDocument.rootElement());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullInput() {
 xmlTreeBuilder.parse(null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullBaseUri() {
 xmlTreeBuilder.parse(xmlInput, null);
 }

 @Test
 public void testParseFragment() throws IOException {
 String xmlFragment = "<element attr=\"value\"/><anotherElement />";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertNotNull(nodes);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithNullInput() {
 xmlTreeBuilder.parseFragment(null, null, ParseErrorList.noTracking(), ParseSettings.preserveCase);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithNullBaseUri() {
 xmlTreeBuilder.parseFragment(xmlInput, null, ParseErrorList.noTracking(), ParseSettings.preserveCase);
 }

 @Test
 public void testProcessingStartTag() throws IOException {
 String xmlInput = "<root><element attr=\"value\"/></root>";
 xmlDocument = xmlTreeBuilder.parse(xmlInput, null);
 Element root = xmlDocument.rootElement();
 Element element = root.element("element");
 assertNotNull(element);
 }

 @Test
 public void testProcessingEndTag() throws IOException {
 String xmlInput = "<root><element><anotherElement /></element></root>";
 xmlDocument = xmlTreeBuilder.parse(xmlInput, null);
 Element element = xmlDocument.rootElement().element("element");
 assertNotNull(element);
 assertNotNull(element.child(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessingUnknownStartTag() throws IOException {
 String xmlInput = "<root><unknownTag /></root>";
 xmlDocument = xmlTreeBuilder.parse(xmlInput, null);
 }

 @Test
 public void testProcessingSelfClosingTag() throws IOException {
 String xmlInput = "<root><selfClosingTag /></root>";
 xmlDocument = xmlTreeBuilder.parse(xmlInput, null);
 Element selfClosingTag = xmlDocument.rootElement().element("selfClosingTag");
 assertNotNull(selfClosingTag);
 assertFalse(selfClosingTag.childNodes().size() > 0);
 }

 @Test
 public void testProcessingComment() throws IOException {
 String xmlInput = "<root><!-- this is a comment --></root>";
 xmlDocument = xmlTreeBuilder.parse(xmlInput, null);
 Comment comment = xmlDocument.rootElement().child(0);
 assertNotNull(comment);
 assertEquals("this is a comment", comment.text());
 }

 @Test
 public void testProcessingCharacter() throws IOException {
 String xmlInput = "<root>&#x26;<root>";
 xmlDocument = xmlTreeBuilder.parse(xmlInput, null);
 TextNode character = xmlDocument.rootElement().child(0);
 assertNotNull(character);
 assertEquals("&amp;", character.text());
 }

 @Test
 public void testProcessingDoctype() throws IOException {
 String xmlInput = "<!DOCTYPE root SYSTEM \"root.dtd\">";
 xmlDocument = xmlTreeBuilder.parse(xmlInput, null);
 DocumentType doctype = xmlDocument.child(0);
 assertNotNull(doctype);
 assertEquals("root", doctype.nodeName());
 assertEquals("root.dtd", doctype.systemIdentifier());
 }

 @Test
 public void testPopStackToClose() {
 xmlDocument = xmlTreeBuilder.parse("<root><element><anotherElement /></element></root>", null);
 Element element = xmlDocument.rootElement().element("element");
 element.element("anotherElement").remove();
 }
}