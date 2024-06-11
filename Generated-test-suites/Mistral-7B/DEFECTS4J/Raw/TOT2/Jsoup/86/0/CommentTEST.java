
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testCommentConstruction() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        assertEquals("#comment", comment.nodeName());
        assertEquals(data, comment.getData());
    }

    @Test
    public void testDeprecatedCommentConstruction() {
        String data = "This is a comment";
        Comment comment = new Comment(data, null);
        assertEquals("#comment", comment.nodeName());
        assertEquals(data, comment.getData());
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testGetData() {
        Comment comment = new Comment("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testOuterHtml() throws IOException {
        Comment comment = new Comment("This is a comment");
        StringBuilder sb = new StringBuilder();
        comment.outerHtml(sb, 0, null);
        String expected = "<!--This is a comment-->";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("This is a comment");
        assertEquals(comment.outerHtml(), comment.toString());
    }

    @Test
    public void testIsXmlDeclarationTrue() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>This is a comment.</p>\n</body>\n</html>");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclarationFalse() {
        Comment comment = new Comment("This is a comment");
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationNull() {
        Comment comment = new Comment("This is a comment");
        assertNull(comment.asXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationValid() {
        String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Comment comment = new Comment(xmlDeclaration);
        XmlDeclaration xml = comment.asXmlDeclaration();
        assertTrue(xml != null);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xml.outerHtml());
    }
}
