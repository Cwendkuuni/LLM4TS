package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseSetup;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.CleaningVisitor;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

public class CleanerTest {

    @org.junit.BeforeClass
    public static void setup() {
        Parser.setDefaultHandler(new ParseErrorHandler());
    }

    private Document createTestDocument() {
        String html = "<html><head><title>Title</title></head>"
                + "<body><h1 id='h1'>Test Heading</h1>"
                + "<script>alert('XSS');</script>"
                + "<p id='p1'>Test paragraph with <b>bold</b> text.</p>"
                + "<p id='p2'>Test paragraph with <script>alert('XSS');</script> text.</p>"
                + "</body></html>";
        return Jsoup.parse(html);
    }

    @org.junit.Test
    public void testCleanerConstructor() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    @org.junit.Test
    public void testCleanMethod() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = createTestDocument();
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Check that the original document is not modified
        assertEquals(dirtyDocument.body().text(), dirtyDocument.body().outerHtml());

        // Check that the clean document only contains allowed tags
        Element root = cleanDocument.body();
        assertTrue(root.tagName().equals("body"));
        Elements h1s = root.select("h1");
        assertTrue(h1s.size() > 0);
        Element h1 = h1s.get(0);
        assertTrue(h1.tagName().equals("h1"));
        assertFalse(root.select("script").isEmpty());

        // Check that the clean document does not contain disallowed tags
        assertNull(root.select("script").first());

