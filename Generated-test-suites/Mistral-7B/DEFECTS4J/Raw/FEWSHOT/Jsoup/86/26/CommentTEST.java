package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;

public class CommentTest {

    @Test
    public void testCommentCreation() {
        Comment comment = new Comment("This is a test comment");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a test comment", comment.getData());
    }

    @Test
    public void testCommentCreationWithDeprecatedConstructor() {
        Comment comment = new Comment("This is a test comment", null);
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a test comment", comment.getData());
    }

    @Test
    public void testCommentOuterHtml() throws IOException {
        Comment comment = new Comment("This is a test comment");
        StringBuilder sb = new StringBuilder();
        comment.outerHtmlHead(sb, 0, null);
        comment.outerHtmlTail(sb, 0, null);
        String expected = "<!--This is a test comment-->";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testCommentToString() {
        Comment comment = new Comment("This is a test comment");
        assertEquals(comment.outerHtml(), comment.toString());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("This is a test comment");
        assertFalse(comment.isXmlDeclaration());

        Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(xmlDeclarationComment.isXmlDeclaration());

        Comment xmlProcessingInstructionComment = new Comment("<?xml-processing-instruction=\"syntax:foo\"?>");
        assertTrue(xmlProcessingInstructionComment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration xmlDeclaration = xmlDeclarationComment.asXmlDeclaration();
        assertSame(xmlDeclarationComment, xmlDeclaration.parent());
        assertSame("#comment", xmlDeclaration.parent().nodeName());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.getData());
        assertEquals("version", xmlDeclaration.name());
        assertEquals("1.0", xmlDeclaration.version());
        assertNull(xmlDeclaration.attributes());

        Comment xmlDeclarationWithAttributesComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">");
        XmlDeclaration xmlDeclarationWithAttributes = xmlDeclarationWithAttributesComment.asXmlDeclaration();
        assertSame(xmlDeclarationWithAttributesComment, xmlDeclarationWithAttributes.parent());
        assertSame("#comment", xmlDeclarationWithAttributes.parent().nodeName());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclarationWithAttributes.getData());
        assertEquals("version", xmlDeclarationWithAttributes.name());
        assertEquals("1.0", xmlDeclarationWithAttributes.version());
        Attributes attributes = xmlDeclarationWithAttributes.attributes();
        assertEquals(2, attributes.size());
        assertEquals("xmlns:xsi", attributes.keyAt(0));
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", attributes.valueAt(0));
        assertEquals("xmlns:xsd", attributes.keyAt(1));
        assertEquals("http://www.w3.org/2001/XMLSchema", attributes.valueAt(1));
    }

    @Test
    public void testAsXmlDeclarationWithInvalidXmlDeclaration() {
        Comment invalidXmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?> foo");
        assertNull(invalidXmlDeclarationComment.asXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationWithEmptyComment() {
        Comment emptyComment = new Comment("");
        assertNull(emptyComment.asXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationWithNoXmlDeclaration() {
        Comment noXmlDeclarationComment = new Comment("This is a test comment");
        assertNull(noXmlDeclarationComment.asXmlDeclaration());
    }
}