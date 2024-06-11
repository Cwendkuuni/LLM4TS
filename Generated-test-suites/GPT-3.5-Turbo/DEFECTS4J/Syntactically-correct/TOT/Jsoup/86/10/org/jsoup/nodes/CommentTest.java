import org.jsoup.nodes.Comment;

import static org.junit.Assert.*;
import org.junit.Test;

public class CommentTest {

    @Test
    public void testConstructorWithData() {
        Comment comment = new Comment("This is a comment.");
        assertNotNull(comment);
        assertEquals("This is a comment.", comment.getData());
    }

    @Test
    public void testConstructorWithDataAndBaseUri() {
        Comment comment = new Comment("This is another comment.", "http://www.example.com");
        assertNotNull(comment);
        assertEquals("This is another comment.", comment.getData());
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("This is a comment.");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testGetXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(comment.isXmlDeclaration());
        assertNotNull(comment.asXmlDeclaration());
    }

    @Test
    public void testGetOuterHtml() {
        Comment comment = new Comment("This is a comment.");
        assertEquals("<!--This is a comment.-->", comment.outerHtml());
    }

    @Test
    public void testGetXmlDeclarationOuterHtml() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertEquals("<!--?xml version=\"1.0\" encoding=\"UTF-8\"?-->", comment.outerHtml());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("This is a comment.");
        assertEquals("<!--This is a comment.-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclarationWithInvalidData() {
        Comment comment = new Comment("Not an XML Declaration");
        assertFalse(comment.isXmlDeclaration());
        assertNull(comment.asXmlDeclaration());
    }

}