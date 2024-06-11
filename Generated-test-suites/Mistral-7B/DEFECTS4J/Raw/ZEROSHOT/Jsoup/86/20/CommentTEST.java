package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

/**
 * Test class for Comment.java
 */
public class CommentTest {

    @Test
    public void testCommentCreation() {
        Comment comment = new Comment("This is a comment.");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment.", comment.getData());
    }

    @Test
    public void testCommentCreationWithBaseUri() {
        Comment comment = new Comment("This is a comment.", "/baseUri");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment.", comment.getData());
        assertEquals("/baseUri", comment.baseUri());
    }

    @Test
    public void testCommentOuterHtml() {
        Comment comment = new Comment("This is a comment.");
        StringBuilder out = new StringBuilder();
        comment.outerHtmlHead(out, 0, null);
        comment.outerHtmlTail(out, 0, null);
        assertEquals("<!--This is a comment.-->", out.toString());
    }

    @Test
    public void testCommentToString() {
        Comment comment = new Comment("This is a comment.");
        assertEquals("<!--This is a comment.-->", comment.toString());
    }

    @Test
    public void testCommentIsXmlDeclarationFalse() {
        Comment comment = new Comment("This is a comment.");
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testCommentIsXmlDeclarationTrue() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<tag attr=\"value\"/>");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testCommentAsXmlDeclarationNull() {
        Comment comment = new Comment("This is a comment.");
        assertNull(comment.asXmlDeclaration());
    }

    @Test
    public void testCommentAsXmlDeclarationValidXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<tag attr=\"value\"/>");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertSame(XmlDeclaration.class, xmlDeclaration.getClass());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.getData());
        assertEquals("1.0", xmlDeclaration.getVersion());
        assertEquals("UTF-8", xmlDeclaration.getEncoding());
        assertEquals(0, xmlDeclaration.attributeSize());
    }

    @Test
    public void testCommentAsXmlDeclarationWithAttributes() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<tag attr1=\"value1\" attr2=\"value2\"/>");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertSame(XmlDeclaration.class, xmlDeclaration.getClass());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.getData());
        assertEquals("1.0", xmlDeclaration.getVersion());
        assertEquals("UTF-8", xmlDeclaration.getEncoding());
        assertEquals(2, xmlDeclaration.attributeSize());
        Attribute attr1 = xmlDeclaration.attribute(0);
        assertEquals("attr1", attr1.getKey());
        assertEquals("value1", attr1.getValue());
        Attribute attr2 = xmlDeclaration.attribute(1);
        assertEquals("attr2", attr2.getKey());
        assertEquals("value2", attr2.getValue());
    }

    @Test
    public void testCommentAsXmlDeclarationWithElementChild() {
        String xmlString = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n<!--This is a comment-->\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>";
        Document document = Jsoup.parse(xmlString);
        Comment comment = (Comment) document.child(1);
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertSame(XmlDeclaration.class, xmlDeclaration.getClass());
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n", xmlDeclaration.getData().substring(0, xmlDeclaration.getData().indexOf("\n<!--This is a comment-->\n")));
    }
}