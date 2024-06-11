import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {
    
    private Document document;
    
    @Before
    public void setUp() {
        document = new Document("https://example.com");
    }
    
    @Test
    public void testCreateShell_validBaseUri_returnsDocumentWithHtmlHeadAndBody() {
        Document shell = Document.createShell("https://example.com");
        assertNotNull(shell);
        assertEquals("html", shell.nodeName());
        assertNotNull(shell.head());
        assertNotNull(shell.body());
    }
    
    @Test
    public void testHead_noHeadElement_returnsNull() {
        assertNull(document.head());
    }
    
    @Test
    public void testHead_withHeadElement_returnsHeadElement() {
        Element headElement = document.createElement("head");
        document.appendChild(headElement);
        assertEquals(headElement, document.head());
    }
    
    @Test
    public void testBody_noBodyElement_returnsNull() {
        assertNull(document.body());
    }
    
    @Test
    public void testBody_withBodyElement_returnsBodyElement() {
        Element bodyElement = document.createElement("body");
        document.appendChild(bodyElement);
        assertEquals(bodyElement, document.body());
    }
    
    @Test
    public void testTitle_noTitleElement_returnsEmptyString() {
        String title = document.title();
        assertEquals("", title);
    }
    
    @Test
    public void testTitle_withTitleElement_returnsTitleText() {
        Element titleElement = document.createElement("title");
        titleElement.text("Test Title");
        document.head().appendChild(titleElement);
        String title = document.title();
        assertEquals("Test Title", title);
    }
    
    @Test
    public void testTitle_setTitleForExistingElement_updatesTitleText() {
        Element titleElement = document.createElement("title");
        document.head().appendChild(titleElement);
        document.title("Updated Title");
        assertEquals("Updated Title", titleElement.text());
    }
    
    @Test
    public void testTitle_setTitleForNonExistingElement_addsTitleToHead() {
        document.title("New Title");
        Element titleElement = document.head().getElementsByTag("title").first();
        assertNotNull(titleElement);
        assertEquals("New Title", titleElement.text());
    }
    
    @Test
    public void testCreateElement_validTagName_returnsNewElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals(document.baseUri(), element.baseUri());
    }
    
    @Test
    public void testNormalise_noHtmlElement_createsHtmlElement() {
        Document normalized = document.normalise();
        Element htmlElement = normalized.getElementsByTag("html").first();
        assertNotNull(htmlElement);
        assertEquals("html", htmlElement.nodeName());
    }
    
    @Test
    public void testNormalise_noHeadElement_createsHeadElementInHtml() {
        Document normalized = document.normalise();
        Element headElement = normalized.head();
        assertNotNull(headElement);
        assertEquals("head", headElement.nodeName());
        assertEquals("html", headElement.parent().nodeName());
    }
    
    @Test
    public void testNormalise_noBodyElement_createsBodyElementInHtml() {
        Document normalized = document.normalise();
        Element bodyElement = normalized.body();
        assertNotNull(bodyElement);
        assertEquals("body", bodyElement.nodeName());
        assertEquals("html", bodyElement.parent().nodeName());
    }
    
    // Test case for normaliseTextNodes() is not provided as it is a private method and not accessible for testing
    
    @Test
    public void testOuterHtml_returnsHtmlContent() {
        String outerHtml = document.outerHtml();
        assertNotNull(outerHtml);
        assertTrue(outerHtml.contains("<html"));
        assertTrue(outerHtml.contains("<head"));
        assertTrue(outerHtml.contains("<body"));
        assertFalse(outerHtml.contains("#document"));
    }
    
    @Test
    public void testText_setsTextInBodyElement() {
        document.text("Test Text");
        assertEquals("Test Text", document.body().text());
    }
    
    @Test
    public void testNodeName_returnsDocument() {
        assertEquals("#document", document.nodeName());
    }
    
    @Test
    public void testOutputSettings_escapeMode_returnsBaseEscapeModeByDefault() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
    }
    
    @Test
    public void testOutputSettings_charset_returnsUTF8ByDefault() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
    }
    
    @Test
    public void testOutputSettings_prettyPrint_returnsTrueByDefault() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertTrue(outputSettings.prettyPrint());
    }
    
    @Test
    public void testOutputSettings_indentAmount_returnsOneByDefault() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertEquals(1, outputSettings.indentAmount());
    }

}