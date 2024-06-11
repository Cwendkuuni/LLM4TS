import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;

public class CommentTest {

    @Test
    public void testCommentConstructor() {
        Comment comment = new Comment("This is a comment");
        Assert.assertEquals("#comment", comment.nodeName());
        Assert.assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testDeprecatedCommentConstructor() {
        Comment comment = new Comment("This is a comment", "http://example.com");
        Assert.assertEquals("#comment", comment.nodeName());
        Assert.assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCommentIsXmlDeclaration() {
        Comment comment = new Comment("<!-- XML Declaration -->");
        Assert.assertTrue(comment.isXmlDeclaration());

        comment = new Comment("--XML Declaration -->"); // Note the missing hyphen before XML
        Assert.assertFalse(comment.isXmlDeclaration());

        comment = new Comment("<!--"); // Missing XML declaration data
        Assert.assertFalse(comment.isXmlDeclaration());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidXmlDeclarationData() {
        String invalidXmlDeclarationData = "<xml"; // Missing declaration keyword
        new Comment("<!--" + invalidXmlDeclarationData + "-->");
    }

    @Test
    public void testCommentAsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element/>\n");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        Assert.assertNotNull(xmlDeclaration);
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.getData());

        comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element/>\n<!DOCTYPE html SYSTEM \"about:legacy-compat\" []>\n");
        xmlDeclaration = comment.asXmlDeclaration();
        Assert.assertNull(xmlDeclaration);
    }
}