import org.jsoup.nodes.Comment;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testConstructor() {
        Comment comment = new Comment("test");
        assertEquals("test", comment.getData());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("test");
        assertEquals("<!--test-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("<!--!xml version='1.0' encoding='UTF-8' ?>-->");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<!--!xml version='1.0' encoding='UTF-8' ?>-->");
        XmlDeclaration declaration = comment.asXmlDeclaration();
        assertNotNull(declaration);
        assertEquals("xml", declaration.getNodeName());
        assertTrue(declaration.getAttributes().containsKey("version"));
        assertTrue(declaration.isProcessingInstruction());
    }
}