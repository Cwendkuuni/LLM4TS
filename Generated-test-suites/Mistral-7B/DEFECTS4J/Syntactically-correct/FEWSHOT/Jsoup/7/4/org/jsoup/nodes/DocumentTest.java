package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class DocumentTest {
    @Test
    public void testDocumentCreation() {
        Document document = new Document("baseUri");
        assertNotNull(document);
        assertEquals("#document", document.nodeName());

        Document shell = Document.createShell("baseUri");
        assertNotNull(shell);
        assertEquals("#document", shell.nodeName());
        assertNotNull(shell.head());
        assertNotNull(shell.body());
    }

    @Test
    public void testTitle() {
        Document document = new Document("baseUri");
        document.title("Title");
        assertEquals("Title", document.title());
    }

    @Test
    public void testTitleNull() {
        Document document = new Document("baseUri");
        assertNull(document.title());

        document.title(null);
        assertNull(document.title());
    }

    @Test
    public void testTitleEmpty() {
        Document document = new Document("baseUri");
        document.title("");
        assertEquals("", document.title());

        document.title("");
        assertEquals("", document.title());
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("baseUri");
        Element div = document.createElement("div");
        assertNotNull(div);
        assertEquals("div", div.nodeName());
        assertEquals(document.baseUri(), div.baseUri());
    }

    @Test
    public void testNormalise() {
        Document document = new Document("baseUri");
        Element html = document.createElement("html");
        document.appendChild(html);
        Element head = document.createElement("head");
        html.appendChild(head);
        Element body = document.createElement("body");
        html.appendChild(body);
        TextNode textNode1 = document.createTextNode("Text1");
        TextNode textNode2 = document.createTextNode("Text2");
        head.appendChild(textNode1);
        body.appendChild(textNode2);

        document.normalise();

        List<Node> bodyChildren = body.childNodes();
        assertEquals(2, bodyChildren.size());
        assertEquals("Text1", bodyChildren.get(0).nodeValue());
        assertEquals("Text2", bodyChildren.get(1).nodeValue());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document document = new Document("baseUri");
        Element html = document.createElement("html");
        document.appendChild(html);
        Element head = document.createElement("head");
        html.appendChild(head);
        Element body = document.createElement("body");
        html.appendChild(body);

        Element title = document.createElement("title");
        title.setText("Title");
        head.appendChild(title);

        Element div = document.createElement("div");
        body.appendChild(div);

        Element found = document.findFirstElementByTagName("title", document);
        assertEquals("Title", found.text());

        found = document.findFirstElementByTagName("div", html);
        assertNotNull(found);

        found = document.findFirstElementByTagName("title", null);
        assertNull(found);
    }

    @Test
    public void testOutputSettings() {
        Document document = new Document("baseUri");
        OutputSettings outputSettings = document.outputSettings();

        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals("UTF-8", outputSettings.charset().name());

        OutputSettings newOutputSettings = outputSettings.charset("ISO-8859-1");
        assertEquals("ISO-8859-1", newOutputSettings.charset().name());

        Document newDocument = new Document("baseUri").outputSettings(newOutputSettings);
        assertEquals("ISO-8859-1", newDocument.outputSettings().charset().name());
    }
}