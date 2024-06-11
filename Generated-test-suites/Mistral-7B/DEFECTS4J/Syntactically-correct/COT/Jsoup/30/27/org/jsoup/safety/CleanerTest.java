package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Whitelist.AllowAllFilter;
import org.jsoup.safety.Whitelist.RelaxedFilter;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Test for Cleaner class
 */
public class CleanerTest {

    private Document dirtyDocument;
    private Document cleanDocument;
    private Whitelist whitelist;
    private Cleaner cleaner;

    @Before
    public void setup() {
        // Create a Whitelist with allowAllFilter for testing purpose
        whitelist = Whitelist.simpleText();
        whitelist.addTags("p", "a");
        whitelist.addAttributes("p", "class", "id");
        whitelist.addAttributes("a", "*");

        cleaner = new Cleaner(whitelist);

        String html = FileUtils.readFileToString(new File("test.html"), "UTF-8");
        dirtyDocument = Jsoup.parse(html);
        cleanDocument = Jsoup.parseBodyFragment(html);
    }

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        Cleaner cleaner = new Cleaner(null);
        assertNull(cleaner.whitelist);
    }

    /**
     * Test clean method with valid input
     */
    @Test
    public void testCleanValidInput() {
        Document expectedCleanDocument = cleanDocument;
        Document actualCleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("Clean method should return the same document when input is valid", expectedCleanDocument, actualCleanDocument);
    }

    /**
     * Test clean method with invalid input
     */
    @Test
    public void testCleanInvalidInput() {
        // Create a test case with invalid input
        Document invalidDocument = Jsoup.parse("<script>alert('XSS')</script>");

        Document expectedCleanDocument = cleanDocument;
        Document actualCleanDocument = cleaner.clean(invalidDocument);
        assertEquals("Clean method should return the same document when input is invalid, but cleaned", expectedCleanDocument, actualCleanDocument);
        assertFalse("Clean method should detect and discard the script tag", actualCleanDocument.getElementsByTag("script").isEmpty());
    }

    /**
     * Test isValid method with valid input
     */
    @Test
    public void testIsValidValidInput() {
        assertTrue("isValid method should return true when input is valid", cleaner.isValid(dirtyDocument));
    }

    /**
     * Test isValid method with invalid input
     */
    @Test
    public void testIsValidInvalidInput() {
        Document invalidDocument = Jsoup.parse("<script>alert('XSS')</script>");
        assertFalse("isValid method should return false when input is invalid", cleaner.isValid(invalidDocument));
    }

    /**
     * Test copySafeNodes method with valid input
     */
    @Test
    public void testCopySafeNodesValidInput() {
        Element root = dirtyDocument.body();
        int numDiscarded = cleaner.copySafeNodes(root, cleanDocument.body());
        assertEquals("Number of discarded nodes should be zero when input is valid", 0, numDiscarded);
    }

    /**
     * Test copySafeNodes method with invalid input (script tag)
     */
    @Test
    public void testCopySafeNodesInvalidInputScript() {
        Element root = dirtyDocument.body();
        int numDiscarded = cleaner.copySafeNodes(root, cleanDocument.body());
        assertTrue("Number of discarded nodes should be greater than zero when input contains script tag", numDiscarded > 0);
    }

    /**
     * Test copySafeNodes method with invalid input (style tag)
     */
    @Test
    public void testCopySafeNodesInvalidInputStyle() {
        String html = "<style>body { color: red; }</style><p>This is a test</p>";
        Document dirtyDocument = Jsoup.parse(html);
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
        assertTrue("Number of discarded nodes should be greater than zero when input contains style tag", numDiscarded > 0);
    }

    /**
     * Test createSafeElement method with valid input
     */
    @Test
    public void testCreateSafeElementValidInput() {
        Element sourceEl = dirtyDocument.body().elementSibling(0);
        ElementMeta actualElementMeta = cleaner.createSafeElement(sourceEl);
        assertNotNull("createSafeElement method should not return null", actualElementMeta);
        assertNotNull("createSafeElement method should create a new Element", actualElementMeta.el);
    }

    /**
     * Test createSafeElement method with invalid input (script tag)
     */
    @Test
    public void testCreateSafeElementInvalidInputScript() {
        Element sourceEl = dirtyDocument.body().elementSibling(0);
        ElementMeta actualElementMeta = cleaner.createSafeElement(sourceEl);
        assertNull("createSafeElement method should return null when input is an script tag", actualElementMeta);
    }

    /**
     * Test createSafeElement method with invalid input (style tag)
     */
    @Test
    public void testCreateSafeElementInvalidInputStyle() {
        Element sourceEl = dirtyDocument.body().select("style").first();
        ElementMeta actualElementMeta = cleaner.createSafeElement(sourceEl);
        assertNull("createSafeElement method should return null when input is a style tag", actualElementMeta);
    }
}