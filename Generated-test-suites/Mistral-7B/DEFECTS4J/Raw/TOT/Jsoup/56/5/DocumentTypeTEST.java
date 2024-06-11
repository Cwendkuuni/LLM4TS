import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        // Test case for creating a DocumentType object with valid arguments
        DocumentType documentType = new DocumentType("html", "HTML", "HTML5", "http://www.example.com");
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr(DocumentType.NAME));
        assertEquals("HTML", documentType.attr(DocumentType.PUBLIC_ID));
        assertEquals("HTML5", documentType.attr(DocumentType.SYSTEM_ID));
        assertEquals("http://www.example.com", documentType.baseUri());

        // Test case for creating a DocumentType object with empty public ID and system ID
        DocumentType documentTypeEmpty = new DocumentType("html", "", "", "http://www.example.com");
        assertEquals("#doctype", documentTypeEmpty.nodeName());
        assertEquals("html", documentTypeEmpty.attr(DocumentType.NAME));
        assertEquals("", documentTypeEmpty.attr(DocumentType.PUBLIC_ID));
        assertEquals("", documentTypeEmpty.attr(DocumentType.SYSTEM_ID));
        assertEquals("http://www.example.com", documentTypeEmpty.baseUri());
    }
}