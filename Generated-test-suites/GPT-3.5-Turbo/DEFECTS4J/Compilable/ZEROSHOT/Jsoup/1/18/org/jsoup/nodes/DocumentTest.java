package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document document = new Document("https://www.example.com");
        assertNotNull(document);
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("https://www.example.com");
        assertNotNull(document);
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testHead() {
        Document document = Document.createShell("https://www.example.com");
        assertNotNull(document.head());
    }

    @Test
    public void testBody() {
        Document document = Document.createShell("https://www.example.com");
        assertNotNull(document.body());
    }

    @Test
    public void testTitle() {
        Document document = Document.createShell("https://www.example.com");
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testTitleNoExistingElement() {
        Document document = Document.createShell("https://www.example.com");
        assertEquals("", document.title());
    }

    @Test
    public void testTitleNullTitle() {
        Document document = Document.createShell("https://www.example.com");
        document.title(null);
        assertEquals("", document.title());
    }

    @Test
    public void testTitleExistingElement() {
        Document document = Document.createShell("https://www.example.com");
        document.title("Old Title");
        document.title("New Title");
        assertEquals("New Title", document.title());
    }

    @Test
    public void testcreateElement() {
        Document document = Document.createShell("https://www.example.com");
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals("https://www.example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document document = Document.createShell("https://www.example.com");
        document.appendElement("p").text("Paragraph 1");
        document.appendElement("p").text("Paragraph 2");
        document.appendElement("p").text("Paragraph 3");
        document.normalise();
        assertEquals("Paragraph 1", document.body().child(0).text());
        assertEquals("Paragraph 2", document.body().child(1).text());
        assertEquals("Paragraph 3", document.body().child(2).text());
    }

    @Test
    public void testouterHtml() {
        Document document = Document.createShell("https://www.example.com");
        document.appendElement("p").text("Paragraph 1");
        document.appendElement("p").text("Paragraph 2");
        assertEquals("<p>Paragraph 1</p><p>Paragraph 2</p>", document.outerHtml());
    }

    @Test
    public void testtext() {
        Document document = Document.createShell("https://www.example.com");
        document.text("Test Text");
        assertEquals("Test Text", document.body().text());
    }

    @Test
    public void testnodeName() {
        Document document = Document.createShell("https://www.example.com");
        assertEquals("#document", document.nodeName());
    }

}