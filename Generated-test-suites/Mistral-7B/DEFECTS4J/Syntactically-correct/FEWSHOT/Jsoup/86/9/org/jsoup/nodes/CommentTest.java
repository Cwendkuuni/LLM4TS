package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

public class CommentTest {

    @Test
    public void testCommentCreation() {
        Comment comment = new Comment("This is a comment.");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment.", comment.getData());
    }

    @Test
    public void testCommentCreationWithBaseUri() {
        Comment comment = new Comment("This is a comment.", "http://example.com");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment.", comment.getData());
        assertEquals("http://example.com", comment.baseUri());
    }

    @Test
    public void testCommentIsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><title>Test</title></head><body>");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testCommentIsNotXmlDeclaration() {
        Comment comment = new Comment("This is a comment.");
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testCastCommentToXmlDeclaration() {
        String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><title>Test</title></head><body>";
        Comment comment = new Comment(xmlDeclaration);
        XmlDeclaration xmlDeclarationObj = comment.asXmlDeclaration();
        assertNotNull(xmlDeclarationObj);
        assertEquals("1.0", xmlDeclarationObj.getVersion());
        assertEquals("UTF-8", xmlDeclarationObj.encoding());
    }

    @Test(expected = ClassCastException.class)
    public void testCastCommentToXmlDeclarationFailed() {
        String data = "This is a comment.";
        Comment comment = new Comment(data);
        comment.asXmlDeclaration();
    }

    @Test
    public void testCommentOuterHtml() throws IOException {
        String data = "<!-- Comment -->";
        Comment comment = new Comment(data);
        Document doc = Jsoup.parse("<html><body>" + comment.outerHtml() + "</body></html>");
        Element element = doc.body().child(0);
        assertEquals("Comment", element.data());
        assertEquals("#comment", element.tagName());
    }
}