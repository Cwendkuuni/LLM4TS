import org.junit.Test;
import org.junit. Assert.*;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;
import java.io.IOException;

public class CommentTest {

 private Comment createComment(String data) {
 return new Comment(data);
 }

 @Test
 public void testCommentConstructor() {
 Comment comment = createComment("This is a comment");
 String expected = "#comment";
 String actual = comment.nodeName();
 Equals(expected, actual);
 }

 @Test
 public void testCommentGetData() {
 Comment comment = createComment("This is a comment");
 String expected = "This is a comment";
 String actual = comment.getData();
 Equals(expected, actual);
 }

 @Test
 public void testIsXmlDeclarationFalse() {
 Comment comment = createComment("This is a comment");
 Boolean expected = false;
 Boolean actual = comment.isXmlDeclaration();
 Equals(expected, actual);
 }

 @Test
 public void testIsXmlDeclarationTrue() {
 Comment comment = createComment("<?xml version='1.0' encoding='UTF-8'?>");
 Boolean expected = true;
 Boolean actual = comment.isXmlDeclaration();
 Equals(expected, actual);
 }

 @Test (expected = AssertionError.class)
 public void testAsXmlDeclarationNull() {
 Comment comment = createComment("This is a comment");
 XmlDeclaration expected = null;
 XmlDeclaration actual = comment.asXmlDeclaration();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testAsXmlDeclarationValidXmlDeclaration() throws IOException {
 Comment comment = createComment("<?xml version='1.0' encoding='UTF-8'?>");
 XmlDeclaration expected = new XmlDeclaration("1.0", "UTF-8");
 XmlDeclaration actual = comment.asXmlDeclaration();
 Equals(expected.toString(), actual.toString());
 }

 @Test (expected = IOException.class)
 public void testAsXmlDeclarationInvalidXmlDeclaration() {
 Comment comment = createComment("This is not a valid xml declaration");
 comment.asXmlDeclaration();
 }

}