package org.jsoup.nodes.test;

import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * DocumentTest.java
 */
public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        assertNotNull(document);
        assertEquals("#document", document.nodeName());
        assertEquals(baseUri, document.baseUri());
        assertNull(document.head());
        assertNull(document.body());
        assertNull(document.title());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        assertNotNull(document);
        assertNotNull(document.head());
        assertNotNull(document.body());
        assertEquals("#document", document.nodeName());
        assertEquals(baseUri, document.baseUri());
        assertNull(document.title());
    }

    @Test
    public void testHeadMethod() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Element head = document.head();
        assertNotNull(head);
        assertEquals("#head", head.nodeName());
        assertEquals(document, head.parent());
    }

    @Test
    public void testBodyMethod() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Element body = document.body();
        assertNotNull(body);
        assertEquals("#body", body.nodeName());
        assertEquals(document, body.parent());
    }

    @Test
    public void testTitleMethod() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test(expected = NullPointerException.class)
    public void testTitleNullParameter() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        document.title(null);
    }

    @Test
    public void testCreateElementMethod() {
        Document document = new Document("http://example.com");
        Element element = document.createElement("a");
        assertNotNull(element);
        assertEquals("#document", document.nodeName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormaliseMethod() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        document.head().appendText("Hello");
        document.appendText("World");
        document.body().appendElement(new TextNode(" testing ", ""));
        document.normalise();
        assertEquals(3, document.childNodes().size());
        Element body = document.body();
        assertEquals(1, body.childNodes().size());
        TextNode textNode = (TextNode) body.childNodes().get(0);
        assertEquals(" testing World", textNode.text());
    }

    @Test
    public void testFindFirstElementByTagNameMethod() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Element html = document.createElement("html");
        document.appendChild(html);
        Element head = document.createElement("head");
        html.appendChild(head);
        Element title = document.createElement("title");
        title.appendText("Test Title");
        head.appendChild(title);
        Element body = document.createElement("body");
        html.appendChild(body);

        Element firstTitle = document.findFirstElementByTagName("title", document.body());
        assertNull(firstTitle);

        firstTitle = document.findFirstElementByTagName("title", html);
        assertEquals("title", firstTitle.nodeName());
        assertEquals("Test Title", firstTitle.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindFirstElementByTagNameNullNode() {
        Document document = new Document("http://example.com");
        document.findFirstElementByTagName("title", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindFirstElementByTagNameInvalidTagName() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        document.findFirstElementByTagName("invalid", document);
    }

    @Test
    public void testOuterHtmlMethod() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Element html = document.createElement("html");
        document.appendChild(html);
        Element head = document.createElement("head");
        html.appendChild(head);
        Element title = document.createElement("title");
        title.appendText("Test Title");
        head.appendChild(title);
        Element body = document.createElement("body");
        html.appendChild(body);
        body.appendText("Test Body");

        String htmlString = document.outerHtml();
        assertEquals("<html><head><title>Test Title</title></head><body>Test Body</body></html>", htmlString);
    }

    @Test
    public void testTextMethod() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Element body = document.body();
        body.text("Test Body");
        assertEquals("Test Body", document.text());
    }

    @Test(expected = NullPointerException.class)
    public void testTextNullParameter() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        document.text(null);
    }

    @Test
    public void testNodeNameMethod() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testCloneMethod() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Document clonedDocument = document.clone();
        assertEquals("#document", clonedDocument.nodeName());
        assertEquals(document, clonedDocument.outputSettings());
        assertEquals(document.outputSettings().charset(), clonedDocument.outputSettings().charset());
    }

    @Test(expected = CloneNotSupportedException.class)
    public void testCloneNotSupportedException() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        document.getClass().getSuperclass().getMethod("clone").setAccessible(true);
        document.clone();
    }

    @Test
    public void testOutputSettingsClass() {
        OutputSettings settings = new OutputSettings();
        OutputSettings clonedSettings = settings.clone();
        assertEquals(Entities.EscapeMode.base, clonedSettings.escapeMode());
        assertEquals("UTF-8", clonedSettings.charset().name());
        assertEquals(Charset.forName("UTF-8"), clonedSettings.charset());
        assertEquals(true, clonedSettings.prettyPrint());
        assertEquals(1, clonedSettings.indentAmount());
    }

    @Test
    public void testOutputSettingsEscapeMode() {
        OutputSettings settings = new OutputSettings();
        OutputSettings settingsWithExtendedEscapeMode = settings.escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, settingsWithExtendedEscapeMode.escapeMode());
    }

    @Test
    public void testOutputSettingsCharset() {
        OutputSettings settings = new OutputSettings();
        OutputSettings settingsWithUtf16Charset = settings.charset(Charset.forName("UTF-16"));
        assertEquals("UTF-16", settingsWithUtf16Charset.charset().name());
    }

    @Test
    public void testOutputSettingsCharsetEncoding() {
        OutputSettings settings = new OutputSettings();
        OutputSettings settingsWithUtf16CharsetEncoding = settings.charset("UTF-16");
        assertEquals("UTF-16", settingsWithUtf16CharsetEncoding.charset().name());
        assertEquals(Charset.forName("UTF-16"), settingsWithUtf16CharsetEncoding.charset());
    }

    @Test
    public void testOutputSettingsPrettyPrint() {
        OutputSettings settings = new OutputSettings();
        OutputSettings settingsWithPrettyPrintFalse = settings.prettyPrint(false);
        assertFalse(settingsWithPrettyPrintFalse.prettyPrint());
    }

    @Test
    public void testOutputSettingsIndentAmount() {
        OutputSettings settings = new OutputSettings();
        OutputSettings settingsWithIndentAmountFive = settings.indentAmount(5);
        assertEquals(5, settingsWithIndentAmountFive.indentAmount());
    }
}