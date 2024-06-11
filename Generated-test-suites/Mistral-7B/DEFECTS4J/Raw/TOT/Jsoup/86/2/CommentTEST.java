import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {

 @Test
 public void testCommentCreation() {
 Comment comment = new Comment("This is a comment");
 assertEquals("This is a comment", comment.getData());
 }

 @Test
 public void testCommentCreationWithBaseUri() {
 Comment comment = new Comment("This is a comment", "/test");
 assertEquals("This is a comment", comment.getData());
 assertNull(comment.getBaseUri());
 }

 @Test
 public void testNodeName() {
 Comment comment = new Comment("Comment data");
 assertEquals("#comment", comment.nodeName());
 }

 @Test
 public void testGetData() {
 Comment comment = new Comment("This is a comment");
 assertEquals("This is a comment", comment.getData());
 }

 @Test
 public void testIsXmlDeclaration() {
 Comment comment1 = new Comment("This is a comment");
 assertFalse(comment1.isXmlDeclaration());

 Comment comment2 = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<!-- This is a comment -->\n</root>");
 assertTrue(comment2.isXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclaration() {
 Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<!-- This is a comment -->\n</root>");
 XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
 assertNotNull(xmlDeclaration);
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n", xmlDeclaration.getDeclarationData());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidXmlDeclaration() {
 Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<!-- This is an error -->");
 assertNull(comment.asXmlDeclaration());
 }

 @Test
 public void testOuterHtml() {
 Comment comment = new Comment("This is a comment");
 try {
 Document doc = Jsoup.parse("<html><head><body><!-- This is a comment --></body></head></html>", null, Parser.htmlParser());
 assertEquals(doc.body().outerHtml(), comment.outerHtml(null, 0, null));
 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException should not be thrown");
 }
 }

 @Test
 public void testIsXmlDeclarationWithEmptyData() {
 Comment comment = new Comment("");
 assertFalse(comment.isXmlDeclaration());
 }

 @Test
 public void testIsXmlDeclarationWithCommentOnly() {
 Comment comment = new Comment("<!-- Comment only -->");
 assertFalse(comment.isXmlDeclaration());
 }

 @Test
 public void testIsXmlDeclarationWithQuotedXmlDeclaration() {
 Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
 assertTrue(comment.isXmlDeclaration());
 }

 @Test
 public void testIsXmlDeclarationWithUnquotedXmlDeclaration() {
 Comment comment = new Comment("<?xml version=1.0 encoding=UTF-8?>");
 assertTrue(comment.isXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclarationWithEmptyData() {
 Comment comment = new Comment("");
 assertNull(comment.asXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclarationWithCommentOnly() {
 Comment comment = new Comment("<!-- Comment only -->");
 assertNull(comment.asXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclarationWithInvalidXmlDeclaration() {
 Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>");
 assertNull(comment.asXmlDeclaration());
 }

 @Test
 public void testAsXmlDeclarationWithValidXmlDeclaration() {
 Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element attribute=\"value\"></element>\n<!-- Comment -->\n</root>");
 XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
 assertNotNull(xmlDeclaration);
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n", xmlDeclaration.getDeclarationData());
 }

 @Test
 public void testAsXmlDeclarationWithXmlDeclarationWithAttributes() {
 Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><element attribute=\"value\">Data</element></root>");
 XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
 assertNotNull(xmlDeclaration);
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n", xmlDeclaration.getDeclarationData());
 assertEquals(2, xmlDeclaration.attributes().size());
 assertEquals("http://www.w3.org/2001/XMLSchema-instance", xmlDeclaration.xmlns("xsi"));
 assertEquals("http://www.w3.org/2001/XMLSchema", xmlDeclaration.xmlns("xsd"));
 }
}