import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import com.fasterxml.jackson.annotation.JsonFormat;

public class NumberSerializerTest {

    private NumberSerializer numberSerializer;
    private JsonGenerator jsonGenerator;
    private JsonSerializer<?> jsonSerializer;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        numberSerializer = new NumberSerializer(Number.class);
        jsonGenerator = mock(JsonGenerator.class);
        jsonSerializer = mock(JsonSerializer.class);
        objectMapper = new ObjectMapper();
    }

    @After
    public void tearDown() {
        numberSerializer = null;
        jsonGenerator = null;
        jsonSerializer = null;
        objectMapper = null;
    }

    @Test
    public void testSerialize_withBigDecimal_shouldWriteNumber() throws IOException {
        BigDecimal value = new BigDecimal("10.5");
        numberSerializer.serialize(value, jsonGenerator, null);
        verify(jsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerialize_withBigInteger_shouldWriteNumber() throws IOException {
        BigInteger value = new BigInteger("10000000000000000000000000000000000000");
        numberSerializer.serialize(value, jsonGenerator, null);
        verify(jsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerialize_withLong_shouldWriteNumber() throws IOException {
        Long value = 100L;
        numberSerializer.serialize(value, jsonGenerator, null);
        verify(jsonGenerator).writeNumber(value.longValue());
    }

    @Test
    public void testSerialize_withDouble_shouldWriteNumber() throws IOException {
        Double value = 10.5;
        numberSerializer.serialize(value, jsonGenerator, null);
        verify(jsonGenerator).writeNumber(value.doubleValue());
    }

    @Test
    public void testSerialize_withFloat_shouldWriteNumber() throws IOException {
        Float value = 10.5f;
        numberSerializer.serialize(value, jsonGenerator, null);
        verify(jsonGenerator).writeNumber(value.floatValue());
    }

    @Test
    public void testSerialize_withInteger_shouldWriteNumber() throws IOException {
        Integer value = 10;
        numberSerializer.serialize(value, jsonGenerator, null);
        verify(jsonGenerator).writeNumber(value.intValue());
    }

    @Test
    public void testSerialize_withByte_shouldWriteNumber() throws IOException {
        Byte value = 10;
        numberSerializer.serialize(value, jsonGenerator, null);
        verify(jsonGenerator).writeNumber(value.intValue());
    }

    @Test
    public void testSerialize_withShort_shouldWriteNumber() throws IOException {
        Short value = 10;
        numberSerializer.serialize(value, jsonGenerator, null);
        verify(jsonGenerator).writeNumber(value.intValue());
    }

    @Test
    public void testSerialize_withUnknownNumberType_shouldWriteNumberAsString() throws IOException {
        Number value = new CustomNumber(10);
        numberSerializer.serialize(value, jsonGenerator, null);
        verify(jsonGenerator).writeNumber(value.toString());
    }

    @Test
    public void testCreateContextual_withFormatShapeString_shouldReturnToStringSerializer() throws JsonMappingException {
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        BeanProperty beanProperty = mock(BeanProperty.class);
        JsonFormat.Value value = JsonFormat.Value.forShape(JsonFormat.Shape.STRING);
        when(numberSerializer.findFormatOverrides(serializerProvider, beanProperty, Number.class)).thenReturn(value);
        JsonSerializer<?> serializer = numberSerializer.createContextual(serializerProvider, beanProperty);
        assertTrue(serializer instanceof ToStringSerializer);
    }

    @Test
    public void testCreateContextual_withUnsupportedFormatShape_shouldReturnNull() throws JsonMappingException {
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        BeanProperty beanProperty = mock(BeanProperty.class);
        JsonFormat.Value value = JsonFormat.Value.forShape(JsonFormat.Shape.ARRAY);
        when(numberSerializer.findFormatOverrides(serializerProvider, beanProperty, Number.class)).thenReturn(value);
        JsonSerializer<?> serializer = numberSerializer.createContextual(serializerProvider, beanProperty);
        assertNull(serializer);
    }

    @Test
    public void testGetSchema_withIntNumberType_shouldReturnIntegerTypeSchemaNode() {
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        JavaType javaType = mock(JavaType.class);
        JsonNode schemaNode = numberSerializer.createSchemaNode("integer", true);
        when(numberSerializer._isInt).thenReturn(true);
        JsonNode result = numberSerializer.getSchema(serializerProvider, javaType);
        assertEquals(schemaNode, result);
    }

    @Test
    public void testGetSchema_withNonIntNumberType_shouldReturnNumberTypeSchemaNode() {
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        JavaType javaType = mock(JavaType.class);
        JsonNode schemaNode = numberSerializer.createSchemaNode("number", true);
        when(numberSerializer._isInt).thenReturn(false);
        JsonNode result = numberSerializer.getSchema(serializerProvider, javaType);
        assertEquals(schemaNode, result);
    }

    @Test
    public void testAcceptJsonFormatVisitor_withIntNumberType_shouldCallVisitIntFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType javaType = mock(JavaType.class);
        when(numberSerializer._isInt).thenReturn(true);
        numberSerializer.acceptJsonFormatVisitor(visitor, javaType);
        verify(visitor).visitIntFormat(javaType, JsonParser.NumberType.BIG_INTEGER);
    }

    @Test
    public void testAcceptJsonFormatVisitor_withBigDecimalNumberType_shouldCallVisitFloatFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType javaType = mock(JavaType.class);
        when(numberSerializer._isInt).thenReturn(false);
        when(numberSerializer.handledType()).thenReturn(BigDecimal.class);
        numberSerializer.acceptJsonFormatVisitor(visitor, javaType);
        verify(visitor).visitFloatFormat(javaType, JsonParser.NumberType.BIG_DECIMAL);
    }

    @Test
    public void testAcceptJsonFormatVisitor_withNonIntAndNonBigDecimalNumberType_shouldCallExpectNumberFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType javaType = mock(JavaType.class);
        when(numberSerializer._isInt).thenReturn(false);
        when(numberSerializer.handledType()).thenReturn(Number.class);
        numberSerializer.acceptJsonFormatVisitor(visitor, javaType);
        verify(visitor).expectNumberFormat(javaType);
    }

    public class CustomNumber extends Number {

        private final int value;

        public CustomNumber(int value) {
            this.value = value;
        }

        @Override
        public int intValue() {
            return value;
        }

        @Override
        public long longValue() {
            return value;
        }

        @Override
        public float floatValue() {
            return value;
        }

        @Override
        public double doubleValue() {
            return value;
        }

    }
}