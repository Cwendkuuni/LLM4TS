import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class AbstractLoaderTest {
    private AbstractLoader abstractLoader;
    private XMLReader parser;

    @Before
    public void setUp() throws Exception {
        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put("publicId", "dtdResourceName");

        abstractLoader = spy(AbstractLoader.class);
        doNothing().when(abstractLoader).setContentHandler(any());
        doNothing().when(abstractLoader).setEntityResolver(any());
        doNothing().when(abstractLoader).setErrorHandler(any());

        parser = mock(XMLReader.class);
        doReturn(parser).when(abstractLoader).getMockParser();
        doReturn(parser).when(abstractLoader).getParser();

        abstractLoader = spy(new AbstractLoader(publicIdToResourceNameMap) {
            @Override
            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                return new InputSource();
            }

            @Override
            public void error(SAXParseException exception) throws SAXException {
                throw exception;
            }

            @Override
            public void fatalError(SAXParseException exception) throws SAXException {
                throw exception;
            }
        });
    }

    @Test
    public void testParseInputSource() throws IOException, SAXException {
        InputSource inputSource = mock(InputSource.class);
        abstractLoader.parseInputSource(inputSource);
        verify(parser, times(1)).parse(inputSource);
    }

    @Test
    public void testResolveEntity() throws IOException, SAXException {
        InputSource inputSource = abstractLoader.resolveEntity("publicId", "systemId");
        verify(parser, times(1)).setEntityResolver(abstractLoader);
        verify(parser, times(1)).parse(inputSource);
    }

    @Test(expected = SAXException.class)
    public void testError() throws SAXException {
        SAXParseException exception = mock(SAXParseException.class);
        abstractLoader.error(exception);
    }

    @Test(expected = SAXException.class)
    public void testFatalError() throws SAXException {
        SAXParseException exception = mock(SAXParseException.class);
        abstractLoader.fatalError(exception);
    }
}