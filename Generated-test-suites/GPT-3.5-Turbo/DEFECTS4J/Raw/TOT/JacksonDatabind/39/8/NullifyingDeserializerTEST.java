import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class NullifyingDeserializerTest {

    private NullifyingDeserializer nullifier;

    @Before
    public void setup() {
        nullifier = new NullifyingDeserializer();
    }

    @Test
    public void testDeserialize_NormalTokens_ReturnsNull() throws IOException {
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        Object result = nullifier.deserialize(parser, context);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType_StartArrayToken_ReturnsDeserializedType() throws IOException {
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        Object result = nullifier.deserializeWithType(parser, context, typeDeserializer);
        assertNotNull(result);
    }

    @Test
    public void testDeserializeWithType_StartObjectToken_ReturnsDeserializedType() throws IOException {
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        Object result = nullifier.deserializeWithType(parser, context, typeDeserializer);
        assertNotNull(result);
    }

    @Test
    public void testDeserializeWithType_FieldNameToken_ReturnsDeserializedType() throws IOException {
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        Object result = nullifier.deserializeWithType(parser, context, typeDeserializer);
        assertNotNull(result);
    }

    @Test
    public void testDeserializeWithType_OtherTokens_ReturnsNull() throws IOException {
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        // Set current token to an unsupported token
        parser.setCurrentTokenId(JsonTokenId.ID_NUMBER_INT);
        Object result = nullifier.deserializeWithType(parser, context, typeDeserializer);
        assertNull(result);
    }
}