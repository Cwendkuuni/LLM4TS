package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class CommentTest {

    @Test
    public void testCommentConstructor() {
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCommentDeprecatedConstructor() {
        Comment comment = new Comment("This is a comment", null);
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCommentIsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(comment.isXmlDeclaration());

        Comment comment2 = new Comment("This is a comment");
        assertFalse(comment2.isXmlDeclaration());
    }

    @Test
    public void testCommentAsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><element/>");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.getData());
        assertEquals("version", xmlDeclaration.name());

        Comment comment2 = new Comment("This is a comment");
        assertNull(comment2.asXmlDeclaration());
    }

    @Test
    public void testCommentOuterHtmlHead() throws IOException {
        Comment comment = new Comment("This is a comment");
        Appendable appendable = null; // You should provide a valid Appendable here
        int depth = 0;
        Document.OutputSettings out = null; // You should provide a valid Document.OutputSettings here

        comment.outerHtmlHead(appendable, depth, out);

        String expected = "-->\n"; // You should assert this output based on your requirements
        assertTrue(expected.equals(appendable.toString()));
        assertNull(appendable.toString()); // You may want to assert other things based on your requirements
    }

    @Test
    public void testCommentOuterHtmlTail() throws IOException {
        Comment comment = new Comment("This is a comment");
        Appendable appendable = null; // You should provide a valid Appendable here
        int depth = 0;
        Document.OutputSettings out = null; // You should provide a valid Document.OutputSettings here

        comment.outerHtmlTail(appendable, depth, out);

        String expected = ""; // You should assert this output based on your requirements
        assertTrue(expected.equals(appendable.toString()));
        assertNull(appendable.toString()); // You may want to assert other things based on your requirements
    }
}