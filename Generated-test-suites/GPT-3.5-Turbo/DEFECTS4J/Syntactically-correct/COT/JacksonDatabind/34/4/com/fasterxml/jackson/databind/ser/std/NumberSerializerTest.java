
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class NumberSerializerTest {

    @Test
    public void testSerialize_BigDecimal() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        BigDecimal value = new BigDecimal("10.5");

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        jsonGenerator.setCodec(mock(ObjectWriter.class));
        jsonGenerator.setOutputStream(byteArrayOutputStream);

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        assertEquals("10.5", new String(byteArrayOutputStream.toByteArray()));
    }

    @Test
    public void testSerialize_BigInteger() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        BigInteger value = new BigInteger("10");

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        jsonGenerator.setCodec(mock(ObjectWriter.class));
        jsonGenerator.setOutputStream(byteArrayOutputStream);

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        assertEquals("10", new String(byteArrayOutputStream.toByteArray()));
    }

    @Test
    public void testSerialize_Integer() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Integer.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        Integer value = 10;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        jsonGenerator.setCodec(mock(ObjectWriter.class));
        jsonGenerator.setOutputStream(byteArrayOutputStream);

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        assertEquals("10", new String(byteArrayOutputStream.toByteArray()));
    }

    @Test
    public void testSerialize_Long() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Long.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        Long value = 10L;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        jsonGenerator.setCodec(mock(ObjectWriter.class));
        jsonGenerator.setOutputStream(byteArrayOutputStream);

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        assertEquals("10", new String(byteArrayOutputStream.toByteArray()));
    }

    @Test
    public void testSerialize_Double() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Double.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        Double value = 10.5;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        jsonGenerator.setCodec(mock(ObjectWriter.class));
        jsonGenerator.setOutputStream(byteArrayOutputStream);

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        assertEquals("10.5", new String(byteArrayOutputStream.toByteArray()));
    }

    @Test
    public void testSerialize_Float() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Float.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        Float value = 10.5f;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        jsonGenerator.setCodec(mock(ObjectWriter.class));
        jsonGenerator.setOutputStream(byteArrayOutputStream);

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        assertEquals("10.5", new String(byteArrayOutputStream.toByteArray()));
    }

    @Test
    public void testSerialize_Byte() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Byte.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        Byte value = (byte) 10;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        jsonGenerator.setCodec(mock(ObjectWriter.class));
        jsonGenerator.setOutputStream(byteArrayOutputStream);

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        assertEquals("10", new String(byteArrayOutputStream.toByteArray()));
    }

    @Test
    public void testSerialize_Short() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Short.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        Short value = (short) 10;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        jsonGenerator.setCodec(mock(ObjectWriter.class));
        jsonGenerator.setOutputStream(byteArrayOutputStream);

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        assertEquals("10", new String(byteArrayOutputStream.toByteArray()));
    }

    @Test
    public void testSerialize_otherTypes() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        Number value = 10;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        jsonGenerator.setCodec(mock(ObjectWriter.class));
        jsonGenerator.setOutputStream(byteArrayOutputStream);

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        assertEquals("10", new String(byteArrayOutputStream.toByteArray()));
    }

    @Test
    public void testGetSchema() {
        NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        Type typeHint = mock(Type.class);

        JsonNode schema = numberSerializer.getSchema(serializerProvider, typeHint);

        assertEquals("integer", schema.get("type").asText());
        assertEquals(true, schema.get("required").asBoolean());
    }

}
