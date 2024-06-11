import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testNodeName() {
        Comment comment = new Comment("");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testGetData() {
        Comment comment = new Comment("Test Comment");
        assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testOuterHtml() {
        Document doc = Jsoup.parse("<div></div>");
        Comment comment = new Comment("Test Comment");
        doc.appendChild(comment);
        assertEquals("<!--Test Comment-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
    }
    
    // Additional test cases for edge cases and exception handling scenarios
    
    @Test
    public void testConstructorWithData() {
        Comment comment = new Comment("Test Comment");
        assertEquals("Test Comment", comment.getData());
    }
    
    @Test
    public void testConstructorWithDataAndBaseUri() {
        Comment comment = new Comment("Test Comment", "http://example.com");
        assertEquals("Test Comment", comment.getData());
    }
    
    @Test
    public void testOuterHtmlWithPrettyPrint() {
        Comment comment = new Comment("Test Comment");
        Document.OutputSettings out = new Document.OutputSettings();
        out.prettyPrint(true);
        String expected = "<!--Test Comment-->";
        assertEquals(expected, comment.outerHtml());
    }
    
    @Test
    public void testOuterHtmlWithoutPrettyPrint() {
        Comment comment = new Comment("Test Comment");
        Document.OutputSettings out = new Document.OutputSettings();
        out.prettyPrint(false);
        String expected = "<!--Test Comment-->";
        assertEquals(expected, comment.outerHtml());
    }
    
    @Test
    public void testIsXmlDeclarationFalse() {
        Comment comment = new Comment("Not an XML declaration");
        assertFalse(comment.isXmlDeclaration());
    }
    
    @Test
    public void testAsXmlDeclarationNull() {
        Comment comment = new Comment("Not an XML declaration");
        assertNull(comment.asXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationInvalidXml() {
        Comment comment = new Comment("<?xml");
        assertNull(comment.asXmlDeclaration());
    }
}

