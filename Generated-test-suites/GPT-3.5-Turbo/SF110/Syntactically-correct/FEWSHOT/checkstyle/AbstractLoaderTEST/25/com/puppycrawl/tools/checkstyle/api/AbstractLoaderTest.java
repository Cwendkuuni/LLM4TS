package com.puppycrawl.tools.checkstyle.api;

import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class AbstractLoaderTest {

    @Test
    public void testConstructorWithPublicIdAndDtdResourceName() throws Exception {
        // Setup
        String publicId = "publicId";
        String dtdResourceName = "dtdResourceName";
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put(publicId, dtdResourceName);

        // Execute
        AbstractLoader abstractLoader = new AbstractLoader(publicId, dtdResourceName) {
        };

        // Verify
        assertEquals(publicIdToResourceNameMap, abstractLoader.publicIdToResourceNameMap);
    }

    @Test
    public void testConstructorWithPublicIdToResourceNameMap() throws Exception {
        // Setup
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();

        // Execute
        AbstractLoader abstractLoader = new AbstractLoader(publicIdToResourceNameMap) {
        };

        // Verify
        assertEquals(publicIdToResourceNameMap, abstractLoader.publicIdToResourceNameMap);
    }

    @Test
    public void testParseInputSource() throws Exception {
        // Setup
        InputSource inputSource = mock(InputSource.class);
        AbstractLoader abstractLoader = spy(AbstractLoader.class);

        // Execute
        abstractLoader.parseInputSource(inputSource);

        // Verify
        verify(abstractLoader.parser).parse(inputSource);
    }

    @Test
    public void testResolveEntityWhenPublicIdExists() throws Exception {
        // Setup
        String publicId = "publicId";
        String dtdResourceName = "dtdResourceName";

        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put(publicId, dtdResourceName);

        AbstractLoader abstractLoader = new AbstractLoader(publicIdToResourceNameMap) {
        };
        AbstractLoader abstractLoaderSpy = spy(abstractLoader);

        String systemId = "systemId";
        InputSource expectedInputSource = mock(InputSource.class);

        when(abstractLoaderSpy.getClass().getClassLoader().getResourceAsStream(dtdResourceName)).thenReturn(mock(InputStream.class));
        whenNew(InputSource.class).withArguments(any(InputStream.class)).thenReturn(expectedInputSource);

        // Execute
        InputSource actualInputSource = abstractLoaderSpy.resolveEntity(publicId, systemId);

        // Verify
        verify(abstractLoaderSpy.publicIdToResourceNameMap).keySet();
        verify(abstractLoaderSpy.publicIdToResourceNameMap).get(publicId);
        verify(abstractLoaderSpy.getClass().getClassLoader()).getResourceAsStream(dtdResourceName);
        verifyNew(InputSource.class).withArguments(any(InputStream.class));
        assertEquals(expectedInputSource, actualInputSource);
    }

    @Test
    public void testResolveEntityWhenPublicIdDoesNotExist() throws Exception {
        // Setup
        String publicId = "publicId";
        String systemId = "systemId";
        AbstractLoader abstractLoader = new AbstractLoader(new HashMap<>()) {
        };

        // Execute
        InputSource inputSource = abstractLoader.resolveEntity(publicId, systemId);

        // Verify
        assertNull(inputSource);
    }

    @Test(expected = SAXException.class)
    public void testError() throws Exception {
        // Setup
        SAXParseException exception = mock(SAXParseException.class);
        AbstractLoader abstractLoader = new AbstractLoader(new HashMap<>()) {
        };

        // Execute
        abstractLoader.error(exception);

        // Verify
        // Exception is thrown
    }

    @Test(expected = SAXException.class)
    public void testFatalError() throws Exception {
        // Setup
        SAXParseException exception = mock(SAXParseException.class);
        AbstractLoader abstractLoader = new AbstractLoader(new HashMap<>()) {
        };

        // Execute
        abstractLoader.fatalError(exception);

        // Verify
        // Exception is thrown
    }
}