import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import java.io.IOException;

public class CommentTest {

    @Test
    public void testCommentConstructorWithData() {
        Comment comment = new Comment("This is a test comment");
        assertEquals("This is a test comment", comment.getData());
    }

    @Test
    public void testCommentConstructorWithDataAndBaseUri() {
        Comment comment = new Comment("This is a test comment", "https://example.com");
        assertEquals("This is a test comment", comment.getData());
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("This is a test comment");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        Comment comment = new Comment("This is a test comment");
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.setPrettyPrint(true);
        try {
            comment.outerHtmlHead(sb, 0, settings);
        } catch (IOException e) {
            fail("Exception thrown while generating outer HTML head");
        }
        assertEquals("<!--This is a test comment-->", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        Comment comment = new Comment("This is a test comment");
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings settings = new Document.OutputSettings();
        try {
            comment.outerHtmlTail(sb, 0, settings);
        } catch (IOException e) {
            fail("Exception thrown while generating outer HTML tail");
        }
        assertEquals("", sb.toString());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("This is a test comment");
        assertEquals("<!--This is a test comment-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclarationWithData() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclarationWithoutData() {
        Comment comment = new Comment("");
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationWithData() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
        XmlDeclaration declaration = comment.asXmlDeclaration();
        assertNotNull(declaration);
        assertEquals("xml", declaration.tagName());
        assertTrue(declaration.isProcessingInstruction());
    }

    @Test
    public void testAsXmlDeclarationWithoutData() {
        Comment comment = new Comment("");
        XmlDeclaration declaration = comment.asXmlDeclaration();
        assertNull(declaration);
    }

    @Test
    public void testAsXmlDeclarationInvalidData() {
        Comment comment = new Comment("<?xml version=\"1.0\"?");
        XmlDeclaration declaration = comment.asXmlDeclaration();
        assertNull(declaration);
    }
}