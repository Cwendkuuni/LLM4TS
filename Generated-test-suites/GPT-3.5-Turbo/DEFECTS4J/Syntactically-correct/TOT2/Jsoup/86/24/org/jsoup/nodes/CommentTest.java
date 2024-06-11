import org.jsoup.nodes.Comment;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class CommentTest {

    @Test
    public void testCommentConstructor() {
        Comment comment = new Comment("Test Comment");
        assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testCommentNodeName() {
        Comment comment = new Comment("Test Comment");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("<!--Test XML Declaration-->");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<!DOCTYPE html>");
        assertNotNull(comment.asXmlDeclaration());
    }

    // Edge case testing for outerHtmlHead method
    @Test
    public void testOuterHtmlHeadWithPrettyPrint() {
        Comment comment = new Comment("Test Comment");
        // Test prettyPrint logic
        assertEquals("<!--Test Comment-->", comment.outerHtml());
    }

    // Exception handling scenario testing for outerHtmlHead method
    @Test(expected = IOException.class)
    public void testOuterHtmlHeadException() throws IOException {
        Comment comment = new Comment("Test Comment");
        comment.outerHtmlHead(null, 0, null);
    }

}