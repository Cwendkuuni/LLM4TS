import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class NullifyingDeserializerTest {

    @Test
    public void deserialize_skipChildren() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("{}");
        DeserializationContext deserializationContext = new ObjectMapper().getDeserializationContext();

        NullifyingDeserializer nullifyingDeserializer = new NullifyingDeserializer();
        assertEquals(null, nullifyingDeserializer.deserialize(jsonParser, deserializationContext));
    }

    @Test
    public void deserializeWithType_startArray() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("[]");
        DeserializationContext deserializationContext = new ObjectMapper().getDeserializationContext();
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);

        NullifyingDeserializer nullifyingDeserializer = new NullifyingDeserializer();
        assertEquals(null, nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer));
    }

    @Test
    public void deserializeWithType_startObject() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("{}");
        DeserializationContext deserializationContext = new ObjectMapper().getDeserializationContext();
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);

        NullifyingDeserializer nullifyingDeserializer = new NullifyingDeserializer();
        assertEquals(null, nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer));
    }

    @Test
    public void deserializeWithType_fieldName() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("\"name\": \"value\"");
        DeserializationContext deserializationContext = new ObjectMapper().getDeserializationContext();
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);

        NullifyingDeserializer nullifyingDeserializer = new NullifyingDeserializer();
        assertEquals(null, nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer));
    }

    @Test
    public void deserializeWithType_defaultCase() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("123");
        DeserializationContext deserializationContext = new ObjectMapper().getDeserializationContext();
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);

        NullifyingDeserializer nullifyingDeserializer = new NullifyingDeserializer();
        assertEquals(null, nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer));
    }
}