
package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner.ElementMeta;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        Document dirtyDocument = Document.createShell("http://example.com");
        Element body = dirtyDocument.body();
        Element div = new Element(Tag.valueOf("div"), "http://example.com");
        div.appendElement("p").text("This is a test.");
        body.appendChild(div);

        Document cleanDocument = cleaner.clean(dirtyDocument);

        Element cleanBody = cleanDocument.body();
        assertEquals(1, cleanBody.children().size());
        assertEquals("div", cleanBody.children().get(0).tagName());
        assertEquals(1, cleanBody.children().get(0).children().size());
        assertEquals("p", cleanBody.children().get(0).children().get(0).tagName());
        assertEquals("This is a test.", cleanBody.children().get(0).children().get(0).text());
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = Document.createShell("http://example.com");
        Element body = dirtyDocument.body();
        Element div = new Element(Tag.valueOf("div"), "http://example.com");
        div.appendElement("p").text("This is a test.");
        body.appendChild(div);

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertEquals(true, isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Element root = new Element(Tag.valueOf("root"), "http://example.com");
        Element p = new Element(Tag.valueOf("p"), "http://example.com");
        p.text("This is a test.");
        root.appendChild(p);

        Element destination = new Element(Tag.valueOf("destination"), "http://example.com");

        int numDiscarded = cleaner.copySafeNodes(root, destination);

        assertEquals(0, numDiscarded);
        assertEquals(1, destination.children().size());
        assertEquals("p", destination.children().get(0).tagName());
        assertEquals("This is a test.", destination.children().get(0).text());
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element(Tag.valueOf("div"), "http://example.com");
        sourceEl.attr("class", "test");
        String sourceTag = sourceEl.tagName();

        ElementMeta meta = cleaner.createSafeElement(sourceEl);

        Element dest = meta.el;
        assertEquals(sourceTag, dest.tagName());
        assertEquals("http://example.com", dest.baseUri());
        assertEquals(0, meta.numAttribsDiscarded);
        Attributes destAttrs = dest.attributes();
        assertEquals(1, destAttrs.size());
        assertEquals("test", destAttrs.get("class"));
    }
}
