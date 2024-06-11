package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testCommentConstructor() {
        Comment comment = new Comment("Test Comment");
        assertEquals("Test Comment", comment.getData());
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("<!DOCTYPE html>");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<!DOCTYPE html>");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("!DOCTYPE", xmlDeclaration.name());
    }

    @Test
    public void testEdgeCases() {
        Comment emptyComment = new Comment("");
        assertEquals("", emptyComment.getData());
        assertFalse(emptyComment.isXmlDeclaration());
        assertNull(emptyComment.asXmlDeclaration());
    }

    @Test
    public void testExceptionHandling() {
        Comment comment = new Comment("<!DOCTYPE html>");
        assertThrows(Exception.class, () -> comment.asXmlDeclaration());
    }
}