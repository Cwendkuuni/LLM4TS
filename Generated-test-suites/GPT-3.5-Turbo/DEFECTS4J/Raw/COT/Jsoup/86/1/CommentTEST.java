import org.jsoup.nodes.Comment;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CommentTest {
    
    @Test
    public void testComment() {
        Comment comment = new Comment("This is a test comment");
        assertEquals("This is a test comment", comment.getData());
    }
    
    @Test
    public void testNodeName() {
        Comment comment = new Comment("");
        assertEquals("#comment", comment.nodeName());
    }
    
    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        assertTrue(comment.isXmlDeclaration());
    }
    
    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        XmlDeclaration declaration = comment.asXmlDeclaration();
        assertNotNull(declaration);
        assertEquals("xml", declaration.tagName());
    }
}