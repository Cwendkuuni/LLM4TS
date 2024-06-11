package org.jsoup.nodes;
import static org.junit.Assert.*;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

public class CommentTest {

    @Test
    public void testCommentCreation() {
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testDeprecatedCommentCreation() {
        Comment comment = new Comment("This is a comment", "baseUri");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
    }

    // Test other methods with basic input
}