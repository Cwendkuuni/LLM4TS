import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.*;

public class CommentTest {

 @Test
 public void testCreateComment() {
 Comment comment = new Comment("This is a test comment.");
 assertEquals("This is a test comment.", comment.getData());
 }

 @Test
 public void testCreateDeprecatedComment() {
 Comment comment = new Comment("This is a test comment.", "baseUri");
 assertEquals("This is a test comment.", comment.getData());
 }

 @Test
 public void testNodeName() {
 Comment comment = new Comment("test comment");
 assertEquals("#comment", comment.nodeName());
 }

 @Test
 public void testGetData() {
 Comment comment = new Comment("test <script>alert('XSS')</script> comment");
 assertEquals("test <script>alert('XSS')</script>", comment.getData());
 }

 @Test(expected = IOException.class)
 public void testOuterHtmlHeadThrowsIOException() throws IOException {
 Comment comment = new Comment("test comment");
 Document doc = Jsoup.parse("<html><head><body><p><!--test--></p></body></head></html>", "", Parser.htmlParser());
 doc.select("p comment").first().replaceWith(comment);
 doc.outputSettings().prettyPrint(false);
 doc.outputSettings().charset("UTF-8");
 doc.outputSettings().format("html");
 doc.outputSettings().indentAmount(0);
 doc.outputSettings().escapeMode(Document.OutputSettings.Mode.html);
 String html = doc.toString();
 comment.outerHtmlHead(null, 0, doc.outputSettings());
 fail("Expected IOException but no exception was thrown.");
 }

 @Test
 public void testIsXmlDeclaration() {
 Comment comment1 = new Comment("<!-- This is an XML declaration -->");
 assertTrue(comment1.isXmlDeclaration());
 Comment comment2 = new Comment("This is a test comment.");
 assertFalse(comment2.isXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclarationNull() {
 Comment comment = new Comment("This is a test comment.");
 assertNull(comment.asXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclarationValidXmlDeclaration() {
 Comment comment = new Comment("<?xml version='1.0' encoding='UTF-8'?>\n<root/>");
 XmlDeclaration decl = comment.asXmlDeclaration();
 assertNotNull(decl);
 assertEquals("<?xml version='1.0' encoding='UTF-8'?>", decl.getDeclaredEncoding());
 assertEquals(true, decl.isStandalone());
 assertEquals("root", decl.getName());
 }

 @Test
 public void testAsXmlDeclarationInvalidXmlDeclaration() {
 Comment comment = new Comment("<?xml version='1.0' encoding='UTF-8'?>\n<root>\n<invalid/>");
 XmlDeclaration decl = comment.asXmlDeclaration();
 assertNull(decl);
 }
}