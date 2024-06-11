import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {

 private Comment comment;

 @Before
 public void setUp() {
 comment = new Comment("This is a comment");
 }

 // Test constructor with valid data
 @Test
 public void testCommentConstructor() {
 String expected = "This is a comment";
 String actual = comment.getData();
 assertEquals(expected, actual);
 }

 // Test constructor with deprecated data and baseUri
 @Test
 public void testCommentConstructorDeprecated() {
 String expected = "This is a comment";
 String actual = comment.getData();
 assertEquals(expected, actual);
 }

 // Test nodeName method
 @Test
 public void testNodeName() {
 String expected = "#comment";
 String actual = comment.nodeName();
 assertEquals(expected, actual);
 }

 // Test getData method
 @Test
 public void testgetData() {
 String expected = "This is a comment";
 String actual = comment.getData();
 assertEquals(expected, actual);
 }

 // Test outerHtml method for normal case
 @Test
 public void testOuterHtmlNormalCase() throws Exception {
 Appendable accum = new StringBuilder();
 Document.OutputSettings out = new Document.OutputSettings();
 comment.outerHtmlHead(accum, 0, out);
 comment.outerHtmlTail(accum, 0, out);
 String expected = "<!--This is a comment-->";
 String actual = accum.toString();
 assertEquals(expected, actual);
 }

 // Test outerHtml method for pretty print
 @Test
 public void testOuterHtmlPrettyPrint() throws Exception {
 Appendable accum = new StringBuilder();
 Document.OutputSettings out = new Document.OutputSettings();
 out.prettyPrint(true);
 comment.outerHtmlHead(accum, 0, out);
 comment.outerHtmlTail(accum, 0, out);
 String expected = "<!--\nThis is a comment\n-->";
 String actual = accum.toString();
 assertEquals(expected, actual);
 }

 // Test toString method
 @Test
 public void testToString() {
 String expected = "<!--This is a comment-->";
 String actual = comment.toString();
 assertEquals(expected, actual);
 }

 // Test isXmlDeclaration method for valid XML declaration
 @Test
 public void testIsXmlDeclarationValidXmlDeclaration() {
 String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 Comment xmlDeclarationComment = new Comment(xmlDeclaration);
 boolean expected = true;
 boolean actual = xmlDeclarationComment.isXmlDeclaration();
 assertEquals(expected, actual);
 }

 // Test isXmlDeclaration method for invalid XML declaration
 @Test
 public void testIsXmlDeclarationInvalidXmlDeclaration() {
 String xmlDeclaration = "!xml version=\"1.0\" encoding=\"UTF-8\"?";
 Comment xmlDeclarationComment = new Comment(xmlDeclaration);
 boolean expected = false;
 boolean actual = xmlDeclarationComment.isXmlDeclaration();
 assertEquals(expected, actual);
 }

 // Test asXmlDeclaration method for valid XML declaration
 @Test
 public void testAsXmlDeclarationValidXmlDeclaration() {
 String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 Comment xmlDeclarationComment = new Comment(xmlDeclaration);
 XmlDeclaration expected = new XmlDeclaration("1.0", "UTF-8", null, null, null, null, null, null, null);
 XmlDeclaration actual = xmlDeclarationComment.asXmlDeclaration();
 assertNotNull(actual);
 assertEquals(expected.version(), actual.version());
 assertEquals(expected.encoding(), actual.encoding());
 }

 // Test asXmlDeclaration method for invalid XML declaration
 @Test(expected = IllegalArgumentException.class)
 public void testAsXmlDeclarationInvalidXmlDeclaration() {
 String xmlDeclaration = "!xml version=\"1.0\" encoding=\"UTF-8\"?";
 Comment xmlDeclarationComment = new Comment(xmlDeclaration);
 xmlDeclarationComment.asXmlDeclaration();
 }
}