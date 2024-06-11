import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = JsonMapperFactory.getMapper();
    }

    @Test
    public void testDefaultConstructor() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        Assert.assertNull(serializer.getDelegateType());
        Assert.assertNull(serializer.getDelegateSerializer());
    }

    @Test
    public void testConstructorWithConverter() {
        Converter<Object, String> converter = new SimpleStringConverter();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        Assert.assertNull(serializer.getDelegateType());
        Assert.assertNull(serializer.getDelegateSerializer());
    }

    @Test
    public void testConstructorWithClass() {
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        Converter<String, Object> converter = new SimpleStringConverter();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(javaType, converter);
        Assert.assertEquals(javaType, serializer.getDelegateType());
        Assert.assertNull(serializer.getDelegateSerializer());
    }

    @Test
    public void testConstructorWithConverterTypeSerializer() {
        Converter<Object, String> converter = new SimpleStringConverter();
        JsonSerializer<String> stringSerializer = mapper.findValueSerializer(String.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, null, stringSerializer);
        Assert.assertNull(serializer.getDelegateType());
        Assert.assertEquals(stringSerializer, serializer.getDelegateSerializer());
    }

    @Test
    public void testIsEmpty() throws IOException {
        StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer(null);
        Object nullValue = null;
        Object emptyString = "";
        Object emptyList = new Object(){}; // Java 8 empty list

        Assert.assertTrue(serializer.isEmpty(nullValue));
        Assert.assertFalse(serializer.isEmpty(emptyString));
        Assert.assertFalse(serializer.isEmpty(emptyList));
    }

    @Test
    public void testSerialize() throws JsonGenerationException, JsonMappingException, IOException {
        StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer(null);
        Object delegateValue = "test";
        String expected = "\"test\"";

        JsonGenerator gen = mapper.getFactory().createGenerator();
        serializer.serialize(delegateValue, gen, new SerializerProvider());
        gen.flush();

        String actual = gen.getOutputBuffer().toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSerializeWithType() throws JsonGenerationException, JsonMappingException, IOException {
        StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer(null);
        Object delegateValue = "test";

        JsonGenerator gen = mapper.getFactory().createGenerator();
        TypeSerializer typeSerializer = mapper.getTypeSerializer();
        serializer.serializeWithType(delegateValue, gen, new SerializerProvider(), typeSerializer);
        gen.flush();

        String actual = gen.getOutputBuffer().toString();
        String expected = "{\"@class\":\"java.lang.String\",\"value\":\"test\"}";
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testResolveWithNullDelegateSerializer() throws JsonMappingException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        serializer.resolve(new SerializerProvider());
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateContextualWithNullDelegateSerializer() throws JsonMappingException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        serializer.createContextual(new SerializerProvider(), null);
    }

    @Test
    public void testCreateContextualWithNullDelegateType() throws JsonMappingException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        BeanProperty property = new BeanProperty("test");
        JsonSerializer<Object> delegateSerializer = mapper.findValueSerializer(String.class);

        StdDelegatingSerializer result = serializer.createContextual(new SerializerProvider(), property);
        Assert.assertNotNull(result);
        Assert.assertEquals(delegateSerializer, result.getDelegateSerializer());
    }

    @Test
    public void testCreateContextualWithNotNullDelegateType() throws JsonMappingException {
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        BeanProperty property = new BeanProperty("test");
        JsonSerializer<Object> delegateSerializer = mapper.findValueSerializer(String.class);

        StdDelegatingSerializer result = serializer.createContextual(new SerializerProvider(), property);
        Assert.assertNotNull(result);
        Assert.assertEquals(javaType, result.getDelegateType());
        Assert.assertEquals(delegateSerializer, result.getDelegateSerializer());
    }

    @Test
    public void testGetConverter() {
        Converter<Object, String> converter = new SimpleStringConverter();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        Assert.assertEquals(converter, serializer.getConverter());
    }

    @Test
    public void testGetDelegatee() {
        Converter<Object, String> converter = new SimpleStringConverter();
        JsonSerializer<String> stringSerializer = mapper.findValueSerializer(String.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null, null, stringSerializer);
        Assert.assertEquals(stringSerializer, serializer.getDelegatee());
    }

    private static class SimpleStringConverter implements Converter<Object, String> {
        @Override
        public String convert(Object value) {
            return value.toString();
        }

        @Override
        public Object convertAndSetAll(Object value, MapperFactory factory) {
            return value;
        }
    }

    private static class BeanProperty {
        String name;

        public BeanProperty(String name) {
            this.name = name;
        }
    }
}