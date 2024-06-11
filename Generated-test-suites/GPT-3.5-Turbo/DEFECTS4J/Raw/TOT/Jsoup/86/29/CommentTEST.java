
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
        Comment comment = new Comment("Test comment", "http://www.example.com");
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
        assertEquals("<!--Test comment-->", comment.outerHtml());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("Test comment");
        assertEquals("<!--Test comment-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclarationTrue() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclarationFalse() {
        Comment comment = new Comment("Test comment");
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationNonNull() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration declaration = comment.asXmlDeclaration();
        assertNotNull(declaration);
    }

    @Test
    public void testAsXmlDeclarationNull() {
        Comment comment = new Comment("Test comment");
        XmlDeclaration declaration = comment.asXmlDeclaration();
        assertNull(declaration);
    }
}
