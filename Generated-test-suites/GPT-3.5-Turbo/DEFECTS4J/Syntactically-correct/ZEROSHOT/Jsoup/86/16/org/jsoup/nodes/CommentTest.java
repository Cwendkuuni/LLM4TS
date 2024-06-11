// Import Junit 4 and other necessary packages
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

// Import Comment class
import org.jsoup.nodes.Comment;

public class CommentTest {
    private Comment comment;

    @Before
    public void setUp() {
        comment = new Comment("This is a test comment");
    }

    @Test
    public void testNodeName() {
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testGetData() {
        assertEquals("This is a test comment", comment.getData());
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
        assertEquals("<!--This is a test comment-->", comment.toString());
    }

    @Test
    public void testConstructorWithData() {
        Comment c = new Comment("Test data");
        assertEquals("Test data", c.getData());
    }

    @Test
    public void testConstructorWithDataAndBaseUri() {
        Comment c = new Comment("Test data", "http://example.com");
        assertEquals("Test data", c.getData());
    }
}

