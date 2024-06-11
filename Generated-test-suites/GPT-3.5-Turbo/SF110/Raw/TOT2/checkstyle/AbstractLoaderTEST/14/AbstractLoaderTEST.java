package com.puppycrawl.tools.checkstyle.api;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AbstractLoaderTest {

    private AbstractLoader abstractLoader;

    @Before
    public void setUp() {
        abstractLoader = new AbstractLoaderTestImpl();
    }

    @Test
    public void testParseInputSource() {
        InputSource inputSource = new InputSource();
        assertThrows(IOException.class, () -> abstractLoader.parseInputSource(inputSource));
    }

    @Test
    public void testResolveEntity() {
        assertThrows(SAXException.class, () -> abstractLoader.resolveEntity("publicId", "systemId"));
    }

    @Test
    public void testError() {
        SAXParseException exception = new SAXParseException("Test", null);
        assertThrows(SAXException.class, () -> abstractLoader.error(exception));
    }

    @Test
    public void testFatalError() {
        SAXParseException exception = new SAXParseException("Test", null);
        assertThrows(SAXException.class, () -> abstractLoader.fatalError(exception));
    }

    private class AbstractLoaderTestImpl extends AbstractLoader {
        public AbstractLoaderTestImpl() throws SAXException, ParserConfigurationException {
            super(new HashMap<>());
        }
    }
}