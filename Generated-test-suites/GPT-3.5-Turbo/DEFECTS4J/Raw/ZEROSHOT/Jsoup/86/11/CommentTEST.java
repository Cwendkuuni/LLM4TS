
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class CommentTest {

    @Test
    public void testCommentStringValue() {
        String expectedData = "test comment";
        Comment comment = new Comment(expectedData);
        Assert.assertEquals(expectedData, comment.getData());
    }

    @Test
    public void testCommentNodeName() {
        String expectedNodeName = "#comment";
        Comment comment = new Comment("test comment");
        Assert.assertEquals(expectedNodeName, comment.nodeName());
    }

    @Test
    public void testCommentOuterHtmlHead() throws IOException {
        Comment comment = new Comment("test comment");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        comment.outerHtmlHead(accum, 0, out);
        String expectedHtml = "<!--test comment-->";
        Assert.assertEquals(expectedHtml, accum.toString());
    }

    @Test
    public void testCommentOuterHtmlTail() throws IOException {
        Comment comment = new Comment("test comment");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        comment.outerHtmlTail(accum, 0, out);
        String expectedHtml = "";
        Assert.assertEquals(expectedHtml, accum.toString());
    }

    @Test
    public void testCommentToString() {
        String expectedHtml = "<!--test comment-->";
        Comment comment = new Comment("test comment");
        Assert.assertEquals(expectedHtml, comment.toString());
    }

    @Test
    public void testCommentIsXmlDeclaration() {
        Comment comment = new Comment("!xml declaration");
        Assert.assertTrue(comment.isXmlDeclaration());

        comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        Assert.assertTrue(comment.isXmlDeclaration());

        comment = new Comment("regular comment");
        Assert.assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testCommentAsXmlDeclaration() {
        Comment comment = new Comment("!xml declaration");

        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        Assert.assertNotNull(xmlDeclaration);
        Assert.assertEquals("!xml declaration", xmlDeclaration.getData());
        Assert.assertTrue(xmlDeclaration.attributes().isEmpty());

        comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

        xmlDeclaration = comment.asXmlDeclaration();
        Assert.assertNotNull(xmlDeclaration);
        Assert.assertEquals("?xml version=\"1.0\" encoding=\"UTF-8\"?", xmlDeclaration.getData());
        Assert.assertTrue(xmlDeclaration.attributes().isEmpty());

        comment = new Comment("regular comment");
        
        xmlDeclaration = comment.asXmlDeclaration();
        Assert.assertNull(xmlDeclaration);
    }
}
