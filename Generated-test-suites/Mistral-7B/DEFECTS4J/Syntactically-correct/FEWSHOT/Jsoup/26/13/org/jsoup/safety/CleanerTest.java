package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

import java.util.Arrays;
import java.util.List;

public class CleanerTest {

    private Whitelist getSampleWhitelist() {
        return Whitelist.simpleText();
    }

    @Test
    public void testCleanerConstruction() {
        Whitelist whitelist = getSampleWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        assertEquals(whitelist, cleaner.whitelist);
    }

    @Test(expected = NullPointerException.class)
    public void testCleanerNullWhitelist() {
        Cleaner cleaner = new Cleaner(null);
    }

    @Test
    public void testCleanDocument() {
        Whitelist whitelist = getSampleWhitelist();
        Document dirtyDocument = Document.createShell("http://example.com");
        Element body = dirtyDocument.createElement("body");
        body.appendChild(element("script", null, null, "alert('XSS')"));
        body.appendChild(element("div", null, null, "Unsafe content"));
        dirtyDocument.setBody(body);

        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNull(cleanDocument.getElementById("xss-script"));
        assertTrue(cleanDocument.body().children().isEmpty());
    }

    @Test
    public void testIsValidDocument() {
        Whitelist whitelist = getSampleWhitelist();
        Document dirtyDocument = Document.createShell("http://example.com");
        Element body = dirtyDocument.createElement("body");
        body.appendChild(element("script", null, null, "alert('XSS')"));
        body.appendChild(element("div", null, null, "Unsafe content"));
        dirtyDocument.setBody(body);

        Cleaner cleaner = new Cleaner(whitelist);
        assertFalse(cleaner.isValid(dirtyDocument));

        Document validDocument = Document.createShell("http://example.com");
        Element bodyValid = validDocument.createElement("body");
        bodyValid.appendChild(element("div", null, null, "Safe content"));
        validDocument.setBody(bodyValid);

        cleaner = new Cleaner(whitelist);
        assertTrue(cleaner.isValid(validDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = getSampleWhitelist();
        Document dirtyDocument = Document.createShell("http://example.com");
        Element body = dirtyDocument.createElement("body");

        Element script = element("script", null, null, "alert('XSS')");
        body.appendChild(script);
        body.appendChild(element("div", null, null, "Safe content"));

        dirtyDocument.setBody(body);

        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element bodyClean = cleanDocument.body();

        List<Node> children = Arrays.asList(
                element("div", null, null, "Safe content")
        );
        assertEquals(children, bodyClean.childNodesList());
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = getSampleWhitelist();
        Document dirtyDocument = Document.createShell("http://example.com");
        Element body = dirtyDocument.createElement("body");
        Element unsafeElement = element("script", null, null, "alert('XSS')");
        body.appendChild(unsafeElement);

        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element bodyClean = cleanDocument.body();

        Element safeElement = element("script", null, null, null);
        Attributes safeAttributes = safeElement.attributes();

        Element createdElement = cleaner.createSafeElement(unsafeElement);
        Element metaElement = createdElement.tag();
        assertEquals(Tag.SCRIPT, metaElement.getName());
        assertSame(safeElement, createdElement);

        List<Map.Entry<String, String>> attributes = safeAttributes.entries();
        assertTrue(attributes.isEmpty());
    }

    private Element element(String tagName, String... classes) {
        Attributes attributes = new Attributes();
        if (classes != null) {
            for (String className : classes) {
                attributes.put(Tag. Clinton.HREF, className);
            }
        }
        return new Element(Tag.valueOf(tagName), null, attributes);
    }

}