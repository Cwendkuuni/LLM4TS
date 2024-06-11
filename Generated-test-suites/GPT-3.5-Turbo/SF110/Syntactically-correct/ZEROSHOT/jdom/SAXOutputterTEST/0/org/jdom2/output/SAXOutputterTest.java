import org.jdom2.*;
import java.util.*;
import org.junit.*;
import org.xml.sax.*;

public class SAXOutputterTest {

    @Test
    public void testConstructor() {
        SAXOutputter saxOutputter = new SAXOutputter();
        Assert.assertFalse(saxOutputter.getReportNamespaceDeclarations());
        Assert.assertTrue(saxOutputter.getReportDTDEvents());
        Assert.assertNotNull(saxOutputter.getSAXOutputProcessor());
        Assert.assertNotNull(saxOutputter.getFormat());
        Assert.assertNull(saxOutputter.getContentHandler());
        Assert.assertNull(saxOutputter.getErrorHandler());
        Assert.assertNull(saxOutputter.getDTDHandler());
        Assert.assertNull(saxOutputter.getEntityResolver());
        Assert.assertNull(saxOutputter.getLexicalHandler());
        Assert.assertNull(saxOutputter.getDeclHandler());
    }

    @Test
    public void testConstructorWithContentHandler() {
        ContentHandler contentHandler = new DefaultHandler();
        SAXOutputter saxOutputter = new SAXOutputter(contentHandler);
        Assert.assertEquals(contentHandler, saxOutputter.getContentHandler());
        Assert.assertNull(saxOutputter.getErrorHandler());
        Assert.assertNull(saxOutputter.getDTDHandler());
        Assert.assertNull(saxOutputter.getEntityResolver());
        Assert.assertNull(saxOutputter.getLexicalHandler());
        Assert.assertNull(saxOutputter.getDeclHandler());
    }

    @Test
    public void testConstructorWithAllParameters() {
        ContentHandler contentHandler = new DefaultHandler();
        ErrorHandler errorHandler = new DefaultHandler();
        DTDHandler dtdHandler = new DefaultHandler();
        EntityResolver entityResolver = new DefaultHandler();
        LexicalHandler lexicalHandler = new DefaultHandler();
        SAXOutputter saxOutputter = new SAXOutputter(contentHandler, errorHandler, dtdHandler, entityResolver, lexicalHandler);
        Assert.assertEquals(contentHandler, saxOutputter.getContentHandler());
        Assert.assertEquals(errorHandler, saxOutputter.getErrorHandler());
        Assert.assertEquals(dtdHandler, saxOutputter.getDTDHandler());
        Assert.assertEquals(entityResolver, saxOutputter.getEntityResolver());
        Assert.assertEquals(lexicalHandler, saxOutputter.getLexicalHandler());
        Assert.assertNull(saxOutputter.getDeclHandler());
    }

    @Test
    public void testSetters() {
        SAXOutputter saxOutputter = new SAXOutputter();
        ContentHandler contentHandler = new DefaultHandler();
        ErrorHandler errorHandler = new DefaultHandler();
        DTDHandler dtdHandler = new DefaultHandler();
        EntityResolver entityResolver = new DefaultHandler();
        LexicalHandler lexicalHandler = new DefaultHandler();
        DeclHandler declHandler = new DefaultHandler();
        saxOutputter.setContentHandler(contentHandler);
        saxOutputter.setErrorHandler(errorHandler);
        saxOutputter.setDTDHandler(dtdHandler);
        saxOutputter.setEntityResolver(entityResolver);
        saxOutputter.setLexicalHandler(lexicalHandler);
        saxOutputter.setDeclHandler(declHandler);
        Assert.assertEquals(contentHandler, saxOutputter.getContentHandler());
        Assert.assertEquals(errorHandler, saxOutputter.getErrorHandler());
        Assert.assertEquals(dtdHandler, saxOutputter.getDTDHandler());
        Assert.assertEquals(entityResolver, saxOutputter.getEntityResolver());
        Assert.assertEquals(lexicalHandler, saxOutputter.getLexicalHandler());
        Assert.assertEquals(declHandler, saxOutputter.getDeclHandler());
    }

