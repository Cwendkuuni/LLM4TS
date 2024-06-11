import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class CommentTest {

    @Test
    public void testCommentConstructorWithData() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        Assert.assertEquals(data, comment.getData());
        Assert.assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testCommentConstructorWithDataAndBaseUri() {
        String data = "This is a comment";
        String baseUri = "http://www.example.com";
        Comment comment = new Comment(data, baseUri);
        Assert.assertEquals(data, comment.getData());
        Assert.assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testCommentOuterHtmlHead() throws Exception {
        String data = "This is a comment";
        Comment comment = new Comment(data);

        Document.OutputSettings out = new Document.OutputSettings();
        StringBuilder accum = new StringBuilder();

        comment.outerHtmlHead(accum, 0, out);
        Assert.assertEquals("<!--This is a comment-->", accum.toString());
    }

    @Test
    public void testCommentToString() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        Assert.assertEquals("<!--This is a comment-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclarationWithXmlDeclarationData() {
        String data = "<?xml version='1.0' encoding='UTF-8'?>";
        Comment comment = new Comment(data);
        Assert.assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclarationWithNormalCommentData() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        Assert.assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationWithXmlDeclarationData() {
        String data = "<?xml version='1.0' encoding='UTF-8'?>";
        Comment comment = new Comment(data);
        XmlDeclaration decl = comment.asXmlDeclaration();
        Assert.assertNotNull(decl);
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", decl.outerHtml());
    }

    @Test
    public void testAsXmlDeclarationWithNormalCommentData() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        XmlDeclaration decl = comment.asXmlDeclaration();
        Assert.assertNull(decl);
    }
}