package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testCommentConstructor() {
        Comment comment = new Comment("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCommentConstructorWithBaseUri() {
        Comment comment = new Comment("This is another comment", "http://example.com");
        assertEquals("This is another comment", comment.getData());
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
    public void testOuterHtmlHead() {
        Comment comment = new Comment("This is a comment");
        StringBuilder builder = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        try {
            comment.outerHtmlHead(builder, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception was thrown: " + e.getMessage());
        }
        assertEquals("<!--This is a comment-->", builder.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        Comment comment = new Comment("This is a comment");
        StringBuilder builder = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        try {
            comment.outerHtmlTail(builder, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception was thrown: " + e.getMessage());
        }
        assertEquals("", builder.toString());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("This is a comment");
        assertEquals("<!--This is a comment-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment1 = new Comment("<!-- This is not an XML declaration -->");
        assertFalse(comment1.isXmlDeclaration());
        
        Comment comment2 = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(comment2.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment1 = new Comment("<!-- This is not an XML declaration -->");
        assertNull(comment1.asXmlDeclaration());

        Comment comment2 = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration xmlDeclaration = comment2.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("xml", xmlDeclaration.nodeName());
        assertTrue(xmlDeclaration.attributes().hasKey("version"));
        assertTrue(xmlDeclaration.attributes().hasKey("encoding"));
    }
}