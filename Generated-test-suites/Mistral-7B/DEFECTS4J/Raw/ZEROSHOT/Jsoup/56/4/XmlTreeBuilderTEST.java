package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

/**
 * Test class for XmlTreeBuilder
 */
public class XmlTreeBuilderTest {

 @Test
 public void testDefaultSettings() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 assertSame(ParseSettings.preserveCase, builder.defaultSettings());
 }

 @Test
 public void testParse() {
 String xmlInput = "<root><child element=\"test\"/><anotherChild/></root>";
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Document doc = builder.parse(xmlInput, "");
 Element root = doc.rootElement();
 assertEquals("root", root.tagName());
 List<Node> children = root.children();
 assertEquals(2, children.size());
 assertEquals("child", children.get(0).tagName());
 assertTrue(children.get(0).hasAttr("element"));
 assertEquals("test", children.get(0).attr("element"));
 assertEquals("anotherChild", children.get(1).tagName());
 }

 @Test
 public void testProcessStartTag() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Token token = new Token.StartTag("root");
 Element root = builder.insert(token);
 assertEquals("root", root.tagName());
 }

 @Test
 public void testProcessEndTag() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Element root = new Element("root", "");
 Element child = new Element("child", "");
 builder.stack.add(root);
 builder.stack.add(child);
 Token token = new Token.EndTag("child");
 builder.popStackToClose(token);
 assertEquals(1, builder.stack.size());
 assertEquals("root", builder.stack.get(0).tagName());
 }

 @Test
 public void testProcessComment() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Token token = new Token.Comment("<!-- comment -->");
 builder.insert(token);
 Comment comment = (Comment) builder.doc.childNodes().get(0);
 assertEquals("comment", comment.text());
 }

 @Test
 public void testProcessCharacter() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Token token = new Token.Character('a');
 builder.insert(token);
 TextNode textNode = (TextNode) builder.doc.childNodes().get(1);
 assertEquals("a", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 Token token = new Token.Doctype("html");
 builder.insert(token);
 DocumentType docTypeNode = (DocumentType) builder.doc.childNodes().get(0);
 assertEquals("html", docTypeNode.tagName());
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<root><child element=\"test\"/><anotherChild/></root>";
 XmlTreeBuilder builder = new XmlTreeBuilder();
 List<Node> nodes = builder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Document doc = new Document();
 doc.setRootElement(nodes.get(0));
 Element root = doc.rootElement();
 assertEquals("root", root.tagName());
 List<Node> children = root.children();
 assertEquals(2, children.size());
 assertEquals("child", children.get(0).tagName());
 assertTrue(children.get(0).hasAttr("element"));
 assertEquals("test", children.get(0).attr("element"));
 assertEquals("anotherChild", children.get(1).tagName());
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 XmlTreeBuilder builder = new XmlTreeBuilder();
 builder.process(new Token(12345));
 }
}