import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommentTest {
    
    private Comment comment;
    
    @Before
    public void setup() {
        comment = new Comment("Test comment");
    }
    
    @Test
    public void testNodeName() {
        assertEquals("#comment", comment.nodeName());
    }
    
    @Test
    public void testGetData() {
        assertEquals("Test comment", comment.getData());
    }
    
    @Test
    public void testIsXmlDeclaration() {
        assertFalse(comment.isXmlDeclaration());
    }
    
    @Test
    public void testAsXmlDeclaration() {
        assertNull(comment.asXmlDeclaration());
    }
    
    @Test
    public void testOuterHtmlHead() {
        StringBuilder sb = new StringBuilder();
        try {
            comment.outerHtmlHead(sb, 0, new Document.OutputSettings());
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("<!--Test comment-->", sb.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        StringBuilder sb = new StringBuilder();
        comment.outerHtmlTail(sb, 0, new Document.OutputSettings());
        assertEquals("", sb.toString());
    }
    
    @Test
    public void testToString() {
        assertEquals("<!--Test comment-->", comment.toString());
    }
}