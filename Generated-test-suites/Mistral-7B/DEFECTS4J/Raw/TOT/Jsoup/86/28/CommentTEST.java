import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.*;

public class CommentTest {

 @Test
 public void testCommentCreation() {
 Comment comment = new Comment("This is a comment.");
 assertEquals("#comment", comment.nodeName());
 assertEquals("This is a comment.", comment.getData());
 }

 @Test
 public void testIsXmlDeclarationFalse() {
 Comment comment = new Comment("This is a comment.");
 assertFalse(comment.isXmlDeclaration());
 }

 @Test
 public void testIsXmlDeclarationTrue() {
 Comment comment = new Comment("!-- xml version=\"1.0\" encoding=\"UTF-8\" --");
 assertTrue(comment.isXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclarationNull() {
 Comment comment = new Comment("This is a comment.");
 assertNull(comment.asXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclarationValid() {
 Comment comment = new Comment("!-- xml version=\"1.0\" encoding=\"UTF-8\" --");
 XmlDeclaration xmlDecl = comment.asXmlDeclaration();
 assertNotNull(xmlDecl);
 assertNotNull(xmlDecl.getVersion());
 assertNotNull(xmlDecl.getEncoding());
 }

 @Test
 public void testToString() {
 Comment comment = new Comment("This is a comment.");
 assertEquals("<!--This is a comment-->", comment.toString());
 }
}