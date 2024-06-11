import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Xmldeclaration;

import java.io.StringReader;
import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
 }

 @Test
 public void testParseWithReaderAndBaseUri() {
 String xmlString = "<xml><tag attr='value'></tag></xml>";
 Reader reader = new StringReader(xmlString);
 String baseUri = "http://example.com";
 Document document = xmlTreeBuilder.parse(reader, baseUri);
 assertEquals("<xml><tag attr='value'></tag></xml>", document.body().html());
 }

 @Test
 public void testParseWithStringAndBaseUri() {
 String xmlString = "<xml><tag attr='value'></tag></xml>";
 Document document = xmlTreeBuilder.parse(xmlString, baseUri);
 assertEquals("<xml><tag attr='value'></tag></xml>", document.body().html());
 }

 @Test
 public void testInitialiseParse() {
 String xmlString = "<xml><tag attr='value'></tag></xml>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();
 ParseSettings settings = new ParseSettings();
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 xmlTreeBuilder.initialiseParse(new StringReader(xmlString), baseUri, errors, settings);
 assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
 assertEquals(baseUri, xmlTreeBuilder.doc.baseUri());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessNullToken() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.process(null);
 }

 @Test
 public void testProcessStartTag() {
 String xmlString = "<tag attr1='value1' attr2='value2'>content</tag>";
 Token startTagToken = new Token.StartTag("tag", null, null, null, null, null, null);

 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Element tagElement = xmlTreeBuilder.insert(startTagToken);
 assertEquals("tag", tagElement.nodeName());
 assertEquals("http://example.com", tagElement.baseUri());
 assertEquals("value1", tagElement.tagName());
 assertEquals(2, tagElement.childNodes().size());
 }

 @Test
 public void testProcessEndTag() {
 String xmlString = "<tag><subtag/></tag>";
 Token endTagToken = new Token.EndTag("tag");

 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Element tagElement = new Element("tag", "http://example.com", null);
 Element subtagElement = new Element("subtag", "http://example.com", null);
 tagElement.appendChild(subtagElement);
 xmlTreeBuilder.stack.add(tagElement);
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertEquals(0, xmlTreeBuilder.stack.size());
 }

 @Test
 public void testProcessComment() {
 String xmlString = "<!-- comment -->";
 Token commentToken = new Token.Comment("comment");

 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Comment commentNode = xmlTreeBuilder.insert(commentToken);
 assertEquals("comment", commentNode.data());
 }

 @Test
 public void testProcessCharacter() {
 String xmlString = "<tag>content</tag>";
 Token characterToken = new Token.Character('c', "content");

 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 TextNode textNode = xmlTreeBuilder.insert(characterToken);
 assertEquals("content", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 String xmlString = "<!DOCTYPE tag [ <!ATTLIST tag attr CDATA #REQUIRED> ]>";
 Token doctypeToken = new Token.Doctype("tag");

 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 DocumentType doctypeNode = xmlTreeBuilder.insert(doctypeToken);
 assertEquals("tag", doctypeNode.nodeName());
 }

 @Test
 public void testPopStackToCloseUnknownTag() {
 String xmlString = "<tag></tag>";
 String tagName = "unknownTag";

 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Element tagElement = new Element("tag", "http://example.com", null);
 xmlTreeBuilder.stack.add(tagElement);
 xmlTreeBuilder.popStackToClose(new Token.EndTag(tagName));
 assertEquals(0, xmlTreeBuilder.stack.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testPopStackToCloseUnknownTagNotSelfClosing() {
 String xmlString = "<tag></tag>";
 String tagName = "tag";

 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Element tagElement = new Element("tag", "http://example.com", null);
 xmlTreeBuilder.stack.add(tagElement);
 xmlTreeBuilder.popStackToClose(new Token.EndTag(tagName));
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<tag attr='value'><subtag/></tag>";
 String baseUri = "http://example.com";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, baseUri, new ParseErrorList(), ParseSettings.preserveCase);
 assertEquals(1, nodes.size());
 assertEquals("tag", nodes.get(0).nodeName());
 }

}