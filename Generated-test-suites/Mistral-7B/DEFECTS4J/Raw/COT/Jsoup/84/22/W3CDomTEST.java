package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.junit.Test;
import static org.junit.Assert.*;

public class W3CDomTest {

 private W3CDom w3CDom = new W3CDom();

 @Test
 public void testFromJsoup() {
 Document jsoupDocument = createJsoupDocument();
 Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
 assertNotNull(w3cDocument);
 }

 @Test(expected = IllegalStateException.class)
 public void testFromJsoupWithNullDocument() {
 w3CDom.fromJsoup(null);
 }

 @Test
 public void testConvert() {
 Document jsoupDocument = createJsoupDocument();
 Document w3cDocument = w33CDom.fromJsoup(jsoupDocument);
 w3CDom.convert(jsoupDocument, w3cDocument);
 assertSame(jsoupDocument, w3cDocument);
 }

 @Test
 public void testAsString() {
 Document w3cDocument = createW3CDocument();
 String output = w3CDom.asString(w3cDocument);
 assertNotNull(output);
 }

 @Test(expected = IllegalStateException.class)
 public void testAsStringWithNullDocument() {
 w3CDom.asString(null);
 }

 @Test
 public void testW3CBuilder_emptyDocument() {
 Document w3cDocument = w3CDom.fromJsoup(createEmptyJsoupDocument());
 Element root = w3cDocument.getDocumentElement();
 assertNotNull(root);
 assertEquals("", root.getNodeValue());
 }

 @Test
 public void testW3CBuilder_elementWithTextChild() {
 Document jsoupDocument = createJsoupDocumentWithTextChild();
 Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
 Element root = w3cDocument.getDocumentElement();
 Text textNode = root.getFirstChild();
 assertEquals("Child text", textNode.getNodeValue());
 }

 @Test
 public void testW3CBuilder_elementWithAttributes() {
 Document jsoupDocument = createJsoupDocumentWithAttributes();
 Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
 Element root = w3cDocument.getDocumentElement();
 assertEquals("foo", root.getAttribute("foo"));
 assertEquals("bar", root.getAttribute("bar"));
 }

 @Test
 public void testW3CBuilder_elementWithNamespace() {
 Document jsoupDocument = createJsoupDocumentWithNamespace();
 Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
 Element root = w3cDocument.getDocumentElement();
 assertEquals("prefix:tag", root.getTagName());
 }

 @Test(expected = IllegalStateException.class)
 public void testConvertWithNullJsoupDocument() {
 w3CDom.convert(null, createW3CDocument());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConvertWithNullW3CDocument() {
 w3CDom.convert(createJsoupDocument(), null);
 }

 @Test
 public void testW3CBuilder_commentNode() {
 Document jsoupDocument = createJsoupDocumentWithComment();
 Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
 Comment comment = w3cDocument.createComment("Comment text");
 Element commentParent = w3cDocument.createElement("parent");
 commentParent.appendChild(comment);
 Element root = w3cDocument.getDocumentElement();
 root.appendChild(commentParent);
 assertEquals("Comment text", comment.getNodeValue());
 }

 @Test
 public void testW3CBuilder_dataNode() {
 Document jsoupDocument = createJsoupDocumentWithDataNode();
 Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
 Text dataNode = w3cDocument.createTextNode("Data text");
 Element root = w3cDocument.getDocumentElement();
 root.appendChild(dataNode);
 assertEquals("Data text", dataNode.getNodeValue());
 }

 @Test
 public void testW3CBuilder_processElementsInOrder() {
 Document jsoupDocument = createJsoupDocumentWithOrderedNodes();
 Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
 Element root = w3cDocument.getDocumentElement();
 Element firstChild = (Element) root.getFirstChild();
 Element secondChild = (Element) root.getFirstChild().getNextSibling();
 assertEquals("First child text", firstChild.getTextContent());
 assertEquals("Second child text", secondChild.getTextContent());
 }

 @Test
 public void testW3CBuilder_processTextNodesWithWhitespace() {
 Document jsoupDocument = createJsoupDocumentWithTextNodesWithWhitespace();
 Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
 Element root = w3cDocument.getDocumentElement();
 Text textNode1 = (Text) root.getFirstChild();
 Text textNode2 = (Text) textNode1.getNextSibling();
 assertEquals("First child text ", textNode1.getTextContent().trim());
 assertEquals("Second child text", textNode2.getTextContent().trim());
 }

 @Test
 public void testW3CBuilder_processDataNodesWithWhitespace() {
 Document jsoupDocument = createJsoupDocumentWithDataNodesWithWhitespace();
 Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
 Element root = w3cDocument.getDocumentElement();
 Text dataNode1 = (Text) root.getFirstChild();
 Text dataNode2 = (Text) dataNode1.getNextSibling();
 assertEquals("First child data  ", dataNode1.getTextContent().trim());
 assertEquals("Second child data", dataNode2.getTextContent().trim());
 }

 @Test
 public void testW3CBuilder_processCommentNodesWithWhitespace() {
 Document jsoupDocument = createJsoupDocumentWithCommentNodesWithWhitespace();
 Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);
 Element root = w3cDocument.getDocumentElement();
 Comment comment1 = (Comment) root.getFirstChild();
 Comment comment2 = (Comment) comment1.getNextSibling();
 assertEquals("Comment 1 text", comment1.getTextContent().trim());
 assertEquals("Comment 2 text", comment2.getTextContent().trim());
 }

