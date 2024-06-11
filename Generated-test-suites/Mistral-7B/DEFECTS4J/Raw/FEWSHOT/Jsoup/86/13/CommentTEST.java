package org.jsoup.nodes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CommentTest {

    @Test
    public void testNewComment() {
        String data = "This is a test comment.";
        Comment comment = new Comment(data);
        assertEquals("#comment", comment.nodeName());
        assertEquals(data, comment.getData());
    }

    @Test
    public void testDeprecatedConstructor() {
        String data = "This is a test comment.";
        Comment comment = new Comment(data, null);
        assertEquals("#comment", comment.nodeName());
        assertEquals(data, comment.getData());
    }

    @Test
    public void testIsXmlDeclaration() {
        String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Comment xmlDeclarationComment = new Comment(xmlDeclaration);
        assertTrue(xmlDeclarationComment.isXmlDeclaration());

        String comment = "This is a regular comment.";
        Comment regularComment = new Comment(comment);
        assertFalse(regularComment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Comment xmlDeclarationComment = new Comment(xmlDeclaration);
        XmlDeclaration xmlDecl = xmlDeclarationComment.asXmlDeclaration();
        assertNotNull(xmlDecl);
        assertEquals("xml", xmlDecl.namespace());
        assertEquals("version", xmlDecl.xmlVersion());
        assertEquals("UTF-8", xmlDecl.encoding());
    }

    @Test
    public void testAsXmlDeclarationEmpty() {
        Comment emptyComment = new Comment("");
        assertNull(emptyComment.asXmlDeclaration());
    }

    @Test(expected = IllegalStateException.class)
    public void testAsXmlDeclarationInvalid() {
        String invalidXmlDeclaration = "<!invalidxml>";
        Comment invalidXmlDeclarationComment = new Comment(invalidXmlDeclaration);
        invalidXmlDeclarationComment.asXmlDeclaration();
    }
}