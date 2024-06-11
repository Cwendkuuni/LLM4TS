package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.helper.Validate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 doc = xmlTreeBuilder.parseFragment("<root><child><subchild/></child></root>", "", new ParseErrorList());
 }

 @Test
 public void testProcessStartTag() {
 Element root = doc.select("root").first();
 Element child = root.select("child").first();
 Element subchild = child.select("subchild").first();

 assertEquals("root", root.nodeName());
 assertEquals("child", child.nodeName());
 assertEquals("subchild", subchild.nodeName());
 }

 @Test
 public void testProcessEndTag() {
 Element root = doc.select("root").first();
 Element child = root.select("child").first();
 child.remove();

 assertTrue(doc.childNodes().isEmpty());
 }

 @Test
 public void testProcessComment() {
 List<Node> comments = doc.childNodes();
 Node comment = comments.get(0);

 assertTrue(comment instanceof Comment);
 assertEquals("", comment.text()); // empty comment
 }

 @Test
 public void testProcessCharacter() {
 List<Node> textNodes = doc.childNodes();
 Node textNode = textNodes.get(1);

 assertTrue(textNode instanceof TextNode);
 assertEquals("", textNode.text()); // empty text node
 }

 @Test
 public void testProcessDoctype() {
 List<Node> doctypeNodes = doc.childNodes();
 Node doctype = doctypeNodes.get(0);

 assertTrue(doctype instanceof DocumentType);
 assertEquals("root", doctype.nodeName());
 }

 @Test(expected = AssertionError.class)
 public void testProcessInvalidToken() {
 xmlTreeBuilder.parseFragment("invalid", "", new ParseErrorList());
 Validate.fail("Unexpected test case passing!");
 }
}