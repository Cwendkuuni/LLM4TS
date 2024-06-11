package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {

    @Test
    public void testNameMethod() {
        String name = "xml";
        XmlDeclaration declaration = new XmlDeclaration(name, "", true);
        assertEquals(name, declaration.name());
    }

    @Test
    public void testGetWholeDeclarationWithAttributes() {
        String name = "xml";
        XmlDeclaration declaration = new XmlDeclaration(name, "", true);
        declaration.attributes().put("version", "1.0");
        declaration.attributes().put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", declaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclarationWithoutAttributes() {
        String name = "html";
        XmlDeclaration declaration = new XmlDeclaration(name, "", true);
        assertEquals(name, declaration.getWholeDeclaration());
    }

    // Edge cases and exception handling scenarios can be added here

}