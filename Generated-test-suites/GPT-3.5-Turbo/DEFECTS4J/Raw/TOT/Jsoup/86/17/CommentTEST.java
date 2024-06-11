import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
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
    public void testConstructor() {
        assertNotNull(comment);
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
    public void testOuterHtmlHead() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        out.prettyPrint(true);
        
        try {
            comment.outerHtmlHead(accum, 0, out);
        } catch (IOException e) {
            fail("Exception thrown");
        }
        
        assertEquals("<!--Test comment-->", accum.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        
        try {
            comment.outerHtmlTail(accum, 0, out);
        } catch (IOException e) {
            fail("Exception thrown");
        }
        
        assertEquals("", accum.toString());
    }
    
    @Test
    public void testToString() {
        assertEquals("<!--Test comment-->", comment.toString());
    }
    
    @Test
    public void testIsXmlDeclaration() {
        assertFalse(comment.isXmlDeclaration());
        
        Comment xmlComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(xmlComment.isXmlDeclaration());
    }
    
    @Test
    public void testAsXmlDeclaration() {
        assertNull(comment.asXmlDeclaration());
        
        Comment xmlComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration xmlDeclaration = xmlComment.asXmlDeclaration();
        
        assertNotNull(xmlDeclaration);
        assertEquals("xml", xmlDeclaration.nodeName());
        assertEquals("1.0", xmlDeclaration.attr("version"));
        assertEquals("UTF-8", xmlDeclaration.attr("encoding"));
    }
}