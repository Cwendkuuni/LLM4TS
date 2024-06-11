package com.puppycrawl.tools.checkstyle.api;

import org.junit.*;
import static org.junit.Assert.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AbstractLoaderTest {

    @Test
    public void testParseInputSource() {
        try {
            AbstractLoader loader = new AbstractLoaderTestImpl();
            InputSource inputSource = new InputSource();
            loader.parseInputSource(inputSource);
        } catch (IOException | SAXException e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testResolveEntity() {
        try {
            AbstractLoader loader = new AbstractLoaderTestImpl();
            String publicId = "testPublicId";
            String systemId = "testSystemId";
            InputSource inputSource = loader.resolveEntity(publicId, systemId);
            assertNotNull(inputSource);
        } catch (SAXException | IOException e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test(expected = SAXException.class)
    public void testError() throws SAXException {
        AbstractLoader loader = new AbstractLoaderTestImpl();
        SAXParseException exception = new SAXParseException("Test error", null);
        loader.error(exception);
    }

    @Test(expected = SAXException.class)
    public void testFatalError() throws SAXException {
        AbstractLoader loader = new AbstractLoaderTestImpl();
        SAXParseException exception = new SAXParseException("Test fatal error", null);
        loader.fatalError(exception);
    }

    private class AbstractLoaderTestImpl extends AbstractLoader {
        public AbstractLoaderTestImpl() throws SAXException, ParserConfigurationException {
            super(new HashMap<>());
        }
    }
}