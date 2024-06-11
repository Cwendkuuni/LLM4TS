import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;

@RunWith(PowerMockRunner.class)
@PrepareForTest({NullifyingDeserializer.class})

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize_SkipChildren_ReturnsNull() {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        assertNull(deserializer.deserialize(null, null));
    }

    @Test
    public void testDeserializeWithType_SkipChildren_ReturnsNull() {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        assertNull(deserializer.deserializeWithType(null, null, null));
    }

    @Test
    public void testDeserializeWithType_StartArray_ReturnsNull() {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        assertNull(deserializer.deserializeWithType(JsonToken.START_ARRAY, null, null));
    }

    @Test
    public void testDeserializeWithType_StartObject_ReturnsNull() {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        assertNull(deserializer.deserializeWithType(JsonToken.START_OBJECT, null, null));
    }

    @Test
    public void testDeserializeWithType_FieldName_ReturnsNull() {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        assertNull(deserializer.deserializeWithType(JsonToken.FIELD_NAME, null, null));
    }
}