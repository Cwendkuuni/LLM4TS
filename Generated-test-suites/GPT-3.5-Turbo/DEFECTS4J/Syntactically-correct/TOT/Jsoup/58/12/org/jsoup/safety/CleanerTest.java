import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testClean() {
        // Typical use case: clean a document with allowed tags and attributes
        String html = "<html><body><h1>Title</h1><p>Paragraph</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Elements cleanElements = cleanDocument.body().children();
        Assert.assertEquals(2, cleanElements.size());
    }

    @Test
    public void testClean_EmptyDocument() {
        // Edge case: clean an empty document
        Document dirtyDocument = new Document("");
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Elements cleanElements = cleanDocument.body().children();
        Assert.assertEquals(0, cleanElements.size());
    }

    @Test
    public void testClean_ValidDocument() {
        // Typical use case: clean a document with allowed tags and attributes and validate it
        String html = "<html><body><h1>Title</h1><p>Paragraph</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        boolean isValid = cleaner.isValid(dirtyDocument);
        Assert.assertTrue(isValid);
    }

    @Test
    public void testClean_InvalidDocument() {
        // Edge case: clean a document with disallowed tag and validate it
        String html = "<html><head><title>Invalid</title></head><body><h1>Title</h1><p>Paragraph</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        boolean isValid = cleaner.isValid(dirtyDocument);
        Assert.assertFalse(isValid);
    }

    @Test
    public void testClean_InvalidAttribute() {
        // Error scenario: clean a document with disallowed attribute and validate it
        String html = "<html><body><img src=\"javascript:alert('XSS')\"></img></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Cleaner cleaner = new Cleaner(Whitelist.basic()); // Only allows <b>, <i>, <em>, <strong>, <a> tags
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Elements cleanElements = cleanDocument.body().children();
        Assert.assertEquals(0, cleanElements.size());
    }

    @Test
    public void testClean_InvalidTextNode() {
        // Error scenario: clean a document with disallowed text node and validate it
        String html = "<html><body><h1>Title</h1><p>Paragraph<script>alert('XSS')</script></p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Elements cleanElements = cleanDocument.body().children();
        Assert.assertEquals(2, cleanElements.size());
        Element paragraph = cleanElements.get(1);
        Assert.assertEquals(1, paragraph.childrenSize());
        Assert.assertTrue(paragraph.childNode(0) instanceof TextNode);
    }

    @Test
    public void testClean_InvalidCommentNode() {
        // Error scenario: clean a document with disallowed comment node and validate it
        String html = "<html><!--Invalid comment--><body><h1>Title</h1><p>Paragraph</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Elements cleanElements = cleanDocument.body().children();
        Assert.assertEquals(2, cleanElements.size());
    }

}