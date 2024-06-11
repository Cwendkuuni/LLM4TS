package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

public class CleanerTest {

    @Test
    public void testCreateCleaner() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    @Test
    public void testCleanDocument() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Cleaner cleaner = new Cleaner(whitelist);

        String dirtyHtml = DataUtil.loadString(this.getClass(), "/cleanerTestData.html");
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
        assertEquals(dirtyDocument.body().text(), cleanDocument.body().text());
    }

    @Test
    public void testIsValidDocument() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Cleaner cleaner = new Cleaner(whitelist);

        String dirtyHtml = DataUtil.loadString(this.getClass(), "/cleanerTestData.html");
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        boolean isValid = cleaner.isValid(dirtyDocument);
        assertFalse(isValid);
    }

    @Test
    public void testIsValidValidDocument() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Cleaner cleaner = new Cleaner(whitelist);

        String validHtml = DataUtil.loadString(this.getClass(), "/cleanerTestDataValid.html");
        Document validDocument = Jsoup.parse(validHtml);

        boolean isValid = cleaner.isValid(validDocument);
        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodesWithSafeElement() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Cleaner cleaner = new Cleaner(whitelist);

        String dirtyHtml = DataUtil.loadString(this.getClass(), "/cleanerTestData.html");
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
        int numDiscarded = copySafeNodes(dirtyDocument.body(), cleanDocument.body(), cleaner);

        Element body = cleanDocument.body();
        assertEquals(6, body.childNodeSize()); // 6 elements in the test data
    }

    @Test
    public void testCopySafeNodesWithUnsafeElement() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);

        String dirtyHtml = DataUtil.loadString(this.getClass(), "/cleanerTestData.html");
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
        int numDiscarded = copySafeNodes(dirtyDocument.body(), cleanDocument.body(), cleaner);

        Element body = cleanDocument.body();
        assertEquals(0, body.childNodeSize());
    }

    @Test
    public void testCreateSafeElementWithSafeAttributes() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Cleaner cleaner = new Cleaner(whitelist);

        Element dirtyElement = new Element(Tag.P, null, new Attributes().put("class", "safeClass"));

        Element safeElement = createSafeElement(dirtyElement, cleaner);
        assertNotNull(safeElement);
        assertEquals("p", safeElement.tagName());
        assertEquals("safeClass", safeElement.classes().get(0));
    }

    @Test
    public void testCreateSafeElementWithUnsafeAttributes() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);

        Element dirtyElement = new Element(Tag.P, null, new Attributes().put("onclick", "alert('XSS')"));

        Element safeElement = createSafeElement(dirtyElement, cleaner);
        assertNull(safeElement);
    }

    private int copySafeNodes(Element root, Element destination, Cleaner cleaner) {
        List<Node> sourceChildren = root.childNodes();
        int numDiscarded = 0;

        for (Node source : sourceChildren) {
            if (source instanceof Element) {
                Element sourceEl = (Element) source;
                Element destChild = copySafeElement(sourceEl, cleaner);
                destination.appendChild(destChild);

                numDiscarded += destChild.numChildren();
                numDiscarded += copySafeNodes((Element) source, destChild, cleaner);
            } else if (source instanceof TextNode) {
                TextNode sourceText = (TextNode) source;
                TextNode destText = new TextNode(sourceText.text(), source.baseUri());
                destination.appendChild(destText);
            }
        }
        return numDiscarded;
    }

    private Element copySafeElement(Element sourceEl, Cleaner cleaner) {
        ElementMeta meta = createSafeElementMeta(sourceEl, cleaner);
        return meta.element;
    }

    private ElementMeta createSafeElementMeta(Element sourceEl, Cleaner cleaner) {
        String sourceTag = sourceEl.tagName();
        Attributes destAttrs = new Attributes();
        Element dest = new Element(Tag.valueOf(sourceTag), sourceEl.baseUri(), destAttrs);
        int numDiscarded = 0;

        Attributes sourceAttrs = sourceEl.attributes();
        for (Attribute sourceAttr : sourceAttrs) {
            if (cleaner.whitelist.isSafeAttribute(sourceTag, sourceEl, sourceAttr)) {
                destAttrs.put(sourceAttr);
            } else {
                numDiscarded++;
            }
        }
        Attributes enforcedAttrs = cleaner.whitelist.getEnforcedAttributes(sourceTag);
        destAttrs.addAll(enforcedAttrs);

        return new ElementMeta(dest, numDiscarded);
    }

    private static class ElementMeta {
        Element element;
        int numChildren;
        int numAttribsDiscarded;

        ElementMeta(Element element, int numChildren, int numAttribsDiscarded) {
            this.element = element;
            this.numChildren = numChildren;
            this.numAttribsDiscarded = numAttribsDiscarded;
        }
    }
}