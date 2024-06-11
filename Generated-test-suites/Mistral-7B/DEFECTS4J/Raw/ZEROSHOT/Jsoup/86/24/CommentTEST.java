package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

/**
 * Test class for Comment.java
 *
 * @author JohnDoe, john.doe@example.com
 */
public class CommentTest {

 @Test
 public void testCommentCreation() {
 Comment comment = new Comment("This is a test comment");
 assertEquals("#comment", comment.nodeName());
 assertEquals("This is a test comment", comment.getData());
 }

 @Test
 public void testCommentCreationWithBaseUri() {
 Comment comment = new Comment("This is a test comment", "/test.html");
 assertEquals("#comment", comment.nodeName());
 assertEquals("This is a test comment", comment.getData());
 assertEquals("/test.html", comment.baseUri());
 }

 @Test
 public void testCommentIsXmlDeclaration() {
 Comment comment = new Comment("<?xml version='1.0' encoding='UTF-8'?>");
 assertTrue(comment.isXmlDeclaration());
 }

 @Test
 public void testCommentIsNotXmlDeclaration() {
 Comment comment = new Comment("This is a test comment");
 assertFalse(comment.isXmlDeclaration());
 }

 @Test
 public void testCommentAsXmlDeclaration() {
 Comment comment = new Comment("<?xml version='1.0' encoding='UTF-8'?>");
 XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
 assertNotNull(xmlDeclaration);
 assertEquals("<?xml version='1.0' encoding='UTF-8'?>", xmlDeclaration.toString());
 }

 @Test
 public void testCommentAsXmlDeclarationNull() {
 Comment comment = new Comment("This is a test comment");
 assertNull(comment.asXmlDeclaration());
 }
}