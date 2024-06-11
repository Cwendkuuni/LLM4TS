package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

public class CleanerTest {

    @Test
    public void testCreateCleaner() {
        Whitelist whitelist = new DefaultWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        assertEquals("Cleaner created with valid whitelist", whitelist, cleaner.whitelist);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateCleanerNullWhitelist() {
        Cleaner cleaner = new Cleaner(null);
    }

    @Test
    public void testCleanDocument() {
        Whitelist whitelist = new DefaultWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document originalDocument = createTestDocument();
        Document cleanDocument = cleaner.clean(originalDocument);

        assertNotNull("Clean document should not be null", cleanDocument);
        assertEquals("Clean document should have same uri as original", originalDocument.baseUri(), cleanDocument.baseUri());
        assertEquals("Clean document should have same number of elements as original", originalDocument.numChildren(), cleanDocument.numChildren());

        NodeTraversor originalTraversor = new NodeTraversor(new IdentityVisitor());
        NodeTraversor cleanTraversor = new NodeTraversor(new IdentityVisitor());
        originalTraversor.traverse(originalDocument);
        cleanTraversor.traverse(cleanDocument);

        // Check that all nodes in the original document are present in the clean document, but are cleaned (if necessary)
        for (Node originalNode : originalTraversor.getTraversedNodes()) {
            Node cleanNode = cleanTraversor.findNode(originalNode);
            assertEquals("Clean node should be instance of same class as original", originalNode.getClass(), cleanNode.getClass());
            // Check that text nodes have same text in both documents
            if (originalNode instanceof TextNode && cleanNode instanceof TextNode) {
                TextNode originalText = (TextNode) originalNode;
                TextNode cleanText = (TextNode) cleanNode;
                assertEquals("Text node text should be same in both documents", originalText.getWholeText(), cleanText.getWholeText());
            }
            // Check that elements have same tag name and attributes in both documents
            if (originalNode instanceof Element && cleanNode instanceof Element) {
                Element originalElement = (Element) originalNode;
                Element cleanElement = (Element) cleanNode;
                assertEquals("Element tag name should be same in both documents", originalElement.tagName(), cleanElement.tagName());
                Attributes originalAttributes = originalElement.attributes();
                Attributes cleanAttributes = cleanElement.attributes();
                for (Attribute originalAttribute : originalAttributes) {
                    Attribute cleanAttribute = cleanElement.attribute(originalAttribute.getKey());
                    assertEquals("Attribute key should be same in both documents", originalAttribute.getKey(), cleanAttribute.getKey());
                    assertEquals("Attribute value should be same in both documents", originalAttribute.getValue(), cleanAttribute.getValue());
                }
            }
        }
    }

    @Test
    public void testIsValidDocument() {
        Whitelist whitelist = new DefaultWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document originalDocument = createTestDocument();
        boolean isValid = cleaner.isValid(originalDocument);
        assertFalse("Invalid document should not be considered valid", isValid);

        Document cleanDocument = cleaner.clean(originalDocument);
        isValid = cleaner.isValid(cleanDocument);
        assertTrue("Valid document should be considered valid", isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = new DefaultWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document originalDocument = createTestDocument();
        Element dest = Document.createShell(originalDocument.baseUri()).body();
        int numDiscarded = cleaner.copySafeNodes(originalDocument.body(), dest);
        assertEquals("Number of discarded nodes should be greater than zero", numDiscarded, originalDocument.numChildren());

        Document cleanDocument = cleaner.clean(originalDocument);
        numDiscarded = cleaner.copySafeNodes(cleanDocument.body(), dest);
        assertEquals("Number of discarded nodes should be zero", 0, numDiscarded);
    }

    private Document createTestDocument() {
        Parser parser = new Parser();
        ParseErrorList errors = new ParseErrorList();
        String html = "<html><head><title>Test Document</title></head><body><h1 id='title'>Test Title<h1><script>alert('XSS');</script><p>Test Paragraph</p></body></html>";
        Document document = null;
        try {
            document = parser.parseBodyFragment(html, "", errors);
        } catch (Parser.MalformedAttributeException e) {
            // Ignore
        } catch (Parser.MalformedElementNameException e) {
            // Ignore
        }
        return document;
    }

    private static class IdentityVisitor implements NodeVisitor {
        public void visit(Node node) {
            // do nothing
        }

        public void head(Node node, int depth) {
            // do nothing
        }

        public void tail(Node node, int depth) {
            // do nothing
        }
    }
}