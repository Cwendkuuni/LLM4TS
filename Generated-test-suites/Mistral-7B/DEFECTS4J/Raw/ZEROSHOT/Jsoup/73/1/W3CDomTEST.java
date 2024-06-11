package org.jsoup.helper;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class W3CDomTest {

 private Document jsoupDocument;
 private Document w3cDocument;

 @Before
 public void setUp() {
 jsoupDocument = new org.jsoup.nodes.Document();
 Element root = jsoupDocument.createElement("html");
 root.setAttribute("xmlns", "http://www.w3.org/1999/xhtml");
 jsoupDocument.appendChild(root);
 Element body = jsoupDocument.createElement("body");
 root.appendChild(body);
 Text text = jsoupDocument.createTextNode("Hello World!");
 body.appendChild(text);
 }

 @Test
 public void testFromJsoup() {
 w3cDocument = W3CDom.fromJsoup(jsoupDocument);
 assertNotNull(w3cDocument);
 }

 @Test
 public void testConvert() {
 W3CDom w3cdom = new W3CDom();
 w3cDocument = Document.newDocument();
 w3cdom.convert(jsoupDocument, w3cDocument);
 assertNotNull(w3cDocument);
 }

 @Test
 public void testAsString() {
 String jsoupString = W3CDom.asString(jsoupDocument);
 String w3cString = W3CDom.asString(w3cDocument);
 assertEquals(jsoupString, w3cString);
 }

 @Test
 public void testConvertWithNamespaces() {
 jsoupDocument.getRootElement().namespaceSet().put("t", "http://test.com/namespace");
 Map<String, String> expectedNamespaces = new HashMap<>();
 expectedNamespaces.put("t", "http://test.com/namespace");
 w3cDocument = Document.newDocument();
 w3cdom.convert(jsoupDocument, w3cDocument);
 Map<String, String> actualNamespaces = new HashMap<>();
 for (String prefix : w3cDocument.getDocumentElement().getNamespaceURI().getPrefixes()) {
 actualNamespaces.put(prefix, w3cDocument.getDocumentElement().getNamespaceURI().getNamespaceURI().substring(0, prefix.length() + 1));
 }
 assertEquals(expectedNamespaces, actualNamespaces);
 }

 @Test
 public void testConvertWithAttributes() {
 jsoupDocument.getRootElement().setAttribute("html:id", "root");
 w3cDocument = Document.newDocument();
 w3cdom.convert(jsoupDocument, w3cDocument);
 String actualId = w3cDocument.getDocumentElement().getAttribute("html:id");
 assertEquals("root", actualId);
 }

 @Test
 public void testConvertWithTextNodes() {
 jsoupDocument.getRootElement().appendChild(jsoupDocument.createTextNode("Hello"));
 jsoupDocument.getRootElement().appendChild(jsoupDocument.createTextNode(" "));
 jsoupDocument.getRootElement().appendChild(jsoupDocument.createTextNode("World!"));
 w3cDocument = Document.newDocument();
 w3cdom.convert(jsoupDocument, w3cDocument);
 Text actualText1 = (Text) w3cDocument.getDocumentElement().getFirstChild();
 Text actualText2 = (Text) w3cDocument.getDocumentElement().getChildNodes().get(1);
 Text actualText3 = (Text) w3cDocument.getDocumentElement().getChildNodes().get(2);
 String actualText1Content = actualText1.getNodeValue();
 String actualText2Content = actualText2.getNodeValue();
 String actualText3Content = actualText3.getNodeValue();
 assertEquals("Hello ", actualText1Content);
 assertEquals(" ", actualText2Content);
 assertEquals("World!", actualText3Content);
 }

 @Test
 public void testConvertWithCommentNode() {
 jsoupDocument.getRootElement().appendChild(jsoupDocument.createComment("This is a comment"));
 w3cDocument = Document.newDocument();
 w3cdom.convert(jsoupDocument, w3cDocument);
 Comment actualComment = (Comment) w3cDocument.getDocumentElement().getLastChild();
 assertEquals("This is a comment", actualComment.getNodeValue());
 }

 @Test
 public void testConvertWithDataNode() {
 DataNode dataNode = jsoupDocument.createElementData("data");
 dataNode.setData("This is data");
 jsoupDocument.getRootElement().appendChild(dataNode);
 w3cDocument = Document.newDocument();
 w3cdom.convert(jsoupDocument, w3cDocument);
 Text actualText = (Text) w3cDocument.getDocumentElement().getLastChild();
 assertEquals("This is data", actualText.getNodeValue());
 }

}