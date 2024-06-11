import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DocumentTest {
    private Document document;

    @Before
    public void setUp() {
        document = new Document("http://example.com");
    }

    //------------------------Document(String baseUri)----------------------

    //Typical Use Case
    @Test
    public void testDocumentConstructor_withValidInput() {
        Document document = new Document("http://example.com");
        assertEquals("#root", document.tagName());
        assertEquals("http://example.com", document.baseUri());
    }

    //Edge Case
    @Test
    public void testDocumentConstructor_withEmptyBaseUri() {
        Document document = new Document("");
        assertEquals("#root", document.tagName());
        assertEquals("", document.baseUri());
    }

    //------------------------createShell(String baseUri)----------------------

    //Typical Use Case
    @Test
    public void testCreateShell_withValidInputs() {
        Document document = Document.createShell("http://example.com");
        assertEquals("html", document.tagName());
        assertEquals("http://example.com", document.baseUri());
        assertEquals("head", document.head().tagName());
        assertEquals("body", document.body().tagName());
    }

    //Edge Case
    @Test(expected = NullPointerException.class)
    public void testCreateShell_withNullBaseUri() {
        Document.createShell(null);
    }

    //------------------------head()----------------------

    //Typical Use Case
    @Test
    public void testHead_withHeadElementPresent() {
        Element headElement = document.createElement("head");
        document.appendChild(headElement);
        assertEquals(headElement, document.head());
    }

    //Edge Case
    @Test
    public void testHead_withNoHeadElement() {
        assertNull(document.head());
    }

    //------------------------body()----------------------

    //Typical Use Case
    @Test
    public void testBody_withBodyElementPresent() {
        Element bodyElement = document.createElement("body");
        document.appendChild(bodyElement);
        assertEquals(bodyElement, document.body());
    }

    //Edge Case
    @Test
    public void testBody_withNoBodyElement() {
        assertNull(document.body());
    }

    //------------------------title()----------------------

    //Typical Use Case
    @Test
    public void testTitle_withTitleElementPresent() {
        Element titleElement = document.createElement("title");
        titleElement.text("Example Title");
        document.head().appendChild(titleElement);
        assertEquals("Example Title", document.title());
    }

    //Edge Case
    @Test
    public void testTitle_withNoTitleElement() {
        assertEquals("", document.title());
    }

    //------------------------title(String title)----------------------

    //Typical Use Case
    @Test
    public void testSetTitle_withTitleElementPresent() {
        Element titleElement = document.createElement("title");
        document.head().appendChild(titleElement);

        document.title("New Title");
        assertEquals("New Title", titleElement.text());
    }

    //Edge Case
    @Test
    public void testSetTitle_withNoTitleElement() {
        document.title("New Title");
        assertEquals("New Title", document.head().getElementsByTag("title").first().text());
    }

    //------------------------createElement(String tagName)----------------------

    //Typical Use Case
    @Test
    public void testCreateElement_withValidTagName() {
        Element element = document.createElement("div");
        assertEquals("div", element.tagName());
        assertEquals("http://example.com", element.baseUri());
    }

    //------------------------normalise()----------------------

    //Typical Use Case
    @Test
    public void testNormalise_withHtmlAndBodyElementsPresent() {
        Element htmlElement = document.createElement("html");
        Element bodyElement = document.createElement("body");
        Element divElement = document.createElement("div");

        bodyElement.appendChild(divElement);
        htmlElement.appendChild(bodyElement);
        document.appendChild(htmlElement);

        document.normalise();

        assertEquals(bodyElement, document.body());
        assertEquals(divElement, document.body().child(0));
        assertEquals(0, htmlElement.children().size());
    }

    //Edge Case
    @Test
    public void testNormalise_withNoHtmlElement() {
        document.normalise();
        assertEquals(1, document.children().size());
    }

    //------------------------text(String text)----------------------

    //Typical Use Case
    @Test
    public void testSetText_withValidText() {
        document.text("Example Text");
        assertEquals("Example Text", document.body().text());
    }

    //------------------------nodeName()----------------------

    //Typical Use Case
    @Test
    public void testNodeName_withDocumentNode() {
        assertEquals("#document", document.nodeName());
    }

    //------------------------clone()----------------------

    //Typical Use Case
    @Test
    public void testClone_withClonedDocument() {
        Document clonedDocument = document.clone();
        assertEquals(document.tagName(), clonedDocument.tagName());
        assertEquals(document.baseUri(), clonedDocument.baseUri());
        assertEquals(document.outputSettings().escapeMode(), clonedDocument.outputSettings().escapeMode());
    }

    //------------------------outputSettings()----------------------

    //Typical Use Case
    @Test
    public void testOutputSettings_withDefaultSettings() {
        Document.OutputSettings defaultSettings = new Document.OutputSettings();
        assertEquals(defaultSettings.charset(), document.outputSettings().charset());
        assertEquals(defaultSettings.escapeMode(), document.outputSettings().escapeMode());
        assertEquals(defaultSettings.prettyPrint(), document.outputSettings().prettyPrint());
        assertEquals(defaultSettings.indentAmount(), document.outputSettings().indentAmount());
    }

    //Edge Case
    @Test
    public void testOutputSettings_withCustomSettings() {
        Document.OutputSettings customSettings = new Document.OutputSettings();
        customSettings.charset("UTF-16");
        customSettings.prettyPrint(false);
        customSettings.indentAmount(2);

        document.outputSettings(customSettings);

        assertEquals(customSettings.charset(), document.outputSettings().charset());
        assertEquals(customSettings.prettyPrint(), document.outputSettings().prettyPrint());
        assertEquals(customSettings.indentAmount(), document.outputSettings().indentAmount());
    }

    //------------------------quirksMode()----------------------

    //Typical Use Case
    @Test
    public void testQuirksMode_withDefaultMode() {
        assertEquals(Document.QuirksMode.noQuirks, document.quirksMode());
    }

    //Edge Case
    @Test
    public void testQuirksMode_withSetMode() {
        document.quirksMode(Document.QuirksMode.quirks);
        assertEquals(Document.QuirksMode.quirks, document.quirksMode());
    }

}