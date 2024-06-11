
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testCommentConstructor() {
        Comment comment = new Comment("test comment");

        assertEquals("test comment", comment.getData());
        assertEquals("#comment", comment.nodeName());
        assertFalse(comment.isXmlDeclaration());
        assertNull(comment.asXmlDeclaration());
    }

    @Test
    public void testCommentConstructorWithBaseUri() {
        Comment comment = new Comment("test comment", "http://example.com");

        assertEquals("test comment", comment.getData());
        assertEquals("#comment", comment.nodeName());
        assertFalse(comment.isXmlDeclaration());
        assertNull(comment.asXmlDeclaration());
    }

    @Test
    public void testGetData() {
        Comment comment = new Comment("test comment");

        assertEquals("test comment", comment.getData());
    }

    @Test
    public void testOuterHtmlHead() {
        Comment comment = new Comment("test comment");

        StringBuilder sb = new StringBuilder();
        try {
            comment.outerHtmlHead(sb, 0, new Document.OutputSettings());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }

        assertEquals("<!--test comment-->", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        Comment comment = new Comment("test comment");

        StringBuilder sb = new StringBuilder();
        try {
            comment.outerHtmlTail(sb, 0, new Document.OutputSettings());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }

        assertEquals("", sb.toString());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("test comment");

        assertEquals("<!--test comment-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment1 = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        Comment comment2 = new Comment("<!DOCTYPE html>");

        assertTrue(comment1.isXmlDeclaration());
        assertFalse(comment2.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment1 = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        Comment comment2 = new Comment("<!DOCTYPE html>");
        Comment comment3 = new Comment("<!ENTITY example 'text'>");

        assertNull(comment2.asXmlDeclaration());

        XmlDeclaration xmlDeclaration1 = comment1.asXmlDeclaration();
        assertNotNull(xmlDeclaration1);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration1.outerHtml());

        XmlDeclaration xmlDeclaration2 = comment3.asXmlDeclaration();
        assertNotNull(xmlDeclaration2);
        assertEquals("<!ENTITY example 'text'>", xmlDeclaration2.outerHtml());
        assertEquals("example", xmlDeclaration2.tagName());
        assertFalse(xmlDeclaration2.isProcessingInstruction());
    }
}
