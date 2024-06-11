package org.jsoup.safety;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        // Typical use case
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document dirty = new Document("<div><script>alert('XSS');</script></div>");
        Document clean = cleaner.clean(dirty);
        assertEquals("<div></div>", clean.toString());
    }

    @Test
    public void testIsValid() {
        // Typical use case
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Document dirty = new Document("<div><p>Valid content</p></div>");
        assertTrue(cleaner.isValid(dirty));

        // Edge case - Document with unsafe elements
        Document unsafe = new Document("<div><iframe src='https://example.com'></iframe></div>");
        assertFalse(cleaner.isValid(unsafe));
    }

    @Test
    public void testCopySafeNodes() {
        // Typical use case
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Element source = new Element(Tag.valueOf("div"), "");
        Element dest = new Element(Tag.valueOf("div"), "");
        source.appendChild(new Element(Tag.valueOf("p"), ""));
        source.appendChild(new Element(Tag.valueOf("a"), ""));
        source.appendChild(new Element(Tag.valueOf("img"), ""));
        int discarded = cleaner.copySafeNodes(source, dest);
        assertEquals(1, discarded);

        // Edge case - Element with unsafe child
        Element parent = new Element(Tag.valueOf("div"), "");
        Element unsafeChild = new Element(Tag.valueOf("iframe"), "");
        parent.appendChild(unsafeChild);
        int discardedUnsafe = cleaner.copySafeNodes(parent, dest);
        assertEquals(1, discardedUnsafe);
    }

    @Test
    public void testCreateSafeElement() {
        Cleaner cleaner = new Cleaner(Whitelist.basic());
        Element sourceEl = new Element(Tag.valueOf("img"), "");
        ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        assertEquals(Tag.valueOf("img"), elementMeta.el.tag());
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }

}