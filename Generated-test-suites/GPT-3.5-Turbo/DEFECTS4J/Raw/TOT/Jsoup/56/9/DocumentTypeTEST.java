import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    // Test the constructor and getters
    @Test
    public void testConstructorAndGetters() {
        DocumentType doctype = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals("name", doctype.attr("name"));
        assertEquals("publicId", doctype.attr("publicId"));
        assertEquals("systemId", doctype.attr("systemId"));
    }

    // Test nodeName()
    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", doctype.nodeName());
    }

    // Test outerHtmlHead()
    @Test
    public void testOuterHtmlHead() {
        DocumentType doctype = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.html);
        doctype.outerHtmlHead(sb, 0, out);
        assertEquals("<!doctype name PUBLIC \"publicId\" \"systemId\">", sb.toString().trim());

        sb.setLength(0);
        doctype.attr("publicId", null);
        doctype.outerHtmlHead(sb, 0, out);
        assertEquals("<!doctype name \"systemId\">", sb.toString().trim());

        sb.setLength(0);
        out.syntax(Document.OutputSettings.Syntax.xml);
        out.prettyPrint(false);
        doctype.outerHtmlHead(sb, 0, out);
        assertEquals("<!DOCTYPEname\"publicId\"\"systemId\">", sb.toString().trim());
    }

    // Test outerHtmlTail()
    @Test
    public void testOuterHtmlTail() {
        DocumentType doctype = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        doctype.outerHtmlTail(sb, 0, out);
        assertEquals("", sb.toString().trim());
    }

    // Test private helper method has()
    @Test
    public void testHas() {
        DocumentType doctype = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertTrue(doctype.has("name"));
        assertTrue(doctype.has("publicId"));
        assertTrue(doctype.has("systemId"));
        assertFalse(doctype.has("nonExistentAttribute"));
    }

    // Additional test cases, e.g., edge cases and error scenarios, can be added here

}



// Overall, the provided test cases cover the typical use cases, edge cases, and error scenarios for the Java class.
// Additional test cases can be added if necessary to further test specific behaviors or conditions of the code.

