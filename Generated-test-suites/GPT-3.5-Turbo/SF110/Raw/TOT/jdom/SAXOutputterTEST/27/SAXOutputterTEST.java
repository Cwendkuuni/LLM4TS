import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SAXOutputterTest {

    private SAXOutputter saxOutputter;

    @Before
    public void setUp() {
        saxOutputter = new SAXOutputter();
    }

    @Test
    public void testsetContentHandler() {
        ContentHandler contentHandler = new CustomContentHandler();
        saxOutputter.setContentHandler(contentHandler);
        assertEquals(contentHandler, saxOutputter.getContentHandler());
    }

    @Test
    public void testsetErrorHandler() {
        ErrorHandler errorHandler = new CustomErrorHandler();
        saxOutputter.setErrorHandler(errorHandler);
        assertEquals(errorHandler, saxOutputter.getErrorHandler());
    }

    @Test
    public void testsetDTDHandler() {
        DTDHandler dtdHandler = new CustomDTDHandler();
        saxOutputter.setDTDHandler(dtdHandler);
        assertEquals(dtdHandler, saxOutputter.getDTDHandler());
    }

    @Test
    public void testsetEntityResolver() {
        EntityResolver entityResolver = new CustomEntityResolver();
        saxOutputter.setEntityResolver(entityResolver);
        assertEquals(entityResolver, saxOutputter.getEntityResolver());
    }

    @Test
    public void testsetLexicalHandler() {
        LexicalHandler lexicalHandler = new CustomLexicalHandler();
        saxOutputter.setLexicalHandler(lexicalHandler);
        assertEquals(lexicalHandler, saxOutputter.getLexicalHandler());
    }

    @Test
    public void testsetDeclHandler() {
        DeclHandler declHandler = new CustomDeclHandler();
        saxOutputter.setDeclHandler(declHandler);
        assertEquals(declHandler, saxOutputter.getDeclHandler());
    }

    @Test
    public void testgetReportNamespaceDeclarations() {
        assertFalse(saxOutputter.getReportNamespaceDeclarations());
        saxOutputter.setReportNamespaceDeclarations(true);
        assertTrue(saxOutputter.getReportNamespaceDeclarations());
    }

    @Test
    public void testgetReportDTDEvents() {
        assertTrue(saxOutputter.getReportDTDEvents());
        saxOutputter.setReportDTDEvents(false);
        assertFalse(saxOutputter.getReportDTDEvents());
    }

    @Test
    public void testsetFeature_validName() throws SAXNotRecognizedException, SAXNotSupportedException {
        boolean value = true;
        saxOutputter.setFeature("http://xml.org/sax/features/namespace-prefixes", value);
        assertEquals(value, saxOutputter.getReportNamespaceDeclarations());
    }

    @Test(expected = SAXNotSupportedException.class)
    public void testsetFeature_invalidNamespacePrefix() throws SAXNotRecognizedException, SAXNotSupportedException {
        boolean value = false;
        saxOutputter.setFeature("http://xml.org/sax/features/namespaces", value);
    }

    @Test(expected = SAXNotRecognizedException.class)
    public void testsetFeature_unrecognizedFeature() throws SAXNotRecognizedException, SAXNotSupportedException {
        boolean value = true;
        saxOutputter.setFeature("http://xml.org/sax/features/test", value);
    }

    @Test
    public void testgetFeature_validName() throws SAXNotRecognizedException, SAXNotSupportedException {
        boolean expectedValue = false;
        assertEquals(expectedValue, saxOutputter.getFeature("http://xml.org/sax/features/namespace-prefixes"));
    }

    @Test(expected = SAXNotRecognizedException.class)
    public void testgetFeature_unrecognizedFeature() throws SAXNotRecognizedException, SAXNotSupportedException {
        saxOutputter.getFeature("http://xml.org/sax/features/test");
    }

    @Test
    public void testsetProperty_lexicalHandler() throws SAXNotRecognizedException, SAXNotSupportedException {
        LexicalHandler lexicalHandler = new CustomLexicalHandler();
        saxOutputter.setProperty("http://xml.org/sax/properties/lexical-handler", lexicalHandler);
        assertEquals(lexicalHandler, saxOutputter.getLexicalHandler());
    }

    @Test
    public void testgetProperty_lexicalHandler() throws SAXNotRecognizedException, SAXNotSupportedException {
        LexicalHandler expectedHandler = new CustomLexicalHandler();
        saxOutputter.setLexicalHandler(expectedHandler);
        assertEquals(expectedHandler, saxOutputter.getProperty("http://xml.org/sax/properties/lexical-handler"));
    }

    @Test
    public void testsetProperty_declHandler() throws SAXNotRecognizedException, SAXNotSupportedException {
        DeclHandler declHandler = new CustomDeclHandler();
        saxOutputter.setProperty("http://xml.org/sax/properties/declaration-handler", declHandler);
        assertEquals(declHandler, saxOutputter.getDeclHandler());
    }

    @Test(expected = SAXNotRecognizedException.class)
    public void testsetProperty_unrecognizedProperty() throws SAXNotRecognizedException, SAXNotSupportedException {
        saxOutputter.setProperty("http://xml.org/sax/properties/test", null);
    }

    @Test
    public void testgetProperty_declHandler() throws SAXNotRecognizedException, SAXNotSupportedException {
        DeclHandler expectedHandler = new CustomDeclHandler();
        saxOutputter.setDeclHandler(expectedHandler);
        assertEquals(expectedHandler, saxOutputter.getProperty("http://xml.org/sax/properties/declaration-handler"));
    }

    @Test
    public void testgetSAXOutputProcessor() {
        SAXOutputProcessor outputProcessor = saxOutputter.getSAXOutputProcessor();
        assertNotNull(outputProcessor);
    }

    @Test
    public void testsetSAXOutputProcessor() {
        SAXOutputProcessor customOutputProcessor = new CustomSAXOutputProcessor();
        saxOutputter.setSAXOutputProcessor(customOutputProcessor);
        assertEquals(customOutputProcessor, saxOutputter.getSAXOutputProcessor());
    }

    @Test
    public void testgetFormat() {
        Format format = saxOutputter.getFormat();
        assertNotNull(format);
    }

    @Test
    public void testsetFormat() {
        Format customFormat = new CustomFormat();
        saxOutputter.setFormat(customFormat);
        assertEquals(customFormat, saxOutputter.getFormat());
    }

    @Test
    public void testbuildTarget_withDocument() {
        Document document = new Document();
        SAXTarget saxTarget = saxOutputter.buildTarget(document);
        assertNotNull(saxTarget);
    }

    @Test
    public void testbuildTarget_withoutDocument() {
        SAXTarget saxTarget = saxOutputter.buildTarget(null);
        assertNotNull(saxTarget);
    }

    @Test
    public void testoutput_withDocument() {
        Document document = new Document();
        try {
            saxOutputter.output(document);
        } catch (JDOMException e) {
            fail("Unexpected JDOMException thrown");
        }
    }

    @Test
    public void testoutput_withList() {
        List<? extends Content> nodes = new ArrayList<>();
        try {
            saxOutputter.output(nodes);
        } catch (JDOMException e) {
            fail("Unexpected JDOMException thrown");
        }
    }

    @Test
    public void testoutput_withElement() {
        Element element = new Element("test");
        try {
            saxOutputter.output(element);
        } catch (JDOMException e) {
            fail("Unexpected JDOMException thrown");
        }
    }

    @Test
    public void testoutputFragment_withList() {
        List<? extends Content> nodes = new ArrayList<>();
        try {
            saxOutputter.outputFragment(nodes);
        } catch (JDOMException e) {
            fail("Unexpected JDOMException thrown");
        }
    }

    @Test
    public void testoutputFragment_withContent() {
        Element element = new Element("test");
        try {
            saxOutputter.outputFragment(element);
        } catch (JDOMException e) {
            fail("Unexpected JDOMException thrown");
        }
    }
}