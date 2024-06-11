
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class CommentTest {

    @Test
    public void testNodeName() {
        Comment comment = new Comment("Test Comment");
        Assert.assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testGetData() {
        Comment comment = new Comment("Test Comment");
        Assert.assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("<!DOCTYPE html>");
        Assert.assertFalse(comment.isXmlDeclaration());

        comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        Assert.assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<!DOCTYPE html>");
        Assert.assertNull(comment.asXmlDeclaration());

        comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration declaration = comment.asXmlDeclaration();

        Assert.assertNotNull(declaration);
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", declaration.outerHtml());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("Test Comment");
        Assert.assertEquals("<!--Test Comment-->", comment.toString());
    }
}
