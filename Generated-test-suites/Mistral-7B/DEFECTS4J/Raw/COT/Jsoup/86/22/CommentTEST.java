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
    public void testDeprecatedCommentConstructor() {
        Comment comment = new Comment("This is a comment", "http://example.com");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testGetNodeName() {
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testgetData() {
        Comment comment = new Comment("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        Comment comment = new Comment("This is a comment");
        StringBuilder accum = new StringBuilder();
        comment.outerHtmlHead(accum, 0, null);
        assertEquals("<!--This is a comment-->", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        Comment comment = new Comment("This is a comment");
        StringBuilder accum = new StringBuilder();
        comment.outerHtmlTail(accum, 0, null);
        assertTrue(accum.toString().isEmpty());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("This is a comment");
        assertEquals("<!--This is a comment-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment1 = new Comment("This is a comment");
        assertFalse(comment1.isXmlDeclaration());

        Comment comment2 = new Comment("<?xml version='1.0' encoding='UTF-8'?>");
        assertTrue(comment2.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<?xml version='1.0' encoding='UTF-8'?>\n<root><element>Value</element></root>");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("Version: 1.0", xmlDeclaration.getVersion());
        assertEquals("Encoding: UTF-8", xmlDeclaration.getEncoding());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAsXmlDeclarationInvalidXml() {
        Comment comment = new Comment("This is a comment");
        comment.asXmlDeclaration();
    }

    @Test(expected = IOException.class)
    public void testAsXmlDeclarationIOException() {
        Comment comment = new Comment("<?xml version='1.0' encoding='UTF-8'?>\n<root><element>Value</element></root>\n<script src=\"javascript:alert('XSS')\">");
        comment.asXmlDeclaration();
    }

}