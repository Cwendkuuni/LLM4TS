import org.jsoup.nodes.Comment;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testCommentConstructor() {
        Comment comment = new Comment("Test Comment");
        assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testCommentNodeName() {
        Comment comment = new Comment("Test Comment");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testIsXmlDeclaration_true() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclaration_false() {
        Comment comment = new Comment("Test Comment");
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration_valid() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertNotNull(comment.asXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration_invalid() {
        Comment comment = new Comment("Test Comment");
        assertNull(comment.asXmlDeclaration());
    }
}