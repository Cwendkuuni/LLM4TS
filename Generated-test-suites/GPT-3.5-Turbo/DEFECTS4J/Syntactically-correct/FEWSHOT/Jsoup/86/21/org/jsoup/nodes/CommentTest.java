
package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {
    @Test
    public void testConstructorWithData() {
        Comment comment = new Comment("Test Comment");
        assertNotNull(comment);
        assertEquals("Test Comment", comment.getData());
        assertEquals("#comment", comment.nodeName());
        assertFalse(comment.isXmlDeclaration());
        assertNull(comment.asXmlDeclaration());
        assertEquals("<!--Test Comment-->", comment.toString());
    }

    @Test
    public void testConstructorWithDataAndBaseURI() {
        Comment comment = new Comment("Test Comment", "http://www.example.com");
        assertNotNull(comment);
        assertEquals("Test Comment", comment.getData());
        assertEquals("#comment", comment.nodeName());
        assertFalse(comment.isXmlDeclaration());
        assertNull(comment.asXmlDeclaration());
        assertEquals("<!--Test Comment-->", comment.toString());
    }

    @Test
    public void testGetData() {
        Comment comment = new Comment("Test Comment");
        assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testOuterHtmlHead() {
        Comment comment = new Comment("Test Comment");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        out.prettyPrint(true);
        try {
            comment.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            fail("Exception not expected");
        }
        assertEquals("<!--Test Comment-->", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        Comment comment = new Comment("Test Comment");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            comment.outerHtmlTail(accum, 0, out);
        } catch (Exception e) {
            fail("Exception not expected");
        }
        assertEquals("", accum.toString());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("Test Comment");
        assertEquals("<!--Test Comment-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("<!--?xml version=\"1.0\" encoding=\"UTF-8\"?-->");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<!--?xml version=\"1.0\" encoding=\"UTF-8\"?-->");
        XmlDeclaration xmlDecl = comment.asXmlDeclaration();
        assertNotNull(xmlDecl);
        assertEquals("?xml", xmlDecl.nodeName());
        assertTrue(xmlDecl.isProcessingInstruction());
        assertEquals("version=\"1.0\" encoding=\"UTF-8\"", xmlDecl.attributes().toString());
    }
}
