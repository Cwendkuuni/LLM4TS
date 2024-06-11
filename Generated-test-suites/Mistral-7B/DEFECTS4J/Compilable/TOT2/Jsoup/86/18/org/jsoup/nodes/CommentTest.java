package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {
    @Test
    public void testCommentConstructor() {
        String data = "This is a comment.";
        Comment comment = new Comment(data);
        assertEquals("#comment", comment.nodeName());
        assertEquals(data, comment.getData());
    }
}