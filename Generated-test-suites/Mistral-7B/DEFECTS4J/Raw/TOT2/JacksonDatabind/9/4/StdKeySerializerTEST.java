import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import java.io.*;
import java.util.Date;

public class StdKeySerializerTest {
    @Test
    public void testSerialize_Date() throws Exception {
        StdKeySerializer serializer = new StdKeySerializer();
        Date date = new Date();
        String expected = "\"2022-12-31T12:00:00.000Z\""; // current date formatted as ISO 8601 string

        StringWriter stringWriter = new StringWriter(new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream())));
        JsonGenerator jgen = new JsonFactory().createGenerator(stringWriter);
        SerializerProvider provider = new StandardSerializerProvider();
        serializer.serialize(date, jgen, provider);
        jgen.close();

        String actual = stringWriter.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testSerialize_String() throws Exception {
        StdKeySerializer serializer = new StdKeySerializer();
        String value = "test";

        StringWriter stringWriter = new StringWriter(new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream())));
        JsonGenerator jgen = new JsonFactory().createGenerator(stringWriter);
        SerializerProvider provider = new StandardSerializerProvider();
        serializer.serialize(value, jgen, provider);
        jgen.close();

        String actual = stringWriter.toString();
        assertEquals("\"test\"", actual);
    }

    @Test
    public void testSerialize_Null() throws Exception {
        StdKeySerializer serializer = new StdKeySerializer();
        StringWriter stringWriter = new StringWriter(new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream())));
        JsonGenerator jgen = new JsonFactory().createGenerator(stringWriter);
        SerializerProvider provider = new StandardSerializerProvider();

        serializer.serialize(null, jgen, provider);
    }

    @Test
    public void testSerialize_OtherTypes() throws Exception {
        StdKeySerializer serializer = new StdKeySerializer();
        int value = 42;

        StringWriter stringWriter = new StringWriter(new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream())));
        JsonGenerator jgen = new JsonFactory().createGenerator(stringWriter);
        SerializerProvider provider = new StandardSerializerProvider();

        serializer.serialize(value, jgen, provider);
        jgen.close();

        String actual = stringWriter.toString();
        assertEquals("\"42\"", actual);
    }

    @Test
    public void testGetSchema() throws Exception {
        StdKeySerializer serializer = new StdKeySerializer();
        Schema schema = serializer.getSchema(new StandardSerializerProvider(), Object.class);

        JsonNode expected = JsonNodeFactory.instance.textNode("string");
        JsonNode actual = schema.getDefaultValue();

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        StdKeySerializer serializer = new StdKeySerializer();

        JsonFactory jsonFactory = new JsonFactory();
        JsonFormatVisitorWrapper visitor = jsonFactory.getPolymorphicSerializerProvider().getSchemaProvider().findRootValueSerializer(String.class);

        serializer.acceptJsonFormatVisitor(visitor, Object.class);

        assertEquals(SerializationFeature.FAIL_ON_EMPTY_BEANS, visitor.getFeatureEnabled(SerializationFeature.FAIL_ON_EMPTY_BEANS));
        assertEquals(SerializationFeature.FAIL_ON_EMPTY_FIELDS, visitor.getFeatureEnabled(SerializationFeature.FAIL_ON_EMPTY_FIELDS));
    }
}