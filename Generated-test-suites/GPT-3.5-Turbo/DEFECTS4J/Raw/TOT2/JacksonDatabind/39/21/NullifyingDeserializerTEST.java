import static org.junit.Assert.*;

import org.junit.Test;

public class NullifyingDeserializerTest {

    NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;

    @Test
    public void testDeserialize_SkipsChildren() {
        // create a dummy JsonParser for testing
        JsonParser dummyParser = new DummyJsonParser();
        
        assertNull(nullifyingDeserializer.deserialize(dummyParser, null));
    }

    @Test
    public void testDeserializeWithType_DeserializeTypedFromAnyForArray() {
        // create a dummy JsonParser for testing
        JsonParser dummyParser = new DummyJsonParser(JsonTokenId.ID_START_ARRAY);

        assertNull(nullifyingDeserializer.deserializeWithType(dummyParser, null, null));
    }

    @Test
    public void testDeserializeWithType_DeserializeTypedFromAnyForObject() {
        // create a dummy JsonParser for testing
        JsonParser dummyParser = new DummyJsonParser(JsonTokenId.ID_START_OBJECT);

        assertNull(nullifyingDeserializer.deserializeWithType(dummyParser, null, null));
    }

    @Test
    public void testDeserializeWithType_DeserializeTypedFromAnyForFieldName() {
        // create a dummy JsonParser for testing
        JsonParser dummyParser = new DummyJsonParser(JsonTokenId.ID_FIELD_NAME);

        assertNull(nullifyingDeserializer.deserializeWithType(dummyParser, null, null));
    }

    @Test
    public void testDeserializeWithType_DefaultCase() {
        // create a dummy JsonParser for testing
        JsonParser dummyParser = new DummyJsonParser(JsonTokenId.ID_STRING);

        assertNull(nullifyingDeserializer.deserializeWithType(dummyParser, null, null));
    }
}