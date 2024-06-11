package org.jsoup.nodes;

import org.jsoup.helper.StringUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class DocumentTypeTest {

    private static DocumentType documentType;

    @BeforeClass
    public static void setUp() {
        documentType = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "http://example.com");
    }

    @AfterClass
    public static void tearDown() {
        documentType = null;
    }

    @Test
    public void testDocumentTypeConstructor() {
        assertEquals("html", documentType.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.0//EN", documentType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/REC-html40/strict.dtd", documentType.attr("systemId"));
        assertEquals("http://example.com", documentType.baseUri());
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead_WithNoPublicId() throws NoSuchFieldException, IllegalAccessException {
        Field publicIdField = DocumentType.class.getDeclaredField("attributes");
        publicIdField.setAccessible(true);
        Attributes attributes = (Attributes) publicIdField.get(documentType);
        attributes.remove("publicId");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html http://www.w3.org/TR/REC-html40/strict.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHead_WithNoSystemId() throws NoSuchFieldException, IllegalAccessException {
        Field systemIdField = DocumentType.class.getDeclaredField("attributes");
        systemIdField.setAccessible(true);
        Attributes attributes = (Attributes) systemIdField.get(documentType);
        attributes.remove("systemId");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html \"-//W3C//DTD HTML 4.0//EN\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHead_WithNoPublicAndSystemId() throws NoSuchFieldException, IllegalAccessException {
        Field publicIdField = DocumentType.class.getDeclaredField("attributes");
        publicIdField.setAccessible(true);
        Attributes attributes = (Attributes) publicIdField.get(documentType);
        attributes.remove("publicId");

        Field systemIdField = DocumentType.class.getDeclaredField("attributes");
        systemIdField.setAccessible(true);
        attributes.remove("systemId");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html>", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, new Document.OutputSettings());
        assertEquals("", accum.toString());
    }

    @Test
    public void testBaseUri() {
        assertEquals("http://example.com", documentType.baseUri());
    }
}