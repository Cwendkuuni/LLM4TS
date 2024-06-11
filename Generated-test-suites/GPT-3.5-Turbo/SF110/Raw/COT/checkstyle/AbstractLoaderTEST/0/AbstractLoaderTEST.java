import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class AbstractLoaderTest {

    @Test
    public void testParseInputSource() throws IOException, SAXException {
        // Setup
        AbstractLoader loader = mock(AbstractLoader.class);
        InputSource inputSource = mock(InputSource.class);

        // Exercise
        loader.parseInputSource(inputSource);

        // Verify
        verify(loader).parseInputSource(inputSource);
    }

    @Test
    public void testResolveEntity() throws IOException, SAXException {
        // Setup
        AbstractLoader loader = mock(AbstractLoader.class);
        String publicId = "examplePublicId";
        String systemId = "exampleSystemId";

        // Exercise
        loader.resolveEntity(publicId, systemId);

        // Verify
        verify(loader).resolveEntity(publicId, systemId);
    }

    @Test(expected = SAXException.class)
    public void testError() throws SAXException {
        // Setup
        AbstractLoader loader = mock(AbstractLoader.class);
        SAXParseException exception = mock(SAXParseException.class);

        // Exercise
        loader.error(exception);

        // Verify
        verify(loader).error(exception);
    }

    @Test(expected = SAXException.class)
    public void testFatalError() throws SAXException {
        // Setup
        AbstractLoader loader = mock(AbstractLoader.class);
        SAXParseException exception = mock(SAXParseException.class);

        // Exercise
        loader.fatalError(exception);

        // Verify
        verify(loader).fatalError(exception);
    }
}