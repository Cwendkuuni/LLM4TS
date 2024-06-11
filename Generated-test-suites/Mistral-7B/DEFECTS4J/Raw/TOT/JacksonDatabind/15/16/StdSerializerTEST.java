import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdSerializerTest {

    @Test
    public void testStringSerializer() throws JsonProcessingException {
        String input = "Hello, World!";
        ObjectMapper mapper = new ObjectMapper();

        StdSerializer<String> stringSerializer = mapper.findValueSerializer(String.class);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);

        stringSerializer.serialize(input, gen, null);
        gen.flush(); // Ensure that the serialized string is printed

        JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(mapper.writeValueAsBytes(input)));
        assertEquals(input, parser.getValueAsString());
    }

    @Test
    public void testToStringSerializer() throws JsonProcessingException {
        Object obj = new Object() {
            @Override
            public String toString() {
                return "Custom Object";
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<Object> serializer = mapper.findValueSerializer(Object.class);

        if (!(serializer instanceof ToStringSerializer)) {
            fail("Expected ToStringSerializer but got " + serializer.getClass().getName());
        }

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        ((ToStringSerializer) serializer).serialize(obj, gen, null);
        gen.flush();

        JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(mapper.writeValueAsBytes(obj)));
        assertEquals("Custom Object", parser.getValueAsString());
    }

    @Test
    public void testCustomSerializer() throws JsonProcessingException {
        int input = 42;
        ObjectMapper mapper = new ObjectMapper();

        class MyCustomSerializer extends StdSerializer<Integer> {
            public MyCustomSerializer() {
                this(Integer.class);
            }

            protected MyCustomSerializer(Class<Integer> t) {
                super(t);
            }

            @Override
            public void serialize(Integer value, JsonGenerator gen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                gen.writeNumber(value.intValue() * 2);
            }
        }

        StdSerializer<Integer> customSerializer = mapper.findValueSerializer(MyCustomSerializer.class);

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        customSerializer.serialize(input, gen, null);
        gen.flush();

        JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(mapper.writeValueAsBytes(input)));
        assertEquals((2 * input), parser.getValueAsInt());
    }

    @Test(expected = JsonMappingException.class)
    public void testExceptionHandling() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        class MyCustomSerializer extends StdSerializer<String> {
            public MyCustomSerializer() {
                this(String.class);
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                throw new RuntimeException("Custom Exception");
            }
        }

        StdSerializer<String> customSerializer = mapper.findValueSerializer(MyCustomSerializer.class);

        Object obj = new Object();
        String fieldName = "test";

        try {
            JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
            customSerializer.serialize(obj, gen, null);
        } catch (JsonMappingException e) {
            // Expected, as the custom serializer throws a RuntimeException
            String message = e.getMessage();
            assertTrue(message.contains("Custom Exception"));
            throw e; // Rethrow the exception for the test to fail
        }
    }
}