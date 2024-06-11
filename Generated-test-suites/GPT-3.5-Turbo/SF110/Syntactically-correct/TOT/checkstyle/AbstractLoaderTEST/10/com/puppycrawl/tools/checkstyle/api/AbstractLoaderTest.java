package com.puppycrawl.tools.checkstyle.api;

import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class AbstractLoaderTest {

    @Test
    public void testConstructor_withPublicIdAndDtdResourceName() throws SAXException, ParserConfigurationException {
        // Setup
        String publicId = "testPublicId";
        String dtdResourceName = "testDtdResourceName";

        // Exercise
        AbstractLoader abstractLoader = new AbstractLoader(publicId, dtdResourceName) {
            @Override
            public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws SAXException {

            }
        };

        // Verify
        assertNotNull(abstractLoader);
        assertEquals(publicId, abstractLoader.publicIdToResourceNameMap.keySet().iterator().next());
        assertEquals(dtdResourceName, abstractLoader.publicIdToResourceNameMap.get(publicId));
    }

    @Test
    public void testConstructor_withMapOfPublicIdToResourceName() throws SAXException, ParserConfigurationException {
        // Setup
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("testPublicId", "testDtdResourceName");

        // Exercise
        AbstractLoader abstractLoader = new AbstractLoader(publicIdToResourceNameMap) {
            @Override
            public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws SAXException {

            }
        };

        // Verify
        assertNotNull(abstractLoader);
        assertEquals(publicIdToResourceNameMap, abstractLoader.publicIdToResourceNameMap);
    }

    @Test
    public void testParseInputSource_withValidInputSource() throws IOException, SAXException {
        // Setup
        InputSource inputSource = new InputSource();

        // Exercise
        AbstractLoader abstractLoader = new AbstractLoader(new HashMap<>()) {
            @Override
            public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws SAXException {

            }
        };
        abstractLoader.parseInputSource(inputSource);

        // Verify
        // No exceptions should be thrown
    }

    @Test(expected = SAXParseException.class)
    public void testError_withSAXParseException() throws SAXException {
        // Setup
        SAXParseException exception = new SAXParseException("Test error", "", "", 1, 1);

        // Exercise
        AbstractLoader abstractLoader = new AbstractLoader(new HashMap<>()) {
            @Override
            public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws SAXException {

            }
        };
        abstractLoader.error(exception);

        // Verify
        // SAXParseException should be thrown
    }

    @Test(expected = SAXParseException.class)
    public void testFatalError_withSAXParseException() throws SAXException {
        // Setup
        SAXParseException exception = new SAXParseException("Test fatal error", "", "", 1, 1);

        // Exercise
        AbstractLoader abstractLoader = new AbstractLoader(new HashMap<>()) {
            @Override
            public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws SAXException {

            }
        };
        abstractLoader.fatalError(exception);

        // Verify
        // SAXParseException should be thrown
    }

    @Test
    public void testResolveEntity_withKnownPublicId() throws IOException, SAXException {
        // Setup
        String publicId = "testPublicId";
        String dtdResourceName = "testDtdResourceName";
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put(publicId, dtdResourceName);

        // Exercise
        AbstractLoader abstractLoader = new AbstractLoader(publicIdToResourceNameMap) {
            @Override
            public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws SAXException {

            }
        };
        InputSource inputSource = abstractLoader.resolveEntity(publicId, "");

        // Verify
        assertNotNull(inputSource);
        assertEquals(dtdResourceName, inputSource.getPublicId());
    }

    @Test
    public void testResolveEntity_withUnknownPublicId() throws IOException, SAXException {
        // Setup
        String publicId = "unknownPublicId";
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();

        // Exercise
        AbstractLoader abstractLoader = new AbstractLoader(publicIdToResourceNameMap) {
            @Override
            public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws SAXException {

            }
        };
        InputSource inputSource = abstractLoader.resolveEntity(publicId, "");

        // Verify
        assertNull(inputSource);
    }
}