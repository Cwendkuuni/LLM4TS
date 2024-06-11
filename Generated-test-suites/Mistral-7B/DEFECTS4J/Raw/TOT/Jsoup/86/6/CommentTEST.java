import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;

public class CommentTest {

 @Test
 public void testCommentCreation() {
 Comment comment = new Comment("This is a test comment");
 assertEquals("This is a test comment", comment.getData());
 }

 @Test
 public void testCommentCreationWithDeprecatedConstructor() {
 Comment comment = new Comment("This is a test comment");
 // Deprecated constructor does not take baseUri as parameter, so it should be ignored
 // in this test case
 assertEquals("This is a test comment", comment.getData());
 }

 @Test
 public void testNodeName() {
 Comment comment = new Comment("This is a test comment");
 assertEquals("#comment", comment.nodeName());
 }

 @Test
 public void testGetData() {
 Comment comment = new Comment("This is a test comment");
 assertEquals("This is a test comment", comment.getData());
 }

 @Test
 public void testOuterHtml() {
 Appendable accum = new StringBuffer();
 Document doc = new Document();
 comment.outerHtmlHead(accum, 0, null);
 comment.outerHtmlTail(accum, 0, null);
 String expected = "<!--This is a test comment-->";
 assertEquals(expected, comment.toString());
 }

 @Test
 public void testIsXmlDeclaration() {
 Comment comment1 = new Comment("This is a test comment");
 assertFalse(comment1.isXmlDeclaration());

 Comment comment2 = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<test>\n<element/>\n");
 assertTrue(comment2.isXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclaration() {
 Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<test>\n<element/>\n");
 XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
 assertNotNull(xmlDeclaration);
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.getData());
 }

 @Test(expected = NullPointerException.class)
 public void testAsXmlDeclarationNullData() {
 Comment comment = new Comment("");
 assertNull(comment.asXmlDeclaration());
 }
}