import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommentTest {
    private Comment comment;

    @Before
    public void setUp() {
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

    // Additional test cases for potential edge cases and exceptions

    @Test
    public void testGetDataWithEmptyComment() {
        Comment emptyComment = new Comment("");
        assertEquals("", emptyComment.getData());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCommentWithDataNull() {
        Comment nullDataComment = new Comment(null);
    }

    @Test
    public void testIsXmlDeclarationWithValidXmlDeclaration() {
        Comment xmlDeclaration = new Comment("!xml version=\"1.0\" encoding=\"UTF-8\"?");
        assertTrue(xmlDeclaration.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationWithValidXmlDeclaration() {
        Comment xmlDeclaration = new Comment("!xml version=\"1.0\" encoding=\"UTF-8\"?");
        XmlDeclaration declaration = xmlDeclaration.asXmlDeclaration();
        assertNotNull(declaration);
        assertEquals("xml", declaration.nodeName());
        assertEquals("1.0", declaration.attr("version"));
        assertEquals("UTF-8", declaration.attr("encoding"));
    }

    @Test
    public void testAsXmlDeclarationWithInvalidXmlDeclaration() {
        Comment invalidXmlDeclaration = new Comment("Test comment");
        assertNull(invalidXmlDeclaration.asXmlDeclaration());
    }
    
    // 
    // All above test cases are included in this complete JUnit 4 test file

    // 
}