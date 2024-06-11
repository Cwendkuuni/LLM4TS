
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CleanerTest {

    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        Document dirtyDocument = new Document("");
        Element dirtyElement = new Element(Tag.valueOf("div"), "");

        dirtyElement.appendElement("a").attr("href", "https://example.com");
        dirtyElement.appendElement("script").text("alert('XSS')");
        dirtyElement.appendText("This is a dirty document");

        dirtyDocument.body().appendChild(dirtyElement);

        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Check that only allowed elements are present in the clean document
        List<String> allowedTags = Arrays.asList("body", "div", "a", "script");
        for (Element element : cleanDocument.getAllElements()) {
            String tag = element.tagName();
            assert(allowedTags.contains(tag));
        }

        // Check that unsafe attributes have been removed
        Element cleanDiv = cleanDocument.body().child(0);
        assert(cleanDiv.children().size() == 1); // Only the <a> element should be present
        assert(cleanDiv.text().equals("This is a dirty document")); // Text nodes should not be modified

        Element cleanA = cleanDiv.child(0);
        assert(cleanA.tagName().equals("a"));
        assert(cleanA.attr("href").equals("https://example.com")); // Only the safe attribute should be preserved

        Element cleanScript = cleanDocument.body().selectFirst("script");
        assert(cleanScript == null); // The <script> element should be removed
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = new Document("");
        Element dirtyElement = new Element(Tag.valueOf("div"), "");

        dirtyElement.appendElement("a").attr("href", "https://example.com");
        dirtyElement.appendElement("script").text("alert('XSS')");

        dirtyDocument.body().appendChild(dirtyElement);

        assert(cleaner.isValid(dirtyDocument)); // The dirty document should be considered valid
    }
}
