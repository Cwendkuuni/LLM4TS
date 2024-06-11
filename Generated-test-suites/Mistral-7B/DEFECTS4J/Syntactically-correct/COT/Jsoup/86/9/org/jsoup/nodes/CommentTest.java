import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

public class CommentTest {

    @Test
    public void testCommentConstruction() {
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCommentDeprecatedConstruction() {
        Comment comment = new Comment("This is a comment", null);
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCommentOuterHtml() throws IOException {
        Comment comment = new Comment("This is a comment");
        StringBuilder accum = new StringBuilder();
        comment.outerHtmlHead(accum, 0, null);
        comment.outerHtmlTail(accum, 0, null);
        String expected = "<!--This is a comment-->";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testCommentToString() {
        Comment comment = new Comment("This is a comment");
        assertEquals("<!--This is a comment-->", comment.toString());
    }

    @Test
    public void testCommentIsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>");
        assertTrue(comment.isXmlDeclaration());

        Comment comment2 = new Comment("This is a comment");
        assertFalse(comment2.isXmlDeclaration());
    }

    @Test(expected = AssertionError.class)
    public void testCommentAsXmlDeclarationNull() {
        Comment comment = new Comment("This is a comment");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNull(xmlDeclaration);
    }

    @Test
    public void testCommentAsXmlDeclarationValid() throws IOException {
        String xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><element name=\"test\">Value</element></root>";
        Comment comment = new Comment(xmlData);
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.xml());
    }
}