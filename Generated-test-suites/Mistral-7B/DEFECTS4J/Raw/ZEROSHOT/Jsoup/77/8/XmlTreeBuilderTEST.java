package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Doctype;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.CommentAsCharacter;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.Text;
import org.jsoup.parser.Token.Unknown;
import org.jsoup.parser.XmlTreeBuilder;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder getXmlTreeBuilder() {
 return new XmlTreeBuilder();
 }

 @Test
 public void testDefaultSettings() {
 XmlTreeBuilder xmlTreeBuilder = getXmlTreeBuilder();
 ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
 assertTrue(parseSettings.preserveCase);
 }

 @Test
 public void testParse() {
 String xmlString = "<root><element><subelement/></element></root>";
 XmlTreeBuilder xmlTreeBuilder = getXmlTreeBuilder();
 Document document = xmlTreeBuilder.parse(new StringReader(xmlString), "baseUrl");

 Element rootElement = document.rootElement();
 Element element = rootElement.child(0);
 Element subelement = element.child(0);

 assertTrue(rootElement.tagName().equals("root"));
 assertTrue(element.tagName().equals("element"));
 assertTrue(subelement.tagName().equals("subelement"));
 assertFalse(subelement.text().isBlank()); // verify subelement doesn't have any text content
 }

 @Test
 public void testInsertElement() {
 XmlTreeBuilder xmlTreeBuilder = getXmlTreeBuilder();
 String xmlString = "<root><element><subelement/></element></root>";

 ParseSettings parseSettings = new ParseSettings();
 Document document = new Document();
 document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
 xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "baseUrl", ParseErrorList.noTracking(), parseSettings);
 xmlTreeBuilder.runParser();

 Element rootElement = document.rootElement();
 Element element = rootElement.child(0);
 Element subelement = new Element(Tag.valueOf("subelement"));

 xmlTreeBuilder.insertNode(subelement);

 assertTrue(rootElement.tagName().equals("root"));
 assertTrue(element.tagName().equals("element"));
 assertTrue(subelement.tagName().equals("subelement"));
 assertTrue(subelement.parent().equals(element));
 assertFalse(subelement.text().isBlank());
 }

 @Test
 public void testInsertComment() {
 XmlTreeBuilder xmlTreeBuilder = getXmlTreeBuilder();
 String xmlString = "<!-- test comment -->";

 ParseSettings parseSettings = new ParseSettings();
 Document document = new Document();
 document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
 xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "baseUrl", ParseErrorList.noTracking(), parseSettings);
 xmlTreeBuilder.runParser();

 Comment comment = document.child(0);

 assertTrue(comment.nodeName().equals("#comment"));
 assertTrue(comment.text().text().equals(" test comment "));
 }

 @Test
 public void testInsertCharacter() {
 XmlTreeBuilder xmlTreeBuilder = getXmlTreeBuilder();
 String xmlString = "<root><![CDATA[test character]]></root>";

 ParseSettings parseSettings = new ParseSettings();
 Document document = new Document();
 document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
 xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "baseUrl", ParseErrorList.noTracking(), parseSettings);
 xmlTreeBuilder.runParser();

 CDataNode cdataNode = (CDataNode) document.child(0);

 assertTrue(cdataNode.nodeName().equals("#cdata"));
 assertTrue(cdataNode.text().text().equals("test character"));
 }

 @Test
 public void testInsertDoctype() {
 XmlTreeBuilder xmlTreeBuilder = getXmlTreeBuilder();
 String xmlString = "<!DOCTYPE root SYSTEM \"file:baseUrl/root.dtd\">";

 ParseSettings parseSettings = new ParseSettings();
 Document document = new Document();
 document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
 xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "baseUrl", ParseErrorList.noTracking(), parseSettings);
 xmlTreeBuilder.runParser();

 Doctype doctypeNode = document.child(0);

 assertTrue(doctypeNode.nodeName().equals("!DOCTYPE"));
 assertTrue(doctypeNode.data().equals("root"));
 assertTrue(doctypeNode.systemId().equals("file:baseUrl/root.dtd"));
 }

 @Test
 public void testPopStackToClose() {
 XmlTreeBuilder xmlTreeBuilder = getXmlTreeBuilder();
 String xmlString = "<root><element><subelement/></element></root>";

 ParseSettings parseSettings = new ParseSettings();
 Document document = new Document();
 document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
 xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "baseUrl", ParseErrorList.noTracking(), parseSettings);
 xmlTreeBuilder.runParser();

 Element rootElement = document.rootElement();
 Element element = rootElement.child(0);
 Element subelement = element.child(0);
 Element closeSubelement = new Element(Tag.valueOf("subelement"));

 xmlTreeBuilder.insertNode(closeSubelement);
 xmlTreeBuilder.popStackToClose(new EndTag("subelement"));

 assertTrue(rootElement.tagName().equals("root"));
 assertTrue(element.tagName().equals("element"));
 assertNull(subelement);
 }

 @Test
 public void testParseFragment() {
 XmlTreeBuilder xmlTreeBuilder = getXmlTreeBuilder();
 String xmlString = "<root><element><subelement/></element></root>";

 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlString, "baseUrl", ParseErrorList.noTracking(), new ParseSettings());

 Element rootElement = (Element) nodes.get(0);
 Element element = (Element) rootElement.child(0);
 Element subelement = (Element) element.child(0);

 assertTrue(rootElement.tagName().equals("root"));
 assertTrue(element.tagName().equals("element"));
 assertTrue(subelement.tagName().equals("subelement"));
 assertFalse(subelement.text().isBlank());
 }

 @Test
 public void testProcessUnknownToken() {
 XmlTreeBuilder xmlTreeBuilder = getXmlTreeBuilder();
 String xmlString = "<unknown>";

 ParseSettings parseSettings = new ParseSettings();
 Document document = new Document();
 document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
 xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "baseUrl", ParseErrorList.noTracking(), parseSettings);

 Token token = xmlTreeBuilder.nextToken();

 assertTrue(token.type == Token.Type.StartTag);
 assertTrue(token.asStartTag().name().equals("unknown"));
 }

 @Test
 public void testProcessCommentAsCharacter() {
 XmlTreeBuilder xmlTreeBuilder = getXmlTreeBuilder();
 String xmlString = "<!-- test comment -->";

 ParseSettings parseSettings = new ParseSettings();
 Document document = new Document();
 document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
 xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "baseUrl", ParseErrorList.noTracking(), parseSettings);

 Token token = xmlTreeBuilder.nextToken();

 assertTrue(token.type == Token.Type.CommentAsCharacter);
 assertTrue(token.asCommentAsCharacter().getData().equals(" test comment "));
 }

 @Test
 public void testProcessEndTag() {
 XmlTreeBuilder xmlTreeBuilder = getXmlTreeBuilder();
 String xmlString = "<root><element><subelement/></element></root>";

 ParseSettings parseSettings = new ParseSettings();
 Document document = new Document();
 document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
 xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "baseUrl", ParseErrorList.noTracking(), parseSettings);
 xmlTreeBuilder.runParser();

 Element rootElement = document.rootElement();
 Element element = rootElement.child(0);
 Element subelement = element.child(0);

 Token token = xmlTreeBuilder.nextToken();

 assertTrue(token.type == Token.Type.EndTag);
 assertTrue(token.asEndTag().name().equals("subelement"));
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 XmlTreeBuilder xmlTreeBuilder = getXmlTreeBuilder();
 String xmlString = "<root><![CDATA[test character]]>";

 ParseSettings parseSettings = new ParseSettings();
 Document document = new Document();
 document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
 xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "baseUrl", ParseErrorList.noTracking(), parseSettings);
 xmlTreeBuilder.runParser();

 Token token = xmlTreeBuilder.nextToken();

 assertTrue(token.type != Token.Type.EOF); // this assertion should fail
 }
}