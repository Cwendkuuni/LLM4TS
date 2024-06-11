import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testCommentConstructor() {
        Comment comment = new Comment("This is a test comment");
        assertEquals("This is a test comment", comment.getData());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment1 = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(comment1.isXmlDeclaration());

        Comment comment2 = new Comment("<!-- This is a regular comment -->");
        assertFalse(comment2.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment1 = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration decl1 = comment1.asXmlDeclaration();
        assertNotNull(decl1);
        assertEquals("xml", decl1.nodeName());

        Comment comment2 = new Comment("<!-- This is a regular comment -->");
        XmlDeclaration decl2 = comment2.asXmlDeclaration();
        assertNull(decl2);
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("This is a test comment");
        assertEquals("<!--This is a test comment-->", comment.toString());
    }
}