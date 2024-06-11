import org.jsoup.nodes.Comment;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class CommentTest {

    @Test
    public void testCommentConstructor() {
        Comment comment = new Comment("Test comment");
        assertEquals("Test comment", comment.getData());
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("Test comment");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("<!DOCTYPE html>");
        assertTrue(comment.isXmlDeclaration());

        Comment comment2 = new Comment("Regular text");
        assertFalse(comment2.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<!DOCTYPE html>");
        assertNotNull(comment.asXmlDeclaration());

        Comment comment2 = new Comment("Regular text");
        assertNull(comment2.asXmlDeclaration());
    }
}