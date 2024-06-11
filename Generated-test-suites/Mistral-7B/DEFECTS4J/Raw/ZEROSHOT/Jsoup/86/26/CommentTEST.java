package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * Test class for Comment.java.
 *
 * @author John Doe, john.doe@example.com
 */
public class CommentTest {

    @Test
    public void testCommentConstruction() {
        // Test constructor with valid data
        Comment comment = new Comment("This is a comment.");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment.", comment.getData());

        // Test constructor with empty data
        Comment emptyComment = new Comment("");
        assertEquals("#comment", emptyComment.nodeName());
        assertEquals("", emptyComment.getData());
    }

    @Test
    public void testCommentOuterHtml() {
        Comment comment = new Comment("This is a comment.");

        StringBuilder expected = new StringBuilder();
        expected.append("<!--This is a comment.-->");

        StringBuilder actual = new StringBuilder();
        try {
            comment.outerHtml(actual, 0, null);
        } catch (IOException e) {
            fail("IOException should not be thrown.");
        }

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testCommentToString() {
        Comment comment = new Comment("This is a comment.");
        assertEquals("<!--This is a comment.-->", comment.toString());
    }

    @Test
    public void testCommentIsXmlDeclaration() {
        Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html></html>");
        assertTrue(xmlDeclarationComment.isXmlDeclaration());

        Comment comment = new Comment("This is a comment.");
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testCommentAsXmlDeclaration() {
        Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html></html>");
        XmlDeclaration xmlDeclaration = xmlDeclarationComment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("xml", xmlDeclaration.namespace());
        assertEquals("version", xmlDeclaration.attribute("version").getKey());
        assertEquals("1.0", xmlDeclaration.attribute("version").getValue());
        assertEquals("encoding", xmlDeclaration.attribute("encoding").getKey());
        assertEquals("UTF-8", xmlDeclaration.attribute("encoding").getValue());

        Comment comment = new Comment("This is a comment.");
        assertNull(comment.asXmlDeclaration());
    }
}