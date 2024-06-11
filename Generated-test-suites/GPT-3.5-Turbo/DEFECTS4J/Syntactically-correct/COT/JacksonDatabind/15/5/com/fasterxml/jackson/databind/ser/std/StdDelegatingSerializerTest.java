import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.StdConverter;

public class StdDelegatingSerializerTest {

    private StdDelegatingSerializer serializer;

    @Before
    public void setUp() {
        Converter<Object, Object> converter = new StdConverter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return value;
            }
        };
        serializer = new StdDelegatingSerializer(converter);
    }

    @Test
    public void testResolve() throws JsonMappingException {
        serializer.resolve(mock(SerializerProvider.class));
        // TODO: Add assertions
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        BeanProperty property = mock(BeanProperty.class);
        serializer.createContextual(mock(SerializerProvider.class), property);
        // TODO: Add assertions
    }

    @Test
    public void testSerialize() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        serializer.serialize("test", gen, provider);
        // TODO: Add assertions
    }

    @Test
    public void testSerializeWithType() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        serializer.serializeWithType("test", gen, provider, typeSer);
        // TODO: Add assertions
    }

    @Test
    public void testIsEmpty() {
        assertTrue(serializer.isEmpty(null));
        assertFalse(serializer.isEmpty(mock(SerializerProvider.class), "test"));
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        JsonNode schema = serializer.getSchema(provider, typeHint);
        // TODO: Add assertions
    }

    @Test
    public void testGetSchemaWithOptional() throws JsonMappingException {
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        boolean isOptional = true;
        JsonNode schema = serializer.getSchema(provider, typeHint, isOptional);
        // TODO: Add assertions
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        // TODO: Add assertions
    }

}