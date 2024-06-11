package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import org.junit.Test;

/**
 * Test class for Comment.java
 */
public class CommentTest {

    // Test constructor

    @Test
    public void testCommentConstructor() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        assertEquals("#comment", comment.nodeName());
        assertEquals(data, comment.getData());
    }

    @Test
    public void testCommentDeprecatedConstructor() {
        String data = "This is a comment";
        Comment comment = new Comment(data, null);
        assertEquals("#comment", comment.nodeName());
        assertEquals(data, comment.getData());
    }

    // Test getters

    @Test
    public void testGetData() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        assertEquals(data, comment.getData());
    }

    // Test methods

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!-- This is a comment -->");
        assertTrue(comment.isXmlDeclaration());

        Comment comment2 = new Comment("This is a comment");
        assertFalse(comment2.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() throws IOException {
        String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!-- This is a comment -->";
        Comment comment = new Comment(xmlDeclaration);
        XmlDeclaration xmlDecl = comment.asXmlDeclaration();
        assertNotNull(xmlDecl);
        assertEquals(xmlDeclaration, xmlDecl.toString());

        Comment comment2 = new Comment("This is a comment");
        assertNull(comment2.asXmlDeclaration());
    }

    // Test edge cases and exceptions

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullData() {
        new Comment(null);
    }

    @Test(expected = NullPointerException.class)
    public void testOuterHtmlHeadWithNullAccumulator() throws IOException {
        Comment comment = new Comment("This is a comment");
        comment.outerHtmlHead(null, 0, null);
    }

    @Test(expected = IOException.class)
    public void testOuterHtmlHeadWithNullDocument() throws IOException {
        Comment comment = new Comment("This is a comment");
        comment.outerHtmlHead(new Appendable() {
            @Override
            public Appendable append(CharSequence csq) throws IOException {
                throw new IOException("Error");
            }

            @Override
            public Appendable append(Char c) throws IOException {
                throw new IOException("Error");
            }
        }, 0, null);
    }

    @Test(expected = NullPointerException.class)
    public void testOuterHtmlTailWithNullAccumulator() throws IOException {
        Comment comment = new Comment("This is a comment");
        comment.outerHtmlTail(null, 0, null);
    }

    @Test(expected = IOException.class)
    public void testOuterHtmlTailWithNullDocument() throws IOException {
        Comment comment = new Comment("This is a comment");
        comment.outerHtmlTail(new Appendable() {
            @Override
            public Appendable append(CharSequence csq) throws IOException {
                throw new IOException("Error");
            }

            @Override
            public Appendable append(Char c) throws IOException {
                throw new IOException("Error");
            }
        }, 0, null);
    }

    @Test(expected = NullPointerException.class)
    public void testParserWithNullXmlDeclaration() {
        String xmlDeclaration = null;
        Parser parser = Jsoup.parse("<" + xmlDeclaration.substring(1, xmlDeclaration.length() -1) + ">", null, null);
    }
}