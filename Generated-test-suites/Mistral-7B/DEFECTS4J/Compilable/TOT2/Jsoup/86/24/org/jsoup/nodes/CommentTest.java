package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CommentTest {
    @Test
    public void testGetData() {
        Comment comment = new Comment("This is a comment");
        String data = comment.getData();
        assertEquals("This is a comment", data);
    }
}