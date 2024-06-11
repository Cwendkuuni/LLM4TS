
import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testCommentConstructorWithValidData() {
        Comment comment = new Comment("This is a valid comment");
        assertEquals("This is a valid comment", comment.getData());
        assertEquals("#comment", comment.nodeName());
        assertFalse(comment.isXmlDeclaration());
        assertNull(comment.asXmlDeclaration());
    }

    @Test
    public void testCommentConstructorWithEmptyData() {
        Comment comment = new Comment("");
        assertEquals("", comment.getData());
        assertEquals("#comment", comment.nodeName());
        assertFalse(comment.isXmlDeclaration());
        assertNull(comment.asXmlDeclaration());
    }

    @Test
    public void testCommentConstructorWithNullData() {
        Comment comment = new Comment(null);
        assertNull(comment.getData());
        assertEquals("#comment", comment.nodeName());
        assertFalse(comment.isXmlDeclaration());
        assertNull(comment.asXmlDeclaration());
    }

    @Test
    public void testOuterHtmlFormatting() {
        Comment comment = new Comment("This is a comment");
        String expected = "<!--This is a comment-->";
        assertEquals(expected, comment.outerHtml());
    }

    @Test
    public void testIsXmlDeclarationWithValidData() {
        Comment comment1 = new Comment("?xml version=\"1.0\"?");
        Comment comment2 = new Comment("!DOCTYPE html");
        Comment comment3 = new Comment("?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?");
        assertTrue(comment1.isXmlDeclaration());
        assertTrue(comment2.isXmlDeclaration());
        assertTrue(comment3.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclarationWithInvalidData() {
        Comment comment1 = new Comment("This is a comment");
        Comment comment2 = new Comment("<p>This is a paragraph</p>");
        Comment comment3 = new Comment("");
        assertFalse(comment1.isXmlDeclaration());
        assertFalse(comment2.isXmlDeclaration());
        assertFalse(comment3.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationWithValidData() {
        Comment comment1 = new Comment("?xml version=\"1.0\"?");
        Comment comment2 = new Comment("!DOCTYPE html");
        Comment comment3 = new Comment("?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?");
        XmlDeclaration decl1 = comment1.asXmlDeclaration();
        XmlDeclaration decl2 = comment2.asXmlDeclaration();
        XmlDeclaration decl3 = comment3.asXmlDeclaration();
        assertNotNull(decl1);
        assertNotNull(decl2);
        assertNotNull(decl3);
        assertEquals("?xml", decl1.nodeName());
        assertEquals("!DOCTYPE", decl2.nodeName());
        assertEquals("?xml", decl3.nodeName());
        assertEquals("version=\"1.0\"", decl1.attributes().toString());
        assertEquals("", decl2.attributes().toString());
        assertEquals("version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"", decl3.attributes().toString());
    }

    @Test
    public void testAsXmlDeclarationWithInvalidData() {
        Comment comment1 = new Comment("This is a comment");
        Comment comment2 = new Comment("<p>This is a paragraph</p>");
        Comment comment3 = new Comment("");
        assertNull(comment1.asXmlDeclaration());
        assertNull(comment2.asXmlDeclaration());
        assertNull(comment3.asXmlDeclaration());
    }

}
