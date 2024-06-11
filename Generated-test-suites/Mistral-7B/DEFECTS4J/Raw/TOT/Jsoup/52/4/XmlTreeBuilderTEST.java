package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Tokenizer tokenizer;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 tokenizer = new Tokenizer();
 }

 @Test
 public void testStartTagParsing() {
 String xmlInput = "<root><element name='test' attr='value'/></root>";
 Document doc = Jsoup.parse(xmlInput, Parser.xmlParser());
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlInput, null, new ParseErrorList());

 Element root = (Element) nodes.get(0);
 Element element = (Element) nodes.get(1);

 assertEquals("root", root.nodeName());
 assertEquals("element", element.nodeName());
 assertEquals("test", element.attribute(0).getValue());
 assertEquals("value", element.attribute(1).getValue());
 }

 @Test
 public void testSelfClosingTagParsing() {
 String xmlInput = "<img src='image.jpg'/>";
 Document doc = Jsoup.parse(xmlInput, Parser.xmlParser());
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlInput, null, new ParseErrorList());

 Element img = (Element) nodes.get(0);
 assertEquals("img", img.nodeName());
 assertNotNull(img.attribute("src"));
 }

 @Test
 public void testCommentParsing() {
 String xmlInput = "<!-- this is a comment --><root><element name='test'/></root>";
 Document doc = Jsoup.parse(xmlInput, Parser.xmlParser());
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlInput, null, new ParseErrorList());

 Element root = (Element) nodes.get(0);
 Element element = (Element) nodes.get(1);
 assertNotNull(nodes.get(2));
 }

 @Test
 public void testDoctypeParsing() {
 String xmlInput = "<!DOCTYPE html SYSTEM 'about:legacy-compat'>";
 Document doc = Jsoup.parse(xmlInput, Parser.xmlParser());
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlInput, null, new ParseErrorList());

 DocumentType doctype = (DocumentType) nodes.get(0);
 assertEquals("html", doctype.nodeName());
 assertEquals("about:legacy-compat", doctype.getSystemIdentifier());
 }

 @Test(expected = RuntimeException.class)
 public void testUnexpectedTokenType() {
 String xmlInput = "<![CDATA[hello world]]>"; // this is not a valid XML tag, but should not cause a failure as per the documentation
 Document doc = Jsoup.parse(xmlInput, Parser.xmlParser());
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlInput, null, new ParseErrorList());
 }
}