import org.junit.Test;
import static org.junit.Assert.*;

public class JsoupTest {

    @Test
    public void parse_ValidHtml_ShouldReturnDocument() {
        // Arrange
        String html = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://www.example.com";
        
        // Act
        Document result = Jsoup.parse(html, baseUri);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.getElementsByTag("h1").size());
        assertEquals("Hello World!", result.getElementsByTag("h1").first().text());
    }
    
    @Test
    public void parse_InvalidHtml_ShouldReturnEmptyDocument() {
        // Arrange
        String html = "<html><head><title>Hello World!</title></head><body><h1>Hello World!</h1>";
        String baseUri = "https://www.example.com";
        
        // Act
        Document result = Jsoup.parse(html, baseUri);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.getElementsByTag("h1").isEmpty());
    }
    
    @Test
    public void parse_WithAlternateParser_ShouldReturnDocument() {
        // Arrange
        String html = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.xmlParser();
        
        // Act
        Document result = Jsoup.parse(html, baseUri, parser);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.getElementsByTag("h1").size());
        assertEquals("Hello World!", result.getElementsByTag("h1").first().text());
    }

    @Test
    public void connect_ValidUrl_ShouldReturnConnection() {
        // Arrange
        String url = "https://www.example.com";
        
        // Act
        Connection result = Jsoup.connect(url);
        
        // Assert
        assertNotNull(result);
    }
    
    @Test(expected = IOException.class)
    public void connect_InvalidUrl_ShouldThrowIOException() throws IOException {
        // Arrange
        String url = "https://www.invalidurl.com";
        
        // Act
        Connection result = Jsoup.connect(url);
        
        // Assert
        // IOException is expected to be thrown
    }
    
    @Test
    public void parseFile_ValidFile_ShouldReturnDocument() throws IOException {
        // Arrange
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        
        // Act
        Document result = Jsoup.parse(file, charsetName, baseUri);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.getElementsByTag("h1").size());
        assertEquals("Hello World!", result.getElementsByTag("h1").first().text());
    }
    
    @Test(expected = IOException.class)
    public void parseFile_InvalidFile_ShouldThrowIOException() throws IOException {
        // Arrange
        File file = new File("invalid.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        
        // Act
        Document result = Jsoup.parse(file, charsetName, baseUri);
        
        // Assert
        // IOException is expected to be thrown
    }
    
    @Test
    public void parseInputStream_ValidInputStream_ShouldReturnDocument() throws IOException {
        // Arrange
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        
        // Act
        Document result = Jsoup.parse(inputStream, charsetName, baseUri);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.getElementsByTag("h1").size());
        assertEquals("Hello World!", result.getElementsByTag("h1").first().text());
    }
    
    @Test(expected = IOException.class)
    public void parseInputStream_InvalidInputStream_ShouldThrowIOException() throws IOException {
        // Arrange
        InputStream inputStream = getClass().getResourceAsStream("invalid.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        
        // Act
        Document result = Jsoup.parse(inputStream, charsetName, baseUri);
        
        // Assert
        // IOException is expected to be thrown
    }
    
    @Test
    public void parseBodyFragment_ValidFragment_ShouldReturnDocument() {
        // Arrange
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "https://www.example.com";
        
        // Act
        Document result = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.getElementsByTag("h1").size());
        assertEquals("Hello World!", result.getElementsByTag("h1").first().text());
    }
    
    @Test
    public void parseBodyFragment_InvalidFragment_ShouldReturnEmptyDocument() {
        // Arrange
        String bodyHtml = "<h1>Hello World!</h1";
        String baseUri = "https://www.example.com";
        
        // Act
        Document result = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.getElementsByTag("h1").isEmpty());
    }
    
    @Test
    public void clean_ValidHtml_ShouldReturnCleanedHtml() {
        // Arrange
        String bodyHtml = "<h1>Hello <strong>World!</strong></h1>";
        String baseUri = "https://www.example.com";
        Whitelist whitelist = Whitelist.basic();
        
        // Act
        String result = Jsoup.clean(bodyHtml, baseUri, whitelist);
        
        // Assert
        assertNotNull(result);
        assertFalse(result.contains("<strong>"));
        assertTrue(result.contains("<h1>Hello World!</h1>"));
    }
    
    @Test
    public void clean_InvalidHtml_ShouldReturnEmptyString() {
        // Arrange
        String bodyHtml = "<h1>Hello <strong>World!</h1>";
        String baseUri = "https://www.example.com";
        Whitelist whitelist = Whitelist.basic();
        
        // Act
        String result = Jsoup.clean(bodyHtml, baseUri, whitelist);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void isValid_ValidHtml_ShouldReturnTrue() {
        // Arrange
        String bodyHtml = "<h1>Hello World!</h1>";
        Whitelist whitelist = Whitelist.basic();
        
        // Act
        boolean result = Jsoup.isValid(bodyHtml, whitelist);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void isValid_InvalidHtml_ShouldReturnFalse() {
        // Arrange
        String bodyHtml = "<h1>Hello <strong>World!</h1>";
        Whitelist whitelist = Whitelist.basic();
        
        // Act
        boolean result = Jsoup.isValid(bodyHtml, whitelist);
        
        // Assert
        assertFalse(result);
    }
}