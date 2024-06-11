package com.puppycrawl.tools.checkstyle.api;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AbstractLoaderTest {

    @InjectMocks
    private AbstractLoader abstractLoader;

    @Test
    public void testParseInputSource() throws IOException, SAXException {
        InputSource inputSource = mock(InputSource.class);
        abstractLoader.parseInputSource(inputSource);
        // Verify that parser's parse method is called with the given inputSource
        verify(abstractLoader.getParser()).parse(inputSource);
    }

    @Test
    public void testResolveEntity() throws IOException, SAXException {
        String publicId = "examplePublicId";
        String systemId = "exampleSystemId";

        // Create a mock map of publicIdToResourceName
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put(publicId, "exampleDtdResourceName");

        // Set up the abstractLoader instance with the mock map
        abstractLoader = new AbstractLoader(publicIdToResourceNameMap) {};

        // Create a mock ClassLoader and InputStream
        ClassLoader loader = mock(ClassLoader.class);
        InputStream dtdIs = mock(InputStream.class);

        // Mock the classLoader.getResourceAsStream() method to return the mock InputStream
        when(loader.getResourceAsStream("exampleDtdResourceName")).thenReturn(dtdIs);

        // Mock the getClass() method to return the mock ClassLoader
        when(abstractLoader.getClass()).thenReturn(loader);

        // Call the resolveEntity method with the publicId and systemId
        InputSource result = abstractLoader.resolveEntity(publicId, systemId);

        // Verify that the getResourceAsStream method is called with the correct dtdResourceName
        verify(loader).getResourceAsStream("exampleDtdResourceName");

        // Verify that a new InputSource is returned with the mock InputStream
        Assert.assertNotNull(result);
        Assert.assertEquals(dtdIs, result.getByteStream());
    }
}