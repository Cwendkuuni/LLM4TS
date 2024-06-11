import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;

public class CommentTest {

    @Test
    public void testCommentConstructor() {
        Comment comment = new Comment("This is a comment");
        String expected = "#comment";
        String actual = comment.nodeName();
        assertEquals(expected, actual);
        String data = comment.getData();
        assertEquals("This is a comment", data);
    }

    @Test
    public void testCommentConstructorDeprecated() {
        Comment comment = new Comment("This is a comment", "baseUri");
        String expected = "#comment";
        String actual = comment.nodeName();
        assertEquals(expected, actual);
        String data = comment.getData();
        assertEquals("This is a comment", data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCommentEmptyConstructor() {
        new Comment();
    }

    @Test
    public void testGetDataMethod() {
        Comment comment = new Comment("This is a comment");
        String data = comment.getData();
        assertEquals("This is a comment", data);
    }

    @Test
    public void testOuterHtmlHeadMethod() throws IOException {
        Comment comment = new Comment("This is a comment");
        StringBuilder expected = new StringBuilder();
        expected.append("<!--This is a comment-->");
        Document document = Jsoup.parseBodyElements(expected.toString());
        StringBuilder actual = new StringBuilder();
        comment.outerHtmlHead(actual, 0, null);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testOuterHtmlTailMethod() throws IOException {
        Comment comment = new Comment("This is a comment");
        StringBuilder actual = new StringBuilder();
        comment.outerHtmlTail(actual, 0, null);
        assertEquals("", actual.toString());
    }

    @Test
    public void testToStringMethod() {
        Comment comment = new Comment("This is a comment");
        String expected = "<!--This is a comment-->";
        String actual = comment.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsXmlDeclarationMethod() {
        Comment comment1 = new Comment("This is a comment");
        Comment comment2 = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        boolean expected1 = false;
        boolean actual1 = comment1.isXmlDeclaration();
        boolean expected2 = true;
        boolean actual2 = comment2.isXmlDeclaration();
        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    @Test
    public void testAsXmlDeclarationMethod() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<root>\r\n<element attr=\"value\"/>\r\n</root>");
        XmlDeclaration expected = new XmlDeclaration("1.0", "UTF-8", null, null, null);
        XmlDeclaration actual = comment.asXmlDeclaration();
        assertEquals(expected.toString(), actual.toString());
        assertEquals(expected.getVersion(), actual.getVersion());
        assertEquals(expected.getEncoding(), actual.getEncoding());
        assertEquals(expected.isStandalone(), actual.isStandalone());
        assertEquals(expected.getDoctypeDeclaration(), actual.getDoctypeDeclaration());
        assertEquals(expected.getDocumentElement(), actual.getDocumentElement());
    }

    @Test(expected = NullPointerException.class)
    public void testAsXmlDeclarationMethodNullBaseUri() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        comment.asXmlDeclaration();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAsXmlDeclarationMethodEmptyComment() {
        Comment comment = new Comment("");
        comment.asXmlDeclaration();
    }
}