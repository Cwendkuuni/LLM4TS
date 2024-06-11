package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

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
 Comment comment = new Comment("<?xml version='1.0' encoding='UTF-8'?>");
 assertTrue(comment.isXmlDeclaration());
 }

 @Test
 public void testCommentIsNotXmlDeclaration() {
 Comment comment = new Comment("This is a regular comment.");
 assertFalse(comment.isXmlDeclaration());
 }

 @Test
 public void testCommentAsXmlDeclaration() {
 Comment comment = new Comment("<?xml version='1.0' encoding='UTF-8'?>");
 XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
 assertNotNull(xmlDeclaration);
 assertEquals("<?xml version='1.0' encoding='UTF-8'?>", xmlDeclaration.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testCommentAsXmlDeclarationNullData() {
 Comment comment = new Comment("");
 comment.asXmlDeclaration();
 }

 @Test
 public void testCommentOuterHtml() throws IOException {
 Comment comment = new Comment("This is a comment.");
 Document doc = Jsoup.parse("<html><head></head><body><!--" + comment.getData() + "--></body></html>");
 assertEquals("<!--This is a comment-->", comment.outerHtml());
 assertEquals("<html><head></head><body><!--This is a comment--></body></html>", doc.toString());
 }

 @Test
 public void testCommentOuterHtmlWithPrettyPrint() throws IOException {
 Document doc = Jsoup.parse("<html><head></head><body><!--This is a comment--></body></html>");
 Comment comment = doc.body().child(0);
 doc.outputSettings().setPrettyPrint(true);
 assertEquals("<html>\n<head>\n</head>\n<body>\n<!--This is a comment-->\n</body>\n</html>", comment.outerHtml());
 }

 @Test
 public void testCommentOuterHtmlWithEmptyData() throws IOException {
 Comment comment = new Comment("");
 Document doc = Jsoup.parse("<html><head></head><body><!-- --></body></html>");
 assertEquals("<html>\n<head>\n</head>\n<body>\n<!-- -->\n</body>\n</html>", comment.outerHtml());
 }

 @Test
 public void testCommentToString() {
 Comment comment = new Comment("This is a comment.");
 assertEquals("<!--This is a comment-->", comment.toString());
 }

 @Test
 public void testCommentWithAttributes() {
 Comment comment = new Comment("<!-- My comment with an attribute: id=\"test\" -->");
 Document doc = Jsoup.parse(comment.toString());
 assertEquals(1, doc.childNodeSize());
 assertEquals("My comment with an attribute:", doc.child(0).text());
 Attribute idAttr = doc.child(0).attributes().get("id");
 assertEquals("test", idAttr.getValue());
 }

}