package org.jsoup.nodes.test;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.AnnotatedClassLoader;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "publicID";
        String systemId = "systemID";
        String baseUri = "baseUri";

        Document document = PowerMockito.mock(Document.class);
        PowerMockito.when(document.baseUri()).thenReturn(baseUri);

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
        assertEquals(baseUri, documentType.baseUri());

        PowerMockito.verifyNew(DocumentType.class).withArguments(ArgumentMatchers.eq(name), ArgumentMatchers.eq(publicId), ArgumentMatchers.eq(systemId), ArgumentMatchers.eq(baseUri));

        Element parent = Powermockito.mock(Element.class);
        documentType.appendChild(parent);
        PowerMockito.verify(parent, Mockito.times(1)).appendChild(documentType);
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", "");
        StringBuilder accum = new StringBuilder();

        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"publicID\" systemID>\n", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", "");

        documentType.outerHtmlTail(accum, 0, null);

        assertEquals("", accum.toString());
    }
}