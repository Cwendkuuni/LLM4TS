
package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class XmlDeclarationTest {

    @Test
    public void testNodeName() {
        XmlDeclaration declaration = new XmlDeclaration("name", "base uri", true);
        assertEquals("#declaration", declaration.nodeName());
    }
    
    @Test
    public void testName() {
        XmlDeclaration declaration = new XmlDeclaration("name", "base uri", true);
        assertEquals("name", declaration.name());
    }
    
    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration declaration = new XmlDeclaration("name", "base uri", true);
        assertEquals("name", declaration.getWholeDeclaration());
    }
    
    @Test
    public void testOuterHtmlHead() {
        XmlDeclaration declaration = new XmlDeclaration("name", "base uri", true);
        StringBuilder stringBuilder = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        try {
            declaration.outerHtmlHead(stringBuilder, 0, outputSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("<?name>", stringBuilder.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        XmlDeclaration declaration = new XmlDeclaration("name", "base uri", true);
        StringBuilder stringBuilder = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        try {
            declaration.outerHtmlTail(stringBuilder, 0, outputSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("", stringBuilder.toString());
    }
    
    @Test
    public void testToString() {
        XmlDeclaration declaration = new XmlDeclaration("name", "base uri", true);
        assertEquals("<?name>", declaration.toString());
    }
}
