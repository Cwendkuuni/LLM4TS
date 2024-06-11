import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.NodeUtils;
import org.jsoup.nodes.XmlDeclaration;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {

    private Comment comment;

    @Before
    public void setup() {
        comment = new Comment("test comment");
    }

    @Test
    public void testNodeName() {
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testGetData() {
        assertEquals("test comment", comment.getData());
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            comment.outerHtmlHead(accum, 0, out);
        } catch (IOException e) {
            fail("IOException occurred");
        }
        assertEquals("<!--test comment-->", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            comment.outerHtmlTail(accum, 0, out);
        } catch (IOException e) {
            fail("IOException occurred");
        }
        assertEquals("", accum.toString());
    }

    @Test
    public void testToString() {
        assertEquals("<!--test comment-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment xmlDecl = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(xmlDecl.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment xmlDecl = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration declaration = xmlDecl.asXmlDeclaration();
        assertNotNull(declaration);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", declaration.outerHtml());
    }
}