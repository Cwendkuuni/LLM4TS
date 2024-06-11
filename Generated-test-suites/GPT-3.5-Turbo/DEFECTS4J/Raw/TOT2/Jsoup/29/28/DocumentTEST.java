package org.jsoup.nodes;

import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = Document.createShell("http://example.com");
    }

    @Test
    public void testCreateShell() {
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testTitle() {
        assertEquals("", document.title());
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testTitleExisting() {
        document.title("Existing Title");
        document.title("New Title");
        assertEquals("New Title", document.title());
    }

    @Test
    public void testTitleNotNull() {
        assertNotNull(document.title());
    }

    @Test
    public void testNormalise() {
        Document normDoc = document.normalise();
        assertNotNull(normDoc.head());
        assertNotNull(normDoc.body());
    }

    @Test
    public void testCreateElement() {
        Element elem = document.createElement("div");
        assertNotNull(elem);
        assertEquals("div", elem.tagName());
    }

    @Test
    public void testClone() {
        Document clone = document.clone();
        assertNotNull(clone);
        assertEquals(document.outputSettings().charset(), clone.outputSettings().charset());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.charset("UTF-8").prettyPrint(false).indentAmount(2);
        document.outputSettings(outputSettings);
        assertEquals(outputSettings, document.outputSettings());
    }
}