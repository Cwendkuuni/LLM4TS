package org.jsoup.parser;

import org.junit.Assert; import org.junit.Test; import static org.junit.Assert.assertNotNull; import static org.junit.Assert.assertNull;

import org.jsoup.Jsoup; import org.jsoup.helper.Validate; import org.jsoup.nodes.*; import org.jsoup.parser.XmlTreeBuilder; import org.jsoup.parser.ParseSettings; import org.jsoup.parser.ParseErrorList;

import java.io.StringReader; import java.util.List;

public class XmlTreeBuilderTest {

 @Test
 public void testDefaultSettings() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 String xmlString = "<root><element attr=\"value\"></element></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");

 Assert.assertNotNull(doc);
 Assert.assertEquals("root", doc.tagName());
 Element root = doc.child(0);
 Assert.assertEquals("element", root.tagName());
 Assert.assertEquals("value", root.attribute("attr"));
 }

 @Test
 public void testParseFragment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 String xmlFragment = "<element attr=\"value\"/>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);

 Assert.assertNotNull(nodes);
 Assert.assertEquals(1, nodes.size());
 Element node = (Element) nodes.get(0);
 Assert.assertEquals("element", node.tagName());
 Assert.assertEquals("value", node.attribute("attr"));
 }

 @Test
 public void testProcessToken_StartTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<root><element attr=\"value\"></element></root>";
 StringReader input = new StringReader(xmlString);
 ParseSettings settings = ParseSettings.preserveCase;
 ParseErrorList errors = ParseErrorList.noTracking();

 try {
 xmlTreeBuilder.initialiseParse(input, "http://example.com", errors, settings);
 xmlTreeBuilder.runParser();
 } catch (Exception e) {
 throw new RuntimeException(e);
 }

 Element root = (Element) xmlTreeBuilder.doc.child(0);
 Element element = root.child(0);

 Token.StartTag startTag = xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1).asStartTag();
 Element expectedElement = xmlTreeBuilder.insert(startTag);

 Assert.assertEquals(expectedElement.tagName(), element.tagName());
 Assert.assertEquals(expectedElement.baseUri(), element.baseUri());
 Assert.assertEquals(expectedElement.attributes().size(), element.attributes().size());
 for (Attribute attr : expectedElement.attributes()) {
 Assert.assertEquals(attr.key(), element.attribute(attr.key()));
 Assert.assertEquals(attr.value(), element.attribute(attr.key()));
 }
 }

 @Test
 public void testProcessToken_EndTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<root><element><subElement/></element></root>";
 StringReader input = new StringReader(xmlString);
 ParseSettings settings = ParseSettings.preserveCase;
 ParseErrorList errors = ParseErrorList.noTracking();

 try {
 xmlTreeBuilder.initialiseParse(input, "http://example.com", errors, settings);
 xmlTreeBuilder.runParser();
 } catch (Exception e) {
 throw new RuntimeException(e);
 }

 Element root = (Element) xmlTreeBuilder.doc.child(0);
 Element element = root.child(0);
 Element subElement = (Element) element.child(0);

 Token.EndTag endTag = (Token.EndTag) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 2);
 xmlTreeBuilder.popStackToClose(endTag);

 Assert.assertNull(subElement.parent());
 }

 @Test
 public void testProcessToken_Comment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<!-- This is a comment -->";
 StringReader input = new StringReader(xmlString);
 ParseSettings settings = ParseSettings.preserveCase;
 ParseErrorList errors = ParseErrorList.noTracking();

 try {
 xmlTreeBuilder.initialiseParse(input, "http://example.com", errors, settings);
 xmlTreeBuilder.runParser();
 } catch (Exception e) {
 throw new RuntimeException(e);
 }

 Comment comment = (Comment) xmlTreeBuilder.doc.child(0);

 Assert.assertEquals("This is a comment", comment.data());
 }

 @Test
 public void testProcessToken_Character() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<root>Test<!DOCTYPE html>\n</root>";
 StringReader input = new StringReader(xmlString);
 ParseSettings settings = ParseSettings.preserveCase;
 ParseErrorList errors = ParseErrorList.noTracking();

 try {
 xmlTreeBuilder.initialiseParse(input, "http://example.com", errors, settings);
 xmlTreeBuilder.runParser();
 } catch (Exception e) {
 throw new RuntimeException(e);
 }

 TextNode textNode = (TextNode) xmlTreeBuilder.doc.child(0);

 Assert.assertEquals("Test", textNode.text());
 }

 @Test
 public void testProcessToken_Doctype() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";
 StringReader input = new StringReader(xmlString);
 ParseSettings settings = ParseSettings.preserveCase;
 ParseErrorList errors = ParseErrorList.noTracking();

 try {
 xmlTreeBuilder.initialiseParse(input, "http://example.com", errors, settings);
 xmlTreeBuilder.runParser();
 } catch (Exception e) {
 throw new RuntimeException(e);
 }

 DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.doc.child(0);

 Assert.assertEquals("html", doctypeNode.tagName());
 Assert.assertNull(doctypeNode.publicIdentifier());
 Assert.assertNull(doctypeNode.systemIdentifier());
 }

 @Test
 public void testProcessToken_DoctypeWithPublicId() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"html.dtd\">";
 StringReader input = new StringReader(xmlString);
 ParseSettings settings = ParseSettings.preserveCase;
 ParseErrorList errors = ParseErrorList.noTracking();

 try {
 xmlTreeBuilder.initialiseParse(input, "http://example.com", errors, settings);
 xmlTreeBuilder.runParser();
 } catch (Exception e) {
 throw new RuntimeException(e);
 }

 DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.doc.child(0);

 Assert.assertEquals("html", doctypeNode.tagName());
 Assert.assertEquals("-//W3C//DTD XHTML 1.0 Strict//EN", doctypeNode.publicIdentifier());
 Assert.assertEquals("html.dtd", doctypeNode.systemIdentifier());
 }

 @Test
 public void testProcessToken_DoctypeWithSystemId() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<!DOCTYPE html SYSTEM \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";
 StringReader input = new StringReader(xmlString);
 ParseSettings settings = ParseSettings.preserveCase;
 ParseErrorList errors = ParseErrorList.noTracking();

 try {
 xmlTreeBuilder.initialiseParse(input, "http://example.com", errors, settings);
 xmlTreeBuilder.runParser();
 } catch (Exception e) {
 throw new RuntimeException(e);
 }

 DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.doc.child(0);

 Assert.assertEquals("html", doctypeNode.tagName());
 Assert.assertNull(doctypeNode.publicIdentifier());
 Assert.assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", doctypeNode.systemIdentifier());
 }

 @Test(expected = Validate.MalformedXmlException.class)
 public void testProcessToken_MalformedXml() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String xmlString = "<!DOCTYPE html <html>"; // missing '>' in DOCTYPE declaration
 StringReader input = new StringReader(xmlString);
 ParseSettings settings = ParseSettings.preserveCase;
 ParseErrorList errors = ParseErrorList.noTracking();

 try {
 xmlTreeBuilder.initialiseParse(input, "http://example.com", errors, settings);
 xmlTreeBuilder.runParser();
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }
}