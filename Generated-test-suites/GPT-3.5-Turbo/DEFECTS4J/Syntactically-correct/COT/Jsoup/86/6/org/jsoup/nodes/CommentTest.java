
import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testNodeName() {
        Comment comment = new Comment("Test Comment");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testGetData() {
        Comment comment = new Comment("Test Comment");
        assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment nonXmlDeclaration = new Comment("Test Comment");
        assertFalse(nonXmlDeclaration.isXmlDeclaration());

        Comment xmlDeclaration = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(xmlDeclaration.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment nonXmlDeclaration = new Comment("Test Comment");
        assertNull(nonXmlDeclaration.asXmlDeclaration());

        Comment xmlDeclaration = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertNotNull(xmlDeclaration.asXmlDeclaration());
    }

    // Additional test cases for edge cases and exceptions

    @Test
    public void testConstructWithBaseUri() {
        Comment comment = new Comment("Test Comment", "http://example.com");
        assertEquals("Test Comment", comment.getData());
        // Verify baseUri is not used
    }

    @Test
    public void testOuterHtmlHeadPrettyPrint() throws IOException {
        Comment comment = new Comment("Test Comment");
        StringBuilder accum = new StringBuilder();
        comment.outerHtmlHead(accum, 0, new Document.OutputSettings().prettyPrint(true));
        assertEquals("<!--Test Comment-->", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadNotPrettyPrint() throws IOException {
        Comment comment = new Comment("Test Comment");
        StringBuilder accum = new StringBuilder();
        comment.outerHtmlHead(accum, 0, new Document.OutputSettings().prettyPrint(false));
        assertEquals("<!--Test Comment-->", accum.toString());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("Test Comment");
        assertEquals("<!--Test Comment-->", comment.toString());
    }
}
