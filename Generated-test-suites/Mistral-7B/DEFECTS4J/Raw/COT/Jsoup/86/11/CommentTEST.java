import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;

public class CommentTest {

    @Test
    public void testCommentConstructor() {
        String data = "This is a test comment";
        Comment comment = new Comment(data);
        Assert.assertEquals(data, comment.getData());
    }

    @Test
    public void testDeprecatedCommentConstructor() {
        String data = "This is a test comment";
        Comment comment = new Comment(data, null);
        Assert.assertEquals(data, comment.getData());
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("#test");
        Assert.assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testGetData() {
        Comment comment = new Comment("This is a test comment");
        Assert.assertEquals("This is a test comment", comment.getData());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment1 = new Comment("<!-- This is a comment -->");
        Assert.assertTrue(comment1.isXmlDeclaration());

        Comment comment2 = new Comment("This is a comment");
        Assert.assertFalse(comment2.isXmlDeclaration());

        Comment comment3 = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE html SYSTEM \"about:legacy-compat\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        Assert.assertTrue(comment3.isXmlDeclaration());
    }

    @Test(expected = AssertionError.class)
    public void testAsXmlDeclarationNull() {
        Comment comment = new Comment("This is a test comment");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        Assert.assertNull(xmlDeclaration);
    }

    @Test
    public void testAsXmlDeclarationValid() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE html SYSTEM \"about:legacy-compat\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        Assert.assertNotNull(xmlDeclaration);
    }
}