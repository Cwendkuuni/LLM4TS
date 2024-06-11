import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testClean_method_should_return_cleaned_document_containing_only_elements_allowed_by_whitelist() {
        // Arrange
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Document.createShell("https://www.example.com");
        Element dirtyBody = dirtyDocument.body();
        Element dirtyElement = new Element(Tag.valueOf("a"), dirtyBody.baseUri(), new Attributes());
        Element dirtyChildElement = new Element(Tag.valueOf("div"), dirtyBody.baseUri(), new Attributes());
        dirtyElement.appendChild(dirtyChildElement);
        dirtyElement.text("This is some dirty text.");
        dirtyBody.appendChild(dirtyElement);

        // Act
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Assert
        Assert.assertNull(cleanDocument.selectFirst("html head"));
        Assert.assertNull(cleanDocument.selectFirst("a div"));
        Assert.assertNotNull(cleanDocument.selectFirst("body a"));
        Assert.assertEquals("This is some dirty text.", cleanDocument.selectFirst("body a").text());
    }

    @Test
    public void testIsValid_method_should_return_true_if_all_tags_and_attributes_in_input_are_allowed_by_whitelist() {
        // Arrange
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Document.createShell("https://www.example.com");
        Element dirtyBody = dirtyDocument.body();
        Element dirtyElement = new Element(Tag.valueOf("a"), dirtyBody.baseUri(), new Attributes());
        dirtyElement.attr("href", "https://www.example.com");
        dirtyElement.text("This is a link.");
        dirtyBody.appendChild(dirtyElement);

        // Act
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Assert
        Assert.assertTrue(isValid);
    }

    @Test
    public void testIsValid_method_should_return_false_if_any_tag_or_attribute_in_input_is_not_allowed_by_whitelist() {
        // Arrange
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Document.createShell("https://www.example.com");
        Element dirtyBody = dirtyDocument.body();
        Element dirtyElement = new Element(Tag.valueOf("script"), dirtyBody.baseUri(), new Attributes());
        dirtyElement.attr("src", "https://www.example.com/malicious.js");
        dirtyBody.appendChild(dirtyElement);

        // Act
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Assert
        Assert.assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes_method_should_correctly_copy_trusted_nodes_from_source_to_destination_element() {
        // Arrange
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Document.createShell("https://www.example.com");
        Element dirtyBody = dirtyDocument.body();
        Element dirtyElement = new Element(Tag.valueOf("a"), dirtyBody.baseUri(), new Attributes());
        Element dirtyChildElement = new Element(Tag.valueOf("div"), dirtyBody.baseUri(), new Attributes());
        dirtyElement.appendChild(dirtyChildElement);
        dirtyElement.text("This is some dirty text.");
        dirtyBody.appendChild(dirtyElement);

        Element cleanBody = new Element(Tag.valueOf("body"), "");
        Element cleanElement = new Element(Tag.valueOf("a"), "");
        Element cleanChildElement = new Element(Tag.valueOf("div"), "");

        // Act
        int numDiscarded = cleaner.copySafeNodes(dirtyBody, cleanBody);

        // Assert
        Assert.assertEquals(0, numDiscarded);
        Assert.assertNull(cleanBody.selectFirst("div"));
        Assert.assertNotNull(cleanBody.selectFirst("a"));
        Assert.assertEquals("This is some dirty text.", cleanBody.selectFirst("a").text());

        numDiscarded = cleaner.copySafeNodes(dirtyElement, cleanElement);
        Assert.assertEquals(0, numDiscarded);
        Assert.assertNull(cleanElement.selectFirst("div"));
        Assert.assertNotNull(cleanElement);
        Assert.assertEquals("This is some dirty text.", cleanElement.text());

        numDiscarded = cleaner.copySafeNodes(dirtyChildElement, cleanChildElement);
        Assert.assertEquals(0, numDiscarded);
        Assert.assertNull(cleanChildElement.selectFirst("div"));
        Assert.assertNull(cleanChildElement.selectFirst("a"));
    }

    @Test
    public void testCreateSafeElement_method_should_create_safe_element_with_safe_attributes_and_enforced_attributes() {
        // Arrange
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        Element dirtyElement = new Element(Tag.valueOf("a"), "");
        dirtyElement.attr("href", "https://www.example.com");
        dirtyElement.attr("class", "link");
        dirtyElement.attr("data-id", "123");

        // Act
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(dirtyElement);
        Element cleanElement = elementMeta.el;

        // Assert
        Assert.assertNotNull(cleanElement);
        Assert.assertEquals("a", cleanElement.tagName());
        Assert.assertEquals("https://www.example.com", cleanElement.attr("href"));
        Assert.assertTrue(cleanElement.hasAttr("class"));
        Assert.assertTrue(cleanElement.hasAttr("data-id"));
        Assert.assertFalse(cleanElement.hasAttr("onclick"));
    }

    @Test
    public void testCreateSafeElement_method_should_discard_unsafe_attributes() {
        // Arrange
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        Element dirtyElement = new Element(Tag.valueOf("a"), "");
        dirtyElement.attr("href", "javascript:alert('XSS')");
        dirtyElement.attr("onclick", "alert('XSS')");

        // Act
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(dirtyElement);
        Element cleanElement = elementMeta.el;

        // Assert
        Assert.assertNotNull(cleanElement);
        Assert.assertEquals("a", cleanElement.tagName());
        Assert.assertNull(cleanElement.attr("href"));
        Assert.assertFalse(cleanElement.hasAttr("onclick"));
    }

}