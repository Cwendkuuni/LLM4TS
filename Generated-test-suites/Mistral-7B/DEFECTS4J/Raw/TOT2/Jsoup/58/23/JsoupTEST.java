
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import static org.junit.Assert.*;

public class JsoupTest {

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";

        Document document = Jsoup.parse(html, baseUri);
        assertEquals("Test", document.title());
        assertEquals("Hello World", document.select("h1").text());
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";
        Connection connection = Jsoup.connect(url);
        Document document = connection.get();
        assertEquals("Example Domain", document.title());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());

        Document document = Jsoup.parse(inputStream, "UTF-8", "");
        assertEquals("Test", document.title());
        assertEquals("Hello World", document.select("h1").text());
    }
}