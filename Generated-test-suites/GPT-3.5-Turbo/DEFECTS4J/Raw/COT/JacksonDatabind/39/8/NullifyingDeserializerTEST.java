import static org.junit.Assert.*;
import org.junit.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        JsonParser parser = new JsonParser(null);
        DeserializationContext context = new DeserializationContext(null, null);
        try {
            Object result = deserializer.deserialize(parser, context);
            assertNull(result);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testDeserializeWithType() {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        JsonParser parser = new JsonParser(null);
        DeserializationContext context = new DeserializationContext(null, null);
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        try {
            Object result = deserializer.deserializeWithType(parser, context, typeDeserializer);
            assertNull(result);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

}