 private Document createJsoupDocument() {
 return new org.jsoup.nodes.Document();
 }

 private Document createJsoupDocumentWithTextChild() {
 Element root = new Element("root");
 Text text = new TextNode("Child text");
 root.appendChild(text);
 return new org.jsoup.nodes.Document(root);
 }

 private Document createJsoupDocumentWithAttributes() {
 Element root = new Element("root");
 root.attr("foo", "fooValue");
 root.attr("bar", "barValue");
 return new org.jsoup.nodes.Document(root);
 }

 private Document createJsoupDocumentWithNamespace() {
 Element root = new Element("prefix:root");
 return new org.jsoup.nodes.Document(root);
 }

 private Document createJsoupDocumentWithComment() {
 Comment comment = new Comment("Comment text");
 Element root = new Element("root");
 root.appendChild(comment);
 return new org.jsoup.nodes.Document(root);
 }

 private Document createJsoupDocumentWithDataNode() {
 Text dataNode = new TextNode("Data text");
 Element root = new Element("root");
 root.appendChild(dataNode);
 return new org.jsoup.nodes.Document(root);
 }

 private Document createEmptyJsoupDocument() {
 return new org.jsoup.nodes.Document();
 }

 private Document createJsoupDocumentWithOrderedNodes() {
 Element firstChild = new Element("firstChild");
 firstChild.setTextContent("First child text");
 Element secondChild = new Element("secondChild");
 secondChild.setTextContent("Second child text");
 Element root = new Element("root");
 root.appendChild(firstChild);
 root.appendChild(secondChild);
 return new org.jsoup.nodes.Document(root);
 }

 private Document createJsoupDocumentWithTextNodesWithWhitespace() {
 Element root = new Element("root");
 Text textNode1 = new TextNode(" First child text ");
 Text textNode2 = new TextNode(" Second child text ");
 root.appendChild(textNode1);
 root.appendChild(textNode2);
 return new org.jsoup.nodes.Document(root);
 }

 private Document createJsoupDocumentWithDataNodesWithWhitespace() {
 Element root = new Element("root");
 Text dataNode1 = new TextNode(" First child data  ");
 Text dataNode2 = new TextNode(" Second child data");
 root.appendChild(dataNode1);
 root.appendChild(dataNode2);
 return new org.jsoup.nodes.Document(root);
 }

 private Document createJsoupDocumentWithCommentNodesWithWhitespace() {
 Element root = new Element("root");
 Comment comment1 = new Comment(" Comment 1 text ");
 Comment comment2 = new Comment(" Comment 2 text ");
 root.appendChild(comment1);
 root.appendChild(comment2);
 return new org.jsoup.nodes.Document(root);
 }

 private Document createW3CDocument() {
 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
 DocumentBuilder builder;
 try {
 builder = factory.newDocumentBuilder();
 } catch (ParserConfigurationException e) {
 throw new IllegalStateException(e);
 }
 Document document = builder.newDocument();
 return document;
 }

}