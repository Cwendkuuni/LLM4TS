import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        Object result = deserializer.deserialize(parser, ctxt);

        assertNull(result);
        Mockito.verify(parser, Mockito.times(1)).skipChildren();
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JsonToken token = JsonToken.VALUE_STRING;
        Mockito.when(parser.getCurrentTokenId()).thenReturn(token.id());

        Object result = deserializer.deserializeWithType(parser, ctxt, null);

        assertNull(result);
    }
}