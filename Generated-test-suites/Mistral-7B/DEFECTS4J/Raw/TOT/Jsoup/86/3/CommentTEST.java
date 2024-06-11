import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;

public class CommentTest {

 @Test
 public void testCommentCreation() {
 Comment comment = new Comment("This is a test comment");
 Assert.assertEquals("This is a test comment", comment.getData());
 }

 @Test
 public void testCommentCreationWithEmptyString() {
 Comment comment = new Comment("");
 Assert.assertEquals("", comment.getData());
 }

 @Test
 public void testCommentCreationWithNullData() {
 // Should throw an exception
 Assert.assertThrows(NullPointerException.class, () -> new Comment(null));
 }

 @Test
 public void testCommentNodeName() {
 Comment comment = new Comment("test comment");
 Assert.assertEquals("#comment", comment.nodeName());
 }

 @Test
 public void testIsXmlDeclarationFalse() {
 Comment comment = new Comment("This is a test comment");
 Assert.assertFalse(comment.isXmlDeclaration());
 }

 @Test
 public void testIsXmlDeclarationTrue() {
 Comment comment = new Comment("!--<?xml version='1.0' encoding='UTF-8'?-->");
 Assert.assertTrue(comment.isXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclarationNull() {
 Comment comment = new Comment("This is a test comment");
 Assert.assertNull(comment.asXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclarationValidXmlDeclaration() {
 Comment comment = new Comment("!--<?xml version='1.0' encoding='UTF-8'?-->");
 XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
 Assert.assertNotNull(xmlDeclaration);
 Assert.assertEquals("xml version='1.0' encoding='UTF-8'", xmlDeclaration.getDeclaration());
 }

 @Test
 public void testOuterHtml() {
 Document doc = Jsoup.parse("<html><head><!-- This is a test comment --></head><body></body></html>");
 Comment comment = doc.selectFirst("html head comment");
 Assert.assertEquals("<!--This is a test comment-->", comment.outerHtml());
 }
}