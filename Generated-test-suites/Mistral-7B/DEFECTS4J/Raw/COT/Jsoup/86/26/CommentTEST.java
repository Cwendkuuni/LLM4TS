import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testCommentConstructor() {
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCommentDeprecatedConstructor() {
        Comment comment = new Comment("This is a comment", "baseUri");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCommentOuterHtmlHead() throws IOException {
        Comment comment = new Comment("This is a comment");
        StringBuilder accum = new StringBuilder();
        comment.outerHtmlHead(accum, 0, null);
        assertEquals("<!--This is a comment-->", accum.toString());
    }

    @Test
    public void testCommentOuterHtmlTail() throws IOException {
        Comment comment = new Comment("This is a comment");
        StringBuilder accum = new StringBuilder();
        comment.outerHtmlTail(accum, 0, null);
        assertEquals("", accum.toString());
    }

    @Test
    public void testCommentIsXmlDeclaration() {
        Comment comment = new Comment("<!-- This is an XML declaration -->");
        assertTrue(comment.isXmlDeclaration());

        Comment comment2 = new Comment("This is a comment");
        assertFalse(comment2.isXmlDeclaration());
    }

    @Test
    public void testCommentAsXmlDeclaration() {
        Comment comment = new Comment("<?xml version='1.0' encoding='UTF-8' standalone='yes'?>\n<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Hello, world!</p>\n</body>\n</html>");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();

        assertEquals("version='1.0'", xmlDeclaration.getVersion());
        assertEquals("UTF-8", xmlDeclaration.getEncoding());
        assertTrue(xmlDeclaration.isStandalone());

        assertNull(comment2.asXmlDeclaration());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCommentAsXmlDeclarationInvalidXml() {
        Comment comment = new Comment("<?xml vesion='1.0' encoding='UTF-8' standalone='yes'?>\n<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Hello, world!</p>\n</body>\n<!-- This is not a valid XML declaration -->");
        comment.asXmlDeclaration();
    }
}