package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;

public class DocumentTest {
    private Document document;

    @Before
    public void setup() {
        document = new Document("http://example.com");
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://example.com");
        assertNotNull(shell.head());
        assertNotNull(shell.body());
        assertEquals("html", shell.head().parent().nodeName());
        assertEquals("html", shell.body().parent().nodeName());

        Element head = shell.head();
        Element body = shell.body();
        int headElementCountBefore = head.children().size();
        int bodyElementCountBefore = body.children().size();

        shell.appendElement("meta");
        shell.appendElement("title");
        shell.appendElement("link");

        shell = Document.createShell("http://example.com");
        head = shell.head();
        body = shell.body();
        int headElementCountAfter = head.children().size();
        int bodyElementCountAfter = body.children().size();

        assertEquals(headElementCountBefore, headElementCountAfter);
        assertEquals(bodyElementCountBefore, bodyElementCountAfter);
    }

    @Test
    public void testHead() {
        Element head = document.head();
        assertNull(head);

        document.appendElement("head");
        head = document.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());

        document.prependElement("meta");
        head = document.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());

        document.prependElement("meta");
        head = document.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Element body = document.body();
        assertNull(body);

        document.appendElement("body");
        body = document.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());

        document.prependElement("div");
        body = document.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());

        document.prependElement("div");
        body = document.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        String title = document.title();
        assertEquals("", title);

        document.appendElement("head").appendElement("title").text("Test Title");
        title = document.title();
        assertEquals("Test Title", title);

        document.title("New Title");
        title = document.title();
        assertEquals("New Title", title);
    }

    @Test
    public void testCreateElement() {
        Element div = document.createElement("div");
        assertNotNull(div);
        assertEquals("div", div.nodeName());
        assertNotNull(div.parent());
        assertEquals("#root", div.parent().nodeName());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://example.com");
        Element html = doc.getElementsByTag("html").first();
        assertNull(html);
        Element head = doc.head();
        assertNull(head);
        Element body = doc.body();
        assertNull(body);

        doc.normalise();
        html = doc.getElementsByTag("html").first();
        assertNotNull(html);
        head = doc.head();
        assertNotNull(head);
        body = doc.body();
        assertNotNull(body);

        assertEquals("html", html.nodeName());
        assertEquals("head", head.nodeName());
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testNormaliseTextNodes() {
        Element div = document.createElement("div");
        TextNode textNode = new TextNode("Text");
        Element span = document.createElement("span");
        span.appendText(" inside ");
        span.appendText("span");
        div.appendChild(textNode);
        div.appendChild(span);
        div.appendChild(new Comment("comment"));
        div.appendChild(new DataNode("data", "http://example.com", new Attributes()));
        div.appendChild(new CDataNode("cdata"));

        document.body().appendChild(div);
        document.normalise();

        List<Node> textNodes = new ArrayList<>();
        for (Node node : document.body().childNodes()) {
            if (node instanceof TextNode)
                textNodes.add(node);
        }

        assertEquals(3, textNodes.size());
        assertTrue(textNodes.get(0) instanceof TextNode);
        assertEquals("Text", ((TextNode) textNodes.get(0)).getWholeText());

        assertTrue(textNodes.get(1) instanceof TextNode);
        assertEquals(" inside ", ((TextNode) textNodes.get(1)).getWholeText());

        assertTrue(textNodes.get(2) instanceof TextNode);
        assertEquals("span", ((TextNode) textNodes.get(2)).getWholeText());
    }

    @Test
    public void testOuterHtml() {
        String html = document.outerHtml();
        assertEquals("", html);

        document.appendElement("div").attr("class", "container").appendElement("p").text("Example Paragraph");
        html = document.outerHtml();
        assertEquals("<div class=\"container\"><p>Example Paragraph</p></div>", html);
    }

    @Test
    public void testText() {
        document.appendElement("body");
        document.text("Example Text");
        Element body = document.body();
        assertEquals("Example Text", body.text());
    }

    @Test
    public void testNodeName() {
        String nodeName = document.nodeName();
        assertEquals("#document", nodeName);
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);

        Entities.EscapeMode escapeMode = outputSettings.escapeMode();
        assertNotNull(escapeMode);
        assertEquals(Entities.EscapeMode.base, escapeMode);

        Charset charset = outputSettings.charset();
        assertNotNull(charset);
        assertEquals(Charset.forName("UTF-8"), charset);

        CharsetEncoder charsetEncoder = outputSettings.encoder();
        assertNotNull(charsetEncoder);

        boolean prettyPrint = outputSettings.prettyPrint();
        assertTrue(prettyPrint);

        int indentAmount = outputSettings.indentAmount();
        assertEquals(1, indentAmount);

        outputSettings.escapeMode(Entities.EscapeMode.extended);
        escapeMode = outputSettings.escapeMode();
        assertNotNull(escapeMode);
        assertEquals(Entities.EscapeMode.extended, escapeMode);

        outputSettings.charset(Charset.forName("ISO-8859-1"));
        charset = outputSettings.charset();
        assertNotNull(charset);
        assertEquals(Charset.forName("ISO-8859-1"), charset);

        outputSettings.charset("UTF-16");
        charset = outputSettings.charset();
        assertNotNull(charset);
        assertEquals(Charset.forName("UTF-16"), charset);

        outputSettings.prettyPrint(false);
        prettyPrint = outputSettings.prettyPrint();
        assertTrue(!prettyPrint);

        outputSettings.indentAmount(4);
        indentAmount = outputSettings.indentAmount();
        assertEquals(4, indentAmount);
    }
}