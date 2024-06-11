import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Comment;
import static org.jsoup.helper.Jsoup.*; // for convenience

public class CommentTest {

    @Test
    public void testCommentConstructor() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        Assert.assertEquals(data, comment.getData());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCommentNullDataConstructor() {
        Comment comment = new Comment(null);
    }

    @Test
    public void testgetNodeName() {
        Comment comment = new Comment("comment data");
        Assert.assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testGetData() {
        Comment comment = new Comment("comment data");
        Assert.assertEquals("comment data", comment.getData());
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        Comment comment = new Comment("comment data");
        StringBuilder accum = new StringBuilder();
        comment.outerHtmlHead(accum, 0, null);
        Assert.assertEquals("<!--comment data-->", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        Comment comment = new Comment("comment data");
        StringBuilder accum = new StringBuilder();
        comment.outerHtmlTail(accum, 0, null);
        Assert.assertEquals("", accum.toString());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("comment data");
        Assert.assertEquals("<!--comment data-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment1 = new Comment("This is a comment");
        Assert.assertFalse(comment1.isXmlDeclaration());

        Comment comment2 = new Comment("!--xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n<title>Test</title>\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>");
        Assert.assertTrue(comment2.isXmlDeclaration());
    }

    @Test(expected = IllegalStateException.class)
    public void testAsXmlDeclarationNullData() {
        Comment comment = new Comment(null);
        comment.asXmlDeclaration();
    }

    @Test
    public void testAsXmlDeclarationValidXmlDeclaration() {
        Comment comment = new Comment("!--xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n<title>Test</title>\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        Assert.assertNotNull(xmlDeclaration);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAsXmlDeclarationInvalidXmlDeclaration() {
        Comment comment = new Comment("This is an invalid xml declaration");
        comment.asXmlDeclaration();
    }

}