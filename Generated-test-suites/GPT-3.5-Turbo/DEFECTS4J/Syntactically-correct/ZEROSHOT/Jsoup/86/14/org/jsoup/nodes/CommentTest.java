
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testCommentCreationWithData() {
        // Arrange
        String data = "Sample comment";

        // Act
        Comment comment = new Comment(data);

        // Assert
        assertEquals(data, comment.getData());
    }

    @Test
    public void testCommentCreationWithDataAndBaseUri() {
        // Arrange
        String data = "Sample comment";
        String baseUri = "http://example.com";

        // Act
        Comment comment = new Comment(data, baseUri);

        // Assert
        assertEquals(data, comment.getData());
    }

    @Test
    public void testNodeName() {
        // Arrange
        Comment comment = new Comment("");

        // Act
        String nodeName = comment.nodeName();

        // Assert
        assertEquals("#comment", nodeName);
    }

    @Test
    public void testGetData() {
        // Arrange
        String data = "Sample comment";
        Comment comment = new Comment(data);

        // Act
        String retrievedData = comment.getData();

        // Assert
        assertEquals(data, retrievedData);
    }

    @Test
    public void testOuterHtmlHead() {
        // Arrange
        Comment comment = new Comment("Sample comment");

        // Act
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        out.prettyPrint(true);
        try {
            comment.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String result = accum.toString();

        // Assert
        assertTrue(result.contains("<!--"));
        assertTrue(result.contains("Sample comment"));
    }

    @Test
    public void testOuterHtmlTail() {
        // Arrange
        Comment comment = new Comment("Sample comment");

        // Act
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            comment.outerHtmlTail(accum, 0, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String result = accum.toString();

        // Assert
        assertEquals("", result);
    }

    @Test
    public void testToString() {
        // Arrange
        Comment comment = new Comment("Sample comment");

        // Act
        String result = comment.toString();

        // Assert
        assertTrue(result.contains("<!--"));
        assertTrue(result.contains("Sample comment"));
    }

    @Test
    public void testIsXmlDeclaration() {
        // Arrange
        Comment comment = new Comment("<!-- <?xml version=\"1.0\" encoding=\"UTF-8\" ?> -->");

        // Act
        boolean isXmlDeclaration = comment.isXmlDeclaration();

        // Assert
        assertTrue(isXmlDeclaration);
    }

    @Test
    public void testAsXmlDeclaration() {
        // Arrange
        Comment comment = new Comment("<!-- <?xml version=\"1.0\" encoding=\"UTF-8\" ?> -->");

        // Act
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();

        // Assert
        assertNotNull(xmlDeclaration);
    }

    @Test
    public void testAsXmlDeclarationWithNonDeclarationComment() {
        // Arrange
        Comment comment = new Comment("<!-- This is just a regular comment -->");

        // Act
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();

        // Assert
        assertNull(xmlDeclaration);
    }
}
