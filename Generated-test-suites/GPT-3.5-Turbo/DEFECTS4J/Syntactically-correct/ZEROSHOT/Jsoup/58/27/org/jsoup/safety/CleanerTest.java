package org.jsoup.safety;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {

    private Cleaner cleaner;
    private Whitelist whitelist;

    @Before
    public void setUp() {
        whitelist = Whitelist.relaxed();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean() {
        Document dirtyDocument = Document.createShell("");
        Element body = new Element(Tag.valueOf("body"), "");
        body.appendChild(new Element(Tag.valueOf("p"), "").text("Hello, World!"));
        dirtyDocument.appendChild(body);

        Document cleanDocument = cleaner.clean(dirtyDocument);

        Elements paragraphs = cleanDocument.getElementsByTag("p");
        assertEquals("Expected one paragraph", 1, paragraphs.size());
        assertEquals("Expected 'Hello, World!'", "Hello, World!", paragraphs.first().text());
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = Document.createShell("");
        Element body = new Element(Tag.valueOf("body"), "");
        body.appendChild(new Element(Tag.valueOf("script"), ""));
        dirtyDocument.appendChild(body);

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse("Expected document to be invalid", isValid);
    }
}