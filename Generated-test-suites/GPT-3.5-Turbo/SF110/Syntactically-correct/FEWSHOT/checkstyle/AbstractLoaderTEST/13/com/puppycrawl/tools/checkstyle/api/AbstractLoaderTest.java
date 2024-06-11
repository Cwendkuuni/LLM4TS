package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Maps;

public class AbstractLoaderTest {

    private AbstractLoader abstractLoader;
    private XMLReader parser;

    @Before
    public void setUp() throws Exception {
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("publicId1", "dtdResource1");
        publicIdToResourceNameMap.put("publicId2", "dtdResource2");

        abstractLoader = spy(AbstractLoader.class);
        when(abstractLoader.getClass().getClassLoader().getResourceAsStream("dtdResource1")).thenReturn(mock(InputStream.class));
        when(abstractLoader.getClass().getClassLoader().getResourceAsStream("dtdResource2")).thenReturn(mock(InputStream.class));
        doReturn(publicIdToResourceNameMap).when(abstractLoader).getPublicIdToResourceNameMap();

        SAXParserFactory factory = mock(SAXParserFactory.class);
        SAXParser saxParser = mock(SAXParser.class);
        parser = mock(XMLReader.class);
        when(factory.newInstance()).thenReturn(saxParser);
        when(saxParser.getXMLReader()).thenReturn(parser);

        abstractLoader = new AbstractLoader(publicIdToResourceNameMap) {
            @Override
            public void parseInputSource(InputSource inputSource) throws IOException, SAXException {
            }
        };
    }

    @Test
    public void testConstructor1() throws ParserConfigurationException, SAXException {
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("publicId1", "dtdResource1");

        AbstractLoader loader = new AbstractLoader("publicId1", "dtdResource1") {
            @Override
            public void parseInputSource(InputSource inputSource) throws IOException, SAXException {

            }
        };

        Map<String, String> actualMap = getPublicIdToResourceNameMap(loader);
        assertEquals(publicIdToResourceNameMap, actualMap);
    }

    @Test
    public void testConstructor2() throws ParserConfigurationException, SAXException {
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();

        AbstractLoader loader = new AbstractLoader(publicIdToResourceNameMap) {
            @Override
            public void parseInputSource(InputSource inputSource) throws IOException, SAXException {

            }
        };

        Map<String, String> actualMap = getPublicIdToResourceNameMap(loader);
        assertEquals(publicIdToResourceNameMap, actualMap);
    }

    @Test
    public void testParseInputSource() throws IOException, SAXException {
        InputSource inputSource = mock(InputSource.class);

        abstractLoader.parseInputSource(inputSource);

        verify(parser).parse(inputSource);
    }

    @Test
    public void testResolveEntity() throws IOException, SAXException {
        AbstractLoader loader = new AbstractLoader(null) {
            @Override
            public void parseInputSource(InputSource inputSource) throws IOException, SAXException {

            }
        };

        InputSource inputSource = loader.resolveEntity("publicId1", "systemId");

        verify(loader.getClass().getClassLoader()).getResourceAsStream("dtdResource1");
        assertNull(inputSource.getByteStream());
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

    private Map<String, String> getPublicIdToResourceNameMap(AbstractLoader loader) throws NoSuchFieldException, IllegalAccessException {
        Field field = AbstractLoader.class.getDeclaredField("publicIdToResourceNameMap");
        field.setAccessible(true);
        return (Map<String, String>) field.get(loader);
    }
}