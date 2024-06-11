package org.jsoup.nodes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;

public class CommentTest {

    @Test
    public void testCommentCreation() {
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testDeprecatedCommentCreation() {
        Comment comment = new Comment("This is a comment", null);
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCommentIsXmlDeclaration() {
        Comment comment = new Comment("<?xml version='1.0' encoding='UTF-8'?>");
        assertTrue(comment.isXmlDeclaration());

        Comment nonXmlDeclaration = new Comment("This is not an XML declaration");
        assertFalse(nonXmlDeclaration.isXmlDeclaration());
    }

    @Test
    public void testCommentAsXmlDeclaration() {
        Comment xmlDeclarationComment = new Comment("<?xml version='1.0' encoding='UTF-8'?>");
        XmlDeclaration xmlDeclaration = xmlDeclarationComment.asXmlDeclaration();

        assertSame(xmlDeclarationComment, xmlDeclaration.node());
        assertEquals("<?xml version='1.0' encoding='UTF-8'?>", xmlDeclaration.getData());
        assertNotNull(xmlDeclaration.getDoctype());

        assertNull(new Comment("This is not an XML declaration").asXmlDeclaration());
    }

    @Test
    public void testCommentOuterHtml() throws IOException {
        Comment comment = new Comment("This is a comment");
        StringBuilder accum = new StringBuilder();

        comment.outerHtmlHead(accum, 0, Document.OutputSettings.defaultSettings());
        comment.outerHtmlTail(accum, 0, Document.OutputSettings.defaultSettings());

        assertEquals("<!--This is a comment-->", accum.toString());
    }

    @Test
    public void testCommentToString() {
        Comment comment = new Comment("This is a comment");
        assertEquals("<!--This is a comment-->", comment.toString());
    }

    @Test
    public void testXmlDeclarationCommentOuterHtml() throws IOException {
        Comment xmlDeclarationComment = new Comment("<?xml version='1.0' encoding='UTF-8'?>");
        StringBuilder accum = new StringBuilder();

        xmlDeclarationComment.outerHtmlHead(accum, 0, Document.OutputSettings.defaultSettings());
        xmlDeclarationComment.outerHtmlTail(accum, 0, Document.OutputSettings.defaultSettings());

        assertEquals("<!--<?xml version='1.0' encoding='UTF-8'?)-->", accum.toString());
    }

    @Test
    public void testXmlDeclarationCommentAsXmlDeclaration() {
        Comment xmlDeclarationComment = new Comment("<?xml version='1.0' encoding='UTF-8'?>");
        XmlDeclaration xmlDeclaration = xmlDeclarationComment.asXmlDeclaration();

        assertSame(xmlDeclarationComment, xmlDeclaration.node());
        assertEquals("<?xml version='1.0' encoding='UTF-8'?>", xmlDeclaration.getData());
        assertNotNull(xmlDeclaration.getDoctype());
    }

    @Test
    public void testCommentWithAttributes() {
        Attribute attr = Attribute.valueOf("test-attr", "test-value");
        Comment comment = new Comment("<!-- Test comment with an attribute -->", null);
        comment.attributes().add(attr);

        StringBuilder accum = new StringBuilder();

        comment.outerHtmlHead(accum, 0, Document.OutputSettings.defaultSettings());
        comment.outerHtmlTail(accum, 0, Document.OutputSettings.defaultSettings());

        assertEquals("<!--Test comment with an attribute test-attr=\"test-value\"-->", accum.toString());
    }
}