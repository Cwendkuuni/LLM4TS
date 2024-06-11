package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class CommentTest {

    @Test
    public void testCommentCreation() {
        Comment comment = new Comment("This is a comment.");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment.", comment.getData());
    }

    @Test
    public void testCommentCreationWithDeprecatedConstructor() {
        Comment comment = new Comment("This is a comment.", null);
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment.", comment.getData());
    }

    @Test
    public void testCommentOuterHtml() throws IOException {
        String data = "This is a comment.";
        Comment comment = new Comment(data);
        Appendable accum = new StringBuilder();

        comment.outerHtmlHead(accum, 0, null);
        comment.outerHtmlTail(accum, 0, null);

        String expected = "<!--" + data + "-->";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testCommentToString() {
        String data = "This is a comment.";
        Comment comment = new Comment(data);
        assertEquals(comment.outerHtml(), comment.toString());
    }

    @Test
    public void testCommentIsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Content</p>\n</body>\n</html>");
        assertTrue(comment.isXmlDeclaration());

        Comment invalidXmlDeclaration = new Comment("This is not an xml declaration.");
        assertFalse(invalidXmlDeclaration.isXmlDeclaration());
    }

    @Test
    public void testCommentAsXmlDeclaration() {
        String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE html SYSTEM \"about:legacy-compat\">\n<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Content</p>\n</body>\n</html>";
        Comment comment = new Comment(xmlDeclaration);
        XmlDeclaration xmlDecl = comment.asXmlDeclaration();

        assertNotNull(xmlDecl);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDecl.data());
    }

    @Test
    public void testCommentAsXmlDeclarationInvalid() {
        Comment comment = new Comment("This is not an xml declaration.");
        assertNull(comment.asXmlDeclaration());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCommentAsXmlDeclarationNullData() {
        Comment comment = new Comment(null);
        comment.asXmlDeclaration();
    }
}