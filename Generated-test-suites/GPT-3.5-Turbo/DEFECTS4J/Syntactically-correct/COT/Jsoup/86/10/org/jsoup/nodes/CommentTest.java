
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
    public void testIsXmlDeclarationWithXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclarationWithoutXmlDeclaration() {
        Comment comment = new Comment("This is a comment");
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationWithValidXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration declaration = comment.asXmlDeclaration();
        assertNotNull(declaration);
        assertEquals("xml", declaration.nodeName());
        assertEquals("version", declaration.attributes().get(0).getKey());
        assertEquals("1.0", declaration.attributes().get(0).getValue());
        assertEquals("encoding", declaration.attributes().get(1).getKey());
        assertEquals("UTF-8", declaration.attributes().get(1).getValue());
    }

    @Test
    public void testAsXmlDeclarationWithInvalidXmlDeclaration() {
        Comment comment = new Comment("This is a comment");
        XmlDeclaration declaration = comment.asXmlDeclaration();
        assertNull(declaration);
    }
}

