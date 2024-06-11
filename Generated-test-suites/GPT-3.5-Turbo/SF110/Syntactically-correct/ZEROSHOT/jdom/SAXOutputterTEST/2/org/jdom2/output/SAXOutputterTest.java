import org.junit.Before;
import org.junit.Test;
import org.jdom2.output.SAXOutputter;
import java.util.ArrayList;
import java.util.List;
import org.jdom2.*;
import org.jdom2.output.support.*;
import org.xml.sax.ext.*;
import org.xml.sax.*;
import static org.junit.Assert.*;

public class SAXOutputterTest {

    private SAXOutputter saxOutputter;

    @Before
    public void setUp() {
        saxOutputter = new SAXOutputter();
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
    public void testSetReportNamespaceDeclarations() {
        boolean declareNamespaces = false;
        saxOutputter.setReportNamespaceDeclarations(declareNamespaces);

        assertEquals(declareNamespaces, saxOutputter.getReportNamespaceDeclarations());
    }

    @Test
    public void testSetReportDTDEvents() {
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
        value = true;
        saxOutputter.setFeature(name, value);

        assertTrue(saxOutputter.getFeature(name));

        name = "http://xml.org/sax/features/validation";
        value = true;
        saxOutputter.setFeature(name, value);

        assertEquals(value, saxOutputter.getReportDTDEvents());
    }

    @Test
    public void testGetFeature() throws SAXNotRecognizedException, SAXNotSupportedException {
        String name = "http://xml.org/sax/features/namespace-prefixes";
        assertTrue(saxOutputter.getFeature(name));

        name = "http://xml.org/sax/features/namespaces";
        assertTrue(saxOutputter.getFeature(name));

        name = "http://xml.org/sax/features/validation";
        assertTrue(saxOutputter.getFeature(name));
    }

    @Test(expected = SAXNotRecognizedException.class)
    public void testGetFeatureInvalidName() throws SAXNotRecognizedException, SAXNotSupportedException {
        saxOutputter.getFeature("invalidName");
    }

    @Test(expected = SAXNotRecognizedException.class)
    public void testSetPropertyInvalidName() throws SAXNotRecognizedException, SAXNotSupportedException {
        saxOutputter.setProperty("invalidName", null);
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
        assertNull(saxOutputter.getProperty(name));

        name = "http://xml.org/sax/properties/declaration-handler";
        assertNull(saxOutputter.getProperty(name));
    }

    @Test(expected = SAXNotRecognizedException.class)
    public void testGetPropertyInvalidName() throws SAXNotRecognizedException, SAXNotSupportedException {
        saxOutputter.getProperty("invalidName");
    }

    @Test
    public void testGetSAXOutputProcessor() {
        SAXOutputProcessor processor = saxOutputter.getSAXOutputProcessor();

        assertEquals(SAXOutputter.DEFAULT_PROCESSOR, processor);
    }

    @Test
    public void testSetSAXOutputProcessor() {
        SAXOutputProcessor processor = new DefaultSAXOutputProcessor();
        saxOutputter.setSAXOutputProcessor(processor);

        assertEquals(processor, saxOutputter.getSAXOutputProcessor());
    }

    @Test
    public void testGetFormat() {
        Format format = saxOutputter.getFormat();

        assertEquals(Format.getRawFormat(), format);
    }

    @Test
    public void testSetFormat() {
        Format format = Format.getPrettyFormat();
        saxOutputter.setFormat(format);

        assertEquals(format, saxOutputter.getFormat());
    }

    @Test
    public void testBuildTarget() {
        Document doc = new Document();
        SAXTarget target = saxOutputter.buildTarget(doc);

        assertNotNull(target);
        assertEquals(saxOutputter.getContentHandler(), target.getContentHandler());
        assertEquals(saxOutputter.getErrorHandler(), target.getErrorHandler());
        assertEquals(saxOutputter.getDTDHandler(), target.getDTDHandler());
        assertEquals(saxOutputter.getEntityResolver(), target.getEntityResolver());
        assertEquals(saxOutputter.getLexicalHandler(), target.getLexicalHandler());
        assertEquals(saxOutputter.getDeclHandler(), target.getDeclHandler());
        assertEquals(saxOutputter.getReportNamespaceDeclarations(), target.shouldDeclareNamespaces());
        assertEquals(saxOutputter.getReportDTDEvents(), target.shouldReportDtdEvents());
    }

    @Test
    public void testOutputDocument() throws JDOMException {
        Document doc = new Document();
        try {
            saxOutputter.output(doc);
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testOutputList() throws JDOMException {
        List<Content> nodes = new ArrayList<>();
        try {
            saxOutputter.output(nodes);
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testOutputElement() throws JDOMException {
        Element node = new Element("test");
        try {
            saxOutputter.output(node);
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testOutputFragmentList() throws JDOMException {
        List<Content> nodes = new ArrayList<>();
        try {
            saxOutputter.outputFragment(nodes);
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testOutputFragmentContent() throws JDOMException {
        Content node = new Text("test");
        try {
            saxOutputter.outputFragment(node);
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }
}