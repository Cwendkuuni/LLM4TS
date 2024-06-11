import org.jdom2.*;
import org.xml.sax.*;
import org.jdom2.output.SAXOutputter;
import org.junit.*;
import static org.junit.Assert.*;

public class SAXOutputterTest {
    private SAXOutputter saxOutputter;

    @Before
    public void setUp() {
        saxOutputter = new SAXOutputter();
    }

    @After
    public void tearDown() {
        saxOutputter = null;
    }

    @Test
    public void testSetContentHandler() {
        ContentHandler contentHandler = new DefaultHandler();
        saxOutputter.setContentHandler(contentHandler);
        assertEquals(contentHandler, saxOutputter.getContentHandler());
    }

    @Test
    public void testSetErrorHandler() {
        ErrorHandler errorHandler = new DefaultHandler();
        saxOutputter.setErrorHandler(errorHandler);
        assertEquals(errorHandler, saxOutputter.getErrorHandler());
    }

    @Test
    public void testSetDTDHandler() {
        DTDHandler dtdHandler = new DefaultHandler();
        saxOutputter.setDTDHandler(dtdHandler);
        assertEquals(dtdHandler, saxOutputter.getDTDHandler());
    }

    @Test
    public void testSetEntityResolver() {
        EntityResolver entityResolver = new DefaultHandler();
        saxOutputter.setEntityResolver(entityResolver);
        assertEquals(entityResolver, saxOutputter.getEntityResolver());
    }

    @Test
    public void testSetLexicalHandler() {
        LexicalHandler lexicalHandler = new DefaultHandler();
        saxOutputter.setLexicalHandler(lexicalHandler);
        assertEquals(lexicalHandler, saxOutputter.getLexicalHandler());
    }

    @Test
    public void testSetDeclHandler() {
        DeclHandler declHandler = new DefaultHandler();
        saxOutputter.setDeclHandler(declHandler);
        assertEquals(declHandler, saxOutputter.getDeclHandler());
    }

    @Test
    public void testGetReportNamespaceDeclarations() {
        boolean declareNamespaces = true;
        saxOutputter.setReportNamespaceDeclarations(declareNamespaces);
        assertEquals(declareNamespaces, saxOutputter.getReportNamespaceDeclarations());
    }

    @Test
    public void testGetReportDTDEvents() {
        boolean reportDtdEvents = false;
        saxOutputter.setReportDTDEvents(reportDtdEvents);
        assertEquals(reportDtdEvents, saxOutputter.getReportDTDEvents());
    }

    @Test
    public void testSetFeature() throws SAXNotRecognizedException, SAXNotSupportedException {
        String name = "http://xml.org/sax/features/namespace-prefixes";
        boolean value = true;
        saxOutputter.setFeature(name, value);
        assertEquals(value, saxOutputter.getReportNamespaceDeclarations());
        
        name = "http://xml.org/sax/features/namespaces";
        value = false;
        try {
            saxOutputter.setFeature(name, value);
            fail("Expected SAXNotSupportedException");
        } catch (SAXNotSupportedException e) {
            // expected
        }
        
        name = "http://xml.org/sax/features/validation";
        value = false;
        saxOutputter.setFeature(name, value);
        assertEquals(value, saxOutputter.getReportDTDEvents());
    }

    @Test
    public void testGetFeature() throws SAXNotRecognizedException, SAXNotSupportedException {
        String name = "http://xml.org/sax/features/namespace-prefixes";
        boolean declareNamespaces = true;
        saxOutputter.setReportNamespaceDeclarations(declareNamespaces);
        assertTrue(saxOutputter.getFeature(name));
        
        name = "http://xml.org/sax/features/namespaces";
        assertTrue(saxOutputter.getFeature(name));
        
        name = "http://xml.org/sax/features/validation";
        boolean reportDtdEvents = false;
        saxOutputter.setReportDTDEvents(reportDtdEvents);
        assertEquals(reportDtdEvents, saxOutputter.getFeature(name));
    }

    @Test
    public void testSetProperty() throws SAXNotRecognizedException, SAXNotSupportedException {
        String name = "http://xml.org/sax/properties/lexical-handler";
        Object value = new DefaultHandler();
        saxOutputter.setProperty(name, value);
        assertEquals(value, saxOutputter.getLexicalHandler());
        
        name = "http://xml.org/sax/properties/declaration-handler";
        value = new DefaultHandler();
        saxOutputter.setProperty(name, value);
        assertEquals(value, saxOutputter.getDeclHandler());
    }

    @Test
    public void testGetProperty() throws SAXNotRecognizedException, SAXNotSupportedException {
        String name = "http://xml.org/sax/properties/lexical-handler";
        saxOutputter.setLexicalHandler(new DefaultHandler());
        assertNotNull(saxOutputter.getProperty(name));
        
        name = "http://xml.org/sax/properties/declaration-handler";
        saxOutputter.setDeclHandler(new DefaultHandler());
        assertNotNull(saxOutputter.getProperty(name));
    }
    
    @Test
    public void testGetSAXOutputProcessor() {
        assertNotNull(saxOutputter.getSAXOutputProcessor());
    }

    @Test
    public void testSetSAXOutputProcessor() {
        SAXOutputProcessor processor = new SAXOutputProcessor();
        saxOutputter.setSAXOutputProcessor(processor);
        assertEquals(processor, saxOutputter.getSAXOutputProcessor());
    }

    @Test
    public void testGetFormat() {
        assertNotNull(saxOutputter.getFormat());
    }

    @Test
    public void testSetFormat() {
        Format format = new Format();
        saxOutputter.setFormat(format);
        assertEquals(format, saxOutputter.getFormat());
    }

    @Test
    public void testBuildTarget() {
        Document document = new Document();
        SAXTarget target = saxOutputter.buildTarget(document);
        assertNotNull(target);
    }

    @Test
    public void testOutputDocument() throws JDOMException {
        Document document = new Document();
        try {
            saxOutputter.output(document);
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testOutputList() throws JDOMException {
        List<? extends Content> nodes = null;
        try {
            saxOutputter.output(nodes);
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testOutputElement() throws JDOMException {
        Element node = new Element("test");
        try {
            saxOutputter.output(node);
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testOutputFragmentList() throws JDOMException {
        List<? extends Content> nodes = null;
        try {
            saxOutputter.outputFragment(nodes);
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testOutputFragmentContent() throws JDOMException {
        Content node = null;
        try {
            saxOutputter.outputFragment(node);
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testHandleError() throws JDOMException {
        JDOMException exception = new JDOMException("Test Exception");
        try {
            saxOutputter.handleError(exception);
        } catch (JDOMException e) {
            assertEquals(exception.getMessage(), e.getMessage());
        }
    }

    @Test
    public void testGetLocator() {
        assertNull(saxOutputter.getLocator());
    }
}