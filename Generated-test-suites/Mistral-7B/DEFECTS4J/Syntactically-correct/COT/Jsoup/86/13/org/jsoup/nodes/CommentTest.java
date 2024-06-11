import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class CommentTest {

    @Test
    public void testCommentConstructor() {
        Comment comment = new Comment("This is a comment");
        Assert.assertEquals("#comment", comment.nodeName());
        Assert.assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testDeprecatedCommentConstructor() {
        Comment comment = new Comment("This is a comment", "");
        Assert.assertEquals("#comment", comment.nodeName());
        Assert.assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testGetData() {
        Comment comment = new Comment("This is a comment");
        Assert.assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("<!--xml declaration-->");
        Assert.assertTrue(comment.isXmlDeclaration());

        Comment comment1 = new Comment("This is a comment");
        Assert.assertFalse(comment1.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element/>\n");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        Assert.assertNotNull(xmlDeclaration);
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.getData());

        Comment comment1 = new Comment("This is a comment");
        XmlDeclaration xmlDeclaration1 = comment1.asXmlDeclaration();
        Assert.assertNull(xmlDeclaration1);
    }
}