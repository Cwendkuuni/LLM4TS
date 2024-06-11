
package com.puppycrawl.tools.checkstyle.api;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.xml.sax.*;

import javax.xml.parsers.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AbstractLoaderTest {

    private AbstractLoader abstractLoader;
    private XMLReader mockXmlReader;
    private InputSource mockInputSource;

    @Before
    public void setUp() throws Exception {
        String publicId = "publicId";
        String dtdResourceName = "dtdResourceName";

        abstractLoader = Mockito.spy(AbstractLoader.class);
        mockXmlReader = Mockito.mock(XMLReader.class);
        mockInputSource = Mockito.mock(InputSource.class);

        doReturn(publicId).when(abstractLoader).getPublicId();
        doReturn(dtdResourceName).when(abstractLoader).getDtdResourceName();

        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        factory.setNamespaceAware(true);
        XMLReader xmlReader = factory.newSAXParser().getXMLReader();
        Mockito.doReturn(xmlReader).when(abstractLoader).getSAXParserXMLReader();
    }

    @Test
    public void testAbstractLoader_CreationWithPublicIdAndDtdResourceName() throws Exception {
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("publicId", "dtdResourceName");

        abstractLoader = new AbstractLoader(publicIdToResourceNameMap) {
            @Override
            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                return null;
            }
        };

        assertEquals(publicIdToResourceNameMap, abstractLoader.getPublicIdToResourceNameMap());
    }

    @Test
    public void testAbstractLoader_CreationWithPublicIdAndDtdResourceName_SAXException() throws Exception {
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("publicId", "dtdResourceName");

        assertThrows(SAXException.class, () -> {
            abstractLoader = new AbstractLoader(publicIdToResourceNameMap) {
                @Override
                public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                    throw new SAXException();
                }
            };
        });
    }

    @Test
    public void testAbstractLoader_CreationWithPublicIdAndDtdResourceName_ParserConfigurationException() throws Exception {
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("publicId", "dtdResourceName");

        assertThrows(ParserConfigurationException.class, () -> {
            abstractLoader = new AbstractLoader(publicIdToResourceNameMap) {
                @Override
                public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                    return null;
                }
            };
        });
    }

    @Test
    public void testAbstractLoader_CreationWithPublicIdAndDtdResourceName_NullPublicIdToResourceNameMap() throws Exception {
        assertThrows(NullPointerException.class, () -> {
            abstractLoader = new AbstractLoader(null) {
                @Override
                public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                    return null;
                }
            };
        });
    }

    @Test
    public void testAbstractLoader_CreationWithPublicIdAndDtdResourceName_WithInputSource() throws Exception {
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("publicId", "dtdResourceName");

        abstractLoader = new AbstractLoader(publicIdToResourceNameMap) {
            @Override
            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                return null;
            }
        };

        abstractLoader.parseInputSource(mockInputSource);
        verify(mockXmlReader).parse(mockInputSource);
    }

    @Test
    public void testResolveEntity_PublicIdContainedInMap_ReturnsInputSource() throws Exception {
        String publicId = "publicId";
        String dtdResourceName = "dtdResourceName";
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put(publicId, dtdResourceName);

        abstractLoader = new AbstractLoader(publicIdToResourceNameMap) {
            @Override
            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                return super.resolveEntity(publicId, systemId);
            }
        };

        doReturn(Mockito.mock(ClassLoader.class)).when(abstractLoader).getClassLoader();

        InputSource result = abstractLoader.resolveEntity(publicId, "systemId");

        assertNotNull(result);
        assertEquals(null, result.getCharacterStream());
    }

    @Test
    public void testResolveEntity_PublicIdNotContainedInMap_ReturnsSuperResolveEntity() throws Exception {
        String publicId = "notFoundPublicId";

        abstractLoader.resolveEntity(publicId, "systemId");

        verify(abstractLoader).resolveEntity(publicId, "systemId");
    }

    @Test(expected = SAXException.class)
    public void testError_ThrowsSAXException() throws SAXException {
        abstractLoader.error(Mockito.mock(SAXParseException.class));
    }

    @Test(expected = SAXException.class)
    public void testFatalError_ThrowsSAXException() throws SAXException {
        abstractLoader.fatalError(Mockito.mock(SAXParseException.class));
    }
}

