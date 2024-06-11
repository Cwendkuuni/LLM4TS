import static org.junit.Assert.*;
import org.junit.Test;

public class CommentTest {
    
    @Test
    public void testConstructorWithData() {
        Comment comment = new Comment("Test Comment");
        assertEquals("Test Comment", comment.getData());
    }
    
    @Test
    public void testConstructorWithDataAndBaseUri() {
        Comment comment = new Comment("Test Comment", "baseUri");
        assertEquals("Test Comment", comment.getData());
    }
    
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
    public void testOuterHtmlHead() {
        Comment comment = new Comment("Test Comment");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            comment.outerHtmlHead(accum, 0, out);
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
        assertEquals("<!--Test Comment-->", accum.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        Comment comment = new Comment("Test Comment");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        comment.outerHtmlTail(accum, 0, out);
        assertEquals("", accum.toString());
    }
    
    @Test
    public void testToString() {
        Comment comment = new Comment("Test Comment");
        assertEquals("<!--Test Comment-->", comment.toString());
    }
    
    @Test
    public void testIsXmlDeclaration_whenNotXmlDeclaration() {
        Comment comment = new Comment("Test Comment");
        assertFalse(comment.isXmlDeclaration());
    }
    
    @Test
    public void testIsXmlDeclaration_whenXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(comment.isXmlDeclaration());
    }
    
    @Test
    public void testAsXmlDeclaration_whenNotXmlDeclaration() {
        Comment comment = new Comment("Test Comment");
        assertNull(comment.asXmlDeclaration());
    }
    
    @Test
    public void testAsXmlDeclaration_whenXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.getData());
    }
}