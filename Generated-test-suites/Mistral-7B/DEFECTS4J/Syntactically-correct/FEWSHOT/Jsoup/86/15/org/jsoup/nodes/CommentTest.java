package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

public class CommentTest {

    @Test
    public void testCreateComment() {
        Comment comment = new Comment("This is a comment.");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment.", comment.getData());
    }

    @Test
    public void testCreateDeprecatedComment() {
        Comment comment = new Comment("This is a comment.", null);
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment.", comment.getData());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("This is a comment.");
        assertFalse(comment.isXmlDeclaration());

        Comment xmlDeclaration = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(xmlDeclaration.isXmlDeclaration());

        Comment xmlProcessingInstruction = new Comment("<?xml-model href=\"model.xml\" type=\"model\"?>");
        assertTrue(xmlProcessingInstruction.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment xmlDeclaration = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration result = xmlDeclaration.asXmlDeclaration();
        assertSame(XmlDeclaration.class, result.getClass());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", result.toString());

        Comment emptyXmlDeclaration = new Comment("<?xml?>");
        assertNull(emptyXmlDeclaration.asXmlDeclaration());

        Comment invalidXmlDeclaration = new Comment("<?xml version=\"1.0\" encoding UTF-8?>"); // Missing quotes in encoding
        assertNull(invalidXmlDeclaration.asXmlDeclaration());
    }

    @Test
    public void testOuterHtml() throws IOException {
        Comment comment = new Comment("This is a comment.");
        String expectedHtml = "<!--This is a comment.-->";
        String actualHtml = comment.outerHtml();
        assertEquals(expectedHtml, actualHtml);
    }

    @Test
    public void testOuterHtmlWithPrettyPrint() throws IOException {
        Comment comment = new Comment("This is a comment.");
        Document doc = Jsoup.parse("<html><head></head><body><!--This is a comment--></body></html>");
        doc.outputSettings().prettyPrint(true);
        String expectedHtml = "<!--\nThis is a comment.\n-->";
        String actualHtml = comment.outerHtml(doc.outputSettings());
        assertEquals(expectedHtml, actualHtml);
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("This is a comment.");
        assertEquals("<!--This is a comment.-->", comment.toString());
    }

    @Test
    public void testXmlDeclarationAsAttributes() throws IOException {
        Comment xmlDeclaration = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration xml = xmlDeclaration.asXmlDeclaration();
        Attributes attributes = new Attributes();
        for (String key : xml.attributes().keySet()) {
            attributes.put(key, xml.attributes().get(key));
        }
        Comment comment = new Comment("", attributes);
        assertEquals(xml.toString(), comment.toString());
    }
}