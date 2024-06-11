package org.jsoup.nodes;

import org.junit.*;
import static org.junit.Assert.*;

public class DocumentTypeTest {
    
    @Test
    public void testNameNotEmpty() {
        DocumentType docType = new DocumentType("name", null, null, null);
        String name = docType.attr("name");
        assertNotNull(name);
        assertFalse(name.isEmpty());
    }
    
    @Test
    public void testNameIsCorrect() {
        String name = "name";
        DocumentType docType = new DocumentType(name, null, null, null);
        assertEquals(name, docType.attr("name"));
    }
    
    @Test
    public void testPublicIdIsCorrect() {
        String publicId = "publicId";
        DocumentType docType = new DocumentType("name", publicId, null, null);
        assertEquals(publicId, docType.attr("publicId"));
    }
    
    @Test
    public void testSystemIdIsCorrect() {
        String systemId = "systemId";
        DocumentType docType = new DocumentType("name", null, systemId, null);
        assertEquals(systemId, docType.attr("systemId"));
    }
    
    @Test
    public void testOuterHtmlHeadOutput() {
        String expectedOutput = "<!DOCTYPE name PUBLIC \"publicId\" \"systemId\">";
        DocumentType docType = new DocumentType("name", "publicId", "systemId", null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, new Document.OutputSettings());
        assertEquals(expectedOutput, accum.toString());
    }
    
    @Test
    public void testOuterHtmlTailOutput() {
        String expectedOutput = "";
        DocumentType docType = new DocumentType("name", null, null, null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlTail(accum, 0, new Document.OutputSettings());
        assertEquals(expectedOutput, accum.toString());
    }
}