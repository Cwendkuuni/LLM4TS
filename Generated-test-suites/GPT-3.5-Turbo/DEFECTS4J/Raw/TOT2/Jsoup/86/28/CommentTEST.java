package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testCommentConstructor() {
        Comment comment = new Comment("This is a test comment");
        assertEquals("This is a test comment", comment.getData());
    }

    @Test
    public void testCommentNodeName() {
        Comment comment = new Comment("Test");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("<!DOCTYPE html>");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration decl = comment.asXmlDeclaration();
        assertNotNull(decl);
        assertEquals("xml", decl.name());
    }
    
    // Edge Cases and Exception Handling
    
    @Test
    public void testCommentConstructorWithEmptyData() {
        Comment comment = new Comment("");
        assertEquals("", comment.getData());
    }

    @Test
    public void testIsNotXmlDeclaration() {
        Comment comment = new Comment("Not an XML declaration");
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationWithInvalidData() {
        Comment comment = new Comment("Invalid data");
        XmlDeclaration decl = comment.asXmlDeclaration();
        assertNull(decl);
    }

}