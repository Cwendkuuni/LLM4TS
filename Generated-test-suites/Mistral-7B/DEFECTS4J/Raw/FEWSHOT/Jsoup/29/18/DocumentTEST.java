package org.jsoup.nodes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class DocumentTest {

    @Test
    public void testDocumentCreation() {
        Document doc = new Document("baseUri");
        assertSame(Tag.valueOf("#root"), doc.tag());
        assertEquals("baseUri", doc.baseUri());

        Document shellDoc = Document.createShell("baseUri");
        assertEquals(3, shellDoc.numberOfChildren());
        Element html = shellDoc.head();
        assertEquals("#html", html.tag().name());
        assertEquals("baseUri", html.baseUri());
        Element head = html.child(0);
        assertEquals("#head", head.tag().name());
        assertEquals("baseUri", head.baseUri());
        Element body = html.child(1);
        assertEquals("#body", body.tag().name());
        assertEquals("baseUri", body.baseUri());
    }

    @Test
    public void testGetters() {
        Document doc = new Document("baseUri");
        Element head = doc.head();
        assertEquals("#head", head.tag().name());
        assertEquals("baseUri", head.baseUri());
        Element body = doc.body();
        assertEquals("#body", body.tag().name());
        assertEquals("baseUri", body.baseUri());

        String title = doc.title();
        assertEquals("", title);
    }

    @Test
    public void testSetters() {
        Document doc = new Document("baseUri");
        doc.title("Test Title");
        String title = doc.title();
        assertEquals("Test Title", title);

        Element titleEl = doc.getElementsByTag("title").first();
        assertNotNull(titleEl);
        assertEquals("Test Title", titleEl.text().trim());

        doc.title(null);
        title = doc.title();
        assertEquals("", title);
        assertNull(doc.getElementsByTag("title").first());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("baseUri");
        Element e = doc.createElement("a");
        assertEquals("#a", e.tag().name());
        assertEquals("baseUri", e.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("baseUri");
        doc.appendText("Text");
        doc.body().appendText(" Other text");

        doc.normalise();
        assertEquals("baseUri", doc.baseUri());
        Element body = doc.body();
        assertEquals("Text Other text", body.text().trim());
    }

    @Test
    public void testNormaliseStructure() {
        Document doc = new Document("baseUri");
        doc.body().appendElement("p1").text("P1");
        doc.body().appendElement("p2").text("P2");
        doc.body().appendElement("p3").text("P3");

        doc.normalise();
        Element bodyEl = doc.body();
        assertEquals(3, bodyEl.numberOfChildren());
        Element masterP = bodyEl.getElementsByTag("p").first();
        assertEquals(3, masterP.numberOfChildren());
        Element p1 = masterP.child(0);
        assertEquals("P1", p1.text().trim());
        Element p2 = masterP.child(1);
        assertEquals("P2", p2.text().trim());
        Element p3 = masterP.child(2);
        assertEquals("P3", p3.text().trim());

        Element dupedP = doc.getElementsByTag("p").get(1);
        assertEquals("P2", dupedP.text().trim());
        bodyEl.removeChild(dupedP);
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("baseUri");
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");

        Element title = doc.findFirstElementByTagName("title", html);
        assertNull(title);

        title = doc.findFirstElementByTagName("title", head);
        assertNull(title);

        title = doc.findFirstElementByTagName("title", body);
        assertNull(title);

        title = doc.findFirstElementByTagName("title", doc);
        assertEquals("#title", title.tag().name());
        assertEquals("", title.text().trim());

        title = doc.appendElement("title").text("Test Title");
        assertEquals("#title", title.tag().name());
        assertEquals("Test Title", title.text().trim());

        title = doc.findFirstElementByTagName("title", doc);
        assertEquals("#title", title.tag().name());
        assertEquals("Test Title", title.text().trim());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("baseUri");
        Element p = doc.appendElement("p").text("Test");

        String html = doc.outerHtml();
        assertEquals("<html><head></head><body><p>Test</p></body></html>", html);

        doc.title("Test Title");
        html = doc.outerHtml();
        assertEquals("<html><head><title>Test Title</title></head><body><p>Test</p></body></html>", html);
    }

    @Test
    public void testText() {
        Document doc = new Document("baseUri");
        doc.text("Test Text");

        assertEquals("Test Text", doc.text().trim());
        assertEquals("Test Text", doc.outerHtml().trim());

        doc.title("Test Title");
        doc.text("Test Text with Title");

        assertEquals("Test Text with Title", doc.text().trim());
        assertEquals("<title>Test Title</title><p>Test Text with Title</p>", doc.outerHtml().trim());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("baseUri");
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testClone() {
        Document doc = new Document("baseUri");
        doc.title("Test Title");

        Document clone = doc.clone();
        assertEquals("Test Title", clone.title());

        // Verify settings are cloned
        OutputSettings outputSettings = doc.outputSettings();
        OutputSettings cloneSettings = clone.outputSettings();
        assertEquals(doc.outputSettings().charset().name(), clone.outputSettings().charset().name());
        assertEquals(doc.outputSettings().escapeMode(), clone.outputSettings().escapeMode());
        assertEquals(doc.outputSettings().prettyPrint(), clone.outputSettings().prettyPrint());
        assertEquals(doc.outputSettings().indentAmount(), clone.outputSettings().indentAmount());

        // Verify quirksMode is not cloned
        assertEquals(doc.quirksMode(), clone.quirksMode());
        assertNotSame(doc.quirksMode(), clone.quirksMode());
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("baseUri");

        OutputSettings outputSettings = doc.outputSettings();
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(StandardCharsets.UTF_8, outputSettings.charset());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());

        OutputSettings newOutputSettings = outputSettings.escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, newOutputSettings.escapeMode());
        assertEquals(outputSettings, doc.outputSettings());
        assertNotSame(outputSettings, newOutputSettings);

        newOutputSettings = outputSettings.charset(Charset.forName("ISO-8859-1"));
        assertEquals(Charset.forName("ISO-8859-1"), newOutputSettings.charset());
        assertEquals(outputSettings, doc.outputSettings());
        assertNotSame(outputSettings, newOutputSettings);

        newOutputSettings = outputSettings.prettyPrint(false);
        assertFalse(newOutputSettings.prettyPrint());
        assertEquals(outputSettings, doc.outputSettings());
        assertNotSame(outputSettings, newOutputSettings);

        newOutputSettings = outputSettings.indentAmount(5);
        assertEquals(5, newOutputSettings.indentAmount());
        assertEquals(outputSettings, doc.outputSettings());
        assertNotSame(outputSettings, newOutputSettings);

        OutputSettings clone = doc.outputSettings().clone();
        assertEquals(outputSettings, clone);
    }

    @Test
    public void testQuirksMode() {
        Document doc = new Document("baseUri");

        assertEquals(QuirksMode.noQuirks, doc.quirksMode());

        doc.quirksMode(QuirksMode.quirks);
        assertEquals(QuirksMode.quirks, doc.quirksMode());

        doc.quirksMode(QuirksMode.limitedQuirks);
        assertEquals(QuirksMode.limitedQuirks, doc.quirksMode());

        doc.quirksMode(QuirksMode.noQuirks);
        assertEquals(QuirksMode.noQuirks, doc.quirksMode());
    }
}