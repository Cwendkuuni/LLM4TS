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
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("Test comment");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testOuterHtml() {
        Comment comment = new Comment("Test comment");
        String expected = "<!--Test comment-->";
        assertEquals(expected, comment.outerHtml());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("Test comment");
        assertEquals("<!--Test comment-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment1 = new Comment("<!DOCTYPE html>");
        Comment comment2 = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

        assertFalse(comment1.isXmlDeclaration());
        assertTrue(comment2.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment1 = new Comment("<!DOCTYPE html>");
        Comment comment2 = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

        assertNull(comment1.asXmlDeclaration());
        assertNotNull(comment2.asXmlDeclaration());
        assertEquals("xml", comment2.asXmlDeclaration().nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        Comment comment = new Comment("Test comment");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        out.prettyPrint(true);

        try {
            comment.outerHtmlHead(accum, 0, out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String expected = "<!--Test comment-->";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        Comment comment = new Comment("Test comment");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        try {
            comment.outerHtmlTail(accum, 0, out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals("", accum.toString());
    }
}