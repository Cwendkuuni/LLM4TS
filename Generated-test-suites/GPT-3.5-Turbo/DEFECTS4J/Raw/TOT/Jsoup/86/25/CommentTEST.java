import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {
    private Comment comment;

    @Before
    public void setUp() {
        comment = new Comment("This is a comment");
    }

    @Test
    public void testConstructorWithData() {
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testConstructorWithDataAndBaseUri() {
        Comment commentWithBaseUri = new Comment("This is a comment", "http://example.com");
        assertEquals("This is a comment", commentWithBaseUri.getData());
    }

    @Test
    public void testIsXmlDeclarationTrue() {
        Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(xmlDeclarationComment.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclarationFalse() {
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationValid() {
        Comment validXmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration xmlDeclaration = validXmlDeclarationComment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("xml", xmlDeclaration.nodeName());
    }

    @Test
    public void testAsXmlDeclarationInvalid() {
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNull(xmlDeclaration);
    }
}