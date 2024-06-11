import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

import static org.mockito.Mockito.*;

public class NumberSerializerTest {

    @Mock
    private JsonGenerator mockJsonGenerator;

    @Mock
    private SerializerProvider mockSerializerProvider;

    private NumberSerializer numberSerializer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        numberSerializer = new NumberSerializer(Number.class);
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        BigDecimal value = new BigDecimal("10.5");

        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);

        verify(mockJsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        BigInteger value = new BigInteger("10");

        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);

        verify(mockJsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerializeInteger() throws IOException {
        Integer value = 10;

        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);

        verify(mockJsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerializeLong() throws IOException {
        Long value = 10L;

        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);

        verify(mockJsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerializeDouble() throws IOException {
        Double value = 10.5;

        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);

        verify(mockJsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerializeFloat() throws IOException {
        Float value = 10.5f;

        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);

        verify(mockJsonGenerator).writeNumber(value);
    }

    @Test
    public void testSerializeByte() throws IOException {
        Byte value = 10;

        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);

        verify(mockJsonGenerator).writeNumber(value.intValue());
    }

    @Test
    public void testSerializeShort() throws IOException {
        Short value = 10;

        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);

        verify(mockJsonGenerator).writeNumber(value.intValue());
    }

    @Test
    public void testSerializeFallback() throws IOException {
        Number value = 10;

        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);

        verify(mockJsonGenerator).writeNumber(value.toString());
    }

    @Test
    public void testGetSchemaIsInt() {
        SerializerProvider mockSerializerProvider = mock(SerializerProvider.class);
        JavaType mockJavaType = mock(JavaType.class);

        numberSerializer.getSchema(mockSerializerProvider, mockJavaType);

        verify(mockSerializerProvider).createSchemaNode("integer", true);
    }

    @Test
    public void testGetSchemaIsNotInt() {
        SerializerProvider mockSerializerProvider = mock(SerializerProvider.class);
        JavaType mockJavaType = mock(JavaType.class);

        NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);

        numberSerializer.getSchema(mockSerializerProvider, mockJavaType);

        verify(mockSerializerProvider).createSchemaNode("number", true);
    }

    @Test
    public void testAcceptJsonFormatVisitorIsInt() throws JsonMappingException {
        JsonFormatVisitorWrapper mockJsonFormatVisitorWrapper = mock(JsonFormatVisitorWrapper.class);
        JavaType mockJavaType = mock(JavaType.class);

        numberSerializer.acceptJsonFormatVisitor(mockJsonFormatVisitorWrapper, mockJavaType);

        verify(mockJsonFormatVisitorWrapper).visitIntFormat(numberSerializer, mockJavaType, JsonParser.NumberType.BIG_INTEGER);
    }

    @Test
    public void testAcceptJsonFormatVisitorIsBigDecimal() throws JsonMappingException {
        JsonFormatVisitorWrapper mockJsonFormatVisitorWrapper = mock(JsonFormatVisitorWrapper.class);
        JavaType mockJavaType = mock(JavaType.class);

        NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);

        numberSerializer.acceptJsonFormatVisitor(mockJsonFormatVisitorWrapper, mockJavaType);

        verify(mockJsonFormatVisitorWrapper).visitFloatFormat(numberSerializer, mockJavaType, JsonParser.NumberType.BIG_INTEGER);
    }

    @Test
    public void testAcceptJsonFormatVisitorIsOther() throws JsonMappingException {
        JsonFormatVisitorWrapper mockJsonFormatVisitorWrapper = mock(JsonFormatVisitorWrapper.class);
        JavaType mockJavaType = mock(JavaType.class);

        NumberSerializer numberSerializer = new NumberSerializer(Float.class);

        numberSerializer.acceptJsonFormatVisitor(mockJsonFormatVisitorWrapper, mockJavaType);

        verify(mockJsonFormatVisitorWrapper).expectNumberFormat(mockJavaType);
    }
}