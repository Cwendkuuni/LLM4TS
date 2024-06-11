
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CommentTest {

    @Test
    public void testCommentConstructor() {
        Comment comment = new Comment("Test comment");
        assertEquals("Test comment", comment.getData());
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testCommentConstructorWithBaseUri() {
        Comment comment = new Comment("Test comment", "http://example.com");
        assertEquals("Test comment", comment.getData());
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testGetNodeName() {
        Comment comment = new Comment("Test comment");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testGetData() {
        Comment comment = new Comment("Test comment");
        assertEquals("Test comment", comment.getData());
    }

    @Test
    public void testOuterHtmlHead() {
        Comment comment = new Comment("Test comment");
        StringBuilder builder = new StringBuilder();
        comment.outerHtmlHead(builder, 0, new Document.OutputSettings());
        assertEquals("<!--Test comment-->", builder.toString());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("Test comment");
        assertEquals("<!--Test comment-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("!DOCTYPE html");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclarationWithRegularComment() {
        Comment comment = new Comment("Test comment");
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("!DOCTYPE html");
        XmlDeclaration declaration = comment.asXmlDeclaration();
        assertEquals("!DOCTYPE", declaration.nodeName());
        assertEquals("html", declaration.tagName());
    }

    @Test
    public void testAsXmlDeclarationWithRegularComment() {
        Comment comment = new Comment("Test comment");
        assertNull(comment.asXmlDeclaration());
    }
}
