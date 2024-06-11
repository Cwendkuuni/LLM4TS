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
    public void testCommentIsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>");
        assertTrue(comment.isXmlDeclaration());

        Comment nonXmlComment = new Comment("This is a regular comment.");
        assertFalse(nonXmlComment.isXmlDeclaration());
    }

    @Test
    public void testCommentAsXmlDeclaration() {
        Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>");
        XmlDeclaration xmlDeclaration = xmlDeclarationComment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.xml());
    }

    @Test
    public void testCommentAsXmlDeclarationEmpty() {
        Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\"?>\n");
        XmlDeclaration xmlDeclaration = xmlDeclarationComment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("<?xml version=\"1.0\"?>", xmlDeclaration.xml());
    }

    @Test
    public void testCommentAsXmlDeclarationNull() {
        Comment nullComment = new Comment(null);
        assertNull(nullComment.asXmlDeclaration());
    }
}