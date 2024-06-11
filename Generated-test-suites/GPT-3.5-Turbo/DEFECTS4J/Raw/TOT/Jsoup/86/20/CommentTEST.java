
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Comment;
import org.jsoup.parser.Parser;

import static org.junit.Assert.*;

public class CommentTest {

    private Comment comment;

    @Before
    public void setUp() {
        comment = new Comment("This is a comment");
    }

    @Test
    public void testConstructorWithData() {
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testConstructorWithDataAndBaseUri() {
        Comment deprecatedComment = new Comment("This is a deprecated comment", "http://example.com");
        assertEquals("This is a deprecated comment", deprecatedComment.getData());
    }

    @Test
    public void testNodeName() {
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testOuterHtml() {
        assertEquals("<!--This is a comment-->", comment.outerHtml());
    }

    @Test
    public void testToString() {
        assertEquals("<!--This is a comment-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclaration() {
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclarationWithDataStartingWithExclamation() {
        Comment xmlDeclarationComment = new Comment("!DOCTYPE html");
        assertTrue(xmlDeclarationComment.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclarationWithDataStartingWithQuestionMark() {
        Comment xmlDeclarationComment = new Comment("?xml version='1.0'?");
        assertTrue(xmlDeclarationComment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        assertNull(comment.asXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationWithDataStartingWithExclamation() {
        Comment xmlDeclarationComment = new Comment("!DOCTYPE html");
        assertNull(xmlDeclarationComment.asXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationWithDataStartingWithQuestionMark() {
        Comment xmlDeclarationComment = new Comment("?xml version='1.0'?");
        assertNotNull(xmlDeclarationComment.asXmlDeclaration());
        assertEquals("xml", xmlDeclarationComment.asXmlDeclaration().nodeName());
        assertEquals("version='1.0'", xmlDeclarationComment.asXmlDeclaration().attributes().toString());
        assertFalse(xmlDeclarationComment.asXmlDeclaration().isProcessingInstruction());
    }
}
