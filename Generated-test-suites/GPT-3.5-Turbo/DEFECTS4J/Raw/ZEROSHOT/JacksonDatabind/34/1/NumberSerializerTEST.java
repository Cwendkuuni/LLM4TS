
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NumberSerializerTest {

    private JsonGenerator jsonGenerator = mock(JsonGenerator.class);
    private SerializerProvider serializerProvider = mock(SerializerProvider.class);

    @Test
    public void testSerializeBigDecimal() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        BigDecimal value = new BigDecimal("123.456");

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        BigInteger value = new BigInteger("1234567890");

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerializeInteger() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        Integer value = 123;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerializeLong() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        Long value = 123L;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerializeDouble() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        Double value = 123.456;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerializeFloat() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        Float value = 123.456f;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerializeByte() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        Byte value = 123;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerializeShort() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        Short value = 123;

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(value.intValue());
    }

    @Test
    public void testSerializeOtherNumber() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        Number value = mock(Number.class);
        when(value.toString()).thenReturn("123");

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(value.toString());
    }

    @Test
    public void testGetSchema() {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);

        JsonNode schema = numberSerializer.getSchema(provider, typeHint);

        // Add assertions for schema if necessary
    }

    @Test
    public void testAcceptJsonFormatVisitor_WhenIsInt() throws JsonMappingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Add assertions for visitor if necessary
    }

    @Test
    public void testAcceptJsonFormatVisitor_WhenIsBigDecimal() throws JsonMappingException {
        NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Add assertions for visitor if necessary
    }
}
