import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CommentTest {

    @Test
    public void testCommentConstructor() {
        Comment comment = new Comment("Test Comment");
        assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testCommentDeprecatedConstructor() {
        Comment comment = new Comment("Test Comment", "http://example.com");
        assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("Test Comment");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(xmlDeclarationComment.isXmlDeclaration());

        Comment regularComment = new Comment("Test Comment");
        assertFalse(regularComment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration xmlDeclaration = xmlDeclarationComment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("xml", xmlDeclaration.attributes().get("version"));
        assertEquals("UTF-8", xmlDeclaration.attributes().get("encoding"));

        Comment regularComment = new Comment("Test Comment");
        assertNull(regularComment.asXmlDeclaration());
    }
}