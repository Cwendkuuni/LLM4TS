package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

public class CommentTest {

    // Test case for Comment constructor
    @Test
    public void testCommentConstructor() {
        Comment comment = new Comment("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }

    // Test case for Comment nodeName method
    @Test
    public void testNodeName() {
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
    }

    // Test case for Comment getData method
    @Test
    public void testGetData() {
        Comment comment = new Comment("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }

    // Test case for Comment outerHtmlHead method
    @Test
    public void testOuterHtmlHead() {
        Comment comment = new Comment("This is a comment");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        out.prettyPrint(false);
        try {
            comment.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
        assertEquals("<!--This is a comment-->", accum.toString());
    }

    // Test case for Comment toString method
    @Test
    public void testToString() {
        Comment comment = new Comment("This is a comment");
        assertEquals("<!--This is a comment-->", comment.toString());
    }

    // Test case for Comment isXmlDeclaration method - typical use case
    @Test
    public void testIsXmlDeclaration_typical() {
        Comment comment = new Comment("!This is an XML Declaration");
        assertTrue(comment.isXmlDeclaration());
    }

    // Test case for Comment isXmlDeclaration method - edge case
    @Test
    public void testIsXmlDeclaration_edge() {
        Comment comment = new Comment("!");
        assertFalse(comment.isXmlDeclaration());
    }

    // Test case for Comment asXmlDeclaration method - typical use case
    @Test
    public void testAsXmlDeclaration_typical() {
        Comment comment = new Comment("!xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"");
        XmlDeclaration decl = comment.asXmlDeclaration();
        assertNotNull(decl);
        assertEquals("xml", decl.nodeName());
        assertEquals("xml", decl.tagName());
        assertEquals(3, decl.attributes().size());
        assertTrue(decl.attributes().hasKey("version"));
        assertTrue(decl.attributes().hasKey("encoding"));
        assertTrue(decl.attributes().hasKey("standalone"));
        assertEquals("1.0", decl.attributes().get("version"));
        assertEquals("UTF-8", decl.attributes().get("encoding"));
        assertEquals("yes", decl.attributes().get("standalone"));
    }

    // Test case for Comment asXmlDeclaration method - error scenario
    @Test
    public void testAsXmlDeclaration_error() {
        Comment comment = new Comment("This is not an XML Declaration");
        assertNull(comment.asXmlDeclaration());
    }

}