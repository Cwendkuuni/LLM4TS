import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Assert;
import org.junit.Test;

public class CommentTest {

 private static final String COMMENT_DATA = "This is a comment";

 @Test
 public void testCommentConstructor() {
 Comment comment = new Comment(COMMENT_DATA);
 Assert.assertEquals(COMMENT_DATA, comment.getData());
 }

 @Test
 public void testDeprecatedCommentConstructor() {
 Comment comment = new Comment(COMMENT_DATA, null);
 Assert.assertEquals(COMMENT_DATA, comment.getData());
 }

 @Test
 public void testNodeName() {
 Comment comment = new Comment(COMMENT_DATA);
 Assert.assertEquals("#comment", comment.nodeName());
 }

 @Test
 public void testGetterData() {
 Comment comment = new Comment(COMMENT_DATA);
 Assert.assertEquals(COMMENT_DATA, comment.getData());
 }

 @Test
 public void testOuterHtmlHead() throws IOException {
 Appendable accum = new StringBuilder();
 Comment comment = new Comment(COMMENT_DATA);
 Document.OutputSettings out = new Document.OutputSettings();
 comment.outerHtmlHead(accum, 0, out);
 Assert.assertEquals("<!--This is a comment-->", accum.toString());
 }

 @Test
 public void testOuterHtmlTail() throws IOException {
 Appendable accum = new StringBuilder();
 Comment comment = new Comment(COMMENT_DATA);
 Document.OutputSettings out = new Document.OutputSettings();
 comment.outerHtmlTail(accum, 0, out);
 Assert.assertEquals("", accum.toString());
 }

 @Test
 public void testToString() {
 Comment comment = new Comment(COMMENT_DATA);
 Assert.assertEquals("<!--This is a comment-->", comment.toString());
 }

 @Test
 public void testIsXmlDeclaration() {
 Comment comment = new Comment(COMMENT_DATA);
 Assert.assertFalse(comment.isXmlDeclaration());

 Comment xmlComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + COMMENT_DATA);
 Assert.assertTrue(comment.isXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclaration() {
 Comment xmlComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + COMMENT_DATA);
 XmlDeclaration xmlDecl = xmlComment.asXmlDeclaration();
 Assert.assertNotNull(xmlDecl);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidXmlDeclaration() {
 Comment invalidXmlComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + ">"); // Invalid XML declaration
 xmlComment.asXmlDeclaration();
 }
}