package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParse() {
 String xmlString = "<xml version='1.0'><root><child></child></root></xml>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://test.com");
 Element root = doc.rootElement();
 Element child = root.child(0);

 // Test root element
 Validate.assertEquals("root", root.tagName());
 Validate.assertEquals("http://test.com", root.baseUri());

 // Test child element
 Validate.assertEquals("child", child.tagName());
 Validate.assertNull(child.baseUri());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullBaseUri() {
 xmlTreeBuilder.parse(new StringReader("<xml version='1.0'><root></root></xml>"), null);
 }

 @Test
 public void testProcessStartTag() {
 String xmlString = "<xml version='1.0'><root><child></child></root></xml>";
 Token startTag = new Token(Token.Type.StartTag, 1, "<root>", null, null, null);
 xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilder.runParser();
 Element root = xmlTreeBuilder.doc.rootElement();
 Element child = root.child(0);

 // Test root element
 Validate.assertEquals("root", root.tagName());
 Validate.assertEquals("http://test.com", root.baseUri());

 // Test child element
 Validate.assertEquals("child", child.tagName());
 Validate.assertNull(child.baseUri());

 // Test process start tag
 Element element = xmlTreeBuilder.insert(startTag);
 Validate.assertEquals("root", element.parent().tagName());
 }

 @Test
 public void testProcessEndTag() {
 String xmlString = "<xml version='1.0'><root><child></child></root></xml>";
 Token startTag = new Token(Token.Type.StartTag, 1, "<root>", null, null, null);
 Token endTag = new Token(Token.Type.EndTag, 1, "</child>", null, null, null);
 xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilder.runParser();
 Element root = xmlTreeBuilder.doc.rootElement();
 Element child = root.child(0);

 // Test start tag
 Validate.assertEquals("root", root.tagName());
 Validate.assertEquals("http://test.com", root.baseUri());

 // Test child element
 Validate.assertEquals("child", child.tagName());
 Validate.assertNull(child.baseUri());

 // Test process end tag
 xmlTreeBuilder.popStackToClose(endTag);
 Validate.assertEquals(0, root.childNodeSize());
 }

 @Test
 public void testProcessComment() {
 String xmlString = "<xml version='1.0'><!--comment--></xml>";
 xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilder.runParser();
 List<Node> commentNodes = xmlTreeBuilder.doc.childNodes();
 Validate.assertEquals(1, commentNodes.size());
 Validate.assertTrue(commentNodes.get(0) instanceof Comment);
 }

 @Test
 public void testProcessCharacter() {
 String xmlString = "<xml version='1.0'>&#160;</xml>";
 xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilder.runParser();
 List<Node> textNodes = xmlTreebuilder.doc.childNodes();
 Validate.assertEquals(1, textNodes.size());
 Validate.assertTrue(textNodes.get(0) instanceof TextNode);
 }

 @Test
 public void testProcessDoctype() {
 String xmlString = "<!DOCTYPE xml PUBLIC \"-//W3C//DTD XML 1.0//EN\" \"http://www.w3.org/TR/xml11/DTD/xml11.dtd\">";
 xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilder.runParser();
 DocumentType doctype = (DocumentType) xmlTreeBuilder.doc.childNodes().get(0);
 Validate.assertEquals("xml", doctype.nodeName());
 Validate.assertEquals("http://www.w3.org/TR/xml11/DTD/xml11.dtd", doctype.systemIdentifier());
 Validate.assertNull(doctype.publicIdentifier());
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<root><child></child></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Element root = (Element) nodes.get(0);
 Element child = (Element) nodes.get(1);

 // Test root element
 Validate.assertEquals("root", root.tagName());
 Validate.assertEquals("http://test.com", root.baseUri());

 // Test child element
 Validate.assertEquals("child", child.tagName());
 Validate.assertNull(child.baseUri());
 }

 @Test
 public void testInsertNode() {
 String xmlString = "<xml version='1.0'><root></root></xml>";
 xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilder.runParser();
 Element root = xmlTreeBuilder.doc.rootElement();

 Element newElement = new Element("newTag");
 xmlTreeBuilder.insertNode(newElement);
 root.appendChild(newElement);

 Validate.assertEquals(1, root.childNodeSize());
 Validate.assertEquals("newTag", root.child(0).tagName());
 }
}