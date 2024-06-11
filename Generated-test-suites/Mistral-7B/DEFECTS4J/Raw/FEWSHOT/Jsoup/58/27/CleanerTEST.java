package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

public class CleanerTest {

    private Whitelist whitelist;
    private Cleaner cleaner;
    private Document dirtyDocument;
    private Document cleanDocument;

    @Before
    public void setUp() {
        whitelist = mock(Whitelist.class);
        cleaner = new Cleaner(whitelist);

        dirtyDocument = Parser.parseBodyFragment("<html><body><div id='test'><p>Test</p></div></body></html>", null);
        cleanDocument = Document.createShell(dirtyDocument.baseUri());
    }

    @Test
    public void testCleanDocument() {
        cleaner.clean(dirtyDocument);
        NodeTraversor traversor = new NodeTraversor();
        traversor.traverse(dirtyDocument.body());
        NodeVisitor visitor = new NodeVisitor() {
            int count = 0;

            @Override
            public void head(Node node, int depth) {
                count++;
            }

            @Override
            public void tail(Node node, int depth) {
            }
        };
        traversor.traverse(cleanDocument.body(), visitor);
        assertEquals(1, count);
    }

    @Test
    public void testIsValidDocument() {
        when(whitelist.isSafeTag("div")).thenReturn(true);
        when(whitelist.isSafeTag("p")).thenReturn(true);

        assertFalse(cleaner.isValid(dirtyDocument));
        assertEquals(1, cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body()).intValue());

        assertFalse(cleaner.isValid(dirtyDocument));

        when(whitelist.isSafeTag("div")).thenReturn(false);

        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCleaningVisitor() {
        when(whitelist.isSafeTag("div")).thenReturn(true);
        when(whitelist.isSafeTag("p")).thenReturn(true);
        when(whitelist.isSafeTag("textNode")).thenReturn(true);
        when(whitelist.isSafeTag("dataNode")).thenReturn(true);

        Element sourceElement = new Element("div", null, new Attributes());
        sourceElement.appendText("Test");
        sourceElement.appendChild(new TextNode("Test"));

        Element destinationElement = new Element("div", null, new Attributes());

        CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(sourceElement, destinationElement);

        Node node = mock(Node.class);
        when(node.nodeName()).thenReturn("body");
        when(node.nodeName()).thenReturn("div");
        when(node.nodeName()).thenReturn("p");
        when(node.nodeName()).thenReturn("textNode");
        when(node.nodeName()).thenReturn("dataNode");
        when(node.nodeName()).thenReturn("script");

        cleaningVisitor.head(node, 0);
        assertEquals(0, cleaningVisitor.numDiscarded);

        cleaningVisitor.head(sourceElement, 0);
        assertEquals(1, cleaningVisitor.numDiscarded);

        cleaningVisitor.tail(node, 0);

        cleaningVisitor.head(node, 1);
        assertEquals(1, cleaningVisitor.numDiscarded);

        cleaningVisitor.tail(sourceElement, 0);
        assertSame(destinationElement, cleaningVisitor.destination);
    }

    @Test
    public void testCreateSafeElement() {
        when(whitelist.isSafeAttribute("div", null, "class")).thenReturn(true);
        when(whitelist.isSafeAttribute("div", null, "id")).thenReturn(true);
        Attributes safeAttributes = new Attributes();
        safeAttributes.put("class", "test");
        safeAttributes.put("id", "test");

        Element sourceElement = new Element("div", null, new Attributes());
        sourceElement.attr("class", "test1");
        sourceElement.attr("id", "test2");

        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        assertEquals(sourceElement.tagName(), elementMeta.el.tagName());
        assertEquals(2, elementMeta.numAttribsDiscarded);
        assertEquals(safeAttributes.toString(), elementMeta.el.attributes().toString());
    }
}