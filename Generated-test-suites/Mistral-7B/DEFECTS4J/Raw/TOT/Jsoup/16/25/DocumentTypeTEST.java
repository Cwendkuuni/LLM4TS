package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        // Expert 1
    }

    @Test(expected = NullPointerException.class)
    public void testNullName() {
        // Expert 1
    }

    @Test(expected = NullPointerException.class)
    public void testNullPublicId() {
        // Expert 1
    }

    @Test(expected = NullPointerException.class)
    public void testNullSystemId() {
        // Expert 1
    }

    @Test(expected = NullPointerException.class)
    public void testNullBaseUri() {
        // Expert 1
    }

    @Test
    public void testDocumentTypeCreationWithEmptyValues() {
        // Expert 3
    }

    @Test
    public void testDocumentTypeCreationWithNullValues() {
        // Expert 3
    }

    @Test
    public void testDocumentTypeToString() {
        // Expert 3
    }

    @Test
    public void testDocumentTypeOuterHtml() {
        // Expert 2
    }

    @Test
    public void testDocumentTypeWithEmptyPublicId() {
        // Expert 2
    }

    @Test
    public void testDocumentTypeWithEmptySystemId() {
        // Expert 2
    }
}