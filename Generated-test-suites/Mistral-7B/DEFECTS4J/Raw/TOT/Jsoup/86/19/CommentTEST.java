import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Assert;
import org.junit.Test;

public class CommentTest {

 @Test
 public void testCreateCommentWithData() {
 String data = "This is a comment";
 Comment comment = new Comment(data);
 Assert.assertEquals("#comment", comment.nodeName());
 Assert.assertEquals(data, comment.getData());
 }

 @Test
 public void testCreateCommentDeprecatedConstructor() {
 String data = "This is a comment";
 Comment comment = new Comment(data, null);
 Assert.assertEquals("#comment", comment.nodeName());
 Assert.assertEquals(data, comment.getData());
 }

 @Test
 public void testCommentIsXmlDeclaration() {
 String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 Comment comment = new Comment(xmlDeclaration);
 Assert.assertTrue(comment.isXmlDeclaration());
 }

 @Test(expected = IllegalStateException.class)
 public void testCommentIsNotXmlDeclaration() {
 String data = "This is a comment";
 Comment comment = new Comment(data);
 comment.asXmlDeclaration();
 }

 @Test
 public void testCommentToString() {
 String data = "This is a comment";
 Comment comment = new Comment(data);
 Assert.assertEquals("<!--" + data + "-->", comment.toString());
 }

 @Test
 public void testCommentOuterHtml() throws IOException {
 String data = "This is a comment";
 Comment comment = new Comment(data);
 Document doc = Jsoup.parse("<root>" + comment.outerHtml() + "</root>");
 Assert.assertEquals(data, doc.select("comment").text());
 }

 @Test
 public void testCommentIsXmlDeclarationWithAttributes() {
 String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE html SYSTEM \"html.dtd\">";
 Comment comment = new Comment(xmlDeclaration);
 XmlDeclaration xmlDecl = comment.asXmlDeclaration();
 Assert.assertNotNull(xmlDecl);
 Assert.assertEquals("html", xmlDecl.getDoctype().getName());
 Assert.assertEquals(2, xmlDecl.getAttributes().size());
 Attribute xmlVersion = xmlDecl.getAttributes().get("version");
 Assert.assertEquals("1.0", xmlVersion.getValue());
 Attribute encoding = xmlDecl.getAttributes().get("encoding");
 Assert.assertEquals("UTF-8", encoding.getValue());
 }
}