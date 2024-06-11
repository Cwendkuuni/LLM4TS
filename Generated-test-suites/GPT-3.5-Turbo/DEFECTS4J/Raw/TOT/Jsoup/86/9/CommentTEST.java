package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

public class CommentTest {

    // Test cases for Comment(String data)
    @Test
    public void testCommentString1() {
        Comment comment = new Comment("This is a test comment");
        assertEquals("This is a test comment", comment.getData());
    }

    // Test cases for Comment(String data, String baseUri)
    // This method is deprecated and should not be tested. No test case proposed.

    // Test cases for nodeName()
    @Test
    public void testNodeName() {
        Comment comment = new Comment("This is a test comment");
        assertEquals("#comment", comment.nodeName());
    }

    // Test cases for getData()
    @Test
    public void testData() {
        Comment comment = new Comment("This is a test comment");
        assertEquals("This is a test comment", comment.getData());
    }

    // Test cases for outerHtmlHead()
    // No test case proposed as this is an internal method.

    // Test cases for outerHtmlTail()
    // No test case proposed as this is an internal method.

    // Test cases for toString()
    @Test
    public void testToString() {
        Comment comment = new Comment("This is a test comment");
        assertEquals("<!--This is a test comment-->", comment.toString());
    }

    // Test cases for isXmlDeclaration()
    @Test
    public void testIsXmlDeclaration1() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclaration2() {
        Comment comment = new Comment("<!-- Comment -->");
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclaration3() {
        Comment comment = new Comment("<? xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(comment.isXmlDeclaration());
    }

    // Test cases for asXmlDeclaration()
    @Test
    public void testAsXmlDeclaration1() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration decl = comment.asXmlDeclaration();
        assertNotNull(decl);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", decl.outerHtml());
    }

    @Test
    public void testAsXmlDeclaration2() {
        Comment comment = new Comment("<!-- Comment -->");
        XmlDeclaration decl = comment.asXmlDeclaration();
        assertNull(decl);
    }

    @Test
    public void testAsXmlDeclaration3() {
        Comment comment = new Comment("<? xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration decl = comment.asXmlDeclaration();
        assertNull(decl);
    }

}