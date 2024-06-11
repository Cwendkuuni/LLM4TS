import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Comment;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CommentTest {

    @Test
    public void testCommentConstructor() {
        Comment comment = new Comment("This is a comment");
        String expected = "This is a comment";
        assertEquals(expected, comment.getData());
    }

    @Test
    public void testCommentDeprecatedConstructor() {
        Comment comment = new Comment("This is a comment", "");
        String expected = "This is a comment";
        assertEquals(expected, comment.getData());
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("This is a comment");
        String expected = "#comment";
        assertEquals(expected, comment.nodeName());
    }

    @Test
    public void testGetData() {
        Comment comment = new Comment("This is a comment");
        String expected = "This is a comment";
        assertEquals(expected, comment.getData());
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        Comment comment = new Comment("This is a comment");
        StringBuilder accum = new StringBuilder();

        comment.outerHtmlHead(accum, 0, null);

        String expected = "<!--This is a comment-->";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        Comment comment = new Comment("This is a comment");
        StringBuilder accum = new StringBuilder();

        comment.outerHtmlTail(accum, 0, null);

        String expected = "";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("This is a comment");
        String expected = "<!--This is a comment-->";
        assertEquals(expected, comment.toString());
    }

    @Test
    public void testIsXmlDeclarationFalse() {
        Comment comment = new Comment("This is a comment");
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclarationTrue() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body>This is a comment</body></html>");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationNull() {
        Comment comment = new Comment("This is a comment");
        assertNull(comment.asXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<declaration xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertEquals("declaration", xmlDeclaration.getName());
        assertFalse(xmlDeclaration.isStandalone());
    }
}