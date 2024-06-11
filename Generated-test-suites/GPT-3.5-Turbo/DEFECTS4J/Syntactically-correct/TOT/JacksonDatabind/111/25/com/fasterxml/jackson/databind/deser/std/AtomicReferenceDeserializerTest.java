
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;

public class AtomicReferenceDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetNullValue() throws IOException {
        String json = "null";
        AtomicReference<Object> expected = new AtomicReference<Object>();

        AtomicReference<Object> result = objectMapper.readValue(json, AtomicReference.class);

        assertEquals(expected, result);
    }

    @Test
    public void testGetEmptyValue() throws IOException {
        String json = "{}";
        AtomicReference<Object> expected = new AtomicReference<Object>();

        AtomicReference<Object> result = objectMapper.readValue(json, AtomicReference.class);

        assertEquals(expected, result);
    }

    @Test
    public void testReferenceValue() throws IOException {
        String json = "{\"contents\": \"example\"}";
        AtomicReference<Object> expected = new AtomicReference<Object>("example");

        AtomicReference<Object> result = objectMapper.readValue(json, AtomicReference.class);

        assertEquals(expected, result);
    }

    @Test
    public void testGetReferenced() {
        AtomicReference<Object> atomicReference = new AtomicReference<>("example");
        Object expected = "example";

        Object result = atomicReference.get();

        assertEquals(expected, result);
    }

    @Test
    public void testUpdateReference() {
        AtomicReference<Object> atomicReference = new AtomicReference<>();
        Object expected = "example";

        atomicReference.updateReference("example");
        Object result = atomicReference.get();

        assertEquals(expected, result);
    }

    @Test
    public void testSupportsUpdate() {
        Boolean expected = true;

        Boolean result = objectMapper.isEnabled(DeserializationConfig.Feature.FEATURE_UPDATE_REFERENCE);

        assertEquals(expected, result);
    }
}
