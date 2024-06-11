package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CommentTest {

    @Test
    public void testGetData() {
        Comment comment = new Comment("Hello World");
        String actualData = comment.getData();
        String expectedData = "Hello World";
        assertEquals(expectedData, actualData);
    }
}