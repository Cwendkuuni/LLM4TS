import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Comment;
import static org.jsoup.helper.Validate.isNull;
import static org.jsoup.helper.Validate.notNull;

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
    public void testEmptyCommentConstructor() {
        Comment comment = new Comment();
        Assert.assertEquals("#comment", comment.nodeName());
        Assert.assertNull(comment.getData());
    }

    @Test
    public void testCommentOuterHtmlHead() {
        Comment comment = new Comment("This is a comment");
        StringBuilder accum = new StringBuilder();
        try {
            comment.outerHtmlHead(accum, 0, null);
            String expected = "<!--This is a comment-->";
            Assert.assertEquals(expected, accum.toString());
        } catch (IOException e) {
            Assert.fail("Unexpected IOException: " + e.getMessage());
        }
    }

    @Test
    public void testCommentOuterHtmlTail() {
        Comment comment = new Comment("This is a comment");
        StringBuilder accum = new StringBuilder();
        try {
            comment.outerHtmlTail(accum, 0, null);
            Assert.assertEquals("", accum.toString());
        } catch (IOException e) {
            Assert.fail("Unexpected IOException: " + e.getMessage());
        }
    }

    @Test
    public void testCommentToString() {
        Comment comment = new Comment("This is a comment");
        String expected = "<!--This is a comment-->";
        String actual = comment.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsXmlDeclarationFalse() {
        Comment comment = new Comment("This is a comment");
        Assert.assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclarationTrue() {
        Comment comment = new Comment("!--xml version='1.0' encoding='UTF-8'?-->");
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
        Comment comment = new Comment("!--xml version='1.0' encoding='UTF-8'?-->");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        Assert.assertNotNull(xmlDeclaration);
    }
}