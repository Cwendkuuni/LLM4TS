package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        Element body = dirtyDocument.body();

        Element div = new Element(Tag.valueOf("div"), "");
        div.text("Hello, World!");
        body.appendChild(div);

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<body><div>Hello, World!</div></body>", cleanDocument.body().outerHtml());
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = Document.createShell("");
        Element body = dirtyDocument.body();

        Element div = new Element(Tag.valueOf("div"), "");
        div.text("Hello, World!");
        body.appendChild(div);

        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValid_invalidDocument() {
        Document dirtyDocument = Document.createShell("");
        Element body = dirtyDocument.body();

        Element script = new Element(Tag.valueOf("script"), "");
        script.text("alert('Hello, World!');");
        body.appendChild(script);

        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCleaningVisitor_headSafeTag() {
        Document dirtyDocument = Document.createShell("");
        Element body = dirtyDocument.body();

        Element p = new Element(Tag.valueOf("p"), "");
        p.text("Hello, World!");
        body.appendChild(p);

        Cleaner.CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(dirtyDocument.body(), new Element(Tag.valueOf("body"), ""));
        cleaningVisitor.head(p, 0);

        assertEquals("<body><p>Hello, World!</p></body>", cleaningVisitor.destination.outerHtml());
    }

    @Test
    public void testCleaningVisitor_headUnsafeTag() {
        Document dirtyDocument = Document.createShell("");
        Element body = dirtyDocument.body();

        Element script = new Element(Tag.valueOf("script"), "");
        script.attr("src", "http://example.com/js/malicious.js");
        body.appendChild(script);

        Cleaner.CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(dirtyDocument.body(), new Element(Tag.valueOf("body"), ""));
        cleaningVisitor.head(script, 0);

        assertEquals("<body></body>", cleaningVisitor.destination.outerHtml());
    }

    @Test
    public void testCleaningVisitor_headTextNode() {
        Document dirtyDocument = Document.createShell("");
        Element body = dirtyDocument.body();

        Node text = new TextNode("Hello, World!", "");
        body.appendChild(text);

        Cleaner.CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(dirtyDocument.body(), new Element(Tag.valueOf("body"), ""));
        cleaningVisitor.head(text, 0);

        assertEquals("<body>Hello, World!</body>", cleaningVisitor.destination.outerHtml());
    }

    @Test
    public void testCleaningVisitor_tailSafeTag() {
        Document dirtyDocument = Document.createShell("");
        Element body = dirtyDocument.body();

        Element p = new Element(Tag.valueOf("p"), "");
        p.text("Hello, World!");
        body.appendChild(p);

        Cleaner.CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(dirtyDocument.body(), new Element(Tag.valueOf("body"), ""));
        cleaningVisitor.tail(p, 0);

        assertEquals("<body></body>", cleaningVisitor.destination.outerHtml());
    }

    @Test
    public void testCleaningVisitor_tailUnsafeTag() {
        Document dirtyDocument = Document.createShell("");
        Element body = dirtyDocument.body();

        Element script = new Element(Tag.valueOf("script"), "");
        script.attr("src", "http://example.com/js/malicious.js");
        body.appendChild(script);

        Cleaner.CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(dirtyDocument.body(), new Element(Tag.valueOf("body"), ""));
        cleaningVisitor.tail(script, 0);

        assertEquals("<body></body>", cleaningVisitor.destination.outerHtml());
    }

    @Test
    public void testCleaningVisitor_tailTextNode() {
        Document dirtyDocument = Document.createShell("");
        Element body = dirtyDocument.body();

        Node text = new TextNode("Hello, World!", "");
        body.appendChild(text);

        Cleaner.CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(dirtyDocument.body(), new Element(Tag.valueOf("body"), ""));
        cleaningVisitor.tail(text, 0);

        assertEquals("<body></body>", cleaningVisitor.destination.outerHtml());
    }

    @Test
    public void testCopySafeNodes() {
        Document dirtyDocument = Document.createShell("");
        Element body = dirtyDocument.body();

        Element p = new Element(Tag.valueOf("p"), "");
        p.text("Hello, World!");
        body.appendChild(p);

        Element dest = new Element(Tag.valueOf("div"), "");
        int numDiscarded = cleaner.copySafeNodes(body, dest);

        assertEquals("<div><p>Hello, World!</p></div>", dest.outerHtml());
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCopySafeNodes_withUnsafeTag() {
        Document dirtyDocument = Document.createShell("");
        Element body = dirtyDocument.body();

        Element script = new Element(Tag.valueOf("script"), "");
        script.text("alert('XSS attack!');");
        body.appendChild(script);

        Element dest = new Element(Tag.valueOf("div"), "");
        int numDiscarded = cleaner.copySafeNodes(body, dest);

        assertEquals("<div></div>", dest.outerHtml());
        assertEquals(1, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element(Tag.valueOf("p"), "");
        sourceEl.attr("class", "text");
        sourceEl.attr("id", "message");
        sourceEl.attr("style", "font-size: 12px;");
        sourceEl.html("Hello, World!");

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

        assertEquals("<p class=\"text\" id=\"message\">Hello, World!</p>", elementMeta.el.outerHtml());
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElement_withUnsafeAttribute() {
        Element sourceEl = new Element(Tag.valueOf("p"), "");
        sourceEl.attr("class", "text");
        sourceEl.attr("id", "message");
        sourceEl.attr("onclick", "alert('XSS attack!');");
        sourceEl.html("Hello, World!");

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

        assertEquals("<p class=\"text\" id=\"message\">Hello, World!</p>", elementMeta.el.outerHtml());
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }

    @Test
    public void testElementMeta() {
        Element element = new Element(Tag.valueOf("p"), "");
        int numAttribsDiscarded = 1;

        Cleaner.ElementMeta elementMeta = new Cleaner.ElementMeta(element, numAttribsDiscarded);

        assertEquals(element, elementMeta.el);
        assertEquals(numAttribsDiscarded, elementMeta.numAttribsDiscarded);
    }
}