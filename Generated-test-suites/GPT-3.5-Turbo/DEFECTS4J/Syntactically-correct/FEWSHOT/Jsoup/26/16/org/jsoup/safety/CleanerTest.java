package org.jsoup.safety;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.List;

public class CleanerTest {

    @Test
    public void testConstructor() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        assertEquals(whitelist, cleaner.whitelist);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullWhitelist() {
        Cleaner cleaner = new Cleaner(null);
    }

    @Test
    public void testClean() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = new Document("");
        dirtyDocument.appendElement("body").append("Hello, <b>world!</b>");

        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><head></head><body>Hello, <b>world!</b></body></html>", cleanDocument.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testCleanNullDocument() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        cleaner.clean(null);
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document validDocument = new Document("");
        validDocument.appendElement("body").append("Hello, <b>world!</b>");

        Document invalidDocument = new Document("");
        invalidDocument.appendElement("body").append("<script>alert(\"XSS Attack!\");</script>");

        assertTrue(cleaner.isValid(validDocument));
        assertFalse(cleaner.isValid(invalidDocument));
    }

    @Test(expected = NullPointerException.class)
    public void testIsValidNullDocument() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        cleaner.isValid(null);
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element source = new Element(Tag.valueOf("div"), "");
        source.append("This is a <b>test</b>.");

        Element dest = new Element(Tag.valueOf("div"), "");
        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertEquals(0, numDiscarded);
        assertEquals("This is a <b>test</b>.", dest.html());
    }

    @Test
    public void testCopySafeNodesWithUnsafeElement() {
        Whitelist whitelist = new Whitelist();
        whitelist.addTags("div");
        Cleaner cleaner = new Cleaner(whitelist);

        Element source = new Element(Tag.valueOf("div"), "");
        source.appendChild(new Element(Tag.valueOf("script"), "").text("alert(\"XSS Attack!\");"));

        Element dest = new Element(Tag.valueOf("div"), "");
        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertEquals(1, numDiscarded);
        assertEquals("", dest.html());
    }

    @Test
    public void testCopySafeNodesWithUnsafeAttribute() {
        Whitelist whitelist = new Whitelist();
        whitelist.addAttributes("a", "href");
        Cleaner cleaner = new Cleaner(whitelist);

        Element source = new Element(Tag.valueOf("a"), "");
        source.attr("href", "javascript:alert('XSS Attack!');");

        Element dest = new Element(Tag.valueOf("a"), "");
        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertEquals(1, numDiscarded);
        assertEquals("", dest.html());
    }

    @Test
    public void testCopySafeNodesWithEnforcedAttribute() {
        Whitelist whitelist = new Whitelist();
        whitelist.addAttributes("a", "href");
        whitelist.addEnforcedAttribute("a", "rel", "nofollow");
        Cleaner cleaner = new Cleaner(whitelist);

        Element source = new Element(Tag.valueOf("a"), "");
        source.attr("href", "http://example.com");

        Element dest = new Element(Tag.valueOf("a"), "");
        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertEquals(0, numDiscarded);
        assertEquals("http://example.com", dest.attr("href"));
        assertEquals("nofollow", dest.attr("rel"));
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element source = new Element(Tag.valueOf("a"), "");
        source.attr("href", "http://example.com");

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(source);

        assertEquals("a", elementMeta.el.tagName());
        assertEquals("http://example.com", elementMeta.el.attr("href"));
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElementWithUnsafeAttribute() {
        Whitelist whitelist = new Whitelist();
        whitelist.addAttributes("a", "href");
        Cleaner cleaner = new Cleaner(whitelist);

        Element source = new Element(Tag.valueOf("a"), "");
        source.attr("href", "javascript:alert('XSS Attack!');");

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(source);

        assertEquals("a", elementMeta.el.tagName());
        assertEquals("", elementMeta.el.attr("href"));
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElementWithEnforcedAttribute() {
        Whitelist whitelist = new Whitelist();
        whitelist.addAttributes("a", "href");
        whitelist.addEnforcedAttribute("a", "rel", "nofollow");
        Cleaner cleaner = new Cleaner(whitelist);

        Element source = new Element(Tag.valueOf("a"), "");
        source.attr("href", "http://example.com");

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(source);

        assertEquals("a", elementMeta.el.tagName());
        assertEquals("http://example.com", elementMeta.el.attr("href"));
        assertEquals("nofollow", elementMeta.el.attr("rel"));
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }

}