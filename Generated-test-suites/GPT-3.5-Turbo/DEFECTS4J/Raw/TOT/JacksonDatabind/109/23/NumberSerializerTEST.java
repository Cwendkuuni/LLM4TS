
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class NumberSerializerTest {

    @Test
    public void testCreateContextualWithStringFormat() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);

        ObjectNode node = JsonNodeFactory.instance
            .objectNode()
            .put("shape", "STRING");

        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        when(visitor.expectObjectFormat(typeHint)).thenReturn(node);

        JsonSerializer<?> contextualSerializer = serializer.createContextual(provider, property);

        assertEquals(ToStringSerializer.instance, contextualSerializer);
    }

    @Test
    public void testCreateContextualWithNonStringFormat() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);

        ObjectNode node = JsonNodeFactory.instance
            .objectNode()
            .put("shape", "OTHER");

        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        when(visitor.expectObjectFormat(typeHint)).thenReturn(node);

        JsonSerializer<?> contextualSerializer = serializer.createContextual(provider, property);

        assertEquals(serializer, contextualSerializer);
    }

    @Test
    public void testSerializeWithBigDecimal() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        BigDecimal value = new BigDecimal("10.50");
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(value);
    }

    @Test
    public void testSerializeWithBigInteger() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        BigInteger value = new BigInteger("1234567890");
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(value);
    }

    @Test
    public void testSerializeWithLong() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        Long value = Long.valueOf(10);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(value.longValue());
    }

    @Test
    public void testSerializeWithDouble() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        Double value = Double.valueOf(10.50);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(value.doubleValue());
    }

    @Test
    public void testSerializeWithFloat() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        Float value = Float.valueOf(10.50f);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(value.floatValue());
    }

    @Test
    public void testSerializeWithInteger() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        Integer value = Integer.valueOf(10);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(value.intValue());
    }

    @Test
    public void testSerializeWithByte() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        Byte value = Byte.valueOf((byte) 10);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(value.intValue());
    }

    @Test
    public void testSerializeWithShort() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        Short value = Short.valueOf((short) 10);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(value.intValue());
    }

    @Test
    public void testSerializeWithOtherNumberType() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        Number value = new Number() {
            @Override
            public int intValue() {
                return 10;
            }
        };
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(value.toString());
    }

    @Test
    public void testGetSchemaWithIntegerType() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectNode schemaNode = JsonNodeFactory.instance.objectNode();

        JsonNode schema = serializer.getSchema(provider, Integer.class);

        assertEquals(schemaNode.put("type", "integer").put("required", true), schema);
    }

    @Test
    public void testGetSchemaWithBigIntegerType() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectNode schemaNode = JsonNodeFactory.instance.objectNode();

        JsonNode schema = serializer.getSchema(provider, BigInteger.class);

        assertEquals(schemaNode.put("type", "integer").put("required", true), schema);
    }

    @Test
    public void testGetSchemaWithBigDecimalType() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectNode schemaNode = JsonNodeFactory.instance.objectNode();

        JsonNode schema = serializer.getSchema(provider, BigDecimal.class);

        assertEquals(schemaNode.put("type", "number").put("required", true), schema);
    }

    @Test
    public void testAcceptJsonFormatVisitorWithBigIntegerType() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor).expectNumberFormat(typeHint);
    }

    @Test
    public void testAcceptJsonFormatVisitorWithBigDecimalType() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor).expectNumberFormat(typeHint);
    }

    @Test
    public void testAcceptJsonFormatVisitorWithOtherNumberType() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor).expectNumberFormat(typeHint);
    }

}

