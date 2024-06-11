import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {

    private ObjectMapper objectMapper;
    private StdDelegatingSerializer<String> stringSerializer;
    private StdDelegatingSerializer<Integer> integerSerializer;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        stringSerializer = new StdDelegatingSerializer<String>(String::valueOf, String.class, new StdSerializer<String>(String.class) {});
        integerSerializer = new StdDelegatingSerializer<Integer>(Integer::toString, Integer.class, new StdSerializer<Integer>(Integer.class) {});
    }

    @Test
    public void testStringSerializer() throws JsonMappingException, JsonGenerationException, IOException {
        String input = "Hello, World!";
        ObjectNode output = objectMapper.writeValueAsTree(stringSerializer.setValue(input));
        String expected = "\"Hello, World!\"";
        Assert.assertEquals(expected, output.toString());
    }

    @Test
    public void testIntegerSerializer() throws JsonMappingException, JsonGenerationException, IOException {
        Integer input = 123;
        ObjectNode output = objectMapper.writeValueAsTree(integerSerializer.setValue(input));
        String expected = "123";
        Assert.assertEquals(expected, output.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSameTypeSerializationFails() throws JsonMappingException, JsonGenerationException, IOException {
        Integer input = 123;
        stringSerializer.serialize(input, objectMapper.getFactory().createGenerator(new StringWriter()), objectMapper.getSerializerProvider());
    }

    @Test
    public void testDynamicSerializer() throws JsonMappingException, JsonGenerationException, IOException {
        Object input = new Object() {
            @Override
            public String toString() {
                return "Dynamic Object";
            }
        };
        ObjectNode output = objectMapper.writeValueAsTree(stringSerializer.serialize(input, objectMapper.getFactory().createGenerator(new StringWriter()), objectMapper.getSerializerProvider()));
        String expected = "\"Dynamic Object\"";
        Assert.assertEquals(expected, output.toString());
    }
}