package org.jsoup.nodes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Before;
import org.junit.Test;

public class XmlDeclarationTest {

    private XmlDeclaration xmlDeclaration;

    @Before
    public void setUp() {
        xmlDeclaration = new XmlDeclaration("xml", "http://example.com", true);
    }

    @Test
    public void testNodeName() {
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testName() {
        assertEquals("xml", xmlDeclaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        assertEquals("xml version=\"1.0\"", xmlDeclaration.getWholeDeclaration());
    }

    // Test for edge case where declaration name is not "xml"
    @Test
    public void testGetWholeDeclarationDifferentName() {
        XmlDeclaration differentDeclaration = new XmlDeclaration("test", "http://example.com", true);
        assertEquals("test", differentDeclaration.getWholeDeclaration());
    }

    // Test for exception handling scenario where version attribute is null
    @Test(expected = NullPointerException.class)
    public void testGetWholeDeclarationNullVersion() {
        XmlDeclaration nullVersionDeclaration = new XmlDeclaration("xml", "http://example.com", true);
        nullVersionDeclaration.attributes.put("version", null);
        nullVersionDeclaration.getWholeDeclaration();
    }

    @Test
    public void testToString() {
        assertEquals("<?xml version=\"1.0\">", xmlDeclaration.toString());
    }
}