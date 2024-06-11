import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.util.Date;

public class StdKeySerializerTest {
    @Test
    public void testSerialize_Date() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        Date date = new Date();

        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(stringWriter);
        SerializerProvider serializerProvider = new StandardSerializerProvider();

        serializer.serialize(date, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String result = stringWriter.toString();
        String expected = ISO_8601_DATE_FORMAT.format(date);

        assertEquals(expected, result);
    }

    @Test
    public void testSerialize_String() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        String value = "test";

        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(stringWriter);
        SerializerProvider serializerProvider = new StandardSerializerProvider();

        serializer.serialize(value, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String result = stringWriter.toString();
        String expected = "test";

        assertEquals(expected, result);
    }

    @Test(expected = IOException.class)
    public void testSerialize_Null() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        Object value = null;

        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(stringWriter);
        SerializerProvider serializerProvider = new StandardSerializerProvider();

        serializer.serialize(value, jsonGenerator, serializerProvider);
    }

    @Test(expected = IOException.class)
    public void testSerialize_EmptyString() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        String value = "";

        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(stringWriter);
        SerializerProvider serializerProvider = new StandardSerializerProvider();

        serializer.serialize(value, jsonGenerator, serializerProvider);
    }

    @Test(expected = IOException.class)
    public void testSerialize_InvalidDate() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        Object value = new Object() {
            @Override
            public long getTime() {
                return -1;
            }
        };

        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(stringWriter);
        SerializerProvider serializerProvider = new StandardSerializerProvider();

        serializer.serialize(value, jsonGenerator, serializerProvider);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerialize_JsonMappingException() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        Object value = new Object();

        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());

        try {
            serializer.serialize(value, jsonGenerator, new StandardSerializerProvider());
        } catch (IOException e) {
            throw new JsonMappingException("Unexpected IOException during serialization", e);
        }
    }
}