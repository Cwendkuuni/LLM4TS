import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.*;

public class CommentTest {

 private Comment comment;

 @Before
 public void setUp() {
 comment = new Comment("This is a comment");
 }

 @Test
 public void testCreateComment() {
 Comment comment = new Comment("This is a comment");
 assertEquals("This is a comment", comment.getData());
 }

 @Test
 public void testCreateCommentWithBaseUri() {
 Comment comment = new Comment("This is a comment", "http://test.com");
 assertEquals("This is a comment", comment.getData());
 assertEquals("http://test.com", comment.baseUri());
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
 public void testIsXmlDeclarationFalse() {
 assertFalse(comment.isXmlDeclaration());
 }

 @Test
 public void testIsXmlDeclarationTrue() {
 Comment xmlComment = new Comment("<?xml version='1.0' encoding='UTF-8'?>\n<comment>This is a comment</comment>");
 assertTrue(xmlComment.isXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclarationNull() {
 assertNull(comment.asXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclarationValidXmlDeclaration() {
 Comment xmlComment = new Comment("<?xml version='1.0' encoding='UTF-8'?>\n<comment>This is a comment</comment>");
 XmlDeclaration xmlDecl = xmlComment.asXmlDeclaration();
 assertNotNull(xmlDecl);
 assertEquals("<?xml version='1.0' encoding='UTF-8'", xmlDecl.getContent().toString());
 }
}