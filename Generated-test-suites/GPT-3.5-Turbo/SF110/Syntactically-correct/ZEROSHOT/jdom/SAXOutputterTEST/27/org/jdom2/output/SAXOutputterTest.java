import org.jdom2.output.SAXOutputter;
import org.jdom2.*;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

import static org.junit.Assert.*;

public class SAXOutputterTest {

    private SAXOutputter saxOutputter;

    @Before
    public void setUp() {
        ContentHandler contentHandler = new DefaultHandler();
        ErrorHandler errorHandler = new DefaultHandler();
        DTDHandler dtdHandler = new DefaultHandler();
        EntityResolver entityResolver = new DefaultHandler();
        LexicalHandler lexicalHandler = new DefaultHandler();

        saxOutputter = new SAXOutputter(contentHandler, errorHandler, dtdHandler, entityResolver, lexicalHandler);
    }

    @Test
    public void getContentHandler() {
        ContentHandler contentHandler = saxOutputter.getContentHandler();

        assertNotNull(contentHandler);
    }

    @Test
    public void setContentHandler() {
        ContentHandler contentHandler = new DefaultHandler();
        saxOutputter.setContentHandler(contentHandler);

        ContentHandler result = saxOutputter.getContentHandler();

        assertEquals(contentHandler, result);
    }

    @Test
    public void getErrorHandler() {
        ErrorHandler errorHandler = saxOutputter.getErrorHandler();

        assertNotNull(errorHandler);
    }

    @Test
    public void setErrorHandler() {
        ErrorHandler errorHandler = new DefaultHandler();
        saxOutputter.setErrorHandler(errorHandler);

        ErrorHandler result = saxOutputter.getErrorHandler();

        assertEquals(errorHandler, result);
    }

    @Test
    public void getDTDHandler() {
        DTDHandler dtdHandler = saxOutputter.getDTDHandler();

        assertNotNull(dtdHandler);
    }

    @Test
    public void setDTDHandler() {
        DTDHandler dtdHandler = new DefaultHandler();
        saxOutputter.setDTDHandler(dtdHandler);

        DTDHandler result = saxOutputter.getDTDHandler();

        assertEquals(dtdHandler, result);
    }

    @Test
    public void getEntityResolver() {
        EntityResolver entityResolver = saxOutputter.getEntityResolver();

        assertNotNull(entityResolver);
    }

    @Test
    public void setEntityResolver() {
        EntityResolver entityResolver = new DefaultHandler();
        saxOutputter.setEntityResolver(entityResolver);

        EntityResolver result = saxOutputter.getEntityResolver();

        assertEquals(entityResolver, result);
    }

    @Test
    public void getLexicalHandler() {
        LexicalHandler lexicalHandler = saxOutputter.getLexicalHandler();

        assertNotNull(lexicalHandler);
    }

    @Test
    public void setLexicalHandler() {
        LexicalHandler lexicalHandler = new DefaultHandler();
        saxOutputter.setLexicalHandler(lexicalHandler);

        LexicalHandler result = saxOutputter.getLexicalHandler();

        assertEquals(lexicalHandler, result);
    }

    @Test
    public void getDeclHandler() {
        assertNotNull(saxOutputter.getDeclHandler());
    }

    @Test
    public void setDeclHandler() {
        DefaultHandler declHandler = new DefaultHandler();
        saxOutputter.setDeclHandler(declHandler);

        DefaultHandler result = saxOutputter.getDeclHandler();

        assertEquals(declHandler, result);
    }

    @Test
    public void getReportNamespaceDeclarations() {
        boolean declareNamespaces = saxOutputter.getReportNamespaceDeclarations();

        assertFalse(declareNamespaces);
    }

    @Test
    public void setReportNamespaceDeclarations() {
        boolean declareNamespaces = true;
        saxOutputter.setReportNamespaceDeclarations(declareNamespaces);

        boolean result = saxOutputter.getReportNamespaceDeclarations();

        assertEquals(declareNamespaces, result);
    }

    @Test
    public void getReportDTDEvents() {
        boolean reportDtdEvents = saxOutputter.getReportDTDEvents();

        assertTrue(reportDtdEvents);
    }

    @Test
    public void setReportDTDEvents() {
        boolean reportDtdEvents = false;
        saxOutputter.setReportDTDEvents(reportDtdEvents);

        boolean result = saxOutputter.getReportDTDEvents();

        assertEquals(reportDtdEvents, result);
    }

    @Test
    public void setFeature() {
        String name = "http://xml.org/sax/features/namespace-prefixes";
        boolean value = true;

        try {
            saxOutputter.setFeature(name, value);
        } catch (Exception e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void getFeature() {
        String name = "http://xml.org/sax/features/namespace-prefixes";

        try {
            boolean result = saxOutputter.getFeature(name);

            assertTrue(result);
        } catch (Exception e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void setProperty() {
        String name = "http://xml.org/sax/properties/lexical-handler";
        Object value = new DefaultHandler();

        try {
            saxOutputter.setProperty(name, value);
        } catch (Exception e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void getProperty() {
        String name = "http://xml.org/sax/properties/lexical-handler";

        try {
            saxOutputter.getProperty(name);
            assertNotNull(saxOutputter.getLexicalHandler());
        } catch (Exception e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void getSAXOutputProcessor() {
        SAXOutputProcessor processor = saxOutputter.getSAXOutputProcessor();

        assertNotNull(processor);
    }

    @Test
    public void setSAXOutputProcessor() {
        DefaultSAXOutputProcessor processor = new DefaultSAXOutputProcessor();
        saxOutputter.setSAXOutputProcessor(processor);

        SAXOutputProcessor result = saxOutputter.getSAXOutputProcessor();

        assertEquals(processor, result);
    }

    @Test
    public void getFormat() {
        Format format = saxOutputter.getFormat();

        assertNotNull(format);
    }

    @Test
    public void setFormat() {
        Format format = Format.getRawFormat();
        saxOutputter.setFormat(format);

        Format result = saxOutputter.getFormat();

        assertEquals(format, result);
    }

    @Test
    public void output() {
        Document document = new Document();
        document.addContent(new Element("test"));

        try {
            saxOutputter.output(document);
        } catch (Exception e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void outputWithList() {
        Element element = new Element("test");
        element.addContent(new Element("child"));

        try {
            saxOutputter.output(element.getChildren());
        } catch (Exception e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void outputWithElement() {
        Element element = new Element("test");
        element.addContent(new Element("child"));

        try {
            saxOutputter.output(element);
        } catch (Exception e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void outputFragmentWithList() {
        Element element = new Element("test");
        element.addContent(new Element("child"));

        try {
            saxOutputter.outputFragment(element.getChildren());
        } catch (Exception e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void outputFragmentWithNullList() {
        try {
            saxOutputter.outputFragment(null);
        } catch (Exception e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void outputFragmentWithContent() {
        Element element = new Element("test");
        element.addContent(new Element("child"));

        try {
            saxOutputter.outputFragment(element);
        } catch (Exception e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void outputFragmentWithNullContent() {
        try {
            saxOutputter.outputFragment(null);
        } catch (Exception e) {
            fail("Should not throw an exception");
        }
    }
}