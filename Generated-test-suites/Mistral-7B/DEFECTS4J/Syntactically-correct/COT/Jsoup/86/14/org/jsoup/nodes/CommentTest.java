import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Comment;
import static org.jsoup.node.NodeFactory.comment;

public class CommentTest {

    @Test
    public void testCommentConstructor() {
        Comment comment = new Comment("This is a comment");
        String expectedData = "This is a comment";
        Assert.assertEquals(expectedData, comment.getData());
    }

    @Test
    public void testDeprecatedCommentConstructor() {
        Comment comment = new Comment("This is a comment", null);
        String expectedData = "This is a comment";
        Assert.assertEquals(expectedData, comment.getData());
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("comment data");
        String expectedNodeName = "#comment";
        Assert.assertEquals(expectedNodeName, comment.nodeName());
    }

    @Test
    public void testGetData() {
        Comment comment = new Comment("This is a comment");
        String expectedData = "This is a comment";
        Assert.assertEquals(expectedData, comment.getData());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("<?xml version='1.0' encoding='UTF-8'?>\n<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World</p>\n</body>\n</html>");
        boolean expectedResult = true;
        Assert.assertEquals(expectedResult, comment.isXmlDeclaration());

        comment = new Comment("This is a comment");
        expectedResult = false;
        Assert.assertEquals(expectedResult, comment.isXmlDeclaration());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAsXmlDeclarationNullData() {
        Comment comment = new Comment(null);
        comment.asXmlDeclaration();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAsXmlDeclarationEmptyData() {
        Comment comment = new Comment("");
        comment.asXmlDeclaration();
    }

    @Test
    public void testAsXmlDeclarationValidXmlDeclaration() {
        Comment comment = new Comment("<?xml version='1.0' encoding='UTF-8'?>");
        XmlDeclaration expectedDecl = new XmlDeclaration("1.0", null, null, null, null, null, null, null, null);
        XmlDeclaration actualDecl = comment.asXmlDeclaration();
        Assert.assertNotNull(actualDecl);
        Assert.assertEquals(expectedDecl.version(), actualDecl.version());
        Assert.assertNull(actualDecl.encoding());
        Assert.assertNull(actualDecl.standalone());
        Assert.assertNull(actualDecl.doctypeDeclaration());
        Assert.assertNull(actualDecl.declaredAttributes());
        Assert.assertNull(actualDecl.xmlLang());
        Assert.assertNull(actualDecl.xmlNS());
        Assert.assertNull(actualDecl.xmlBase());
    }

    @Test
    public void testAsXmlDeclarationInvalidXmlDeclaration() {
        Comment comment = new Comment("<?xml version='1.0'?>\n<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Hello World</p>\n</body>\n</html>");
        Assert.assertNull(comment.asXmlDeclaration());
    }
}