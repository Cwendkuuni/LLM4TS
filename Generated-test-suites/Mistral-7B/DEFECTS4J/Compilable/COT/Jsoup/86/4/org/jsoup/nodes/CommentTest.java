package org.jsoup.nodes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Assert;
import org.junit.Test;

public class CommentTest {

 private Comment createComment(String data) {
 return new Comment(data);
 }

 @Test
 public void testCommentConstructor() {
 Comment comment = createComment("This is a comment");
 Assert.assertEquals("This is a comment", comment.getData());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCommentConstructorNullData() {
 try {
 new Comment(null);
 } catch (IllegalArgumentException e) {
 Assert.assertEquals("data is marked non-null but is null", e.getMessage());
 throw e;
 }
 }

 @Test
 public void testNodeName() {
 Comment comment = createComment("test comment");
 Assert.assertEquals("#comment", comment.nodeName());
 }

 @Test
 public void testGetData() {
 Comment comment = createComment("test comment");
 Assert.assertEquals("test comment", comment.getData());
 }

 @Test
 public void testOuterHtmlHead() throws Exception {
 Appendable accum = new StringBuilder();
 Comment comment = createComment("test comment");
 comment.outerHtmlHead(accum, 0, null);
 Assert.assertEquals("<!--test comment-->", accum.toString());
 }

 @Test
 public void testOuterHtmlTail() throws Exception {
 Appendable accum = new StringBuilder();
 Comment comment = createComment("test comment");
 comment.outerHtmlTail(accum, 0, null);
 // empty
 Assert.assertEquals("", accum.toString());
 }

 @Test
 public void testToString() {
 Comment comment = createComment("test comment");
 Assert.assertEquals("<!--test comment-->", comment.toString());
 }

 @Test
 public void testIsXmlDeclarationFalse() {
 Comment comment = createComment("test comment");
 Assert.assertFalse(comment.isXmlDeclaration());
 }

 @Test
 public void testIsXmlDeclarationTrue() {
 Comment comment = createComment("<?xml version='1.0' encoding='UTF-8'?>");
 Assert.assertTrue(comment.isXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclarationNull() {
 Comment comment = createComment("test comment");
 Assert.assertNull(comment.asXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclarationValid() throws Exception {
 Comment comment = createComment("<?xml version='1.0' encoding='UTF-8'?>");
 XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
 Assert.assertNotNull(xmlDeclaration);
 }
}