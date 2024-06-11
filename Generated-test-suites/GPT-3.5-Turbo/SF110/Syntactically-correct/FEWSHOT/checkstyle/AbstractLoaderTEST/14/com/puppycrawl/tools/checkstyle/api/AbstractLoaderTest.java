package com.puppycrawl.tools.checkstyle.api;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class AbstractLoaderTest {
    private AbstractLoader abstractLoader;

    @Before
    public void setup() throws SAXException, ParserConfigurationException {
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("publicId1", "resource1");

        abstractLoader = spy(AbstractLoader.class);
        doReturn(null).when(abstractLoader).resolveEntity(anyString(), anyString());

        SAXParserFactory saxParserFactory = mock(SAXParserFactory.class);
        SAXParser saxParser = mock(SAXParser.class);
        XMLReader xmlReader = mock(XMLReader.class);

        when(saxParserFactory.newInstance()).thenReturn(saxParserFactory);
        when(saxParserFactory.setValidating(true)).thenReturn(saxParserFactory);
        when(saxParserFactory.setNamespaceAware(true)).thenReturn(saxParserFactory);
        when(saxParserFactory.newSAXParser()).thenReturn(saxParser);
        when(saxParser.getXMLReader()).thenReturn(xmlReader);

        abstractLoader.publicIdToResourceNameMap = publicIdToResourceNameMap;
        abstractLoader.parser = xmlReader;
    }

    @Test
    public void testConstructorWithPublicIdAndDtdResourceName() throws SAXException, ParserConfigurationException {
        AbstractLoader loader = new AbstractLoader("publicId1", "resource1");
        assertEquals("resource1", loader.publicIdToResourceNameMap.get("publicId1"));
    }

    @Test
    public void testConstructorWithPublicIdToResourceNameMap() throws SAXException, ParserConfigurationException {
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("publicId1", "resource1");
        publicIdToResourceNameMap.put("publicId2", "resource2");

        AbstractLoader loader = new AbstractLoader(publicIdToResourceNameMap);
        assertEquals("resource1", loader.publicIdToResourceNameMap.get("publicId1"));
        assertEquals("resource2", loader.publicIdToResourceNameMap.get("publicId2"));
    }

    @Test
    public void testParseInputSource() throws IOException, SAXException {
        InputSource inputSource = new InputSource(new ByteArrayInputStream("test data".getBytes()));

        abstractLoader.parseInputSource(inputSource);

        verify(abstractLoader.parser).parse(any(InputSource.class));
    }

    @Test
    public void testResolveEntityWhenPublicIdExists() throws SAXException, IOException {
        InputSource expectedInputSource = new InputSource();
        mockStatic(ClassLoader.class);
        ClassLoader classLoader = mock(ClassLoader.class);
        InputStream inputStream = mock(InputStream.class);
        when(ClassLoader.getSystemResourceAsStream("resource1")).thenReturn(inputStream);
        when(abstractLoader.getClass().getClassLoader()).thenReturn(classLoader);
        when(classLoader.getResourceAsStream("resource1")).thenReturn(inputStream);

        InputSource inputSource = abstractLoader.resolveEntity("publicId1", "systemId");

        verify(abstractLoader, never()).resolveEntity(anyString(), anyString());
        assertEquals(expectedInputSource, inputSource);
    }

    @Test
    public void testResolveEntityWhenPublicIdDoesNotExist() throws SAXException, IOException {
        InputSource defaultInputSource = new InputSource();

        InputSource inputSource = abstractLoader.resolveEntity("publicId2", "systemId");

        verify(abstractLoader).resolveEntity("publicId2", "systemId");
        assertEquals(defaultInputSource, inputSource);
    }

    @Test
    public void testError() throws SAXException {
        SAXParseException saxParseException = mock(SAXParseException.class);
        try {
            abstractLoader.error(saxParseException);
        } catch (SAXException e) {
            assertEquals(saxParseException, e);
        }
    }

    @Test
    public void testFatalError() throws SAXException {
        SAXParseException saxParseException = mock(SAXParseException.class);
        try {
            abstractLoader.fatalError(saxParseException);
        } catch (SAXException e) {
            assertEquals(saxParseException, e);
        }
    }
}