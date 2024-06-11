package com.puppycrawl.tools.checkstyle.api;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class AbstractLoaderTest {

    private AbstractLoader abstractLoader;
    private XMLReader xmlReader;

    @Before
    public void setUp() {
        abstractLoader = Mockito.mock(AbstractLoader.class, Mockito.CALLS_REAL_METHODS);
        xmlReader = Mockito.mock(XMLReader.class);
        abstractLoader.setXMLReader(xmlReader);
    }

    @Test
    public void testParseInputSource() throws IOException, SAXException {
        InputSource inputSource = Mockito.mock(InputSource.class);
        abstractLoader.parseInputSource(inputSource);
        verify(xmlReader, times(1)).parse(inputSource);
    }

    @Test(expected = SAXException.class)
    public void testResolveEntity_PublicIdExists() throws SAXException, IOException {
        String publicId = "publicId";
        String systemId = "systemId";
        String dtdResourceName = "dtdResourceName";

        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put(publicId, dtdResourceName);

        abstractLoader.setPublicIdToResourceNameMap(publicIdToResourceNameMap);
        ClassLoader classLoader = Mockito.mock(ClassLoader.class);
        InputStream inputStream = Mockito.mock(InputStream.class);
        when(abstractLoader.getClass().getClassLoader()).thenReturn(classLoader);
        when(classLoader.getResourceAsStream(dtdResourceName)).thenReturn(inputStream);

        InputSource inputSource = abstractLoader.resolveEntity(publicId, systemId);

        verify(xmlReader, never()).parse(any(InputSource.class));
        verify(abstractLoader).setEntityResolver(abstractLoader);
        verify(abstractLoader).error(any(SAXParseException.class));
    }

    @Test
    public void testResolveEntity_PublicIdDoesNotExist() throws SAXException, IOException {
        String publicId = "publicId";
        String systemId = "systemId";

        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        abstractLoader.setPublicIdToResourceNameMap(publicIdToResourceNameMap);

        InputSource inputSource = abstractLoader.resolveEntity(publicId, systemId);

        verify(xmlReader, never()).parse(any(InputSource.class));
        verify(abstractLoader).setEntityResolver(abstractLoader);
    }

    @Test(expected = SAXException.class)
    public void testError() throws SAXException {
        SAXParseException exception = Mockito.mock(SAXParseException.class);
        abstractLoader.error(exception);
        verify(xmlReader).error(exception);
    }

    @Test(expected = SAXException.class)
    public void testFatalError() throws SAXException {
        SAXParseException exception = Mockito.mock(SAXParseException.class);
        abstractLoader.fatalError(exception);
        verify(xmlReader).fatalError(exception);
    }
}