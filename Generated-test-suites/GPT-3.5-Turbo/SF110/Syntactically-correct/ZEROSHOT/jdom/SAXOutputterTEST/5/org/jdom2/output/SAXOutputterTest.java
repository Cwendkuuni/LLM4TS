
package org.jdom2.output;

import org.junit.Test;
import org.xml.sax.*;

import static org.junit.Assert.*;

public class SAXOutputterTest {

    @Test
    public void setContentHandler() {
        ContentHandler contentHandler = new DefaultHandler();
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setContentHandler(contentHandler);

        assertEquals(contentHandler, saxOutputter.getContentHandler());
    }

    @Test
    public void setErrorHandler() {
        ErrorHandler errorHandler = new DefaultHandler();
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setErrorHandler(errorHandler);

        assertEquals(errorHandler, saxOutputter.getErrorHandler());
    }

    @Test
    public void setDTDHandler() {
        DTDHandler dtdHandler = new DefaultHandler();
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setDTDHandler(dtdHandler);

        assertEquals(dtdHandler, saxOutputter.getDTDHandler());
    }

    @Test
    public void setEntityResolver() {
        EntityResolver entityResolver = new DefaultHandler();
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setEntityResolver(entityResolver);

        assertEquals(entityResolver, saxOutputter.getEntityResolver());
    }

    @Test
    public void setLexicalHandler() {
        LexicalHandler lexicalHandler = new DefaultHandler();
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setLexicalHandler(lexicalHandler);

        assertEquals(lexicalHandler, saxOutputter.getLexicalHandler());
    }

    @Test
    public void setDeclHandler() {
        DeclHandler declHandler = new DefaultHandler();
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setDeclHandler(declHandler);

        assertEquals(declHandler, saxOutputter.getDeclHandler());
    }

    @Test
    public void setReportNamespaceDeclarations() {
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setReportNamespaceDeclarations(true);

        assertTrue(saxOutputter.getReportNamespaceDeclarations());
    }

    @Test
    public void setReportDTDEvents() {
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setReportDTDEvents(true);

        assertTrue(saxOutputter.getReportDTDEvents());
    }

    @Test
    public void getFeature() {
        SAXOutputter saxOutputter = new SAXOutputter();

        assertThrows(SAXNotRecognizedException.class, () -> saxOutputter.getFeature("invalid_feature_name"));
    }

    @Test
    public void getProperty() {
        SAXOutputter saxOutputter = new SAXOutputter();

        assertThrows(SAXNotRecognizedException.class, () -> saxOutputter.getProperty("invalid_property_name"));
    }

    @Test
    public void setSAXOutputProcessor() {
        SAXOutputProcessor processor = new DefaultSAXOutputProcessor();
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setSAXOutputProcessor(processor);

        assertEquals(processor, saxOutputter.getSAXOutputProcessor());
    }

    @Test
    public void setFormat() {
        Format format = Format.getPrettyFormat();
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setFormat(format);

        assertEquals(format, saxOutputter.getFormat());
    }

    @Test
    public void output() throws JDOMException {
        Document document = new Document();
        SAXOutputter saxOutputter = new SAXOutputter();

        assertThrows(NullPointerException.class, () -> saxOutputter.output(document));
    }

    @Test
    public void outputFragment_withNullNodes() throws JDOMException {
        SAXOutputter saxOutputter = new SAXOutputter();

        assertThrows(NullPointerException.class, () -> saxOutputter.outputFragment(null));
    }

    @Test
    public void outputFragment_withNullNode() throws JDOMException {
        SAXOutputter saxOutputter = new SAXOutputter();

        assertThrows(NullPointerException.class, () -> saxOutputter.outputFragment(null));
    }

}
