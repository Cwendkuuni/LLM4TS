import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {

 // Test constructor with valid data
 @Test
 public void testCommentConstructorWithValidData() {
 String data = "This is a comment";
 Comment comment = new Comment(data);
 assertEquals("#comment", comment.nodeName());
 assertEquals(data, comment.getData());
 }

 // Test constructor with deprecated data and baseUri
 @Test
 public void testCommentConstructorWithDeprecatedDataAndBaseUri() {
 String data = "This is a comment";
 Comment comment = new Comment(data, null);
 assertEquals("#comment", comment.nodeName());
 assertEquals(data, comment.getData());
 }

 // Test isXmlDeclaration method with valid xml declaration comment
 @Test
 public void testIsXmlDeclarationWithValidXmlDeclaration() {
 String xmlDeclaration = "<?xml version='1.0' encoding='UTF-8'?>";
 Comment comment = new Comment(xmlDeclaration);
 assertTrue(comment.isXmlDeclaration());
 }

 // Test isXmlDeclaration method with invalid comment
 @Test
 public void testIsXmlDeclarationWithInvalidComment() {
 String comment = "This is a comment";
 Comment commentObj = new Comment(comment);
 assertFalse(commentObj.isXmlDeclaration());
 }

 // Test asXmlDeclaration method with valid xml declaration comment
 @Test
 public void testAsXmlDeclarationWithValidXmlDeclaration() {
 String xmlDeclaration = "<?xml version='1.0' encoding='UTF-8'?>";
 Comment comment = new Comment(xmlDeclaration);
 XmlDeclaration xmlDecl = comment.asXmlDeclaration();
 assertNotNull(xmlDecl);
 }

 // Test asXmlDeclaration method with invalid comment
 @Test (expected = IllegalArgumentException.class)
 public void testAsXmlDeclarationWithInvalidComment() {
 Comment comment = new Comment("This is a comment");
 comment.asXmlDeclaration();
 }

 // Test outerHtml method with pretty print
 @Test
 public void testOuterHtmlWithPrettyPrint() throws IOException {
 String data = "This is a comment";
 Comment comment = new Comment(data);
 StringBuilder sb = new StringBuilder();
 comment.outerHtml(sb, 0, null);
 String expected = "<!--" + data + "-->";
 assertEquals(expected, sb.toString());
 }

 // Test outerHtml method without pretty print
 @Test
 public void testOuterHtmlWithoutPrettyPrint() throws IOException {
 String data = "This is a comment";
 Comment comment = new Comment(data);
 StringBuilder sb = new StringBuilder();
 comment.outerHtml(sb, 0, null);
 String expected = "<!--" + data + "-->";
 assertEquals(expected, sb.toString());
 // Test if the output is the same with and without pretty print
 StringBuilder sb1 = new StringBuilder();
 comment.outerHtmlHead(sb1, 0, null);
 StringBuilder sb2 = new StringBuilder();
 comment.outerHtmlTail(sb2, 0, null);
 StringBuilder sb3 = new StringBuilder();
 sb1.append(sb2);
 sb3.append(expected);
 assertEquals(sb3.toString(), sb1.toString());
 }
}