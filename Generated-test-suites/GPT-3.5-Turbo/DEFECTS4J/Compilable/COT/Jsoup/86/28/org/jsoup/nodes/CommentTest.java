package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class CommentTest {

    // Test the constructor with one argument
    @Test
    public void testConstructorWithOneArgument() {
        Comment comment = new Comment("Test Comment");
        assertEquals("Test Comment", comment.getData());
    }

    // Test the constructor with two arguments
    @Test
    public void testConstructorWithTwoArguments() {
        Comment comment = new Comment("Test Comment", "http://example.com");
        assertEquals("Test Comment", comment.getData());
    }

    // Test the getNodeName method
    @Test
    public void testNodeName() {
        Comment comment = new Comment("Test Comment");
        assertEquals("#comment", comment.nodeName());
    }

    // Test the getData method
    @Test
    public void testGetData() {
        Comment comment = new Comment("Test Comment");
        assertEquals("Test Comment", comment.getData());
    }

    // Test the toString method
    @Test
    public void testToString() {
        Comment comment = new Comment("Test Comment");
        assertEquals("<!--Test Comment-->", comment.toString());
    }

    // Test the isXmlDeclaration method when the comment is an XML declaration
    @Test
    public void testIsXmlDeclarationTrue() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(comment.isXmlDeclaration());
    }

    // Test the isXmlDeclaration method when the comment is not an XML declaration
    @Test
    public void testIsXmlDeclarationFalse() {
        Comment comment = new Comment("Test Comment");
        assertFalse(comment.isXmlDeclaration());
    }

    // Test the asXmlDeclaration method when the comment can be parsed as an XML declaration
    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration declaration = comment.asXmlDeclaration();
        assertNotNull(declaration);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", declaration.outerHtml());
    }

    // Test the asXmlDeclaration method when the comment cannot be parsed as an XML declaration
    @Test
    public void testAsXmlDeclarationNonXml() {
        Comment comment = new Comment("Test Comment");
        assertNull(comment.asXmlDeclaration());
    }
}
