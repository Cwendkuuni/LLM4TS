import org.junit.Test;
import org.jsoup.nodes.Comment;
import static org.junit.Assert.*;

public class CommentTest {

 private Comment comment;

 @Test
 public void testCommentConstructor() {
 comment = new Comment("This is a comment.");
 assertEquals("This is a comment.", comment.getData());
 }

 @Test
 public void testDeprecatedCommentConstructor() {
 comment = new Comment("This is a comment.", "");
 assertEquals("This is a comment.", comment.getData());
 }

 @Test
 public void testNodeName() {
 comment = new Comment("This is a comment.");
 assertEquals("#comment", comment.nodeName());
 }

 @Test
 public void testGetData() {
 comment = new Comment("This is a comment.");
 assertEquals("This is a comment.", comment.getData());
 }

 @Test
 public void testOuterHtmlHead() throws IOException {
 Appendable accum = new StringBuilder();
 Document.OutputSettings out = new Document.OutputSettings();
 comment = new Comment("This is a comment.");
 comment.outerHtmlHead(accum, 0, out);
 assertEquals("<!--This is a comment-->", accum.toString());
 }

 @Test
 public void testOuterHtmlTail() throws IOException {
 Appendable accum = new StringBuilder();
 Document.OutputSettings out = new Document.OutputSettings();
 comment = new Comment("This is a comment.");
 comment.outerHtmlTail(accum, 0, out);
 assertTrue(accum.toString().isEmpty());
 }

 @Test
 public void testToString() {
 comment = new Comment("This is a comment.");
 assertEquals("<!--This is a comment-->", comment.toString());
 }

 @Test
 public void testIsXmlDeclaration() {
 comment = new Comment("<!-- This is an XML declaration -->");
 assertTrue(comment.isXmlDeclaration());
 comment = new Comment("This is a comment.");
 assertFalse(comment.isXmlDeclaration());
 }

 @Test(expected = IOException.class)
 public void testAsXmlDeclarationNullData() {
 comment = new Comment("");
 comment.asXmlDeclaration();
 }

 @Test
 public void testAsXmlDeclarationValidXml() {
 String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><child/>";
 comment = new Comment(xml);
 Comment xmlDecl = comment.asXmlDeclaration();
 assertNotNull(xmlDecl);
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDecl.toString());
 }

 @Test
 public void testAsXmlDeclarationInvalidXml() {
 String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"<child/>";
 comment = new Comment(xml);
 assertNull(comment.asXmlDeclaration());
 }
}