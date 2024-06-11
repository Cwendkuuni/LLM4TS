package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
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
    public void testCleaner() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Parser.parseBodyFragment("<html><body><div id=\"foo\"><p>Text1 <img src=\"evil.js\"></p><script>alert('XSS');</script><p>Text2</p></div></body></html>", null);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("Text1 Text2", cleanDocument.body().text());
        assertNull(cleanDocument.body().child(0));
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);

        Document invalidDocument = Parser.parseBodyFragment("<html><body><div id=\"foo\"><p>Text1 <img src=\"evil.js\"></p><script>alert('XSS');</script><p>Text2</p></div></body></html>", null);
        assertFalse(cleaner.isValid(invalidDocument));
    }

    @Test
    public void testIsValidValidDocument() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);

        Document validDocument = Parser.parseBodyFragment("<html><body><div id=\"foo\"><p>Text1</p><p>Text2</p></div></body></html>", null);
        assertTrue(cleaner.isValid(validDocument));
    }

    @Test
    public void testCleaningVisitor() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Parser.parseBodyFragment("<html><body><div id=\"foo\"><p>Text1 <img src=\"evil.js\"></p><script>alert('XSS');</script><p>Text2</p></div></body></html>", null);
        Element root = dirtyDocument.body();
        Element destination = new Element("div");
        CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, destination);

        NodeTraversor traversor = new NodeTraversor(cleaningVisitor);
        traversor.traverse(root);

        assertEquals("Text1 Text2", destination.text());
        assertNull(destination.child(0));
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = new Whitelist() {
            @Override
            public boolean isSafeAttribute(String tag, Element context, Attribute attribute) {
                return tag.equals("p") && attribute.getKey().equals("class");
            }

            @Override
            public boolean isSafeTag(String tag) {
                return tag.equals("p");
            }

            @Override
            public Attributes getEnforcedAttributes(String tag) {
                Attributes attributes = new Attributes();
                attributes.put("class", "safe-class");
                return attributes;
            }
        };

        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceElement = new Element("p", null);
        sourceElement.attributes().put("class", "unsafe-class");
        sourceElement.attributes().put("id", "source-id");

        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        assertEquals("p", elementMeta.el.tagName());
        assertSame(sourceElement.baseUri(), elementMeta.el.baseUri());
        assertEquals("safe-class", elementMeta.el.classAttrs().get("class"));
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);

        Element source = new Element("div");
        source.appendText("Text1");
        source.appendElement(new Element("p").text("Text2"));
        source.appendElement(new Element("img").attr("src", "evil.js"));

        Element destination = new Element("div");

        int numDiscarded = cleaner.copySafeNodes(source, destination);

        assertEquals("Text1", destination.child(0).text());
        assertSame(destination.child(0).parent(), destination);
        assertNull(destination.child(1));
        assertEquals(1, numDiscarded);
    }
}