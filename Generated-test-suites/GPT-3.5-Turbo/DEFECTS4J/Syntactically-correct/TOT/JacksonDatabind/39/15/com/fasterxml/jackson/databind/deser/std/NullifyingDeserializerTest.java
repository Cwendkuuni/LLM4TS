import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize_skipChildren() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        NullifyingDeserializer nd = new NullifyingDeserializer();

        try {
            Object result = nd.deserialize(p, ctxt);
            assertNull(result);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testDeserializeWithType_startArray() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        NullifyingDeserializer nd = new NullifyingDeserializer();

        try {
            p.setCurrentTokenId(JsonTokenId.ID_START_ARRAY);
            Object result = nd.deserializeWithType(p, ctxt, typeDeserializer);
            assertNull(result);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testDeserializeWithType_startObject() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        NullifyingDeserializer nd = new NullifyingDeserializer();

        try {
            p.setCurrentTokenId(JsonTokenId.ID_START_OBJECT);
            Object result = nd.deserializeWithType(p, ctxt, typeDeserializer);
            assertNull(result);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testDeserializeWithType_fieldName() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        NullifyingDeserializer nd = new NullifyingDeserializer();

        try {
            p.setCurrentTokenId(JsonTokenId.ID_FIELD_NAME);
            Object result = nd.deserializeWithType(p, ctxt, typeDeserializer);
            assertNull(result);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testDeserializeWithType_default() {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        NullifyingDeserializer nd = new NullifyingDeserializer();

        try {
            p.setCurrentTokenId(JsonTokenId.ID_UNKNOWN);
            Object result = nd.deserializeWithType(p, ctxt, typeDeserializer);
            assertNull(result);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

}