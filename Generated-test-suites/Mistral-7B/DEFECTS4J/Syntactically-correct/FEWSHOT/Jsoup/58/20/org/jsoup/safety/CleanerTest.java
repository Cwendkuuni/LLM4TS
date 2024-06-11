package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

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

public class CleanerTest {

    @Test
    public void testCreateCleaner() {
        Whitelist sampleWhitelist = Whitelist.simpleTextOnlyWhitelist();
        Cleaner cleaner = new Cleaner(sampleWhitelist);
        Validate.notNull(cleaner);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateCleanerWithNullWhitelist() {
        Cleaner cleaner = new Cleaner(null);
    }

    @Test
    public void testCleanDocument() {
        Whitelist sampleWhitelist = Whitelist.simpleTextOnlyWhitelist();
        Cleaner cleaner = new Cleaner(sampleWhitelist);
        Document dirtyDocument = Parser.parseBodyFragment("<html><body><script>alert('XSS');</script></body></html>", null);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Check that the original document is not modified
        assertEquals(0, dirtyDocument.body().childNodeSize());

        // Check that the clean document only contains nodes allowed by the whitelist
        int cleanDocumentBodyChildNodeSize = cleanDocument.body().childNodeSize();
        for (int i = 0; i < cleanDocumentBodyChildNodeSize; i++) {
            Node node = cleanDocument.body().childNode(i);
            if (node instanceof Element) {
                String nodeTagName = node.nodeName();
                assertTrue(sampleWhitelist.isSafeTag(nodeTagName));
            }
        }
    }

    @Test
    public void testIsValidDocument() {
        Whitelist sampleWhitelist = Whitelist.simpleTextOnlyWhitelist();
        Cleaner cleaner = new Cleaner(sampleWhitelist);

        Document validDocument = Parser.parseBodyFragment("<html><body><p>This is a valid document.</p></body></html>");
        assertTrue(cleaner.isValid(validDocument));

        Document invalidDocument = Parser.parseBodyFragment("<html><body><p>This is a valid document.</p><script>alert('XSS');</script></body></html>");
        assertFalse(cleaner.isValid(invalidDocument));
    }

    @Test
    public void testCleaningVisitor() {
        Whitelist sampleWhitelist = Whitelist.simpleTextOnlyWhitelist();
        Cleaner cleaner = new Cleaner(sampleWhitelist);

        Document dirtyDocument = Parser.parseBodyFragment("<html><body><p id='p1'>This is a <script>dirty</script> document.</p></body></html>");
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
        int numDiscardedNodes = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        // Check that only nodes allowed by the whitelist are copied
        int cleanDocumentBodyChildNodeSize = cleanDocument.body().childNodeSize();
        for (int i = 0; i < cleanDocumentBodyChildNodeSize; i++) {
            Node node = cleanDocument.body().childNode(i);
            if (node instanceof Element) {
                String nodeTagName = node.nodeName();
                assertTrue(sampleWhitelist.isSafeTag(nodeTagName));
            }
        }
        assertEquals(1, numDiscardedNodes);

        // Check that script nodes are discarded
        assertEquals(1, dirtyDocument.body().childNode(0).childNodeSize());
        assertEquals(0, cleanDocument.body().childNode(0).childNodeSize());

        // Check that text nodes are copied
        TextNode dirtyTextNode = (TextNode) dirtyDocument.body().childNode(0).childNode(0);
        TextNode cleanTextNode = (TextNode) cleanDocument.body().childNode(0).childNode(0);
        assertSame(dirtyTextNode.text(), cleanTextNode.text());
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist sampleWhitelist = Whitelist.simpleTextOnlyWhitelist();
        Cleaner cleaner = new Cleaner(sampleWhitelist);

        Element dirtyElement = new Element("div", null);
        dirtyElement.attr("id", "div1");
        dirtyElement.attr("class", "dirtyClass");

        Element cleanElement = cleaner.createSafeElement(dirtyElement);
        assertEquals("div", cleanElement.tagName());

        Attributes cleanElementAttributes = cleanElement.attributes();
        Attribute cleanElementIdAttribute = cleanElementAttributes.get("id");
        assertEquals("div1", cleanElementIdAttribute.getValue());

        assertTrue(cleanElementAttributes.valueExists("class"));
        assertNull(cleanElementAttributes.get("class").getValue());

        Attributes enforcedAttributes = sampleWhitelist.getEnforcedAttributes("div");
        cleanElementAttributes.addAll(enforcedAttributes);

        assertEquals(2, cleanElementAttributes.size());
    }
}