package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

/**
 * Tests for {@link Document} class.
 */
public class DocumentTest {

    @Test
    public void testDocumentCreation() {
        Document document = new Document("baseUri");
        assertNotNull(document);
        assertEquals("#root", document.tagName());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("baseUri");
        assertTrue(document.head() != null);
        assertTrue(document.body() != null);
    }

    @Test
    public void testGetTitle() {
        Document document = new Document("baseUri");
        document.title("Test Title");
        assertEquals("Test Title", document.title());
        document.title("");
        assertEquals("", document.title());
    }

    @Test
    public void testSetTitle() {
        Document document = new Document("baseUri");
        document.title("Test Title");
        document.title("Updated Title");
        assertEquals("Updated Title", document.title());
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("baseUri");
        Element element = document.createElement("a");
        assertNotNull(element);
        assertEquals("a", element.tagName());
    }

    @Test
    public void testNormalise() {
        Document document = new Document("baseUri");
        document.title("Test Title");
        Element titleElement = document.getElementsByTag("title").first();
        assertEquals("Test Title", titleElement.text());

        Element aElement = document.createElement("a");
        document.head().appendElement(titleElement);
        document.head().appendElement(aElement);
        document.body().appendText("Test Body");

        document.normalise();

        Element newTitleElement = document.getElementsByTag("title").first();
        assertEquals("Test Title", newTitleElement.text());
        Element newAPlement = document.getElementsByTag("a").first();
        assertEquals("", newAPlement.text());
        assertEquals("Test Body", document.body().text());
    }

    @Test(expected = NullPointerException.class)
    public void testFindFirstElementByTagNameNullNode() {
        Document document = new Document("baseUri");
        findFirstElementByTagName(null, document);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindFirstElementByTagNameInvalidTagName() {
        Document document = new Document("baseUri");
        findFirstElementByTagName("InvalidTagName", document);
    }

    @Test
    public void testFindFirstElementByTagNameValidTagName() {
        Document document = new Document("baseUri");
        document.head().appendElement(new Element("head"));
        document.head().appendElement(new Element("title"));

        Element titleElement = findFirstElementByTagName("title", document.head());
        assertNotNull(titleElement);
        assertEquals("title", titleElement.tagName());
    }

    private Element findFirstElementByTagName(String tagName, Node node) {
        if (node.nodeName().equals(tagName))
            return (Element) node;
        else {
            for (Node child : node.childNodes) {
                Element found = findFirstElementByTagName(tagName, child);
                if (found != null)
                    return found;
            }
        }
        return null;
    }

    @Test
    public void testOuterHtml() {
        Document document = new Document("baseUri");
        document.title("Test Title");
        document.body().appendText("Test Body");

        assertEquals("<html><head></head><body></body></html>", document.outerHtml());
        document.title("Updated Title");
        document.body().appendText("Updated Body");
        assertEquals("<html><head><title>Updated Title</title></head><body></body></html>", document.outerHtml());
    }

    @Test
    public void testText() {
        Document document = new Document("baseUri");
        document.body().appendText("Test Body");

        document.text("Updated Body");
        assertEquals("Updated Body", document.body().text());
    }

    @Test
    public void testNodeName() {
        Document document = new Document("baseUri");
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testOutputSettingsCreation() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        assertNotNull(outputSettings);
    }

    @Test
    public void testGetOutputSettings() {
        Document document = new Document("baseUri");
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
    }

    @Test
    public void testGetEscapeMode() {
        Document document = new Document("baseUri");
        Document.OutputSettings outputSettings = document.outputSettings();
        assertEquals(Document.Entities.EscapeMode.base, outputSettings.escapeMode());
    }

    @Test
    public void testSetEscapeMode() {
        Document document = new Document("baseUri");
        Document.OutputSettings outputSettings = document.outputSettings();
        outputSettings.escapeMode(Document.Entities.EscapeMode.extended);
        assertEquals(Document.Entities.EscapeMode.extended, outputSettings.escapeMode());
    }

    @Test
    public void testGetCharset() {
        Document document = new Document("baseUri");
        Document.OutputSettings outputSettings = document.outputSettings();
        assertEquals("UTF-8", outputSettings.charset().name());
    }

    @Test
    public void testSetCharset() {
        Document document = new Document("baseUri");
        Document.OutputSettings outputSettings = document.outputSettings();
        outputSettings.charset("ISO-8859-1");
        assertEquals("ISO-8859-1", outputSettings.charset().name());
    }

    @Test
    public void testGetEncoder() {
        Document document = new Document("baseUri");
        Document.OutputSettings outputSettings = document.outputSettings();
        CharsetEncoder encoder = outputSettings.encoder();
        assertNotNull(encoder);
    }

    @Test
    public void testIsPrettyPrintEnabled() {
        Document document = new Document("baseUri");
        Document.OutputSettings outputSettings = document.outputSettings();
        assertTrue(outputSettings.prettyPrint());
    }

    @Test
    public void testSetPrettyPrintEnabled() {
        Document document = new Document("baseUri");
        Document.OutputSettings outputSettings = document.outputSettings();
        outputSettings.prettyPrint(false);
        assertFalse(outputSettings.prettyPrint());
    }

    @Test
    public void testGetIndentAmount() {
        Document document = new Document("baseUri");
        Document.OutputSettings outputSettings = document.outputSettings();
        assertEquals(1, outputSettings.indentAmount());
    }

    @Test
    public void testSetIndentAmount() {
        Document document = new Document("baseUri");
        Document.OutputSettings outputSettings = document.outputSettings();
        outputSettings.indentAmount(2);
        assertEquals(2, outputSettings.indentAmount());
    }
}