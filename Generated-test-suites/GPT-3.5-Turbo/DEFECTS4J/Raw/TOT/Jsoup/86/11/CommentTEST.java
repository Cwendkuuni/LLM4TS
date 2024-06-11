import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testCommentConstructor() {
        Comment comment = new Comment("Test comment");

        assertEquals("Test comment", comment.getData());
    }

    @Test
    public void testCommentConstructorWithBaseUri() {
        Comment comment = new Comment("Test comment", "http://example.com");

        assertEquals("Test comment", comment.getData());
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testGetData() {
        Comment comment = new Comment("Test comment");

        assertEquals("Test comment", comment.getData());
    }

    @Test
    public void testOuterHtmlHead() {
        Comment comment = new Comment("Test comment");

        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings().prettyPrint(true);
        try {
            comment.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }

        assertEquals("<!--Test comment-->", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        Comment comment = new Comment("Test comment");

        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings().prettyPrint(true);
        try {
            comment.outerHtmlTail(accum, 0, out);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }

        assertEquals("", accum.toString());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("Test comment");

        assertEquals("<!--Test comment-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclarationWithXmlDeclarationComment() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclarationWithInvalidXmlDeclarationComment() {
        Comment comment = new Comment("<?xml version=\"1.0\"?>");

        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationWithXmlDeclarationComment() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

        XmlDeclaration declaration = comment.asXmlDeclaration();

        assertNotNull(declaration);
        assertEquals("xml", declaration.tagName());
        assertEquals(true, declaration.isProcessingInstruction());
        assertEquals("version=\"1.0\" encoding=\"UTF-8\"", declaration.getWholeDeclaration());
    }

    @Test
    public void testAsXmlDeclarationWithInvalidXmlDeclarationComment() {
        Comment comment = new Comment("<?xml version=\"1.0\"?>");

        XmlDeclaration declaration = comment.asXmlDeclaration();

        assertNull(declaration);
    }
}