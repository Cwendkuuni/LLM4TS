package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 @Test
 public void testDefaultSettings() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
 }

 @Test
 public void testParseWithReaderAndBaseUri() {
 String xmlInput = "<xml><tag attr='value'></tag></xml>";
 String baseUri = "http://example.com/";
 Document document = xmlTreeBuilder.parse(new StringReader(xmlInput), baseUri);
 assertNotNull(document);
 assertEquals(1, document.childNodes().size());
 }

 @Test
 public void testParseWithStringInputAndBaseUri() {
 String xmlInput = "<xml><tag attr='value'></tag></xml>";
 String baseUri = "http://example.com/";
 Document document = xmlTreeBuilder.parse(xmlInput, baseUri);
 assertNotNull(document);
 assertEquals(1, document.childNodes().size());
 }

 @Test
 public void testInitialiseParse() {
 String xmlInput = "<xml><tag attr='value'></tag></xml>";
 String baseUri = "http://example.com/";
 ParseErrorList errors = new ParseErrorList();
 ParseSettings settings = ParseSettings.preserveCase;
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.initialiseParse(new StringReader(xmlInput), baseUri, errors, settings);
 assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
 assertNotNull(xmlTreeBuilder.doc);
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 String xmlInput = "<xml>";
 TreeBuilder tb = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return false;
 }
 };
 tb.initialiseParse(new StringReader(xmlInput), null, null, null);
 }

 @Test
 public void testProcessStartTag() {
 String xmlInput = "<tag attr='value'/>";
 Token startTag = tokenize(xmlInput);
 Element result = xmlTreeBuilder.insert(startTag.asStartTag());
 assertEquals("tag", result.tagName());
 }

 @Test
 public void testProcessEndTag() {
 String xmlInput = "<tag><subtag></subtag></tag>";
 Token endTag = tokenize(xmlInput).get(2);
 xmlTreeBuilder.popStackToClose(endTag.asEndTag());
 assertEquals(0, xmlTreeBuilder.stack.size());
 }

 @Test
 public void testProcessComment() {
 String xmlInput = "<!-- comment -->";
 Token commentToken = tokenize(xmlInput).get(0);
 xmlTreeBuilder.insert(commentToken.asComment());
 assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
 }

 @Test
 public void testProcessCharacter() {
 String xmlInput = "<xml>&amp;character;</xml>";
 Token characterToken = tokenize(xmlInput).get(0);
 xmlTreeBuilder.insert(characterToken.asCharacter());
 assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
 }

 @Test
 public void testProcessDoctype() {
 String xmlInput = "<!DOCTYPE xml SYSTEM \"system_id\">";
 Token doctypeToken = tokenize(xmlInput).get(0);
 xmlTreeBuilder.insert(doctypeToken.asDoctype());
 assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
 }

 @Test
 public void testParseFragment() {
 String xmlInput = "<xml><tag attr='value'></tag></xml>";
 String baseUri = "http://example.com/";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlInput, baseUri, new ParseErrorList(), ParseSettings.preserveCase);
 assertEquals(1, nodes.size());
 }

 private List<Token> tokenize(String input) {
 return Jsoup.parse(input, null, Parse.xmlParser()).body().children();
 }

}