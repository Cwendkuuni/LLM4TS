import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DocumentTest {
    private Document document;

    @Before
    public void setUp() {
        document = new Document("");
    }

    @Test
    public void testConstructor() {
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "";
        Document shell = Document.createShell(baseUri);

        assertNotNull(shell.head());
        assertNotNull(shell.body());

        Element html = shell.getElementsByTag("html").first();
        assertNotNull(html);

        Element head = html.getElementsByTag("head").first();
        assertNotNull(head);

        Element body = html.getElementsByTag("body").first();
        assertNotNull(body);
    }

    @Test
    public void testHead() {
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        String title = document.title();
        assertEquals("", title);

        document.title("Test Title");
        title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testTitleWithExistingTitleElement() {
        Element titleElement = new Element(Tag.valueOf("title"), "");
        titleElement.text("Existing Title");
        document.head().appendChild(titleElement);

        String title = document.title();
        assertEquals("Existing Title", title);

        document.title("New Title");
        title = document.title();
        assertEquals("New Title", title);
    }

    @Test
    public void testTitleWithNull() {
        document.title(null);
        String title = document.title();
        assertEquals("", title);
    }

    @Test
    public void testCreateElement() {
        String tagName = "div";
        Element element = document.createElement(tagName);

        assertNotNull(element);
        assertEquals(tagName, element.nodeName());
    }

    @Test
    public void testNormalise() {
        Element html = new Element(Tag.valueOf("html"), "");
        document.appendChild(html);

        document.normalise();

        assertNotNull(document.head());
        assertNotNull(document.body());
        assertEquals(1, document.childNodes().size());

        Element normalizedHtml = (Element) document.childNodes().get(0);
        assertEquals("html", normalizedHtml.nodeName());
        assertNotNull(normalizedHtml.getElementsByTag("head").first());
        assertNotNull(normalizedHtml.getElementsByTag("body").first());
    }

    @Test
    public void testNormaliseWithExistingElements() {
        Element head = new Element(Tag.valueOf("head"), "");
        Element body = new Element(Tag.valueOf("body"), "");
        Element html = new Element(Tag.valueOf("html"), "");

        document.appendChild(head);
        document.appendChild(body);
        document.appendChild(html);

        document.normalise();

        assertNotNull(document.head());
        assertNotNull(document.body());
        assertEquals(1, document.childNodes().size());

        Element normalizedHtml = (Element) document.childNodes().get(0);
        assertEquals("html", normalizedHtml.nodeName());
        assertEquals(head, normalizedHtml.getElementsByTag("head").first());
        assertEquals(body, normalizedHtml.getElementsByTag("body").first());
    }

    @Test
    public void testNormaliseTextNodes() {
        Element div = new Element(Tag.valueOf("div"), "");
        Element p = new Element(Tag.valueOf("p"), "");
        TextNode textNode1 = new TextNode("Text 1", "");
        TextNode textNode2 = new TextNode("Text 2", "");
        div.appendChild(textNode1);
        p.appendChild(textNode2);

        document.appendChild(div);
        document.appendChild(p);

        document.normalise();

        assertEquals(2, document.childNodes().size());
        assertEquals(2, document.body().childNodes().size());
        assertEquals(" ", document.body().childNodes().get(0).outerHtml());
        assertEquals("Text 1", document.body().childNodes().get(1).outerHtml());
        assertEquals("Text 2", document.body().childNodes().get(2).outerHtml());
    }

    @Test
    public void testOuterHtml() {
        Element div = new Element(Tag.valueOf("div"), "");
        div.appendChild(new TextNode("Test", ""));
        document.appendChild(div);

        String outerHtml = document.outerHtml();
        assertEquals("<div>Test</div>", outerHtml);
    }

    @Test
    public void testText() {
        String text = "Test";
        document.text(text);

        String outerHtml = document.outerHtml();
        assertEquals("<body>" + text + "</body>", outerHtml);
    }

    @Test
    public void testNodeName() {
        String nodeName = document.nodeName();
        assertEquals("#document", nodeName);
    }

    @Test
    public void testOutputSettingsEscapeMode() {
        Document.OutputSettings outputSettings = document.outputSettings();

        Entities.EscapeMode escapeMode = outputSettings.escapeMode();
        assertEquals(Entities.EscapeMode.base, escapeMode);

        escapeMode = Entities.EscapeMode.extended;
        outputSettings.escapeMode(escapeMode);

        escapeMode = outputSettings.escapeMode();
        assertEquals(Entities.EscapeMode.extended, escapeMode);
    }

    @Test
    public void testOutputSettingsCharset() {
        Document.OutputSettings outputSettings = document.outputSettings();

        Charset charset = outputSettings.charset();
        assertEquals(Charset.forName("UTF-8"), charset);

        charset = Charset.forName("ISO-8859-1");
        outputSettings.charset(charset);

        charset = outputSettings.charset();
        assertEquals(Charset.forName("ISO-8859-1"), charset);
    }

    @Test
    public void testOutputSettingsPrettyPrint() {
        Document.OutputSettings outputSettings = document.outputSettings();

        boolean prettyPrint = outputSettings.prettyPrint();
        assertEquals(true, prettyPrint);

        prettyPrint = false;
        outputSettings.prettyPrint(prettyPrint);

        prettyPrint = outputSettings.prettyPrint();
        assertEquals(false, prettyPrint);
    }

    @Test
    public void testOutputSettingsIndentAmount() {
        Document.OutputSettings outputSettings = document.outputSettings();

        int indentAmount = outputSettings.indentAmount();
        assertEquals(1, indentAmount);

        indentAmount = 4;
        outputSettings.indentAmount(indentAmount);

        indentAmount = outputSettings.indentAmount();
        assertEquals(4, indentAmount);
    }

    @Test
    public void testOutputSettingsCharsetEncoder() {
        Document.OutputSettings outputSettings = document.outputSettings();

        CharsetEncoder charsetEncoder = outputSettings.encoder();
        assertNotNull(charsetEncoder);
    }

    @Test
    public void testOutputSettingsCharsetWithString() {
        Document.OutputSettings outputSettings = document.outputSettings();

        Charset charset = Charset.forName("ISO-8859-1");
        outputSettings.charset(charset.toString());

        CharsetEncoder charsetEncoder = outputSettings.encoder();
        assertNotNull(charsetEncoder);
        assertEquals(charset, charsetEncoder.charset());
    }

    @Test
    public void testOutputSettingsCharsetWithStringInvalidCharset() {
        Document.OutputSettings outputSettings = document.outputSettings();

        Charset charset = Charset.forName("INVALID_CHARSET");
        outputSettings.charset(charset.toString());

        CharsetEncoder charsetEncoder = outputSettings.encoder();
        assertNotNull(charsetEncoder);
        assertEquals(Charset.forName("UTF-8"), charsetEncoder.charset());
    }

    @Test
    public void testOutputSettingsCharsetWithNullCharset() {
        Document.OutputSettings outputSettings = document.outputSettings();

        outputSettings.charset((Charset) null);

        CharsetEncoder charsetEncoder = outputSettings.encoder();
        assertNotNull(charsetEncoder);
        assertEquals(Charset.forName("UTF-8"), charsetEncoder.charset());
    }
}