import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        try {
            assertNull(deserializer.deserialize(parser, context));
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testDeserializeWithType_StartArray() {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        when(parser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY);

        try {
            assertNotNull(deserializer.deserializeWithType(parser, context, typeDeserializer));
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testDeserializeWithType_StartObject() {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        when(parser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT);
        
        try {
            assertNotNull(deserializer.deserializeWithType(parser, context, typeDeserializer));
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testDeserializeWithType_FieldName() {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        when(parser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_FIELD_NAME);
        
        try {
            assertNotNull(deserializer.deserializeWithType(parser, context, typeDeserializer));
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testDeserializeWithType_OtherToken() {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        
        when(parser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_TRUE);

        try {
            assertNull(deserializer.deserializeWithType(parser, context, typeDeserializer));
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
}