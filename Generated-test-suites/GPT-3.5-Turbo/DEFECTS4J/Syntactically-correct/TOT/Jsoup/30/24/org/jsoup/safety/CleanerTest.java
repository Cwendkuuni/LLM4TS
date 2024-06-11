
import org.junit.Test;
import org.junit.Assert;

public class CleanerTest {

    @Test
    public void testClean() {
        // Typical use case: dirty document has body with safe tags and attributes
        Document dirtyDocument = Jsoup.parseBodyFragment("<div><p>Hello, World!</p></div>");
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document cleanDocument = cleaner.clean(dirtyDocument);

        Assert.assertTrue(cleanDocument.hasSameValue(dirtyDocument)); // Clean document should have the same value as dirty document
    }

    @Test
    public void testClean_WithUnsafeTagsAndAttributes() {
        // Edge case: dirty document has body with unsafe tags and attributes
        Document dirtyDocument = Jsoup.parseBodyFragment("<div onclick=\"alert('Hello')\"><script>alert('World')</script></div>");
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document cleanDocument = cleaner.clean(dirtyDocument);

        Assert.assertFalse(cleanDocument.hasSameValue(dirtyDocument)); // Clean document should not have the same value as dirty document
        Assert.assertTrue(cleanDocument.body().childNodes().isEmpty()); // Clean document body should be empty
    }

    @Test
    public void testIsValid_ValidDocument() {
        // Typical use case: valid document according to whitelist
        Document dirtyDocument = Jsoup.parseBodyFragment("<div><p>Hello, World!</p></div>");
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        boolean isValid = cleaner.isValid(dirtyDocument);

        Assert.assertTrue(isValid); // Document is valid
    }

    @Test
    public void testIsValid_InvalidDocument() {
        // Typical use case: invalid document according to whitelist
        Document dirtyDocument = Jsoup.parseBodyFragment("<div onclick=\"alert('Hello')\"><script>alert('World')</script></div>");
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        boolean isValid = cleaner.isValid(dirtyDocument);

        Assert.assertFalse(isValid); // Document is not valid
    }

    @Test
    public void testCopySafeNodes_ElementWithSafeTagsAndAttributes() {
        // Typical use case: element has safe tags and attributes
        Element root = new Element(Tag.valueOf("div"), "");
        Element child = new Element(Tag.valueOf("p"), "");
        child.attr("id", "hello");
        root.appendChild(child);

        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document cleanDocument = Document.createShell("");
        int numDiscarded = cleaner.copySafeNodes(root, cleanDocument.body());

        Assert.assertEquals(0, numDiscarded); // No discarded tags or attributes
        Assert.assertEquals(1, cleanDocument.body().children().size()); // Clean document should have one child
        Assert.assertEquals("div", cleanDocument.body().child(0).tagName()); // Child element tag should be "div"
    }

    @Test
    public void testCopySafeNodes_ElementWithUnsafeTagsAndAttributes() {
        // Typical use case: element has unsafe tags and attributes
        Element root = new Element(Tag.valueOf("div"), "");
        Element child = new Element(Tag.valueOf("p"), "");
        child.attr("onclick", "alert('Hello')");
        root.appendChild(child);

        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document cleanDocument = Document.createShell("");
        int numDiscarded = cleaner.copySafeNodes(root, cleanDocument.body());

        Assert.assertEquals(1, numDiscarded); // One discarded tag or attribute
        Assert.assertEquals(0, cleanDocument.body().children().size()); // Clean document should not have any child
    }
    
    @Test
    public void testCreateSafeElement_ElementWithSafeAttributes() {
        // Typical use case: element has safe attributes
        Element sourceElement = new Element(Tag.valueOf("p"), "");
        sourceElement.attr("id", "hello");
        sourceElement.attr("class", "world");

        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        Assert.assertEquals(0, elementMeta.numAttribsDiscarded); // No discarded attributes
        Assert.assertEquals("p", elementMeta.el.tagName()); // Safe element has the same tag as source element
        Assert.assertEquals(2, elementMeta.el.attributes().size()); // Safe element has all the attributes from source element
    }
    
    @Test
    public void testCreateSafeElement_ElementWithUnsafeAttributes() {
        // Typical use case: element has unsafe attributes
        Element sourceElement = new Element(Tag.valueOf("p"), "");
        sourceElement.attr("onclick", "alert('Hello')");
        sourceElement.attr("onmouseover", "alert('World')");

        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        Assert.assertEquals(2, elementMeta.numAttribsDiscarded); // Two discarded attributes
        Assert.assertEquals("p", elementMeta.el.tagName()); // Safe element has the same tag as source element
        Assert.assertEquals(0, elementMeta.el.attributes().size()); // Safe element should not have any attribute
    }

}