    @Test
    public void testFeature() {
        SAXOutputter saxOutputter = new SAXOutputter();
        try {
            saxOutputter.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
            Assert.assertTrue(saxOutputter.getReportNamespaceDeclarations());
            saxOutputter.setFeature("http://xml.org/sax/features/namespaces", false);
            Assert.assertTrue(saxOutputter.getReportDTDEvents());
            saxOutputter.setFeature("http://xml.org/sax/features/validation", true);
            Assert.assertFalse(saxOutputter.getReportDTDEvents());
        } catch (SAXNotSupportedException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (SAXNotRecognizedException e) {
            e.printStackTrace();
            Assert.fail();
        }

        try {
            saxOutputter.setFeature("invalid_feature", true);
            Assert.fail();
        } catch (SAXNotSupportedException e) {
            // expected exception
        } catch (SAXNotRecognizedException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testGetProperty() {
        SAXOutputter saxOutputter = new SAXOutputter();
        try {
            saxOutputter.getProperty("http://xml.org/sax/properties/lexical-handler");
            saxOutputter.getProperty("http://xml.org/sax/properties/declaration-handler");
        } catch (SAXNotSupportedException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (SAXNotRecognizedException e) {
            e.printStackTrace();
            Assert.fail();
        }

        try {
            saxOutputter.getProperty("invalid_property");
            Assert.fail();
        } catch (SAXNotSupportedException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (SAXNotRecognizedException e) {
            // expected exception
        }
    }

    @Test
    public void testOutput() {
        SAXOutputter saxOutputter = new SAXOutputter();
        Document document = new Document();

        try {
            saxOutputter.output(document);
        } catch (JDOMException e) {
            e.printStackTrace();
            Assert.fail();
        }

        List<Content> nodes = new ArrayList<Content>();
        try {
            saxOutputter.output(nodes);
        } catch (JDOMException e) {
            e.printStackTrace();
            Assert.fail();
        }

        Element element = new Element("element");
        try {
            saxOutputter.output(element);
        } catch (JDOMException e) {
            e.printStackTrace();
            Assert.fail();
        }

        List<Content> fragmentNodes = new ArrayList<Content>();
        try {
            saxOutputter.outputFragment(fragmentNodes);
        } catch (JDOMException e) {
            e.printStackTrace();
            Assert.fail();
        }

        try {
            saxOutputter.outputFragment(null);
        } catch (JDOMException e) {
            e.printStackTrace();
            Assert.fail();
        }

        CDATA cdata = new CDATA("cdata");
        Comment comment = new Comment("comment");
        EntityRef entityRef = new EntityRef("entityRef");
        ProcessingInstruction processingInstruction = new ProcessingInstruction("target", "data");
        Text text = new Text("text");

        try {
            saxOutputter.outputFragment(cdata);
        } catch (JDOMException e) {
            e.printStackTrace();
            Assert.fail();
        }

        try {
            saxOutputter.outputFragment(comment);
        } catch (JDOMException e) {
            e.printStackTrace();
            Assert.fail();
        }

        try {
            saxOutputter.outputFragment(entityRef);
        } catch (JDOMException e) {
            e.printStackTrace();
            Assert.fail();
        }

        try {
            saxOutputter.outputFragment(processingInstruction);
        } catch (JDOMException e) {
            e.printStackTrace();
            Assert.fail();
        }

        try {
            saxOutputter.outputFragment(text);
        } catch (JDOMException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testHandleError() throws JDOMException {
        SAXOutputter saxOutputter = new SAXOutputter();
        saxOutputter.setErrorHandler(new DefaultHandler());

        JDOMException exception = new JDOMException("error");
        try {
            saxOutputter.handleError(exception);
        } catch (JDOMException e) {
            e.printStackTrace();
            Assert.fail();
        }

        saxOutputter.setErrorHandler(null);
        try {
            saxOutputter.handleError(exception);
            Assert.fail();
        } catch (JDOMException e) {
            Assert.assertEquals(exception, e);
        }
    }

    @Test
    public void testGetLocator() {
        SAXOutputter saxOutputter = new SAXOutputter();
        Assert.assertNull(saxOutputter.getLocator());
    }
}