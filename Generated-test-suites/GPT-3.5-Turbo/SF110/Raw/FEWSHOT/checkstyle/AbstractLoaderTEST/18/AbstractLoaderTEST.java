import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AbstractLoaderTest {

    @Test
    public void testParseInputSource() throws IOException, SAXException {
        AbstractLoader loader = Mockito.mock(AbstractLoader.class, Mockito.CALLS_REAL_METHODS);

        InputSource inputSource = new InputSource(new ByteArrayInputStream("<xml></xml>".getBytes()));

        loader.parseInputSource(inputSource);

        ArgumentCaptor<InputSource> captor = ArgumentCaptor.forClass(InputSource.class);
        Mockito.verify(loader.getParser(), Mockito.times(1)).parse(captor.capture());

        assertEquals(inputSource, captor.getValue());
    }

    @Test
    public void testResolveEntity() throws IOException, SAXException {
        AbstractLoader loader = Mockito.mock(AbstractLoader.class, Mockito.CALLS_REAL_METHODS);

        String publicId = "publicId";
        String systemId = "systemId";
        String dtdResourceName = "dtdResourceName";

        Map<String, String> publicIdToResourceNameMap = new HashMap<>();
        publicIdToResourceNameMap.put(publicId, dtdResourceName);

        loader.setPublicIdToResourceNameMap(publicIdToResourceNameMap);

        ClassLoader mockClassLoader = Mockito.mock(ClassLoader.class);
        loader.setClassLoader(mockClassLoader);

        InputStream mockInputStream = Mockito.mock(InputStream.class);
        Mockito.when(mockClassLoader.getResourceAsStream(dtdResourceName)).thenReturn(mockInputStream);

        InputSource resolvedEntity = loader.resolveEntity(publicId, systemId);

        ArgumentCaptor<String> publicIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> systemIdCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(loader, Mockito.times(1)).superResolveEntity(publicIdCaptor.capture(), systemIdCaptor.capture());

        assertEquals(publicId, publicIdCaptor.getValue());
        assertEquals(systemId, systemIdCaptor.getValue());
        assertEquals(mockInputStream, resolvedEntity.getByteStream());
    }

    @Test(expected = SAXException.class)
    public void testError() throws SAXException {
        AbstractLoader loader = Mockito.mock(AbstractLoader.class, Mockito.CALLS_REAL_METHODS);

        SAXParseException exception = Mockito.mock(SAXParseException.class);

        loader.error(exception);
    }

    @Test(expected = SAXException.class)
    public void testFatalError() throws SAXException {
        AbstractLoader loader = Mockito.mock(AbstractLoader.class, Mockito.CALLS_REAL_METHODS);

        SAXParseException exception = Mockito.mock(SAXParseException.class);

        loader.fatalError(exception);
    }
}