import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class NumberSerializersTest {

    private ObjectMapper mapper;
    private SerializerProvider provider;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        provider = mapper.getSerializerProvider();
    }

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Long.class.getName()));
        assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Short.class.getName()));
        assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Double.class.getName()));
        assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Float.class.getName()));
        assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
    }

    @Test
    public void testShortSerializer() throws IOException {
        NumberSerializers.ShortSerializer serializer = NumberSerializers.ShortSerializer.instance;
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);

        serializer.serialize((short) 10, gen, provider);
        gen.flush();

        assertEquals("10", gen.toString());
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);

        serializer.serialize(100, gen, provider);
        gen.flush();

        assertEquals("100", gen.toString());
    }

    @Test(expected = Exception.class)
    public void testIntegerSerializerWithInvalidType() throws IOException {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);

        serializer.serialize("invalid", gen, provider);
        gen.flush();
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        NumberSerializers.IntLikeSerializer serializer = NumberSerializers.IntLikeSerializer.instance;
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);

        serializer.serialize(5, gen, provider);
        gen.flush();

        assertEquals("5", gen.toString());
    }

    @Test
    public void testLongSerializer() throws IOException {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);

        serializer.serialize(5000000000L, gen, provider);
        gen.flush();

        assertEquals("5000000000", gen.toString());
    }

    @Test
    public void testFloatSerializer() throws IOException {
        NumberSerializers.FloatSerializer serializer = NumberSerializers.FloatSerializer.instance;
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);

        serializer.serialize(3.14f, gen, provider);
        gen.flush();

        assertEquals("3.14", gen.toString());
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);

        serializer.serialize(123.456, gen, provider);
        gen.flush();

        assertEquals("123.456", gen.toString());
    }

    @Test
    public void testBaseGetSchema() {
        NumberSerializers.Base<Object> base = new NumberSerializers.Base<>(Double.class, JsonParser.NumberType.DOUBLE, "number");
        assertNull(base.getSchema(provider, null));
    }

    @Test
    public void testBaseAcceptJsonFormatVisitor() throws JsonMappingException {
        NumberSerializers.Base<Object> base = new NumberSerializers.Base<>(Double.class, JsonParser.NumberType.DOUBLE, "number");
        JsonFormatVisitorWrapper visitor = new ObjectMapper().getDeserializationConfig().getDefaultPrettyPrinter();
        try {
            base.acceptJsonFormatVisitor(visitor, null);
        } catch (Exception ex) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testBaseCreateContextual() throws JsonMappingException {
        NumberSerializers.Base<Object> base = new NumberSerializers.Base<>(Double.class, JsonParser.NumberType.DOUBLE, "number");
        try {
            base.createContextual(provider, null);
        } catch (Exception ex) {
            fail("Exception should not be thrown");
        }
    }

}