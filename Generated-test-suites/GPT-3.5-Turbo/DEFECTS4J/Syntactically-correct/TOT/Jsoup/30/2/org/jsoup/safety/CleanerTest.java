import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.W3CDom;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

    private Cleaner cleaner;

    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.relaxed());
    }

    @Test
    public void testClean() {
        try {
            Document dirtyDocument = Jsoup.parse("<html><head></head><body><p><a href='http://example.com'>Link</a></p><script>alert('XSS');</script></body></html>");
            Document cleanDocument = cleaner.clean(dirtyDocument);
            assertFalse(cleanDocument.toString().contains("<script>"));
            assertTrue(cleanDocument.toString().contains("<p><a href=\"http://example.com\">Link</a></p>"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIsValid_ValidDocument() {
        try {
            Document validDocument = Jsoup.parse("<html><head></head><body><p><a href='http://example.com'>Link</a></p></body></html>");
            boolean isValid = cleaner.isValid(validDocument);
            assertTrue(isValid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIsValid_InvalidDocument() {
        try {
            Document invalidDocument = Jsoup.parse("<html><head></head><body><p><a href='javascript:void(0)'>Link</a></p></body></html>");
            boolean isValid = cleaner.isValid(invalidDocument);
            assertFalse(isValid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCopySafeNodes_SingleElement() {
        Element root = new Element("div");
        Element destination = new Element("div");
        Element child = new Element("p");
        child.text("Hello, world!");
        root.appendChild(child);

        int numDiscarded = cleaner.copySafeNodes(root, destination);

        assertEquals(0, numDiscarded);
        assertEquals("<div>\n" +
                " <p>Hello, world!</p>\n" +
                "</div>", destination.outerHtml());
    }

    @Test
    public void testCopySafeNodes_NestedElements() {
        Element root = new Element("div");
        Element destination = new Element("div");
        Element child1 = new Element("p");
        child1.text("Hello, world!");
        Element child2 = new Element("span");
        child2.text("Now");
        Element child3 = new Element("em");
        child3.text("is");
        Element child4 = new Element("strong");
        child4.text("nested");
        child3.appendChild(child4);
        child2.appendChild(child3);
        child1.appendChild(child2);
        root.appendChild(child1);

        int numDiscarded = cleaner.copySafeNodes(root, destination);

        assertEquals(0, numDiscarded);
        assertEquals("<div>\n" +
                " <p>Hello, world!<span>Now<em>is<strong>nested</strong></em></span></p>\n" +
                "</div>", destination.outerHtml());
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element("a");
        sourceEl.attr("href", "http://example.com");
        sourceEl.attr("target", "_blank");
        sourceEl.attr("onclick", "alert('XSS')");
        sourceEl.attr("data-custom-attr", "123");

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

        assertEquals("<a href=\"http://example.com\">", elementMeta.el.outerHtml());
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }

}