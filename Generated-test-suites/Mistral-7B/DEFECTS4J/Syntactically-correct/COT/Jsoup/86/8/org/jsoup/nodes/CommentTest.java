package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

/**
 * Test class for Comment.java
 */
public class CommentTest {

 private Comment comment;

 @Before
 public void setUp() {
 comment = new Comment("This is a comment.");
 }

 /**
  * Test constructor with valid data
  */
 @Test
 public void testCommentConstructor() {
 Comment comment = new Comment("This is a comment.");
 assertEquals("#comment", comment.nodeName());
 assertEquals("This is a comment.", comment.getData());
 }

 /**
  * Test constructor with deprecated data and baseUri
  */
 @Test
 public void testDeprecatedCommentConstructor() {
 Comment comment = new Comment("This is a comment.", "baseUri");
 assertEquals("#comment", comment.nodeName());
 assertEquals("This is a comment.", comment.getData());
 }

 /**
  * Test getData() method
  */
 @Test
 public void testGetData() {
 assertEquals("This is a comment.", comment.getData());
 }

 /**
  * Test isXmlDeclaration() method
  */
 @Test
 public void testIsXmlDeclaration() {
 assertFalse(comment.isXmlDeclaration());
 String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 Comment xmlDeclarationComment = new Comment(xmlDeclaration);
 assertTrue(xmlDeclarationComment.isXmlDeclaration());
 }

 /**
  * Test asXmlDeclaration() method with valid XML declaration
  */
 @Test
 public void testAsXmlDeclarationValid() {
 String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 Comment xmlDeclarationComment = new Comment(xmlDeclaration);
 XmlDeclaration xmlDeclarationObj = xmlDeclarationComment.asXmlDeclaration();
 assertNotNull(xmlDeclarationObj);
 assertEquals("version", xmlDeclarationObj.version());
 assertEquals("UTF-8", xmlDeclarationObj.encoding());
 }

 /**
  * Test asXmlDeclaration() method with invalid XML declaration
  */
 @Test(expected = IllegalArgumentException.class)
 public void testAsXmlDeclarationInvalid() {
 String invalidXmlDeclaration = "!xml version=\"1.0\" encoding=\"UTF-8\"?>";
 Comment invalidXmlDeclarationComment = new Comment(invalidXmlDeclaration);
 invalidXmlDeclarationComment.asXmlDeclaration();
 }

 /**
  * Test outerHtml() method
  */
 @Test
 public void testOuterHtml() {
 StringBuilder sb = new StringBuilder();
 comment.outerHtml(sb, 0, null);
 assertEquals("<!--This is a comment.-->", sb.toString());
 }

 /**
  * Test toString() method
  */
 @Test
 public void testToString() {
 assertEquals("<!--This is a comment.-->", comment.toString());
 }
}