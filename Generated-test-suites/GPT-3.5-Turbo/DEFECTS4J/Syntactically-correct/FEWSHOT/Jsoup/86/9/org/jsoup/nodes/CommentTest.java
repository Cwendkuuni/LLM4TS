package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class CommentTest {
    @Test
    public void testCommentConstructorWithData() {
        Comment comment = new Comment("Test comment");
        assertEquals("Test comment", comment.getData());
        assertEquals("#comment", comment.nodeName());
        assertEquals("<!--Test comment-->", comment.toString());
        assertFalse(comment.isXmlDeclaration());
        assertNull(comment.asXmlDeclaration());
    }

    @Test
    public void testCommentConstructorWithDataAndBaseUri() {
        Comment comment = new Comment("Test comment", "https://example.com");
        assertEquals("Test comment", comment.getData());
        assertEquals("#comment", comment.nodeName());
        assertEquals("<!--Test comment-->", comment.toString());
        assertFalse(comment.isXmlDeclaration());
        assertNull(comment.asXmlDeclaration());
    }

    @Test
    public void testCommentConstructorWithEmptyData() {
        Comment comment = new Comment("");
        assertEquals("", comment.getData());
        assertEquals("#comment", comment.nodeName());
        assertEquals("<!-- -->", comment.toString());
        assertFalse(comment.isXmlDeclaration());
        assertNull(comment.asXmlDeclaration());
    }

    @Test
    public void testGetData() {
        Comment comment = new Comment("Test comment");
        assertEquals("Test comment", comment.getData());
    }

    @Test
    public void testOuterHtmlHead() {
        Comment comment = new Comment("Test comment");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        try {
            comment.outerHtmlHead(accum, 0, outputSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("<!--Test comment-->", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        Comment comment = new Comment("Test comment");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        try {
            comment.outerHtmlTail(accum, 0, outputSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Nothing to test as the method does nothing.
        assertEquals("", accum.toString());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("Test comment");
        assertEquals("<!--Test comment-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclarationWithValidXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclarationWithInvalidXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"");
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationWithValidXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration declaration = comment.asXmlDeclaration();
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", declaration.outerHtml());
    }

    @Test
    public void testAsXmlDeclarationWithInvalidXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"");
        assertNull(comment.asXmlDeclaration());
    }
}