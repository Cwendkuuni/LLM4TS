
package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
    private Whitelist whitelist;
    private Cleaner cleaner;

    @Before
    public void setup() {
        whitelist = Whitelist.simpleText();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean() {
        String dirtyHtml = "<html><head><title>Dirty HTML</title></head><body><h1>Welcome!</h1><p>This is dirty <i>HTML</i></p></body></html>";
        
        Document dirtyDocument = Jsoup.parse(dirtyHtml);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        assertEquals("Welcome!", cleanDocument.body().getElementsByTag("h1").text());
        assertEquals("This is dirty HTML", cleanDocument.body().getElementsByTag("p").text());
    }

    @Test
    public void testIsValid() {
        String safeHtml = "<h1>Welcome!</h1><p>This is safe HTML</p>";
        Document safeDocument = Jsoup.parse(safeHtml);
        
        assertTrue(cleaner.isValid(safeDocument));
        
        String unsafeHtml = "<h1>Welcome!</h1><script>alert('XSS');</script>";
        Document unsafeDocument = Jsoup.parse(unsafeHtml);
        
        assertFalse(cleaner.isValid(unsafeDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Element sourceElement = new Element(Tag.valueOf("div"), "");
        Element destElement = new Element(Tag.valueOf("div"), "");
        
        Element p = new Element(Tag.valueOf("p"), "");
        p.text("This is safe");
        sourceElement.appendChild(p);
        
        Element script = new Element(Tag.valueOf("script"), "");
        script.text("alert('XSS');");
        sourceElement.appendChild(script);
        
        int numDiscarded = cleaner.copySafeNodes(sourceElement, destElement);
        
        assertEquals(1, numDiscarded);
        assertEquals("This is safe", destElement.getElementsByTag("p").text());
        assertEquals(0, destElement.getElementsByTag("script").size());
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceElement = new Element(Tag.valueOf("div"), "");
        sourceElement.attr("unsafe", "true");
        sourceElement.attr("safe", "true");
        
        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
        Element destElement = elementMeta.el;
        
        Attributes destAttributes = destElement.attributes();
        
        assertTrue(destAttributes.hasKey("safe"));
        assertFalse(destAttributes.hasKey("unsafe"));
    }
}
