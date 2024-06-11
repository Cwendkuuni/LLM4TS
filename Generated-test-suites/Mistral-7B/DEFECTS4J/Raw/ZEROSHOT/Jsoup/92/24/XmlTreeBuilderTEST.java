package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Parser parser;
 private Document document;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 parser = Mockito.spy(new Parser(xmlTreeBuilder));
 document = Mockito.spy(new Document());
 }

 @Test
 public void testParseXmlDocument() throws IOException {
 String xmlString = "<root><element><subElement/></element></root>";
 Reader xmlInput = new StringReader(xmlString);
 Document parsedDocument = xmlTreeBuilder.parse(xmlInput, "baseUrl");

 assertEquals("baseUrl", parsedDocument.baseUri());
 assertEquals("root", parsedDocument.tagName());

 Element rootElement = parsedDocument.select("root").first();
 assertNotNull(rootElement);
 assertEquals("root", rootElement.tagName());

 Element element = rootElement.select("element").first();
 assertNotNull(element);
 assertEquals("element", element.tagName());

 assertEquals(0, element.childNodes().size());
 }

 @Test
 public void testParseFragment() throws IOException {
 String xmlFragment = "<root><element><subElement/></element></root>";
 String baseUri = "baseUrl";
 List<Node> parsedNodes = xmlTreeBuilder.parseFragment(xmlFragment, document, baseUri, parser);

 assertEquals(3, parsedNodes.size());
 Element rootElement = (Element) parsedNodes.get(0);
 assertEquals("root", rootElement.tagName());

 Element element = (Element) parsedNodes.get(1);
 assertEquals("element", element.tagName());

 TextNode textNode = (TextNode) parsedNodes.get(2);
 assertEquals("", textNode.text());
 }

 @Test
 public void testParseXmlDeclaration() throws IOException {
 String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><element><subElement/></element></root>";
 Reader xmlInput = new StringReader(xmlString);
 Document parsedDocument = xmlTreeBuilder.parse(xmlInput, "baseUrl");

 assertEquals("baseUrl", parsedDocument.baseUri());
 assertEquals("root", parsedDocument.tagName());

 XmlDeclaration xmlDeclaration = (XmlDeclaration) parsedDocument.childNodes().get(0);
 assertNotNull(xmlDeclaration);
 assertEquals("1.0", xmlDeclaration.version());
 assertEquals("UTF-8", xmlDeclaration.encoding());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidXmlString() throws IOException {
 String invalidXmlString = "<root><element><subElement/><invalidTag/>";
 Reader xmlInput = new StringReader(invalidXmlString);

 // This should throw an IllegalArgumentException as the XML is not valid
 xmlTreeBuilder.parse(xmlInput, "baseUrl");
 }
}