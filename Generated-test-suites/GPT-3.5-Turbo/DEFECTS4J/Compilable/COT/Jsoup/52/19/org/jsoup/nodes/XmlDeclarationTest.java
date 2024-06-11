package org.jsoup.nodes;
import org.jsoup.helper.Validate;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {

    @Test
    public void testNodeName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("", "", true);
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "", true);
        assertEquals("xml", xmlDeclaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "", true);
        assertEquals("xml", xmlDeclaration.getWholeDeclaration());
    }

    // Edge case: name is "xml" and attributes are present
    @Test
    public void testGetWholeDeclarationWithAttributes() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "", true);
        xmlDeclaration.attributes.put("version", "1.0");
        xmlDeclaration.attributes.put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
    }

    // Exception scenario: name is not "xml"
    @Test
    public void testGetWholeDeclarationDifferentName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("declaration", "", true);
        xmlDeclaration.attributes.put("version", "1.0");
        xmlDeclaration.attributes.put("encoding", "UTF-8");
        assertEquals("declaration", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "", true);
        assertEquals("<!xml>", xmlDeclaration.toString());
    }

    // Edge case: isProcessingInstruction is false
    @Test
    public void testToStringIsProcessingInstructionFalse() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "", false);
        assertEquals("<?xml>", xmlDeclaration.toString());
    }

}