import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class CommentTest {

    @Test
    public void testComment() {
        Comment comment = new Comment("This is a comment.");
        Assert.assertEquals("This is a comment.", comment.getData());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("<!DOCTYPE html>");
        Assert.assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<!DOCTYPE html>");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        Assert.assertNotNull(xmlDeclaration);
        Assert.assertEquals("<!DOCTYPE html>", xmlDeclaration.outerHtml());
    }

    @Test
    public void testAsXmlDeclaration_NotXmlDeclaration() {
        Comment comment = new Comment("This is not an XML declaration.");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        Assert.assertNull(xmlDeclaration);
    }
}