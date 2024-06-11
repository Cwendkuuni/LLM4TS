package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.IOException;

/**
 * Test class for Comment.java
 */
public class CommentTest {

    @org.junit.Test
    public void testCommentConstruction() {
        // Test constructor with valid data
        Comment comment = new Comment("This is a comment.");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment.", comment.getData());

        // Test constructor with empty string as data
        Comment emptyComment = new Comment("");
        assertEquals("#comment", emptyComment.nodeName());
        assertEquals("", emptyComment.getData());

        // Test deprecated constructor
        Comment deprecatedComment = new Comment("This is a comment.", "baseUri");
        assertEquals("#comment", deprecatedComment.nodeName());
        assertEquals("This is a comment.", deprecatedComment.getData());
    }

    @org.junit.Test
    public void testCommentOuterHtml() throws IOException {
        String html = "<html><head><!-- This is a comment --></head><body></body></html>";
        Document document = Jsoup.parse(html);
        Comment comment = document.body().child(0).child(0);

        StringBuilder expected = new StringBuilder();
        expected.append("<!--")
                .append(comment.getData())
                .append("-->");

        StringBuilder actual = new StringBuilder();
        comment.outerHtmlHead(actual, 0, null);
        comment.outerHtmlTail(actual, 0, null);

        assertEquals(expected.toString(), actual.toString());
    }

    @org.junit.Test
    public void testCommentToString() throws IOException {
        String html = "<html><head><!-- This is a comment --></head><body></body></html>";
        Document document = Jsoup.parse(html);
        Comment comment = document.body().child(0).child(0);

        String expected = comment.outerHtml();
        String actual = comment.toString();

        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testCommentIsXmlDeclaration() {
        Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(xmlDeclarationComment.isXmlDeclaration());

        Comment nonXmlDeclarationComment = new Comment("This is a comment.");
        assertFalse(nonXmlDeclarationComment.isXmlDeclaration());
    }

    @org.junit.Test
    public void testCommentAsXmlDeclaration() throws IOException {
        String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String html = "<html><head><!--" + xmlDeclaration + "--></head><body></body></html>";
        Document document = Jsoup.parse(html);
        Comment comment = document.body().child(0).child(0);

        XmlDeclaration expected = new XmlDeclaration("1.0", "UTF-8");
        Attribute versionAttribute = expected.attributes().get("version");
        Attribute encodingAttribute = expected.attributes().get("encoding");

        XmlDeclaration actual = comment.asXmlDeclaration();

        assertEquals("1.0", actual.version());
        assertEquals("UTF-8", actual.encoding());
        assertEquals(expected.attributes().size(), actual.attributes().size());
        assertEquals(versionAttribute.name(), actual.versionElement().tagName());
        assertEquals(versionAttribute.value(), actual.versionElement().text());
        assertEquals(encodingAttribute.name(), actual.encodingElement().tagName());
        assertEquals(encodingAttribute.value(), actual.encodingElement().text());

        assertNull(comment.asXmlDeclaration());
    }
}