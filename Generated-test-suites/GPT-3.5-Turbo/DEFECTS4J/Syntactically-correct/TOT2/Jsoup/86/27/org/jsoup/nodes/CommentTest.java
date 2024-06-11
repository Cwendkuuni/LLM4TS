import static org.junit.Assert.*;
import org.junit.Test;

public class CommentTest {

    // Test case for Comment(String data)
    @Test
    public void testCommentWithData() {
        Comment comment = new Comment("Test comment");
        assertEquals("Test comment", comment.getData());
        assertEquals("#comment", comment.nodeName());
    }

    // Test case for Comment(String data, String baseUri)
    @Test
    public void testCommentWithDataAndBaseUri() {
        Comment comment = new Comment("Test comment", "http://example.com");
        assertEquals("Test comment", comment.getData());
        assertEquals("#comment", comment.nodeName());
    }

    // Test case for isXmlDeclaration()
    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("<!DOCTYPE html>");
        assertTrue(comment.isXmlDeclaration());
    }

    // Test case for asXmlDeclaration()
    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\"?>");
        XmlDeclaration declaration = comment.asXmlDeclaration();
        assertNotNull(declaration);
        assertEquals("xml", declaration.nodeName());
        assertEquals("1.0", declaration.attributes().get("version"));
    }

    // Test case for outerHtmlHead()
    @Test
    public void testOuterHtmlHead() {
        // Testing for IOException handled scenario
        Comment comment = new Comment("Test comment");
        StringBuilder sb = new StringBuilder();
        try {
            comment.outerHtmlHead(sb, 0, new Document.OutputSettings());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
        assertTrue(sb.toString().contains("<!--Test comment-->"));
    }

    // Test case for outerHtmlTail()
    @Test
    public void testOuterHtmlTail() {
        Comment comment = new Comment("Test comment");
        StringBuilder sb = new StringBuilder();
        comment.outerHtmlTail(sb, 0, new Document.OutputSettings());
        assertEquals("", sb.toString());
    }

    // Test case for toString()
    @Test
    public void testToString() {
        Comment comment = new Comment("Test comment");
        assertEquals("<!--Test comment-->", comment.toString());
    }
}