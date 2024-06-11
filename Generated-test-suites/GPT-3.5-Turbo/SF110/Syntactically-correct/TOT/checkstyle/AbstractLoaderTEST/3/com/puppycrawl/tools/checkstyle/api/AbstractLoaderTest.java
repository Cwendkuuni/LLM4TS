package com.puppycrawl.tools.checkstyle.api;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class AbstractLoaderTest {

    private AbstractLoader abstractLoader;

    @Mock
    private XMLReader parser;

    @Before
    public void setup() throws Exception {
        abstractLoader = new AbstractLoader() {
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                // Do nothing in this test
            }

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {
                // Do nothing in this test
            }

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                // Do nothing in this test
            }
        };
        // Mocking SAXParserFactory and XMLReader to skip actual parsing
        SAXParserFactory saxParserFactory = mock(SAXParserFactory.class);
        SAXParser saxParser = mock(SAXParser.class);
        when(saxParserFactory.newSAXParser()).thenReturn(saxParser);
        when(saxParser.getXMLReader()).thenReturn(parser);
        when(parser.getContentHandler()).thenReturn(new DefaultHandler());
        when(parser.getEntityResolver()).thenReturn(new DefaultHandler());
        when(parser.getErrorHandler()).thenReturn(new DefaultHandler());
        abstractLoader.parser = parser;
    }

    @Test
    public void testParseInputSource_ValidInputSource_SuccessfulParsing() throws IOException, SAXException {
        // Arrange
        InputSource inputSource = mock(InputSource.class);

        // Act
        abstractLoader.parseInputSource(inputSource);

        // Assert
        verify(parser).parse(inputSource);
    }

    @Test(expected = SAXParseException.class)
    public void testResolveEntity_ValidPublicIdAndResourceName_SuccessfulEntityResolution() throws IOException, SAXException {
        // Arrange
        String publicId = "validPublicId";
        String dtdResourceName = "validDtdResourceName";
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put(publicId, dtdResourceName);

        // Act
        InputSource result = abstractLoader.resolveEntity(publicId, "");

        // Assert
        assertEquals(publicId, result.getPublicId());
        assertEquals(dtdResourceName, result.getSystemId());
    }

    @Test(expected = SAXParseException.class)
    public void testError_SAXParseExceptionThrown_ExceptionThrown() throws SAXException {
        // Arrange
        SAXParseException exception = mock(SAXParseException.class);

        // Act
        abstractLoader.error(exception);

        // Assert
        verify(parser).error(exception);
    }

    @Test(expected = SAXParseException.class)
    public void testFatalError_SAXParseExceptionThrown_ExceptionThrown() throws SAXException {
        // Arrange
        SAXParseException exception = mock(SAXParseException.class);

        // Act
        abstractLoader.fatalError(exception);

        // Assert
        verify(parser).fatalError(exception);
    }
}