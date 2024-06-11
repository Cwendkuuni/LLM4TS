import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CommentTest {

 // Test constructor with valid data
 @Test
 public void testCommentConstructorValidData() {
 Comment comment = new Comment("This is a comment");
 assertEquals("#comment", comment.nodeName());
 assertEquals("This is a comment", comment.getData());
 }

 // Test constructor with deprecated data and baseUri
 @Test
 public void testCommentConstructorDeprecated() {
 Comment comment = new Comment("This is a comment", "baseUri");
 assertEquals("#comment", comment.nodeName());
 assertEquals("This is a comment", comment.getData());
 }

 // Test getData method
 @Test
 public void testGetDataMethod() {
 Comment comment = new Comment("This is a comment");
 assertEquals("This is a comment", comment.getData());
 }

 // Test isXmlDeclaration method with valid XML declaration comment
 @Test
 public void testIsXmlDeclarationValidXmlDeclaration() {
 Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body>");
 assertTrue(comment.isXmlDeclaration());
 }

 // Test isXmlDeclaration method with invalid comment
 @Test
 public void testIsXmlDeclarationInvalidComment() {
 Comment comment = new Comment("This is a comment");
 assertFalse(comment.isXmlDeclaration());
 }

 // Test asXmlDeclaration method with valid XML declaration comment
 @Test
 public void testAsXmlDeclarationValidXmlDeclaration() {
 Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body>");
 XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
 assertNotNull(xmlDeclaration);
 }

 // Test asXmlDeclaration method with invalid comment
 @Test
 public void testAsXmlDeclarationInvalidComment() {
 Comment comment = new Comment("This is a comment");
 assertNull(comment.asXmlDeclaration());
 }

 // Test outerHtmlHead method with prettyPrint true
 @Test
 public void testOuterHtmlHeadWithPrettyPrintTrue() throws IOException {
 Appendable accumulator = new StringBuilder();
 Comment comment = new Comment("This is a comment");
 comment.outerHtmlHead(accumulator, 0, null);
 assertEquals("<!--This is a comment-->", accumulator.toString());
 }

 // Test outerHtmlHead method with prettyPrint false
 @Test
 public void testOuterHtmlHeadWithPrettyPrintFalse() throws IOException {
 Appendable accumulator = new StringBuilder();
 Comment comment = new Comment("This is a comment");
 comment.outerHtmlHead(accumulator, 0, null);
 assertEquals("<!--This is a comment-->", accumulator.toString());
 }

 // Test outerHtmlTail method
 @Test
 public void testOuterHtmlTail() throws IOException {
 Appendable accumulator = new StringBuilder();
 Comment comment = new Comment("This is a comment");
 comment.outerHtmlTail(accumulator, 0, null);
 assertEquals("", accumulator.toString());
 }

 // Test toString method
 @Test
 public void testToStringMethod() {
 Comment comment = new Comment("This is a comment");
 assertEquals("<!--This is a comment-->", comment.toString());
 }

}