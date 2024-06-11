import org.jsoup.nodes.Comment;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testCommentConstructorWithData() {
        Comment comment = new Comment("Test Comment");
        assertNotNull(comment);
        assertEquals("#comment", comment.nodeName());
        assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testCommentIsXmlDeclaration() {
        Comment comment = new Comment("<!--?xml version=\"1.0\" encoding=\"UTF-8\"?-->");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testCommentAsXmlDeclaration() {
        Comment comment = new Comment("<!--?xml version=\"1.0\" encoding=\"UTF-8\"?-->");
        assertNotNull(comment.asXmlDeclaration());
    }

}