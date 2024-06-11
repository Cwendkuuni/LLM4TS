package com.puppycrawl.tools.checkstyle.api;

import org.junit.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AbstractLoaderTest {
    
    private AbstractLoader abstractLoader;
    
    private class MockAbstractLoader extends AbstractLoader {
        public MockAbstractLoader(String publicId, String dtdResourceName) throws SAXException, ParserConfigurationException {
            super(publicId, dtdResourceName);
        }
        
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            // implement your own startElement logic
        }
        
        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            // implement your own endElement logic
        }
        
        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            // implement your own characters logic
        }
    }
    
    @Before
    public void setUp() throws SAXException, ParserConfigurationException {
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("publicId", "dtdResourceName.dtd");
        abstractLoader = new MockAbstractLoader("publicId", "dtdResourceName.dtd", publicIdToResourceNameMap);
    }
    
    @Test
    public void testParseInputSource() throws IOException, SAXException {
        InputSource inputSource = new InputSource();
        abstractLoader.parseInputSource(inputSource);
        // add your assertions
    }
    
    @Test
    public void testResolveEntity() throws IOException, SAXException {
        InputSource inputSource = abstractLoader.resolveEntity("publicId", "systemId");
        assertNotNull(inputSource);
        // add your assertions
    }
    
    @Test
    public void testError() throws SAXException {
        SAXParseException exception = new SAXParseException("test", null);
        try {
            abstractLoader.error(exception);
        } catch (SAXException e) {
            assertEquals(exception, e);
            return;
        }
        assertTrue("Exception not thrown", false);
    }
    
    @Test
    public void testFatalError() throws SAXException {
        SAXParseException exception = new SAXParseException("test", null);
        try {
            abstractLoader.fatalError(exception);
        } catch (SAXException e) {
            assertEquals(exception, e);
            return;
        }
        assertTrue("Exception not thrown", false);
    }
}