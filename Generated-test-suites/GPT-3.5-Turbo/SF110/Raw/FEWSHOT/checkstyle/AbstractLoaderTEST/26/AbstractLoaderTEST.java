package com.puppycrawl.tools.checkstyle.api;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;

public class AbstractLoaderTest {
    
    @Mock
    XMLReader parser;
    
    @InjectMocks
    AbstractLoader abstractLoader;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testConstructorWithStringParameters() throws SAXException, ParserConfigurationException {
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("publicId1", "resourceName1.dtd");
        
        AbstractLoader abstractLoader = new AbstractLoader("publicId1", "resourceName1.dtd") {
            @Override
            public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws SAXException {
            }
        };
        
        assertNotNull(abstractLoader.publicIdToResourceNameMap);
    }
    
    @Test
    public void testConstructorWithMapParameter() throws SAXException, ParserConfigurationException {
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("publicId1", "resourceName1.dtd");
        
        AbstractLoader abstractLoader = new AbstractLoader(publicIdToResourceNameMap) {
            @Override
            public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws SAXException {
            }
        };
        
        assertNotNull(abstractLoader.publicIdToResourceNameMap);
    }
    
    @Test
    public void testParseInputSource() throws IOException, SAXException {
        InputSource inputSource = new InputSource();
        abstractLoader.parseInputSource(inputSource);
        
        verify(parser).parse(inputSource);
    }
    
    @Test
    public void testResolveEntityWithMatchingPublicId() throws SAXException, IOException {
        String publicId = "publicId1";
        String systemId = "";
        
        InputSource inputSource = abstractLoader.resolveEntity(publicId, systemId);
        
        assertNotNull(inputSource);
    }
    
    @Test
    public void testResolveEntityWithoutMatchingPublicId() throws SAXException, IOException {
        String publicId = "publicId2";
        String systemId = "";
        
        InputSource inputSource = abstractLoader.resolveEntity(publicId, systemId);
        
        assertNotNull(inputSource);
    }
    
    @Test(expected = SAXException.class)
    public void testError() throws SAXException {
        SAXParseException exception = new SAXParseException("", "", "", 0, 0);
        abstractLoader.error(exception);
    }
    
    @Test(expected = SAXException.class)
    public void testFatalError() throws SAXException {
        SAXParseException exception = new SAXParseException("", "", "", 0, 0);
        abstractLoader.fatalError(exception);
    }
}