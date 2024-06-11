package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class XmlDeclarationTest {
    
    @Test
    public void testXmlDeclarationName() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", true);
        assertEquals("xml", declaration.name());
    }
    
    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", true);
        assertEquals("xml", declaration.getWholeDeclaration());
    }
    
    @Test
    public void testOuterHtmlHead() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", true);
        StringBuffer sb = new StringBuffer();
        try {
            declaration.outerHtmlHead(sb, 0, new Document.OutputSettings());
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("<!xml>", sb.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", true);
        StringBuffer sb = new StringBuffer();
        declaration.outerHtmlTail(sb, 0, new Document.OutputSettings());
        assertEquals("", sb.toString());
    }
}