import static org.junit.Assert.*;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

public class StringArrayDeserializerTest {

    private StringArrayDeserializer deserializer;

    @Before
    public void setUp() {
        deserializer = new StringArrayDeserializer();
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String[] expected = null;
        String[] result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializeCustom() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String[] expected = null;
        String[] result = deserializer._deserializeCustom(jp, ctxt);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        TypeDeserializer typeDeserializer = null;
        Object expected = null;
        Object result = deserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        assertEquals(expected, result);
    }

    @Test
    public void testHandleNonArray() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String[] expected = null;
        String[] result = deserializer.handleNonArray(jp, ctxt);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testCreateContextual() throws IOException {
        DeserializationContext ctxt = null;
        BeanProperty property = null;
        JsonDeserializer<?> expected = null;
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);
        assertEquals(expected, result);
    }

}