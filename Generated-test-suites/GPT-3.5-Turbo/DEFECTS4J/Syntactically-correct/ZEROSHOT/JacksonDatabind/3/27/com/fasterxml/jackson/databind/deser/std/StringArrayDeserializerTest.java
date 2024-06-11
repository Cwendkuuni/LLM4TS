import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        JsonParser jp = null;
        DeserializationContext ctxt = null;
        
        // Test case 1: jp is null, ctxt is null
        assertThrows(IOException.class, () -> {
            String[] result = deserializer.deserialize(jp, ctxt);
        });

        // Test case 2: jp is null, ctxt is not null
        jp = null;
        ctxt = new DeserializationContext(null, null, null);
        assertThrows(IOException.class, () -> {
            String[] result = deserializer.deserialize(jp, ctxt);
        });

        // Test case 3: jp is not null, ctxt is null
        jp = new JsonParser(null);
        ctxt = null;
        assertThrows(IOException.class, () -> {
            String[] result = deserializer.deserialize(jp, ctxt);
        });

        // Test case 4: jp is not null, ctxt is not null
        jp = new JsonParser(null);
        ctxt = new DeserializationContext(null, null, null);
        assertThrows(IOException.class, () -> {
            String[] result = deserializer.deserialize(jp, ctxt);
        });

    }

    @Test
    public void testDeserializeWithType() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        JsonParser jp = null;
        DeserializationContext ctxt = null;
        TypeDeserializer typeDeserializer = null;

        // Test case 1: jp is null, ctxt is null, typeDeserializer is null
        assertThrows(IOException.class, () -> {
            Object result = deserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        });

        // Test case 2: jp is null, ctxt is not null, typeDeserializer is null
        jp = null;
        ctxt = new DeserializationContext(null, null, null);
        assertThrows(IOException.class, () -> {
            Object result = deserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        });

        // Test case 3: jp is not null, ctxt is null, typeDeserializer is null
        jp = new JsonParser(null);
        ctxt = null;
        assertThrows(IOException.class, () -> {
            Object result = deserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        });

        // Test case 4: jp is not null, ctxt is not null, typeDeserializer is null
        jp = new JsonParser(null);
        ctxt = new DeserializationContext(null, null, null);
        assertThrows(IOException.class, () -> {
            Object result = deserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        });

    }

    @Test
    public void testHandleNonArray() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        JsonParser jp = null;
        DeserializationContext ctxt = null;

        // Test case 1: jp is null, ctxt is null
        assertThrows(IOException.class, () -> {
            String[] result = deserializer.handleNonArray(jp, ctxt);
        });

        // Test case 2: jp is null, ctxt is not null
        jp = null;
        ctxt = new DeserializationContext(null, null, null);
        assertThrows(IOException.class, () -> {
            String[] result = deserializer.handleNonArray(jp, ctxt);
        });

        // Test case 3: jp is not null, ctxt is null
        jp = new JsonParser(null);
        ctxt = null;
        assertThrows(IOException.class, () -> {
            String[] result = deserializer.handleNonArray(jp, ctxt);
        });

        // Test case 4: jp is not null, ctxt is not null
        jp = new JsonParser(null);
        ctxt = new DeserializationContext(null, null, null);
        assertThrows(IOException.class, () -> {
            String[] result = deserializer.handleNonArray(jp, ctxt);
        });

    }

    @Test
    public void testCreateContextual() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        DeserializationContext ctxt = null;
        BeanProperty property = null;

        // Test case 1: ctxt is null, property is null
        JsonDeserializer<?> result1 = deserializer.createContextual(ctxt, property);
        assertEquals(deserializer, result1);

        // Test case 2: ctxt is null, property is not null
        ctxt = null;
        property = new BeanProperty();
        JsonDeserializer<?> result2 = deserializer.createContextual(ctxt, property);
        assertEquals(deserializer, result2);

        // Test case 3: ctxt is not null, property is null
        ctxt = new DeserializationContext(null, null, null);
        property = null;
        JsonDeserializer<?> result3 = deserializer.createContextual(ctxt, property);
        assertEquals(deserializer, result3);

        // Test case 4: ctxt is not null, property is not null
        ctxt = new DeserializationContext(null, null, null);
        property = new BeanProperty();
        JsonDeserializer<?> result4 = deserializer.createContextual(ctxt, property);
        assertEquals(deserializer, result4);

    }

    private void assertThrows(Class<? extends Throwable> expectedException, Runnable block) {
        try {
            block.run();
            fail("Expected exception not thrown");
        } catch (Throwable throwable) {
            if (!expectedException.isInstance(throwable)) {
                fail("Expected exception " + expectedException + " but got " + throwable.getClass());
            }
        }
    }
}