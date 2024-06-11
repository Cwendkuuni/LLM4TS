
package org.jsoup.safety;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class CleanerTest {
    public static final String BASE_URI = "https://www.example.com";

    private Cleaner cleaner;

    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        // Given
        String dirtyHtml = "<html><body><p>Hello, <b>World</b>!</p></body></html>";
        Document dirtyDocument = org.jsoup.Jsoup.parseBodyFragment(dirtyHtml, BASE_URI);

        // When
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Then
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <p>Hello, <b>World</b>!</p>\n </body>\n</html>\n", cleanDocument.outerHtml());
    }

    @Test
    public void testIsValid() {
        // Given
        String validHtml = "<html><body><p>Hello, <b>World</b>!</p></body></html>";
        Document validDocument = org.jsoup.Jsoup.parseBodyFragment(validHtml, BASE_URI);

        String invalidHtml = "<html><body><script>alert('XSS');</script></body></html>";
        Document invalidDocument = org.jsoup.Jsoup.parseBodyFragment(invalidHtml, BASE_URI);

        // When
        boolean validResult = cleaner.isValid(validDocument);
        boolean invalidResult = cleaner.isValid(invalidDocument);

        // Then
        Assert.assertTrue(validResult);
        Assert.assertFalse(invalidResult);
    }

    @Test
    public void testCopySafeNodes() {
        // Given
        Element root = new Element(Tag.valueOf("div"), BASE_URI);
        Element destination = new Element(Tag.valueOf("div"), BASE_URI);
        ArrayList<Node> sourceChildren = new ArrayList<>();
        Element sourceEl = new Element(Tag.valueOf("p"), BASE_URI);
        sourceEl.text("Hello, ");
        sourceChildren.add(sourceEl);

        Element sourceEl2 = new Element(Tag.valueOf("b"), BASE_URI);
        sourceEl2.text("World");
        sourceChildren.add(sourceEl2);

        Element sourceEl3 = new Element(Tag.valueOf("span"), BASE_URI);
        sourceEl3.text("!");
        sourceChildren.add(sourceEl3);

        root.insertChildren(sourceChildren.size(), sourceChildren);

        // When
        int numDiscarded = cleaner.copySafeNodes(root, destination);

        // Then
        Assert.assertEquals(0, numDiscarded);
        Assert.assertEquals("<div>\n <p>Hello, <b>World</b><span>!</span></p>\n</div>\n", destination.outerHtml());
    }

    @Test
    public void testCreateSafeElement() {
        // Given
        Element sourceEl = new Element(Tag.valueOf("a"), BASE_URI);
        Attributes sourceAttrs = sourceEl.attributes();
        sourceAttrs.put("href", "https://www.example.com");
        sourceAttrs.put("onclick", "alert('XSS')");

        // When
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

        // Then
        Assert.assertNotNull(elementMeta);
        Assert.assertEquals("a", elementMeta.el.tagName());
        Assert.assertEquals(1, elementMeta.numAttribsDiscarded);
        Assert.assertEquals("<a href=\"https://www.example.com\"></a>", elementMeta.el.outerHtml());
    }
}
