package com.puppycrawl.tools.checkstyle.api;

import org.junit.*;
import org.xml.sax.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class AbstractLoaderTest {

    private AbstractLoader abstractLoader;
    private XMLReader parser;
    private InputSource inputSource;

    @Before
    public void setup() throws SAXException, ParserConfigurationException {
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("publicId", "dtdResourceName");

        abstractLoader = spy(new AbstractLoader(publicIdToResourceNameMap) {
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            }
        });

        parser = mock(XMLReader.class);
        abstractLoader.parser = parser;

        inputSource = mock(InputSource.class);
    }

    @Test
    public void testParseInputSource() throws IOException, SAXException {
        abstractLoader.parseInputSource(inputSource);

        verify(parser, times(1)).parse(inputSource);
    }

    @Test
    public void testResolveEntity() throws SAXException, IOException {
        when(abstractLoader.publicIdToResourceNameMap.keySet()).thenReturn(new HashSet<String>() {{
            add("publicId");
        }});
        when(abstractLoader.publicIdToResourceNameMap.get("publicId")).thenReturn("dtdResourceName");

        ClassLoader loader = mock(ClassLoader.class);
        when(abstractLoader.getClass().getClassLoader()).thenReturn(loader);

        InputStream dtdIs = mock(InputStream.class);
        when(loader.getResourceAsStream("dtdResourceName")).thenReturn(dtdIs);

        InputSource resolvedInputSource = abstractLoader.resolveEntity("publicId", "systemId");

        Assert.assertNotNull(resolvedInputSource);
        Assert.assertEquals(dtdIs, resolvedInputSource.getByteStream());
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
}