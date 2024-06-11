import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.*;

public class SAXOutputterTest {

    @Test
    public void testContentHandler() {
        ContentHandler contentHandler = new MyContentHandler();
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setContentHandler(contentHandler);
        Assert.assertEquals(contentHandler, saxOutputter.getContentHandler());
    }

    @Test
    public void testErrorHandler() {
        ErrorHandler errorHandler = new MyErrorHandler();
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setErrorHandler(errorHandler);
        Assert.assertEquals(errorHandler, saxOutputter.getErrorHandler());
    }

    @Test
    public void testDTDHandler() {
        DTDHandler dtdHandler = new MyDTDHandler();
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setDTDHandler(dtdHandler);
        Assert.assertEquals(dtdHandler, saxOutputter.getDTDHandler());
    }

    @Test
    public void testEntityResolver() {
        EntityResolver entityResolver = new MyEntityResolver();
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setEntityResolver(entityResolver);
        Assert.assertEquals(entityResolver, saxOutputter.getEntityResolver());
    }

    @Test
    public void testLexicalHandler() {
        LexicalHandler lexicalHandler = new MyLexicalHandler();
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setLexicalHandler(lexicalHandler);
        Assert.assertEquals(lexicalHandler, saxOutputter.getLexicalHandler());
    }

    @Test
    public void testDeclHandler() {
        DeclHandler declHandler = new MyDeclHandler();
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setDeclHandler(declHandler);
        Assert.assertEquals(declHandler, saxOutputter.getDeclHandler());
    }

    @Test
    public void testReportNamespaceDeclarations() {
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setReportNamespaceDeclarations(true);
        Assert.assertTrue(saxOutputter.getReportNamespaceDeclarations());
    }

    @Test
    public void testReportDTDEvents() {
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setReportDTDEvents(false);
        Assert.assertFalse(saxOutputter.getReportDTDEvents());
    }

    @Test
    public void testFeature() {
        SAXOutputter saxOutputter = new SAXOutputter();
        try {
            saxOutputter.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
            Assert.assertTrue(saxOutputter.getReportNamespaceDeclarations());

            saxOutputter.setFeature("http://xml.org/sax/features/namespaces", true);

            boolean exceptionThrown = false;
            try {
                saxOutputter.setFeature("http://xml.org/sax/features/namespaces", false);
            } catch (SAXNotSupportedException e) {
                exceptionThrown = true;
            }
            Assert.assertTrue(exceptionThrown);

            saxOutputter.setFeature("http://xml.org/sax/features/validation", false);
            Assert.assertFalse(saxOutputter.getReportDTDEvents());

            exceptionThrown = false;
            try {
                saxOutputter.setFeature("http://xml.org/sax/features/invalid", false);
            } catch (SAXNotRecognizedException e) {
                exceptionThrown = true;
            }
            Assert.assertTrue(exceptionThrown);

        } catch (SAXNotRecognizedException | SAXNotSupportedException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetProperty() {
        SAXOutputter saxOutputter = new SAXOutputter();
        try {
            saxOutputter.setContentHandler(new MyContentHandler());
            Assert.assertEquals(saxOutputter.getLexicalHandler(), saxOutputter.getProperty("http://xml.org/sax/properties/lexical-handler"));
            Assert.assertEquals(saxOutputter.getDeclHandler(), saxOutputter.getProperty("http://xml.org/sax/properties/declaration-handler"));

            boolean exceptionThrown = false;
            try {
                saxOutputter.getProperty("http://xml.org/sax/properties/invalid");
            } catch (SAXNotRecognizedException e) {
                exceptionThrown = true;
            }
            Assert.assertTrue(exceptionThrown);

        } catch (SAXNotRecognizedException | SAXNotSupportedException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSAXOutputProcessor() {
        SAXOutputProcessor saxOutputProcessor = new MySAXOutputProcessor();
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setSAXOutputProcessor(saxOutputProcessor);
        Assert.assertEquals(saxOutputProcessor, saxOutputter.getSAXOutputProcessor());
    }

    @Test
    public void testFormat() {
        Format format = Format.getRawFormat().setExpandEmptyElements(true);
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setFormat(format);
        Assert.assertEquals(format, saxOutputter.getFormat());
    }

    @Test
    public void testBuildTarget() {
        SAXOutputter saxOutputter = new SAXOutputter();
        Document document = new Document();
        SAXTarget target = saxOutputter.buildTarget(document);
        Assert.assertNotNull(target);
    }

    @Test
    public void testOutput() {
        SAXOutputter saxOutputter = new SAXOutputter();
        Document document = new Document();
        try {
            saxOutputter.output(document);
        } catch (JDOMException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testOutputList() {
        SAXOutputter saxOutputter = new SAXOutputter();
        List<Content> nodes = new ArrayList<>();
        try {
            saxOutputter.output(nodes);
        } catch (JDOMException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testOutputElement() {
        SAXOutputter saxOutputter = new SAXOutputter();
        Element element = new Element("test");
        try {
            saxOutputter.output(element);
        } catch (JDOMException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testOutputFragmentList() {
        SAXOutputter saxOutputter = new SAXOutputter();
        List<Content> nodes = new ArrayList<>();
        try {
            saxOutputter.outputFragment(nodes);
        } catch (JDOMException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testOutputFragmentContent() {
        SAXOutputter saxOutputter = new SAXOutputter();
        CDATA cdata = new CDATA("test");
        try {
            saxOutputter.outputFragment(cdata);
        } catch (JDOMException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testHandleError() {
        SAXOutputter saxOutputter = new SAXOutputter();
        JDOMException exception = new JDOMException("test");
        boolean exceptionThrown = false;
        try {
            saxOutputter.handleError(exception);
        } catch (JDOMException e) {
            exceptionThrown = true;
            Assert.assertEquals(exception, e);
        }
        Assert.assertTrue(exceptionThrown);
    }

}