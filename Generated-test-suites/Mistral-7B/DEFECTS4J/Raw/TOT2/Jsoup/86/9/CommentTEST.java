package org.jsoup.nodes;

import org.junit.Test;
import org.jsoup.nodes.Comment;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CommentTest {

    @Test
    public void testCommentConstructor() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        assertNotNull(comment);
        assertEquals(data, comment.getData());
    }

    @Test
    public void testCommentEmptyStringConstructor() {
        Comment comment = new Comment("");
        assertNotNull(comment);
        assertEquals("", comment.getData());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCommentNullConstructor() {
        Comment comment = new Comment(null);
    }

    @Test
    public void testCommentConstructorWithNullBaseUri() {
        String data = "This is a comment";
        Comment comment = new Comment(data, null);
        assertNotNull(comment);
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("Test Comment");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testGetData() {
        Comment comment = new Comment("Test Comment");
        assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        Comment comment = new Comment("Test Comment");
        Appendable accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        comment.outerHtmlHead(accum, 0, out);
        assertEquals("<!--Test Comment-->", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        Comment comment = new Comment("Test Comment");
        Appendable accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        comment.outerHtmlTail(accum, 0, out);
        assertEquals("", accum.toString());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("Test Comment");
        assertEquals("<!--Test Comment-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!--Test Comment-->");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!--Test Comment-->");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
    }

    @Test
    public void testIsXmlDeclarationFalse() {
        Comment comment = new Comment("This is a test comment");
        assertFalse(comment.isXmlDeclaration());
    }
}