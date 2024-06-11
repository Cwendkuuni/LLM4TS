import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CleanerTest {
    private Cleaner cleaner;
    private Document dirtyDocument;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
        dirtyDocument = Jsoup.parse("<html><head></head><body><h1>Hello World!</h1></body></html>");
    }

    @After
    public void tearDown() {
        cleaner = null;
        dirtyDocument = null;
    }

    @Test
    public void testClean() {
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element body = cleanDocument.body();
        Assert.assertNotNull(body);
        Assert.assertEquals(1, body.children().size());
        Assert.assertEquals("h1", body.child(0).tagName());
        Assert.assertEquals("Hello World!", body.child(0).text());
    }

    @Test
    public void testIsValid_ValidDocument() {
        boolean isValid = cleaner.isValid(dirtyDocument);
        Assert.assertTrue(isValid);
    }

    @Test
    public void testIsValid_InvalidDocument() {
        dirtyDocument = Jsoup.parse("<html><head></head><body><script>alert('XSS');</script></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        Assert.assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes_WithSafeTag() {
        Element sourceElement = new Element("p");
        Element destination = new Element("div");
        List<Attribute> attributes = Arrays.asList(new Attribute("class", "paragraph"), new Attribute("title", "test"));
        sourceElement.attr(attributes);

        int numDiscarded = cleaner.copySafeNodes(sourceElement, destination);
        Assert.assertEquals(0, numDiscarded);
        Assert.assertEquals(1, destination.children().size());
        Assert.assertEquals("p", destination.child(0).tagName());
        Assert.assertEquals("paragraph", destination.child(0).attr("class"));
        Assert.assertEquals("test", destination.child(0).attr("title"));
    }

    @Test
    public void testCopySafeNodes_WithUnsafeTag() {
        Element sourceElement = new Element("script");
        Element destination = new Element("div");

        int numDiscarded = cleaner.copySafeNodes(sourceElement, destination);
        Assert.assertEquals(1, numDiscarded);
        Assert.assertEquals(0, destination.children().size());
    }

    @Test
    public void testCreateSafeElement_WithSafeAttribute() {
        Element sourceElement = new Element("p");
        sourceElement.attr("class", "paragraph");
        sourceElement.attr("title", "test");

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
        Assert.assertEquals("p", elementMeta.el.tagName());
        Assert.assertEquals("paragraph", elementMeta.el.attr("class"));
        Assert.assertEquals("test", elementMeta.el.attr("title"));
        Assert.assertEquals(0, elementMeta.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElement_WithUnsafeAttribute() {
        Element sourceElement = new Element("p");
        sourceElement.attr("class", "paragraph");
        sourceElement.attr("onclick", "alert('XSS')");

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
        Assert.assertEquals("p", elementMeta.el.tagName());
        Assert.assertEquals("paragraph", elementMeta.el.attr("class"));
        Assert.assertEquals(1, elementMeta.numAttribsDiscarded);
    }

}