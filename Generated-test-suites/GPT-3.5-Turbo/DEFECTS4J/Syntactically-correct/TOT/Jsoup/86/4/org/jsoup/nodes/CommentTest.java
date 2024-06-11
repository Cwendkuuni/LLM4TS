
import org.junit.Test;
import org.jsoup.nodes.Comment;
import org.jsoup.parser.Parser;

import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testCommentConstructorWithData() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        assertEquals(data, comment.getData());
    }

    @Test
    public void testCommentConstructorWithDataAndBaseUri() {
        String data = "This is a comment";
        String baseUri = "http://www.example.com";
        Comment comment = new Comment(data, baseUri);
        assertEquals(data, comment.getData());
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testGetData() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        assertEquals(data, comment.getData());
    }

    @Test
    public void testOuterHtmlHead() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            comment.outerHtmlHead(accum, depth, out);
        } catch (Exception e) {
            fail("Exception thrown while generating outerHtmlHead");
        }
        assertEquals("<!--This is a comment-->", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            comment.outerHtmlTail(accum, depth, out);
        } catch (Exception e) {
            fail("Exception thrown while generating outerHtmlTail");
        }
        assertEquals("", accum.toString());
    }

    @Test
    public void testToString() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        assertEquals("<!--This is a comment-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclarationReturnsFalseForRegularComment() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclarationReturnsTrueForXmlDeclarationComment() {
        String data = "! This is an XML declaration";
        Comment comment = new Comment(data);
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationReturnsNullForRegularComment() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        assertNull(comment.asXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationReturnsXmlDeclarationForXmlDeclarationComment() {
        String data = "! This is an XML declaration";
        Comment comment = new Comment(data);
        assertNotNull(comment.asXmlDeclaration());

        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertEquals("!DOCTYPE", xmlDeclaration.nodeName());
    }
}

