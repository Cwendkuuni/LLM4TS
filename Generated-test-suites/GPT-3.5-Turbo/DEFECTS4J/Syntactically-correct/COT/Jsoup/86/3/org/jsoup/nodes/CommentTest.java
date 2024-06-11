
import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testNodeName() {
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testGetData() {
        Comment comment = new Comment("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration declaration = comment.asXmlDeclaration();
        assertNotNull(declaration);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", declaration.outerHtml());
    }
    
    // Additional edge cases and exception handling scenarios
    
    @Test
    public void testEmptyData() {
        Comment comment = new Comment("");
        assertEquals("", comment.getData());
    }
    
    @Test(expected = IOException.class)
    public void testIOExceptionInOuterHtmlHead() throws IOException {
        Comment comment = new Comment("This is a comment");
        comment.outerHtmlHead(null, 0, null);
    }
    
    @Test
    public void testNullData() {
        Comment comment = new Comment(null);
        assertNull(comment.getData());
    }

    @Test
    public void testIsXmlDeclarationFalse() {
        Comment comment = new Comment("This is a comment");
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationInvalid() {
        Comment comment = new Comment("This is not an XML declaration");
        assertNull(comment.asXmlDeclaration());
    }

    // Add more test cases for other methods if needed
    
}

