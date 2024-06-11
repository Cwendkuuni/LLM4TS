import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.jsoup.parser.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class CleanerTest {

    private Cleaner cleaner;

    @BeforeEach
    public void setup() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void clean_validDirtyDocument_returnsCleanDocument() {
        String dirtyHtml = "<html><head></head><body><p>Hello World!</p></body></html>";
        Document dirtyDocument = Document.createShell(dirtyHtml);
        
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertNotEquals(dirtyDocument, cleanDocument);
        assertFalse(cleanDocument.html().contains("<head>"));
    }

    @Test
    public void clean_dirtyDocumentWithUnsafeTags_removesUnsafeTags() {
        String dirtyHtml = "<html><head></head><body><p>Hello <script>alert('XSS');</script>World!</p></body></html>";
        Document dirtyDocument = Document.createShell(dirtyHtml);

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertNotEquals(dirtyDocument, cleanDocument);
        assertFalse(cleanDocument.html().contains("<script>"));
    }

    @Test
    public void clean_dirtyDocumentWithUnsafeAttributes_removesUnsafeAttributes() {
        String dirtyHtml = "<html><head></head><body><p id='test' onclick='alert(\"XSS\")'>Hello World!</p></body></html>";
        Document dirtyDocument = Document.createShell(dirtyHtml);

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertNotEquals(dirtyDocument, cleanDocument);
        assertFalse(cleanDocument.html().contains("onclick"));
    }

    @Test
    public void clean_dirtyDocumentWithUnsafeContent_removesUnsafeContent() {
        String dirtyHtml = "<html><head></head><body><p>Hello <![CDATA[<script>alert('XSS');</script>]]>World!</p></body></html>";
        Document dirtyDocument = Document.createShell(dirtyHtml);

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertNotEquals(dirtyDocument, cleanDocument);
        assertFalse(cleanDocument.html().contains("<script>"));
    }

    @Test
    public void clean_nullDirtyDocument_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> cleaner.clean(null));
    }

    @Test
    public void isValid_validDocument_returnsTrue() {
        String validHtml = "<html><head></head><body><p>Hello World!</p></body></html>";
        Document validDocument = Document.createShell(validHtml);

        boolean isValid = cleaner.isValid(validDocument);

        assertTrue(isValid);
    }

    @Test
    public void isValid_documentWithUnsafeTags_returnsFalse() {
        String dirtyHtml = "<html><head></head><body><p>Hello <script>alert('XSS');</script>World!</p></body></html>";
        Document dirtyDocument = Document.createShell(dirtyHtml);

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }

    @Test
    public void isValid_documentWithUnsafeAttributes_returnsFalse() {
        String dirtyHtml = "<html><head></head><body><p id='test' onclick='alert(\"XSS\")'>Hello World!</p></body></html>";
        Document dirtyDocument = Document.createShell(dirtyHtml);

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }

    @Test
    public void isValid_nullDirtyDocument_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> cleaner.isValid(null));
    }

    @Test
    public void copySafeNodes_sourceElementWithSafeTagsAndAttributes_copiesToDestination() {
        Element sourceElement = new Element(Tag.valueOf("div"), "");
        sourceElement.attr("class", "test");
        sourceElement.attr("id", "testId");
        sourceElement.text("Hello World!");
        
        Element destElement = new Element(Tag.valueOf("div"), "");

        int numDiscarded = cleaner.copySafeNodes(sourceElement, destElement);

        assertEquals(0, numDiscarded);
        assertEquals(sourceElement.html(), destElement.html());
    }

    @Test
    public void copySafeNodes_sourceElementWithUnsafeTags_removesUnsafeTags() {
        Element sourceElement = new Element(Tag.valueOf("div"), "");
        sourceElement.appendChild(new Element(Tag.valueOf("script"), "").text("alert('XSS');"));

        Element destElement = new Element(Tag.valueOf("div"), "");

        int numDiscarded = cleaner.copySafeNodes(sourceElement, destElement);

        assertEquals(1, numDiscarded);
        assertTrue(destElement.html().isEmpty());
    }

    @Test
    public void copySafeNodes_sourceTextNode_copiesToDestination() {
        TextNode sourceTextNode = new TextNode("Hello World!", "");
        
        Element destElement = new Element(Tag.valueOf("div"), "");

        int numDiscarded = cleaner.copySafeNodes(sourceTextNode, destElement);

        assertEquals(0, numDiscarded);
        assertEquals("Hello World!", destElement.html());
    }

    @Test
    public void copySafeNodes_nullSource_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> cleaner.copySafeNodes(null, new Element(Tag.valueOf("div"), "")));
    }

    @Test
    public void createSafeElement_sourceElementWithSafeAttributes_returnsSafeElementMeta() {
        String sourceTag = "div";
        Element sourceElement = new Element(Tag.valueOf(sourceTag), "");
        sourceElement.attr("class", "test");
        sourceElement.attr("id", "testId");
        
        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        assertNotNull(elementMeta);
        assertNotNull(elementMeta.el);
        assertEquals(sourceTag, elementMeta.el.tagName());
        assertEquals(2, elementMeta.el.attributes().size());
        assertEquals(Collections.emptyList(), elementMeta.numAttribsDiscarded);
    }

    @Test
    public void createSafeElement_sourceElementWithUnsafeAttributes_removesUnsafeAttributes() {
        String sourceTag = "div";
        Element sourceElement = new Element(Tag.valueOf(sourceTag), "");
        sourceElement.attr("onclick", "alert('XSS');");
        sourceElement.attr("href", "javascript:alert('XSS');");

        ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);

        assertNotNull(elementMeta);
        assertNotNull(elementMeta.el);
        assertEquals(sourceTag, elementMeta.el.tagName());
        assertEquals(0, elementMeta.el.attributes().size());
        assertEquals(2, elementMeta.numAttribsDiscarded);
    }

    @Test
    public void createSafeElement_nullSource_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> cleaner.createSafeElement(null));
    }

    @Test
    public void isValid_documentWithUnsafeTags_returnsFalse() {
        String dirtyHtml = "<html><head></head><body><p>Hello <script>alert('XSS');</script>World!</p></body></html>";
        Document dirtyDocument = Document.createShell(dirtyHtml);

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }

    @Test
    public void isValid_documentWithUnsafeAttributes_returnsFalse() {
        String dirtyHtml = "<html><head></head><body><p id='test' onclick='alert(\"XSS\")'>Hello World!</p></body></html>";
        Document dirtyDocument = Document.createShell(dirtyHtml);

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }

    @Test
    public void isValid_nullDirtyDocument_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> cleaner.isValid(null));
    }
}