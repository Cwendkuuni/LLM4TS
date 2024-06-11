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
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CleanerTest {

    @Test
    public void testClean() {
        // Create a new cleaner with a whitelist
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        // Create a dirty document with body content
        Document dirtyDocument = Document.createShell("http://example.com");
        Element body = dirtyDocument.body();
        Element div = body.appendElement("div");
        div.appendText("This is a dirty document");

        // Clean the dirty document using the cleaner
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Assert that the clean document only contains a body with authorized tags and attributes
        assertEquals("body", cleanDocument.body().tagName());
        assertEquals(1, cleanDocument.body().childNodes().size());
        assertEquals("div", cleanDocument.body().child(0).tagName());
        assertEquals("This is a dirty document", cleanDocument.body().child(0).text());
    }

    @Test
    public void testIsValid() {
        // Create a new cleaner with a whitelist
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        // Create a dirty document with body content
        Document dirtyDocument = Document.createShell("http://example.com");
        Element body = dirtyDocument.body();
        Element div = body.appendElement("div");
        div.appendText("This is a dirty document");

        // Check if the dirty document is valid according to the whitelist
        boolean isValid = cleaner.isValid(dirtyDocument);

        // Assert that the dirty document is not valid
        assertEquals(false, isValid);
    }

    @Test
    public void testCleaningVisitorHead() {
        // Create a new cleaner with a whitelist
        Whitelist whitelist = new Whitelist();
        Cleaner.CleaningVisitor cleaningVisitor = new Cleaner.CleaningVisitor(new Element("root"), new Element("destination"));

        // Create a source node and invoke the 'head' method of the cleaning visitor
        Element sourceElement = new Element("div");
        sourceElement.attr("class", "test");
        cleaningVisitor.head(sourceElement, 0);

        // Assert that the destination element contains the safe attributes
        Element destinationElement = cleaningVisitor.destination;
        assertEquals("div", destinationElement.tagName());
        assertEquals("test", destinationElement.attr("class"));
    }

    @Test
    public void testCleaningVisitorTail() {
        // Create a new cleaner with a whitelist
        Whitelist whitelist = new Whitelist();
        Cleaner.CleaningVisitor cleaningVisitor = new Cleaner.CleaningVisitor(new Element("root"), new Element("destination"));

        // Create a source node and invoke the 'tail' method of the cleaning visitor
        Element sourceElement = new Element("div");
        cleaningVisitor.head(sourceElement, 0);
        cleaningVisitor.tail(sourceElement, 0);

        // Assert that the destination element is the parent
        Element destinationElement = cleaningVisitor.destination;
        assertEquals("root", destinationElement.tagName());
    }

    @Test
    public void testCopySafeNodes() {
        // Create a new cleaner with a whitelist
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        // Create a dirty document with body content
        Document dirtyDocument = Document.createShell("http://example.com");
        Element body = dirtyDocument.body();
        Element div = body.appendElement("div");
        div.appendText("This is a dirty document");

        // Create a clean document
        Document cleanDocument = Document.createShell("http://example.com");
        Element cleanBody = cleanDocument.body();

        // Copy safe nodes from the dirty document to the clean document
        int numDiscarded = cleaner.copySafeNodes(body, cleanBody);

        // Assert that the clean document only contains the safe nodes
        assertEquals(0, numDiscarded);
        assertEquals("div", cleanBody.child(0).tagName());
        assertEquals("This is a dirty document", cleanBody.child(0).text());
    }

    @Test
    public void testCreateSafeElement() {
        // Create a new cleaner with a whitelist
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        // Create a source element with unsafe attributes
        Element sourceElement = new Element("div");
        sourceElement.attr("class", "test");
        sourceElement.attr("onclick", "alert('Hello World!')");

        // Create a safe element using the cleaner
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        // Assert that the safe element only contains the safe attributes
        Element safeElement = elementMeta.el;
        assertEquals("div", safeElement.tagName());
        assertEquals("test", safeElement.attr("class"));
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }
}