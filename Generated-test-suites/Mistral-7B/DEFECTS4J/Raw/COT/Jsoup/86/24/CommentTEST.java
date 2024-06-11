import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Comment;
import static org.jsoup.helper.W3C.s;

public class CommentTest {

 private Comment comment;

 @Test
 public void testCommentConstructor() {
 comment = new Comment("This is a comment");
 Assert.assertEquals("This is a comment", comment.getData());
 }

 @Test (expected = IllegalArgumentException.class)
 public void testCommentDeprecatedConstructor() {
 comment = new Comment("This is a comment", "baseUri");
 }

 @Test
 public void testNodeName() {
 Assert.assertEquals("#comment", comment.nodeName());
 }

 @Test
 public void testGetData() {
 Assert.assertEquals("This is a comment", comment.getData());
 }

 @Test
 public void testOuterHtmlHead() throws IOException {
 Appendable accum = s.append("testOutput");
 comment.outerHtmlHead(accum, 0, null);
 Assert.assertEquals("<!--This is a comment-->", accum.toString());
 }

 @Test
 public void testOuterHtmlTail() throws IOException {
 Appendable accum = s.append("testOutput");
 comment.outerHtmlTail(accum, 0, null);
 Assert.assertEquals("", accum.toString());
 }

 @Test
 public void testToString() {
 Assert.assertEquals("<!--This is a comment-->", comment.toString());
 }

 @Test
 public void testIsXmlDeclaration() {
 String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 comment = new Comment(xmlDeclaration);
 Assert.assertTrue(comment.isXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclarationNull() {
 Assert.assertNull(comment.asXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclarationValid() throws IOException {
 String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><element/>";
 comment = new Comment(xmlDeclaration);
 XmlDeclaration xmlDecl = comment.asXmlDeclaration();
 Assert.assertNotNull(xmlDecl);
 Assert.assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDecl.getData());
 }

 @Test (expected = IllegalArgumentException.class)
 public void testAsXmlDeclarationInvalid() {
 String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><element/>";
 comment = new Comment(xmlDeclaration);
 }
}