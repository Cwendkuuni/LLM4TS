import static org.junit.Assert.*;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<String, JsonSerializer<?>>();
        NumberSerializers.addAll(allDeserializers);
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

    @Test
    public void testShortSerializer() throws IOException {
        JsonSerializer<Short> serializer = new ShortSerializer();
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(System.out);
        serializer.serialize((short) 1, generator, null);
        generator.flush();
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        JsonSerializer<Object> serializer = new IntegerSerializer();
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(System.out);
        serializer.serialize(1, generator, null);
        generator.flush();
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        JsonSerializer<Number> serializer = new IntLikeSerializer();
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(System.out);
        serializer.serialize(1, generator, null);
        generator.flush();
    }

    @Test
    public void testLongSerializer() throws IOException {
        JsonSerializer<Object> serializer = new LongSerializer();
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(System.out);
        serializer.serialize(1L, generator, null);
        generator.flush();
    }

    @Test
    public void testFloatSerializer() throws IOException {
        JsonSerializer<Object> serializer = new FloatSerializer();
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(System.out);
        serializer.serialize(1.0f, generator, null);
        generator.flush();
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        JsonSerializer<Object> serializer = new DoubleSerializer();
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(System.out);
        serializer.serialize(1.0, generator, null);
        generator.flush();
    }
}