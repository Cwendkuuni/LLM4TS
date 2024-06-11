package org.jsoup.nodes;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "HTML";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        Assert.assertEquals("#doctype", documentType.nodeName());
        Assert.assertEquals(name, documentType.attr("name"));
        Assert.assertEquals(publicId, documentType.attr("publicId"));
        Assert.assertEquals(systemId, documentType.attr("systemId"));
        Assert.assertEquals(baseUri, documentType.baseUri());
    }

    // Test cases for outerHtmlHead and outerHtmlTail methods can be added here
}