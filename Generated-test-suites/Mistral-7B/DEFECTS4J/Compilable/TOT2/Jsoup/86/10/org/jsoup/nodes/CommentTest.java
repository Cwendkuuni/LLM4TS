package org.jsoup.nodes;
import static org.junit.Assert.*;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;

public class CommentTest {

    @org.junit.Test
    public void testCommentWithData() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        String commentName = comment.nodeName();
        String commentData = comment.getData();
        assertEquals("#comment", commentName);
        assertEquals(data, commentData);
    }

    // Add similar test cases for other methods
}