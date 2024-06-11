package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.IOException;

/**
 * Test class for Comment.java
 */
public class CommentTest {

    @org.junit.Test
    public void testCommentCreation() {
        String commentData = "This is a test comment.";
        Comment comment = new Comment(commentData);
        assertEquals("#comment", comment.nodeName());
        assertEquals(commentData, comment.getData());
    }

    @org.junit.Test
    public void testCommentCreationDeprecated() {
        String commentData = "This is a test comment.";
        Comment comment = new Comment(commentData, null);
        assertEquals("#comment", comment.nodeName());
        assertEquals(commentData, comment.getData());
    }

    @org.junit.Test
    public void testCommentOuterHtml() throws IOException {
        String commentData = "This is a test comment.";
        Comment comment = new Comment(commentData);
        StringBuilder sb = new StringBuilder();
        comment.outerHtmlHead(sb, 0, null);
        assertEquals("<!--This is a test comment.-->", sb.toString());
    }

    @org.junit.Test
    public void testCommentIsXmlDeclarationTrue() {
        String commentData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Comment comment = new Comment(commentData);
        assertTrue(comment.isXmlDeclaration());
    }

    @org.junit.Test
    public void testCommentIsXmlDeclarationFalse() {
        String commentData = "This is a test comment.";
        Comment comment = new Comment(commentData);
        assertFalse(comment.isXmlDeclaration());
    }

    @org.junit.Test
    public void testCommentAsXmlDeclarationNull() {
        String commentData = "This is a test comment.";
        Comment comment = new Comment(commentData);
        assertNull(comment.asXmlDeclaration());
    }

    @org.junit.Test
    public void testCommentAsXmlDeclarationValidXmlDeclaration() throws IOException {
        String commentData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><element/></root>";
        Comment comment = new Comment(commentData);
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.getData());
    }

    @org.junit.Test
    public void testCommentAsXmlDeclarationInvalidXmlDeclaration() throws IOException {
        String commentData = "<?xml version=\"1.0\" encoding=\"UTF-8\" <tag/>?";
        Comment comment = new Comment(commentData);
        assertNull(comment.asXmlDeclaration());
    }
}