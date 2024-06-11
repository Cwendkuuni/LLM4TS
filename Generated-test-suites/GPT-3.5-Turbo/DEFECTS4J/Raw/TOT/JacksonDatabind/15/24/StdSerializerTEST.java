
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class StdSerializerTest {

    @Test
    public void testSerialize() throws IOException {
        StdSerializer<String> serializer = new TestStringSerializer();
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new SerializerProvider();
        serializer.serialize("test", jsonGenerator, provider);
        jsonGenerator.flush();
        assertEquals("\"test\"", writer.toString());
    }

    @Test
    public void testGetSchema() throws IOException {
        StdSerializer<String> serializer = new TestStringSerializer();
        SerializerProvider provider = new SerializerProvider();
        assertEquals("{\"type\":\"string\"}", serializer.getSchema(provider, null).toString());
    }

    @Test
    public void testGetSchemaWithOptional() throws IOException {
        StdSerializer<String> serializer = new TestStringSerializer();
        SerializerProvider provider = new SerializerProvider();
        assertEquals("{\"type\":\"string\",\"required\":false}", serializer.getSchema(provider, null, false).toString());
    }

    @Test
    public void testFindConvertingContentSerializer() throws IOException {
        StdSerializer<String> serializer = new TestStringSerializer();
        SerializerProvider provider = new SerializerProvider();
        BeanProperty prop = new BeanProperty() {
            @Override
            public String getName() {
                return "test";
            }

            @Override
            public AnnotatedMember getMember() {
                return null;
            }

            @Override
            public Class<?> getMemberClass() {
                return null;
            }
        };
        assertEquals(null, serializer.findConvertingContentSerializer(provider, prop, null));
    }

    @Test
    public void testFindPropertyFilter() throws IOException {
        StdSerializer<String> serializer = new TestStringSerializer();
        SerializerProvider provider = new SerializerProvider();
        Object filterId = "test";
        Object valueToFilter = "value";
        assertEquals(null, serializer.findPropertyFilter(provider, filterId, valueToFilter));
    }

    private class TestStringSerializer extends StdSerializer<String> {
        public TestStringSerializer() {
            super(String.class);
        }

        @Override
        public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeString(value);
        }
    }
}
