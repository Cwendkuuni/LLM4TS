import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CommentTest {

 @Test
 public void testCommentCreation() {
 // Test creating a new comment with valid data
 String data = "This is a comment.";
 Comment comment = new Comment(data);
 assertEquals("#comment", comment.nodeName());
 assertEquals(data, comment.getData());
 }

 @Test
 public void testCommentCreationWithBaseUri() {
 // Test creating a new comment with deprecated constructor and null baseUri
 String data = "This is a comment.";
 Comment comment = new Comment(data, null);
 assertEquals("#comment", comment.nodeName());
 assertEquals(data, comment.getData());
 }

 @Test
 public void testCommentOuterHtml() throws IOException {
 // Test generating outer HTML for a comment
 Comment comment = new Comment("This is a comment.");
 Document doc = Jsoup.parse("<html><head><title>Test</title></head><body>", "", Parser.htmlParser());
 comment.outerHtmlHead(doc.body().appendElement("p"), 0, null);
 comment.outerHtmlTail(doc.body().appendElement("p"), 0, null);
 String expected = "<p><!--This is a comment--></p>";
 assertEquals(expected, doc.body().html());
 }

 @Test
 public void testCommentToString() {
 // Test generating outer HTML as a string for a comment
 Comment comment = new Comment("This is a comment.");
 assertEquals("<!--This is a comment-->", comment.toString());
 }

 @Test
 public void testIsXmlDeclarationFalse() {
 // Test checking if a comment is an XML declaration when it isn't
 Comment comment = new Comment("This is a comment.");
 assertFalse(comment.isXmlDeclaration());
 }

 @Test
 public void testIsXmlDeclarationTrue() {
 // Test checking if a comment is an XML declaration when it is
 Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
 assertTrue(comment.isXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclarationNull() {
 // Test casting a comment to an XML declaration when it can't be cast
 Comment comment = new Comment("This is a comment.");
 assertNull(comment.asXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclarationValid() throws IOException {
 // Test casting a comment to an XML declaration when it can be cast
 String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE html SYSTEM \"about:legacy-compat\">\n<html><head><title>Test</title></head><body>";
 Comment comment = new Comment(xmlDeclaration);
 XmlDeclaration xmlDecl = comment.asXmlDeclaration();
 assertNotNull(xmlDecl);
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDecl.toString());
 }
}