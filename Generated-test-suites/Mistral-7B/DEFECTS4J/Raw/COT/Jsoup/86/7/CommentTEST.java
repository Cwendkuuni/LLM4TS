package org.jsoup.nodes.test;

import org.jsoup.nodes.Comment;
import org.junit.framework.TestCase;
import org.jsoup.nodes.XmlDeclaration;

import java.io.IOException;

public class CommentTest extends TestCase {

    // Test constructor with valid data
    public void testCommentConstructor() {
        String data = "This is a comment.";
        Comment comment = new Comment(data);
        assertEquals("#comment", comment.nodeName());
        assertEquals(data, comment.getData());
    }

    // Test constructor with deprecated version
    public void testCommentConstructorDeprecated() {
        String data = "This is a comment.";
        Comment comment = new Comment(data, null);
        assertEquals("#comment", comment.nodeName());
        assertEquals(data, comment.getData());
    }

    // Test getData method
    public void testGetDataMethod() {
        String data = "This is a comment.";
        Comment comment = new Comment(data);
        assertEquals(data, comment.getData());
    }

    // Test isXmlDeclaration method
    public void testIsXmlDeclarationMethod() {
        Comment comment1 = new Comment("<!--This is a comment-->");
        assertFalse(comment1.isXmlDeclaration());

        Comment comment2 = new Comment("!--xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!--This is a comment-->");
        assertTrue(comment2.isXmlDeclaration());
    }

    // Test asXmlDeclaration method
    public void testAsXmlDeclarationMethod() {
        Comment comment = new Comment("!--xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n<title>Test Page</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("1.0", xmlDeclaration.getVersion());
        assertEquals("UTF-8", xmlDeclaration.getEncoding());
    }

    // Test outerHtml method for normal comments
    public void testOuterHtmlMethodForNormalComments() throws IOException {
        Comment comment = new Comment("This is a comment.");
        String expected = "<!--This is a comment-->";
        assertEquals(expected, comment.outerHtml());
    }

    // Test outerHtml method for xml declaration comments
    public void testOuterHtmlMethodForXmlDeclarationComments() throws IOException {
        Comment comment = new Comment("!--xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!--This is a comment-->");
        String expected = "<!--xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!--This is a comment-->";
        assertEquals(expected, comment.outerHtml());
    }

    // Test exception handling when creating a comment with null data
    public void testExceptionHandlingForNullData() {
        try {
            new Comment(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    // Test exception handling when creating a comment with empty data
    public void testExceptionHandlingForEmptyData() {
        try {
            new Comment("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    // Test exception handling when creating a comment with baseUri as argument
    public void testExceptionHandlingForBaseUriArgument() {
        try {
            new Comment("This is a comment.", "test");
            fail("Expected DeprecationException");
        } catch (DeprecationException e) {
            // expected
        }
    }
}