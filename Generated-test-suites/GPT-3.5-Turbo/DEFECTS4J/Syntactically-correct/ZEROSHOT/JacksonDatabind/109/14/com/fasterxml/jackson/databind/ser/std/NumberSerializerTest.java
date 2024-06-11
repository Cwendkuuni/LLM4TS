import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

import org.junit.Before;
import org.junit.Test;

public class NumberSerializerTest {

    private NumberSerializer numberSerializer;

    @Before
    public void setUp() {
        numberSerializer = new NumberSerializer(Number.class);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider providerMock = mock(SerializerProvider.class);
        BeanProperty propertyMock = mock(BeanProperty.class);

        JsonSerializer<?> result = numberSerializer.createContextual(providerMock, propertyMock);

        assertEquals(numberSerializer, result);
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        Number valueMock = mock(BigDecimal.class);
        JsonGenerator gMock = mock(JsonGenerator.class);
        SerializerProvider providerMock = mock(SerializerProvider.class);

        numberSerializer.serialize(valueMock, gMock, providerMock);

        verify(gMock).writeNumber((BigDecimal) valueMock);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        Number valueMock = mock(BigInteger.class);
        JsonGenerator gMock = mock(JsonGenerator.class);
        SerializerProvider providerMock = mock(SerializerProvider.class);

        numberSerializer.serialize(valueMock, gMock, providerMock);

        verify(gMock).writeNumber((BigInteger) valueMock);
    }

    @Test
    public void testSerializeLong() throws IOException {
        Number valueMock = mock(Long.class);
        JsonGenerator gMock = mock(JsonGenerator.class);
        SerializerProvider providerMock = mock(SerializerProvider.class);

        numberSerializer.serialize(valueMock, gMock, providerMock);

        verify(gMock).writeNumber(valueMock.longValue());
    }

    @Test
    public void testSerializeDouble() throws IOException {
        Number valueMock = mock(Double.class);
        JsonGenerator gMock = mock(JsonGenerator.class);
        SerializerProvider providerMock = mock(SerializerProvider.class);

        numberSerializer.serialize(valueMock, gMock, providerMock);

        verify(gMock).writeNumber(valueMock.doubleValue());
    }

    @Test
    public void testSerializeFloat() throws IOException {
        Number valueMock = mock(Float.class);
        JsonGenerator gMock = mock(JsonGenerator.class);
        SerializerProvider providerMock = mock(SerializerProvider.class);

        numberSerializer.serialize(valueMock, gMock, providerMock);

        verify(gMock).writeNumber(valueMock.floatValue());
    }

    @Test
    public void testSerializeInteger() throws IOException {
        Number valueMock = mock(Integer.class);
        JsonGenerator gMock = mock(JsonGenerator.class);
        SerializerProvider providerMock = mock(SerializerProvider.class);

        numberSerializer.serialize(valueMock, gMock, providerMock);

        verify(gMock).writeNumber(valueMock.intValue());
    }

    @Test
    public void testSerializeByte() throws IOException {
        Number valueMock = mock(Byte.class);
        JsonGenerator gMock = mock(JsonGenerator.class);
        SerializerProvider providerMock = mock(SerializerProvider.class);

        numberSerializer.serialize(valueMock, gMock, providerMock);

        verify(gMock).writeNumber(valueMock.intValue());
    }

    @Test
    public void testSerializeShort() throws IOException {
        Number valueMock = mock(Short.class);
        JsonGenerator gMock = mock(JsonGenerator.class);
        SerializerProvider providerMock = mock(SerializerProvider.class);

        numberSerializer.serialize(valueMock, gMock, providerMock);

        verify(gMock).writeNumber(valueMock.intValue());
    }

    @Test
    public void testSerializeOther() throws IOException {
        String valueMock = "test";
        JsonGenerator gMock = mock(JsonGenerator.class);
        SerializerProvider providerMock = mock(SerializerProvider.class);

        numberSerializer.serialize(valueMock, gMock, providerMock);

        verify(gMock).writeNumber(valueMock.toString());
    }

    @Test
    public void testGetSchema() {
        SerializerProvider providerMock = mock(SerializerProvider.class);
        Type typeHintMock = mock(Type.class);

        JsonNode result = numberSerializer.getSchema(providerMock, typeHintMock);

        assertEquals(numberSerializer.createSchemaNode("number", true), result);
    }

    @Test
    public void testAcceptJsonFormatVisitorForInt() throws JsonMappingException {
        JsonFormatVisitorWrapper visitorMock = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHintMock = mock(JavaType.class);

        ((NumberSerializer) numberSerializer).acceptJsonFormatVisitor(visitorMock, typeHintMock);

        verify(visitorMock).expectNumberFormat(typeHintMock);
    }

    @Test
    public void testAcceptJsonFormatVisitorForBigDecimal() throws JsonMappingException {
        JsonFormatVisitorWrapper visitorMock = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHintMock = mock(JavaType.class);
        Class<?> handledTypeMock = BigDecimal.class;

        // Update handledType to BigDecimal
        ((NumberSerializer) numberSerializer).handledTypeOverride(handledTypeMock);

        ((NumberSerializer) numberSerializer).acceptJsonFormatVisitor(visitorMock, typeHintMock);

        verify(visitorMock).expectNumberFormat(typeHintMock);
    }

    // Add more tests for remaining methods if needed

}