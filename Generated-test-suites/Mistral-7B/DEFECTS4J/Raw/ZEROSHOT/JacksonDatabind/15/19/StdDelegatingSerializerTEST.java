import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {

    private ObjectMapper mapper;
    private StdDelegatingSerializer<String> stringDelegatingSerializer;
    private StdDelegatingSerializer<Integer> integerDelegatingSerializer;
    private StdDelegatingSerializer<Object> objectDelegatingSerializer;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        Converter<String, Integer> stringToIntegerConverter = new StringToIntegerConverter();
        JavaType integerType = mapper.getTypeFactory().constructType(Integer.class);
        JsonSerializer<Integer> integerSerializer = mapper.findValueSerializer(Integer.class);
        stringDelegatingSerializer = new StdDelegatingSerializer<String>(stringToIntegerConverter, integerType, integerSerializer);
        JavaType objectType = mapper.getTypeFactory().constructType(Object.class);
        integerDelegatingSerializer = new StdDelegatingSerializer<Integer>(null, objectType, integerSerializer);
        objectDelegatingSerializer = new StdDelegatingSerializer<Object>(null, objectType, integerSerializer);
    }

    @Test
    public void testStringToIntegerSerialization() throws JsonGenerationException, JsonMappingException, IOException {
        String inputString = "123";
        Integer expectedOutput = 123;

        byte[] actualOutput = mapper.writeValueAsBytes(stringDelegatingSerializer, inputString);
        Object actualDeserialized = mapper.readValue(actualOutput, Object.class);

        Assert.assertEquals(expectedOutput, actualDeserialized);
    }

    @Test
    public void testNullInputString() throws JsonGenerationException, JsonMappingException {
        String inputString = null;

        byte[] actualOutput = mapper.writeValueAsBytes(stringDelegatingSerializer, inputString);

        Assert.assertArrayEquals(ObjectMapper.getDefaultInstance().writeValueAsBytes(null), actualOutput);
    }

    @Test
    public void testIntegerSerialization() throws JsonGenerationException, JsonMappingException, IOException {
        Integer inputInteger = 123;

        byte[] actualOutput = mapper.writeValueAsBytes(integerDelegatingSerializer, inputInteger);
        Object actualDeserialized = mapper.readValue(actualOutput, Object.class);

        Assert.assertEquals(inputInteger, actualDeserialized);
    }

    @Test
    public void testNullInputInteger() throws JsonGenerationException, JsonMappingException {
        Integer inputInteger = null;

        byte[] actualOutput = mapper.writeValueAsBytes(integerDelegatingSerializer, inputInteger);

        Assert.assertArrayEquals(ObjectMapper.getDefaultInstance().writeValueAsBytes(null), actualOutput);
    }

    @Test
    public void testObjectSerialization() throws JsonGenerationException, JsonMappingException, IOException {
        String inputString = "{\"name\":\"John\",\"age\":30}";
        Object inputObject = mapper.readValue(inputString, Object.class);

        byte[] actualOutput = mapper.writeValueAsBytes(objectDelegatingSerializer, inputObject);
        Object actualDeserialized = mapper.readValue(actualOutput, Object.class);

        Assert.assertEquals(inputObject, actualDeserialized);
    }

    @Test
    public void testNullInputObject() throws JsonGenerationException, JsonMappingException {
        Object inputObject = null;

        byte[] actualOutput = mapper.writeValueAsBytes(objectDelegatingSerializer, inputObject);

        Assert.assertArrayEquals(ObjectMapper.getDefaultInstance().writeValueAsBytes(null), actualOutput);
    }
}

class StringToIntegerConverter implements Converter<String, Integer> {

    @Override
    public Integer convert(String text) {
        return Integer.parseInt(text);
    }

    @Override
    public String convertAndCreateOffset(String text, int targetSize) {
        return Integer.toString(Integer.parseInt(text));
    }

    @Override
    public Integer getOutputType(SerializerProvider serializerProvider, Type typeHint) {
        return Integer.TYPE;
    }
}