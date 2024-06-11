import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

    private ObjectMapper mapper;
    private String[] testArray;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        testArray = new String[]{"test1", "test2", "test3"};
    }

    @Test
    public void testDeserialize() throws IOException {
        String json = "[\"test1\", \"test2\", \"test3\"]";

        JsonParser jp = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String[] result = StringArrayDeserializer.instance.deserialize(jp, ctxt);
        assertArrayEquals(testArray, result);
    }

    @Test
    public void testDeserializeCustom() throws IOException {
        String json = "[\"test1\", \"test2\", \"test3\"]";

        JsonParser jp = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        JsonDeserializer<String> deser = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return "custom";
            }
        };

        StringArrayDeserializer deserializer = new StringArrayDeserializer(deser);
        String[] result = deserializer._deserializeCustom(jp, ctxt);

        String[] expected = new String[]{"custom", "custom", "custom"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        String json = "[\"test1\", \"test2\", \"test3\"]";

        JsonParser jp = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        JsonDeserializer<String> valueDeser = mapper.findContextualValueDeserializer(mapper.constructType(String.class), null);
        assertEquals("test1", valueDeser.deserialize(jp, ctxt));
        assertEquals("test2", valueDeser.deserialize(jp, ctxt));
        assertEquals("test3", valueDeser.deserialize(jp, ctxt));
    }

    @Test
    public void testHandleNonArray() throws IOException {
        String json = "\"test\"";

        JsonParser jp = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        String[] result = StringArrayDeserializer.instance.handleNonArray(jp, ctxt);
        String[] expected = new String[]{"test"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testCreateContextual() throws IOException {
        JsonDeserializer<?> deser = mapper.findContextualValueDeserializer(mapper.constructType(String.class), null);

        StringArrayDeserializer original = new StringArrayDeserializer();
        JsonDeserializer<?> result = original.createContextual(mapper.getDeserializationContext(), null);
        assertNull(result);

        StringArrayDeserializer custom = new StringArrayDeserializer(deser);
        result = original.createContextual(mapper.getDeserializationContext(), null);
        assertEquals(custom, result);
    }
}