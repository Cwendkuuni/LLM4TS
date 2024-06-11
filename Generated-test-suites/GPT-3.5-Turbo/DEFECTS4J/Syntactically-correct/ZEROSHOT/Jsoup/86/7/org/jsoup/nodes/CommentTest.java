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
        comment = new Comment("!DOCTYPE html");
        assertTrue(comment.isXmlDeclaration());
        comment = new Comment("?xml version=\"1.0\"?");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        assertNull(comment.asXmlDeclaration());
        comment = new Comment("!DOCTYPE html");
        assertNotNull(comment.asXmlDeclaration());
        comment = new Comment("?xml version=\"1.0\"?");
        assertNotNull(comment.asXmlDeclaration());
    }
}