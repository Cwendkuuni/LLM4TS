import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.*;

public class CommentTest {

 private Document doc;

 @Before
 public void setUp() {
 doc = Jsoup.parse("", Parser.htmlParser());
 }

 @Test
 public void testCommentCreation() {
 Comment comment = new Comment("This is a comment.");
 assertEquals("#comment", comment.nodeName());
 assertEquals("This is a comment.", comment.getData());
 }

 @Test
 public void testEmptyCommentCreation() {
 Comment comment = new Comment("");
 assertEquals("#comment", comment.nodeName());
 assertEquals("", comment.getData());
 }

 @Test
 public void testCommentCreationWithBaseUri() {
 Comment comment = new Comment("This is a comment.", "/baseuri");
 assertEquals("#comment", comment.nodeName());
 assertEquals("This is a comment.", comment.getData());
 assertEquals("/baseuri", comment.baseUri());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCommentCreationWithNullData() {
 Comment comment = new Comment(null);
 fail("Should throw IllegalArgumentException with null data.");
 }

 @Test
 public void testCommentOuterHtml() {
 Comment comment = new Comment("This is a comment.");
 StringBuilder sb = new StringBuilder();
 try {
 comment.outerHtmlHead(sb, 0, null);
 comment.outerHtmlTail(sb, 0, null);
 } catch (IOException e) {
 fail("Should not throw IOException.");
 }
 assertEquals("<!--This is a comment-->", sb.toString());
 }

 @Test
 public void testCommentToString() {
 Comment comment = new Comment("This is a comment.");
 assertEquals("<!--This is a comment-->", comment.toString());
 }

 @Test
 public void testCommentIsXmlDeclaration() {
 Comment xmlComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!-- Comment -->");
 assertTrue(xmlComment.isXmlDeclaration());
 Comment nonXmlComment = new Comment("This is a comment.");
 assertFalse(nonXmlComment.isXmlDeclaration());
 }

 @Test
 public void testCommentAsXmlDeclaration() {
 Comment xmlComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!-- Comment -->");
 XmlDeclaration xmlDeclaration = xmlComment.asXmlDeclaration();
 assertNotNull(xmlDeclaration);
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.toString());
 Comment nonXmlComment = new Comment("This is a comment.");
 assertNull(nonXmlComment.asXmlDeclaration());
 }
}