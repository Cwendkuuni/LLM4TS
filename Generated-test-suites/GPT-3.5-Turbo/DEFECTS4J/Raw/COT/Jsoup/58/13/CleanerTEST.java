import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

public class CleanerTest {

    private Cleaner cleaner = new Cleaner(Whitelist.relaxed());

    @Test
    public void testClean() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document dirtyDocument = Jsoup.parse(html, "", Parser.htmlParser());

        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Ensure the clean document only contains the allowed tags
        assert cleanDocument.select("title").size() == 0;
        assert cleanDocument.select("head").size() == 0;
        assert cleanDocument.select("body").size() == 1;
        assert cleanDocument.select("h1").size() == 1;
    }

    @Test
    public void testIsValid() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document dirtyDocument = Jsoup.parse(html, "", Parser.htmlParser());

        boolean isValid = cleaner.isValid(dirtyDocument);

        // Ensure the dirty document is valid and does not require any elements or attributes to be removed
        assert isValid;
    }

}