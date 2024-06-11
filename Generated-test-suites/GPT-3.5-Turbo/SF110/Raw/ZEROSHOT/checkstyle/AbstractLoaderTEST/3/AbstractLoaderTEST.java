package com.puppycrawl.tools.checkstyle.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AbstractLoaderTest {

    private AbstractLoader abstractLoader;

    @Before
    public void setUp() throws ParserConfigurationException, SAXException {
        abstractLoader = new AbstractLoader("publicId", "dtdResourceName") {
            @Override
            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                return null;
            }

            @Override
            public void error(SAXParseException exception) throws SAXException {
                throw new SAXException();
            }

            @Override
            public void fatalError(SAXParseException exception) throws SAXException {
                throw new SAXException();
            }
        };
    }

    @Test
    public void testParseInputSource() throws IOException, SAXException {
        InputSource inputSource = new InputSource();
        abstractLoader.parseInputSource(inputSource);
        // Assert statements
    }

    @Test
    public void testResolveEntity() throws IOException, SAXException {
        String publicId = "publicId";
        String systemId = "systemId";
        InputSource inputSource = abstractLoader.resolveEntity(publicId, systemId);
        // Assert statements
    }

    @Test(expected = SAXException.class)
    public void testError() throws SAXException {
        SAXParseException exception = new SAXParseException("", "", "");
        abstractLoader.error(exception);
    }

    @Test(expected = SAXException.class)
    public void testFatalError() throws SAXException {
        SAXParseException exception = new SAXParseException("", "", "");
        abstractLoader.fatalError(exception);
    }
}