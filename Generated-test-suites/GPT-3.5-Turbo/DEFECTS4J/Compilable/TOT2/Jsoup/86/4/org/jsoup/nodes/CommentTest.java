package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testCommentConstructor() {
        Comment comment = new Comment("Test Comment");
        assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testCommentNodeName() {
        Comment comment = new Comment("Test Node Name");
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
        assertEquals("!DOCTYPE", xmlDeclaration.nodeName());
    }

    @Test
    public void testCommentToString() {
        Comment comment = new Comment("Test Comment ToString");
        assertEquals("<!--Test Comment ToString-->", comment.toString());
    }

    // Add more test cases for edge cases and error scenarios

}