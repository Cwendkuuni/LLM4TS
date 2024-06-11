package org.jsoup.nodes;

import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {

    private XmlDeclaration xmlDeclaration;

    @Before
    public void setUp() {
        xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
    }

    @Test
    public void testName() {
        assertEquals("xml", xmlDeclaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        String wholeDeclaration = xmlDeclaration.getWholeDeclaration();
        assertEquals("<?xml version=\"null\"", wholeDeclaration);
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        xmlDeclaration.outerHtmlHead(sb, 0, outputSettings);
        assertEquals("<?xml version=\"null\"?>", sb.toString());
    }

    @Test
    public void testToString() {
        assertEquals("<?xml version=\"null\"?>", xmlDeclaration.toString());
    }

}