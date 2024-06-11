package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

public class CommentTest {
    private Comment comment;

    @Before
    public void setup() {
        comment = new Comment("Sample comment");
    }

    @Test
    public void testNodeName() {
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testGetData() {
        assertEquals("Sample comment", comment.getData());
    }

    @Test
    public void testIsXmlDeclaration() {
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        assertNull(comment.asXmlDeclaration());
    }

    @Test
    public void testToString() {
        String expectedOutput = "<!--Sample comment-->";
        assertEquals(expectedOutput, comment.toString());
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        out.prettyPrint(true);
        out.indentAmount(4);
        try {
            comment.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {}
        String expectedOutput = "<!--Sample comment-->";
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            comment.outerHtmlTail(accum, 0, out);
        } catch (Exception e) {}
        String expectedOutput = "";
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testCommentConstructor() {
        Comment commentWithBaseUri = new Comment("Sample comment", "http://example.com");
        assertEquals("Sample comment", commentWithBaseUri.getData());
    }
}