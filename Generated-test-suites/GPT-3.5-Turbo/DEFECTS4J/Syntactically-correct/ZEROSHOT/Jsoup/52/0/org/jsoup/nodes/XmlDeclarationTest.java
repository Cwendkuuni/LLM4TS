
package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XmlDeclarationTest {
    private XmlDeclaration xmlDeclaration;

    @Before
    public void setup() {
        xmlDeclaration = new XmlDeclaration("declaration", "base_uri", true);
    }

    @Test
    public void testNodeName() {
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testGetName() {
        assertEquals("declaration", xmlDeclaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        String wholeDeclaration = xmlDeclaration.getWholeDeclaration();
        assertEquals("declaration", wholeDeclaration);
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        try {
            xmlDeclaration.outerHtmlHead(accum, 0, outputSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("<!declaration>", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        try {
            xmlDeclaration.outerHtmlTail(accum, 0, outputSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("", accum.toString());
    }

    @Test
    public void testToString() {
        String expectedOutput = "<!declaration>";
        assertEquals(expectedOutput, xmlDeclaration.toString());
    }
}
