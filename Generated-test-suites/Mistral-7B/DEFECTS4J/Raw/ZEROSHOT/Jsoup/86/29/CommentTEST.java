package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

/**
 * Test class for Comment class.
 */
public class CommentTest {

 @Test
 public void testCommentCreation() {
 Comment comment = new Comment("This is a comment.");
 assertEquals("#comment", comment.nodeName());
 assertEquals("This is a comment.", comment.getData());
 }

 @Test
 public void testCommentCreationWithBaseUri() {
 Comment comment = new Comment("This is a comment.", "/test.html");
 assertEquals("#comment", comment.nodeName());
 assertEquals("This is a comment.", comment.getData());
 assertEquals("/test.html", comment.baseUri());
 }

 @Test
 public void testCommentIsXmlDeclaration() {
 Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>");
 assertTrue(comment.isXmlDeclaration());

 Comment notXmlComment = new Comment("This is a regular comment.");
 assertFalse(notXmlComment.isXmlDeclaration());
 }

 @Test
 public void testCommentAsXmlDeclaration() {
 Comment xmlComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>");
 XmlDeclaration xmlDeclaration = xmlComment.asXmlDeclaration();
 assertSame(xmlDeclaration.version(), "1.0");
 assertSame(xmlDeclaration.encoding(), "UTF-8");
 assertNull(xmlDeclaration.getDoctype());

 // Check for attributes
 Attributes xmlDeclarationAttributes = xmlDeclaration.attributes();
 assertEquals(2, xmlDeclarationAttributes.size());
 assertEquals("version", xmlDeclarationAttributes.keyAt(0));
 assertEquals("1.0", xmlDeclarationAttributes.valueAt(0));
 assertEquals("encoding", xmlDeclarationAttributes.keyAt(1));
 assertEquals("UTF-8", xmlDeclarationAttributes.valueAt(1));
 }

 @Test
 public void testCommentOuterHtml() throws IOException {
 Comment comment = new Comment("This is a comment.");
 String outerHtml = comment.outerHtml();
 assertEquals("<!--This is a comment.-->", outerHtml);
 }

 @Test
 public void testCommentOuterHtmlWithIndentation() throws IOException {
 Comment comment = new Comment("This is a comment.");
 Document document = Jsoup.parse("<html></html>");
 document.setBaseUri("/test.html");
 comment.setParent(document);
 comment.outerHtmlHead(Jsoup.connect("/test.html/output.txt").buffer(), 0, null);
 comment.outerHtmlTail(Jsoup.connect("/test.html/output.txt").buffer(), 0, null);
 String outerHtml = Jsoup.connect("/test.html/output.txt").toString();
 assertEquals("<html>\n<!--This is a comment.\n-->\n</html>", outerHtml);
 }

 @Test
 public void testCommentToString() {
 Comment comment = new Comment("This is a comment.");
 assertEquals("<!--This is a comment.-->", comment.toString());
 }
}