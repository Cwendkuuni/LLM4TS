import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class CleanerTest {

    @Test
    public void testClean() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        // Create dirty document
        Document dirtyDocument = Jsoup.parseBodyFragment("<div><p>Dirty content</p></div>");

        // Clean the document
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Verify that only allowed elements are present in the clean document
        List<Element> cleanElements = cleanDocument.body().children();
        Assert.assertEquals(1, cleanElements.size());
        Assert.assertEquals("div", cleanElements.get(0).tagName());
        Assert.assertEquals(1, cleanElements.get(0).children().size());
        Assert.assertEquals("p", cleanElements.get(0).child(0).tagName());
        Assert.assertEquals("Dirty content", cleanElements.get(0).child(0).text());
    }

    @Test
    public void testIsValid_ValidDocument() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        // Create valid document
        Document validDocument = Jsoup.parseBodyFragment("<p>Valid content</p>");

        // Check if the document is valid
        boolean isValid = cleaner.isValid(validDocument);

        // The document should be valid
        Assert.assertTrue(isValid);
    }

    @Test
    public void testIsValid_InvalidDocument() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        // Create invalid document
        Document invalidDocument = Jsoup.parseBodyFragment("<script>Invalid content</script>");

        // Check if the document is valid
        boolean isValid = cleaner.isValid(invalidDocument);

        // The document should be invalid
        Assert.assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        // Create source element
        Element source = new Element(Tag.valueOf("div"), "");

        // Add child elements to source
        Element child1 = new Element(Tag.valueOf("p"), "");
        child1.text("Child 1");
        source.appendChild(child1);

        Element child2 = new Element(Tag.valueOf("span"), "");
        child2.text("Child 2");
        source.appendChild(child2);

        // Create destination element
        Element dest = new Element(Tag.valueOf("div"), "");

        // Copy safe nodes from source to destination
        int numDiscarded = cleaner.copySafeNodes(source, dest);

        // Verify that the safe nodes are copied successfully
        List<Element> destElements = dest.children();
        Assert.assertEquals(2, destElements.size());
        Assert.assertEquals("p", destElements.get(0).tagName());
        Assert.assertEquals("Child 1", destElements.get(0).text());
        Assert.assertEquals("span", destElements.get(1).tagName());
        Assert.assertEquals("Child 2", destElements.get(1).text());
        Assert.assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        // Create source element
        Element source = new Element(Tag.valueOf("div"), "");

        // Add attributes to source element
        source.attr("class", "test");
        source.attr("data-id", "123");

        // Create safe element from source
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(source);
        Element dest = elementMeta.el;

        // Verify that safe element is created successfully
        Assert.assertEquals("div", dest.tagName());
        Assert.assertEquals(2, dest.attributes().size());
        Assert.assertEquals("test", dest.attr("class"));
        Assert.assertEquals("123", dest.attr("data-id"));
        Assert.assertEquals(0, elementMeta.numAttribsDiscarded);
    }

}