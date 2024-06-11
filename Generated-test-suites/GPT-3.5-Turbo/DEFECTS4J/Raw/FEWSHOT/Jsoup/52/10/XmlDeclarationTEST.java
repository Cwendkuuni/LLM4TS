package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.junit.Test;

public class XmlDeclarationTest {
    private final String name = "testDeclaration";
    private final String baseUri = "http://example.com";
    private final boolean isProcessingInstruction = false;

    @Test
    public void testConstructor() {
        XmlDeclaration declaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        assertNotNull(declaration);
        assertEquals(name, declaration.name());
        assertEquals("#declaration", declaration.nodeName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullName_ThrowsException() {
        new XmlDeclaration(null, baseUri, isProcessingInstruction);
    }

    @Test
    public void testGetWholeDeclaration_WithXmlNameAndAttributes_ReturnsDeclarationWithVersionAndEncoding() {
        XmlDeclaration declaration = new XmlDeclaration("xml", baseUri, isProcessingInstruction);
        declaration.attributes.put("version", "1.0");
        declaration.attributes.put("encoding", "UTF-8");
        
        String expectedDeclaration = "xml version=\"1.0\" encoding=\"UTF-8\"";
        
        assertEquals(expectedDeclaration, declaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclaration_WithXmlNameAndNoAttributes_ReturnsDeclarationWithNameOnly() {
        XmlDeclaration declaration = new XmlDeclaration("xml", baseUri, isProcessingInstruction);
        
        assertEquals("xml", declaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclaration_WithNonXmlName_ReturnsDeclarationWithNameOnly() {
        XmlDeclaration declaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        
        assertEquals(name, declaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead_WithProcessingInstruction_ReturnsDeclarationWithQuestionMark() {
        XmlDeclaration declaration = new XmlDeclaration(name, baseUri, true);
        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append("<?").append(declaration.getWholeDeclaration()).append(">");
        
        StringBuilder actualHtml = new StringBuilder();
        try {
            declaration.outerHtmlHead(actualHtml, 0, new Document.OutputSettings());
        } catch (Exception e) {
            // ignore
        }
        
        assertEquals(expectedHtml.toString(), actualHtml.toString());
    }
    
    @Test
    public void testOuterHtmlHead_WithDeclaration_ReturnsDeclarationWithExclamationMark() {
        XmlDeclaration declaration = new XmlDeclaration(name, baseUri, false);
        StringBuilder expectedHtml = new StringBuilder();
        expectedHtml.append("<!").append(declaration.getWholeDeclaration()).append(">");
        
        StringBuilder actualHtml = new StringBuilder();
        try {
            declaration.outerHtmlHead(actualHtml, 0, new Document.OutputSettings());
        } catch (Exception e) {
            // ignore
        }
        
        assertEquals(expectedHtml.toString(), actualHtml.toString());
    }

    @Test
    public void testOuterHtmlTail_WithDeclaration_ReturnsEmptyString() {
        XmlDeclaration declaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        
        StringBuilder actualHtml = new StringBuilder();
        try {
            declaration.outerHtmlTail(actualHtml, 0, new Document.OutputSettings());
        } catch (Exception e) {
            // ignore
        }
        
        assertEquals("", actualHtml.toString());
    }

    @Test
    public void testToString_ReturnsOuterHtml() {
        XmlDeclaration declaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        assertNotNull(declaration.toString());
        assertTrue(declaration.toString().startsWith("<!") || declaration.toString().startsWith("<?"));
        assertTrue(declaration.toString().endsWith(">"));
    }
}