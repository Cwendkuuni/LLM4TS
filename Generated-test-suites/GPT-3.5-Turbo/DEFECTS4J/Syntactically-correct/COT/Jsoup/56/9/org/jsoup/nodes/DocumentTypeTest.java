
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("name", "publicId", "systemId", "");
        String expected = "#doctype";
        String actual = docType.nodeName();
        assertEquals(expected, actual);
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType docType = new DocumentType("name", "publicId", "systemId", "");
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings settings = new Document.OutputSettings();
        try {
            docType.outerHtmlHead(sb, 0, settings);
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
        String expected = "<!DOCTYPE name PUBLIC \"publicId\" \"systemId\">";
        String actual = sb.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("name", "publicId", "systemId", "");
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings settings = new Document.OutputSettings();
        docType.outerHtmlTail(sb, 0, settings);
        String expected = "";
        String actual = sb.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testHas() {
        DocumentType docType = new DocumentType("name", "publicId", "systemId", "");
        boolean expected = true;
        boolean actual = docType.has("name");
        assertEquals(expected, actual);

        expected = false;
        actual = docType.has("invalidAttr");
        assertEquals(expected, actual);
    }

}
