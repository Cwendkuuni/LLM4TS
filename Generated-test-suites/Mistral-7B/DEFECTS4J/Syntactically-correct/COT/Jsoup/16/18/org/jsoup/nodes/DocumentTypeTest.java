package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

/**
 * Tests for the {@link DocumentType} class.
 */
public class DocumentTypeTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        String name = "html";
        String publicId = "HTML";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://example.com";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", docType.nodeName());
        assertEquals(name, docType.attr("name"));
        assertEquals(publicId, docType.attr("publicId"));
        assertEquals(systemId, docType.attr("systemId"));
        assertEquals(baseUri, docType.baseUri());
    }

    /**
     * Test nodeName method.
     */
    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("html", null, null, null);
        assertEquals("#doctype", docType.nodeName());
    }

    /**
     * Test outerHtmlHead method with a valid doctype.
     */
    @Test
    public void testOuterHtmlHeadValid() {
        DocumentType docType = new DocumentType("html", "HTML", "http://www.w3.org/TR/html4/strict.dtd", null);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"HTML\" http://www.w3.org/TR/html4/strict.dtd>";
        assertEquals(expected, accum.toString());
    }

    /**
     * Test outerHtmlHead method with an empty publicId.
     */
    @Test
    public void testOuterHtmlHeadEmptyPublicId() {
        DocumentType docType = new DocumentType("html", null, "http://www.w3.org/TR/html4/strict.dtd", null);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html";
        assertEquals(expected, accum.toString());
    }

    /**
     * Test outerHtmlHead method with an empty systemId.
     */
    @Test
    public void testOuterHtmlHeadEmptySystemId() {
        DocumentType docType = new DocumentType("html", "HTML", null, null);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"HTML\"";
        assertEquals(expected, accum.toString());
    }

    /**
     * Test outerHtmlHead method with null accumulator.
     */
    @Test(expected = NullPointerException.class)
    public void testOuterHtmlHeadNullAccumulator() {
        DocumentType docType = new DocumentType("html", null, null, null);
        docType.outerHtmlHead(null, 0, null);
    }

    /**
     * Test outerHtmlTail method as it is empty by default.
     */
    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("html", null, null, null);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlTail(accum, 0, null);

        assertEquals("", accum.toString());
    }

    /**
     * Test equals method.
     */
    @Test
    public void testEquals() {
        DocumentType docType1 = new DocumentType("html", null, null, null);
        DocumentType docType2 = new DocumentType("html", null, null, null);

        assertEquals(docType1, docType2);
    }

    /**
     * Test equals method with different names.
     */
    @Test
    public void testEqualsDifferentNames() {
        DocumentType docType1 = new DocumentType("html", null, null, null);
        DocumentType docType2 = new DocumentType("head", null, null, null);

        assertFalse(docType1.equals(docType2));
    }

    /**
     * Test equals method with different publicIds.
     */
    @Test
    public void testEqualsDifferentPublicIds() {
        DocumentType docType1 = new DocumentType("html", "HTML", null, null);
        DocumentType docType2 = new DocumentType("html", "HEAD", null, null);

        assertFalse(docType1.equals(docType2));
    }

    /**
     * Test equals method with different systemIds.
     */
    @Test
    public void testEqualsDifferentSystemIds() {
        DocumentType docType1 = new DocumentType("html", null, "http://www.w3.org/TR/html4/strict.dtd", null);
        DocumentType docType2 = new DocumentType("html", null, "http://www.w3.org/TR/html5/strict.dtd", null);

        assertFalse(docType1.equals(docType2));
    }

    /**
     * Test equals method with different base Uris.
     */
    @Test
    public void testEqualsDifferentBaseUris() {
        DocumentType docType1 = new DocumentType("html", null, null, "http://example.com");
        DocumentType docType2 = new DocumentType("html", null, null, "http://another-example.com");

        assertFalse(docType1.equals(docType2));
    }

    /**
     * Test hashCode method.
     */
    @Test
    public void testHashCode() {
        DocumentType docType = new DocumentType("html", null, null, null);
        int hashCode = docType.hashCode();

        DocumentType docType2 = new DocumentType("html", null, null, null);
        int hashCode2 = docType2.hashCode();

        assertEquals(hashCode, hashCode2);
    }

    /**
     * Test hashCode method with different names.
     */
    @Test
    public void testHashCodeDifferentNames() {
        DocumentType docType1 = new DocumentType("html", null, null, null);
        DocumentType docType2 = new DocumentType("head", null, null, null);

        assertNotEquals(docType1.hashCode(), docType2.hashCode());
    }

    /**
     * Test hashCode method with different publicIds.
     */
    @Test
    public void testHashCodeDifferentPublicIds() {
        DocumentType docType1 = new DocumentType("html", "HTML", null, null);
        DocumentType docType2 = new DocumentType("html", "HEAD", null, null);

        assertNotEquals(docType1.hashCode(), docType2.hashCode());
    }

    /**
     * Test hashCode method with different systemIds.
     */
    @Test
    public void testHashCodeDifferentSystemIds() {
        DocumentType docType1 = new DocumentType("html", null, "http://www.w3.org/TR/html4/strict.dtd", null);
        DocumentType docType2 = new DocumentType("html", null, "http://www.w3.org/TR/html5/strict.dtd", null);

        assertNotEquals(docType1.hashCode(), docType2.hashCode());
    }

    /**
     * Test hashCode method with different base Uris.
     */
    @Test
    public void testHashCodeDifferentBaseUris() {
        DocumentType docType1 = new DocumentType("html", null, null, "http://example.com");
        DocumentType docType2 = new DocumentType("html", null, null, "http://another-example.com");

        assertNotEquals(docType1.hashCode(), docType2.hashCode());
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        DocumentType docType = new DocumentType("html", null, null, null);

        String toString = docType.toString();
        String expected = "<html>";

        assertEquals(expected, toString);
    }

    /**
     * Test cloneNode method with a cloned node.
     */
    @Test
    public void testCloneNode() {
        DocumentType docType = new DocumentType("html", null, null, null);
        DocumentType clonedDocType = (DocumentType) docType.cloneNode(true);

        assertEquals(docType, clonedDocType);
    }

    /**
     * Test cloneNode method with a deep cloned node.
     */
    @Test
    public void testCloneNodeDeep() {
        DocumentType docType = new DocumentType("html", null, null, null);
        Document doc = new Document();
        doc.setRootElem(docType);

        Document clonedDoc = doc.clone();
        DocumentType clonedDocType = (DocumentType) clonedDoc.rootElem();

        assertEquals(docType, clonedDocType);
    }

    /**
     * Test cloneNode method with a null cloneParent.
     */
    @Test
    public void testCloneNodeNullParent() {
        DocumentType docType = new DocumentType("html", null, null, null);

        Node cloneNode = docType.cloneNode(null);

        assertNull(cloneNode);
    }

    /**
     * Test cloneNode method with a shallow cloned node.
     */
    @Test
    public void testCloneNodeShallow() {
        DocumentType docType = new DocumentType("html", null, null, null);
        Document doc = new Document();
        doc.setRootElem(docType);

        Node cloneNode = docType.cloneNode(false);
        Element clonedElement = (Element) cloneNode;

        assertEquals(docType, clonedElement.parent());
    }
}