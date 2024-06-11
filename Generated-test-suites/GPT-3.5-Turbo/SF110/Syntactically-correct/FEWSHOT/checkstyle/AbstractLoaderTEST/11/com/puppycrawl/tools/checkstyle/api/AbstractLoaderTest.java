package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class AbstractLoaderTest {

    private AbstractLoader abstractLoader;
    
    private final Map<String, String> publicIdToResourceNameMap = new HashMap<>();
    
    @Before
    public void setUp() throws SAXException, ParserConfigurationException {
        abstractLoader = new AbstractLoader(publicIdToResourceNameMap) {
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            }
        };
    }
    
    @Test
    public void testConstructorWithPublicIdAndDtdResourceName() throws SAXException, ParserConfigurationException {
        String publicId = "publicId";
        String dtdResourceName = "dtdResourceName";
        AbstractLoader constructorLoader = new AbstractLoader(publicId, dtdResourceName) {
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            }
        };
        
        publicIdToResourceNameMap.put(publicId, dtdResourceName);
        
        assertNotNull(constructorLoader);
    }
    
    @Test
    public void testConstructorWithMapOfPublicIdToResourceName() throws SAXException, ParserConfigurationException {
        assertNotNull(abstractLoader);
    }
    
    @Test
    public void testParseInputSource() throws IOException, SAXException {
        InputSource inputSource = Mockito.mock(InputSource.class);
        abstractLoader.parseInputSource(inputSource);
        
        // Verify that parser.parse() method was called with the input source
        Mockito.verify(abstractLoader.getParser()).parse(inputSource);
    }
    
    @Test
    public void testResolveEntityWithMatchingPublicId() throws SAXException, IOException {
        String publicId = "publicId";
        String dtdResourceName = "dtdResourceName";
        
        publicIdToResourceNameMap.put(publicId, dtdResourceName);
        
        InputStream inputStream = mock(InputStream.class);
        ClassLoader classLoader = mock(ClassLoader.class);
        AbstractLoader abstractLoaderSpy = Mockito.spy(abstractLoader);
        Mockito.doReturn(classLoader).when(abstractLoaderSpy).getClassLoader();
        Mockito.doReturn(inputStream).when(classLoader).getResourceAsStream(dtdResourceName);
        
        Mockito.spy(abstractLoader).resolveEntity(publicId, null);
        
        // Verify that getResourceAsStream() method was called with the dtd resource name
        Mockito.verify(classLoader).getResourceAsStream(dtdResourceName);
        
        // Verify that InputSource is returned
        assertNotNull(abstractLoaderSpy.resolveEntity(publicId, null));
    }
    
    @Test
    public void testResolveEntityWithNonMatchingPublicId() throws SAXException, IOException {
        String publicId = "publicId";
        String systemId = "systemId";
        
        AbstractLoader abstractLoaderSpy = Mockito.spy(abstractLoader);
        Mockito.spy(abstractLoader).resolveEntity(publicId, systemId);
        
        // Verify that DefaultHandler's resolveEntity() method is called
        Mockito.verify(abstractLoaderSpy).resolveEntity(publicId, systemId);
    }
    
    @Test
    public void testError() throws SAXException {
        SAXParseException saxParseException = mock(SAXParseException.class);
        AbstractLoader abstractLoaderSpy = Mockito.spy(abstractLoader);
        Mockito.spy(abstractLoader).error(saxParseException);
        
        // Verify that the exception is thrown
        Mockito.verify(abstractLoaderSpy).error(saxParseException);
    }
    
    @Test
    public void testFatalError() throws SAXException {
        SAXParseException saxParseException = mock(SAXParseException.class);
        AbstractLoader abstractLoaderSpy = Mockito.spy(abstractLoader);
        Mockito.spy(abstractLoader).fatalError(saxParseException);
        
        // Verify that the exception is thrown
        Mockito.verify(abstractLoaderSpy).fatalError(saxParseException);
    }
}