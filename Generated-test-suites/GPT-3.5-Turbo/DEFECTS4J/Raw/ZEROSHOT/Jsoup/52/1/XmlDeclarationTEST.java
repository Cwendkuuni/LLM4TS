package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class XmlDeclarationTest {

    private XmlDeclaration xmlDeclaration;
    private String name = "test";
    private String baseUri = "http://example.com";
    private boolean isProcessingInstruction = true;

    @Before
    public void setUp() {
        xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
    }

    @Test
    public void testConstructor() {
        assertNotNull(xmlDeclaration);
        assertEquals(name, xmlDeclaration.name());
    }

    @Test
    public void testNodeName() {
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testGetWholeDeclaration() {
        String wholeDeclaration = xmlDeclaration.getWholeDeclaration();
        assertNotNull(wholeDeclaration);
        assertEquals(name, wholeDeclaration);
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        Appendable accum = mock(Appendable.class);
        int depth = 1;
        Document.OutputSettings outputSettings = mock(Document.OutputSettings.class);

        xmlDeclaration.outerHtmlHead(accum, depth, outputSettings);

        verify(accum).append("<!");
        verify(accum).append(getWholeDeclaration());
        verify(accum).append(">");
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        Appendable accum = mock(Appendable.class);
        int depth = 1;
        Document.OutputSettings outputSettings = mock(Document.OutputSettings.class);

        xmlDeclaration.outerHtmlTail(accum, depth, outputSettings);
    }

    @Test
    public void testToString() {
        assertEquals(xmlDeclaration.outerHtml(), xmlDeclaration.toString());
    }
}