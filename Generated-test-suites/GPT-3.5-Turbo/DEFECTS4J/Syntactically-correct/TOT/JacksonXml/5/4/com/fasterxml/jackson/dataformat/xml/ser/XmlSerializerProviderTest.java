import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;

public class XmlSerializerProviderTest {

    @Test
    public void testSerializeValue_withNonNullValue_shouldCallSerializeMethodOfSuperclass() throws Exception {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        TokenBuffer gen = new TokenBuffer(new ObjectMapper(), false);
        Object value = new Object();
        // Act
        provider.serializeValue(gen, value);
        // Assert
        assertTrue(gen.getBuffer().size() > 0);
    }

    @Test
    public void testSerializeValue_withNullValue_shouldCallSerializeMethodOfSuperclass() throws Exception {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        TokenBuffer gen = new TokenBuffer(new ObjectMapper(), false);
        Object value = null;
        // Act
        provider.serializeValue(gen, value);
        // Assert
        assertTrue(gen.getBuffer().size() > 0);
    }

    @Test
    public void testSerializeValue_withNonNullValueAndRootType_shouldCallSerializeMethodOfSuperclass() throws Exception {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        TokenBuffer gen = new TokenBuffer(new ObjectMapper(), false);
        Object value = new Object();
        JavaType rootType = JavaType.define(Object.class);
        JsonSerializer<Object> ser = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString("Test");
            }
        };
        // Act
        provider.serializeValue(gen, value, rootType, ser);
        // Assert
        assertEquals("Test", gen.asParser().readValueAs(String.class));
    }

    @Test
    public void testSerializeValue_withNullValueAndRootType_shouldCallSerializeMethodOfSuperclass() throws Exception {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        TokenBuffer gen = new TokenBuffer(new ObjectMapper(), false);
        Object value = null;
        JavaType rootType = JavaType.define(Object.class);
        JsonSerializer<Object> ser = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString("Test");
            }
        };
        // Act
        provider.serializeValue(gen, value, rootType, ser);
        // Assert
        assertEquals("Test", gen.asParser().readValueAs(String.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeValue_withExceptionInSerializeMethod_shouldThrowJsonMappingException() throws Exception {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        TokenBuffer gen = new TokenBuffer(new ObjectMapper(), false);
        Object value = new Object();
        JavaType rootType = JavaType.define(Object.class);
        JsonSerializer<Object> ser = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                throw new IOException("Test Exception");
            }
        };
        // Act
        provider.serializeValue(gen, value, rootType, ser);
    }
}