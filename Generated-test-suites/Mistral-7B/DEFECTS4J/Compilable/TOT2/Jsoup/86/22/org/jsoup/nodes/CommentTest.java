package org.jsoup.nodes;
import org.junit.Test;
import org.jsoup.nodes.Comment;
import static org.junit.Assert.assertEquals;

public class CommentTest {
    @Test
    public void testCommentConstructor() {
        Comment comment = new Comment("This is a comment");
        String expected = "#comment";
        assertEquals(expected, comment.nodeName());
    }

    @Test
    public void testCommentGetData() {
        Comment comment = new Comment("This is a comment");
        String expected = "This is a comment";
        assertEquals(expected, comment.getData());
    }

    // Test cases for other methods can be added here
}