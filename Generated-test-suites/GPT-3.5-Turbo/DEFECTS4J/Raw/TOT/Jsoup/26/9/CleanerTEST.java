import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {

    private Whitelist whitelist;
    private Cleaner cleaner;

    @Before
    public void setUp() {
        whitelist = new Whitelist();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean() {
        Document dirtyDocument = new Document("");
        Element body = new Element(Tag.valueOf("body"), "");
        dirtyDocument.appendChild(body);

        Element div1 = new Element(Tag.valueOf("div"), "");
        body.appendChild(div1);

        Element p = new Element(Tag.valueOf("p"), "");
        p.attr("class", "content");
        p.attr("data-id", "12345");
        div1.appendChild(p);

        Element div2 = new Element(Tag.valueOf("div"), "");
        body.appendChild(div2);

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("body", cleanDocument.body().tagName());
        assertEquals(2, cleanDocument.body().children().size());

        Element cleanDiv1 = cleanDocument.body().child(0);
        assertEquals("div", cleanDiv1.tagName());

        Element cleanP = cleanDiv1.child(0);
        assertEquals("p", cleanP.tagName());
        assertTrue(cleanP.hasAttr("class"));
        assertEquals("content", cleanP.attr("class"));
        assertTrue(cleanP.hasAttr("data-id"));
        assertEquals("12345", cleanP.attr("data-id"));

        Element cleanDiv2 = cleanDocument.body().child(1);
        assertEquals("div", cleanDiv2.tagName());
    }

    @Test
    public void testIsValid_ValidDocument() {
        Document dirtyDocument = new Document("");
        Element body = new Element(Tag.valueOf("body"), "");
        dirtyDocument.appendChild(body);

        Element div = new Element(Tag.valueOf("div"), "");
        body.appendChild(div);

        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValid_InvalidDocument() {
        Document dirtyDocument = new Document("");
        Element body = new Element(Tag.valueOf("body"), "");
        dirtyDocument.appendChild(body);

        Element script = new Element(Tag.valueOf("script"), "");
        body.appendChild(script);

        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodes_EmptySource() {
        Document dirtyDocument = new Document("");
        Element body = new Element(Tag.valueOf("body"), "");
        dirtyDocument.appendChild(body);

        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
        Element cleanBody = cleanDocument.body();

        int numDiscarded = cleaner.copySafeNodes(body, cleanBody);

        assertEquals(0, numDiscarded);
        assertTrue(cleanBody.childNodes().isEmpty());
    }

    @Test
    public void testCopySafeNodes_SafeElement() {
        Document dirtyDocument = new Document("");
        Element body = new Element(Tag.valueOf("body"), "");
        dirtyDocument.appendChild(body);

        Element p = new Element(Tag.valueOf("p"), "");
        body.appendChild(p);

        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
        Element cleanBody = cleanDocument.body();

        int numDiscarded = cleaner.copySafeNodes(body, cleanBody);

        assertEquals(0, numDiscarded);
        assertEquals(1, cleanBody.childNodes().size());

        Element cleanP = (Element) cleanBody.childNode(0);
        assertEquals("p", cleanP.tagName());
        assertEquals(body.baseUri(), cleanP.baseUri());
    }

    @Test
    public void testCopySafeNodes_UnsafeElement() {
        Document dirtyDocument = new Document("");
        Element body = new Element(Tag.valueOf("body"), "");
        dirtyDocument.appendChild(body);

        Element script = new Element(Tag.valueOf("script"), "");
        body.appendChild(script);

        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
        Element cleanBody = cleanDocument.body();

        int numDiscarded = cleaner.copySafeNodes(body, cleanBody);

        assertEquals(1, numDiscarded);
        assertTrue(cleanBody.childNodes().isEmpty());
    }

    @Test
    public void testCopySafeNodes_TextNode() {
        Document dirtyDocument = new Document("");
        Element body = new Element(Tag.valueOf("body"), "");
        dirtyDocument.appendChild(body);

        body.appendChild("This is some content");

        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
        Element cleanBody = cleanDocument.body();

        int numDiscarded = cleaner.copySafeNodes(body, cleanBody);

        assertEquals(0, numDiscarded);
        assertEquals(1, cleanBody.childNodes().size());

        assertEquals("This is some content", cleanBody.text());
    }

    @Test
    public void testCreateSafeElement_SafeTag() {
        Element sourceEl = new Element(Tag.valueOf("p"), "");
        sourceEl.attr("class", "content");
        sourceEl.attr("data-id", "12345");

        Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceEl);

        Element cleanEl = meta.el;
        assertEquals("p", cleanEl.tagName());
        assertEquals(sourceEl.baseUri(), cleanEl.baseUri());
        assertTrue(cleanEl.hasAttr("class"));
        assertEquals("content", cleanEl.attr("class"));
        assertTrue(cleanEl.hasAttr("data-id"));
        assertEquals("12345", cleanEl.attr("data-id"));

        assertEquals(0, meta.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElement_UnsafeTag() {
        Element sourceEl = new Element(Tag.valueOf("script"), "");

        Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceEl);

        Element cleanEl = meta.el;
        assertEquals("script", cleanEl.tagName());
        assertEquals(sourceEl.baseUri(), cleanEl.baseUri());
        assertFalse(cleanEl.hasAttributes());

        assertEquals(0, meta.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElement_WithDiscardedAttributes() {
        Element sourceEl = new Element(Tag.valueOf("p"), "");
        sourceEl.attr("onclick", "alert('XSS')");

        Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceEl);

        Element cleanEl = meta.el;
        assertEquals("p", cleanEl.tagName());
        assertEquals(sourceEl.baseUri(), cleanEl.baseUri());
        assertFalse(cleanEl.hasAttributes());

        assertEquals(1, meta.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElement_WithEnforcedAttributes() {
        Element sourceEl = new Element(Tag.valueOf("a"), "");
        sourceEl.attr("href", "example.com");

        Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceEl);

        Element cleanEl = meta.el;
        assertEquals("a", cleanEl.tagName());
        assertEquals(sourceEl.baseUri(), cleanEl.baseUri());
        assertTrue(cleanEl.hasAttr("href"));
        assertEquals("example.com", cleanEl.attr("href"));

        assertEquals(0, meta.numAttribsDiscarded);
    }
}