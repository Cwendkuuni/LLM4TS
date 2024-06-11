import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CommentTest {

 // Test constructor method
 @Test
 public void testCommentConstructor() {
 Comment comment = new Comment("This is a comment");
 assertEquals("#comment", comment.nodeName());
 assertEquals("This is a comment", comment.getData());
 }

 // Test deprecated constructor method
 @Test
 public void testCommentConstructorDeprecated() {
 Comment comment = new Comment("This is a comment", null);
 assertEquals("#comment", comment.nodeName());
 assertEquals("This is a comment", comment.getData());
 }

 // Test isXmlDeclaration method
 @Test
 public void testIsXmlDeclaration() {
 Comment comment = new Comment("!-- This is a comment --");
 assertTrue(comment.isXmlDeclaration());

 Comment normalComment = new Comment("This is a comment");
 assertFalse(normalComment.isXmlDeclaration());
 }

 // Test asXmlDeclaration method
 @Test
 public void testAsXmlDeclaration() {
 String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 Comment xmlDeclarationComment = new Comment(xmlDeclaration);
 XmlDeclaration xmlDecl = xmlDeclarationComment.asXmlDeclaration();
 assertNotNull(xmlDecl);
 assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDecl.data());
 }

 // Test asXmlDeclaration method when input is not valid XML declaration
 @Test
 public void testAsXmlDeclarationInvalidInput() {
 Comment invalidXmlDeclarationComment = new Comment("Invalid xml declaration");
 assertNull(invalidXmlDeclarationComment.asXmlDeclaration());
 }

 // Test outerHtml method
 @Test
 public void testOuterHtml() {
 Comment comment = new Comment("This is a comment");
 assertEquals("<!--This is a comment-->", comment.outerHtml());
 }

 // Test toString method
 @Test
 public void testToString() {
 Comment comment = new Comment("This is a comment");
 assertEquals("<!--This is a comment-->", comment.toString());
 }

 // Edge case test for empty string as comment data
 @Test
 public void testEmptyCommentData() {
 Comment emptyComment = new Comment("");
 assertEquals("#comment", emptyComment.nodeName());
 assertEquals("", emptyComment.getData());
 }
}