import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {

    private ObjectMapper objectMapper;
    private StdDelegatingSerializer<String> stringDelegatingSerializer;
    private StdDelegatingSerializer<Integer> integerDelegatingSerializer;
    private StdDelegatingSerializer<Object> objectDelegatingSerializer;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        stringDelegatingSerializer = new StdDelegatingSerializer<String>(
                new SimpleStringConverter(), JsonTypeFactory.getDefaultInstance().constructType(String.class)) {
            @Override
            protected Object convertValue(Object value) {
                return value.toString();
            }
        };
        integerDelegatingSerializer = new StdDelegatingSerializer<Integer>(
                Integer.class, new SimpleIntegerConverter(), null) {
            @Override
            protected Object convertValue(Object value) {
                return (Integer) value;
            }
        };
        objectDelegatingSerializer = new StdDelegatingSerializer<Object>(
                null, null, null);
    }

    @Test
    public void testSerializeString() throws JsonGenerationException, IOException {
        String input = "Hello World";
        JsonSerializer<Object> delegateSerializer = objectMapper.findValueSerializer(String.class);

        stringDelegatingSerializer.delegateSerializer = delegateSerializer;
        String output = stringDelegatingSerializer.serialize(input, null, null);
        JsonNode expected = objectMapper.readTree("{\"string\":\"Hello World\"}");
        Assert.assertEquals(expected.toString(), output);
    }

    @Test
    public void testSerializeInteger() throws JsonGenerationException, IOException {
        Integer input = 123;
        JsonSerializer<Object> delegateSerializer = objectMapper.findValueSerializer(Integer.class);

        integerDelegatingSerializer.delegateSerializer = delegateSerializer;
        String output = integerDelegatingSerializer.serialize(input, null, null);
        JsonNode expected = objectMapper.readTree("{\"number\":123}");
        Assert.assertEquals(expected.toString(), output);
    }

    @Test(expected = IllegalStateException.class)
    public void testSerializeObjectNullConverter() throws JsonGenerationException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        StdDelegatingSerializer<Object> objectDelegatingSerializer = new StdDelegatingSerializer<Object>(
                null, null, null);

        objectDelegatingSerializer.serialize(null, null, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testSerializeObjectNullDelegateSerializer() throws JsonGenerationException, IOException {
        StdDelegatingSerializer<Object> objectDelegatingSerializer = new StdDelegatingSerializer<Object>(
                null, null, null);

        objectDelegatingSerializer.delegateSerializer = null;
        objectDelegatingSerializer.serialize(new Object(), null, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testSerializeObjectNullDelegateType() throws JsonGenerationException, IOException {
        StdDelegatingSerializer<Object> objectDelegatingSerializer = new StdDelegatingSerializer<Object>(
                null, null, null);

        objectDelegatingSerializer.delegateType = null;
        objectDelegatingSerializer.serialize(new Object(), null, null);
    }

    @Test
    public void testSerializeObject() throws JsonGenerationException, IOException {
        Object input = new Object() {
            @Override
            public String toString() {
                return "Hello World";
            }
        };
        JsonSerializer<Object> delegateSerializer = objectMapper.findValueSerializer(Object.class);

        objectDelegatingSerializer.delegateSerializer = delegateSerializer;
        String output = objectDelegatingSerializer.serialize(input, null, null);
        JsonNode expected = objectMapper.readTree("{\"string\":\"Hello World\"}");
        Assert.assertEquals(expected.toString(), output);
    }

    @Test
    public void testGetSchemaString() throws JsonMappingException {
        JsonNode expected = objectMapper.readTree("{\"type\":\"string\",\"properties\":{}}");
        JsonNode output = stringDelegatingSerializer.getSchema(objectMapper.getSerializationConfig(), String.class);
        Assert.assertEquals(expected.toString(), output.toString());
    }

    @Test
    public void testGetSchemaInteger() throws JsonMappingException {
        JsonNode expected = objectMapper.readTree("{\"type\":\"number\",\"numberFormat\":{\"precision\":3}}");
        JsonNode output = integerDelegatingSerializer.getSchema(objectMapper.getSerializationConfig(), Integer.class);
        Assert.assertEquals(expected.toString(), output.toString());
    }

    @Test
    public void testGetSchemaObject() throws JsonMappingException {
        JsonNode expected = objectMapper.readTree("{ \"type\": \"object\", \"properties\": {}}");
        JsonNode output = objectDelegatingSerializer.getSchema(objectMapper.getSerializationConfig(), Object.class);
        Assert.assertEquals(expected.toString(), output.toString());
    }
}