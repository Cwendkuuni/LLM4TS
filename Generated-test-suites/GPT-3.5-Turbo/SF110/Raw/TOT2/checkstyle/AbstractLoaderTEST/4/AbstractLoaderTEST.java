package com.puppycrawl.tools.checkstyle.api;

import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.Assert.assertTrue;

public class AbstractLoaderTest {

    @Test
    public void testParseInputSource() throws IOException, SAXException {
        AbstractLoader abstractLoader = new AbstractLoader(new HashMap<>(){});
        InputSource inputSource = new InputSource("test.xml");
        abstractLoader.parseInputSource(inputSource);
        assertTrue(true);
    }

    @Test
    public void testResolveEntity() throws IOException, SAXException {
        AbstractLoader abstractLoader = new AbstractLoader(new HashMap<>(){});
        String publicId = "-//Test//DTD Test//EN";
        String systemId = "test.dtd";
        InputSource inputSource = abstractLoader.resolveEntity(publicId, systemId);
        assertTrue(inputSource instanceof InputSource);
    }

    @Test(expected = SAXException.class)
    public void testError() throws SAXException {
        AbstractLoader abstractLoader = new AbstractLoader(new HashMap<>(){});
        abstractLoader.error(new SAXParseException("Error", null));
    }

    @Test(expected = SAXException.class)
    public void testFatalError() throws SAXException {
        AbstractLoader abstractLoader = new AbstractLoader(new HashMap<>(){});
        abstractLoader.fatalError(new SAXParseException("Fatal Error", null));
    }

}