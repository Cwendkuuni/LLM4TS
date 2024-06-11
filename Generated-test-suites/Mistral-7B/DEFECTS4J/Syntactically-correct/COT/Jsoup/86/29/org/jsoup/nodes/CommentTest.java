import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Comment;
import static org.jsoup.node.NodeFactory.comment;
import static org.jsoup.node.NodeFactory.xmlDocument;

public class CommentTest {

    @Test
    public void testCommentConstructor() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        Assert.assertEquals(data, comment.getData());
    }

    @Test
    public void testCommentDeprecatedConstructor() {
        String data = "This is a comment";
        Comment comment = new Comment(data, null);
        Assert.assertEquals(data, comment.getData());
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("#comment");
        Assert.assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testGetData() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        Assert.assertEquals(data, comment.getData());
    }

    @Test(expected = IllegalStateException.class)
    public void testOuterHtmlHeadThrowsExceptionForLeafNode() {
        Comment comment = new Comment("#comment");
        Appendable accum = null;
        comment.outerHtmlHead(accum, 0, null);
    }

    @Test
    public void testOuterHtmlTail() {
        Comment comment = new Comment("#comment");
        Appendable accum = null;
        comment.outerHtmlTail(accum, 0, null);
    }

    @Test
    public void testToString() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        String result = comment.toString();
        Assert.assertEquals("<!--" + data + "-->", result);
    }

    @Test
    public void testIsXmlDeclarationFalse() {
        Comment comment = new Comment("This is a comment");
        Assert.assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclarationTrue() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        Assert.assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationNull() {
        Comment comment = new Comment("This is a comment");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        Assert.assertNull(xmlDeclaration);
    }

    @Test
    public void testAsXmlDeclarationValidXmlDeclaration() {
        String xmlDeclarationData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n <note>\n <to>Tove</to>\n <from>Jani</from>\n <heading>Reminder:</heading>\n <body>Don't forget me this weekend!</body>\n </note>";
        Comment comment = new Comment(xmlDeclarationData);
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        Assert.assertNotNull(xmlDeclaration);
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.getData());
    }
}