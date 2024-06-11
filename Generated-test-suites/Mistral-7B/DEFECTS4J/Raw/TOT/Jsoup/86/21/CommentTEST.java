import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class CommentTest {

 private Comment comment;

 @Before
 public void setUp() {
 comment = new Comment("This is a comment");
 }

 @Test
 public void testNodeName() {
 assertEquals("#comment", comment.nodeName());
 }

 @Test
 public void testGetData() {
 assertEquals("This is a comment", comment.getData());
 }

 @Test
 public void testIsXmlDeclarationWithSimpleComment() {
 assertFalse(comment.isXmlDeclaration());
 }

 @Test
 public void testIsXmlDeclarationWithXmlDeclarationComment() {
 Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
 assertTrue(xmlDeclarationComment.isXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclarationWithSimpleComment() {
 assertNull(comment.asXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclarationWithXmlDeclarationComment() {
 XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
 assertNotNull(xmlDeclaration);
 }

 @Test
 public void testOuterHtmlHead() throws IOException {
 Document doc = Jsoup.parseBodyElements(comment.outerHtml());
 Comment commentFromOuterHtml = (Comment) doc.get(0);
 assertNotNull(commentFromOuterHtml);
 assertEquals("<!--This is a comment-->", commentFromOuterHtml.getData());
 }

 @Test
 public void testToString() {
 assertEquals("<!--This is a comment-->", comment.toString());
 }

 @Test
 public void testCreateCommentWithBaseUri() throws IOException {
 String baseUri = "http://example.com";
 Comment commentWithBaseUri = new Comment("This is a comment", baseUri);
 assertEquals("#comment", commentWithBaseUri.nodeName());
 assertEquals("This is a comment", commentWithBaseUri.getData());
 assertEquals("http://example.com", commentWithBaseUri.baseUri());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateCommentWithNullData() {
 assertNull(new Comment(null));
 }
}