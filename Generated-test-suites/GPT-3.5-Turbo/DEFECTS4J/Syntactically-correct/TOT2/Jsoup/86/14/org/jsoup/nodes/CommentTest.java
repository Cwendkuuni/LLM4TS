import org.jsoup.nodes.Comment;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testGetData() {
        Comment comment = new Comment("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("<!--?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?-->");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<!--?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?-->");
        assertNotNull(comment.asXmlDeclaration());
    }
}