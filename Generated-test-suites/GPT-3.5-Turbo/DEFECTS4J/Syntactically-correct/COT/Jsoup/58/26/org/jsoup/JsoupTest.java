
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.*;

public class JsoupTest {

    @Test
    public void parseWithBaseUri_shouldParseHtmlAndReturnDocument() {
        // Arrange
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";

        // Act
        Document document = Jsoup.parse(html, baseUri);

        // Assert
        assertNotNull(document);
        assertEquals("Hello World!", document.select("h1").text());
    }

    @Test
    public void parseWithBaseUriAndParser_shouldParseHtmlAndReturnDocument() {
        // Arrange
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // Act
        Document document = Jsoup.parse(html, baseUri, parser);

        // Assert
        assertNotNull(document);
        assertEquals("Hello World!", document.select("h1").text());
    }

    @Test
    public void parse_shouldParseHtmlAndReturnDocument() {
        // Arrange
        String html = "<html><body><h1>Hello World!</h1></body></html>";

        // Act
        Document document = Jsoup.parse(html);

        // Assert
        assertNotNull(document);
        assertEquals("Hello World!", document.select("h1").text());
    }

    @Test
    public void connect_shouldReturnConnectionObject() {
        // Arrange
        String url = "http://example.com";

        // Act
        Connection connection = Jsoup.connect(url);

        // Assert
        assertNotNull(connection);
    }

    @Test
    public void parseFile_shouldParseHtmlFileAndReturnDocument() throws IOException {
        // Arrange
        File file = new File("path/to/file.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act
        Document document = Jsoup.parse(file, charsetName, baseUri);

        // Assert
        assertNotNull(document);
    }

    @Test
    public void parseFile_shouldParseHtmlFileAndReturnDocumentWithDefaultBaseUri() throws IOException {
        // Arrange
        File file = new File("path/to/file.html");
        String charsetName = "UTF-8";

        // Act
        Document document = Jsoup.parse(file, charsetName);

        // Assert
        assertNotNull(document);
    }

    @Test
    public void parseInputStream_shouldParseHtmlInputStreamAndReturnDocument() throws IOException {
        // Arrange
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Act
        Document document = Jsoup.parse(inputStream, charsetName, baseUri);

        // Assert
        assertNotNull(document);
    }

    @Test
    public void parseInputStream_shouldParseHtmlInputStreamAndReturnDocumentWithDefaultBaseUri() throws IOException {
        // Arrange
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        String charsetName = "UTF-8";

        // Act
        Document document = Jsoup.parse(inputStream, charsetName);

        // Assert
        assertNotNull(document);
    }

    @Test
    public void parseBodyFragment_shouldParseHtmlBodyFragmentAndReturnDocument() {
        // Arrange
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";

        // Act
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);

        // Assert
        assertNotNull(document);
        assertEquals("Hello World!", document.select("h1").text());
    }

    @Test
    public void parseBodyFragment_shouldParseHtmlBodyFragmentAndReturnDocumentWithDefaultBaseUri() {
        // Arrange
        String bodyHtml = "<h1>Hello World!</h1>";

        // Act
        Document document = Jsoup.parseBodyFragment(bodyHtml);

        // Assert
        assertNotNull(document);
        assertEquals("Hello World!", document.select("h1").text());
    }

    @Test
    public void parseUrl_shouldParseUrlAndReturnDocument() throws IOException {
        // Arrange
        String url = "http://example.com";
        int timeoutMillis = 5000;

        // Act
        Document document = Jsoup.parse(new URL(url), timeoutMillis);

        // Assert
        assertNotNull(document);
    }

    @Test
    public void clean_shouldCleanHtmlAndReturnSafeHtml() {
        // Arrange
        String bodyHtml = "<script>alert('Hello world!');</script><p>Hello world!</p>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();

        // Act
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);

        // Assert
        assertEquals("<p>Hello world!</p>", cleanHtml);
    }

    @Test
    public void clean_shouldCleanHtmlAndReturnSafeHtmlWithDefaultBaseUri() {
        // Arrange
        String bodyHtml = "<script>alert('Hello world!');</script><p>Hello world!</p>";
        Whitelist whitelist = Whitelist.basic();

        // Act
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);

        // Assert
        assertEquals("<p>Hello world!</p>", cleanHtml);
    }

    @Test
    public void clean_shouldCleanHtmlAndReturnSafeHtmlWithOutputSettings() {
        // Arrange
        String bodyHtml = "<p>Hello world!</p>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);

        // Act
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);

        // Assert
        assertEquals("<p>Hello world!</p>", cleanHtml);
    }

    @Test
    public void isValid_shouldReturnTrueForValidHtml() {
        // Arrange
        String bodyHtml = "<p>Hello world!</p>";
        Whitelist whitelist = Whitelist.basic();

        // Act
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);

        // Assert
        assertTrue(isValid);
    }
}
