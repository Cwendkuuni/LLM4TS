import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.TestNameResolver;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class XmlSerializerProviderTest {

    @Test
    public void testSerializeValueNull() throws IOException {
        ObjectMapper mapper = new JsonMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        XmlSerializerProvider provider = new XmlSerializerProvider(config.getDefaultRootNameResolver());
        JsonGenerator gen = mock(JsonGenerator.class);

        provider.serializeValue(gen, null);

        verify(gen, times(1)).writeStartElement("null");
        verify(gen, times(1)).writeEndElement();
    }

    @Test
    public void testSerializeValueString() throws IOException {
        ObjectMapper mapper = new JsonMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        XmlSerializerProvider provider = new XmlSerializerProvider(config.getDefaultRootNameResolver());
        JsonGenerator gen = mock(JsonGenerator.class);
        String value = "test";

        provider.serializeValue(gen, value);

        verify(gen, times(1)).writeStartElement("test");
        verify(gen, times(1)).writeEndElement();
    }

    @Test
    public void testSerializeValueInteger() throws IOException {
        ObjectMapper mapper = new JsonMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        XmlSerializerProvider provider = new XmlSerializerProvider(config.getDefaultRootNameResolver());
        JsonGenerator gen = mock(JsonGenerator.class);
        int value = 123;

        provider.serializeValue(gen, value);

        verify(gen, times(1)).writeNumber(value);
    }

    @Test(expected = IOException.class)
    public void testSerializeValueException() throws IOException {
        ObjectMapper mapper = new JsonMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        XmlSerializerProvider provider = new XmlSerializerProvider(config.getDefaultRootNameResolver());
        JsonGenerator gen = mock(JsonGenerator.class);
        String value = "test";

        doThrow(new IOException("Test exception")).when(gen).writeStartElement(value);

        provider.serializeValue(gen, value);

        // verify no calls to writeEndElement() as exception is thrown
        verify(gen, never()).writeEndElement();
    }
}