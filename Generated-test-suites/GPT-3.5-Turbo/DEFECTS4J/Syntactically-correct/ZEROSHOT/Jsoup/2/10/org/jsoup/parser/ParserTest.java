import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ParserTest {
    private static final String BASE_URI = "https://example.com";

    @Before
    public void setUp() {
        // Add any necessary setup code here
    }

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        Document document = Parser.parse(html, BASE_URI);

        assertThat(document.title(), is("Test"));
        assertThat(document.body().getElementsByTag("h1").first().text(), is("Hello, World!"));
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        Document document = Parser.parseBodyFragment(bodyHtml, BASE_URI);
        
        assertThat(document.body().getElementsByTag("h1").first().text(), is("Hello, World!"));
    }
}