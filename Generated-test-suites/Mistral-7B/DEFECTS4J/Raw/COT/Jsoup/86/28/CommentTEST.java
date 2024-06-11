import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Comment;
import static org.jsoup.node.NodeFactory.newComment;
import static org.jsoup.node.NodeFactory.newXmlDeclaration;

public class CommentTest {

    @Test
    public void testCommentConstructor() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        Assert.assertEquals("#comment", comment.nodeName());
        Assert.assertEquals(data, comment.getData());
    }

    @Test
    public void testCommentDeprecatedConstructor() {
        String data = "This is a comment";
        Comment comment = new Comment(data, null);
        Assert.assertEquals("#comment", comment.nodeName());
        Assert.assertEquals(data, comment.getData());
    }

    @Test
    public void testCommentOuterHtml() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        StringBuilder expected = new StringBuilder();
        expected.append("<!--");
        expected.append(data);
        expected.append("-->");

        StringBuilder actual = new StringBuilder();
        comment.outerHtmlHead(actual, 0, null);
        comment.outerHtmlTail(actual, 0, null);

        Assert.assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testCommentToString() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        Assert.assertEquals(comment.outerHtml(), comment.toString());
    }

    @Test
    public void testCommentIsXmlDeclaration() {
        Comment comment = newComment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        Assert.assertTrue(comment.isXmlDeclaration());

        comment = newComment("This is a comment");
        Assert.assertFalse(comment.isXmlDeclaration());
    }

    @Test(expected = IllegalStateException.class)
    public void testCommentAsXmlDeclarationNullData() {
        Comment comment = newComment("");
        comment.asXmlDeclaration();
    }

    @Test
    public void testCommentAsXmlDeclarationValidData() {
        String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><element>Value</element></root>";
        Comment comment = newComment(data);
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        Assert.assertNotNull(xmlDeclaration);
    }
}