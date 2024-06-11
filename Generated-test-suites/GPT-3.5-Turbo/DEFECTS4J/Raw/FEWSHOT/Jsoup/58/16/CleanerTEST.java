package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CleanerTest {
    private static final String BASE_URI = "http://example.com";

    private Cleaner cleaner;

    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.relaxed());
    }

    @Test
    public void testClean() {
        Document dirtyDoc = mock(Document.class);
        Element dirtyBody = mock(Element.class);
        Element cleanBody = mock(Element.class);
        when(dirtyDoc.body()).thenReturn(dirtyBody);
        when(cleanBody.baseUri()).thenReturn(BASE_URI);

        Document cleanDoc = cleaner.clean(dirtyDoc);

        assertEquals(BASE_URI, cleanDoc.baseUri());
        assertEquals(cleanBody, cleanDoc.body());
    }

    @Test
    public void testIsValid() {
        Document dirtyDoc = mock(Document.class);
        Element dirtyBody = mock(Element.class);
        Element cleanBody = mock(Element.class);
        when(dirtyDoc.body()).thenReturn(dirtyBody);
        when(cleanBody.baseUri()).thenReturn(BASE_URI);

        boolean isValid = cleaner.isValid(dirtyDoc);

        assertEquals(true, isValid);
    }

    @Test
    public void testCleaningVisitorWithSafeTag() {
        Element root = mock(Element.class);
        Element destination = mock(Element.class);
        Element sourceEl = mock(Element.class);
        when(sourceEl.tagName()).thenReturn("a");
        when(sourceEl.baseUri()).thenReturn(BASE_URI);
        when(destination.parent()).thenReturn(destination);

        cleaner.new CleaningVisitor(root, destination).head(sourceEl, 0);

        assertEquals(destination, destination);
    }

    @Test
    public void testCleaningVisitorWithUnsafeTag() {
        Element root = mock(Element.class);
        Element destination = mock(Element.class);
        Element sourceEl = mock(Element.class);
        when(sourceEl.tagName()).thenReturn("script");
        when(destination.parent()).thenReturn(destination);

        cleaner.new CleaningVisitor(root, destination).head(sourceEl, 0);

        assertNull(destination);
    }

    @Test
    public void testCleaningVisitorWithTextNode() {
        Element root = mock(Element.class);
        Element destination = mock(Element.class);
        TextNode sourceText = mock(TextNode.class);
        when(sourceText.getWholeText()).thenReturn("Hello");
        when(sourceText.baseUri()).thenReturn(BASE_URI);

        cleaner.new CleaningVisitor(root, destination).head(sourceText, 0);

        assertEquals(destination, destination);
    }

    @Test
    public void testCleaningVisitorWithDataNode() {
        Element root = mock(Element.class);
        Element destination = mock(Element.class);
        Element sourceParent = mock(Element.class);
        when(sourceParent.nodeName()).thenReturn("a");
        DataNode sourceData = mock(DataNode.class);
        when(sourceData.getWholeData()).thenReturn("data");
        when(sourceData.baseUri()).thenReturn(BASE_URI);
        when(sourceData.parent()).thenReturn(sourceParent);

        cleaner.new CleaningVisitor(root, destination).head(sourceData, 0);

        assertNull(destination);
    }

    @Test
    public void testCleaningVisitorWithOtherNode() {
        Element root = mock(Element.class);
        Element destination = mock(Element.class);
        Element sourceEl = mock(Element.class);
        when(destination.parent()).thenReturn(destination);

        cleaner.new CleaningVisitor(root, destination).head(sourceEl, 0);

        assertNull(destination);
    }

    @Test
    public void testCleaningVisitorWithSafeTagTail() {
        Element root = mock(Element.class);
        Element destination = mock(Element.class);
        Element sourceEl = mock(Element.class);
        when(sourceEl.nodeName()).thenReturn("a");
        when(destination.parent()).thenReturn(destination);

        cleaner.new CleaningVisitor(root, destination).tail(sourceEl, 0);

        assertEquals(destination.parent(), destination);
    }

    @Test
    public void testCleaningVisitorWithUnsafeTagTail() {
        Element root = mock(Element.class);
        Element destination = mock(Element.class);
        Element sourceEl = mock(Element.class);
        when(sourceEl.nodeName()).thenReturn("script");

        cleaner.new CleaningVisitor(root, destination).tail(sourceEl, 0);

        assertNull(destination.parent());
    }

    @Test
    public void testCopySafeNodes() {
        Element source = mock(Element.class);
        Element dest = mock(Element.class);
        Cleaner.CleaningVisitor cleaningVisitor = mock(Cleaner.CleaningVisitor.class);
        NodeTraversor traversor = mock(NodeTraversor.class);
        when(cleaningVisitor.numDiscarded).thenReturn(0);
        when(traversor.traverse(source)).thenReturn(traversor);

        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElementWithSafeAttribute() {
        Element sourceEl = mock(Element.class);
        Attributes sourceAttrs = mock(Attributes.class);
        Attribute sourceAttr = mock(Attribute.class);
        when(sourceEl.tagName()).thenReturn("a");
        when(sourceEl.baseUri()).thenReturn(BASE_URI);
        when(sourceEl.attributes()).thenReturn(sourceAttrs);
        when(sourceAttrs.iterator()).thenReturn(new Attributes().iterator());
        when(sourceAttrs.get("href")).thenReturn(sourceAttr);
        when(Whitelist.relaxed().isSafeAttribute("a", sourceEl, sourceAttr)).thenReturn(true);
        Attributes destAttrs = mock(Attributes.class);
        Attributes enforcedAttrs = mock(Attributes.class);
        when(sourceEl.attributes()).thenReturn(sourceAttrs);
        when(enforcedAttrs.iterator()).thenReturn(new Attributes().iterator());
        when(Whitelist.relaxed().getEnforcedAttributes("a")).thenReturn(enforcedAttrs);
        when(destAttrs.addAll(enforcedAttrs)).thenReturn(destAttrs);

        cleaner.createSafeElement(sourceEl);

        assertEquals(destAttrs, destAttrs);
    }

    @Test
    public void testCreateSafeElementWithUnsafeAttribute() {
        Element sourceEl = mock(Element.class);
        Attributes sourceAttrs = mock(Attributes.class);
        Attribute sourceAttr = mock(Attribute.class);
        when(sourceEl.tagName()).thenReturn("a");
        when(sourceEl.baseUri()).thenReturn(BASE_URI);
        when(sourceEl.attributes()).thenReturn(sourceAttrs);
        when(sourceAttrs.iterator()).thenReturn(new Attributes().iterator());
        when(sourceAttrs.get("href")).thenReturn(sourceAttr);
        when(Whitelist.relaxed().isSafeAttribute("a", sourceEl, sourceAttr)).thenReturn(false);
        Attributes destAttrs = mock(Attributes.class);
        Attributes enforcedAttrs = mock(Attributes.class);
        when(sourceEl.attributes()).thenReturn(sourceAttrs);
        when(enforcedAttrs.iterator()).thenReturn(new Attributes().iterator());
        when(Whitelist.relaxed().getEnforcedAttributes("a")).thenReturn(enforcedAttrs);
        when(destAttrs.addAll(enforcedAttrs)).thenReturn(destAttrs);

        cleaner.createSafeElement(sourceEl);

        assertEquals(destAttrs, destAttrs);
    }
}