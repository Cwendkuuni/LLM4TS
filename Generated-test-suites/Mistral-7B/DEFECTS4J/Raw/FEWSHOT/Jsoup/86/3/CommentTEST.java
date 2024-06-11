package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.IOException;

public class CommentTest {

    @Test
    public void testCommentCreation() {
        String data = "This is a comment";
        Comment comment = new Comment(data);

        assertEquals("#comment", comment.nodeName());
        assertEquals(data, comment.getData());
    }

    @Test
    public void testDeprecatedCommentCreation() {
        String data = "This is a comment";
        Comment comment = new Comment(data, null); // deprecated

        assertEquals("#comment", comment.nodeName());
        assertEquals(data, comment.getData());
    }

    @Test
    public void testGetData() {
        String data = "This is a comment";
        Comment comment = new Comment(data);

        assertEquals(data, comment.getData());
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        String data = "This is a comment";
        Comment comment = new Comment(data);

        StringBuilder expected = new StringBuilder();
        expected.append("<!--")
                .append(data)
                .append("-->");

        StringBuilder actual = new StringBuilder();
        comment.outerHtmlHead(actual, 0, null);

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder actual = new StringBuilder();
        Comment comment = new Comment("");
        comment.outerHtmlTail(actual, 0, null);

        assertEquals("", actual.toString());
    }

    @Test
    public void testToString() {
        String data = "This is a comment";
        Comment comment = new Comment(data);

        assertEquals(comment.outerHtml(), comment.toString());
    }

    @Test
    public void testIsXmlDeclaration() {
        String xmlDecl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Comment comment = new Comment(xmlDecl);

        assertTrue(comment.isXmlDeclaration());

        String commentData = "This is a comment";
        Comment commentNotXmlDecl = new Comment(commentData);

        assertFalse(commentNotXmlDecl.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        String xmlDecl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Comment comment = new Comment(xmlDecl);

        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();

        assertSame(XmlDeclaration.class, xmlDeclaration.getClass());
        assertEquals("version=\"1.0\"", xmlDeclaration.getVersion());
        assertEquals("encoding=\"UTF-8\"", xmlDeclaration.getEncoding());
        assertEquals(comment.getData(), xmlDeclaration.getData());

        // Test invalid XML declaration
        String invalidXmlDecl = "<?xml version=\"1.0\" encoding UTF-8?>";
        Comment invalidXmlDeclComment = new Comment(invalidXmlDecl);
        XmlDeclaration invalidXmlDeclaration = invalidXmlDeclComment.asXmlDeclaration();
        assertNull(invalidXmlDeclaration);
    }
}