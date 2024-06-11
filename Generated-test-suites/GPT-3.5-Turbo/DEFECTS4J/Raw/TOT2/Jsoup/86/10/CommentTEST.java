import org.jsoup.nodes.Comment;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CommentTest {

    @Test
    public void testConstructorWithData() {
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
        String expectedOutput = "<!--This is a comment-->";
        assertEquals(expectedOutput, comment.toString());
        assertFalse(comment.isXmlDeclaration());
        assertNull(comment.asXmlDeclaration());
    }

    @Test
    public void testConstructorWithDataAndBaseUri() {
        Comment comment = new Comment("This is another comment", "http://example.com");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is another comment", comment.getData());
        String expectedOutput = "<!--This is another comment-->";
        assertEquals(expectedOutput, comment.toString());
        assertFalse(comment.isXmlDeclaration());
        assertNull(comment.asXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclarationTrue() {
        Comment comment = new Comment("!DOCTYPE");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclarationFalse() {
        Comment comment = new Comment("This is not a declaration");
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationValid() {
        Comment comment = new Comment("!DOCTYPE html");
        assertNotNull(comment.asXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationInvalid() {
        Comment comment = new Comment("This is not a declaration");
        assertNull(comment.asXmlDeclaration());
    }

}