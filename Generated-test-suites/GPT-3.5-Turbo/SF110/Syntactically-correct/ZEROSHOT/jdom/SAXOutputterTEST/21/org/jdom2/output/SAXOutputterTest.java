
package org.jdom2.output;

import org.xml.sax.ext.*;
import java.util.*;
import org.jdom2.*;
import org.xml.sax.*;
import org.jdom2.output.support.*;
import org.junit.*;
import static org.junit.Assert.*;

public class SAXOutputterTest {
    
    @Test
    public void testSetContentHandler() {
        ContentHandler contentHandler = new ContentHandlerImpl();
        SAXOutputter outputter = new SAXOutputter();
        outputter.setContentHandler(contentHandler);
        assertEquals(contentHandler, outputter.getContentHandler());
    }
    
    @Test
    public void testSetErrorHandler() {
        ErrorHandler errorHandler = new ErrorHandlerImpl();
        SAXOutputter outputter = new SAXOutputter();
        outputter.setErrorHandler(errorHandler);
        assertEquals(errorHandler, outputter.getErrorHandler());
    }
    
    @Test
    public void testSetDTDHandler() {
        DTDHandler dtdHandler = new DTDHandlerImpl();
        SAXOutputter outputter = new SAXOutputter();
        outputter.setDTDHandler(dtdHandler);
        assertEquals(dtdHandler, outputter.getDTDHandler());
    }
    
    @Test
    public void testSetEntityResolver() {
        EntityResolver entityResolver = new EntityResolverImpl();
        SAXOutputter outputter = new SAXOutputter();
        outputter.setEntityResolver(entityResolver);
        assertEquals(entityResolver, outputter.getEntityResolver());
    }
    
    @Test
    public void testSetLexicalHandler() {
        LexicalHandler lexicalHandler = new LexicalHandlerImpl();
        SAXOutputter outputter = new SAXOutputter();
        outputter.setLexicalHandler(lexicalHandler);
        assertEquals(lexicalHandler, outputter.getLexicalHandler());
    }
    
    @Test
    public void testSetDeclHandler() {
        DeclHandler declHandler = new DeclHandlerImpl();
        SAXOutputter outputter = new SAXOutputter();
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
        String name = "http://xml.org/sax/features/namespace-prefixes";
        boolean value = true;
        SAXOutputter outputter = new SAXOutputter();
        outputter.setFeature(name, value);
        assertTrue(outputter.getReportNamespaceDeclarations());
        
        name = "http://xml.org/sax/features/namespaces";
        value = false;
        try {
            outputter.setFeature(name, value);
            fail("Should throw SAXNotSupportedException");
        } catch (SAXNotSupportedException e) {
            // Expected
        }
        
        name = "http://xml.org/sax/features/validation";
        value = false;
        outputter.setFeature(name, value);
        assertFalse(outputter.getReportDTDEvents());
    }
    
    @Test
    public void testGetFeature() {
        String name = "http://xml.org/sax/features/namespace-prefixes";
        SAXOutputter outputter = new SAXOutputter();
        assertTrue(outputter.getFeature(name));
        
        name = "http://xml.org/sax/features/namespaces";
        assertTrue(outputter.getFeature(name));
        
        name = "http://xml.org/sax/features/validation";
        assertTrue(outputter.getFeature(name));
        
        name = "unknown-feature";
        try {
            outputter.getFeature(name);
            fail("Should throw SAXNotRecognizedException");
        } catch (SAXNotRecognizedException e) {
            // Expected
        }
    }
    
    @Test
    public void testSetProperty() {
        String name = "http://xml.org/sax/properties/lexical-handler";
        LexicalHandler value = new LexicalHandlerImpl();
        SAXOutputter outputter = new SAXOutputter();
        outputter.setProperty(name, value);
        assertEquals(value, outputter.getLexicalHandler());
        
        name = "http://xml.org/sax/properties/declaration-handler";
        DeclHandler value2 = new DeclHandlerImpl();
        outputter.setProperty(name, value2);
        assertEquals(value2, outputter.getDeclHandler());
        
        name = "unknown-property";
        try {
            outputter.setProperty(name, value);
            fail("Should throw SAXNotRecognizedException");
        } catch (SAXNotRecognizedException e) {
            // Expected
        }
    }
    
    @Test
    public void testGetProperty() {
        String name = "http://xml.org/sax/properties/lexical-handler";
        SAXOutputter outputter = new SAXOutputter();
        assertNull(outputter.getProperty(name));
        
        name = "http://xml.org/sax/properties/declaration-handler";
        assertNull(outputter.getProperty(name));
        
        name = "unknown-property";
        try {
            outputter.getProperty(name);
            fail("Should throw SAXNotRecognizedException");
        } catch (SAXNotRecognizedException e) {
            // Expected
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
        SAXOutputProcessor processor = new SAXOutputProcessorImpl();
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
        Format format = new Format();
        outputter.setFormat(format);
        assertEquals(format, outputter.getFormat());
    }
    
    @Test
    public void testBuildTarget() {
        Document doc = new Document(new Element("root"));
        SAXOutputter outputter = new SAXOutputter();
        SAXTarget target = outputter.buildTarget(doc);
        assertNotNull(target);
        assertEquals("root", target.getRootName());
    }
    
    @Test
    public void testOutput() throws JDOMException {
        Document doc = new Document(new Element("root"));
        SAXOutputter outputter = new SAXOutputter();
        outputter.setContentHandler(new ContentHandlerImpl());
        outputter.output(doc);
        // Verify output
    }
    
    @Test
    public void testOutputList() throws JDOMException {
        List<Content> nodes = new ArrayList<>();
        nodes.add(new Element("element"));
        nodes.add(new Text("text"));
        SAXOutputter outputter = new SAXOutputter();
        outputter.setContentHandler(new ContentHandlerImpl());
        outputter.output(nodes);
        // Verify output
    }
    
    @Test
    public void testOutputFragment() throws JDOMException {
        Element element = new Element("element");
        SAXOutputter outputter = new SAXOutputter();
        outputter.setContentHandler(new ContentHandlerImpl());
        outputter.outputFragment(element);
        // Verify output
    }
}
