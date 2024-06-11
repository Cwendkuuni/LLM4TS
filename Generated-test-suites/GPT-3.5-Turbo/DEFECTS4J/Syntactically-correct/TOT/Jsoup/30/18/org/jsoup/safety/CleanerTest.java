
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CleanerTest {

    private Cleaner cleaner;
    private Document dirtyDocument;

    @Before
    public void setUp() {
        Whitelist whitelist = Whitelist.basicWithImages();
        this.cleaner = new Cleaner(whitelist);
        this.dirtyDocument = new Document("");
    }

    @Test
    public void clean_validDirtyDocument_returnsCleanedDocument() {
        // Arrange
        dirtyDocument.body().appendChild(createElementWithContent("div", "Hello, World!"));

        // Act
        Document cleanedDocument = cleaner.clean(dirtyDocument);

        // Assert
        Assert.assertNotNull(cleanedDocument);
        Assert.assertEquals("<html><head></head><body><div>Hello, World!</div></body></html>", cleanedDocument.outerHtml());
    }

    @Test
    public void clean_dirtyDocumentWithoutBody_returnsEmptyCleanedDocument() {
        // Act
        Document cleanedDocument = cleaner.clean(dirtyDocument);

        // Assert
        Assert.assertNotNull(cleanedDocument);
        Assert.assertEquals("<html><head></head><body></body></html>", cleanedDocument.outerHtml());
    }

    @Test
    public void isValid_validDirtyDocument_returnsTrue() {
        // Arrange
        dirtyDocument.body().appendChild(createElementWithContent("p", "This is a paragraph."));

        // Act
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Assert
        Assert.assertTrue(isValid);
    }

    @Test
    public void isValid_dirtyDocumentWithUnsafeTag_returnsFalse() {
        // Arrange
        dirtyDocument.body().appendChild(createElementWithContent("script", "alert('XSS attack');"));

        // Act
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Assert
        Assert.assertFalse(isValid);
    }

    @Test
    public void copySafeNodes_dirtyDocumentWithSafeTags_copiesSafeNodesToDestination() {
        // Arrange
        Element root = dirtyDocument.body();
        Element div = createElementWithContent("div", "This is a div element.");
        Element span = createElementWithContent("span", "This is a span element.");
        root.appendChild(div);
        div.appendChild(span);

        // Act
        Document cleanDocument = Document.createShell("");
        int numDiscarded = cleaner.copySafeNodes(root, cleanDocument.body());

        // Assert
        Assert.assertEquals(0, numDiscarded);
        Assert.assertEquals("<html><head></head><body><div><span>This is a span element.</span></div></body></html>", cleanDocument.outerHtml());
    }

    @Test
    public void copySafeNodes_dirtyDocumentWithUnsafeTags_discardsUnsafeNodes() {
        // Arrange
        Element root = dirtyDocument.body();
        Element script = createElementWithContent("script", "alert('XSS attack');");
        Element iframe = createElementWithContent("iframe", "");
        root.appendChild(script);
        root.appendChild(iframe);

        // Act
        Document cleanDocument = Document.createShell("");
        int numDiscarded = cleaner.copySafeNodes(root, cleanDocument.body());

        // Assert
        Assert.assertEquals(2, numDiscarded);
        Assert.assertEquals("<html><head></head><body></body></html>", cleanDocument.outerHtml());
    }

    @Test
    public void createSafeElement_safeSourceElement_returnsSafeDestinationElement() {
        // Arrange
        Element sourceElement = createElementWithAttributes("a", "href", "https://example.com");

        // Act
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
        Element destinationElement = elementMeta.el;

        // Assert
        Assert.assertEquals("a", destinationElement.tagName());
        Assert.assertEquals("https://example.com", destinationElement.attr("href"));
    }

    @Test
    public void createSafeElement_unsafeSourceElementWithUnsafeAttributes_discardsUnsafeAttributes() {
        // Arrange
        Element sourceElement = createElementWithAttributes("div", "data-src", "https://example.com", "onclick", "alert('XSS attack')");

        // Act
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
        Element destinationElement = elementMeta.el;

        // Assert
        Assert.assertEquals("div", destinationElement.tagName());
        Assert.assertEquals(1, destinationElement.attributes().size());
        Assert.assertEquals("https://example.com", destinationElement.attr("data-src"));
    }

    @Test
    public void createSafeElement_unsafeSourceElementWithEnforcedAttributes_includesEnforcedAttributes() {
        // Arrange
        Element sourceElement = createElementWithAttributes("a", "href", "https://example.com");
        Whitelist whitelist = new Whitelist();
        whitelist.addEnforcedAttribute("a", "target", "_blank");
        Cleaner cleaner = new Cleaner(whitelist);

        // Act
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
        Element destinationElement = elementMeta.el;

        // Assert
        Assert.assertEquals("a", destinationElement.tagName());
        Assert.assertEquals(2, destinationElement.attributes().size());
        Assert.assertEquals("https://example.com", destinationElement.attr("href"));
        Assert.assertEquals("_blank", destinationElement.attr("target"));
    }

    private Element createElementWithContent(String tagName, String content) {
        Element element = new Element(Tag.valueOf(tagName), "");
        element.text(content);
        return element;
    }

    private Element createElementWithAttributes(String tagName, String... attributes) {
        Element element = new Element(Tag.valueOf(tagName), "");
        for (int i = 0; i < attributes.length; i += 2) {
            element.attr(attributes[i], attributes[i + 1]);
        }
        return element;
    }
}

