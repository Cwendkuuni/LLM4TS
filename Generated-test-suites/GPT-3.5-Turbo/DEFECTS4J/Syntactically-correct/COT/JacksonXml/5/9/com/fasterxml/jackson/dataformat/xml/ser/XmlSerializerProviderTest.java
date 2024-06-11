
import org.junit.Test;
import javax.xml.namespace.QName;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

    @Test
    public void testSerializeValueNull() throws IOException {
        // Given
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());

        // When
        JsonGenerator gen = new ToXmlGenerator();
        provider.serializeValue(gen, null);

        // Then
        // No exception is thrown
    }

    @Test
    public void testSerializeValue() throws IOException {
        // Given
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());

        // When
        JsonGenerator gen = new ToXmlGenerator();
        provider.serializeValue(gen, new Object());

        // Then
        // No exception is thrown
    }

    @Test
    public void testSerializeValueWithType() throws IOException {
        // Given
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());

        // When
        JsonGenerator gen = new ToXmlGenerator();
        provider.serializeValue(gen, new Object(), JavaType.valueOf(Object.class.getName()),
                new JsonSerializer<Object>());

        // Then
        // No exception is thrown
    }

    @Test
    public void test_serializeValue_with_nullable_toxmlgenerator() throws IOException {
        // Given
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());

        // When
        JsonGenerator gen = new TokenBuffer();
        provider.serializeValue(gen, new Object());

        // Then
        // No exception is thrown
    }

    @Test(expected = JsonMappingException.class)
    public void test_wrapped_as_ioe_with_non_io_exception() throws IOException {
        // Given
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());

        // When
        JsonGenerator gen = new ToXmlGenerator();
        provider.serializeValue(gen, new Object(), JavaType.valueOf(Object.class.getName()),
                new JsonSerializer<Object>() {
                    @Override
                    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider)
                            throws IOException {
                        throw new RuntimeException("Error");
                    }
                });
    }

    @Test(expected = IOException.class)
    public void test_wrapped_as_ioe_with_io_exception() throws IOException {
        // Given
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());

        // When
        JsonGenerator gen = new ToXmlGenerator();
        provider.serializeValue(gen, new Object(), JavaType.valueOf(Object.class.getName()),
                new JsonSerializer<Object>() {
                    @Override
                    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider)
                            throws IOException {
                        throw new IOException("IO Error");
                    }
                });
    }
}
