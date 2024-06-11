import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.Document;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.HttpConnection;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class JsoupTest {

    @Test
    public void testParseStringWithBaseUri() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://test.com";
        Document doc = Jsoup.parse(html, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testParseStringWithoutBaseUri() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
    }

    @Test
    public void testParseStringWithParser() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://test.com";
        Parser parser = Parser.htmlParser();
        Document doc = Jsoup.parse(html, baseUri, parser);
        assertNotNull(doc);
    }

    @Test
    public void testConnectUrl() {
        String url = "http://test.com";
        Connection connection = Jsoup.connect(url);
        Document doc = connection.get();
        assertNotNull(doc);
    }

    @Test
    public void testParseFile() {
        String filePath = "test.html";
        File file = new File(filePath);
        Document doc = null;
        try {
            doc = Jsoup.parse(file, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStream() {
        String filePath = "test.html";
        File file = new File(filePath);
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Document doc = null;
        try {
            doc = Jsoup.parse(inputStream, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(doc);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        assertNotNull(doc);
    }

    @Test
    public void testParseWithInvalidHtml() {
        String html = "<html><body><h1>Hello, World!<script>";
        assertThrows(IllegalArgumentException.class, () -> Jsoup.parse(html));
    }

    @Test
    public void testCleanWithEmptyWhitelist() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        Whitelist emptyWhitelist = Whitelist.none();
        String cleanedHtml = Jsoup.clean(bodyHtml, emptyWhitelist);
        assertNotNull(cleanedHtml);
        Document doc = Jsoup.parse(cleanedHtml);
        Element h1 = doc.selectFirst("h1");
        assertNotNull(h1);
    }

    @Test
    public void testCleanWithAllowedTagsAndAttributes() {
        String bodyHtml = "<div id='myDiv' class='myClass'><h1>Hello, World!<a href='http://test.com'>Test Link</a></h1></div>";
        Whitelist whitelist = Whitelist.simpleTagNames("div", "h1", "a");
        String cleanedHtml = Jsoup.clean(bodyHtml, whitelist);
        Document doc = Jsoup.parse(cleanedHtml);
        Element div = doc.selectFirst("div");
        Element h1 = doc.selectFirst("h1");
        Element a = doc.selectFirst("a");
        assertNotNull(div);
        assertNotNull(h1);
        assertNotNull(a);
    }

    @Test
    public void testCleanWithDisallowedTags() {
        String bodyHtml = "<script>alert('Hello, World!');</script>";
        Whitelist whitelist = Whitelist.simpleTagNames();
        String cleanedHtml = Jsoup.clean(bodyHtml, whitelist);
        assertNull(cleanedHtml);
    }
}