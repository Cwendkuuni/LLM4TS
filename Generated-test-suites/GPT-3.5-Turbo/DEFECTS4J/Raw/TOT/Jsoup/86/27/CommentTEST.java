package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testCommentConstructorWithData() {
        Comment comment = new Comment("Test comment");
        assertEquals("Test comment", comment.getData());
        assertEquals("#comment", comment.nodeName());
        assertFalse(comment.isXmlDeclaration());
        assertNull(comment.asXmlDeclaration());
    }
    
    @Test
    public void testCommentConstructorWithDataAndBaseUri() {
        Comment comment = new Comment("Test comment", "http://example.com/");
        assertEquals("Test comment", comment.getData());
        assertEquals("#comment", comment.nodeName());
        assertFalse(comment.isXmlDeclaration());
        assertNull(comment.asXmlDeclaration());
    }

    @Test
    public void testOuterHtml() {
        Comment comment = new Comment("Test comment");
        String expectedHtml = "<!--Test comment-->";
        assertEquals(expectedHtml, comment.outerHtml());
    }

    @Test
    public void testIsXmlDeclarationWithInvalidData() {
        Comment comment = new Comment("Invalid data");
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclarationWithXmlDeclarationData() {
        Comment comment = new Comment("!xml version=\"1.0\" encoding=\"UTF-8\"");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationWithInvalidData() {
        Comment comment = new Comment("Invalid data");
        assertNull(comment.asXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationWithValidXmlDeclarationData() {
        Comment comment = new Comment("!xml version=\"1.0\" encoding=\"UTF-8\"");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("xml", xmlDeclaration.nodeName());
        assertTrue(xmlDeclaration.isProcessingInstruction());
        assertEquals("xml", xmlDeclaration.tagName());
        assertEquals("version", xmlDeclaration.attributes().get(0).getKey());
        assertEquals("1.0", xmlDeclaration.attributes().get(0).getValue());
        assertEquals("encoding", xmlDeclaration.attributes().get(1).getKey());
        assertEquals("UTF-8", xmlDeclaration.attributes().get(1).getValue());
    }
}