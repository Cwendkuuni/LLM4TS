import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testConstructor() {
        Comment comment = new Comment("");
        assertNotNull(comment);
        assertEquals("", comment.getData());
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testGetData() {
        Comment comment = new Comment("");
        assertEquals("", comment.getData());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("");
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("");
        assertNull(comment.asXmlDeclaration());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("");
        assertEquals("<!-- -->", comment.toString());
    }
}