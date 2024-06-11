import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CommentTest {
    
    private Comment comment;
    
    @Before
    public void setUp() {
        comment = new Comment("This is a comment");
    }
    
    @Test
    public void testNodeName() {
        assertEquals("#comment", comment.nodeName());
    }
    
    @Test
    public void testGetData() {
        assertEquals("This is a comment", comment.getData());
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
    public void testOuterHtml() {
        String expectedHtml = "<!--This is a comment-->";
        assertEquals(expectedHtml, comment.outerHtml());
    }
    
    // Edge Cases and Exception Handling
    
    // Test empty comment
    @Test
    public void testEmptyComment() {
        Comment emptyComment = new Comment("");
        assertEquals("", emptyComment.getData());
    }
    
    // Test comment with XML declaration like content
    @Test
    public void testXmlDeclarationLikeComment() {
        Comment declarationLikeComment = new Comment("?xml version=\"1.0\" encoding=\"UTF-8\"?");
        assertTrue(declarationLikeComment.isXmlDeclaration());
        XmlDeclaration declaration = declarationLikeComment.asXmlDeclaration();
        assertNotNull(declaration);
        assertEquals("?xml", declaration.tagName());
        assertEquals(true, declarationLikeComment.outerHtml().startsWith("<!--?xml version=\"1.0\" encoding=\"UTF-8\"?-->"));
    }
    
}