        // Check that the clean document does not contain disallowed attributes
        Element p1 = root.selectFirst("p#p1");
        assertTrue(p1 != null);
        assertTrue(p1.tagName().equals("p"));
        assertFalse(p1.hasAttr("onclick"));
    }

    @org.junit.Test
    public void testIsValidMethod() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = createTestDocument();
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertFalse(isValid);
    }

    @org.junit.Test
    public void testIsValidMethodWithAllowedDocument() {
        Whitelist whitelist = new Whitelist.SimpleTagNameElementWhitelist("h1", "p");
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = createTestDocument();
        Document cleanDocument = Jsoup.parse("<html><body><h1>Test Heading</h1></body></html>");
        boolean isValid = cleaner.isValid(cleanDocument);
        assertTrue(isValid);
    }

    @org.junit.Test
    public void testCleaningVisitor() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = createTestDocument();
        Element root = dirtyDocument.body();

        CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, null);
        assertTrue(cleaningVisitor.numDiscarded == 0); // Initially there are no discarded nodes

        // Test handling of safe elements
        Element h1 = root.selectFirst("h1");
        Element clone = cleaningVisitor.head(h1, 0);
        assertTrue(clone != null);
        assertTrue(clone.tagName().equals("h1"));
        assertTrue(cleaningVisitor.numDiscarded == 0);

        // Test handling of text nodes
        TextNode textNode = root.selectFirstText();
        cleaningVisitor.head(textNode, 0);
        assertTrue(cleaningVisitor.numDiscarded == 0);

        // Test handling of data nodes
        DataNode dataNode = root.selectFirstData();
        cleaningVisitor.head(dataNode, 0);
        assertTrue(cleaningVisitor.numDiscarded == 0);

        // Test handling of disallowed elements
        Element script = root.selectFirst("script");
        cleaningVisitor.head(script, 0);
        assertTrue(cleaningVisitor.numDiscarded > 0);

        // Test handling of disallowed attributes
        Element p = root.selectFirst("p");
        Element pClone = cleaningVisitor.head(p, 0);
        assertTrue(pClone != null);
        assertTrue(pClone.hasAttr("onclick")); // Enforced attribute is still present
        assertTrue(cleaningVisitor.numDiscarded > 0);

        // Test handling of empty elements
        Element img = root.selectFirst("img");
        cleaningVisitor.head(img, 0);
        assertTrue(cleaningVisitor.numDiscarded > 0);
    }

    @org.junit.Test
    public void testCopySafeNodesMethod() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = createTestDocument();
        Element root = dirtyDocument.body();

        CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, null);
        Element destination = new Element("div");

        // Test copying safe nodes
        Element h1 = root.selectFirst("h1");
        cleaningVisitor.head(h1, 0);
        Element h1Clone = cleaningVisitor.destination;
        assertTrue(h1Clone != null);
        assertTrue(h1Clone.tagName().equals("h1"));
        assertTrue(cleaningVisitor.numDiscarded == 0);
        destination.appendChild(h1Clone);

        TextNode textNode = root.selectFirstText();
        cleaningVisitor.head(textNode, 0);
        TextNode textNodeClone = cleaningVisitor.destination;
        assertTrue(textNodeClone != null);
        assertTrue(textNodeClone.text().equals(textNode.text()));
        assertTrue(cleaningVisitor.numDiscarded == 0);
        destination.appendChild(textNodeClone);

        DataNode dataNode = root.selectFirstData();
        cleaningVisitor.head(dataNode, 0);
        DataNode dataNodeClone = cleaningVisitor.destination;
        assertTrue(dataNodeClone != null);
        assertTrue(dataNodeClone.data().equals(dataNode.data()));
        assertTrue(cleaningVisitor.numDiscarded == 0);
        destination.appendChild(dataNodeClone);

        cleaningVisitor.head(root.selectFirst("script"), 0);
        assertTrue(cleaningVisitor.numDiscarded > 0);

        // Test copying safe nodes with enforced attributes
        Element p = root.selectFirst("p");
        cleaningVisitor.head(p, 0);
        Element pClone = cleaningVisitor.destination;
        assertTrue(pClone != null);
        assertTrue(pClone.tagName().equals("p"));
        assertTrue(pClone.hasAttr("onclick"));
        assertTrue(cleaningVisitor.numDiscarded > 0);
        destination.appendChild(pClone);
    }

    @org.junit.Test
    public void testCreateSafeElementMethod() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        Element sourceEl = new Element("div", "id", "testDiv");

        ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        assertTrue(elementMeta.el != null);
        assertTrue(elementMeta.el.tagName().equals("div"));
        assertTrue(elementMeta.numAttribsDiscarded == 1); // The "id" attribute is discarded
    }

    @org.junit.Test
    public void testCopySafeNodesMethodWithEmptyDocument() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Document.createShell(null);

        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), null);
        assertTrue(numDiscarded == 0);
    }

    @org.junit.Test
    public void testParseErrorHandler() {
        Parser parser = new Parser();
        parser.setDefaultHandler(new ParseErrorHandler());

        ParseError error = assertThrows(ParseError.class, () -> parser.parse("<html><body><script>1+1;</script></body></html>"));
        assertTrue(error.getLine() > 0);
        assertTrue(error.getColumn() > 0);
        assertTrue(error.getMessage().contains("script"));
    }

    @org.junit.Test
    public void testIsValidMethodWithEmptyDocument() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Document.createShell(null);

        boolean isValid = cleaner.isValid(dirtyDocument);
        assertFalse(isValid);
    }

    private static class ParseErrorHandler implements Parser.Handler {
        @Override
        public void handleError(ParseError error) {
            throw new RuntimeException("Parse error: " + error.getMessage(), error);
        }

        @Override
        public void handleStart(int depth, int pos, int line, int column) {
            // No action
        }

        @Override
        public void handleText(int depth, int pos, int line, int column, String text) {
            // No action
        }

        @Override
        public void handleStartTag(int depth, int pos, int line, int column, String name, Attributes attrs) {
            // No action
        }

        @Override
        public void handleEndTag(int depth, int pos, int line, int column, String name) {
            // No action
        }

        @Override
        public void handleEmptyTag(int depth, int pos, int line, int column, String name) {
            // No action
        }

        @Override
        public void handleAttribute(int depth, int pos, int line, int column, String name, String value) {
            // No action
        }

        @Override
        public void handleComment(int depth, int pos, int line, int column, String text) {
            // No action
        }

        @Override
        public void handleScript(int depth, int pos, int line, int column, String src) {
            // No action
        }

        @Override
        public void handleEntity(int depth, int pos, int line, int column, Entity entity) {
            // No action
        }

        @Override
        public void handleError(ParseError error, int depth, int pos, int line, int column) {
            // No action
        }
    }

}