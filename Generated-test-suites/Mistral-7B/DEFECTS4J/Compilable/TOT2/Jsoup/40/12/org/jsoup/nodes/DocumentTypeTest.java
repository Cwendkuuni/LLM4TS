package org.jsoup.nodes;
import org.junit.Test;
import org.jsoup.nodes.DocumentType;
import static org.junit.Assert.assertEquals;

public class DocumentTypeTest {

    @Test
    public void testConstructor_ValidArguments() {
        DocumentType docType = new DocumentType("html", "publicId1", "systemId1", "baseUri1");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("publicId1", docType.attr("publicId"));
        assertEquals("systemId1", docType.attr("systemId"));
        assertEquals("baseUri1", docType.baseUri());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_EmptyName() {
        new DocumentType("", "publicId", "systemId", "baseUri");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullName() {
        new DocumentType(null, "publicId", "systemId", "baseUri");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_EmptyPublicId() {
        new DocumentType("name", "", "systemId", "baseUri");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullPublicId() {
        new DocumentType("name", null, "systemId", "baseUri");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_EmptySystemId() {
        new DocumentType("name", "publicId", "", "baseUri");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullSystemId() {
        new DocumentType("name", "publicId", null, "baseUri");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_EmptyBaseUri() {
        new DocumentType("name", "publicId", "systemId", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullBaseUri() {
        new DocumentType("name", "publicId", "systemId", null);
    }
}