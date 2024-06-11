import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testConstructor() {
        DocumentType doctype = new DocumentType("doctype", "pubId", "sysId", "baseUri");

        assertEquals("doctype", doctype.attr("name"));
        assertEquals("pubId", doctype.attr("publicId"));
        assertEquals("sysId", doctype.attr("systemId"));
        assertEquals("baseUri", doctype.baseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("doctype", "pubId", "sysId", "baseUri");

        assertEquals("#doctype", doctype.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType doctype1 = new DocumentType("doctype", "", "sysId", "baseUri");
        DocumentType doctype2 = new DocumentType("doctype", "pubId", "", "baseUri");
        DocumentType doctype3 = new DocumentType("doctype", "", "", "baseUri");
        DocumentType doctype4 = new DocumentType("doctype", "pubId", "sysId", "baseUri");

        assertEquals("<!doctype doctype>", doctype1.outerHtml());
        assertEquals("<!DOCTYPE doctype PUBLIC \"pubId\">", doctype2.outerHtml());
        assertEquals("<!DOCTYPE doctype \"sysId\">", doctype3.outerHtml());
        assertEquals("<!DOCTYPE doctype PUBLIC \"pubId\" \"sysId\">", doctype4.outerHtml());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType doctype = new DocumentType("doctype", "pubId", "sysId", "baseUri");

        assertEquals("", doctype.outerHtmlTail());
    }

    @Test
    public void testHas() {
        DocumentType doctype1 = new DocumentType("doctype", "", "sysId", "baseUri");
        DocumentType doctype2 = new DocumentType("doctype", "pubId", "sysId", "baseUri");
        DocumentType doctype3 = new DocumentType("doctype", "", "", "baseUri");
        DocumentType doctype4 = new DocumentType("doctype", "pubId", "", "baseUri");

        assertFalse(doctype1.has(DocumentType.PUBLIC_ID));
        assertTrue(doctype2.has(DocumentType.PUBLIC_ID));
        assertFalse(doctype3.has(DocumentType.PUBLIC_ID));
        assertTrue(doctype4.has(DocumentType.PUBLIC_ID));

        assertTrue(doctype1.has(DocumentType.SYSTEM_ID));
        assertTrue(doctype2.has(DocumentType.SYSTEM_ID));
        assertFalse(doctype3.has(DocumentType.SYSTEM_ID));
        assertFalse(doctype4.has(DocumentType.SYSTEM_ID));
    }
}