package org.jsoup.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.xml.Attribute;
import org.jsoup.nodes.xml.XmlDeclaration;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;

import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

 private Document doc;
 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @After
 public void tearDown() {
 doc = null;
 }

 @Test
 public void testEmptyXml() {
 doc = xmlTreeBuilder.parseFragment("", "baseUri", new ParseErrorList());
 assertNotNull(doc);
 assertEquals(0, doc.childNodes().size());
 }

 @Test
 public void testXmlDeclaration() {
 doc = xmlTreeBuilder.parseFragment("<?xml version=\"1.0\"?>\n<root></root>", "baseUri", new ParseErrorList());
 Element root = doc.childNodes().get(0);
 assertNotNull(root.childNodes().get(0));
 XmlDeclaration xmlDeclaration = (XmlDeclaration) root.childNodes().get(0);
 assertEquals("1.0", xmlDeclaration.version());
 }

 @Test
 public void testComment() {
 doc = xmlTreeBuilder.parseFragment("<!-- This is a comment -->\n<root><element/></root>", "baseUri", new ParseErrorList());
 List<Node> nodes = doc.childNodes();
 assertEquals(2, nodes.size());
 Node commentNode = nodes.get(0);
 assertEquals(Node.COMMENT_NODE, commentNode.nodeType());
 Comment comment = (Comment) commentNode;
 assertEquals(" This is a comment ", comment.getData());
 }

 @Test
 public void testSelfClosingTag() {
 doc = xmlTreeBuilder.parseFragment("<root><tag/></root>", "baseUri", new ParseErrorList());
 List<Node> nodes = doc.childNodes();
 assertEquals(2, nodes.size());
 Element root = (Element) nodes.get(0);
 Element tag = (Element) nodes.get(1);
 assertEquals("tag", tag.nodeName());
 assertEquals(0, tag.childNodes().size());
 assertEquals(1, tag.attributeCount());
 Attribute attribute = tag.attribute(0);
 assertEquals("", attribute.getName());
 assertEquals("", attribute.getValue());
 }

 @Test
 public void testTagWithChildren() {
 doc = xmlTreeBuilder.parseFragment("<root><elem1><elem2></elem2></elem1></root>", "baseUri", new ParseErrorList());
 List<Node> nodes = doc.childNodes();
 assertEquals(1, nodes.size());
 Element root = (Element) nodes.get(0);
 assertEquals("root", root.nodeName());
 List<Node> rootChildren = root.childNodes();
 assertEquals(1, rootChildren.size());
 Element elem1 = (Element) rootChildren.get(0);
 assertEquals("elem1", elem1.nodeName());
 List<Node> elem1Children = elem1.childNodes();
 assertEquals(1, elem1Children.size());
 Element elem2 = (Element) elem1Children.get(0);
 assertEquals("elem2", elem2.nodeName());
 assertEquals(0, elem2.childNodes().size());
 }

 @Test
 public void testNestedTags() {
 doc = xmlTreeBuilder.parseFragment("<root><elem1><elem2><elem3></elem3></elem2></elem1></root>", "baseUri", new ParseErrorList());
 List<Node> nodes = doc.childNodes();
 assertEquals(1, nodes.size());
 Element root = (Element) nodes.get(0);
 List<Node> rootChildren = root.childNodes();
 assertEquals(1, rootChildren.size());
 Element elem1 = (Element) rootChildren.get(0);
 List<Node> elem1Children = elem1.childNodes();
 assertEquals(1, elem1Children.size());
 Element elem2 = (Element) elem1Children.get(0);
 List<Node> elem2Children = elem2.childNodes();
 assertEquals(1, elem2Children.size());
 Element elem3 = (Element) elem2Children.get(0);
 assertEquals("elem3", elem3.nodeName());
 assertEquals(0, elem3.childNodes().size());
 }

 @Test
 public void testDoctype() {
 doc = xmlTreeBuilder.parseFragment("<!DOCTYPE root [ <!ATTLIST root attr CDATA #REQUIRED > ]>\n<root></root>", "baseUri", new ParseErrorList());
 List<Node> nodes = doc.childNodes();
 assertEquals(2, nodes.size());
 DocumentType doctype = (DocumentType) nodes.get(0);
 assertEquals("root", doctype.getName());
 List<Attribute> attributes = doctype.attributes();
 assertEquals(1, attributes.size());
 Attribute attribute = attributes.get(0);
 assertEquals("attr", attribute.getName());
 assertEquals("CDATA", attribute.getType());
 assertEquals("REQUIRED", attribute.getValue());
 Element root = (Element) nodes.get(1);
 assertEquals("root", root.nodeName());
 assertEquals(0, root.childNodes().size());
 }
}