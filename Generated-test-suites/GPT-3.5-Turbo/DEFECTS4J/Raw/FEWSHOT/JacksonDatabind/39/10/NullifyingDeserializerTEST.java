import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class NullifyingDeserializerTest {
    @Test
    public void testDeserialize() {
        // Test skipping all content and returning null
        assertNull(NullifyingDeserializer.instance.deserialize(null, null));
    }

    @Test
    public void testDeserializeWithType() {
        // Test deserializing with type information
        assertNull(NullifyingDeserializer.instance.deserializeWithType(null, null, null));
        // Test deserializing different token ids
        // Start array
        assertNull(NullifyingDeserializer.instance.deserializeWithType(JsonTokenId.ID_START_ARRAY, null, null));
        // Start object
        assertNull(NullifyingDeserializer.instance.deserializeWithType(JsonTokenId.ID_START_OBJECT, null, null));
        // Field name
        assertNull(NullifyingDeserializer.instance.deserializeWithType(JsonTokenId.ID_FIELD_NAME, null, null));
    }
}