
import org.jsoup.nodes.Comment;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.*;

public class CommentTest {
  
    @Test
    public void testConstructorWithData() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        assertEquals(data, comment.getData());
    }

    @Test
    public void testConstructorWithDataAndBaseUri() {
        String data = "This is a comment";
        String baseUri = "http://example.com";
        Comment comment = new Comment(data, baseUri);
        assertEquals(data, comment.getData());
    }

    @Test
    public void testGetNodeName() {
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
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
        assertEquals("1.0", declaration.attr("version"));
        assertEquals("UTF-8", declaration.attr("encoding"));
    }
  
    @Test
    public void testAsXmlDeclaration_Invalid() {
        Comment comment = new Comment("This is not an XML declaration");
        XmlDeclaration declaration = comment.asXmlDeclaration();
        assertNull(declaration);
    }

    @Test
    public void testOuterHtml() {
        Comment comment = new Comment("This is a comment");
        assertEquals("<!--This is a comment-->", comment.outerHtml());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("This is a comment");
        assertEquals("<!--This is a comment-->", comment.toString());
    }
}
