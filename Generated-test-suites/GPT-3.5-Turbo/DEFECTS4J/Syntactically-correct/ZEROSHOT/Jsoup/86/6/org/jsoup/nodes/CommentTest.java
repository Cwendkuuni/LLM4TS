
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class CommentTest {

    @Test
    public void testCommentCreation() {
        Comment comment = new Comment("Test Comment");
        Assert.assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("Test Comment");
        Assert.assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testOuterHtml() throws IOException {
        Comment comment = new Comment("Test Comment");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Assert.assertEquals("<!--Test Comment-->", comment.outerHtml());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("Test Comment");
        Assert.assertEquals("<!--Test Comment-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment1 = new Comment("!DOCTYPE html");
        Assert.assertTrue(comment1.isXmlDeclaration());

        Comment comment2 = new Comment("?xml version=\"1.0\"?");
        Assert.assertTrue(comment2.isXmlDeclaration());

        Comment comment3 = new Comment("Test Comment");
        Assert.assertFalse(comment3.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment1 = new Comment("!DOCTYPE html");
        XmlDeclaration xmlDeclaration1 = comment1.asXmlDeclaration();
        Assert.assertNotNull(xmlDeclaration1);

        Comment comment2 = new Comment("?xml version=\"1.0\"?");
        XmlDeclaration xmlDeclaration2 = comment2.asXmlDeclaration();
        Assert.assertNotNull(xmlDeclaration2);

        Comment comment3 = new Comment("Test Comment");
        XmlDeclaration xmlDeclaration3 = comment3.asXmlDeclaration();
        Assert.assertNull(xmlDeclaration3);
    }
}
