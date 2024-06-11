import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class NumberSerializersTest {

    private JsonFactory jsonFactory;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        jsonFactory = new JsonFactory();
        objectMapper = new ObjectMapper(jsonFactory);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
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
    public void testIntegerSerializer() throws IOException {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
        JsonGenerator generator = jsonFactory.createGenerator(System.out);

        serializer.serialize(10, generator, objectMapper.getSerializerProvider());

        String expected = "10";
        assertEquals(expected, generator.toString());
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        NumberSerializers.IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();
        JsonGenerator generator = jsonFactory.createGenerator(System.out);

        serializer.serialize(10, generator, objectMapper.getSerializerProvider());

        String expected = "10";
        assertEquals(expected, generator.toString());
    }

    @Test
    public void testShortSerializer() throws IOException {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();
        JsonGenerator generator = jsonFactory.createGenerator(System.out);

        serializer.serialize((short) 10, generator, objectMapper.getSerializerProvider());

        String expected = "10";
        assertEquals(expected, generator.toString());
    }

    // Add test cases for other serializers

    // Add test cases for shared base class

    // Add additional test cases for specific scenarios

}