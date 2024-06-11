package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.jsoup.helper.DataStructures;
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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CleanerTest {

    @Test
    public void testCleaner() {
        Whitelist whitelist = new Whitelist.SimpleTextOnlyWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        // Test empty document
        Document dirtyDocument = Document.createShell("", "");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertTrue(cleanDocument.body().childNodeSize() == 0);

        // Test valid document
        Document validDocument = Document.createShell("", "");
        Element root = validDocument.createElement("html");
        Element body = validDocument.createElement("body");
        root.appendChild(body);
        body.appendChild(validDocument.createTextNode("Hello World"));
        cleanDocument = cleaner.clean(validDocument);
        assertEquals(validDocument.body().childNodeSize(), cleanDocument.body().childNodeSize());
        assertTrue(cleanDocument.body().childNode(0) instanceof TextNode);
        assertEquals("Hello World", cleanDocument.body().childNode(0).text());

        // Test invalid document
        Document invalidDocument = Document.createShell("", "");
        Element rootInvalid = invalidDocument.createElement("html");
        Element bodyInvalid = invalidDocument.createElement("body");
        rootInvalid.appendChild(bodyInvalid);
        bodyInvalid.appendChild(invalidDocument.createElement("script", null, null, ""));
        cleanDocument = cleaner.clean(invalidDocument);
        assertEquals(validDocument.body().childNodeSize(), cleanDocument.body().childNodeSize());
        assertTrue(cleanDocument.body().childNode(0) instanceof TextNode);
        assertEquals("", cleanDocument.body().childNode(0).text());

        // Test isValid()
        Document validDocument2 = Document.createShell("", "");
        Element rootValid2 = validDocument2.createElement("html");
        Element bodyValid2 = validDocument2.createElement("body");
        rootValid2.appendChild(bodyValid2);
        Element divValid = validDocument2.createElement("div");
        bodyValid2.appendChild(divValid);
        divValid.appendChild(validDocument2.createTextNode("Hello World"));
        boolean isValid = cleaner.isValid(validDocument2);
        assertTrue(isValid);

        Document invalidDocument2 = Document.createShell("", "");
        Element rootInvalid2 = invalidDocument2.createElement("html");
        Element bodyInvalid2 = invalidDocument2.createElement("body");
        rootInvalid2.appendChild(bodyInvalid2);
        Element scriptInvalid = invalidDocument2.createElement("script");
        bodyInvalid2.appendChild(scriptInvalid);
        scriptInvalid.appendChild(invalidDocument2.createTextNode("alert('Hello')"));
        isValid = cleaner.isValid(invalidDocument2);
        assertFalse(isValid);

        // Test cleaner with custom whitelist
        Whitelist customWhitelist = new Whitelist.SimpleElementWhitelist(Arrays.asList("html", "body", "div"), Arrays.asList("class"));
        Cleaner customCleaner = new Cleaner(customWhitelist);
        Document customValidDocument = Document.createShell("", "");
        Element rootCustomValid = customValidDocument.createElement("html");
        Element bodyCustomValid = customValidDocument.createElement("body");
        rootCustomValid.appendChild(bodyCustomValid);
        Element divCustomValid = customValidDocument.createElement("div");
        bodyCustomValid.appendChild(divCustomValid);
        divCustomValid.setAttribute("class", "foo");
        Document cleanCustomDocument = customCleaner.clean(customValidDocument);
        assertEquals(customValidDocument.body().childNodeSize(), cleanCustomDocument.body().childNodeSize());
        assertTrue(cleanCustomDocument.body().childNode(0) instanceof Element);
        assertEquals("div", cleanCustomDocument.body().childNode(0).tagName());
        assertTrue(cleanCustomDocument.body().childNode(0).hasAttr("class"));
        assertEquals("foo", cleanCustomDocument.body().childNode(0).getClass("class"));

        Document customInvalidDocument = Document.createShell("", "");
        Element rootCustomInvalid = customInvalidDocument.createElement("html");
        Element bodyCustomInvalid = customInvalidDocument.createElement("body");
        rootCustomInvalid.appendChild(bodyCustomInvalid);
        Element scriptCustomInvalid = customInvalidDocument.createElement("script");
        Element headCustomInvalid = customInvalidDocument.createElement("head");
        headCustomInvalid.appendChild(scriptCustomInvalid);
        bodyCustomInvalid.appendChild(headCustomInvalid);
        Document cleanCustomInvalidDocument = customCleaner.clean(customInvalidDocument);
        assertEquals(customValidDocument.body().childNodeSize(), cleanCustomInvalidDocument.body().childNodeSize());
        assertTrue(cleanCustomInvalidDocument.body().childNode(0) instanceof TextNode);
        assertEquals("", cleanCustomInvalidDocument.body().childNode(0).text());
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = new Whitelist.SimpleTextOnlyWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        // Test empty document
        Element source = Document.createShell("", "").createElement("html").body();
        Element destination = Document.createShell("", "").createElement("html").body();
        int numDiscarded = cleaner.copySafeNodes(source, destination);
        assertTrue(numDiscarded == 0);

        // Test valid document
        Document validDocument = Document.createShell("", "");
        Element sourceValid = validDocument.createElement("html").body();
        Element destinationValid = Document.createElement("html").body();
        numDiscarded = cleaner.copySafeNodes(sourceValid, destinationValid);
        assertTrue(numDiscarded == 0);
        assertSame(sourceValid, destinationValid.child(0));

        // Test invalid document
        Document invalidDocument = Document.createShell("", "");
        Element sourceInvalid = invalidDocument.createElement("html").body();
        Element script = invalidDocument.createElement("script");
        script.appendChild(invalidDocument.createTextNode("alert('Hello')"));
        sourceInvalid.appendChild(script);
        Element destinationInvalid = Document.createElement("html").body();
        numDiscarded = cleaner.copySafeNodes(sourceInvalid, destinationInvalid);
        assertTrue(numDiscarded > 0);
        assertTrue(destinationInvalid.childNodeSize() == 0);

        // Test copySafeNodes with custom whitelist
        Whitelist customWhitelist = new Whitelist.SimpleElementWhitelist(Arrays.asList("html", "body", "div"), Arrays.asList("class"));
        Cleaner customCleaner = new Cleaner(customWhitelist);
        Document customValidDocument = Document.createShell("", "");
        Element sourceCustomValid = customValidDocument.createElement("html").body();
        Element divCustomValid = customValidDocument.createElement("div");
        divCustomValid.setAttribute("class", "foo");
        sourceCustomValid.appendChild(divCustomValid);
        Element destinationCustomValid = Document.createElement("html").body();
        numDiscarded = customCleaner.copySafeNodes(sourceCustomValid, destinationCustomValid);
        assertTrue(numDiscarded == 0);
        assertSame(divCustomValid, destinationCustomValid.child(0));

        Document customInvalidDocument = Document.createShell("", "");
        Element sourceCustomInvalid = customInvalidDocument.createElement("html").body();
        Element scriptCustomInvalid = customInvalidDocument.createElement("script");
        scriptCustomInvalid.appendChild(customInvalidDocument.createTextNode("alert('Hello')"));
        sourceCustomInvalid.appendChild(scriptCustomInvalid);
        Element headCustomInvalid = customInvalidDocument.createElement("head");
        scriptCustomInvalid.appendChild(headCustomInvalid);
        sourceCustomInvalid.appendChild(headCustomInvalid);
        Element destinationCustomInvalid = Document.createElement("html").body();
        numDiscarded = customCleaner.copySafeNodes(sourceCustomInvalid, destinationCustomInvalid);
        assertTrue(numDiscarded > 0);
        assertTrue(destinationCustomInvalid.childNodeSize() == 0);
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = new Whitelist.SimpleTextOnlyWhitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        // Test valid document
        Document validDocument = Document.createShell("", "");
        boolean isValid = cleaner.isValid(validDocument);
        assertTrue(isValid);

        // Test invalid document
        Document invalidDocument = Document.createShell("", "");
        Element script = invalidDocument.createElement("script");
        script.appendChild(invalidDocument.createTextNode("alert('Hello')"));
        invalidDocument.body().appendChild(script);
        isValid = cleaner.isValid(invalidDocument);
        assertFalse(isValid);

        // Test valid document with enforced attributes
        Document validDocumentWithEnforcedAttributes = Document.createShell("", "");
        Element rootValidWithEnforcedAttributes = validDocumentWithEnforcedAttributes.createElement("html");
        Element bodyValidWithEnforcedAttributes = validDocumentWithEnforcedAttributes.createElement("body");
        rootValidWithEnforcedAttributes.appendChild(bodyValidWithEnforcedAttributes);
        Element divValidWithEnforcedAttributes = validDocumentWithEnforcedAttributes.createElement("div");
        divValidWithEnforcedAttributes.setAttribute("class", "foo");
        bodyValidWithEnforcedAttributes.appendChild(divValidWithEnforcedAttributes);
        isValid = cleaner.isValid(validDocumentWithEnforcedAttributes);
        assertTrue(isValid);

        // Test invalid document with enforced attributes
        Document invalidDocumentWithEnforcedAttributes = Document.createShell("", "");
        Element rootInvalidWithEnforcedAttributes = invalidDocumentWithEnforcedAttributes.createElement("html");
        Element bodyInvalidWithEnforcedAttributes = invalidDocumentWithEnforcedAttributes.createElement("body");
        rootInvalidWithEnforcedAttributes.appendChild(bodyInvalidWithEnforcedAttributes);
        Element scriptInvalidWithEnforcedAttributes = invalidDocumentWithEnforcedAttributes.createElement("script");
        Element headInvalidWithEnforcedAttributes = invalidDocumentWithEnforcedAttributes.createElement("head");
        scriptInvalidWithEnforcedAttributes.appendChild(headInvalidWithEnforcedAttributes);
        bodyInvalidWithEnforcedAttributes.appendChild(scriptInvalidWithEnforcedAttributes);
        Element divInvalidWithEnforcedAttributes = invalidDocumentWithEnforcedAttributes.createElement("div");
        divInvalidWithEnforcedAttributes.setAttribute("class", "bar");
        bodyInvalidWithEnforcedAttributes.appendChild(divInvalidWithEnforcedAttributes);
        isValid = cleaner.isValid(invalidDocumentWithEnforcedAttributes);
        assertFalse(isValid);

        // Test valid document with custom whitelist
        Whitelist customWhitelist = new Whitelist.SimpleElementWhitelist(Arrays.asList("html", "body", "div"), Arrays.asList("class"));
        Cleaner customCleaner = new Cleaner(customWhitelist);
        Document customValidDocument = Document.createShell("", "");
        Element sourceCustomValid = customValidDocument.createElement("html").body();
        Element divCustomValid = customValidDocument.createElement("div");
        divCustomValid.setAttribute("class", "foo");
        sourceCustomValid.appendChild(divCustomValid);
        boolean isValidCustom = customCleaner.isValid(customValidDocument);
        assertTrue(isValidCustom);

        Document customInvalidDocument = Document.createShell("", "");
        Element sourceCustomInvalid = customInvalidDocument.createElement("html").body();
        Element scriptCustomInvalid = customInvalidDocument.createElement("script");
        Element headCustomInvalid = customInvalidDocument.createElement("head");
        scriptCustomInvalid.appendChild(headCustomInvalid);
        sourceCustomInvalid.appendChild(scriptCustomInvalid);
        Element divCustomInvalid = customInvalidDocument.createElement("div");
        divCustomInvalid.setAttribute("class", "bar");
        sourceCustomInvalid.appendChild(divCustomInvalid);
        isValidCustom = customCleaner.isValid(customInvalidDocument);
        assertFalse(isValidCustom);
    }
}