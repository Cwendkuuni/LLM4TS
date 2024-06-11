package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

public class CommentTest {

    @Test
    public void testNodeName() {
        Comment comment = new Comment("comment data");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testGetData() {
        Comment comment = new Comment("comment data");
        assertEquals("comment data", comment.getData());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment1 = new Comment("comment data");
        assertFalse(comment1.isXmlDeclaration());

        Comment comment2 = new Comment("!-- XML declaration --");
        assertTrue(comment2.isXmlDeclaration());

        Comment comment3 = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(comment3.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("!-- XML declaration --");
        assertNull(comment.asXmlDeclaration());

        Comment xmlDeclarationComment1 = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration xmlDeclaration1 = xmlDeclarationComment1.asXmlDeclaration();
        assertNotNull(xmlDeclaration1);
        assertEquals("version", xmlDeclaration1.name());
        assertEquals("1.0", xmlDeclaration1.version());

        Comment xmlDeclarationComment2 = new Comment("<?xml version=\"1.0\"?>");
        XmlDeclaration xmlDeclaration2 = xmlDeclarationComment2.asXmlDeclaration();
        assertNotNull(xmlDeclaration2);
        assertEquals("version", xmlDeclaration2.name());
        assertEquals("1.0", xmlDeclaration2.version());

        Comment xmlDeclarationComment3 = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
        XmlDeclaration xmlDeclaration3 = xmlDeclarationComment3.asXmlDeclaration();
        assertNotNull(xmlDeclaration3);
        assertEquals("version", xmlDeclaration3.name());
        assertEquals("1.0", xmlDeclaration3.version());
        assertEquals("no", xmlDeclaration3.standalone());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("comment data");
        assertEquals("<!--comment data-->", comment.toString());
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        Comment comment = new Comment("comment data");
        Appendable accum = new StringBuilder();

        comment.outerHtmlHead(accum, 0, Document.OutputSettings.prettyPrint());

        assertEquals("<!--comment data-->", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        Comment comment = new Comment("comment data");

        Appendable accum = new StringBuilder();
        comment.outerHtmlHead(accum, 0, Document.OutputSettings.prettyPrint());
        comment.outerHtmlTail(accum, 0, Document.OutputSettings.prettyPrint());

        assertEquals("<!--comment data-->", accum.toString());
    }
}