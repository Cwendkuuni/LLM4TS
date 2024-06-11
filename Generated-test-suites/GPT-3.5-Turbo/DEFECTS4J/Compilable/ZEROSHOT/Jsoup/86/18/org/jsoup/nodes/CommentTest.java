package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testCommentConstructor() {
        Comment comment = new Comment("Test comment");
        assertEquals("Test comment", comment.getData());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("Test comment");
        assertEquals("<!--Test comment-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment1 = new Comment("<!DOCTYPE html>");
        Comment comment2 = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        Comment comment3 = new Comment("Regular comment");

        assertTrue(comment1.isXmlDeclaration());
        assertTrue(comment2.isXmlDeclaration());
        assertFalse(comment3.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment1 = new Comment("<!DOCTYPE html>");
        Comment comment2 = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        Comment comment3 = new Comment("Regular comment");

        XmlDeclaration xmlDeclaration1 = comment1.asXmlDeclaration();
        XmlDeclaration xmlDeclaration2 = comment2.asXmlDeclaration();
        XmlDeclaration xmlDeclaration3 = comment3.asXmlDeclaration();

        assertNotNull(xmlDeclaration1);
        assertNotNull(xmlDeclaration2);
        assertNull(xmlDeclaration3);
    }

}