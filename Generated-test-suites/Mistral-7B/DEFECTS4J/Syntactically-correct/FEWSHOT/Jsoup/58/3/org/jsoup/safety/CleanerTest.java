package org.jsoup.safety;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

public class CleanerTest {

    @Test
    public void testCreateCleaner() {
        Whitelist whitelist = new BasicCollectorWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    @Test
    public void testCleanDocument() {
        Whitelist whitelist = new BasicCollectorWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Parser.parseBodyFragment("<html><body><div>Hello <script>alert('world')</script></div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Element root = cleanDocument.body();
        assertNotNull(root);
        assertTrue(root.children().size() == 0);
    }

    @Test
    public void testIsValidDocument() {
        Whitelist whitelist = new BasicCollectorWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Parser.parseBodyFragment("<html><body><div>Hello <script>alert('world')</script></div></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertFalse(isValid);

        Document validDocument = Parser.parseBodyFragment("<html><body><div>Hello World</div></body></html>");
        isValid = cleaner.isValid(validDocument);
        assertTrue(isValid);
    }

    @Test
    public void testCleanDocumentWithSafeContent() {
        Whitelist whitelist = new BasicCollectorWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Parser.parseBodyFragment("<html><body><div>Hello <strong>World</strong></div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Element root = cleanDocument.body();
        Element div = root.child(0);
        assertEquals("div", div.tagName());
        assertEquals("Hello World", div.text());
        assertTrue(div.children().size() == 0);
    }

    @Test
    public void testCleanDocumentWithAttributes() {
        Whitelist whitelist = new BasicCollectorWhitelist();
        Whitelist attributeWhitelist = new AttributesWhitelist();
        whitelist.addAttributeSpec("*", "*", "*", "class");
        whitelist.addAttributeSpec("div", "*", "*", "id", "style");
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Parser.parseBodyFragment("<html><body><div id='test' class='test' style='color: red;'>Hello World</div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Element root = cleanDocument.body();
        Element div = root.child(0);
        assertEquals("div", div.tagName());
        assertEquals("Hello World", div.text());
        Element idAttribute = div.attr("id");
        assertEquals("test", idAttribute.text());
        Elements classAttributes = div.classes();
        assertEquals(1, classAttributes.size());
        assertEquals("test", classAttributes.get(0).text());
        Element styleAttribute = div.attr("style");
        assertEquals("color: red;", styleAttribute.text());
    }

    @Test
    public void testCleanDocumentWithFrameset() {
        Whitelist whitelist = new BasicCollectorWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Parser.parseBodyFragment("<html><head><title>Test</title></head><frameset rows='50%, 50%'><frameset cols='50%, 50%'><frame src='frame1.html'></frame><frame src='frame2.html'></frame></frameset></frameset></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Element root = cleanDocument.body();
        assertNotNull(root);
        assertTrue(root.children().size() == 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCleanerWithNullWhitelist() {
        Cleaner cleaner = new Cleaner(null);
    }
}