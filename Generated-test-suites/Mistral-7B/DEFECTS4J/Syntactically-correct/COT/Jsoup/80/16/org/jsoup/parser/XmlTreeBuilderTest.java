import org.junit.Assert;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Tokenizer;

import java.io.StringReader;
import java.util.List;
import java.util.ArrayList;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 @org.junit.Test
 public void testDefaultSettings() {
 Document document = xmlTreeBuilder.defaultSettings().parse(new StringReader("<xml/>"), "baseUri");
 Assert.assertNotNull(document);
 }

 @org.junit.Test
 public void testParseWithReaderAndBaseUri() {
 String xmlString = "<xml><tag attr='value'></tag></xml>";
 Document document = xmlTreeBuilder.parse(new StringReader(xmlString), "baseUri");
 Assert.assertNotNull(document);
 Assert.assertEquals(1, document.numberOfChildren());
 Assert.assertEquals("tag", document.child(0).nodeName());
 }

 @org.junit.Test
 public void testParseWithStringAndBaseUri() {
 String xmlString = "<xml><tag attr='value'></tag></xml>";
 Document document = xmlTreeBuilder.parse(xmlString, "baseUri");
 Assert.assertNotNull(document);
 Assert.assertEquals(1, document.numberOfChildren());
 Assert.assertEquals("tag", document.child(0).nodeName());
 }

 @org.junit.Test
 public void testInitialiseParse() {
 String xmlString = "<xml><tag attr='value'></tag></xml>";
 Document document = new Document();
 xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Assert.assertNotNull(document);
 Assert.assertEquals(1, document.numberOfChildren());
 Assert.assertEquals("tag", document.child(0).nodeName());
 }

 @org.junit.Test
 public void testProcessStartTag() {
 String xmlString = "<xml><tag attr='value'></tag></xml>";
 Document document = new Document();
 Tokenizer tokenizer = new Tokenizer(new StringReader(xmlString));
 Token token = tokenizer.nextToken();
 XmlTreeBuilder xmlTreeBuilderSpy = new XmlTreeBuilder() {
 @Override
 protected void process(Token token) {
 super.process(token);
 if (token.type == Token.Type.StartTag) {
 insert(token.asStartTag());
 }
 }
 };
 xmlTreeBuilderSpy.initialiseParse(new StringReader(xmlString), "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilderSpy.runParser();
 Assert.assertEquals(1, document.numberOfChildren());
 Assert.assertEquals("tag", document.child(0).nodeName());
 }

 @org.junit.Test
 public void testProcessEndTag() {
 String xmlString = "<xml><tag><tag2 attr='value'></tag2></tag></xml>";
 Document document = new Document();
 Tokenizer tokenizer = new Tokenizer(new StringReader(xmlString));
 Token token = tokenizer.nextToken();
 XmlTreeBuilder xmlTreeBuilderSpy = new XmlTreeBuilder() {
 @Override
 protected void process(Token token) {
 super.process(token);
 if (token.type == Token.Type.EndTag) {
 xmlTreeBuilder.popStackToClose(token.asEndTag());
 }
 }
 };
 xmlTreeBuilderSpy.initialiseParse(new StringReader(xmlString), "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilderSpy.runParser();
 Assert.assertEquals(0, document.numberOfChildren());
 }

 @org.junit.Test
 public void testProcessComment() {
 String xmlString = "<xml><!--comment--></xml>";
 Document document = new Document();
 Tokenizer tokenizer = new Tokenizer(new StringReader(xmlString));
 Token token = tokenizer.nextToken();
 XmlTreeBuilder xmlTreeBuilderSpy = new XmlTreeBuilder() {
 @Override
 protected void process(Token token) {
 super.process(token);
 if (token.type == Token.Type.Comment) {
 insert(token.asComment());
 }
 }
 };
 xmlTreeTreeBuilderSpy.initialiseParse(new StringReader(xmlString), "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilderSpy.runParser();
 Assert.assertEquals(1, document.numberOfChildren());
 Assert.assertTrue(document.child(0) instanceof Comment);
 }

 @org.junit.Test
 public void testProcessCharacter() {
 String xmlString = "<xml>char</xml>";
 Document document = new Document();
 Tokenizer tokenizer = new Tokenizer(new StringReader(xmlString));
 Token token = tokenizer.nextToken();
 XmlTreeBuilder xmlTreeBuilderSpy = new XmlTreeBuilder() {
 @Override
 protected void process(Token token) {
 super.process(token);
 if (token.type == Token.Type.Character) {
 insert(token.asCharacter());
 }
 }
 };
 xmlTreeBuilderSpy.initialiseParse(new StringReader(xmlString), "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilderSpy.runParser();
 Assert.assertEquals(1, document.numberOfChildren());
 Assert.assertTrue(document.child(0) instanceof TextNode);
 Assert.assertEquals("char", document.child(0).text());
 }

 @org.junit.Test
 public void testProcessDoctype() {
 String xmlString = "<!DOCTYPE xml [ <!ATTLIST xml foo CDATA #IMPLIED> ]>";
 Document document = new Document();
 Tokenizer tokenizer = new Tokenizer(new StringReader(xmlString));
 Token token = tokenizer.nextToken();
 XmlTreeBuilder xmlTreeBuilderSpy = new XmlTreeBuilder() {
 @Override
 protected void process(Token token) {
 super.process(token);
 if (token.type == Token.Type.Doctype) {
 insert(token.asDoctype());
 }
 }
 };
 xmlTreeBuilderSpy.initialiseParse(new StringReader(xmlString), "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilderSpy.runParser();
 Assert.assertEquals(1, document.numberOfChildren());
 Assert.assertTrue(document.child(0) instanceof DocumentType);
 }

 @org.junit.Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 String xmlString = "<xml>";
 Document document = new Document();
 Tokenizer tokenizer = new Tokenizer(new StringReader(xmlString));
 XmlTreeBuilder xmlTreeBuilderSpy = new XmlTreeBuilder() {
 @Override
 protected void process(Token token) {
 super.process(token);
 if (token.type == -1) { // Unknown token type
 throw new AssertionError("Unexpected token type: " + token.type);
 }
 }
 };
 xmlTreeBuilderSpy.initialiseParse(new StringReader(xmlString), "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilderSpy.runParser();
 }

 @org.junit.Test
 public void testParseFragmentWithBaseUriAndParseSettings() {
 String xmlFragment = "<xml><tag attr='value'></tag></xml>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Assert.assertEquals(1, nodes.size());
 Assert.assertEquals("tag", nodes.get(0).nodeName());
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithNullBaseUri() {
 String xmlFragment = "<xml><tag attr='value'></tag></xml>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, ParseErrorList.noTracking(), ParseSettings.preserveCase);
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithNullParseSettings() {
 String xmlFragment = "<xml><tag attr='value'></tag></xml>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "baseUri", null, ParseErrorList.noTracking());
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithNullXmlFragment() {
 List<Node> nodes = xmlTreeBuilder.parseFragment(null, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 }
}