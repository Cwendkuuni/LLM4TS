import org.jsoup.safety.*;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import java.util.List;

public class CleanerTest {

    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic()); // Using basic whitelist for testing
    }

    @Test
    public void testClean() {
        // Create a dirty document to test
        Document dirtyDocument = Document.createShell("");
        String dirtyHtml = "<html><head></head><body><div><script>alert('XSS Attack');</script></div></body></html>";
        dirtyDocument.html(dirtyHtml);

        // Call the clean method
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Validate the clean document
        List<Node> cleanNodes = cleanDocument.body().childNodes();
        Element cleanElement = (Element) cleanNodes.get(0);
        Assert.assertEquals("div", cleanElement.tagName());
        Assert.assertTrue(cleanElement.childNodes().isEmpty());
    }

    @Test
    public void testIsValidWithValidDocument() {
        // Create a valid document to test
        Document validDocument = Document.createShell("");
        String validHtml = "<html><head></head><body><div></div></body></html>";
        validDocument.html(validHtml);

        // Call the isValid method
        boolean isValid = cleaner.isValid(validDocument);

        // Validate the result
        Assert.assertTrue(isValid);
    }

    @Test
    public void testIsValidWithInvalidDocument() {
        // Create an invalid document to test
        Document invalidDocument = Document.createShell("");
        String invalidHtml = "<html><head></head><body><div><script>alert('XSS Attack');</script></div></body></html>";
        invalidDocument.html(invalidHtml);

        // Call the isValid method
        boolean isValid = cleaner.isValid(invalidDocument);

        // Validate the result
        Assert.assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        // Create a source element to test
        Element sourceElement = new Element(Tag.valueOf("div"), "");
        sourceElement.append("<script>alert('XSS Attack');</script>");

        // Create a destination element to copy into
        Element destElement = new Element(Tag.valueOf("div"), "");

        // Call the copySafeNodes method
        int numDiscarded = cleaner.copySafeNodes(sourceElement, destElement);

        // Validate the result
        Assert.assertEquals(1, numDiscarded);
        Assert.assertTrue(destElement.childNodes().isEmpty());
    }

    @Test
    public void testCreateSafeElement() {
        // Create a source element to test
        Element sourceElement = new Element(Tag.valueOf("div"), "");
        sourceElement.attr("onclick", "alert('XSS Attack')");

        // Call the createSafeElement method
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        // Validate the created element
        Element destElement = elementMeta.el;
        Assert.assertEquals("div", destElement.tagName());
        Assert.assertEquals(1, destElement.attributes().size());
        Assert.assertEquals("onclick", destElement.attributes().get(0).getKey());
    }
}