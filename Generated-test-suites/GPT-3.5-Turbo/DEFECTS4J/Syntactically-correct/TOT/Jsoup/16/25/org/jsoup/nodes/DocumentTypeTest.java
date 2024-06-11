
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DocumentTypeTest {

    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("html", "", "", "");
        assertEquals("#doctype", doctype.nodeName());
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdAndSystemId() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/html4/strict.dtd", "");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        doctype.outerHtmlHead(accum, 0, outputSettings);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0//EN\" http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdOnly() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "", "");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        doctype.outerHtmlHead(accum, 0, outputSettings);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0//EN\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithSystemIdOnly() {
        DocumentType doctype = new DocumentType("html", "", "http://www.w3.org/TR/html4/strict.dtd", "");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        doctype.outerHtmlHead(accum, 0, outputSettings);
        assertEquals("<!DOCTYPE html http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithoutPublicIdAndSystemId() {
        DocumentType doctype = new DocumentType("html", "", "", "");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        doctype.outerHtmlHead(accum, 0, outputSettings);
        assertEquals("<!DOCTYPE html>", accum.toString());
    }

    // Additional Tests

    @Test
    public void testAttrMethod() {
        DocumentType doctype = new DocumentType("html", "", "", "");
        doctype.attr("name", "newName");
        assertEquals("newName", doctype.attr("name"));
    }

    @Test
    public void testBaseUri() {
        DocumentType doctype = new DocumentType("html", "", "", "http://example.com");
        assertEquals("http://example.com", doctype.baseUri());
    }

    @Test
    public void testAttributes() {
        DocumentType doctype = new DocumentType("html", "", "", "");
        assertFalse(doctype.hasAttributes());
        doctype.attr("name", "doctype");
        assertTrue(doctype.hasAttributes());
    }
}

