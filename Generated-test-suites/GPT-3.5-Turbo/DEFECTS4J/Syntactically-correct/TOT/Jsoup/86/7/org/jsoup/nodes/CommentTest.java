import org.jsoup.parser.Parser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.NodeUtils;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CommentTest {

    private Comment comment;

    @Before
    public void setUp() {
        comment = new Comment("This is a comment");
    }

    @Test
    public void testGetData() {
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testIsXmlDeclarationWithValidXmlDeclaration() {
        Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(xmlDeclarationComment.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclarationWithInvalidXmlDeclaration() {
        Comment invalidXmlDeclarationComment = new Comment("<!-- This is not an XML declaration -->");
        assertFalse(invalidXmlDeclarationComment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationWithValidXmlDeclaration() {
        Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration xmlDeclaration = xmlDeclarationComment.asXmlDeclaration();
        
        assertEquals("xml", xmlDeclaration.tagName());
        assertEquals("version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.attributes().toString());
    }

    @Test
    public void testAsXmlDeclarationWithInvalidXmlDeclaration() {
        Comment invalidXmlDeclarationComment = new Comment("<!-- This is not an XML declaration -->");
        XmlDeclaration xmlDeclaration = invalidXmlDeclarationComment.asXmlDeclaration();
        
        assertEquals(null, xmlDeclaration);
    }

    @Test
    public void testToString() {
        assertEquals("<!--This is a comment-->", comment.toString());
    }
  
}