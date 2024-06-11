import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<String, JsonSerializer<?>>();
        NumberSerializers.addAll(allDeserializers);

        // Check if all the necessary serializers have been added
        assertEquals(8, allDeserializers.size());

        // Check if Integer class serializer has been added
        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertNotNull(allDeserializers.get(Integer.class.getName()));

        // Check if Integer type serializer has been added
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        assertNotNull(allDeserializers.get(Integer.TYPE.getName()));

        // Check if Long class serializer has been added
        assertTrue(allDeserializers.containsKey(Long.class.getName()));
        assertNotNull(allDeserializers.get(Long.class.getName()));

        // Check if Long type serializer has been added
        assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        assertNotNull(allDeserializers.get(Long.TYPE.getName()));

        // Check if Byte class serializer has been added
        assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        assertNotNull(allDeserializers.get(Byte.class.getName()));

        // Check if Byte type serializer has been added
        assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        assertNotNull(allDeserializers.get(Byte.TYPE.getName()));

        // Check if Short class serializer has been added
        assertTrue(allDeserializers.containsKey(Short.class.getName()));
        assertNotNull(allDeserializers.get(Short.class.getName()));

        // Check if Short type serializer has been added
        assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
        assertNotNull(allDeserializers.get(Short.TYPE.getName()));

        // Check if Double class serializer has been added
        assertTrue(allDeserializers.containsKey(Double.class.getName()));
        assertNotNull(allDeserializers.get(Double.class.getName()));

        // Check if Double type serializer has been added
        assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
        assertNotNull(allDeserializers.get(Double.TYPE.getName()));

        // Check if Float class serializer has been added
        assertTrue(allDeserializers.containsKey(Float.class.getName()));
        assertNotNull(allDeserializers.get(Float.class.getName()));

        // Check if Float type serializer has been added
        assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
        assertNotNull(allDeserializers.get(Float.TYPE.getName()));
    }

    @Test
    public void testBaseGetSchema() {
        NumberSerializers.Base<?> serializer = new NumberSerializers.Base<Integer>(Integer.class, JsonParser.NumberType.INT, "integer") {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) {
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
            }

            @Override
            public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
                return null;
            }
        };

        // Test getSchema method
        ObjectMapper mapper = new ObjectMapper();
        JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), null);
        assertEquals("number", schema.get("type").asText());
        assertNull(schema.get("properties"));
    }

    @Test
    public void testBaseAcceptJsonFormatVisitor() {
        NumberSerializers.Base<?> serializer = new NumberSerializers.Base<Float>(Float.class, JsonParser.NumberType.FLOAT, "number") {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) {
            }

            @Override
            public void getSchema(SerializerProvider provider, Type typeHint) {
            }

            @Override
            public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
                return null;
            }
        };

        // Test acceptJsonFormatVisitor method
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base(mapper.getSerializerProvider());
        try {
            serializer.acceptJsonFormatVisitor(visitor, null);
        } catch (JsonMappingException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testBaseCreateContextual() {
        NumberSerializers.Base<?> serializer = new NumberSerializers.Base<Long>(Long.class, JsonParser.NumberType.LONG, "number") {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) {
            }

            @Override
            public void getSchema(SerializerProvider provider, Type typeHint) {
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
            }
        };

        // Test createContextual method - case when format shape is STRING
        ObjectMapper mapper = new ObjectMapper();
        BeanProperty property = null;
        try {
            JsonSerializer<?> result = serializer.createContextual(mapper.getSerializerProvider(), property);
            assertNotNull(result);
            assertEquals(NumberSerializers.ToStringSerializer.instance, result);
        } catch (JsonMappingException e) {
            fail("Exception should not be thrown");
        }

        // Test createContextual method - case when format shape is not STRING
        property = new BeanProperty.Std(null, null, null, null);
        try {
            JsonSerializer<?> result = serializer.createContextual(mapper.getSerializerProvider(), property);
            assertNotNull(result);
            assertEquals(serializer, result);
        } catch (JsonMappingException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testShortSerializerSerialize() {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();

        // Test serialize method
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        try {
            serializer.serialize((short) 10, gen, provider);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testIntegerSerializerSerialize() {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);

        // Test serialize method
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        try {
            serializer.serialize(10, gen, provider);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }

        // Test serializeWithType method
        TypeSerializer typeSer = null;
        try {
            serializer.serializeWithType(10, gen, provider, typeSer);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testIntLikeSerializerSerialize() {
        NumberSerializers.IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();

        // Test serialize method
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        try {
            serializer.serialize(10, gen, provider);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testLongSerializerSerialize() {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);

        // Test serialize method
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        try {
            serializer.serialize(10L, gen, provider);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testFloatSerializerSerialize() {
        NumberSerializers.FloatSerializer serializer = new NumberSerializers.FloatSerializer();

        // Test serialize method
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        try {
            serializer.serialize(10.5f, gen, provider);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testDoubleSerializerSerialize() {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);

        // Test serialize method
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        try {
            serializer.serialize(10.5, gen, provider);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }

        // Test serializeWithType method
        TypeSerializer typeSer = null;
        try {
            serializer.serializeWithType(10.5, gen, provider, typeSer);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

}