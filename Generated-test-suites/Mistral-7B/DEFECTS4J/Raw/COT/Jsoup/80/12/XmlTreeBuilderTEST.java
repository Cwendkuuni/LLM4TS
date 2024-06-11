package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.ParseErrorList;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 @Test
 public void testDefaultSettings() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 Assert.assertEquals(ParseSettings.preserveCase, settings);
 }

 @Test
 public void testParseWithReaderAndBaseUri() {
 String xmlString = "<xml><root><child>Hello World</child></root></xml>";
 String baseUri = "http://example.com";
 Reader input = new StringReader(xmlString);
 Document document = xmlTreeBuilder.parse(input, baseUri);
 Assert.assertNotNull(document);
 }

 @Test
 public void testParseWithStringAndBaseUri() {
 String xmlString = "<xml><root><child>Hello World</child></root></xml>";
 String baseUri = "http://example.com";
 Document document = xmlTreeBuilder.parse(xmlString, baseUri);
 Assert.assertNotNull(document);
 }

 @Test
 public void testInitialiseParse() {
 String xmlString = "<xml><root><child>Hello World</child></root></xml>";
 String baseUri = "http://example.com";
 ParseErrorList errors = ParseErrorList.noTracking();
 ParseSettings settings = ParseSettings.preserveCase;
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 xmlTreeBuilder.initialiseParse(new StringReader(xmlString), baseUri, errors, settings);

 Assert.assertEquals(xmlString, doc.body().html()); // checks that doc is initialized correctly
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessNullToken() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Token nullToken = null;
 xmlTreeBuilder.process(nullToken);
 }

 @Test
 public void testProcessStartTag() {
 String xmlString = "<xml><root><child>Hello World</child></root></xml>";
 String baseUri = "http://example.com";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 XMLParser parser = Jsoup.parse(xmlString, baseUri, Parser.xmlParser());
 Token startTagToken = parser.nextToken();

 xmlTreeBuilder.process(startTagToken);

 Element root = stack.get(0);
 Assert.assertEquals("root", root.nodeName());
 }

 @Test
 public void testProcessEndTag() {
 String xmlString = "<xml><root><child><grandchild/></child></root></xml>";
 String baseUri = "http://example.com";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 XMLParser parser = Jsoup.parse(xmlString, baseUri, Parser.xmlParser());
 Token startTagToken = parser.nextToken();
 Token endTagToken = parser.nextToken();

 xmlTreeBuilder.process(startTagToken);
 xmlTreeBuilder.process(startTagToken); // should not throw an error for self closing tag
 xmlTreeBuilder.process(endTagToken);

 List<Node> childNodes = stack.get(stack.size() -1).childNodes();
 Assert.assertEquals(1, childNodes.size());
 }

 @Test(expected = RuntimeException.class)
 public void testProcessCommentBogus() {
 String xmlString = "<!DOCTYPE xml SYSTEM \"test.dtd\">";
 String baseUri = "http://example.com";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 XMLParser parser = Jsoup.parse(xmlString, baseUri, Parser.xmlParser());
 Token commentToken = parser.nextToken();

 xmlTreeBuilder.process(commentToken);
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<root><child>Hello World</child></root>";
 String baseUri = "http://example.com";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Element root = (Element) nodes.get(0);
 Assert.assertEquals("root", root.nodeName());
 Assert.assertEquals(1, root.childNodes().size());
 }

}