package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class CommentTest {

    @Test
    public void testCommentCreation() {
        Comment comment = new Comment("Hello World");
        assertEquals("#comment", comment.nodeName());
        assertEquals("Hello World", comment.getData());
    }

    @Test
    public void testCommentCreationWithBaseUri() {
        Comment comment = new Comment("Hello World", "/index.html");
        assertEquals("#comment", comment.nodeName());
        assertEquals("Hello World", comment.getData());
        assertEquals("/index.html", comment.baseUri()); // baseUri is deprecated
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("Hello World");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testGetData() {
        Comment comment = new Comment("Hello World");
        assertEquals("Hello World", comment.getData());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("--declare foo bar=\"baz\"");
        assertTrue(comment.isXmlDeclaration());

        Comment comment2 = new Comment("Hello World");
        assertFalse(comment2.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.toString());

        Comment comment2 = new Comment("Hello World");
        assertNull(comment2.asXmlDeclaration());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("Hello World");
        assertEquals("<!--Hello World-->", comment.toString());
    }
}