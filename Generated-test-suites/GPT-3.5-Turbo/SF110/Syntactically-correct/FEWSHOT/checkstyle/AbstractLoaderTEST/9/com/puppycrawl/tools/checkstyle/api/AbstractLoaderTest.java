package com.puppycrawl.tools.checkstyle.api;

import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AbstractLoaderTest {

    @Test
    public void testConstructorWithPublicIdAndDtdResourceName() {
        try {
            AbstractLoader loader = new AbstractLoader("publicId", "dtdResourceName") {
                @Override
                public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                    return null;
                }

                @Override
                public void error(SAXParseException exception) throws SAXException {
                    // TODO Auto-generated method stub
                    
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                    // TODO Auto-generated method stub
                    
                }
            };
            assertNotNull(loader);
        } catch (SAXException | ParserConfigurationException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testConstructorWithPublicIdToResourceNameMap() {
        try {
            Map<String, String> publicIdToResourceNameMap = new HashMap<>();
            publicIdToResourceNameMap.put("publicId", "dtdResourceName");

            AbstractLoader loader = new AbstractLoader(publicIdToResourceNameMap) {
                @Override
                public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                    return null;
                }

                @Override
                public void error(SAXParseException exception) throws SAXException {
                    // TODO Auto-generated method stub
                    
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                    // TODO Auto-generated method stub
                    
                }
            };
            assertNotNull(loader);
        } catch (SAXException | ParserConfigurationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testParseInputSource() {
        try {
            AbstractLoader loader = new AbstractLoader("") {
                @Override
                public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                    return null;
                }

                @Override
                public void error(SAXParseException exception) throws SAXException {
                    // TODO Auto-generated method stub
                    
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                    // TODO Auto-generated method stub
                    
                }
            };
            InputSource inputSource = new InputSource();
            loader.parseInputSource(inputSource);
        } catch (IOException | SAXException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testResolveEntity() {
        try {
            AbstractLoader loader = new AbstractLoader("") {
                @Override
                public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                    return null;
                }

                @Override
                public void error(SAXParseException exception) throws SAXException {
                    // TODO Auto-generated method stub
                    
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                    // TODO Auto-generated method stub
                    
                }
            };
            InputSource inputSource = loader.resolveEntity("publicId", "systemId");
            assertNull(inputSource);
        } catch (IOException | SAXException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testError() {
        try {
            AbstractLoader loader = new AbstractLoader("") {
                @Override
                public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                    return null;
                }

                @Override
                public void error(SAXParseException exception) throws SAXException {
                    assertEquals("Error", exception.getMessage());
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                    // TODO Auto-generated method stub
                    
                }
            };
            SAXParseException exception = new SAXParseException("Error", null);
            loader.error(exception);
        } catch (SAXException | IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testFatalError() {
        try {
            AbstractLoader loader = new AbstractLoader("") {
                @Override
                public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                    return null;
                }

                @Override
                public void error(SAXParseException exception) throws SAXException {
                    // TODO Auto-generated method stub
                    
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                    assertEquals("Fatal Error", exception.getMessage());
                }
            };
            SAXParseException exception = new SAXParseException("Fatal Error", null);
            loader.fatalError(exception);
        } catch (SAXException | IOException e) {
            fail(e.getMessage());
        }
    }
}