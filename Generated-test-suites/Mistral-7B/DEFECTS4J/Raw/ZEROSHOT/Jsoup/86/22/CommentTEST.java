package org.jsoup.nodes;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CommentTest {

 @Test
 public void testCreateComment() {
 String commentData = "This is a test comment";
 Comment comment = new Comment(commentData);
 assertEquals("#comment", comment.nodeName());
 assertEquals(commentData, comment.getData());
 }

 @Test
 public void testCreateCommentDeprecated() {
 String commentData = "This is a test comment";
 Comment comment = new Comment(commentData, null); // deprecated constructor
 assertEquals("#comment", comment.nodeName());
 assertEquals(commentData, comment.getData());
 }

 @Test
 public void testIsXmlDeclaration() {
 Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
 assertTrue(comment.isXmlDeclaration());

 Comment comment2 = new Comment("This is a test comment");
 assertFalse(comment2.isXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclaration() throws IOException {
 Comment xmlComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
 XmlDeclaration xmlDeclaration = xmlComment.asXmlDeclaration();
 assertNotNull(xmlDeclaration);
 assertEquals("1.0", xmlDeclaration.getVersion());
 assertEquals("UTF-8", xmlDeclaration.getEncoding());

 Comment nonXmlComment = new Comment("This is a test comment");
 assertNull(nonXmlComment.asXmlDeclaration());
 }

 @Test
 public void testOuterHtml() throws IOException {
 String commentData = "This is a test comment";
 Comment comment = new Comment(commentData);
 String expected = "<!--" + commentData + "-->";
 String actual = comment.outerHtml();
 assertEquals(expected, actual);
 }

 @Test
 public void testToString() throws IOException {
 String commentData = "This is a test comment";
 Comment comment = new Comment(commentData);
 String expected = "<!--" + commentData + "-->";
 String actual = comment.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testCreateCommentWithAttributes() throws IOException {
 String commentData = "This is a test comment <Class='Test'>";
 Comment comment = new Comment(commentData);
 Element element = Jsoup.parse(comment.outerHtml()).child(0);
 Attribute attribute = element.attr("Class");
 assertEquals("Test", attribute.getValue());
 }

 @Test
 public void testCreateCommentWithNestedTextNode() throws IOException {
 String commentData = "This is a test comment <Text>Nested Text</Text>";
 Comment comment = new Comment(commentData);
 Element element = Jsoup.parse(comment.outerHtml()).child(0);
 TextNode nestedTextNode = (TextNode) element.child(0);
 assertEquals("Nested Text", nestedText.toString());
 }
}