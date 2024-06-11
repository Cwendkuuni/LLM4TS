import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testCommentConstructor() {
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testDeprecatedCommentConstructor() {
        Comment comment = new Comment("This is a comment", "baseUri");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("<!-- This is an XML declaration -->");
        assertTrue(comment.isXmlDeclaration());
        Comment simpleComment = new Comment("This is a simple comment");
        assertFalse(simpleComment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><title>Test</title></head><body><p>Content</p></body></html>");
        XmlDeclaration xmlDeclaration = xmlDeclarationComment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.data());
        assertTrue(xmlDeclaration.isStandalone());

        Comment commentWithoutDeclaration = new Comment("This is a comment without an XML declaration");
        assertNull(commentWithoutDeclaration.asXmlDeclaration());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeDepthInOuterHtmlMethod() {
        Comment comment = new Comment("Test Comment");
        Appendable accum = new StringWriter();
        Document document = new Document();
        document.setOutputSettings(new Document.OutputSettings().indent(5));
        comment.outerHtmlHead(accum, -1, document.outputSettings());
    }

    @Test(expected = IOException.class)
    public void testIOExceptionInOuterHtmlMethod() {
        Comment comment = new Comment("Test Comment");
        Appendable accum = new StringWriter();
        Document document = new Document();
        document.setOutputSettings(new Document.OutputSettings().prettyPrint(false));
        try {
            comment.outerHtmlHead(accum, 0, document.outputSettings());
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testToStringMethod() {
        Comment comment = new Comment("This is a comment");
        assertEquals("<!--This is a comment-->", comment.toString());
    }
}