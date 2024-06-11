package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.OutputSettings;
import org.jsoup.nodes.QuirksMode;
import org.jsoup.parser.Tag;
import org.junit.Test;

/**
 * DocumentTest.java
 */
public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document document = new Document("http://example.com");
        assertNotNull(document);
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        Element html = document.getByTagName("html");
        assertNotNull(html);
        Element head = html.getByTagName("head");
        assertNotNull(head);
        Element body = html.getByTagName("body");
        assertNotNull(body);
    }

    @Test
    public void testElementHead() {
        Document document = new Document("http://example.com");
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testElementBody() {
        Document document = new Document("http://example.com");
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testStringTitle() {
        Document document = new Document("http://example.com");
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testSetTitleWithNullTitle() {
        Document document = new Document("http://example.com");
        document.title(null);
        assertNull(document.title());
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("http://example.com");
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document document = new Document("http://example.com");
        document.title("Test Title");
        document.appendText("Test ");
        document.body().appendText("Content");
        document.normalise();
        Element body = document.body();
        Element title = document.getByTagName("title");
        assertEquals("Test TitleTest ", title.text().trim());
        assertEquals("Test Content", body.text().trim());
    }

    @Test(expected = NullPointerException.class)
    public void testNormaliseWithNullDocument() {
        Document document = null;
        document.normalise();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindFirstElementByTagNameWithNullNode() {
        Document document = new Document("http://example.com");
        findFirstElementByTagName(document, null, "html");
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document document = new Document("http://example.com");
        Element html = document.getByTagName("html");
        Element head = findFirstElementByTagName(html, "head", "html");
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testOuterHtml() {
        Document document = new Document("http://example.com");
        Element html = document.getByTagName("html");
        html.appendText("Test");
        assertEquals("<html>Test</html>", document.outerHtml());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetTextWithNullText() {
        Document document = new Document("http://example.com");
        document.setText(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetTextWithEmptyString() {
        Document document = new Document("http://example.com");
        document.setText("");
    }

    @Test
    public void testText() {
        Document document = new Document("http://example.com");
        document.text("Test");
        assertEquals("Test", document.text().toString());
    }

    @Test
    public void testNodeName() {
        Document document = new Document("http://example.com");
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testClone() {
        Document document = new Document("http://example.com");
        OutputSettings outputSettings = document.outputSettings();
        OutputSettings clonedOutputSettings = outputSettings.clone();
        Document clonedDocument = document.clone();
        assertEquals(outputSettings, clonedOutputSettings);
        assertEquals(document, clonedDocument);
    }

    @Test
    public void testOutputSettings() {
        Document document = new Document("http://example.com");
        OutputSettings outputSettings = document.outputSettings();
        Charset charset = StandardCharsets.UTF_8();
        OutputSettings updatedOutputSettings = outputSettings.charset(charset);
        assertEquals(StandardCharsets.UTF_8(), updatedOutputSettings.charset());
        assertEquals(outputSettings, document.outputSettings());
    }

    @Test
    public void testQuirksMode() {
        Document document = new Document("http://example.com");
        QuirksMode quirksMode = QuirksMode.quirks;
        Document quirksModeDocument = document.quirksMode(quirksMode);
        assertEquals(quirksMode, quirksModeDocument.quirksMode());
        assertEquals(document, document.quirksMode(QuirksMode.noQuirks));
    }

    private Element findFirstElementByTagName(Node node, String tagName, String nodeName) {
        if (node.nodeName().equals(nodeName)) {
            Element element = (Element) node;
            if (tagName.equals(element.nodeName())) {
                return element;
            }
        }
        for (Node child : node.childNodes()) {
            Element found = findFirstElementByTagName(child, tagName, nodeName);
            if (found != null) {
                return found;
            }
        }
        return null;
    }
}