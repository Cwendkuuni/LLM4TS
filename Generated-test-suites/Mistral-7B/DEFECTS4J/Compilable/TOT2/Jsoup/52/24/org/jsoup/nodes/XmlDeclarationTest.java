package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.XmlDeclaration;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationConstructor() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        String wholeDeclaration = xmlDeclaration.getWholeDeclaration();
        assertTrue(wholeDeclaration.startsWith("<declaration version="));
    }
}