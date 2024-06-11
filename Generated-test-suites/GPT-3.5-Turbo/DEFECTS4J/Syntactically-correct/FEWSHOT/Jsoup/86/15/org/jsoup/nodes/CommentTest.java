import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;


public class CommentTest {
    @Test
    public void testConstructorWithData() {
        Comment comment = new Comment("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testConstructorWithDataAndBaseUri() {
        Comment comment = new Comment("This is a comment", "http://www.example.com");
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        Comment comment = new Comment("This is a comment");
        assertEquals("<!--This is a comment-->", comment.outerHtml());
    }

    @Test
    public void testOuterHtmlTail() {
        Comment comment = new Comment("This is a comment");
        assertEquals("", comment.outerHtml());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("This is a comment");
        assertEquals("<!--This is a comment-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclarationWithXmlDeclaration() {
        Comment comment = new Comment("<!DOCTYPE html>");
        assertEquals(true, comment.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclarationWithoutXmlDeclaration() {
        Comment comment = new Comment("This is a comment");
        assertEquals(false, comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationWithValidXmlDeclaration() {
        Comment comment = new Comment("<!DOCTYPE html>");
        XmlDeclaration decl = comment.asXmlDeclaration();
        assertEquals(true, decl instanceof XmlDeclaration);
    }

    @Test
    public void testAsXmlDeclarationWithInvalidXmlDeclaration() {
        Comment comment = new Comment("This is a comment");
        assertNull(comment.asXmlDeclaration());
    }
}