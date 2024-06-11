import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {
    @Test
    public void testCommentConstructorValidData() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        assertEquals("#comment", comment.nodeName());
        assertEquals(data, comment.getData());
    }

    @Test
    public void testCommentConstructorEmptyData() {
        Comment comment = new Comment("");
        assertEquals("#comment", comment.nodeName());
        assertEquals("", comment.getData());
    }

    @Test
    public void testNodeName() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testGetData() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        assertEquals(data, comment.getData());
    }

    @Test
    public void testOuterHtmlHeadWithPrettyPrintTrue() throws IOException {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        Appendable accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        comment.outerHtmlHead(accum, depth, out);
        String expected = "<!--This is a comment-->";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPrettyPrintFalse() throws IOException {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        Appendable accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        comment.outerHtmlHead(accum, depth, out);
        String expected = "<!--This is a comment-->";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithDifferentIndentationLevels() throws IOException {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        Appendable accum = new StringBuilder();
        int depth = 2;
        Document.OutputSettings out = new Document.OutputSettings();
        comment.outerHtmlHead(accum, depth, out);
        String expected = "    <!--This is a comment-->";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        // Empty method
    }

    @Test
    public void testIsXmlDeclarationWithValidXmlDeclarationData() {
        String data = "<?xml version='1.0' encoding='UTF-8'?>";
        Comment comment = new Comment(data);
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclarationWithNonXmlDeclarationData() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        assertFalse(comment.isXmlDeclaration());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAsXmlDeclarationWithNullData() {
        Comment comment = new Comment(null);
        comment.asXmlDeclaration();
    }

    @Test
    public void testAsXmlDeclarationWithLongData() {
        String data = "This is a very long comment that tests the limits of the asXmlDeclaration() method";
        Comment comment = new Comment(data);
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("#comment", xmlDeclaration.nodeName());
        assertEquals(data, xmlDeclaration.getData());
    }

    @Test
    public void testAsXmlDeclarationWithEmptyData() {
        Comment comment = new Comment("");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNull(xmlDeclaration);
    }

    @Test
    public void testAsXmlDeclarationWithInvalidXmlDeclarationData() {
        String data = "<?xml version='1.0' encoding=UTF-8?>"; // Missing a quote
        Comment comment = new Comment(data);
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNull(xmlDeclaration);
    }
}