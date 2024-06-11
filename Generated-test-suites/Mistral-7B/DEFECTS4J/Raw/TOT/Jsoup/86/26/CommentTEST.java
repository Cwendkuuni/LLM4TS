import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CommentTest {

 @Test
 public void testCreateComment() {
 Comment comment = new Comment("This is a test comment");
 assertEquals("#comment", comment.nodeName());
 assertEquals("This is a test comment", comment.getData());
 }

 @Test
 public void testCreateDeprecatedComment() {
 Comment comment = new Comment("This is a test comment", null);
 assertEquals("#comment", comment.nodeName());
 assertEquals("This is a test comment", comment.getData());
 }

 @Test
 public void testEmptyComment() {
 Comment comment = new Comment("");
 assertEquals("#comment", comment.nodeName());
 assertEquals("", comment.getData());
 }

 @Test
 public void testIsXmlDeclarationFalse() {
 Comment comment = new Comment("This is a test comment");
 assertFalse(comment.isXmlDeclaration());
 }

 @Test
 public void testIsXmlDeclarationTrue() {
 Comment comment = new Comment("!--xml version=\"1.0\"--");
 assertTrue(comment.isXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclarationNull() {
 Comment comment = new Comment("This is a test comment");
 assertNull(comment.asXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclarationValid() {
 Comment comment = new Comment("!--xml version=\"1.0\" encoding=\"UTF-8\"--");
 XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
 assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.data());
 }

 @Test (expected = IOException.class)
 public void testOuterHtmlThrowsIOException() throws IOException {
 Document doc = Jsoup.parse("<html><head><!--</head>--></html>");
 Comment comment = doc.selectFirst("#comment");
 comment.outerHtmlHead(null, 0, null);
 }
}