import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class StdDelegatingSerializerTest {

    private ObjectMapper mapper;
    private StdDelegatingSerializer serializer;
    private Converter<Object, Object> converter;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        converter = (Converter<Object, Object>) (fromObject) -> fromObject;
        serializer = new StdDelegatingSerializer(converter);
    }

    @Test
    public void testDefaultConstructor() throws JsonMappingException, JsonGenerationException, IOException {
        // Test default constructor
        serializer.serialize(null, null, null);
    }

    @Test
    public void testConstructorWithObjectClass() throws JsonMappingException, JsonGenerationException, IOException {
        // Test constructor with Object class
        serializer = new StdDelegatingSerializer(Object.class, converter);
        serializer.serialize(new Object(), null, null);
    }

    @Test
    public void testConstructorWithConverter() throws JsonMappingException, JsonGenerationException, IOException {
        // Test constructor with converter
        serializer = new StdDelegatingSerializer(converter);
        serializer.serialize(new Object(), null, null);
    }

    @Test
    public void testConstructorWithConverterDelegateTypeAndDelegateSerializer() throws JsonMappingException, JsonGenerationException, IOException {
        // Test constructor with converter, delegateType, and delegateSerializer
        ObjectMapper objectMapper = new ObjectMapper();
        JsonSerializer<Object> delegateSerializer = objectMapper.findValueSerializer(String.class);
        JavaType delegateType = objectMapper.getTypeFactory().constructType(String.class);
        serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
        serializer.serialize("test", null, null);
    }

    @Test
    public void testWithDelegateMethod() throws JsonMappingException, JsonGenerationException, IOException {
        // Test withDelegate method
        ObjectMapper objectMapper = new ObjectMapper();
        JsonSerializer<Object> delegateSerializer = objectMapper.findValueSerializer(String.class);
        JavaType delegateType = objectMapper.getTypeFactory().constructType(String.class);
        serializer = new StdDelegatingSerializer(converter, null, null);
        StdDelegatingSerializer newSerializer = serializer.withDelegate(converter, delegateType, delegateSerializer);
        newSerializer.serialize("test", null, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testSubclassMustOverrideWithDelegateMethod() throws JsonMappingException, JsonGenerationException, IOException {
        // Test IllegalStateException when subclass doesn't override withDelegate method
        class TestSerializer extends StdDelegatingSerializer {
            @Override
            protected StdDelegatingSerializer withDelegate(Converter<Object,?> converter,
                    JavaType delegateType, JsonSerializer<?> delegateSerializer) {
                // no implementation
            }
        }
        new TestSerializer(converter, null, null).serialize("test", null, null);
    }

    @Test
    public void testResolveMethod() throws JsonMappingException {
        // Test resolve method
        SerializerProvider provider = mapper.getSerializerProvider();
        serializer = new StdDelegatingSerializer(converter, null, null);
        serializer.resolve(provider);
    }

    @Test
    public void testCreateContextualMethod() throws JsonMappingException, JsonGenerationException, IOException {
        // Test createContextual method
        SerializerProvider provider = mapper.getSerializerProvider();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonSerializer<Object> delegateSerializer = objectMapper.findValueSerializer(String.class);
        JavaType delegateType = objectMapper.getTypeFactory().constructType(String.class);
        serializer = new StdDelegatingSerializer(converter, null, null);
        JsonSerializer<?> contextualSerializer = serializer.createContextual(provider, null);
        Assert.assertEquals(delegateSerializer.getClass(), contextualSerializer.getClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateContextualMethodWithNullDelegateSerializer() throws JsonMappingException, JsonGenerationException, IOException {
        // Test JsonMappingException when delegateSerializer is null
        SerializerProvider provider = mapper.getSerializerProvider();
        JsonSerializer<Object> delegateSerializer = null;
        serializer = new StdDelegatingSerializer(converter, null, null);
        JsonSerializer<?> contextualSerializer = serializer.createContextual(provider, null);
    }

    @Test
    public void testGetConverterMethod() throws JsonMappingException, JsonGenerationException, IOException {
        // Test getConverter method
        Assert.assertEquals(converter, serializer.getConverter());
    }

    @Test
    public void testGetDelegateSerializerMethod() throws JsonMappingException, JsonGenerationException, IOException {
        // Test getDelegateSerializer method
        ObjectMapper objectMapper = new ObjectMapper();
        JsonSerializer<Object> delegateSerializer = objectMapper.findValueSerializer(String.class);
        JavaType delegateType = objectMapper.getTypeFactory().constructType(String.class);
        serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
        Assert.assertEquals(delegateSerializer, serializer.getDelegatee());
    }

    @Test
    public void testSerializeMethodWithNullValue() throws JsonMappingException, JsonGenerationException, IOException {
        // Test serialize method with null value
        serializer.serialize(null, null, null);
    }

    @Test
    public void testSerializeMethod() throws JsonMappingException, JsonGenerationException, IOException {
        // Test serialize method
        ObjectMapper objectMapper = new ObjectMapper();
        JsonSerializer<Object> delegateSerializer = objectMapper.findValueSerializer(String.class);
        JavaType delegateType = objectMapper.getTypeFactory().constructType(String.class);
        serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
        String testString = "test";
        String expectedJson = "\"test\"";
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();
        serializer.serialize(testString, jsonGenerator, null);
        jsonGenerator.flush();
        String actualJson = jsonGenerator.getOutputContent().toString();
        Assert.assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeMethodWithTypeSerializer() throws JsonMappingException, JsonGenerationException, IOException {
        // Test serialize method with TypeSerializer
        ObjectMapper objectMapper = new ObjectMapper();
        JsonSerializer<Object> delegateSerializer = objectMapper.findValueSerializer(String.class);
        JavaType delegateType = objectMapper.getTypeFactory().constructType(String.class);
        TypeSerializer typeSerializer = mapper.getTypeSerializer();
        serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
        String testString = "test";
        String expectedJson = "\"test\"";
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();
        serializer.serializeWithType(testString, jsonGenerator, null, typeSerializer);
        jsonGenerator.flush();
        String actualJson = jsonGenerator.getOutputContent().toString();
        Assert.assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testIsEmptyMethodWithNullValue() throws JsonMappingException, JsonGenerationException, IOException {
        // Test isEmpty method with null value
        Assert.assertFalse(serializer.isEmpty(null));
    }

    @Test
    public void testIsEmptyMethod() throws JsonMappingException, JsonGenerationException, IOException {
        // Test isEmpty method
        ObjectMapper objectMapper = new ObjectMapper();
        JsonSerializer<Object> delegateSerializer = objectMapper.findValueSerializer(String.class);
        JavaType delegateType = objectMapper.getTypeFactory().constructType(String.class);
        serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
        String testString = "";
        boolean expectedResult = true;
        boolean actualResult = serializer.isEmpty(null);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetSchemaMethod() throws JsonMappingException {
        // Test getSchema method
        SerializerProvider provider = mapper.getSerializerProvider();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonSerializer<Object> delegateSerializer = objectMapper.findValueSerializer(String.class);
        JavaType delegateType = objectMapper.getTypeFactory().constructType(String.class);
        serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
        Assert.assertNotNull(serializer.getSchema(provider, delegateType.getRawClass()));
    }

    @Test
    public void testGetSchemaMethodWithOptional() throws JsonMappingException {
        // Test getSchema method with isOptional flag
        SerializerProvider provider = mapper.getSerializerProvider();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonSerializer<Object> delegateSerializer = objectMapper.findValueSerializer(String.class);
        JavaType delegateType = objectMapper.getTypeFactory().constructType(String.class);
        serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
        Assert.assertNotNull(serializer.getSchema(provider, delegateType.getRawClass(), true));
    }

    @Test
    public void testAcceptJsonFormatVisitorMethod() throws JsonMappingException {
        // Test acceptJsonFormatVisitor method
        SerializerProvider provider = mapper.getSerializerProvider();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonSerializer<Object> delegateSerializer = objectMapper.findValueSerializer(String.class);
        JavaType delegateType = objectMapper.getTypeFactory().constructType(String.class);
        serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
        JsonFormatVisitorWrapper visitor = mapper.getSerializerConfig().getFactory().createJsonFormatVisitor(null, null, null);
        serializer.acceptJsonFormatVisitor(visitor, delegateType);
    }
}