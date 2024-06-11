import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.DoubleSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.FloatSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntegerSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntLikeSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.LongSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.ShortSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.Base;

import org.junit.Before;
import org.junit.Test;

public class NumberSerializersTest {

    private static Map<String, JsonSerializer<?>> allDeserializers = new HashMap<String, JsonSerializer<?>>();

    @Before
    public void setUp() throws Exception {
        NumberSerializers.addAll(allDeserializers);
    }

    // Test IntegerSerializer.serialize
    @Test
    public void testIntegerSerializerSerialize() throws IOException {
        IntegerSerializer integerSerializer = new IntegerSerializer();
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(System.out);
        SerializerProvider serializerProvider = new SerializerProvider();
        serializerProvider = serializerProvider.createInstance(jsonFactory, new JsonSerializer<Object>() {

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                assertEquals(42, value);
            }
        });
        integerSerializer.serialize(42, jsonGenerator, serializerProvider);
    }

    // Test IntegerSerializer.serializeWithType
    @Test
    public void testIntegerSerializerSerializeWithType() throws IOException {
        IntegerSerializer integerSerializer = new IntegerSerializer();
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(System.out);
        SerializerProvider serializerProvider = new SerializerProvider();
        serializerProvider = serializerProvider.createInstance(jsonFactory, new JsonSerializer<Object>() {

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                assertEquals(42, value);
            }
        });
        TypeSerializer typeSerializer = null;
        integerSerializer.serializeWithType(42, jsonGenerator, serializerProvider, typeSerializer);
    }

    // Test IntegerSerializer.createContextual
    @Test
    public void testIntegerSerializerCreateContextual() throws JsonMappingException {
        IntegerSerializer integerSerializer = new IntegerSerializer();
        // Test property is null
        JsonSerializer<?> result = integerSerializer.createContextual(null, null);
        assertEquals(integerSerializer, result);
        // Test JsonFormat.Value is null
        result = integerSerializer.createContextual(null, new BeanProperty() {
        });
        assertEquals(integerSerializer, result);
        // Test JsonFormat.Shape.STRING
        result = integerSerializer.createContextual(null, new BeanProperty() {

            @Override
            public AnnotatedMember getMember() {
                return null;
            }
        });
        assertTrue(result instanceof ToStringSerializer);
    }

    // Test ShortSerializer.serialize
    @Test
    public void testShortSerializerSerialize() throws IOException {
        ShortSerializer shortSerializer = new ShortSerializer();
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(System.out);
        SerializerProvider serializerProvider = new SerializerProvider();
        serializerProvider = serializerProvider.createInstance(jsonFactory, new JsonSerializer<Object>() {

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                assertEquals((short) 42, value);
            }
        });
        shortSerializer.serialize((short) 42, jsonGenerator, serializerProvider);
    }

    // Test IntegerSerializer.getSchema
    @Test
    public void testIntegerSerializerGetSchema() throws JsonParseException, IOException {
        IntegerSerializer integerSerializer = new IntegerSerializer();
        SerializerProvider serializerProvider = new SerializerProvider();
        JavaType typeHint = null;
        JsonNode result = integerSerializer.getSchema(serializerProvider, typeHint);
        assertEquals("integer", result.get("type").asText());
    }

    // Test IntegerSerializer.acceptJsonFormatVisitor
    @Test
    public void testIntegerSerializerAcceptJsonFormatVisitor() throws JsonMappingException {
        IntegerSerializer integerSerializer = new IntegerSerializer();
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base(JsonFactory.builder().build());
        JavaType typeHint = null;
        integerSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        assertTrue(visitor.getBeanProperty() != null);
    }

    // Test IntLikeSerializer.serialize
    @Test
    public void testIntLikeSerializerSerialize() throws IOException {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(System.out);
        SerializerProvider serializerProvider = new SerializerProvider();
        serializerProvider = serializerProvider.createInstance(jsonFactory, new JsonSerializer<Object>() {

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                assertEquals(42, value);
            }
        });
        intLikeSerializer.serialize(42, jsonGenerator, serializerProvider);
    }

    // Test LongSerializer.serialize
    @Test
    public void testLongSerializerSerialize() throws IOException {
        LongSerializer longSerializer = new LongSerializer();
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(System.out);
        SerializerProvider serializerProvider = new SerializerProvider();
        serializerProvider = serializerProvider.createInstance(jsonFactory, new JsonSerializer<Object>() {

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                assertEquals(42L, value);
            }
        });
        longSerializer.serialize(42L, jsonGenerator, serializerProvider);
    }

    // Test FloatSerializer.serialize
    @Test
    public void testFloatSerializerSerialize() throws IOException {
        FloatSerializer floatSerializer = new FloatSerializer();
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(System.out);
        SerializerProvider serializerProvider = new SerializerProvider();
        serializerProvider = serializerProvider.createInstance(jsonFactory, new JsonSerializer<Object>() {

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                assertEquals(42.0f, value);
            }
        });
        floatSerializer.serialize(42.0f, jsonGenerator, serializerProvider);
    }

    // Test DoubleSerializer.serialize
    @Test
    public void testDoubleSerializerSerialize() throws IOException {
        DoubleSerializer doubleSerializer = new DoubleSerializer();
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(System.out);
        SerializerProvider serializerProvider = new SerializerProvider();
        serializerProvider = serializerProvider.createInstance(jsonFactory, new JsonSerializer<Object>() {

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                assertEquals(42.0, value);
            }
        });
        doubleSerializer.serialize(42.0, jsonGenerator, serializerProvider);
    }

    // Test DoubleSerializer.serializeWithType
    @Test
    public void testDoubleSerializerSerializeWithType() throws IOException {
        DoubleSerializer doubleSerializer = new DoubleSerializer();
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(System.out);
        SerializerProvider serializerProvider = new SerializerProvider();
        serializerProvider = serializerProvider.createInstance(jsonFactory, new JsonSerializer<Object>() {

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                assertEquals(42.0, value);
            }
        });
        TypeSerializer typeSerializer = null;
        doubleSerializer.serializeWithType(42.0, jsonGenerator, serializerProvider, typeSerializer);
    }

    // Test each test case and test edge cases for allDeserializers
    @Test
    public void testAllDeserializers() {
        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Long.class.getName()));
        assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Short.class.getName()));
        assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Float.class.getName()));
        assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Double.class.getName()));
        assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
    }

}