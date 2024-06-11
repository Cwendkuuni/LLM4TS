package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.IOException;

import org.junit.Test;

/**
 * Test class for the Comment class
 *
 * @author John Doe, john.doe@example.com
 */
public class CommentTest {

    @Test
    public void testCommentCreation() {
        // Test constructor with data
        String data = "This is a comment";
        Comment comment = new Comment(data);
        assertEquals("#comment", comment.nodeName());
        assertEquals(data, comment.getData());

        // Test constructor with deprecated data and baseUri
        String dataWithBaseUri = "This is a comment with baseUri";
        Comment commentWithBaseUri = new Comment(dataWithBaseUri, "/baseUri");
        assertEquals("#comment", commentWithBaseUri.nodeName());
        assertEquals(dataWithBaseUri, commentWithBaseUri.getData());
        assertFalse(commentWithBaseUri.hasAttr("baseUri"));
    }

    @Test
    public void testCommentOuterHtml() throws IOException {
        // Test simple comment
        String data = "This is a simple comment";
        Comment comment = new Comment(data);
        StringBuilder sb = new StringBuilder();
        comment.outerHtmlHead(sb, 0, null);
        comment.outerHtmlTail(sb, 0, null);
        String expected = "<!--" + data + "-->";
        assertEquals(expected, sb.toString());

        // Test comment with XML declaration-like data
        String xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!-- This is an XML comment -->";
        Comment xmlComment = new Comment(xmlData);
        sb.setLength(0);
        xmlComment.outerHtmlHead(sb, 0, null);
        xmlComment.outerHtmlTail(sb, 0, null);
        expected = "<!--<?xml version=\"1.0\" encoding=\"UTF-8\"?>\nThis is an XML comment-->";
        assertEquals(expected, sb.toString());

        // Test comment with indentation
        String indentedData = "  This is an indented comment";
        Comment indentedComment = new Comment(indentedData);
        sb.setLength(0);
        indentedComment.outerHtmlHead(sb, 0, null);
        indentedComment.outerHtmlTail(sb, 0, null);
        expected = "<!--\n  This is an indented comment\n-->";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testCommentIsXmlDeclaration() {
        // Test simple comment
        String data = "This is a comment";
        Comment comment = new Comment(data);
        assertFalse(comment.isXmlDeclaration());

        // Test comment with XML declaration-like data
        String xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!-- This is an XML comment -->";
        Comment xmlComment = new Comment(xmlData);
        assertTrue(xmlComment.isXmlDeclaration());
    }

    @Test
    public void testCommentAsXmlDeclaration() {
        // Test simple comment
        String data = "This is a comment";
        Comment comment = new Comment(data);
        assertNull(comment.asXmlDeclaration());

        // Test comment with XML declaration-like data
        String xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!-- <!DOCTYPE html>\n<html>\n<head>\n<title>Page title</title>\n-->";
        Comment xmlComment = new Comment(xmlData);
        XmlDeclaration xmlDecl = xmlComment.asXmlDeclaration();
        assertSame(XmlDeclaration.class, xmlDecl.getClass());
        assertEquals("html", xmlDecl.getDoctype().getName());
        assertEquals("Page title", xmlDecl.getDocumentElement().tagName());
    }
}