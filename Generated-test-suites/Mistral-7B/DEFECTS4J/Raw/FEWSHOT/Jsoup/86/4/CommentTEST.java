package org.jsoup.nodes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CommentTest {

    @Test
    public void testCommentWithData() {
        String data = "This is a comment";
        Comment comment = new Comment(data);

        assertEquals("#comment", comment.nodeName());
        assertEquals(data, comment.getData());
    }

    @Test
    public void testCommentWithEmptyData() {
        Comment comment = new Comment("");

        assertEquals("#comment", comment.nodeName());
        assertNull(comment.getData());
    }

    @Test
    public void testToString() {
        String data = "This is a comment";
        Comment comment = new Comment(data);

        assertEquals("<!--" + data + "-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclaration() {
        String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Comment comment = new Comment(xmlDeclaration);

        assertTrue(comment.isXmlDeclaration());

        String notXmlDeclaration = "This is not an XML declaration";
        Comment notXmlComment = new Comment(notXmlDeclaration);

        assertFalse(notXmlComment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Comment comment = new Comment(xmlDeclaration);

        XmlDeclaration xmlDecl = comment.asXmlDeclaration();

        assertSame(XmlDeclaration.class, xmlDecl.getClass());
        assertEquals("version=\"1.0\" encoding=\"UTF-8\"", xmlDecl.data());
    }

    @Test
    public void testAsXmlDeclarationWithInvalidXmlDeclaration() {
        String invalidXmlDeclaration = "<?xml version=\"1.0\" encoding=UTF-8?>";
        Comment comment = new Comment(invalidXmlDeclaration);

        assertNull(comment.asXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationWithEmptyComment() {
        Comment comment = new Comment("");

        assertNull(comment.asXmlDeclaration());
    }
}