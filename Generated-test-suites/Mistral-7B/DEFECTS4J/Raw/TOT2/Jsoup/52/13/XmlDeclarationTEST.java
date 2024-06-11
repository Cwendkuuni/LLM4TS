package org.junit.test;

import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {

    @Test
    public void testName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        String expectedName = "xml";
        String actualName = xmlDeclaration.name();
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetWholeDeclaration_ValidDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        xmlDeclaration.attributes.put("version", "1.0");
        xmlDeclaration.attributes.put("encoding", "UTF-8");
        String expectedDeclaration = "<declaration version=\"1.0\" encoding=\"UTF-8\" />";
        String actualDeclaration = xmlDeclaration.getWholeDeclaration();
        assertEquals(expectedDeclaration, actualDeclaration);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWholeDeclaration_InvalidName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("invalid", null, false);
        String actualDeclaration = xmlDeclaration.getWholeDeclaration();
        fail("Expected IllegalArgumentException, but no exception was thrown.");
    }

    // Add test cases for other methods (nodeName(), toString(), outerHtmlHead(), and outerHtmlTail())
}