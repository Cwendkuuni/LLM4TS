import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testNodeName() {
        // create DocumentType object
        DocumentType documentType = new DocumentType("doctype", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");

        // test nodeName() method
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        // create DocumentType object
        DocumentType documentType = new DocumentType("doctype", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");

        // create StringBuilder to store output
        StringBuilder stringBuilder = new StringBuilder();

        // create Document.OutputSettings object
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        // test outerHtmlHead() method
        documentType.outerHtmlHead(stringBuilder, 0, outputSettings);

        // check the output
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" http://www.w3.org/TR/html4/strict.dtd\">", stringBuilder.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        // create DocumentType object
        DocumentType documentType = new DocumentType("doctype", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");

        // create StringBuilder to store output
        StringBuilder stringBuilder = new StringBuilder();

        // create Document.OutputSettings object
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        // test outerHtmlTail() method
        documentType.outerHtmlTail(stringBuilder, 0, outputSettings);

        // check the output (expect empty string)
        assertEquals("", stringBuilder.toString());
    }

    // Additional test cases for edge cases and exceptions

    @Test
    public void testConstructorEmptyValues() {
        // create DocumentType object with empty values
        DocumentType documentType = new DocumentType("", "", "", "");

        // test nodeName() method with empty values
        assertEquals("#doctype", documentType.nodeName());
        
        // test outerHtmlHead() method with empty values
        StringBuilder stringBuilder = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        documentType.outerHtmlHead(stringBuilder, 0, outputSettings);
        assertEquals("<!DOCTYPE html>", stringBuilder.toString());
    }

    @Test
    public void testConstructorNullValues() {
        // create DocumentType object with null values
        DocumentType documentType = new DocumentType(null, null, null, null);

        // test nodeName() method with null values
        assertEquals("#doctype", documentType.nodeName());

        // test outerHtmlHead() method with null values
        StringBuilder stringBuilder = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        documentType.outerHtmlHead(stringBuilder, 0, outputSettings);
        assertEquals("<!DOCTYPE html>", stringBuilder.toString());
    }
}