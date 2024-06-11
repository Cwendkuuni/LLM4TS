import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {

    // Test case for Comment(String data)
    @Test
    public void testCommentWithData() {
        Comment comment = new Comment("Test Comment");
        assertEquals("Test Comment", comment.getData());
    }

    // Test case for Comment(String data, String baseUri)
    @Test
    public void testCommentWithDataAndBaseUri() {
        Comment comment = new Comment("Test Comment", "http://example.com");
        assertEquals("Test Comment", comment.getData());
    }

    // Test case for nodeName()
    @Test
    public void testNodeName() {
        Comment comment = new Comment("Test Comment");
        assertEquals("#comment", comment.nodeName());
    }

    // Test case for getData()
    @Test
    public void testGetData() {
        Comment comment = new Comment("Test Comment");
        assertEquals("Test Comment", comment.getData());
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
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("xml", xmlDeclaration.tagName());
    }
    
    // Test case for outerHtmlHead()
    // Edge case: Empty comment
    @Test
    public void testOuterHtmlHeadWithEmptyComment() throws IOException {
        Comment comment = new Comment("");
        StringBuilder sb = new StringBuilder();
        comment.outerHtmlHead(sb, 0, new Document.OutputSettings());
        assertEquals("<!-- -->", sb.toString());
    }

    // Test case for toString()
    @Test
    public void testToString() {
        Comment comment = new Comment("Test Comment");
        assertEquals("<!--Test Comment-->", comment.toString());
    }
}