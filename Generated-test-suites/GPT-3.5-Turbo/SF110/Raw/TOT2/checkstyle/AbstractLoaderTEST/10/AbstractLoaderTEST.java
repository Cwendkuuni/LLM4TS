package com.puppycrawl.tools.checkstyle.api;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AbstractLoaderTest {

    private AbstractLoader loader;

    @Before
    public void setUp() throws SAXException, ParserConfigurationException {
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("testId", "test.dtd");
        loader = new AbstractLoader(publicIdToResourceNameMap) {
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) {
                // Do nothing
            }
        };
    }

    @Test
    public void testParseInputSource() throws IOException, SAXException {
        InputSource inputSource = new InputSource();
        assertDoesNotThrow(() -> loader.parseInputSource(inputSource));
    }

    @Test
    public void testResolveEntity() throws IOException, SAXException {
        InputSource result = loader.resolveEntity("testId", "test.dtd");
        assertNotNull(result);
    }

    @Test(expected = SAXException.class)
    public void testError() throws SAXException {
        SAXParseException exception = new SAXParseException("Test Error", null);
        loader.error(exception);
    }

    @Test(expected = SAXException.class)
    public void testFatalError() throws SAXException {
        SAXParseException exception = new SAXParseException("Test Fatal Error", null);
        loader.fatalError(exception);
    }
}