package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.LazyMap;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testCreateCleaner() {
        Whitelist whitelist = mockWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    @Test
    public void testCleanDocument() {
        Whitelist whitelist = mockWhitelist();
        Document dirtyDocument = createDocument();
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertEquals(dirtyDocument.body(), cleanDocument.body());
    }

    @Test
    public void testCleanInvalidDocument() {
        Whitelist whitelist = mockWhitelist();
        Document dirtyDocument = createInvalidDocument();
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertFalse(dirtyDocument.body().equals(cleanDocument.body()));
    }

    @Test
    public void testIsValidDocument() {
        Whitelist whitelist = mockWhitelist();
        Document dirtyDocument = createValidDocument();
        Cleaner cleaner = new Cleaner(whitelist);

        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsInvalidDocument() {
        Whitelist whitelist = mockWhitelist();
        Document dirtyDocument = createInvalidDocument();
        Cleaner cleaner = new Cleaner(whitelist);

        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = mockWhitelist();
        Element root = createRootElement();
        Element destination = new Element(Tag.valueOf("html"), null, new Attributes());
        int numDiscarded = copySafeNodes(root, destination, whitelist);
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCopySafeNodesWithInvalidElements() {
        Whitelist whitelist = mockWhitelist();
        Element root = createRootElementWithInvalidElements();
        Element destination = new Element(Tag.valueOf("html"), null, new Attributes());
        int numDiscarded = copySafeNodes(root, destination, whitelist);
        assertEquals(4, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = mockWhitelist();
        Element sourceElement = createSourceElement();
        Element destElement = createSafeElement(sourceElement, whitelist);
        assertNotNull(destElement);
        assertEquals(sourceElement.tagName(), destElement.tagName());
    }

    @Test
    public void testCreateSafeElementWithInvalidAttribute() {
        Whitelist whitelist = mockWhitelist();
        Element sourceElement = createSourceElementWithInvalidAttribute();
        Element destElement = createSafeElement(sourceElement, whitelist);
        assertNull(destElement);
    }

    @Test
    public void testCreateSafeElementWithValidAttributes() {
        Whitelist whitelist = mockWhitelist();
        Element sourceElement = createSourceElementWithValidAttributes();
        Element destElement = createSafeElement(sourceElement, whitelist);
        assertNotNull(destElement);
        assertEquals(sourceElement.tagName(), destElement.tagName());
        assertEquals(sourceElement.attributes().size(), destElement.attributes().size());
    }

    private Document createDocument() {
        return Document.createShell("http://example.com");
    }

    private Document createInvalidDocument() {
        Document document = Document.createShell("http://example.com");
        Element body = new Element(Tag.valueOf("body"), null, new Attributes());
        body.appendChild(new Element(Tag.valueOf("script"), null, new Attributes().put("src", "evil.js")));
        document.setBody(body);
        return document;
    }

    private Document createValidDocument() {
        Document document = Document.createShell("http://example.com");
        Element body = new Element(Tag.valueOf("body"), null, new Attributes());
        body.appendChild(new Element(Tag.valueOf("p"), null, new Attributes().put("class", "text")));
        body.appendChild(new TextNode("This is a valid document."));
        document.setBody(body);
        return document;
    }

    private Element createRootElement() {
        return new Element(Tag.valueOf("html"), null, new Attributes());
    }

    private Element createRootElementWithInvalidElements() {
        Element root = new Element(Tag.valueOf("html"), null, new Attributes());

        root.appendChild(new Element(Tag.valueOf("script"), null, new Attributes().put("src", "evil.js")));
        root.appendChild(new Element(Tag.valueOf("style"), null, new Attributes().put("type", "text/css").put("onload", "alert('XSS')")));
        root.appendChild(new Element(Tag.valueOf("marquee"), null, new Attributes().put("behavior", "scroll")));
        root.appendChild(new Element(Tag.valueOf("applet"), null, new Attributes().put("codebase", "http://evil.com")));

        return root;
    }

    private Element createSourceElement() {
        return new Element(Tag.valueOf("p"), null, new Attributes().put("class", "text"));
    }

    private Element createSourceElementWithInvalidAttribute() {
        return new Element(Tag.valueOf("p"), null, new Attributes().put("class", "text").put("onclick", "alert('XSS')"));
    }

    private Element createSourceElementWithValidAttributes() {
        return new Element(Tag.valueOf("p"), null, new Attributes().put("class", "text").put("style", "color: blue"));
    }

    private int copySafeNodes(Element root, Element destination, Whitelist whitelist) {
        List<Node> nodes = root.childNodes();
        int numDiscarded = 0;

        for (Node node : nodes) {
            if (node instanceof Element) {
                Element sourceElement = (Element) node;
                Element destChild = createSafeElement((Element) node, whitelist).el;
                destination.appendChild(destChild);

                numDiscarded += copySafeNodes(sourceElement, destChild, whitelist).intValue();
            } else if (node instanceof TextNode) {
                TextNode sourceText = (TextNode) node;
                TextNode destText = new TextNode(sourceText.getWholeText(), sourceText.baseUri());
                destination.appendChild(destText);
            }
        }
        return numDiscarded;
    }

    private Whitelist mockWhitelist() {
        Map<String, List<String>> tags = new LazyMap<String, List<String>>() {
            @Override
            protected List<String> computeIfAbsent(String key) {
                return new ArrayList<>();
            }
        };
        tags.put("p", Arrays.asList("class"));

        Whitelist whitelist = mock(Whitelist.class);
        when(whitelist.isSafeTag(anyString())).thenAnswer(i -> tags.containsKey(i.getString()));
        when(whitelist.isSafeAttribute(anyString(), any(Element.class), any(Attribute.class))).thenReturn(true);
        when(whitelist.getEnforcedAttributes(anyString())).thenReturn(new Attributes());
        return whitelist;
    }
}