package org.jsoup.nodes;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.*;

public class CommentTest {

    private Comment comment;

    @Before
    public void setup() {
        comment = new Comment("This is a comment");
    }

    
    @Test
    public void testConstructorWithData() {
        assertEquals("This is a comment", comment.getData());
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testConstructorWithDataAndBaseUri() {
        Comment comment = new Comment("This is a comment", "https://www.example.com");
        assertEquals("This is a comment", comment.getData());
        assertEquals("#comment", comment.nodeName());
    }
    
    @Test
    public void testGetData() {
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        comment.outerHtmlHead(sb, 0, out);
        assertEquals("<!--This is a comment-->", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        comment.outerHtmlTail(sb, 0, out);
        assertEquals("", sb.toString());
    }

    @Test
    public void testToString() {
        assertEquals("<!--This is a comment-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment1 = new Comment("<!DOCTYPE html>");
        assertTrue(comment1.isXmlDeclaration());

        Comment comment2 = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(comment2.isXmlDeclaration());
        
        Comment comment3 = new Comment("This is not an XML Declaration");
        assertFalse(comment3.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment1 = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration decl1 = comment1.asXmlDeclaration();
        assertNotNull(decl1);
        assertEquals("xml", decl1.nodeName());

        Comment comment2 = new Comment("<!DOCTYPE html>");
        XmlDeclaration decl2 = comment2.asXmlDeclaration();
        assertNotNull(decl2);
        assertEquals("!DOCTYPE", decl2.nodeName());

        Comment comment3 = new Comment("This is not an XML Declaration");
        XmlDeclaration decl3 = comment3.asXmlDeclaration();
        assertNull(decl3);
    }

}