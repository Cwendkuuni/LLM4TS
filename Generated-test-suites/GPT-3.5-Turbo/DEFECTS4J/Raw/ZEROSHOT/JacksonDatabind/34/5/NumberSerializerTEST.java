import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class NumberSerializerTest {

    private NumberSerializer numberSerializer;
    private Number number;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;

    @Before
    public void setup() {
        numberSerializer = new NumberSerializer(Number.class);
        number = mock(Number.class);
        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        BigDecimal value = new BigDecimal("123.456");
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        BigInteger value = new BigInteger("1234567890");
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerializeInteger() throws IOException {
        Integer value = 123;
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerializeLong() throws IOException {
        Long value = 1234567890L;
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerializeDouble() throws IOException {
        Double value = 123.456;
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerializeFloat() throws IOException {
        Float value = 123.456f;
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerializeByte() throws IOException {
        Byte value = 123;
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeNumber(value.intValue());
    }

    @Test
    public void testSerializeShort() throws IOException {
        Short value = 123;
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeNumber(value.intValue());
    }

    @Test
    public void testSerializeOtherNumber() throws IOException {
        Number value = mock(Number.class);
        when(value.toString()).thenReturn("12345");
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeNumber("12345");
    }

    @Test
    public void testGetSchema() {
        assertEquals("integer", numberSerializer.getSchema(serializerProvider, Number.class).asText());
        assertEquals("number", numberSerializer.getSchema(serializerProvider, BigDecimal.class).asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        if (numberSerializer.isInt()) {
            verify(visitor).expectIntFormat(eq(typeHint), eq(JsonParser.NumberType.BIG_INTEGER));
        } else {
            Class<?> handledType = numberSerializer.handledType();
            if (handledType == BigDecimal.class) {
                verify(visitor).expectFloatFormat(eq(typeHint), eq(JsonParser.NumberType.BIG_INTEGER));
            } else {
                verify(visitor).expectNumberFormat(eq(typeHint));
            }
        }
    }
}