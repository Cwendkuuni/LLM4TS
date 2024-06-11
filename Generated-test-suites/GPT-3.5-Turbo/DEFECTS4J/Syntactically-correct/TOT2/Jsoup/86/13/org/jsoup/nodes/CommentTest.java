package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testConstructorWithData() {
        Comment comment = new Comment("test data");
        assertEquals("test data", comment.getData());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("test data");
        assertEquals("<!--test data-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("!DOCTYPE html");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("!DOCTYPE html");
        XmlDeclaration xmlDecl = comment.asXmlDeclaration();
        assertNotNull(xmlDecl);
        assertEquals("DOCTYPE", xmlDecl.nodeName());
        assertFalse(xmlDecl.isProcessingInstruction());
    }

    @Test
    public void testConstructorWithBaseUri() {
        Comment comment = new Comment("test data", "http://example.com");
        assertEquals("test data", comment.getData());
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("test data");
        assertEquals("#comment", comment.nodeName());
    }

    // Edge case: empty comment data
    @Test
    public void testEmptyComment() {
        Comment comment = new Comment("");
        assertEquals("", comment.getData());
    }

    // Edge case: comment with special characters
    @Test
    public void testSpecialCharactersComment() {
        Comment comment = new Comment("!@#$%&*()_-");
        assertEquals("!@#$%&*()_-", comment.getData());
    }

    // Error scenario: handling IOException in outerHtmlHead method
    @Test
    public void testIOExceptionInOuterHtmlHead() {
        Comment comment = new Comment("test data");
        try {
            comment.outerHtmlHead(null, 0, new Document.OutputSettings());
            fail("IOException should have been thrown");
        } catch (IOException e) {
            assertTrue(true);
        }
    }
}