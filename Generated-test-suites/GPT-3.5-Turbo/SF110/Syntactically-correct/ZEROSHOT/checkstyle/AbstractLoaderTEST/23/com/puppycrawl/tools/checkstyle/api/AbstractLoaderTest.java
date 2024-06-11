
package com.puppycrawl.tools.checkstyle.api;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import com.google.common.collect.Maps;

public class AbstractLoaderTest {
    
    private AbstractLoader abstractLoader;
    private XMLReader parser;
    
    @Before
    public void setUp() throws Exception {
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("publicId", "dtdResource");
        abstractLoader = Mockito.mock(AbstractLoader.class, Mockito.CALLS_REAL_METHODS);
        Mockito.doCallRealMethod().when(abstractLoader).resolveEntity(any(String.class), any(String.class));
        Mockito.doCallRealMethod().when(abstractLoader).error(any(SAXParseException.class));
        Mockito.doCallRealMethod().when(abstractLoader).fatalError(any(SAXParseException.class));
        abstractLoader.publicIdToResourceNameMap = publicIdToResourceNameMap;
    }

    @Test
    public void testParseInputSource() throws IOException, SAXException {
        InputSource inputSource = Mockito.mock(InputSource.class);
        parser = Mockito.mock(XMLReader.class);
        abstractLoader.parser = parser;
        
        abstractLoader.parseInputSource(inputSource);
        
        verify(parser).parse(inputSource);
    }
    
    @Test
    public void testResolveEntity() throws SAXException, IOException {
        String publicId = "publicId";
        String systemId = "systemId";
        String dtdResourceName = "dtdResource";
        ClassLoader loader = Mockito.mock(ClassLoader.class);
        InputStream dtdIs = Mockito.mock(InputStream.class);
        InputSource expectedInputSource = Mockito.mock(InputSource.class);
        
        when(abstractLoader.publicIdToResourceNameMap.keySet()).thenReturn(new HashSet<String>(){{ add(publicId); }});
        when(abstractLoader.publicIdToResourceNameMap.get(publicId)).thenReturn(dtdResourceName);
        when(abstractLoader.getClass().getClassLoader()).thenReturn(loader);
        when(loader.getResourceAsStream(dtdResourceName)).thenReturn(dtdIs);
        when(dtdIs.resolveEntity(publicId, systemId)).thenReturn(expectedInputSource);

        InputSource result = abstractLoader.resolveEntity(publicId, systemId);
        
        verify(dtdIs).resolveEntity(publicId, systemId);
        assertEquals(expectedInputSource, result);
    }
    
    @Test(expected = SAXException.class)
    public void testError() throws SAXException {
        SAXParseException exception = Mockito.mock(SAXParseException.class);
        
        abstractLoader.error(exception);
    }
    
    @Test(expected = SAXException.class)
    public void testFatalError() throws SAXException {
        SAXParseException exception = Mockito.mock(SAXParseException.class);
        
        abstractLoader.fatalError(exception);
    }
}
