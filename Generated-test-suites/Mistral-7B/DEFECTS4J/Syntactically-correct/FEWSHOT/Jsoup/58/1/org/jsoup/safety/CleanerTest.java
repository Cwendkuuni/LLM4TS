package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

/**
 * Test cases for Cleaner class
 */
public class CleanerTest {

    @Test
    public void testCleanerWithEmptyWhitelist() {
        Whitelist emptyWhitelist = Whitelist.empty();
        Cleaner cleaner = new Cleaner(emptyWhitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNull(cleanDocument.body());
    }

    @Test
    public void testCleanerWithBasicWhitelist() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><p>Hello World!</p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><body><p></p></body></html>", cleanDocument.html());
    }

    @Test
    public void testCleanerWithBasicInvalidDocument() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
        assertFalse(cleaner.isValid(dirtyDocument));
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><body></body></html>", cleanDocument.html());
    }

    @Test
    public void testCleanerWithInvalidDocumentWithScriptTag() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
        assertFalse(cleaner.isValid(dirtyDocument));
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><body></body></html>", cleanDocument.html());
    }

    @Test
    public void testCleanerWithInvalidDocumentWithScriptTagInsideScriptTag() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><script><script>alert('XSS')</script></script></body></html>");
        assertFalse(cleaner.isValid(dirtyDocument));
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><body></body></html>", cleanDocument.html());
    }

    @Test
    public void testCleanerWithInvalidDocumentWithScriptTagInsideScriptTagAndComments() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><script><!--<script>alert('XSS')</script>--></script></body></html>");
        assertFalse(cleaner.isValid(dirtyDocument));
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><body></body></html>", cleanDocument.html());
    }

    @Test
    public void testCleanerWithValidDocument() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><p>Hello <b>World!</b></p></body></html>");
        assertTrue(cleaner.isValid(dirtyDocument));
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><body><p><b></b></p></body></html>", cleanDocument.html());
    }

    @Test
    public void testCleanerWithValidDocumentWithScriptTag() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><script src=\"script.js\"></script><p>Hello World!</p></body></html>");
        assertTrue(cleaner.isValid(dirtyDocument));
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><body><p></p></body></html>", cleanDocument.html());
    }

    @Test
    public void testCleanerWithValidDocumentWithScriptTagAndAllowedAttribute() {
        Whitelist whitelist = Whitelist.simpleText();
        whitelist.addTagCssClass("script", "src");
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><script src=\"script.js\"></script><p>Hello World!</p></body></html>");
        assertTrue(cleaner.isValid(dirtyDocument));
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element script = cleanDocument.selectFirst("script");
        assertEquals("script.js", script.attr("src"));
        assertEquals("<html><body><script src=\"script.js\"></script><p></p></body></html>", cleanDocument.html());
    }

    @Test
    public void testCleanerWithValidDocumentWithScriptTagAndDisallowedAttribute() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><script src=\"script.js\" type=\"text/javascript\"></script><p>Hello World!</p></body></html>");
        assertFalse(cleaner.isValid(dirtyDocument));
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><body><p></p></body></html>", cleanDocument.html());
    }

    @Test
    public void testCleanerWithDocumentWithMultipleNodes() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><p>Hello <b>World!</b></p><script>alert('XSS')</script></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element p = cleanDocument.selectFirst("p");
        Element b = p.selectFirst("b");
        assertEquals("<html><body><p><b></b></p></body></html>", cleanDocument.html());
        assertEquals("Hello <b></b>", p.text());
        assertNull(cleanDocument.body().child(1));
    }

    @Test
    public void testCleanerWithDocumentWithMultipleScriptTags() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><script src=\"script1.js\"></script><script src=\"script2.js\"></script><p>Hello World!</p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element script1 = cleanDocument.selectFirst("script");
        Element script2 = cleanDocument.selectFirst("script");
        assertEquals("<html><body><p></p></body></html>", cleanDocument.html());
        assertNull(cleanDocument.body().child(1));
        assertNull(cleanDocument.body().child(2));
    }

    @Test
    public void testCleanerWithDocumentWithMultipleNodesAndAllowedScriptTag() {
        Whitelist whitelist = Whitelist.simpleText();
        whitelist.addTagCssClass("script", "src");
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><script src=\"script1.js\"></script><p>Hello World!</p><script src=\"script2.js\"></script></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element script1 = cleanDocument.selectFirst("script");
        Element script2 = cleanDocument.selectFirst("script");
        assertEquals("<html><body><script src=\"script1.js\"></script><p></p><script src=\"script2.js\"></script></body></html>", cleanDocument.html());
        assertEquals("script1.js", script1.attr("src"));
        assertEquals("script2.js", script2.attr("src"));
    }

    @Test
    public void testCleanerWithDocumentWithMultipleNodesAndDisallowedScriptTag() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><script src=\"script1.js\"></script><script src=\"script2.js\"></script><p>Hello World!</p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element p = cleanDocument.selectFirst("p");
        assertEquals("<html><body><p></p></body></html>", cleanDocument.html());
        assertNull(cleanDocument.body().child(1));
        assertNull(cleanDocument.body().child(2));
    }

    @Test
    public void testCleanerWithDocumentWithComment() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><p>Hello World! <!-- <script>alert('XSS')</script> --></p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><body><p>Hello World!</p></body></html>", cleanDocument.html());
    }

    @Test
    public void testCleanerWithDocumentWithCssClass() {
        Whitelist whitelist = Whitelist.simpleText();
        whitelist.addTagCssClass("body", "class");
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body class=\"someClass\"><p>Hello World!</p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><body class=\"\"><p></p></body></html>", cleanDocument.html());
    }

    @Test
    public void testCleanerWithDocumentWithCssClassAndAllowedCssClass() {
        Whitelist whitelist = Whitelist.simpleText();
        whitelist.addTagCssClass("body", "class");
        whitelist.addTagCssClass("p", "class");
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body class=\"someClass\"><p class=\"otherClass\">Hello World!</p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element body = cleanDocument.body();
        Element p = body.selectFirst("p");
        assertEquals("<html><body class=\"\"><p class=\"\"></p></body></html>", cleanDocument.html());
        assertEquals("otherClass", p.classAttr());
    }

    @Test
    public void testCleanerWithDocumentWithCssClassAndDisallowedCssClass() {
        Whitelist whitelist = Whitelist.simpleText();
        whitelist.addTagCssClass("body", "class");
        whitelist.addTagCssClass("p", "class");
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body class=\"someClass\"><p class=\"someClass otherClass\">Hello World!</p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><body class=\"\"><p></p></body></html>", cleanDocument.html());
    }

    @Test
    public void testCleanerWithDocumentWithParseError() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><script>document.write('Hello World!');</script></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><body></body></html>", cleanDocument.html());
    }

    @Test
    public void testCleanerWithDocumentWithParseErrorAndAllowedScriptTag() {
        Whitelist whitelist = Whitelist.simpleText();
        whitelist.addTagCssClass("script", "src");
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><script src=\"script.js\">document.write('Hello World!');</script></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element script = cleanDocument.selectFirst("script");
        assertEquals("<html><body><script src=\"script.js\"></script></body></html>", cleanDocument.html());
        assertNotNull(script);
    }

    @Test
    public void testCleanerWithDocumentWithParseErrorAndDisallowedScriptTag() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><script src=\"script.js\">document.write('Hello World!');</script></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><body></body></html>", cleanDocument.html());
    }
}