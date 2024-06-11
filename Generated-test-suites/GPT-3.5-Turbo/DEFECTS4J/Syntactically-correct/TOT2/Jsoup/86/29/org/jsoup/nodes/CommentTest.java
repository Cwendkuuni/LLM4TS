import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class CommentTest {

    @Test
    public void testConstructorWithData() {
        Comment comment = new Comment("Test comment");
        assertEquals("Test comment", comment.getData());
    }

    @Test
    public void testConstructorWithDataAndBaseUri() {
        Comment comment = new Comment("Test comment", "https://example.com");
        assertEquals("Test comment", comment.getData());
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("Test comment");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("<!DOCTYPE html>");
        assertTrue(comment.isXmlDeclaration());

        Comment comment2 = new Comment("<!-- Comment -->");
        assertFalse(comment2.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<!DOCTYPE html>");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);

        Comment comment2 = new Comment("<!-- Comment -->");
        XmlDeclaration xmlDeclaration2 = comment2.asXmlDeclaration();
        assertNull(xmlDeclaration2);
    }
    
    @Test
    public void testOuterHtml() {
        Comment comment = new Comment("Test comment");
        assertEquals("<!--Test comment-->", comment.outerHtml());
    }

}