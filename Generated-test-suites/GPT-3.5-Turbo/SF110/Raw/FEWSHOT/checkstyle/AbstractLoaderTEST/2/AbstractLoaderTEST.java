
package com.puppycrawl.tools.checkstyle.api;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AbstractLoaderTest {
    private AbstractLoader abstractLoader;
    private XMLReader parser;

    @Before
    public void setUp() throws SAXException, ParserConfigurationException {
        parser = mock(XMLReader.class);
        abstractLoader = new AbstractLoader(new HashMap<String, String>()) {
            @Override
            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                return null;
            }
        };
        abstractLoader.parser = parser;
    }

    @Test
    public void testConstructorWithPublicIdAndDtdResourceName() throws SAXException, ParserConfigurationException {
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("publicId", "dtdResourceName");
        abstractLoader = new AbstractLoader("publicId", "dtdResourceName") {
            @Override
            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                return null;
            }
        };
        assertEquals("publicId", publicIdToResourceNameMap.keySet().iterator().next());
        assertEquals("dtdResourceName", publicIdToResourceNameMap.get("publicId"));
        assertNotNull(abstractLoader.publicIdToResourceNameMap);
    }

    @Test
    public void testConstructorWithPublicIdToResourceNameMap() throws SAXException, ParserConfigurationException {
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        abstractLoader = new AbstractLoader(publicIdToResourceNameMap) {
            @Override
            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                return null;
            }
        };
        assertNotNull(abstractLoader.publicIdToResourceNameMap);
    }

    @Test
    public void testParseInputSource() throws IOException, SAXException {
        InputSource inputSource = mock(InputSource.class);
        IOException ioException = new IOException();
        SAXException saxException = new SAXException();
        doThrow(ioException).when(parser).parse(inputSource);
        try {
            abstractLoader.parseInputSource(inputSource);
            fail("Should throw IOException");
        } catch (IOException e) {
            assertEquals(ioException, e);
        }
        doThrow(saxException).when(parser).parse(inputSource);
        try {
            abstractLoader.parseInputSource(inputSource);
            fail("Should throw SAXException");
        } catch (SAXException e) {
            assertEquals(saxException, e);
        }
    }

    @Test
    public void testResolveEntity() throws SAXException, IOException {
        assertNull(abstractLoader.resolveEntity("publicId", "systemId"));
        HashMap<String, String> publicIdToResourceNameMap = new HashMap<>();
        abstractLoader.publicIdToResourceNameMap = publicIdToResourceNameMap;
        publicIdToResourceNameMap.put("publicId", "dtdResourceName");

        InputStream inputStream = mock(InputStream.class);
        ClassLoader classLoader = mock(ClassLoader.class);
        doReturn(inputStream).when(classLoader).getResourceAsStream("dtdResourceName");
        abstractLoader = new AbstractLoader(publicIdToResourceNameMap) {
            @Override
            public ClassLoader getClassLoader() {
                return classLoader;
            }
        };
        assertNotNull(abstractLoader.resolveEntity("publicId", "systemId"));
    }

    @Test
    public void testError() throws SAXException {
        SAXParseException saxParseException = mock(SAXParseException.class);
        try {
            abstractLoader.error(saxParseException);
            fail("Should throw SAXException");
        } catch (SAXException e) {
            assertEquals(saxParseException, e);
        }
    }

    @Test
    public void testFatalError() throws SAXException {
        SAXParseException saxParseException = mock(SAXParseException.class);
        try {
            abstractLoader.fatalError(saxParseException);
            fail("Should throw SAXException");
        } catch (SAXException e) {
            assertEquals(saxParseException, e);
        }
    }
}
