import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class CleanerTest {

    @Test
    public void testClean() {
        // Typical Use Cases
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document dirtyDocument = Jsoup.parseBodyFragment("<p>Test</p>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element body = cleanDocument.body();
        Assert.assertEquals("p", body.child(0).tagName());
        Assert.assertEquals("Test", body.child(0).text());

        // Edge Cases
        dirtyDocument = Jsoup.parseBodyFragment("<p>Test<script>alert('XSS')</script></p>");
        cleanDocument = cleaner.clean(dirtyDocument);
        body = cleanDocument.body();
        Assert.assertEquals("p", body.child(0).tagName());
        Assert.assertEquals("Test", body.child(0).text());

        // Error Scenarios
        cleaner = new Cleaner(Whitelist.none());
        dirtyDocument = Jsoup.parseBodyFragment("<p>Test</p>");
        cleanDocument = cleaner.clean(dirtyDocument);
        body = cleanDocument.body();
        Assert.assertNull("Expected null", body);
    }

    @Test
    public void testIsValid() {
        // Typical Use Cases
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document dirtyDocument = Jsoup.parseBodyFragment("<p>Test</p>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        Assert.assertTrue("Expected true", isValid);

        // Edge Cases
        dirtyDocument = Jsoup.parseBodyFragment("<p>Test<script>alert('XSS')</script></p>");
        isValid = cleaner.isValid(dirtyDocument);
        Assert.assertFalse("Expected false", isValid);

        // Error Scenarios
        cleaner = new Cleaner(Whitelist.none());
        dirtyDocument = Jsoup.parseBodyFragment("<p>Test</p>");
        isValid = cleaner.isValid(dirtyDocument);
        Assert.assertFalse("Expected false", isValid);
    }

    @Test
    public void testCopySafeNodes() {
        // Typical Use Cases
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document dirtyDocument = Jsoup.parseBodyFragment("<p>Test</p>");
        Document cleanDocument = Document.createShell("");
        Element dirtyBody = dirtyDocument.body();
        Element cleanBody = cleanDocument.body();

        int numDiscarded = cleaner.copySafeNodes(dirtyBody, cleanBody);
        Assert.assertEquals("Expected 0", 0, numDiscarded);
        Assert.assertEquals("p", cleanBody.child(0).tagName());
        Assert.assertEquals("Test", cleanBody.child(0).text());

        // Edge Cases
        dirtyDocument = Jsoup.parseBodyFragment("<p>Test<script>alert('XSS')</script></p>");
        dirtyBody = dirtyDocument.body();
        cleanBody = cleanDocument.body();

        numDiscarded = cleaner.copySafeNodes(dirtyBody, cleanBody);
        Assert.assertEquals("Expected 0", 0, numDiscarded);
        Assert.assertEquals("p", cleanBody.child(0).tagName());
        Assert.assertEquals("Test", cleanBody.child(0).text());

        // Error Scenarios
        cleaner = new Cleaner(Whitelist.none());
        dirtyDocument = Jsoup.parseBodyFragment("<p>Test</p>");
        dirtyBody = dirtyDocument.body();
        cleanBody = cleanDocument.body();

        numDiscarded = cleaner.copySafeNodes(dirtyBody, cleanBody);
        Assert.assertEquals("Expected 1", 1, numDiscarded);
        Assert.assertNull("Expected null", cleanBody.child(0));
    }

    @Test
    public void testCreateSafeElement() {
        // Typical Use Cases
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document dirtyDocument = Jsoup.parseBodyFragment("<p>Test</p>");
        Element dirtyBody = dirtyDocument.body();
        Element dirtyElement = dirtyBody.child(0);

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(dirtyElement);
        Element cleanElement = elementMeta.el;
        Assert.assertEquals("p", cleanElement.tagName());
        Assert.assertEquals(dirtyElement.baseUri(), cleanElement.baseUri());
        Assert.assertEquals("Test", cleanElement.ownText());

        // Edge Cases
        dirtyDocument = Jsoup.parseBodyFragment("<p class='class1 class2'>Test</p>");
        dirtyBody = dirtyDocument.body();
        dirtyElement = dirtyBody.child(0);

        elementMeta = cleaner.createSafeElement(dirtyElement);
        cleanElement = elementMeta.el;
        Assert.assertEquals("p", cleanElement.tagName());
        Assert.assertEquals(dirtyElement.baseUri(), cleanElement.baseUri());
        Assert.assertEquals("Test", cleanElement.ownText());
        List<Attribute> attributes = cleanElement.attributes().asList();
        Assert.assertEquals("class1", attributes.get(0).getValue());
        Assert.assertEquals("class2", attributes.get(1).getValue());

        // Error Scenarios
        cleaner = new Cleaner(Whitelist.none());
        dirtyDocument = Jsoup.parseBodyFragment("<p>Test</p>");
        dirtyBody = dirtyDocument.body();
        dirtyElement = dirtyBody.child(0);

        elementMeta = cleaner.createSafeElement(dirtyElement);
        cleanElement = elementMeta.el;
        Assert.assertNull("Expected null", cleanElement);
        Assert.assertEquals("Expected 1", 1, elementMeta.numAttribsDiscarded);
    }
}