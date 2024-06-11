import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        String baseUri = "http://example.com";
        document = new Document(baseUri);
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://example.com");

        assertEquals("http://example.com", shell.baseUri());
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
        assertNull(head);

        Element newHead = document.createElement("head");
        document.appendChild(newHead);

        head = document.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Element body = document.body();
        assertNull(body);

        Element newBody = document.createElement("body");
        document.appendChild(newBody);

        body = document.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        String title = document.title();
        assertEquals("", title);

        document.head().appendElement("title").text("Test Title");

        title = document.title();
        assertEquals("Test Title", title);

        document.title("New Title");

        title = document.title();
        assertEquals("New Title", title);
    }

    @Test
    public void testTitle_nullTitle() {
        try {
            document.title(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("title must not be null", e.getMessage());
        }
    }

    @Test
    public void testTitle_noHead() {
        document.head().remove();

        document.title("Title with No Head");

        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());

        String title = document.title();
        assertEquals("Title with No Head", title);
    }

    @Test
    public void testcreateElement() {
        Element element = document.createElement("div");

        assertNotNull(element);
        assertEquals("div", element.nodeName());
    }

    @Test
    public void testnormalise() {
        Element htmlEl = document.getElementsByTag("html").first();
        assertNull(htmlEl);

        document.normalise();

        htmlEl = document.getElementsByTag("html").first();
        assertNotNull(htmlEl);

        Element head = document.head();
        assertNotNull(head);

        Element body = document.body();
        assertNotNull(body);

        List<Node> textNodes = new ArrayList<>();
        for (Element element: document.getAllElements()) {
            for (Node childNode: element.childNodes()) {
                if (childNode instanceof TextNode) {
                    textNodes.add(childNode);
                }
            }
        }

        for (Node textNode: textNodes) {
            if (!textNode.isBlank()) {
                assertFalse(textNode.parentNode().equals(document));
                assertTrue(textNode.parentNode().equals(body));

                Element firstChild = body.child(0);
                assertEquals(" ", firstChild.outerHtml());

                assertTrue(firstChild.nextSibling().equals(textNode));
            }
        }
    }

    @Test
    public void testouterHtml() {
        String outerHtml = document.outerHtml();
        assertEquals("", outerHtml);

        document.appendElement("div");

        outerHtml = document.outerHtml();
        assertEquals("<div></div>", outerHtml);
    }

    @Test
    public void testtext() {
        document.text("Hello World");

        Element body = document.body();
        assertEquals("Hello World", body.text());
    }

    @Test
    public void testnodeName() {
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);

        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());

        outputSettings.escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, outputSettings.escapeMode());

        CharsetEncoder charsetEncoder = Charset.forName("ISO-8859-1").newEncoder();
        outputSettings.charset(Charset.forName("ISO-8859-1"));
        assertEquals(Charset.forName("ISO-8859-1"), outputSettings.charset());
        assertEquals(charsetEncoder, outputSettings.encoder());

        outputSettings.prettyPrint(false);
        assertFalse(outputSettings.prettyPrint());

        outputSettings.indentAmount(2);
        assertEquals(2, outputSettings.indentAmount());
    }

    @Test
    public void testOutputSettings_charsetByName() {
        Document.OutputSettings outputSettings = document.outputSettings();

        outputSettings.charset("ISO-8859-1");
        assertEquals(Charset.forName("ISO-8859-1"), outputSettings.charset());
    }

    @Test
    public void testOutputSettings_indentAmount_lessThanZero() {
        Document.OutputSettings outputSettings = document.outputSettings();

        try {
            outputSettings.indentAmount(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("indentAmount must be >= 0", e.getMessage());
        }
    }

    @Test
    public void testOutputSettings_prettyPrint_false() {
        Document.OutputSettings outputSettings = document.outputSettings();

        outputSettings.prettyPrint(false);

        String html = "<html><head></head><body><div></div></body></html>";
        document.append(html);

        String expectedOutput = "<html><head></head><body><div></div></body></html>";
        String output = document.html();
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testOutputSettings_prettyPrint_true() {
        Document.OutputSettings outputSettings = document.outputSettings();

        outputSettings.prettyPrint(true);

        String html = "<html><head></head><body><div></div></body></html>";
        document.append(html);

        String expectedOutput = "<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <div></div>\n" +
                " </body>\n" +
                "</html>";
        String output = document.html();
        assertEquals(expectedOutput, output);
    }
}