package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.IOException;

public class CommentTest {

    @Test
    public void testNewComment() {
        String data = "This is a comment.";
        Comment comment = new Comment(data);
        assertEquals("#comment", comment.nodeName());
        assertEquals(data, comment.getData());
        assertEquals(data, comment.outerHtml());
        assertEquals(data, comment.toString());
    }

    @Test
    public void testNewDeprecatedComment() {
        String data = "This is a comment.";
        Comment comment = new Comment(data, null);
        assertEquals("#comment", comment.nodeName());
        assertEquals(data, comment.getData());
        assertEquals(data, comment.outerHtml());
        assertEquals(data, comment.toString());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root>");
        assertTrue(comment.isXmlDeclaration());

        Comment comment2 = new Comment("<!-- This is a comment -->");
        assertFalse(comment2.isXmlDeclaration());

        Comment comment3 = new Comment("!");
        assertTrue(comment3.isXmlDeclaration());

        Comment comment4 = new Comment("?");
        assertTrue(comment4.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() throws IOException {
        String xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><child/>";
        Comment xmlComment = new Comment(xmlData);

        XmlDeclaration xmlDecl = xmlComment.asXmlDeclaration();
        assertNull(xmlDecl);

        String invalidXmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>invalidXML";
        Comment invalidXmlComment = new Comment(invalidXmlData);

        XmlDeclaration xmlDecl2 = invalidXmlComment.asXmlDeclaration();
        assertNull(xmlDecl2);

        String validXmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Comment validXmlComment = new Comment(validXmlData);

        XmlDeclaration xmlDecl3 = validXmlComment.asXmlDeclaration();
        assertSame(validXmlData, xmlDecl3.getData());
        Attributes attributes = xmlDecl3.attributes();
        assertEquals(1, attributes.size());
        assertTrue(attributes.hasKey("version"));
        assertEquals("1.0", attributes.get("version"));
        assertEquals(1, xmlDecl3.childNodeSize());
    }
}