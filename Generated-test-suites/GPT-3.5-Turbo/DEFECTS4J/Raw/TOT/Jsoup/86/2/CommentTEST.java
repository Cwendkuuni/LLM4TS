
import org.jsoup.nodes.Comment;
import org.jsoup.parser.Parser;
import org.jsoup.Jsoup;

import static org.junit.Assert.*;
import org.junit.Test;

public class CommentTest {
    @Test
    public void testCommentNodeName() {
        Comment comment = new Comment("Test Comment");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testCommentGetData() {
        Comment comment = new Comment("Test Comment");
        assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testCommentIsXmlDeclaration() {
        Comment comment = new Comment("!xml declaration");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testCommentIsNotXmlDeclaration() {
        Comment comment = new Comment("Test Comment");
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testCommentAsXmlDeclaration() {
        Comment comment = new Comment("!xml declaration");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("!xml declaration", xmlDeclaration.toString());
    }

    @Test
    public void testCommentAsXmlDeclarationInvalid() {
        Comment comment = new Comment("Test Comment");
        assertNull(comment.asXmlDeclaration());
    }
    
    @Test
    public void testCommentOuterHtml() {
        Comment comment = new Comment("Test Comment");
        assertEquals("<!--Test Comment-->", comment.outerHtml());
    }
}

