import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        assertNull(deserializer.deserialize(parser, context));
    }

    @Test
    public void testDeserializeWithType_Array() throws IOException {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        parser.nextToken(JsonToken.START_ARRAY);
        assertNull(deserializer.deserializeWithType(parser, context, typeDeserializer));
    }

    @Test
    public void testDeserializeWithType_Object() throws IOException {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        parser.nextToken(JsonToken.START_OBJECT);
        assertNull(deserializer.deserializeWithType(parser, context, typeDeserializer));
    }

    @Test
    public void testDeserializeWithType_FieldName() throws IOException {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        parser.nextToken(JsonToken.FIELD_NAME);
        assertNull(deserializer.deserializeWithType(parser, context, typeDeserializer));
    }
}