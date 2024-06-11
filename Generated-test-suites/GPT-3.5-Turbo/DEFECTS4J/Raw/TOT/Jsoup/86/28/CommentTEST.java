
import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testConstructorWithData() {
        Comment comment = new Comment("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testConstructorWithDataAndBaseUri() {
        Comment comment = new Comment("This is a comment", "http://example.com");
        assertEquals("This is a comment", comment.getData());
    }

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
    public void testIsXmlDeclarationWithEmptyData() {
        Comment comment = new Comment("");
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclarationWithInvalidData() {
        Comment comment = new Comment("This is not an XML declaration");
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclarationWithValidData() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationWithEmptyData() {
        Comment comment = new Comment("");
        assertNull(comment.asXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationWithInvalidData() {
        Comment comment = new Comment("This is not an XML declaration");
        assertNull(comment.asXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationWithValidData() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("xml", xmlDeclaration.nodeName());
        assertTrue(xmlDeclaration.hasAttribute("version"));
        assertEquals("1.0", xmlDeclaration.attr("version"));
        assertTrue(xmlDeclaration.hasAttribute("encoding"));
        assertEquals("UTF-8", xmlDeclaration.attr("encoding"));
    }
}
