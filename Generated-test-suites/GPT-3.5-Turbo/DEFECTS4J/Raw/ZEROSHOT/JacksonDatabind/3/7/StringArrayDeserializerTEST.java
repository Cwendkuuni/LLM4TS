import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;

public class StringArrayDeserializerTest {

    private StringArrayDeserializer stringArrayDeserializer;

    @Before
    public void setUp() {
        stringArrayDeserializer = new StringArrayDeserializer();
    }

    @Test
    public void testDeserialize() throws Exception {
        String[] expected = new String[]{"value1", "value2", "value3"};
        String json = "[\"value1\", \"value2\", \"value3\"]";
        JsonParser jp = new JsonFactory().createParser(json);
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        String[] result = stringArrayDeserializer.deserialize(jp, ctxt);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        String[] expected = new String[]{"value1", "value2", "value3"};
        String json = "[\"value1\", \"value2\", \"value3\"]";
        JsonParser jp = new JsonFactory().createParser(json);
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        TypeDeserializer typeDeserializer = new ObjectMapper().getTypeFactory().constructType(String[].class).getTypeDeserializer();
        Object result = stringArrayDeserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        assertArrayEquals(expected, (String[]) result);
    }

    @Test
    public void testHandleNonArray() throws Exception {
        String[] expected = new String[]{"value"};
        String json = "\"value\"";
        JsonParser jp = new JsonFactory().createParser(json);
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        String[] result = stringArrayDeserializer.handleNonArray(jp, ctxt);
        assertArrayEquals(expected, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleNonArrayWithRejectSingleValue() throws Exception {
        String json = "\"value\"";
        JsonParser jp = new JsonFactory().createParser(json);
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        ctxt.disable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        stringArrayDeserializer.handleNonArray(jp, ctxt);
    }

    @Test
    public void testHandleNonArrayWithEmptyStringAsNullObject() throws Exception {
        String[] expected = null;
        String json = "\"\"";
        JsonParser jp = new JsonFactory().createParser(json);
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        ctxt.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        String[] result = stringArrayDeserializer.handleNonArray(jp, ctxt);
        assertArrayEquals(expected, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeCustom() throws Exception {
        String json = "[\"value1\", \"value2\", \"value3\"]";
        JsonParser jp = new JsonFactory().createParser(json);
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        stringArrayDeserializer.deserializeCustom(jp, ctxt);
    }

    @Test
    public void testCreateContextual() throws Exception {
        StringArrayDeserializer result = (StringArrayDeserializer) stringArrayDeserializer.createContextual(new DeserializationContext(null, null), null);
        assertNotNull(result);
    }
}