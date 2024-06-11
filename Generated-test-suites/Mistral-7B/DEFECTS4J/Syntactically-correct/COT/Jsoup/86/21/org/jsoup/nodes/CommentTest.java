import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class CommentTest {

 private Comment comment;

 @Test
 public void testCommentConstructorWithData() {
 comment = new Comment("This is a comment");
 }

 @Test
 public void testCommentConstructorWithDataAndBaseUri() {
 comment = new Comment("This is a comment", null);
 }

 @Test
 public void testNodeName() {
 comment = new Comment("Test comment");
 assertEquals("#comment", comment.nodeName());
 }

 @Test
 public void testGetData() {
 comment = new Comment("Test comment");
 assertEquals("Test comment", comment.getData());
 }

 @Test
 public void testOuterHtmlHead() throws Exception {
 String expectedResult = "<!--Test comment-->";
 Appendable accum = new StringBuilder();
 comment.outerHtmlHead(accum, 0, null);
 assertEquals(expectedResult, accum.toString());
 }

 @Test
 public void testOuterHtmlTail() throws Exception {
 String expectedResult = "";
 Appendable accum = new StringBuilder();
 comment.outerHtmlTail(accum, 0, null);
 assertEquals(expectedResult, accum.toString());
 }

 @Test
 public void testToString() {
 comment = new Comment("Test comment");
 assertEquals("<!--Test comment-->", comment.toString());
 }

 @Test
 public void testIsXmlDeclaration() {
 comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
 assertTrue(comment.isXmlDeclaration());
 }

 @Test
 public void testIsXmlDeclarationWithInvalidData() {
 comment = new Comment("This is not an XML declaration");
 assertFalse(comment.isXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclarationWithValidData() {
 comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
 XmlDeclaration expectedResult = new XmlDeclaration("1.0", true);
 XmlDeclaration actualResult = comment.asXmlDeclaration();
 assertNotNull(actualResult);
 assertEquals(expectedResult.toString(), actualResult.toString());
 }

 @Test
 public void testAsXmlDeclarationWithInvalidData() {
 comment = new Comment("This is a comment");
 assertNull(comment.asXmlDeclaration());
 }
}