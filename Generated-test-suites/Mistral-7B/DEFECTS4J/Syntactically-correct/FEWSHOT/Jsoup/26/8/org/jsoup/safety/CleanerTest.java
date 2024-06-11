package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

import java.util.Arrays;
import java.util.List;

public class CleanerTest {

    @Test
    public void testCreateCleaner() {
        Whitelist whitelist = mock(Whitelist.class);
        Cleaner cleaner = new Cleaner(whitelist);
        assertTrue(cleaner != null);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateCleanerWithNullWhitelist() {
        Cleaner cleaner = new Cleaner(null);
    }

    @Test
    public void testClean() {
        Whitelist whitelist = mock(Whitelist.class);
        Document dirtyDocument = mock(Document.class);
        Document cleanDocument = mock(Document.class);

        when(whitelist.isSafeTag("div")).thenReturn(true);
        when(whitelist.isSafeTag("script")).thenReturn(false);

        when(dirtyDocument.body()).thenReturn(new Element("body"));
        when(dirtyDocument.baseUri()).thenReturn("http://example.com");

        when(Document.createShell(dirtyDocument.baseUri())).thenReturn(cleanDocument);

        Cleaner cleaner = new Cleaner(whitelist);
        cleaner.clean(dirtyDocument);

        Element body = cleanDocument.body();
        assertTrue(body != null);
        assertTrue(body.tagName().equals("body"));
        assertTrue(body.childNodes().size() > 0);
        assertTrue(body.childNodes().get(0) instanceof TextNode);

        TextNode textNode = (TextNode) body.childNodes().get(0);
        assertTrue(textNode.getWholeText().length() > 0);

        List<Node> dirtyChildren = Arrays.asList(new Element("div"), new Element("script"), new TextNode("Test text"));
        when(dirtyDocument.body().childNodes()).thenReturn(dirtyChildren.iterator());

        cleaner.clean(dirtyDocument);

        List<Node> cleanChildren = Arrays.asList(new TextNode("Test text"));
        when(cleanDocument.body().childNodes()).thenReturn(cleanChildren.iterator());

        assertEquals(2, dirtyChildren.size());
        assertEquals(1, cleanChildren.size());
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = mock(Whitelist.class);
        Document dirtyDocument = mock(Document.class);

        when(whitelist.isSafeTag("div")).thenReturn(true);
        when(whitelist.isSafeTag("script")).thenReturn(false);

        when(dirtyDocument.body()).thenReturn(new Element("body"));
        when(dirtyDocument.baseUri()).thenReturn("http://example.com");

        Cleaner cleaner = new Cleaner(whitelist);
        assertTrue(cleaner.isValid(dirtyDocument));

        Element unsafeElement = new Element("script");
        Element parentElement = new Element("body");
        parentElement.appendChild(unsafeElement);
        dirtyDocument = new Document(parentElement);

        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = mock(Whitelist.class);
        Document dirtyDocument = mock(Document.class);
        Element dest = mock(Element.class);

        when(whitelist.isSafeTag("div")).thenReturn(true);
        when(whitelist.isSafeTag("script")).thenReturn(false);

        when(dirtyDocument.body()).thenReturn(new Element("body"));
        when(dirtyDocument.baseUri()).thenReturn("http://example.com");

        Cleaner cleaner = new Cleaner(whitelist);

        Element source = new Element("div");
        ElementMeta meta = cleaner.createSafeElement(source);
        Element destChild = meta.el;
        dest.appendChild(destChild);

        List<Node> sourceChildren = Arrays.asList(new Element("div"), new Element("script"), new TextNode("Test text"));
        when(dirtyDocument.body().childNodes()).thenReturn(sourceChildren.iterator());

        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), dest);

        assertEquals(2, numDiscarded);
        assertEquals(1, dest.childNodes().size());
        assertEquals(1, dest.childNodes().get(0).childNodes().size());
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = mock(Whitelist.class);
        Element sourceEl = new Element("div");
        sourceEl.attributes().put("data-foo", "bar");

        when(whitelist.isSafeTag("div")).thenReturn(true);
        when(whitelist.isSafeAttribute("div", sourceEl, new Attribute("data-foo", "bar"))).thenReturn(true);
        when(whitelist.getEnforcedAttributes("div")).thenReturn(new Attributes(){{put("class", "cleaner-class");}});

        Cleaner cleaner = new Cleaner(whitelist);
        ElementMeta meta = cleaner.createSafeElement(sourceEl);

        assertEquals("div", meta.el.tagName());
        assertEquals("cleaner-class", meta.el.classAttrs().get("class"));
        assertEquals(1, meta.numAttribsDiscarded);

        assertTrue(sourceEl.attributes().hasKey("data-foo"));
        assertTrue(meta.el.attributes().hasKey("data-foo"));
    }
}