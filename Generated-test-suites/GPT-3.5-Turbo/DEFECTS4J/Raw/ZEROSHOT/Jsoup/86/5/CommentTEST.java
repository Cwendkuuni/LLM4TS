import org.jsoup.nodes.Comment;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommentTest {
    private Comment comment;

    @Before
    public void setup() {
        comment = new Comment("Test Comment");
    }

    @Test
    public void testNodeName() {
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testGetData() {
        assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testIsXmlDeclaration() {
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        assertNull(comment.asXmlDeclaration());
    }

    @Test
    public void testToString() {
        assertEquals("<!--Test Comment-->", comment.toString());
    }
}