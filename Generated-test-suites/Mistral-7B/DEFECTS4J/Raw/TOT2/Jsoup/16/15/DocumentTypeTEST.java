// Test file for DocumentType class
package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.DocumentType;
import org.jsoup.helper.StringUtil;

public class DocumentTypeTest {
    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("html", "", "", "");
        String name = documentType.nodeName();
        assertEquals("#doctype", name);
    }

    @Test
    public void testDocumentTypeWithEmptyName() {
        DocumentType documentType = new DocumentType("", "", "", "");
        String name = documentType.nodeName();
        assertEquals("#doctype", name);
    }

    @Test
    public void testDocumentTypeWithEmptyPublicId() {
        DocumentType documentType = new DocumentType("html", "", "", "");
        String publicId = documentType.attr("publicId");
        assertTrue(StringUtil.isBlank(publicId));
    }

    @Test
    public void testDocumentTypeWithEmptySystemId() {
        DocumentType documentType = new DocumentType("html", "", "", "");
        String systemId = documentType.attr("systemId");
        assertTrue(StringUtil.isBlank(systemId));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeWithNullName() {
        try {
            new DocumentType(null, "", "", "");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeWithNullBaseUri() {
        try {
            new DocumentType("html", "", "", null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeWithInvalidPublicId() {
        DocumentType documentType = new DocumentType("html", "foo", "", "");
        String publicId = documentType.attr("publicId");
        assertEquals("foo", publicId);

        try {
            new DocumentType("html", "<script>", "", "");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeWithInvalidSystemId() {
        DocumentType documentType = new DocumentType("html", "", "<script>", "");
        String systemId = documentType.attr("systemId");
        assertEquals("", systemId);

        try {
            new DocumentType("html", "", "foo<script>", "");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}

// Test file end