import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

public class CommentTest {

    @Test
    public void testComment() {
        // Test constructor
        Comment comment = new Comment("Test comment");
        assertEquals(comment.getData(), "Test comment");
    }

    @Test
    public void testConstructorWithBaseUri() {
        // Test deprecated constructor
        Comment comment = new Comment("Test comment", "http://example.com");
        assertEquals(comment.getData(), "Test comment");
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("Test comment");
        assertEquals(comment.nodeName(), "#comment");
    }

    @Test
    public void testGetData() {
        Comment comment = new Comment("Test comment");
        assertEquals(comment.getData(), "Test comment");
    }

    @Test
    public void testOuterHtmlHead() {
        // Create a dummy Appendable object for testing
        StringBuilder sb = new StringBuilder();
        Comment comment = new Comment("Test comment");
        Document.OutputSettings out = new Document.OutputSettings();
        out.prettyPrint(true);
        try {
            comment.outerHtmlHead(sb, 0, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(sb.toString(), "<!--Test comment-->");
    }

    @Test
    public void testOuterHtmlTail() {
        // Create a dummy Appendable object for testing
        StringBuilder sb = new StringBuilder();
        Comment comment = new Comment("Test comment");
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            comment.outerHtmlTail(sb, 0, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(sb.toString(), "");
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("Test comment");
        assertEquals(comment.toString(), "<!--Test comment-->");
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("! Test comment");
        assertTrue(comment.isXmlDeclaration());

        Comment comment2 = new Comment("? Test comment");
        assertTrue(comment2.isXmlDeclaration());

        Comment comment3 = new Comment("Test comment");
        assertTrue(!comment3.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("! Test comment");
        XmlDeclaration declaration = comment.asXmlDeclaration();
        assertNull(declaration);

        Comment comment2 = new Comment("!xml version=\"1.0\" encoding=\"UTF-8\"");
        XmlDeclaration declaration2 = comment2.asXmlDeclaration();
        assertEquals(declaration2.tagName(), "!xml");
        assertEquals(declaration2.hasAttributes(), true);
    }
}