package org.jsoup.nodes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = Document.createShell("http://example.com");
    }

    @Test
    public void testHead() {
        Element head = document.head();
        assertNotNull("Head element should not be null", head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Element body = document.body();
        assertNotNull("Body element should not be null", body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        String title = "Test Title";
        document.title(title);
        assertEquals(title, document.title());
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull("Created element should not be null", element);
        assertEquals("div", element.nodeName());
    }

    @Test
    public void testNormalise() {
        String text = "Test Text";
        document.text(text);
        document.normalise();
        assertEquals(text, document.body().text().trim());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(false);
        document.outputSettings(outputSettings);
        assertEquals(outputSettings, document.outputSettings());
    }

}