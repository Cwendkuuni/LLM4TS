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
    public void testCommentConstructor() {
        Comment comment = new Comment("Test comment");
        assertEquals("Test comment", comment.getData());
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testDeprecatedCommentConstructor() {
        Comment comment = new Comment("Test comment", "fakeURI");
        assertEquals("Test comment", comment.getData());
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        Comment comment = new Comment("Test comment");
        Appendable accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        out.prettyPrint(true);
        try {
            comment.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
        assertEquals("<!--Test comment-->", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        Comment comment = new Comment("Test comment");
        Appendable accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        out.prettyPrint(true);
        try {
            comment.outerHtmlTail(accum, 0, out);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
        assertEquals("", accum.toString());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("Test comment");
        assertEquals("<!--Test comment-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("Test comment");
        assertFalse(comment.isXmlDeclaration());

        Comment xmlDeclaration = new Comment("! Test comment");
        assertTrue(xmlDeclaration.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("Test comment");
        assertNull(comment.asXmlDeclaration());

        Comment xmlDeclaration = new Comment("! Test comment");
        XmlDeclaration decl = xmlDeclaration.asXmlDeclaration();
        assertNotNull(decl);
        assertEquals("! Test comment", decl.getData());
    }
}