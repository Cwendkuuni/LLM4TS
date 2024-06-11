import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {

    private Converter<Object,?> converter;
    private JsonSerializer<Object> delegateSerializer;
    private StdDelegatingSerializer serializerUnderTest;

    @Before
    public void setup() {
        converter = new Converter.StandardConverter(Object.class, String.class);
        delegateSerializer = new JsonSerializer.StdSerializers.NullSerializer();
        serializerUnderTest = new StdDelegatingSerializer(converter, null, delegateSerializer);
    }

    @Test
    public void testEmptyConstructor() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer();
        Assert.assertNull(serializer.getDelegateSerializer());
        Assert.assertNull(serializer.getDelegateType());
    }

    @Test
    public void testConstructorWithConverter() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        Assert.assertNull(serializer.getDelegateSerializer());
        Assert.assertNull(serializer.getDelegateType());
    }

    @Test
    public void testConstructorWithConverterAndDelegateType() {
        JavaType delegateType = TypeFactory.defaultInstance().constructType(String.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
        Assert.assertEquals(delegateType, serializer.getType());
        Assert.assertEquals(delegateSerializer, serializer.getDelegateSerializer());
        Assert.assertNull(serializer.getConverter());
    }

    @Test
    public void testConstructorWithConverterAndDelegateSerializer() {
        JsonSerializer<Object> delegateSerializer = new JsonSerializer.StdSerializers.StringSerializer();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, null, delegateSerializer);
        Assert.assertEquals(delegateSerializer, serializer.getDelegateSerializer());
        Assert.assertNull(serializer.getDelegateType());
        Assert.assertNull(serializer.getConverter());
    }

    @Test
    public void testConstructorWithConverterAndDelegateTypeAndDelegateSerializer() {
        JavaType delegateType = TypeFactory.defaultInstance().constructType(String.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
        Assert.assertEquals(delegateType, serializer.getType());
        Assert.assertEquals(delegateSerializer, serializer.getDelegateSerializer());
        Assert.assertNull(serializer.getConverter());
    }

    @Test
    public void testWithDelegate() {
        JsonSerializer<Object> delegateSerializer = new JsonSerializer.StdSerializers.StringSerializer();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, null, null);
        StdDelegatingSerializer newSerializer = serializer.withDelegate(converter, null, delegateSerializer);
        Assert.assertEquals(delegateSerializer, newSerializer.getDelegateSerializer());
        Assert.assertNull(newSerializer.getDelegateType());
        Assert.assertNull(newSerializer.getConverter());
    }

    @Test
    public void testResolve() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType intType = TypeFactory.defaultInstance().constructType(int[].class);
        JsonSerializer<int[]> intSerializer = mapper.findValueSerializer(intType);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, null, intSerializer);
        SerializerProvider provider = mapper.getSerializerProvider();
        serializer.resolve(provider);
        Assert.assertEquals(intSerializer, serializer.getDelegateSerializer());
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Integer> intSerializer = mapper.findValueSerializer(Integer.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, null, null);
        SerializerProvider provider = mapper.getSerializerProvider();
        JsonSerializer<?> delegateSerializer = serializer.createContextual(provider, null);
        Assert.assertEquals(intSerializer, delegateSerializer);
    }

    @Test
    public void testSerialize() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        String expected = "\"test\"";
        Object writable = "test";
        JsonSerializer<String> stringSerializer = mapper.findValueSerializer(String.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, String.class, stringSerializer);
        JsonGenerator generator = mapper.getFactory().createGenerator();
        serializer.serialize(writable, generator, mapper.getSerializerProvider());
        String actual = generator.getOutputAsString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSerializeWithType() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        String expected = "\"test\"";
        Object writable = "test";
        TypeSerializer typeSerializer = mapper.getTypeSerializer();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, String.class, null);
        JsonGenerator generator = mapper.getFactory().createGenerator();
        serializer.serializeWithType(writable, generator, mapper.getSerializerProvider(), typeSerializer);
        String actual = generator.getOutputAsString();
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeWithNullValue() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Object> nullSerializer = mapper.findValueSerializer(Object.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, null, nullSerializer);
        JsonGenerator generator = mapper.getFactory().createGenerator();
        serializer.serialize(null, generator, mapper.getSerializerProvider());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode expected = mapper.getFactory().createObjectNode();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, null, null);
        JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), TypeFactory.defaultInstance().constructType(String.class));
        Assert.assertEquals(expected, schema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = mapper.getFactory().actOnNext(new JsonFormatVisitorWrapper());
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, null, null);
        serializer.acceptJsonFormatVisitor(visitor, TypeFactory.defaultInstance().constructType(String.class));
    }

    @Test
    public void testConvertValue() {
        Object input = new Object() {
            @Override
            public String toString() {
                return "test";
            }
        };
        Object output = serializerUnderTest.convertValue(input);
        Assert.assertEquals("test", output);
    }
}