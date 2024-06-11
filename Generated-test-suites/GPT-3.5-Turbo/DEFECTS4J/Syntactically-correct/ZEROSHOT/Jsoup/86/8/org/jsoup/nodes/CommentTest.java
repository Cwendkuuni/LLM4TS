
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class CommentTest {

    @Test
    public void testConstructorWithData() {
        Comment comment = new Comment("Test Comment");
        Assert.assertEquals(comment.getData(), "Test Comment");
    }

    @Test
    public void testConstructorWithDataAndBaseUri() {
        Comment comment = new Comment("Test Comment", "http://example.com");
        Assert.assertEquals(comment.getData(), "Test Comment");
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("Test Comment");
        Assert.assertEquals(comment.nodeName(), "#comment");
    }

    @Test
    public void testGetData() {
        Comment comment = new Comment("Test Comment");
        Assert.assertEquals(comment.getData(), "Test Comment");
    }

    @Test
    public void testOuterHtmlHead() {
        Comment comment = new Comment("Test Comment");
        StringBuilder sb = new StringBuilder();
        try {
            comment.outerHtmlHead(sb, 0, new Document.OutputSettings());
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        Assert.assertEquals(sb.toString(), "<!--Test Comment-->");
    }

    @Test
    public void testOuterHtmlTail() {
        Comment comment = new Comment("Test Comment");
        StringBuilder sb = new StringBuilder();
        try {
            comment.outerHtmlTail(sb, 0, new Document.OutputSettings());
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        Assert.assertEquals(sb.toString(), "");
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("Test Comment");
        Assert.assertEquals(comment.toString(), "<!--Test Comment-->");
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment1 = new Comment("<!-- Test Comment -->");
        Comment comment2 = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        Comment comment3 = new Comment("<!DOCTYPE html>");
        Comment comment4 = new Comment("<!ENTITY name \"value\">");

        Assert.assertFalse(comment1.isXmlDeclaration());
        Assert.assertTrue(comment2.isXmlDeclaration());
        Assert.assertTrue(comment3.isXmlDeclaration());
        Assert.assertFalse(comment4.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment1 = new Comment("<!-- Test Comment -->");
        Comment comment2 = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        Comment comment3 = new Comment("<!DOCTYPE html>");
        Comment comment4 = new Comment("<!ENTITY name \"value\">");

        Assert.assertNull(comment1.asXmlDeclaration());
        Assert.assertNotNull(comment2.asXmlDeclaration());
        Assert.assertNotNull(comment3.asXmlDeclaration());
        Assert.assertNull(comment4.asXmlDeclaration());
    }
}
