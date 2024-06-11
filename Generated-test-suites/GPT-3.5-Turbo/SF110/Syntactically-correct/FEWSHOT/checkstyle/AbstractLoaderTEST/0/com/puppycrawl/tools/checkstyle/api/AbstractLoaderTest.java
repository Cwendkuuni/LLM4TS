
package com.puppycrawl.tools.checkstyle.api;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class AbstractLoaderTest {

    private AbstractLoader abstractLoader;
    private String publicId = "publicId";
    private String dtdResourceName = "dtdResourceName";
    private Map<String, String> publicIdToResourceNameMap;

    @Before
    public void setUp() {
        publicIdToResourceNameMap = new HashMap<>();
        abstractLoader = mock(AbstractLoader.class);
        abstractLoader.publicIdToResourceNameMap = publicIdToResourceNameMap;
    }

    @Test
    public void testResolveEntity() throws Exception {
        publicIdToResourceNameMap.put(publicId, dtdResourceName);
        ClassLoader classLoader = mock(ClassLoader.class);
        InputStream inputStream = mock(InputStream.class);
        when(abstractLoader.getClass().getClassLoader()).thenReturn(classLoader);
        when(classLoader.getResourceAsStream(dtdResourceName)).thenReturn(inputStream);

        InputSource result = abstractLoader.resolveEntity(publicId, "");

        verify(classLoader, times(1)).getResourceAsStream(dtdResourceName);
        assertEquals(new InputSource(inputStream), result);
    }

    @Test
    public void testResolveEntity_NotFound() throws Exception {
        InputSource result = abstractLoader.resolveEntity(publicId, "");

        assertEquals(DefaultHandler.super.resolveEntity(publicId, ""), result);
    }

    @Test
    public void testError() throws SAXException {
        SAXParseException exception = mock(SAXParseException.class);

        try {
            abstractLoader.error(exception);
        } catch (SAXException e) {
            assertEquals(exception, e);
            throw e;
        }
    }

    @Test
    public void testFatalError() throws SAXException {
        SAXParseException exception = mock(SAXParseException.class);

        try {
            abstractLoader.fatalError(exception);
        } catch (SAXException e) {
            assertEquals(exception, e);
            throw e;
        }
    }
}
