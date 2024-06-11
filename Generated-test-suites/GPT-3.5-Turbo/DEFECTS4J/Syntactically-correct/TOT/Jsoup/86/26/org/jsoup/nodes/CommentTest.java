
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import static org.junit.Assert.*;
import org.junit.Test;

public class CommentTest {
    
    @Test
    public void testCommentConstructorWithData() {
        Comment comment = new Comment("test comment");
        assertEquals("test comment", comment.getData());
    }
    
    @Test
    public void testIsXmlDeclarationWithDataStartingWithExclamation() {
        Comment comment = new Comment("!DOCTYPE");
        assertTrue(comment.isXmlDeclaration());
    }
    
    @Test
    public void testIsXmlDeclarationWithDataStartingWithQuestionMark() {
        Comment comment = new Comment("?xml");
        assertTrue(comment.isXmlDeclaration());
    }
    
    @Test
    public void testIsXmlDeclarationWithDataEmpty() {
        Comment comment = new Comment("");
        assertFalse(comment.isXmlDeclaration());
    }
    
    @Test
    public void testAsXmlDeclarationWithDataStartingWithExclamation() {
        Comment comment = new Comment("!DOCTYPE");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("!DOCTYPE", xmlDeclaration.getData());
    }
    
    @Test
    public void testAsXmlDeclarationWithDataStartingWithQuestionMark() {
        Comment comment = new Comment("?xml");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("?xml", xmlDeclaration.getData());
    }
}
