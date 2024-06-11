import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CleanerTest {

    @Test
    public void testClean_WithValidDocument_ShouldReturnCleanedDocument() {
        // Arrange
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<html><body><div><p>This is a <b>clean</b> document.</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        
        // Act
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        // Assert
        Assert.assertNotNull(cleanDocument);
        Assert.assertEquals("<html><head></head><body><div><p>This is a <b>clean</b> document.</p></div></body></html>", cleanDocument.html());
    }

    @Test
    public void testClean_WithMissingBodyElement_ShouldReturnEmptyCleanedDocument() {
        // Arrange
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<html><head><title>Test</title></head><div><p>This is some content.</p></div></html>";
        Document dirtyDocument = Jsoup.parse(html);

        // Act
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Assert
        Assert.assertNotNull(cleanDocument);
        Assert.assertEquals("<html><head></head><body></body></html>", cleanDocument.html());
    }

    @Test
    public void testIsValid_WithValidDocument_ShouldReturnTrue() {
        // Arrange
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<html><body><div><p>This is a <b>clean</b> document.</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        // Act
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Assert
        Assert.assertTrue(isValid);
    }

    @Test
    public void testIsValid_WithInvalidDocument_ShouldReturnFalse() {
        // Arrange
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<html><body><script>alert('XSS')</script><div><p>This is a <b>clean</b> document.</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        // Act
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Assert
        Assert.assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes_WithElementContainingSafeTags_ShouldCopySafeNodes() {
        // Arrange
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        Element root = new Element(Tag.valueOf("div"), "");
        Element child1 = new Element(Tag.valueOf("p"), "");
        Element child2 = new Element(Tag.valueOf("script"), "");
        Element child3 = new Element(Tag.valueOf("a"), "");
        root.appendChild(child1);
        root.appendChild(child2);
        root.appendChild(child3);

        // Act
        Element destination = new Element(Tag.valueOf("div"), "");
        int numDiscarded = cleaner.copySafeNodes(root, destination);

        // Assert
        Assert.assertEquals(1, numDiscarded);
        Assert.assertEquals(2, destination.children().size());
        Assert.assertTrue(destination.children().contains(child1));
        Assert.assertFalse(destination.children().contains(child2));
        Assert.assertTrue(destination.children().contains(child3));
    }

    @Test
    public void testCopySafeNodes_WithElementContainingSafeAttributes_ShouldCopySafeAttributes() {
        // Arrange
        Whitelist whitelist = new Whitelist();
        whitelist.addTags("div");
        whitelist.addAttributes("div", "id");
        Cleaner cleaner = new Cleaner(whitelist);
        Element sourceEl = new Element(Tag.valueOf("div"), "");
        sourceEl.attr("id", "testId");
        sourceEl.attr("class", "testClass");

        // Act
        Element destination = new Element(Tag.valueOf("div"), "");
        int numDiscarded = cleaner.copySafeNodes(sourceEl, destination);

        // Assert
        Assert.assertEquals(1, numDiscarded);
        Assert.assertEquals("testId", destination.attr("id"));
        Assert.assertTrue(destination.attributes().hasKey("id"));
        Assert.assertFalse(destination.attributes().hasKey("class"));
    }

    @Test
    public void testCopySafeNodes_WithElementContainingUnsafeTags_ShouldDiscardUnsafeTags() {
        // Arrange
        Whitelist whitelist = Whitelist.none();
        Cleaner cleaner = new Cleaner(whitelist);
        Element root = new Element(Tag.valueOf("div"), "");
        Element child1 = new Element(Tag.valueOf("p"), "");
        Element child2 = new Element(Tag.valueOf("script"), "");
        Element child3 = new Element(Tag.valueOf("a"), "");
        root.appendChild(child1);
        root.appendChild(child2);
        root.appendChild(child3);

        // Act
        Element destination = new Element(Tag.valueOf("div"), "");
        int numDiscarded = cleaner.copySafeNodes(root, destination);

        // Assert
        Assert.assertEquals(3, numDiscarded);
        Assert.assertEquals(0, destination.children().size());
    }

    @Test
    public void testCopySafeNodes_WithElementContainingTextNodes_ShouldCopyTextNodes() {
        // Arrange
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        Element root = new Element(Tag.valueOf("div"), "");
        root.appendChild("This is some text");
        root.appendChild(new Element(Tag.valueOf("p"), ""));
        root.appendChild("This is some more text");

        // Act
        Element destination = new Element(Tag.valueOf("div"), "");
        int numDiscarded = cleaner.copySafeNodes(root, destination);

        // Assert
        Assert.assertEquals(0, numDiscarded);
        Assert.assertEquals(2, destination.childNodes().size());
        Assert.assertEquals("This is some text", destination.childNodes().get(0).toString());
        Assert.assertEquals("This is some more text", destination.childNodes().get(1).toString());
    }
}