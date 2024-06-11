import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class NumberSerializerTest {

    @Test
    public void testConstructorWithRawType() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        assertEquals(Number.class, serializer.handledType());
    }

    @Test
    public void testCreateContextualShouldReturnThis() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        BeanProperty property = null;
        JsonSerializer<?> contextualSerializer = serializer.createContextual(provider, property);
        assertEquals(serializer, contextualSerializer);
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        StringBuilder sb = new StringBuilder();
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(sb);
        serializer.serialize(new BigDecimal("10.5"), generator, provider);
        String result = sb.toString();
        assertEquals("10.5", result);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        StringBuilder sb = new StringBuilder();
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(sb);
        serializer.serialize(new BigInteger("10"), generator, provider);
        String result = sb.toString();
        assertEquals("10", result);
    }

    @Test
    public void testSerializeLong() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Long.class);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        StringBuilder sb = new StringBuilder();
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(sb);
        serializer.serialize(10L, generator, provider);
        String result = sb.toString();
        assertEquals("10", result);
    }

    @Test
    public void testSerializeDouble() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Double.class);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        StringBuilder sb = new StringBuilder();
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(sb);
        serializer.serialize(10.5, generator, provider);
        String result = sb.toString();
        assertEquals("10.5", result);
    }

    @Test
    public void testSerializeFloat() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Float.class);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        StringBuilder sb = new StringBuilder();
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(sb);
        serializer.serialize(10.5f, generator, provider);
        String result = sb.toString();
        assertEquals("10.5", result);
    }

    @Test
    public void testSerializeInt() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        StringBuilder sb = new StringBuilder();
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(sb);
        serializer.serialize(10, generator, provider);
        String result = sb.toString();
        assertEquals("10", result);
    }

    @Test
    public void testSerializeByte() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Byte.class);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        StringBuilder sb = new StringBuilder();
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(sb);
        serializer.serialize((byte) 10, generator, provider);
        String result = sb.toString();
        assertEquals("10", result);
    }

    @Test
    public void testSerializeShort() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Short.class);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        StringBuilder sb = new StringBuilder();
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(sb);
        serializer.serialize((short) 10, generator, provider);
        String result = sb.toString();
        assertEquals("10", result);
    }

    @Test
    public void testSerializeOtherTypes() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        StringBuilder sb = new StringBuilder();
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(sb);
        serializer.serialize(10, generator, provider);
        String result = sb.toString();
        assertEquals("10", result);
    }

    @Test
    public void testGetSchemaShouldReturnNumberSchema() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        JavaType typeHint = null;
        JsonNode schema = serializer.getSchema(provider, typeHint);
        assertEquals("number", schema.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitorWithBigInteger() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        StringBuilder sb = new StringBuilder();
        JsonFormatVisitorWrapper visitor = new ObjectMapper().getFactory().getSerializerProviderInstance().visitorForClass(sb, BigInteger.class);
        JavaType typeHint = null;
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        String result = sb.toString();
        assertEquals("integer", result);
    }

    @Test
    public void testAcceptJsonFormatVisitorWithBigDecimal() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        StringBuilder sb = new StringBuilder();
        JsonFormatVisitorWrapper visitor = new ObjectMapper().getFactory().getSerializerProviderInstance().visitorForClass(sb, BigDecimal.class);
        JavaType typeHint = null;
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        String result = sb.toString();
        assertEquals("number", result);
    }
    
}