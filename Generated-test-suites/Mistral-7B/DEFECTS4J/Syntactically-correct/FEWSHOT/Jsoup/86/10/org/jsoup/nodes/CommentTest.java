package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

public class CommentTest {

    @Test
    public void testNewComment() {
        String data = "This is a comment.";
        Comment comment = new Comment(data);
        assertEquals("#comment", comment.nodeName());
        assertEquals(data, comment.getData());
    }

    @Test
    public void testNewCommentWithBaseUri() {
        String data = "This is a comment.";
        Comment comment = new Comment(data, null);
        assertEquals("#comment", comment.nodeName());
        assertEquals(data, comment.getData());
    }

    @Test
    public void testEmptyComment() {
        Comment comment = new Comment("");
        assertEquals("#comment", comment.nodeName());
        assertEquals("", comment.getData());
    }

    @Test
    public void testNodeName() {
        String data = "This is a comment.";
        Comment comment = new Comment(data);
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testGetData() {
        String data = "This is a comment.";
        Comment comment = new Comment(data);
        assertEquals(data, comment.getData());
    }

    @Test
    public void testIsXmlDeclarationFalse() {
        String data = "This is a comment.";
        Comment comment = new Comment(data);
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclarationTrue() {
        String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Comment comment = new Comment(xmlDeclaration);
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationNull() {
        String data = "This is a comment.";
        Comment comment = new Comment(data);
        assertNull(comment.asXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationXmlDeclaration() {
        String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element/>\n</root>";
        Comment comment = new Comment(xmlDeclaration);
        XmlDeclaration xmlDecl = comment.asXmlDeclaration();
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDecl.xmlDecl());
        assertEquals("root", xmlDecl.name());
        assertEquals(2, xmlDecl.childNodeSize());
        assertNotSame(comment, xmlDecl);
    }
}