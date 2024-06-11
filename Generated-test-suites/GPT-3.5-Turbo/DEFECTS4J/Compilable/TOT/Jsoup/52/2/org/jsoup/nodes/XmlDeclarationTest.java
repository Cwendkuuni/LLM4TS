package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {

    // Test case for nodeName() method
    @Test
    public void testNodeName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", true);
        String expectedNodeName = "#declaration";
        assertEquals(expectedNodeName, xmlDeclaration.nodeName());
    }

    // Test case for name() method
    @Test
    public void testName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", true);
        String expectedName = "xml";
        assertEquals(expectedName, xmlDeclaration.name());
    }

    // Test case for getWholeDeclaration() method - typical use case
    @Test
    public void testGetWholeDeclarationTypical() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", true);
        String expectedDeclaration = "xml";
        assertEquals(expectedDeclaration, xmlDeclaration.getWholeDeclaration());
    }

    // Test case for getWholeDeclaration() method - edge case
    @Test
    public void testGetWholeDeclarationEdge() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", true);
        xmlDeclaration.attributes.put("version", "1.0");
        xmlDeclaration.attributes.put("encoding", "UTF-8");
        String expectedDeclaration = "xml version=\"1.0\" encoding=\"UTF-8\"";
        assertEquals(expectedDeclaration, xmlDeclaration.getWholeDeclaration());
    }

    // Test case for getWholeDeclaration() method - error scenario
    @Test
    public void testGetWholeDeclarationError() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("other", "http://www.example.com", true);
        xmlDeclaration.attributes.put("version", "1.0");
        xmlDeclaration.attributes.put("encoding", "UTF-8");
        String expectedDeclaration = "other";
        assertEquals(expectedDeclaration, xmlDeclaration.getWholeDeclaration());
    }

    // Test case for toString() method
    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", true);
        String expectedToString = "<!" + xmlDeclaration.getWholeDeclaration() + ">";
        assertEquals(expectedToString, xmlDeclaration.toString());
    }

    // Test case for outerHtmlHead() method
    // This method is not tested separately as it is indirectly tested in the toString() method test case

    // Test case for outerHtmlTail() method
    // This method is not tested separately as it is not modifying any data and does not have any specific functionality

}