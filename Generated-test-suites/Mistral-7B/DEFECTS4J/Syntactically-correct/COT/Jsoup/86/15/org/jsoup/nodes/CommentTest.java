import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;
import static org.jsoup.nodes.NodeFactory.newComment;
import static org.jsoup.nodes.NodeFactory.newXmlDeclaration;

public class CommentTest {

    @Test
    public void testCommentConstructor() {
        Comment comment = newComment("This is a comment");
        String expected = "#comment";
        String actual = comment.nodeName();
        assertEquals(expected, actual);

        String data = comment.getData();
        String expectedData = "This is a comment";
        assertEquals(expectedData, data);
    }

    @Test
    public void testCommentDeprecatedConstructor() {
        Comment comment = newComment("This is a comment", "/baseUri");
        String nodeName = comment.nodeName();
        String expectedNodeName = "#comment";
        assertEquals(expectedNodeName, nodeName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCommentNullConstructor() {
        newComment(null);
    }

    @Test
    public void testCommentIsXmlDeclaration() {
        Comment comment = newComment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(comment.isXmlDeclaration());

        Comment comment1 = newComment("This is a comment");
        assertFalse(comment1.isXmlDeclaration());
    }

    @Test
    public void testCommentAsXmlDeclaration() {
        String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Comment comment = newComment(xmlDeclaration);
        XmlDeclaration expectedXmlDeclaration = newXmlDeclaration(xmlDeclaration);
        XmlDeclaration actualXmlDeclaration = comment.asXmlDeclaration();
        assertEquals(expectedXmlDeclaration.text(), actualXmlDeclaration.text());
        assertEquals(expectedXmlDeclaration.getDeclaredAttributes().size(), actualXmlDeclaration.getDeclaredAttributes().size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCommentGetAttributes() {
        Comment comment = newComment("This is a comment");
        comment.attributes();
    }
}