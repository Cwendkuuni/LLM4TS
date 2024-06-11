package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class DocumentTest {
    @Test
    public void testConstructor() {
        String baseUri = "http://www.example.com";
        Document doc = new Document(baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "http://www.example.com";
        Document doc = Document.createShell(baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testHead() {
        Document doc = new Document("http://www.example.com");
        Element head = doc.head();
        assertNull(head);
    }

    @Test
    public void testBody() {
        Document doc = new Document("http://www.example.com");
        Element body = doc.body();
        assertNull(body);
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://www.example.com");
        String title = doc.title();
        assertEquals("", title);

        doc = Document.createShell("http://www.example.com");
        doc.title("Test Title");
        title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://www.example.com");
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals(doc.baseUri(), element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://www.example.com");
        doc.normalise();
        // Test not implemented
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://www.example.com");
        String outerHtml = doc.outerHtml();
        assertNotNull(outerHtml);
    }

    @Test
    public void testText() {
        Document doc = new Document("http://www.example.com");
        doc.text("test");
        // Test not implemented
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("http://www.example.com");
        String nodeName = doc.nodeName();
        assertEquals("#document", nodeName);
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://www.example.com");
        Document.OutputSettings outputSettings = doc.outputSettings();
        assertNotNull(outputSettings);
    }
}