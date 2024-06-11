import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CommentTest {

 @Test
 public void testCommentCreation() {
 // Test constructor with valid data
 Comment comment = new Comment("This is a test comment");
 // Test constructor with deprecated baseUri
 Comment commentWithBaseUri = new Comment("This is a test comment", "http://example.com");
 // Test nodeName method
 assertEquals("#comment", comment.nodeName());
 commentWithBaseUri.nodeName(); // deprecated, does nothing
 // Test getData method
 assertEquals("This is a test comment", comment.getData());
 commentWithBaseUri.getData(); // deprecated, returns coreValue()
 }

 @Test
 public void testCommentOuterHtml() throws Exception {
 // Test outerHtmlHead method
 Appendable accum = new StringBuilder();
 Comment comment = new Comment("This is a test comment");
 comment.outerHtmlHead(accum, 0, null);
 assertEquals("<!--This is a test comment-->", accum.toString());
 // Test outerHtmlTail method
 comment.outerHtmlTail(accum, 0, null);
 // empty string, since we didn't call indent or append anything
 assertEquals("", accum.toString());
 // Test toString method
 assertEquals(comment.outerHtml(), comment.toString());
 }

 @Test
 public void testIsXmlDeclaration() {
 // Test positive case
 Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
 assertTrue(xmlDeclarationComment.isXmlDeclaration());
 // Test negative case
 Comment regularComment = new Comment("This is a regular comment");
 assertFalse(regularComment.isXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclaration() {
 // Test positive case
 Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
 XmlDeclaration xmlDeclaration = xmlDeclarationComment.asXmlDeclaration();
 assertNotNull(xmlDeclaration);
 // Test null case
 Comment emptyComment = new Comment("");
 xmlDeclaration = xmlDeclarationComment.asXmlDeclaration();
 assertNull(xmlDeclaration);
 // Test invalid XML declaration
 Comment invalidXmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding UTF-8?>");
 xmlDeclaration = xmlDeclarationComment.asXmlDeclaration();
 assertNull(xmlDeclaration);
 }
}