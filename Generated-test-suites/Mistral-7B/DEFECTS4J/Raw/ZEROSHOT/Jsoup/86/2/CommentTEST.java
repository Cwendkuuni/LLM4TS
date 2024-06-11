package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * Test class for Comment.java
 *
 * @author John Doe, john.doe@example.com
 */
public class CommentTest {

 /**
 * Test constructor with valid data.
 */
 @Test
 public void testCommentConstructorValidData() {
 String data = "This is a comment.";
 Comment comment = new Comment(data);
 assertEquals("#comment", comment.nodeName());
 assertEquals(data, comment.getData());
 }

 /**
 * Test constructor with deprecated data and baseUri.
 */
 @Test
 public void testCommentConstructorDeprecatedDataAndBaseUri() {
 String data = "This is a comment.";
 Comment comment = new Comment(data, null);
 assertEquals("#comment", comment.nodeName());
 assertEquals(data, comment.getData());
 }

 /**
 * Test isXmlDeclaration() method.
 */
 @Test
 public void testIsXmlDeclaration() {
 // Test empty comment
 String emptyComment = "";
 Comment emptyCommentNode = new Comment(emptyComment);
 assertFalse(emptyCommentNode.isXmlDeclaration());

 // Test comment with XML Declaration
 String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 Comment xmlDeclarationNode = new Comment(xmlDeclaration);
 assertTrue(xmlDeclarationNode.isXmlDeclaration());
 }

 /**
 * Test asXmlDeclaration() method.
 */
 @Test
 public void testAsXmlDeclaration() throws IOException {
 // Test empty comment
 Comment emptyCommentNode = new Comment("");
 assertNull(emptyCommentNode.asXmlDeclaration());

 // Test XML Declaration comment
 String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 Comment xmlDeclarationNode = new Comment(xmlDeclaration);
 XmlDeclaration xmlDeclarationObj = xmlDeclarationNode.asXmlDeclaration();
 assertEquals("xml", xmlDeclarationObj.getDoctype().name());
 assertEquals("XML Declaration", xmlDeclarationObj.getDocumentElement().nodeName());
 assertEquals(xmlDeclaration, xmlDeclarationObj.outerHtml());
 }

 /**
 * Test outerHtml() method.
 */
 @Test
 public void testOuterHtml() throws IOException {
 String data = "This is a comment.";
 Comment comment = new Comment(data);
 Document doc = Jsoup.parse("<html><head><body>" + comment.outerHtml() + "</body></head></html>");
 Element commentElement = doc.body().child(0);
 String expected = "<!--" + data + "-->";
 assertEquals(expected, commentElement.outerHtml());
 }

 /**
 * Test toString() method.
 */
 @Test
 public void testToString() {
 String data = "This is a comment.";
 Comment comment = new Comment(data);
 String expected = "<!--" + data + "-->";
 assertEquals(expected, comment.toString());
 }
}