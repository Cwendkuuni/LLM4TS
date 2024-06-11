
package com.puppycrawl.tools.checkstyle.api;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.xml.sax.*;

import javax.xml.parsers.*;
import java.io.*;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AbstractLoaderTest {

    private AbstractLoader abstractLoader;
    private XMLReader mockedXMLReader;

    @Mock
    private InputSource mockedInputSource;

    @Before
    public void setup() throws SAXException, ParserConfigurationException {
        MockitoAnnotations.initMocks(this);

        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("publicId1", "dtdResourceName1");
        publicIdToResourceNameMap.put("publicId2", "dtdResourceName2");

        abstractLoader = new AbstractLoader(publicIdToResourceNameMap) {
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                // Do nothing
            }

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {
                // Do nothing
            }
        };

        mockedXMLReader = mock(XMLReader.class);
        abstractLoader.parser = mockedXMLReader;

        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        factory.setNamespaceAware(true);
        SAXParser saxParser = factory.newSAXParser();
        XMLReader xmlReader = saxParser.getXMLReader();
        abstractLoader.parser.setContentHandler(abstractLoader);
        abstractLoader.parser.setEntityResolver(abstractLoader);
        abstractLoader.parser.setErrorHandler(abstractLoader);
    }

    @Test
    public void testParseInputSource() throws IOException, SAXException {
        abstractLoader.parseInputSource(mockedInputSource);

        verify(mockedXMLReader, times(1)).parse(mockedInputSource);
    }

    @Test
    public void testResolveEntityWithMatchingPublicId() throws SAXException, IOException {
        AbstractLoader spyAbstractLoader = spy(abstractLoader);
        String publicId = "publicId1";
        String dtdResourceName = "dtdResourceName1";
        String systemId = "systemId1";

        when(spyAbstractLoader.resolveEntity(publicId, systemId)).thenCallRealMethod();
        when(spyAbstractLoader.publicIdToResourceNameMap.keySet()).thenReturn(new HashSet<>(Collections.singletonList(publicId)));
        when(spyAbstractLoader.publicIdToResourceNameMap.get(publicId)).thenReturn(dtdResourceName);
        when(spyAbstractLoader.getClass().getClassLoader().getResourceAsStream(dtdResourceName)).thenReturn(mock(InputStream.class));

        InputSource resolvedEntity = spyAbstractLoader.resolveEntity(publicId, systemId);

        assertNotNull(resolvedEntity);
    }

    @Test
    public void testResolveEntityWithNoMatchingPublicId() throws SAXException, IOException {
        AbstractLoader spyAbstractLoader = spy(abstractLoader);
        String publicId = "publicId1";
        String systemId = "systemId1";

        when(spyAbstractLoader.resolveEntity(publicId, systemId)).thenCallRealMethod();
        when(spyAbstractLoader.publicIdToResourceNameMap.keySet()).thenReturn(new HashSet<>());

        InputSource resolvedEntity = spyAbstractLoader.resolveEntity(publicId, systemId);

        assertNull(resolvedEntity);
    }

    @Test(expected = SAXException.class)
    public void testError() throws SAXException {
        abstractLoader.error(new SAXParseException("Error", null));
    }

    @Test(expected = SAXException.class)
    public void testFatalError() throws SAXException {
        abstractLoader.fatalError(new SAXParseException("Fatal Error", null));
    }
}
