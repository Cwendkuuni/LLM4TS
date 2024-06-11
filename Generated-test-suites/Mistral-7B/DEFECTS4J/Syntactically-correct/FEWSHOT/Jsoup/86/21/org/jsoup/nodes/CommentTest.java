package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class CommentTest {

    @Test
    public void testCommentCreation() {
        Comment c1 = new Comment("comment1");
        Comment c2 = new Comment("comment2");

        assertEquals("comment1", c1.getData());
        assertEquals("comment2", c2.getData());

        assertEquals("#comment", c1.nodeName());
        assertEquals("#comment", c2.nodeName());

        assertEquals("comment1", c1.toString());
        assertEquals("comment2", c2.toString());
    }

    @Test
    public void testCommentIsXmlDeclaration() {
        Comment c1 = new Comment("<!DOCTYPE html>");
        Comment c2 = new Comment("<!-- comment -->");

        assertTrue(c1.isXmlDeclaration());
        assertFalse(c2.isXmlDeclaration());
    }

    @Test
    public void testCommentAsXmlDeclaration() throws IOException {
        String xmlDeclaration = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">\n";
        Comment c1 = new Comment(xmlDeclaration);

        XmlDeclaration xmlDecl = c1.asXmlDeclaration();

        assertEquals("about:legacy-compat", xmlDecl.publicId());
        assertTrue(xmlDecl.isStandalone());

        Attributes attrs = xmlDecl.attributes();
        assertEquals(1, attrs.size());
        assertEquals("DOCTYPE", attrs.get("xml-model"));
        assertEquals("html", attrs.get("xml-version"));
    }

    @Test
    public void testCommentNullData() {
        Comment c1 = new Comment(null);
        assertNull(c1);
    }

    @Test
    public void testCommentEmptyData() {
        Comment c1 = new Comment("");
        assertEquals("", c1.getData());
        assertEquals("#comment", c1.nodeName());
        assertEquals("", c1.toString());
    }

    @Test
    public void testCommentWithOuterHtml() throws IOException {
        String commentData = "<!-- My Comment -->\n";
        Comment comment = new Comment(commentData);

        Document doc = Jsoup.parse("<html><body>" + commentData + "</body></html>");
        String html = comment.outerHtml();

        assertEquals("<!-- My Comment -->", html);
    }

    @Test
    public void testCommentWithoutOuterHtml() throws IOException {
        String commentData = "<!-- My Comment -->\n";
        Comment comment = new Comment(commentData);

        assertEquals("My Comment", comment.getData());
        assertEquals("#comment", comment.nodeName());
        assertEquals("<!-- My Comment -->", comment.toString());
    }

    @Test
    public void testCommentEquals() {
        Comment c1 = new Comment("comment1");
        Comment c2 = new Comment("comment1");

        assertEquals(c1, c2);
        assertEquals(c2, c1);
    }

    @Test
    public void testCommentNotEqual() {
        Comment c1 = new Comment("comment1");
        Comment c2 = new Comment("comment2");

        assertNotEquals(c1, c2);
        assertNotEquals(c2, c1);
    }
}