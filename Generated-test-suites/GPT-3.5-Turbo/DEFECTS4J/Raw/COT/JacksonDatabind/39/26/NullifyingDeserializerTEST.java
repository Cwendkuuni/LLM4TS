import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import java.io.IOException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.core.JsonTokenId;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        DeserializationContext ctxt = null;
        JsonParser parser = null;
        
        // Basic test case
        Object result = deserializer.deserialize(parser, ctxt);
        assertEquals(null, result);
        
        // Edge case: parser and ctxt are null
        assertThatThrownBy(() -> {
            deserializer.deserialize(null, null);
        }).isInstanceOf(IOException.class).hasMessage("parser and ctxt cannot be null");
        
        // Edge case: parser is null
        assertThatThrownBy(() -> {
            deserializer.deserialize(null, ctxt);
        }).isInstanceOf(IOException.class).hasMessage("parser cannot be null");
        
        // Edge case: ctxt is null
        assertThatThrownBy(() -> {
            deserializer.deserialize(parser, null);
        }).isInstanceOf(IOException.class).hasMessage("ctxt cannot be null");
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        DeserializationContext ctxt = null;
        JsonParser parser = null;
        TypeDeserializer typeDeserializer = null;
        
        // Basic test case
        Object result = deserializer.deserializeWithType(parser, ctxt, typeDeserializer);
        assertEquals(null, result);
        
        // Edge case: parser, ctxt, and typeDeserializer are null
        assertThatThrownBy(() -> {
            deserializer.deserializeWithType(null, null, null);
        }).isInstanceOf(IOException.class).hasMessage("parser, ctxt, and typeDeserializer cannot be null");
        
        // Edge case: parser is null
        assertThatThrownBy(() -> {
            deserializer.deserializeWithType(null, ctxt, typeDeserializer);
        }).isInstanceOf(IOException.class).hasMessage("parser cannot be null");
        
        // Edge case: ctxt is null
        assertThatThrownBy(() -> {
            deserializer.deserializeWithType(parser, null, typeDeserializer);
        }).isInstanceOf(IOException.class).hasMessage("ctxt cannot be null");
        
        // Edge case: typeDeserializer is null
        assertThatThrownBy(() -> {
            deserializer.deserializeWithType(parser, ctxt, null);
        }).isInstanceOf(IOException.class).hasMessage("typeDeserializer cannot be null");
    }
}