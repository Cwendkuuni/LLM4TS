import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonGenerator;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {

    private ObjectMapper objectMapper;
    private StdDelegatingSerializer<String> stringDelegatingSerializer;
    private StdDelegatingSerializer<Integer> integerDelegatingSerializer;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        stringDelegatingSerializer = new StdDelegatingSerializer<String>(new StringConverter(), String.class, null);
        integerDelegatingSerializer = new StdDelegatingSerializer<Integer>(null, Integer.class, null);
    }

    // Basic test cases for all methods

    @Test
    public void testDefaultConstructor() {
        StdDelegatingSerializer<?> serializer = new StdDelegatingSerializer<>(null);
        Assert.assertNotNull(serializer);
    }

    @Test
    public void testConstructorWithConverter() {
        Assert.assertNotNull(stringDelegatingSerializer);
    }

    @Test
    public void testConstructorWithClass() {
        Assert.assertNotNull(integerDelegatingSerializer);
    }

    @Test
    public void testConstructorWithConverterDelegateTypeDelegateSerializer() {
        StdDelegatingSerializer<Double> serializer = new StdDelegatingSerializer<>(
                new DoubleConverter(), Double.class, null);
        Assert.assertNotNull(serializer);
    }

    @Test
    public void testWithDelegate() {
        StdDelegatingSerializer<String> stringSerializer = new StdDelegatingSerializer<String>(
                new StringConverter(), String.class, null);
        StdDelegatingSerializer<Integer> integerSerializer = new StdDelegatingSerializer<Integer>(
                new IntegerConverter(), Integer.class, null);
        StdDelegatingSerializer<String> stringSerializerWithDelegate = stringSerializer.withDelegate(
                new StringConverter(), String.class, integerSerializer);
        Assert.assertNotSame(stringSerializer, stringSerializerWithDelegate);
    }

    @Test
    public void testResolve() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdDelegatingSerializer<String> serializer = new StdDelegatingSerializer<String>(
                new StringConverter(), String.class, null);
        serializer.resolve(mapper.getSerializerProvider());
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdDelegatingSerializer<String> serializer = new StdDelegatingSerializer<String>(
                new StringConverter(), String.class, null);
        JsonSerializer<?> contextualSerializer = serializer.createContextual(
                mapper.getSerializerProvider(), null);
        Assert.assertNotNull(contextualSerializer);
    }

    @Test
    public void testSerialize() throws IOException, JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        String value = "test";
        String expectedJson = "\"test\"";
        JsonGenerator gen = mapper.getFactory().createGenerator();
        gen.setPrettyPrinting();
        serializer.serialize(value, gen, mapper.getSerializerProvider());
        gen.flush();
        String actualJson = gen.getOutputContent();
        Assert.assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeWithType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Integer value = 1;
        String expectedJson = "{\"@type\":\"java.lang.Integer\"}1";
        JsonGenerator gen = mapper.getFactory().createGenerator();
        gen.setPrettyPrinting();
        TypeSerializer typeSerializer = mapper.getTypeSerializer();
        serializer.serializeWithType(value, gen, mapper.getSerializerProvider(), typeSerializer);
        gen.flush();
        String actualJson = gen.getOutputContent();
        Assert.assertEquals(expectedJson, actualJson);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeWithNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator();
        gen.setPrettyPrinting();
        serializer.serialize(null, gen, mapper.getSerializerProvider());
    }

    @Test(expected = JsonMappingException.class)
    public void testGetDelegateeNullDelegateSerializer() {
        Assert.assertNull(integerDelegatingSerializer.getDelegatee());
    }

    @Test(expected = JsonMappingException.class)
    public void testGetDelegateeNonJsonSerializer() {
        Object obj = new Object();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer<Object>(null, Object.class, null);
        Assert.assertNull(serializer.getDelegatee());
    }

    @Test
    public void testGetDelegateeSameSerializerInstance() {
        ObjectMapper mapper = new ObjectMapper();
        StdDelegatingSerializer<String> serializer = new StdDelegatingSerializer<String>(
                new StringConverter(), String.class, null);
        JsonSerializer<String> stringSerializer = mapper.findValueSerializer(String.class);
        Assert.assertSame(serializer.getDelegatee(), stringSerializer);
    }

    @Test
    public void testGetDelegateeDifferentSerializerInstance() throws JsonMappingException {
        ObjectMapper mapper1 = new ObjectMapper();
        ObjectMapper mapper2 = new ObjectMapper();
        StdDelegatingSerializer<String> serializer = new StdDelegatingSerializer<String>(
                new StringConverter(), String.class, null);
        JsonSerializer<String> stringSerializer1 = mapper1.findValueSerializer(String.class);
        JsonSerializer<String> stringSerializer2 = mapper2.findValueSerializer(String.class);
        Assert.assertNotSame(serializer.getDelegatee(), stringSerializer1);
        Assert.assertNotSame(serializer.getDelegatee(), stringSerializer2);
    }

    @Test
    public void testGetConverter() {
        Assert.assertSame(stringDelegatingSerializer.getConverter(), new StringConverter());
    }

    // Edge cases and exception handling test cases

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullConverter() {
        new StdDelegatingSerializer<Object>(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullDelegateType() {
        new StdDelegatingSerializer<Object>(new StringConverter(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullDelegateSerializer() {
        new StdDelegatingSerializer<Object>(new StringConverter(), String.class, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithDelegateWithNullDelegateSerializer() {
        StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<Object>(
                new StringConverter(), Object.class, null);
        StdDelegatingSerializer<Object> serializerWithDelegate = serializer.withDelegate(null, Object.class, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testWithDelegateWithNonStdDelegatingSerializer() {
        StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<Object>(
                new StringConverter(), Object.class, null);
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Object> nonStdDelegatingSerializer = mapper.findValueSerializer(String.class);
        StdDelegatingSerializer<Object> serializerWithDelegate = serializer.withDelegate(
                nonStdDelegatingSerializer, Object.class, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testWithDelegateWithSameClass() {
        StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<Object>(
                new StringConverter(), Object.class, null);
        StdDelegatingSerializer<Object> serializerWithDelegate = serializer.withDelegate(
                serializer, Object.class, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeWithNullDelegateValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        serializer.serialize(null, mapper.getFactory().createGenerator(), mapper.getSerializerProvider());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeWithInvalidDelegateValueType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Integer value = 1;
        JsonGenerator gen = mapper.getFactory().createGenerator();
        gen.setPrettyPrinting();
        serializer.serializeWithType(value, gen, mapper.getSerializerProvider(), mapper.getTypeSerializer());
    }
}