import org.jsoup.nodes.*;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = new Document("http://www.example.com");
    }

    // Test for the constructor
    @Test
    public void testDocumentConstructor() {
        assertEquals("#root", document.tagName());
        assertEquals("http://www.example.com", document.baseUri());
    }

    // Test for the createShell method
    @Test
    public void testCreateShell() {
        Document shellDocument = Document.createShell("http://www.example.com");

        assertEquals("http://www.example.com", shellDocument.baseUri());
        assertEquals("html", shellDocument.child(0).tagName());
        assertEquals("head", shellDocument.child(0).child(0).tagName());
        assertEquals("body", shellDocument.child(0).child(1).tagName());
    }

    // Test for the head method
    @Test
    public void testHead() {
        assertNull(document.head());

        Element head = document.appendElement("head");

        assertEquals(head, document.head());
    }

    // Test for the body method
    @Test
    public void testBody() {
        assertNull(document.body());

        Element body = document.appendElement("body");

        assertEquals(body, document.body());
    }

    // Test for the title method
    @Test
    public void testTitle() {
        assertEquals("", document.title());

        Element titleEl = document.appendElement("title");
        titleEl.appendText("Example Title");

        assertEquals("Example Title", document.title());
    }

    // Test for the title method when title element is not present
    @Test
    public void testTitleNoElement() {
        assertEquals("", document.title());

        document.appendElement("head");

        assertEquals("", document.title());
    }

    // Test for the title method when title element is empty
    @Test
    public void testTitleEmptyElement() {
        Element titleEl = document.appendElement("title");

        assertEquals("", document.title());

        titleEl.appendText("  ");

        assertEquals("", document.title());
    }

    // Test for the title method when title element contains only whitespace
    @Test
    public void testTitleWhitespaceElement() {
        Element titleEl = document.appendElement("title");

        assertEquals("", document.title());

        titleEl.appendText("  Example Title  ");

        assertEquals("Example Title", document.title());
    }

    // Test for the title method when title element contains only non-whitespace characters
    @Test
    public void testTitleCharactersElement() {
        Element titleEl = document.appendElement("title");

        assertEquals("", document.title());

        titleEl.appendText("Example Title");

        assertEquals("Example Title", document.title());
    }

    // Test for the title method when title element is null
    @Test(expected = NullPointerException.class)
    public void testTitleNull() {
        document.title(null);
    }

    // Test for the title method when title element already exists
    @Test
    public void testTitleAlreadyExists() {
        Element head = document.appendElement("head");
        Element titleEl = head.appendElement("title");

        document.title("New Title");

        assertEquals("New Title", document.title());
        assertEquals(1, head.getElementsByTag("title").size());
        assertEquals(titleEl, head.getElementsByTag("title").first());
    }

    // Test for the createElement method
    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");

        assertEquals("div", element.tagName());
        assertEquals("http://www.example.com", element.baseUri());
    }

    // Test for the normalise method
    @Test
    public void testNormalise() {
        Document normalisedDoc = document.normalise();

        assertEquals("http://www.example.com", normalisedDoc.baseUri());
        assertNotNull(normalisedDoc.head());
        assertNotNull(normalisedDoc.body());
        assertEquals("html", normalisedDoc.child(0).tagName());
        assertEquals("head", normalisedDoc.head().tagName());
        assertEquals("body", normalisedDoc.body().tagName());

        Element root = document.appendElement("html").appendElement("head").appendElement("body");
        root.appendText("Text1");
        root.appendElement("div").appendText("Text2");

        normalisedDoc = document.normalise();

        Element body = normalisedDoc.body();
        assertEquals(2, body.childNodeSize());
        assertEquals("Text1 Text2", body.text());
    }

    // Test for the outerHtml method
    @Test
    public void testOuterHtml() {
        document.appendElement("div");

        assertEquals("<div></div>", document.outerHtml());
    }

    // Test for the text method
    @Test
    public void testText() {
        document.appendElement("body");

        document.text("Example Text");

        assertEquals("Example Text", document.body().text());
    }

    // Test for the text method when body element is not present
    @Test
    public void testTextNoBody() {
        document.appendElement("html");

        document.text("Example Text");

        assertNull(document.body());
    }

    // Test for the nodeName method
    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }

    // Test for the OutputSettings class
    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();

        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertEquals(true, outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
    }

    // Test for the OutputSettings class
    @Test
    public void testOutputSettingsSetters() {
        Document.OutputSettings outputSettings = document.outputSettings()
                .escapeMode(Entities.EscapeMode.extended)
                .charset("ISO-8859-1")
                .prettyPrint(false)
                .indentAmount(2);

        assertEquals(Entities.EscapeMode.extended, outputSettings.escapeMode());
        assertEquals(Charset.forName("ISO-8859-1"), outputSettings.charset());
        assertEquals(false, outputSettings.prettyPrint());
        assertEquals(2, outputSettings.indentAmount());
    }

}