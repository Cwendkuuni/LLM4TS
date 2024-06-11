package com.puppycrawl.tools.checkstyle.api;

import org.junit.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class AbstractLoaderTest {

    class TestAbstractLoader extends AbstractLoader {
        public TestAbstractLoader() throws ParserConfigurationException, SAXException {
            super(new HashMap<String, String>());
        }

        @Override
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            return super.resolveEntity(publicId, systemId);
        }

        @Override
        public void error(SAXParseException exception) throws SAXException {
            super.error(exception);
        }

        @Override
        public void fatalError(SAXParseException exception) throws SAXException {
            super.fatalError(exception);
        }
    }

    @Test
    public void testParseInputSource() {
        InputSource inputSource = new InputSource();
        try {
            TestAbstractLoader loader = new TestAbstractLoader();
            loader.parseInputSource(inputSource);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testResolveEntity() {
        TestAbstractLoader loader = new TestAbstractLoader();

        InputSource inputSource;
        try {
            inputSource = loader.resolveEntity("publicId", "systemId");
            assertNotNull(inputSource);
            InputStream inputStream = inputSource.getByteStream();
            assertNotNull(inputStream);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testError() {
        TestAbstractLoader loader = new TestAbstractLoader();
        try {
            loader.error(new SAXParseException("Test Error", null));
        } catch (SAXException e) {
            assertEquals("Test Error", e.getMessage());
        }
    }

    @Test
    public void testFatalError() {
        TestAbstractLoader loader = new TestAbstractLoader();
        try {
            loader.fatalError(new SAXParseException("Test Fatal Error", null));
        } catch (SAXException e) {
            assertEquals("Test Fatal Error", e.getMessage());
        }
    }
}