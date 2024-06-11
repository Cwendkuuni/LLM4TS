import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {
    private Document document;

    @Before
    public void setUp() {
        document = new Document("http://example.com");
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://example.com");

        assertNotNull(shell.head());
        assertNotNull(shell.body());
        assertEquals("html", shell.head().parent().nodeName());
        assertEquals("html", shell.body().parent().nodeName());
    }

    @Test
    public void testHead() {
        assertNull(document.head());

        Element head = document.createElement("head");
        document.appendChild(head);

        assertEquals(head, document.head());
    }

    @Test
    public void testBody() {
        assertNull(document.body());

        Element body = document.createElement("body");
        document.appendChild(body);

        assertEquals(body, document.body());
    }

    @Test
    public void testTitle() {
        String title = "Example Title";
        document.title(title);

        assertEquals(title, document.title());
    }

    @Test
    public void testTitle_NoTitleElement() {
        String title = "Example Title";

        document.title(title);

        Element titleElement = document.getElementsByTag("title").first();
        assertNotNull(titleElement);
        assertEquals(title, titleElement.text());
    }

    @Test
    public void testTitle_ExistingTitleElement() {
        String existingTitle = "Existing Title";
        String newTitle = "New Title";

        Element titleElement = document.createElement("title");
        titleElement.text(existingTitle);
        document.head().appendChild(titleElement);

        document.title(newTitle);

        assertEquals(newTitle, titleElement.text());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://example.com");

        Element p = doc.createElement("p");
        p.appendChild(doc.createElement("span").text("Hello, "));
        p.appendChild(doc.createElement("span").text("World!"));

        doc.body().appendChild(p);

        doc.normalise();

        assertEquals("Hello, World!", doc.body().text());
    }

    @Test
    public void testFirstChildByTagName_ExistingElement() {
        Element head = document.createElement("head");
        document.appendChild(head);

        assertEquals(head, document.findFirstElementByTagName("head", document));
    }

    @Test
    public void testFirstChildByTagName_NonExistingElement() {
        assertNull(document.findFirstElementByTagName("head", document));
    }

    @Test
    public void testOuterHtml() {
        assertEquals("", document.outerHtml());

        document.appendElement("div");

        assertEquals("<div></div>", document.outerHtml());
    }

    @Test
    public void testText() {
        String text = "Example Text";

        document.text(text);

        assertEquals(text, document.body().text());
    }

    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();

        assertNotNull(outputSettings);
        assertNotNull(outputSettings.escapeMode());
        assertNotNull(outputSettings.charset());
        assertNotNull(outputSettings.prettyPrint());
        assertNotNull(outputSettings.indentAmount());
    }
}