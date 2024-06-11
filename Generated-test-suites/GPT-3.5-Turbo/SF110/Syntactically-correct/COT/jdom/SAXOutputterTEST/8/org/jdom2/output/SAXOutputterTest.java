import org.jdom2.*;
import org.jdom2.output.support.*;
import org.junit.*;
import static org.junit.Assert.*;

public class SAXOutputterTest {

    @Test
    public void testSetContentHandler() {
        SAXOutputter outputter = new SAXOutputter();
        ContentHandler contentHandler = new DefaultHandler();
        outputter.setContentHandler(contentHandler);
        assertEquals(contentHandler, outputter.getContentHandler());
    }

    @Test
    public void testSetErrorHandler() {
        SAXOutputter outputter = new SAXOutputter();
        ErrorHandler errorHandler = new DefaultHandler();
        outputter.setErrorHandler(errorHandler);
        assertEquals(errorHandler, outputter.getErrorHandler());
    }

    @Test
    public void testSetDTDHandler() {
        SAXOutputter outputter = new SAXOutputter();
        DTDHandler dtdHandler = new DefaultHandler();
        outputter.setDTDHandler(dtdHandler);
        assertEquals(dtdHandler, outputter.getDTDHandler());
    }

    @Test
    public void testSetEntityResolver() {
        SAXOutputter outputter = new SAXOutputter();
        EntityResolver entityResolver = new DefaultHandler();
        outputter.setEntityResolver(entityResolver);
        assertEquals(entityResolver, outputter.getEntityResolver());
    }

    @Test
    public void testSetLexicalHandler() {
        SAXOutputter outputter = new SAXOutputter();
        LexicalHandler lexicalHandler = new DefaultHandler();
        outputter.setLexicalHandler(lexicalHandler);
        assertEquals(lexicalHandler, outputter.getLexicalHandler());
    }

    @Test
    public void testSetDeclHandler() {
        SAXOutputter outputter = new SAXOutputter();
        DeclHandler declHandler = new DefaultHandler();
        outputter.setDeclHandler(declHandler);
        assertEquals(declHandler, outputter.getDeclHandler());
    }

    @Test
    public void testGetReportNamespaceDeclarations() {
        SAXOutputter outputter = new SAXOutputter();
        assertFalse(outputter.getReportNamespaceDeclarations());
    }

    @Test
    public void testSetReportNamespaceDeclarations() {
        SAXOutputter outputter = new SAXOutputter();
        outputter.setReportNamespaceDeclarations(true);
        assertTrue(outputter.getReportNamespaceDeclarations());
    }

    @Test
    public void testGetReportDTDEvents() {
        SAXOutputter outputter = new SAXOutputter();
        assertTrue(outputter.getReportDTDEvents());
    }

    @Test
    public void testSetReportDTDEvents() {
        SAXOutputter outputter = new SAXOutputter();
        outputter.setReportDTDEvents(false);
        assertFalse(outputter.getReportDTDEvents());
    }

    @Test
    public void testSetFeature() {
        SAXOutputter outputter = new SAXOutputter();
        try {
            outputter.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
            assertTrue(outputter.getReportNamespaceDeclarations());
            outputter.setFeature("http://xml.org/sax/features/namespaces", true);
            assertTrue(outputter.getFeature("http://xml.org/sax/features/namespaces"));
            outputter.setFeature("http://xml.org/sax/features/validation", true);
            assertTrue(outputter.getReportDTDEvents());
        } catch (SAXNotRecognizedException | SAXNotSupportedException e) {
            fail("Failed to set feature");
        }
    }

    @Test
    public void testGetFeature() {
        SAXOutputter outputter = new SAXOutputter();
        try {
            assertFalse(outputter.getFeature("invalid.feature"));
        } catch (SAXNotRecognizedException | SAXNotSupportedException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testSetProperty() {
        SAXOutputter outputter = new SAXOutputter();
        try {
            outputter.setProperty("http://xml.org/sax/properties/lexical-handler", new DefaultHandler());
            assertNotNull(outputter.getProperty("http://xml.org/sax/properties/lexical-handler"));
            outputter.setProperty("http://xml.org/sax/properties/declaration-handler", new DefaultHandler());
            assertNotNull(outputter.getProperty("http://xml.org/sax/properties/declaration-handler"));
        } catch (SAXNotRecognizedException | SAXNotSupportedException e) {
            fail("Failed to set property");
        }
    }

    @Test
    public void testGetProperty() {
        SAXOutputter outputter = new SAXOutputter();
        try {
            assertNull(outputter.getProperty("invalid.property"));
        } catch (SAXNotRecognizedException | SAXNotSupportedException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testGetSAXOutputProcessor() {
        SAXOutputter outputter = new SAXOutputter();
        assertNotNull(outputter.getSAXOutputProcessor());
    }

    @Test
    public void testSetSAXOutputProcessor() {
        SAXOutputter outputter = new SAXOutputter();
        SAXOutputProcessor processor = new DefaultSAXOutputProcessor();
        outputter.setSAXOutputProcessor(processor);
        assertEquals(processor, outputter.getSAXOutputProcessor());
    }

    @Test
    public void testGetFormat() {
        SAXOutputter outputter = new SAXOutputter();
        assertNotNull(outputter.getFormat());
    }

    @Test
    public void testSetFormat() {
        SAXOutputter outputter = new SAXOutputter();
        Format format = Format.getDefaultFormat();
        outputter.setFormat(format);
        assertEquals(format, outputter.getFormat());
    }

    @Test
    public void testOutputDocument() {
        SAXOutputter outputter = new SAXOutputter();
        Document document = new Document();
        try {
            outputter.output(document);
        } catch (JDOMException e) {
            fail("Outputting document failed");
        }
    }

    @Test
    public void testOutputList() {
        SAXOutputter outputter = new SAXOutputter();
        List<Content> nodes = new ArrayList<>();
        try {
            outputter.output(nodes);
        } catch (JDOMException e) {
            fail("Outputting list failed");
        }
    }

    @Test
    public void testOutputElement() {
        SAXOutputter outputter = new SAXOutputter();
        Element element = new Element("test");
        try {
            outputter.output(element);
        } catch (JDOMException e) {
            fail("Outputting element failed");
        }
    }

    @Test
    public void testOutputFragmentList() {
        SAXOutputter outputter = new SAXOutputter();
        List<Content> nodes = new ArrayList<>();
        try {
            outputter.outputFragment(nodes);
        } catch (JDOMException e) {
            fail("Outputting fragment list failed");
        }
    }

    @Test
    public void testOutputFragmentContent() {
        SAXOutputter outputter = new SAXOutputter();
        Content content = new Text("Test");
        try {
            outputter.outputFragment(content);
        } catch (JDOMException e) {
            fail("Outputting fragment content failed");
        }
    }
}