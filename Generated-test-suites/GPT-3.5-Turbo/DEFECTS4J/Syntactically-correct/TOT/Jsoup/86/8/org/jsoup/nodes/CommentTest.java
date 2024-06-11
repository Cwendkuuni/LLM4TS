
package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommentTest {

    @Test
    public void testCommentNodeCreationWithData() {
        // Typical use case
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());

        // Edge case: Empty data
        Comment emptyComment = new Comment("");
        assertEquals("", emptyComment.getData());

        // Error scenario: Null data
        try {
            Comment nullComment = new Comment(null);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testCommentNodeCreationWithDataAndBaseUri() {
        // Typical use case
        Comment comment = new Comment("This is a comment", "http://example.com");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());

        // Edge case: Empty data
        Comment emptyComment = new Comment("", "http://example.com");
        assertEquals("", emptyComment.getData());

        // Error scenario: Null data
        try {
            Comment nullComment = new Comment(null, "http://example.com");
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testOuterHtmlHead() {
        // Typical use case
        Comment comment = new Comment("This is a comment");
        String expectedOutput = "<!--This is a comment-->";
        StringBuilder accum = new StringBuilder();
        try {
            comment.outerHtmlHead(accum, 0, new Document.OutputSettings());
        } catch (Exception e) {
            Assert.fail("Unexpected exception");
        }
        assertEquals(expectedOutput, accum.toString());

        // Edge case: Empty comment
        Comment emptyComment = new Comment("");
        expectedOutput = "<!---->";
        accum = new StringBuilder();
        try {
            emptyComment.outerHtmlHead(accum, 0, new Document.OutputSettings());
        } catch (Exception e) {
            Assert.fail("Unexpected exception");
        }
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        // No specific case to test for this method as it does not have any logic.
        // It should be an empty method.
    }

    @Test
    public void testIsXmlDeclaration() {
        // Typical use case: Comment is an XML declaration
        Comment xmlDeclaration = new Comment("?xml version=\"1.0\"?");
        assertTrue(xmlDeclaration.isXmlDeclaration());

        // Typical use case: Comment is not an XML declaration
        Comment notXmlDeclaration = new Comment("This is not an XML declaration");
        assertFalse(notXmlDeclaration.isXmlDeclaration());

        // Edge case: Empty comment
        Comment emptyComment = new Comment("");
        assertFalse(emptyComment.isXmlDeclaration());

        // Error scenario: Null comment
        Comment nullComment = new Comment(null);
        assertFalse(nullComment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        // Typical use case: Comment is an XML declaration
        Comment xmlDeclaration = new Comment("!DOCTYPE html");
        XmlDeclaration expectedDeclaration = new XmlDeclaration("!DOCTYPE", true);
        XmlDeclaration actualDeclaration = xmlDeclaration.asXmlDeclaration();
        assertEquals(expectedDeclaration.toString(), actualDeclaration.toString());

        // Typical use case: Comment is not an XML declaration
        Comment notXmlDeclaration = new Comment("This is not an XML declaration");
        XmlDeclaration actualDeclaration2 = notXmlDeclaration.asXmlDeclaration();
        assertNull(actualDeclaration2);

        // Edge case: Empty comment
        Comment emptyComment = new Comment("");
        XmlDeclaration actualDeclaration3 = emptyComment.asXmlDeclaration();
        assertNull(actualDeclaration3);

        // Error scenario: Null comment
        Comment nullComment = new Comment(null);
        XmlDeclaration actualDeclaration4 = nullComment.asXmlDeclaration();
        assertNull(actualDeclaration4);
    }
}

