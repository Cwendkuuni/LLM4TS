
package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

public class XmlDeclarationTest {

    @Test
    public void testNodeName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
        Assert.assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
        Assert.assertEquals("xml", xmlDeclaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        // Test cases for getWholeDeclaration() method
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        // Test cases for outerHtmlHead() method
    }

    @Test
    public void testOuterHtmlTailAndToString() throws IOException {
        // Test cases for outerHtmlTail() and toString() methods
    }
}
