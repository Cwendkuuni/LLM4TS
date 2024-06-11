import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testNodeName() {
        Comment comment = new Comment("Test comment");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testGetData() {
        Comment comment = new Comment("Test comment");
        assertEquals("Test comment", comment.getData());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration declaration = comment.asXmlDeclaration();
        assertNotNull(declaration);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", declaration.outerHtml());
    }
    
    @Test
    public void testOuterHtml() {
        Comment comment = new Comment("Test comment");
        assertEquals("<!--Test comment-->", comment.outerHtml());
    }
}