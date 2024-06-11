import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.Base;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.DoubleSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.FloatSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntLikeSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntegerSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.LongSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.ShortSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;

public class NumberSerializersTest {
    private Map<String, JsonSerializer<?>> allDeserializers;

    @Before
    public void setUp() {
        allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
    }

    @Test
    public void testIntegerSerializerSerialize() throws IOException {
        JsonSerializer<?> serializer = allDeserializers.get(Integer.class.getName());
        assertNotNull(serializer);
        SerializerProvider provider = null;
        JsonGenerator generator = null;
        Integer value = 100;
        serializer.serialize(value, generator, provider);
        //additional assertions
    }

    @Test
    public void testIntegerSerializerSerializeWithType() throws IOException {
        JsonSerializer<?> serializer = allDeserializers.get(Integer.class.getName());
        assertNotNull(serializer);
        SerializerProvider provider = null;
        JsonGenerator generator = null;
        Integer value = 100;
        TypeSerializer typeSerializer = null;
        serializer.serializeWithType(value, generator, provider, typeSerializer);
        //additional assertions
    }

    @Test
    public void testLongSerializerSerialize() throws IOException {
        JsonSerializer<?> serializer = allDeserializers.get(Long.class.getName());
        assertNotNull(serializer);
        SerializerProvider provider = null;
        JsonGenerator generator = null;
        Long value = 100L;
        serializer.serialize(value, generator, provider);
        //additional assertions
    }

    @Test
    public void testDoubleSerializerSerialize() throws IOException {
        JsonSerializer<?> serializer = allDeserializers.get(Double.class.getName());
        assertNotNull(serializer);
        SerializerProvider provider = null;
        JsonGenerator generator = null;
        Double value = 10.5;
        serializer.serialize(value, generator, provider);
        //additional assertions
    }

    @Test
    public void testDoubleSerializerSerializeWithType() throws IOException {
        JsonSerializer<?> serializer = allDeserializers.get(Double.class.getName());
        assertNotNull(serializer);
        SerializerProvider provider = null;
        JsonGenerator generator = null;
        Double value = 10.5;
        TypeSerializer typeSerializer = null;
        serializer.serializeWithType(value, generator, provider, typeSerializer);
        //additional assertions
    }

    @Test
    public void testFloatSerializerSerialize() throws IOException {
        JsonSerializer<?> serializer = allDeserializers.get(Float.class.getName());
        assertNotNull(serializer);
        SerializerProvider provider = null;
        JsonGenerator generator = null;
        Float value = 5.5f;
        serializer.serialize(value, generator, provider);
        //additional assertions
    }

    @Test
    public void testShortSerializerSerialize() throws IOException {
        JsonSerializer<?> serializer = allDeserializers.get(Short.class.getName());
        assertNotNull(serializer);
        SerializerProvider provider = null;
        JsonGenerator generator = null;
        Short value = 2;
        serializer.serialize(value, generator, provider);
        //additional assertions
    }

    @Test
    public void testShortSerializerSerializeWithType() throws IOException {
        JsonSerializer<?> serializer = allDeserializers.get(Short.class.getName());
        assertNotNull(serializer);
        SerializerProvider provider = null;
        JsonGenerator generator = null;
        Short value = 2;
        TypeSerializer typeSerializer = null;
        serializer.serializeWithType(value, generator, provider, typeSerializer);
        //additional assertions
    }

    @Test
    public void testIntLikeSerializerSerialize() throws IOException {
        JsonSerializer<?> serializer = allDeserializers.get(Number.class.getName());
        assertNotNull(serializer);
        SerializerProvider provider = null;
        JsonGenerator generator = null;
        Number value = 123456;
        serializer.serialize(value, generator, provider);
        //additional assertions
    }

    @Test
    public void testBaseGetSchema() throws IOException {
        Base<?> serializer = new IntegerSerializer(Integer.class);
        SerializerProvider provider = null;
        Type typeHint = null;
        JsonNode schema = serializer.getSchema(provider, typeHint);
        //additional assertions
    }

    @Test
    public void testBaseAcceptJsonFormatVisitor() throws JsonMappingException {
        Base<?> serializer = new IntegerSerializer(Integer.class);
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        //additional assertions
    }

    @Test
    public void testBaseCreateContextual() throws JsonMappingException {
        Base<?> serializer = new IntegerSerializer(Integer.class);
        SerializerProvider prov = null;
        BeanProperty property = null;
        JsonSerializer<?> contextualSerializer = serializer.createContextual(prov, property);
        //additional assertions
    }

}