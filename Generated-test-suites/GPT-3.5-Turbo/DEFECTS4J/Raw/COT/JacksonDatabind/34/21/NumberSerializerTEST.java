import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.mockito.Mockito.*;

public class NumberSerializerTest {

    private NumberSerializer numberSerializer;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;

    @Before
    public void setup() {
        numberSerializer = new NumberSerializer(Number.class);
        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        BigDecimal value = new BigDecimal("123.45");

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        BigInteger value = new BigInteger("12345678901234567890");

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerializeInteger() throws IOException {
        Integer value = 42;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(value.intValue());
    }

    @Test
    public void testSerializeLong() throws IOException {
        Long value = 1234567890L;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(value.longValue());
    }

    @Test
    public void testSerializeDouble() throws IOException {
        Double value = 3.14159;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(value.doubleValue());
    }

    @Test
    public void testSerializeFloat() throws IOException {
        Float value = 2.71828f;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(value.floatValue());
    }

    @Test
    public void testSerializeByte() throws IOException {
        Byte value = 100;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(value.intValue());
    }

    @Test
    public void testSerializeShort() throws IOException {
        Short value = 1000;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(value.intValue());
    }

    @Test
    public void testSerializeFallback() throws IOException {
        Number value = 42.5;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(value.toString());
    }

    @Test
    public void testGetSchema() {
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = Number.class;

        numberSerializer.getSchema(provider, typeHint);

        verify(provider).createSchemaNode("number", true);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor).expectNumberFormat(typeHint);
    }
}