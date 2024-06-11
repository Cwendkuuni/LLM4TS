import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class AbstractLoaderTest {

    @Test
    public void testParseInputSource() throws SAXException, IOException {
        AbstractLoader loader = Mockito.spy(AbstractLoader.class);
        InputSource inputSource = mock(InputSource.class);

        doNothing().when(loader.parser).parse(inputSource);

        loader.parseInputSource(inputSource);

        verify(loader.parser, times(1)).parse(inputSource);
    }

    @Test
    public void testResolveEntityWhenPublicIdExists() throws SAXException, IOException {
        AbstractLoader loader = new AbstractLoader(new HashMap<String, String>() {{
            put("publicId", "resourceName");
        }});

        String publicId = "publicId";
        String systemId = "systemId";

        InputSource inputSource = loader.resolveEntity(publicId, systemId);

        assertEquals("resourceName", inputSource.getByteStream().toString());
    }

    @Test
    public void testResolveEntityWhenPublicIdDoesNotExists() throws SAXException, IOException {
        AbstractLoader loader = new AbstractLoader(new HashMap<String, String>());

        String publicId = "nonexistentPublicId";
        String systemId = "systemId";

        InputSource inputSource = loader.resolveEntity(publicId, systemId);

        assertNull(inputSource);
    }

    @Test(expected = SAXParseException.class)
    public void testError() throws SAXException {
        AbstractLoader loader = new AbstractLoader(new HashMap<String, String>());

        SAXParseException exception = mock(SAXParseException.class);

        loader.error(exception);
    }

    @Test(expected = SAXParseException.class)
    public void testFatalError() throws SAXException {
        AbstractLoader loader = new AbstractLoader(new HashMap<String, String>());

        SAXParseException exception = mock(SAXParseException.class);

        loader.fatalError(exception);
    }
}