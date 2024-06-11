
package org.jsoup.safety;

import org.junit.Test;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

public class CleanerTest {
    @Test
    public void testClean() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.createShell("http://example.com");
        dirtyDocument.body().append("<h1>Test</h1>");
        dirtyDocument.body().append("<script>alert('XSS')</script>");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertTrue(cleanDocument.body().child(0) instanceof Element);
        assertEquals("h1", cleanDocument.body().child(0).tagName());
        assertEquals("Test", cleanDocument.body().child(0).text());
        assertNull(cleanDocument.body().child(1));
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document validDocument = Document.createShell("http://example.com");
        validDocument.body().append("<h1>Test</h1>");

        Document invalidDocument = Document.createShell("http://example.com");
        invalidDocument.body().append("<script>alert('XSS')</script>");

        assertTrue(cleaner.isValid(validDocument));
        assertFalse(cleaner.isValid(invalidDocument));
    }
}
