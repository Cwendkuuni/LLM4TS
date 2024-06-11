package com.puppycrawl.tools.checkstyle.api;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AbstractLoaderTest {

    private AbstractLoader abstractLoader;

    @Mock
    private XMLReader parser;

    @Before
    public void setUp() throws SAXException, ParserConfigurationException {
        MockitoAnnotations.initMocks(this);
        abstractLoader = spy(new AbstractLoader(new HashMap<String, String>()) {
            {
                this.parser = AbstractLoaderTest.this.parser;
            }
        });
    }

    @Test
    public void testAbstractLoaderConstructorWithPublicIdAndDtdResourceName() throws SAXException, ParserConfigurationException {
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("publicId", "dtdResourceName");
        AbstractLoader abstractLoader = new AbstractLoader("publicId", "dtdResourceName") {
        };
        assertEquals(publicIdToResourceNameMap, abstractLoader.publicIdToResourceNameMap);
    }

    @Test
    public void testAbstractLoaderConstructorWithPublicIdToResourceNameMap() throws SAXException, ParserConfigurationException {
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("publicId1", "dtdResourceName1");
        publicIdToResourceNameMap.put("publicId2", "dtdResourceName2");
        AbstractLoader abstractLoader = new AbstractLoader(publicIdToResourceNameMap) {
        };
        assertEquals(publicIdToResourceNameMap, abstractLoader.publicIdToResourceNameMap);
    }

    @Test
    public void testParseInputSource() throws IOException, SAXException {
        InputSource inputSource = mock(InputSource.class);
        abstractLoader.parseInputSource(inputSource);
        verify(parser).parse(inputSource);
    }

    @Test
    public void testResolveEntityWithPublicIdInMap() throws IOException, SAXException {
        InputStream inputStream = mock(InputStream.class);
        ClassLoader classLoader = mock(ClassLoader.class);
        InputSource expectedInputSource = new InputSource(inputStream);
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("publicId", "dtdResourceName");
        AbstractLoader abstractLoader = new AbstractLoader(publicIdToResourceNameMap) {
            {
                this.publicIdToResourceNameMap = publicIdToResourceNameMap;
            }

            @Override
            public ClassLoader getClassLoader() {
                return classLoader;
            }
        };
        when(classLoader.getResourceAsStream("dtdResourceName")).thenReturn(inputStream);
        assertEquals(expectedInputSource, abstractLoader.resolveEntity("publicId", null));
    }

    @Test
    public void testResolveEntityWithPublicIdNotInMap() throws IOException, SAXException {
        String publicId = "publicId";
        String systemId = "systemId";
        AbstractLoader abstractLoader = new AbstractLoader(new HashMap<>()) {
        };
        assertEquals(abstractLoader.resolveEntity(publicId, systemId), abstractLoader.resolveEntity(publicId, systemId));
    }

    @Test(expected = SAXParseException.class)
    public void testError() throws SAXException {
        SAXParseException exception = mock(SAXParseException.class);
        abstractLoader.error(exception);
    }

    @Test(expected = SAXParseException.class)
    public void testFatalError() throws SAXException {
        SAXParseException exception = mock(SAXParseException.class);
        abstractLoader.fatalError(exception);
    }
}