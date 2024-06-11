package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

public class CommentTest {

    @Test
    public void testCommentCreation() {
        Comment comment = new Comment("This is a test comment.");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a test comment.", comment.getData());
    }

    @Test
    public void testCommentCreationWithEmptyString() {
        Comment comment = new Comment("");
        assertEquals("#comment", comment.nodeName());
        assertEquals("", comment.getData());
    }

    @Test
    public void testCommentNodeName() {
        Comment comment = new Comment("This is a test comment.");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testCommentgetData() {
        Comment comment = new Comment("This is a test comment.");
        assertEquals("This is a test comment.", comment.getData());
    }

    @Test
    public void testCommentOuterHtml() {
        Comment comment = new Comment("This is a test comment.");
        String expectedOutput = "<!--This is a test comment-->";
        assertEquals(expectedOutput, comment.outerHtml());
    }

    @Test
    public void testCommentOuterHtmlHead() throws IOException {
        Comment comment = new Comment("This is a test comment.");
        StringBuilder expectedOutput = new StringBuilder();
        comment.outerHtmlHead(expectedOutput, 0, null);
        String output = expectedOutput.toString();
        String expected = "<!--This is a test comment-->";
        assertEquals(expected, output);
    }

    @Test
    public void testCommentOuterHtmlTail() throws IOException {
        Comment comment = new Comment("This is a test comment.");
        StringBuilder expectedOutput = new StringBuilder();
        comment.outerHtmlTail(expectedOutput, 0, null);
        String output = expectedOutput.toString();
        assertEquals("", output);
    }

    @Test
    public void testCommentIsXmlDeclaration() {
        Comment comment = new Comment("<!DOCTYPE html SYSTEM \"about:legacy-compat\">");
        assertTrue(comment.isXmlDeclaration());

        Comment comment2 = new Comment("This is a test comment.");
        assertFalse(comment2.isXmlDeclaration());
    }

    @Test
    public void testCommentAsXmlDeclaration() {
        Comment comment = new Comment("<!DOCTYPE html SYSTEM \"about:legacy-compat\">");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("about:legacy-compat", xmlDeclaration.publicId());
        assertEquals("", xmlDeclaration.systemId());
        assertEquals("", xmlDeclaration.internalSubset());
        assertTrue(xmlDeclaration.isStandalone());

        Comment comment2 = new Comment("This is a test comment.");
        assertNull(comment2.asXmlDeclaration());
    }
}