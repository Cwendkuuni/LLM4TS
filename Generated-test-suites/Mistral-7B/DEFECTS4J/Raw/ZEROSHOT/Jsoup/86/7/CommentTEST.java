package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.IOException;

/**
 * Test class for Comment class
 *
 * @author John Doe
 */
public class CommentTest {

    @org.junit.Test
    public void testCommentCreation() throws IOException {
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
    }

    @org.junit.Test
    public void testCommentCreationWithBaseUri() throws IOException {
        Comment comment = new Comment("This is a comment", "http://example.com");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
        assertSame("http://example.com", comment.baseUri());
    }

    @org.junit.Test
    public void testCommentOuterHtml() throws IOException {
        Comment comment = new Comment("This is a comment");
        StringBuilder out = new StringBuilder();
        comment.outerHtmlHead(out, 0, null);
        comment.outerHtmlTail(out, 0, null);
        assertEquals("<!--This is a comment-->", out.toString());
    }

    @org.junit.Test
    public void testCommentToString() throws IOException {
        Comment comment = new Comment("This is a comment");
        assertEquals("<!--This is a comment-->", comment.toString());
    }

    @org.junit.Test
    public void testCommentIsXmlDeclaration() throws IOException {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue("Expected comment to be an XML declaration", comment.isXmlDeclaration());

        Comment comment2 = new Comment("This is a comment");
        assertFalse("Expected comment not to be an XML declaration", comment2.isXmlDeclaration());
    }

    @org.junit.Test
    public void testCommentAsXmlDeclaration() throws IOException {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root/>");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertSame("Expected XML declaration to be instance of XmlDeclaration", XmlDeclaration.class, xmlDeclaration.getClass());
        assertEquals("1.0", xmlDeclaration.version());
        assertEquals("UTF-8", xmlDeclaration.encoding());
        assertNull("Expected no attributes for XML declaration", xmlDeclaration.attributes().size());
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testCommentAsXmlDeclarationWithoutDeclaration() throws IOException {
        Comment comment = new Comment("This is a comment");
        comment.asXmlDeclaration();
    }
}