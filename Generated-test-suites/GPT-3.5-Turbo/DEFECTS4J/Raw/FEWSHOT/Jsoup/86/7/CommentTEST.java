
package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testCommentValue() {
        Comment comment = new Comment("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCommentNodeName() {
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testXmlDeclaration() {
        Comment comment = new Comment("<!--?xml version='1.0' encoding='UTF-8'?-->");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testNonXmlDeclaration() {
        Comment comment = new Comment("<!-- This is not an XML declaration -->");
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<!--?xml version='1.0' encoding='UTF-8'?-->");
        XmlDeclaration declaration = comment.asXmlDeclaration();
        assertNotNull(declaration);
        assertEquals("?", declaration.nodeName());
        assertEquals("version='1.0' encoding='UTF-8'", declaration.coreValue());
    }

    @Test
    public void testInvalidXmlDeclaration() {
        Comment comment = new Comment("<!-- This is not an XML declaration -->");
        XmlDeclaration declaration = comment.asXmlDeclaration();
        assertNull(declaration);
    }
}
