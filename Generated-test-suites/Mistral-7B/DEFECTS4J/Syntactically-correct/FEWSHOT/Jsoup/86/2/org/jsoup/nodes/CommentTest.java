package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class CommentTest {

    @Test
    public void testCommentCreation() {
        Comment comment = new Comment("This is a comment.");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment.", comment.getData());
    }

    @Test
    public void testCommentCreationWithDeprecatedConstructor() {
        Comment comment = new Comment("This is a comment.", null);
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment.", comment.getData());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment1 = new Comment("<!-- This is a comment -->");
        assertTrue(comment1.isXmlDeclaration());

        Comment comment2 = new Comment("This is a comment");
        assertFalse(comment2.isXmlDeclaration());

        Comment comment3 = new Comment("!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"");
        assertTrue(comment3.isXmlDeclaration());

        Comment comment4 = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
        assertTrue(comment4.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.xml());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAsXmlDeclarationWithInvalidDeclaration() {
        Comment comment = new Comment("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
        comment.asXmlDeclaration();
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("This is a comment.");
        assertEquals("<!--This is a comment-->", comment.toString());
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        Comment comment = new Comment("This is a comment.");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        comment.outerHtmlHead(accum, 0, out);
        assertEquals("<!--This is a comment-->", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        Comment comment = new Comment("This is a comment.");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        comment.outerHtmlTail(accum, 0, out);
        assertEquals("", accum.toString());
    }
}