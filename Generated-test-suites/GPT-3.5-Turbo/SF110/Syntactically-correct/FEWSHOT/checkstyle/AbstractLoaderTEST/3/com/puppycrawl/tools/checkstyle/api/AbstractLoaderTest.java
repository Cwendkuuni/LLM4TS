
package com.puppycrawl.tools.checkstyle.api;

import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class AbstractLoaderTest {
    private AbstractLoader createAbstractLoader() throws Exception {
        return new AbstractLoader() {
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            }

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {
            }
        };
    }

    @Test
    public void testConstructor() throws Exception {
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("publicId1", "dtdResourceName1");
        publicIdToResourceNameMap.put("publicId2", "dtdResourceName2");
        AbstractLoader abstractLoader = new AbstractLoader(publicIdToResourceNameMap) {
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            }
            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {
            }
        };

        assertEquals(publicIdToResourceNameMap, abstractLoader.publicIdToResourceNameMap);

        SAXParserFactory factoryMock = mock(SAXParserFactory.class);
        SAXParser saxParserMock = mock(SAXParser.class);
        doReturn(saxParserMock).when(factoryMock).newSAXParser();
        doReturn(mock(XMLReader.class)).when(saxParserMock).getXMLReader();

        AbstractLoader abstractLoaderWithMocks = new AbstractLoader(publicIdToResourceNameMap) {
            @Override
            protected SAXParserFactory getSAXParserFactory() {
                return factoryMock;
            }
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            }
            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {
            }
        };

        abstractLoaderWithMocks.getClass();
        verify(factoryMock, times(1)).setValidating(true);
        verify(factoryMock, times(1)).setNamespaceAware(true);
        verify(factoryMock, times(1)).newSAXParser();
    }

    @Test
    public void testParseInputSource() throws Exception {
        AbstractLoader abstractLoaderSpy = spy(createAbstractLoader());
        InputSource inputSourceMock = mock(InputSource.class);

        abstractLoaderSpy.parseInputSource(inputSourceMock);

        verify(abstractLoaderSpy, times(1)).parser.parse(inputSourceMock);
    }

    @Test
    public void testResolveEntityContained() throws Exception {
        AbstractLoader abstractLoaderSpy = spy(createAbstractLoader());
        String publicId = "publicId1";
        String dtdResourceName = "dtdResourceName1";
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put(publicId, dtdResourceName);
        abstractLoaderSpy.publicIdToResourceNameMap = publicIdToResourceNameMap;

        String systemId = "systemId1";
        InputStream inputStreamMock = mock(InputStream.class);
        ClassLoader classLoaderMock = mock(ClassLoader.class);

        when(abstractLoaderSpy.getClass().getClassLoader()).thenReturn(classLoaderMock);
        when(classLoaderMock.getResourceAsStream(dtdResourceName)).thenReturn(inputStreamMock);

        InputSource inputSource = abstractLoaderSpy.resolveEntity(publicId, systemId);

        verify(classLoaderMock, times(1)).getResourceAsStream(dtdResourceName);
        assertEquals(inputStreamMock, inputSource.getByteStream());
        assertNull(inputSource.getCharacterStream());
        assertNull(inputSource.getPublicId());
        assertNull(inputSource.getSystemId());
    }

    @Test
    public void testResolveEntityNotContained() throws Exception {
        AbstractLoader abstractLoaderSpy = spy(createAbstractLoader());
        String publicId = "publicId1";
        String dtdResourceName = "dtdResourceName1";
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put(publicId, dtdResourceName);
        abstractLoaderSpy.publicIdToResourceNameMap = publicIdToResourceNameMap;

        String systemId = "systemId1";

        InputSource inputSource = abstractLoaderSpy.resolveEntity(publicId, systemId);

        assertNull(inputSource);
    }

    @Test
    public void testError() throws Exception {
        AbstractLoader abstractLoader = createAbstractLoader();
        SAXParseException exceptionMock = mock(SAXParseException.class);

        try {
            abstractLoader.error(exceptionMock);
        } catch (SAXParseException e) {
            assertEquals(exceptionMock, e);
        }
    }

    @Test
    public void testFatalError() throws Exception {
        AbstractLoader abstractLoader = createAbstractLoader();
        SAXParseException exceptionMock = mock(SAXParseException.class);

        try {
            abstractLoader.fatalError(exceptionMock);
        } catch (SAXParseException e) {
            assertEquals(exceptionMock, e);
        }
    }
}
