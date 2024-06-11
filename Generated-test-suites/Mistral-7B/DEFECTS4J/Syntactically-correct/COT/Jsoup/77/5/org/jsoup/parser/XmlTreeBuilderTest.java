import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Tokenizer;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder createXmlTreeBuilder() {
 return new XmlTreeBuilder();
 }

 @Test
 public void testDefaultSettings() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 assertEquals(ParseSettings.preserveCase, xmlTreeBuilder.defaultSettings());
 }

 @Test
 public void testParse() throws Exception {
 String xmlData = "<xml><tag attr=\"value\">content</tag></xml>";
 Document document = createXmlTreeBuilder().parse(new StringReader(xmlData), "baseUri");
 assertNotNull(document);
 assertNotNull(document.body());
 assertEquals(1, document.numChildren());
 Element tag = document.select("tag").first();
 assertEquals("tag", tag.tagName());
 assertEquals("value", tag.attr("attr"));
 assertEquals("content", tag.text());
 }

 @Test
 public void testParseWithBaseUri() throws Exception {
 String xmlData = "<xml xmlns:my=\"http://example.com\"><tag attr=\"value\">content</tag></xml>";
 Document document = createXmlTreeBuilder().parse(new StringReader(xmlData), "baseUri/xmlPath");
 assertNotNull(document);
 assertNotNull(document.body());
 Element tag = document.select("tag").first();
 assertEquals("tag", tag.tagName());
 assertEquals("value", tag.attr("attr"));
 assertEquals("content", tag.text());
 assertEquals("http://example.com", tag.namespaceURI());
 }

 @Test
 public void testParseWithErrorListAndSettings() throws Exception {
 ParseErrorList errors = new ParseErrorList();
 ParseSettings settings = new ParseSettings();
 settings.setErrorHandler(errorHandler);
 String xmlData = "<xml><tag><tag>";
 Document document = createXmlTreeBuilder().parse(new StringReader(xmlData), "baseUri", errors, settings);
 assertNotNull(document);
 assertEquals(1, errors.size());
 assertEquals("Unexpected end tag : tag", errors.get(0).getMessage());
 }

 @Test
 public void testInitialiseParse() throws Exception {
 XmlTreeBuilder xmlTreeBuilder = createXmlTreeBuilder();
 Tokenizer tokenizer = new Tokenizer(new StringReader("<xml><tag attr=\"value\">content</tag></xml>"), xmlTreeBuilder.getDefaultSettings());
 xmlTreeBuilder.initialiseParse(new StringReader("<xml><tag attr=\"value\">content</tag></xml>"), "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(0, xmlTreeBuilder.stack.size());
 xmlTreeBuilder.runParser();
 assertEquals(1, xmlTreeBuilder.stack.size());
 assertEquals(document.body(), xmlTreeBuilder.stack.get(0));
 }

 @Test
 public void testProcessStartTag() throws Exception {
 XmlTreeBuilder xmlTreeBuilder = createXmlTreeBuilder();
 Tokenizer tokenizer = new Tokenizer(new StringReader("<xml><tag attr=\"value\">content</tag></xml>"), xmlTreeBuilder.getDefaultSettings());
 Token startTag = tokenizer.nextToken();
 assertEquals(StartTag.class, startTag.getClass());
 xmlTreeBuilder.initialiseParse(new StringReader("<xml><tag attr=\"value\">content</tag></xml>"), "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Element tag = xmlTreeBuilder.insert(startTag);
 assertEquals("tag", tag.tagName());
 assertEquals("value", tag.attr("attr"));
 }

 @Test
 public void testProcessEndTag() throws Exception {
 XmlTreeBuilder xmlTreeBuilder = createXmlTreeBuilder();
 Tokenizer tokenizer = new Tokenizer(new StringReader("<xml><tag><tag/></tag></xml>"), xmlTreeBuilder.getDefaultSettings());
 Token endTag = tokenizer.nextToken();
 assertEquals(EndTag.class, endTag.getClass());
 xmlTreeBuilder.initialiseParse(new StringReader("<xml><tag><tag/></tag></xml>"), "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilder.popStackToClose(endTag);
 assertEquals(0, xmlTreeBuilder.stack.size());
 }

 @Test
 public void testProcessComment() throws Exception {
 XmlTreeBuilder xmlTreeBuilder = createXmlTreeBuilder();
 Tokenizer tokenizer = new Tokenizer(new StringReader("<xml><!-- Comment --></xml>"), xmlTreeBuilder.getDefaultSettings());
 Token commentToken = tokenizer.nextToken();
 assertEquals(Comment.class, commentToken.getClass());
 xmlTreeBuilder.initialiseParse(new StringReader("<xml><!-- Comment --></xml>"), "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilder.insert(commentToken);
 assertEquals(1, xmlTreeBuilder.stack.size());
 assertEquals(commentToken.getData(), xmlTreeBuilder.stack.get(0).text());
 }

 @Test
 public void testProcessCharacter() throws Exception {
 XmlTreeBuilder xmlTreeBuilder = createXmlTreeBuilder();
 Tokenizer tokenizer = new Tokenizer(new StringReader("<xml>text</xml>"), xmlTreeBuilder.getDefaultSettings());
 Token characterToken = tokenizer.nextToken();
 assertEquals(Character.class, characterToken.getClass());
 xmlTreeBuilder.initialiseParse(new StringReader("<xml>text</xml>"), "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilder.insert(characterToken);
 assertEquals(1, xmlTreeBuilder.stack.size());
 assertEquals(characterToken.getData(), xmlTreeBuilder.stack.get(0).text());
 }

 @Test
 public void testProcessDoctype() throws Exception {
 XmlTreeBuilder xmlTreeBuilder = createXmlTreeBuilder();
 Tokenizer tokenizer = new Tokenizer(new StringReader("<!DOCTYPE xml [ <!ATTLIST xml root EMPTY > ]><xml></xml>"), xmlTreeBuilder.getDefaultSettings());
 Token doctypeToken = tokenizer.nextToken();
 assertEquals(Doctype.class, doctypeToken.getClass());
 xmlTreeBuilder.initialiseParse(new StringReader("<!DOCTYPE xml [ <!ATTLIST xml root EMPTY > ]><xml></xml>"), "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 DocumentType documentType = (DocumentType) xmlTreeBuilder.insert(doctypeToken);
 assertEquals("xml", documentType.getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessUnexpectedTokenType() throws Exception {
 XmlTreeBuilder xmlTreeBuilder = createXmlTreeBuilder();
 Tokenizer tokenizer = new Tokenizer(new StringReader("<xml>"), xmlTreeBuilder.getDefaultSettings());
 Token unexpectedToken = tokenizer.nextToken();
 Validate.fail("Unexpected token type: " + unexpectedToken.type);
 }

 @Test
 public void testInsertNode() throws Exception {
 XmlTreeBuilder xmlTreeBuilder = createXmlTreeBuilder();
 Document document = Jsoup.parse("<xml><tag><tag/></tag></xml>", "baseUri", Parser.xmlParser());
 Element tag = document.select("tag").first();
 xmlTreeBuilder.insertNode(tag);
 assertEquals(1, xmlTreeBuilder.stack.size());
 assertEquals(tag, xmlTreeBuilder.stack.get(0));
 }

 @Test
 public void testParseFragment() throws Exception {
 String xmlFragment = "<tag attr=\"value\">content</tag>";
 List<Node> nodes = createXmlTreeBuilder().parseFragment(xmlFragment, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(1, nodes.size());
 assertEquals("tag", nodes.get(0).tagName());
 assertEquals("value", nodes.get(0).attr("attr"));
 assertEquals("content", nodes.get(0).text());
 }
}