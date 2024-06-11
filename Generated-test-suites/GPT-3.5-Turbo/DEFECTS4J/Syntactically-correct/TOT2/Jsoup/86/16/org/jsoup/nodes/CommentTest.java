package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert;

public class CommentTest {

    @Test
    public void testCommentConstructor() {
        Comment comment = new Comment("Test Comment");
        Assert.assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testCommentConstructorWithBaseUri() {
        Comment comment = new Comment("Test Comment", "http://example.com");
        Assert.assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("Test Comment");
        Assert.assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        Assert.assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        Assert.assertNotNull(xmlDeclaration);
        Assert.assertEquals("xml", xmlDeclaration.nodeName());
    }

    @Test
    public void testOuterHtml() {
        Comment comment = new Comment("Test Comment");
        String expectedOutput = "<!--Test Comment-->";
        Assert.assertEquals(expectedOutput, comment.outerHtml());
    }

    @Test
    public void testOuterHtmlHead() {
        // Test for the outerHtmlHead method
    }

    @Test
    public void testOuterHtmlTail() {
        // Test for the outerHtmlTail method
    }

}