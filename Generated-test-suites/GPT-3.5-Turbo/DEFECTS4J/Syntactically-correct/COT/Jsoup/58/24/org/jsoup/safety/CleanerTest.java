
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class CleanerTest {
    private Cleaner cleaner;
    private Whitelist whitelist;

    @Before
    public void setup() {
        whitelist = Whitelist.relaxed();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean() {
        // Create a dirty document
        String dirtyHtml = "<html><body><div><p>Dirty text</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        // Clean the dirty document
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Verify that the clean document only contains allowed elements
        List<Element> allowedElements = whitelist.getTags();
        for (Element element : cleanDocument.getAllElements()) {
            Assert.assertTrue(allowedElements.contains(element.tagName()));
        }
    }

    @Test
    public void testIsValid() {
        // Create a dirty document with invalid content in the head
        String dirtyHtml = "<html><head><script>alert('XSS');</script></head><body><div><p>Clean text</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        // Check if the document is valid
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Verify that the document is not valid due to the content in the head
        Assert.assertFalse(isValid);
    }
}
