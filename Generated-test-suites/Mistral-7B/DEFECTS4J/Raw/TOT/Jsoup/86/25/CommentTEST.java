import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.*;

public class CommentTest {

 // Test constructor with valid data
 @Test
 public void testCommentConstructor() {
 String data = "This is a comment";
 Comment comment = new Comment(data);
 assertEquals("#comment", comment.nodeName());
 assertEquals(data, comment.getData());
 }

 // Test constructor with empty data
 @Test
 public void testCommentConstructorEmptyData() {
 Comment comment = new Comment("");
 assertEquals("#comment", comment.nodeName());
 assertNull(comment.getData());
 }

 // Test constructor with deprecated method and valid data
 @Test
 public void testCommentConstructorDeprecated() {
 String data = "This is a comment";
 Comment comment = new Comment(data, null);
 assertEquals("#comment", comment.nodeName());
 assertEquals(data, comment.getData());
 }

 // Test constructor with deprecated method and empty data
 @Test
 public void testCommentConstructorDeprecatedEmptyData() {
 Comment comment = new Comment(null, null);
 assertEquals("#comment", comment.nodeName());
 assertNull(comment.getData());
 }

 // Test getData() method
 @Test
 public void testGetData() {
 String data = "This is a comment";
 Comment comment = new Comment(data);
 assertEquals(data, comment.getData());
 }

 // Test isXmlDeclaration() method with valid XML declaration
 @Test
 public void testIsXmlDeclarationValidXmlDeclaration() {
 String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 Comment comment = new Comment(data);
 assertTrue(comment.isXmlDeclaration());
 }

 // Test isXmlDeclaration() method with comment and not XML declaration
 @Test
 public void testIsXmlDeclarationNotXmlDeclaration() {
 String data = "This is a comment";
 Comment comment = new Comment(data);
 assertFalse(comment.isXmlDeclaration());
 }

 // Test asXmlDeclaration() method with valid XML declaration
 @Test
 public void testAsXmlDeclarationValidXmlDeclaration() {
 String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 Comment comment = new Comment(data);
 XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
 assertNotNull(xmlDeclaration);
 }

 // Test asXmlDeclaration() method with comment and not XML declaration
 @Test
 public void testAsXmlDeclarationNotXmlDeclaration() {
 String data = "This is a comment";
 Comment comment = new Comment(data);
 assertNull(comment.asXmlDeclaration());
 }

 // Test outerHtml() method
 @Test
 public void testOuterHtml() {
 String data = "This is a comment";
 Comment comment = new Comment(data);
 assertEquals("<!--" + data + "-->", comment.outerHtml());
 }

 // Test isXmlDeclaration() method with valid XML declaration and attributes
 @Test
 public void testIsXmlDeclarationWithAttributes() {
 String data = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" SYSTEM\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
                 "<!--\n" +
                 "title = 'Test Page'\n" +
                 "class = 'test-page'\n" +
                 "-->";
 Comment comment = new Comment(data);
 assertTrue(comment.isXmlDeclaration());
 }

 // Test asXmlDeclaration() method with valid XML declaration and attributes
 @Test
 public void testAsXmlDeclarationWithAttributes() {
 String data = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" SYSTEM\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
                 "<!--\n" +
                 "title = 'Test Page'\n" +
                 "class = 'test-page'\n" +
                 "-->";
 Comment comment = new Comment(data);
 XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
 assertNotNull(xmlDeclaration);
 Element rootElement = xmlDeclaration.getDocument().rootElement();
 List<Attribute> attributes = rootElement.attributes();
 assertEquals(2, attributes.size());
 Attribute titleAttribute = attributes.get(0);
 assertEquals("title", titleAttribute.getKey());
 assertEquals("Test Page", titleAttribute.getValue());
 Attribute classAttribute = attributes.get(1);
 assertEquals("class", classAttribute.getKey());
 assertEquals("test-page", classAttribute.getValue());
 }

 // Test outerHtml() method with indentation and OutputSettings
 @Test
 public void testOuterHtmlWithIndentationAndOutputSettings() throws IOException {
 String data = "This is a comment";
 Comment comment = new Comment(data);
 Document document = Jsoup.parseBodyFragment("<html><body></body></html>");
 document.appendElement("comment", comment);
 document.outputSettings().prettyPrint(true);
 assertEquals("<html>\n<body>\n<!--" + data + "-->\n</body>\n</html>", comment.outerHtml(document.outputSettings(), 0));
 }

 // Test asXmlDeclaration() method with invalid XML declaration
 @Test (expected = IllegalArgumentException.class)
 public void testAsXmlDeclarationInvalidXmlDeclaration() {
 String data = "!xml version=\"1.0\"";
 Comment comment = new Comment(data);
 comment.asXmlDeclaration();
 }
}