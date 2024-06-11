import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.eq;

public class ParserTest {

    @Test
    public void testParse_WhenHtmlAndBaseUriProvided_ShouldReturnParsedDocument() {
        // Arrange
        String html = "<html><body><h1>Title</h1></body></html>";
        String baseUri = "https://www.example.com";
        Parser parser = new Parser(html, baseUri, false);

        // Act
        Document document = parser.parse();

        // Assert
        Assert.assertNotNull(document);
        Assert.assertEquals(html, document.html());
    }

    @Test
    public void testParse_WhenEmptyHtmlProvided_ShouldReturnEmptyDocument() {
        // Arrange
        String html = "";
        String baseUri = "https://www.example.com";
        Parser parser = new Parser(html, baseUri, false);

        // Act
        Document document = parser.parse();

        // Assert
        Assert.assertNotNull(document);
        Assert.assertEquals("", document.html());
    }

    @Test
    public void testParse_WhenHtmlWithCommentsProvided_ShouldIgnoreComments() {
        // Arrange
        String htmlWithComments = "<html><!-- This is a comment --><body>This is the body</body></html>";
        String expectedHtml = "<html><body>This is the body</body></html>";
        String baseUri = "https://www.example.com";
        Parser parser = new Parser(htmlWithComments, baseUri, false);

        // Act
        Document document = parser.parse();

        // Assert
        Assert.assertNotNull(document);
        Assert.assertEquals(expectedHtml, document.html());
    }

    @Test
    public void testParse_WhenHtmlWithCDATAProvided_ShouldParseCDATACorrectly() {
        // Arrange
        String htmlWithCDATA = "<html><body><![CDATA[This is CDATA]]></body></html>";
        String expectedHtml = "<html><body>This is CDATA</body></html>";
        String baseUri = "https://www.example.com";
        Parser parser = new Parser(htmlWithCDATA, baseUri, false);

        // Act
        Document document = parser.parse();

        // Assert
        Assert.assertNotNull(document);
        Assert.assertEquals(expectedHtml, document.html());
    }

    @Test
    public void testParse_WhenHtmlWithXmlDeclProvided_ShouldParseXmlDeclarationCorrectly() {
        // Arrange
        String htmlWithXmlDecl = "<html><?xml version=\"1.0\" encoding=\"UTF-8\"?><body>This is the body</body></html>";
        String expectedHtml = "<html><body>This is the body</body></html>";
        String baseUri = "https://www.example.com";
        Parser parser = new Parser(htmlWithXmlDecl, baseUri, false);

        // Act
        Document document = parser.parse();

        // Assert
        Assert.assertNotNull(document);
        Assert.assertEquals(expectedHtml, document.html());
    }
    
    // Add more test cases for other methods

}