package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document.OutputSettings.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class DocumentTypeTest {
    private static DocumentType docType;

    @BeforeClass
    public static void setUp() {
        docType = new DocumentType("name", "publicId", "systemId", "baseUri");
    }

    @Test
    public void testConstructor() {
        assertEquals("name", docType.attr("name"));
        assertEquals("publicId", docType.attr("publicId"));
        assertEquals("systemId", docType.attr("systemId"));
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        Appendable accum = mock(Appendable.class);
        Document.OutputSettings out = mock(Document.OutputSettings.class);
        when(out.syntax()).thenReturn(Syntax.html);
        when(docType.has(anyString())).thenReturn(false);

        try {
            docType.outerHtmlHead(accum, 0, out);
            verify(accum).append("<!doctype");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testOuterHtmlTail() {
        Appendable accum = mock(Appendable.class);
        Document.OutputSettings out = mock(Document.OutputSettings.class);

        docType.outerHtmlTail(accum, 0, out);

        verifyNoInteractions(accum);
    }

    @Test
    public void testHas() {
        when(docType.attr(anyString())).thenReturn("value");

        assertTrue(docType.has("attribute"));
    }

    @Test
    public void testHasWithBlankAttribute() {
        when(docType.attr(anyString())).thenReturn("");

        assertFalse(docType.has("attribute"));
    }
}