import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {

    private ObjectMapper mapper;
    private Converter<Object, String> converter;
    private StdDelegatingSerializer serializer;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        converter = mapper.getTypeFactory().constructValueType(String.class, Object.class);
        serializer = new StdDelegatingSerializer(converter);
    }

    @Test
    public void testStandardSerialization() throws JsonGenerationException, JsonMappingException, IOException {
        String input = "Test Input";
        ObjectNode expectedOutput = mapper.createObjectNode();
        expectedOutput.put("value", input);

        ObjectNode output = mapper.writeValueAsTree(serializer.serialize(input, mapper.getJsonFactory(), mapper));
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testSerializationWithContextualization() throws JsonGenerationException, JsonMappingException, IOException {
        Object input = new Object() {
            @Override
            public String toString() {
                return "Test Input";
            }
        };

        ObjectNode expectedOutput = mapper.createObjectNode();
        expectedOutput.put("value", "Test Input");

        ObjectNode output = mapper.writeValueAsTree(serializer.serialize(input, mapper.getJsonFactory(), mapper));
        Assert.assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncompatibleTypes() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper mapperWithStringSerializer = new ObjectMapper();
        StdDelegatingSerializer<String> stringSerializer = new StdDelegatingSerializer<String>(converter);

        stringSerializer.serialize("Test Input", mapperWithStringSerializer.getJsonFactory(), mapperWithStringSerializer);
    }

    @Test(expected = JsonMappingException.class)
    public void testNullInput() throws JsonGenerationException, JsonMappingException, IOException {
        serializer.serialize(null, mapper.getJsonFactory(), mapper);
    }
}