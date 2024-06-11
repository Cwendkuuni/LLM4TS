package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.IOException;

/**
 * Test class for Comment.java
 */
public class CommentTest {

    /**
     * Test constructor with data
     */
    @Test
    public void testCommentWithData() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        Assert.assertEquals(data, comment.getData());
    }

    /**
     * Test constructor with deprecated data and baseUri
     */
    @Test(expected = DeprecationException.class)
    public void testCommentWithDeprecatedDataAndBaseUri() {
        String data = "This is a comment";
        Comment comment = new Comment(data, "baseUri");
    }

    /**
     * Test nodeName method
     */
    @Test
    public void testNodeName() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        Assert.assertEquals("#comment", comment.nodeName());
    }

    /**
     * Test getData method
     */
    @Test
    public void testGetData() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        Assert.assertEquals(data, comment.getData());
    }

    /**
     * Test isXmlDeclaration method
     */
    @Test
    public void testIsXmlDeclaration() {
        String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Comment comment = new Comment(xmlDeclaration);
        Assert.assertTrue(comment.isXmlDeclaration());

        String commentString = "This is a comment";
        Comment comment2 = new Comment(commentString);
        Assert.assertFalse(comment2.isXmlDeclaration());
    }

    /**
     * Test asXmlDeclaration method
     */
    @Test
    public void testAsXmlDeclaration() throws IOException {
        String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><element name=\"elementName\"/></root>";
        Comment comment = new Comment(xmlDeclaration);
        XmlDeclaration xmlDeclarationObj = comment.asXmlDeclaration();

        Assert.assertNotNull(xmlDeclarationObj);
        Assert.assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclarationObj.getData());
        Assert.assertEquals("xml", xmlDeclarationObj.getVersion());
        Assert.assertTrue(xmlDeclarationObj.isStandalone());

        Element rootElement = Jsoup.parse(xmlDeclaration).body();
        for (Attribute attribute : rootElement.attributes()) {
            xmlDeclarationObj.attributes().add(attribute);
        }
    }

    /**
     * Test outerHtml method
     */
    @Test
    public void testOuterHtml() throws IOException {
        String data = "This is a comment";
        Comment comment = new Comment(data);

        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append("<!--")
                .append(data)
                .append("-->");

        String actualHtml = comment.outerHtml();
        Assert.assertEquals(expectedHtml.toString(), actualHtml);
    }
}