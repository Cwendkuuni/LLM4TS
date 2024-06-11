
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommentTest {

    private Comment comment;

    @Before
    public void setUp() {
        comment = new Comment("test comment");
    }

    @Test
    public void testConstructor() {
        String data = comment.getData();
        assertEquals("test comment", data);
    }

    @Test
    public void testNodeName() {
        String nodeName = comment.nodeName();
        assertEquals("#comment", nodeName);
    }

    @Test
    public void testGetData() {
        String data = comment.getData();
        assertEquals("test comment", data);
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder accum = new StringBuilder();
        try {
            comment.outerHtmlHead(accum, 0, new Document.OutputSettings());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals("<!--test comment-->", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        try {
            comment.outerHtmlTail(accum, 0, new Document.OutputSettings());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals("", accum.toString());
    }

    @Test
    public void testToString() {
        String str = comment.toString();
        assertEquals("<!--test comment-->", str);
    }

    @Test
    public void testIsXmlDeclaration() {
        boolean isXmlDecl = comment.isXmlDeclaration();
        assertFalse(isXmlDecl);
    }

    @Test
    public void testAsXmlDeclaration() {
        XmlDeclaration decl = comment.asXmlDeclaration();
        assertNull(decl);
    }
}
