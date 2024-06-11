import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;


public class NumberSerializersTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @BeforeClass
    public static void setup() {
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        // Given
        Map<String, JsonSerializer<?>> deserializers = new HashMap<>();
        NumberSerializers.addAll(deserializers);
        JsonSerializer<?> integerSerializer = deserializers.get(Integer.class.getName());

        // When
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        integerSerializer.serialize(42, generator, new SerializerProvider.NullProvider());

        // Then
        generator.flush();
        Assert.assertEquals("42", writer.toString());
    }

    @Test
    public void testIntegerSerializerWithNullValue() throws IOException {
        // Given
        Map<String, JsonSerializer<?>> deserializers = new HashMap<>();
        NumberSerializers.addAll(deserializers);
        JsonSerializer<?> integerSerializer = deserializers.get(Integer.class.getName());

        // When
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        integerSerializer.serialize(null, generator, new SerializerProvider.NullProvider());

        // Then
        generator.flush();
        Assert.assertEquals("null", writer.toString());
    }

    @Test
    public void testLongSerializer() throws IOException {
        // Given
        Map<String, JsonSerializer<?>> deserializers = new HashMap<>();
        NumberSerializers.addAll(deserializers);
        JsonSerializer<?> longSerializer = deserializers.get(Long.class.getName());

        // When
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        longSerializer.serialize(9876543210L, generator, new SerializerProvider.NullProvider());

        // Then
        generator.flush();
        Assert.assertEquals("9876543210", writer.toString());
    }

    @Test
    public void testShortSerializer() throws IOException {
        // Given
        Map<String, JsonSerializer<?>> deserializers = new HashMap<>();
        NumberSerializers.addAll(deserializers);
        JsonSerializer<?> shortSerializer = deserializers.get(Short.class.getName());

        // When
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        shortSerializer.serialize((short) 12345, generator, new SerializerProvider.NullProvider());

        // Then
        generator.flush();
        Assert.assertEquals("12345", writer.toString());
    }

    @Test
    public void testFloatSerializer() throws IOException {
        // Given
        Map<String, JsonSerializer<?>> deserializers = new HashMap<>();
        NumberSerializers.addAll(deserializers);
        JsonSerializer<?> floatSerializer = deserializers.get(Float.class.getName());

        // When
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        floatSerializer.serialize(123.456f, generator, new SerializerProvider.NullProvider());

        // Then
        generator.flush();
        Assert.assertEquals("123.456", writer.toString());
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        // Given
        Map<String, JsonSerializer<?>> deserializers = new HashMap<>();
        NumberSerializers.addAll(deserializers);
        JsonSerializer<?> doubleSerializer = deserializers.get(Double.class.getName());

        // When
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        doubleSerializer.serialize(123.456789, generator, new SerializerProvider.NullProvider());

        // Then
        generator.flush();
        Assert.assertEquals("123.456789", writer.toString());
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        // Given
        Map<String, JsonSerializer<?>> deserializers = new HashMap<>();
        NumberSerializers.addAll(deserializers);
        JsonSerializer<?> intLikeSerializer = deserializers.get(Number.class.getName());

        // When
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        intLikeSerializer.serialize(42.42, generator, new SerializerProvider.NullProvider());

        // Then
        generator.flush();
        Assert.assertEquals("42", writer.toString());
    }

    @Test
    public void testBaseGetSchema() throws IOException {
        // Given
        JsonSerializer.NumberType numberType = JsonSerializer.NumberType.INT;
        NumberSerializers.Base<?> base = new NumberSerializers.IntLikeSerializer();

        // When
        JsonNode schema = base.getSchema(new SerializerProvider(), numberType);

        // Then
        Assert.assertEquals(JsonNodeType.STRING, schema.getNodeType());
        Assert.assertEquals("number", schema.textValue());
    }

    @Test
    public void testBaseAcceptJsonFormatVisitor() throws IOException {
        // Given
        JsonSerializer.NumberType numberType = JsonSerializer.NumberType.INT;
        NumberSerializers.Base<?> base = new NumberSerializers.IntLikeSerializer();

        // When
        JsonFormatVisitorWrapper visitor = MAPPER.getProvider().getJsonFormatVisitor(base.handledType());

        // Then
        Assert.assertEquals(JsonNodeType.NUMBER, visitor.expectNumberFormat(base.handledType()).getNumberType());
    }

    @Test
    public void testBaseCreateContextual() throws IOException {
        // Given
        NumberSerializers.Base<?> base = new NumberSerializers.IntLikeSerializer();
        SerializerProvider provider = new SerializerProvider();
        BeanProperty property = new BeanProperty();

        // When
        JsonSerializer<?> contextualSerializer = base.createContextual(provider, property);

        // Then
        Assert.assertEquals(base, contextualSerializer);
    }

    @Test
    public void testIntegerSerializerSerializeWithType() throws IOException {
        // Given
        Map<String, JsonSerializer<?>> deserializers = new HashMap<>();
        NumberSerializers.addAll(deserializers);
        JsonSerializer<?> integerSerializer = deserializers.get(Integer.class.getName());

        // When
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        TypeSerializer typeSerializer = null;
        integerSerializer.serializeWithType(42, generator, new SerializerProvider.NullProvider(), typeSerializer);

        // Then
        generator.flush();
        Assert.assertEquals("42", writer.toString());
    }

    @Test
    public void testDoubleSerializerSerializeWithType() throws IOException {
        // Given
        Map<String, JsonSerializer<?>> deserializers = new HashMap<>();
        NumberSerializers.addAll(deserializers);
        JsonSerializer<?> doubleSerializer = deserializers.get(Double.class.getName());

        // When
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        TypeSerializer typeSerializer = null;
        doubleSerializer.serializeWithType(123.456789, generator, new SerializerProvider.NullProvider(), typeSerializer);

        // Then
        generator.flush();
        Assert.assertEquals("123.456789", writer.toString());
    }

}