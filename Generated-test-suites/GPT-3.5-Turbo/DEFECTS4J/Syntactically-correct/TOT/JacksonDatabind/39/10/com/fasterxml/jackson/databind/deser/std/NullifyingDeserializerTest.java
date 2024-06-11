import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;

public class NullifyingDeserializerTest {

    // Testing deserialize() method
    @Test
    public void testDeserialize() throws Exception {
        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        JsonParser jsonParser = new JsonParser(null, null);
        DeserializationContext deserializationContext = new DeserializationContext(null);
        
        // Test when JsonParser contains FIELD_NAME
        JsonTokenId tokenId = JsonTokenId.ID_FIELD_NAME;
        jsonParser.setCurrentTokenId(tokenId);
        Object result = nullifyingDeserializer.deserialize(jsonParser, deserializationContext);
        assertNull(result);
        
        // Test when JsonParser contains non-FIELD_NAME tokens
        tokenId = JsonTokenId.ID_STRING;
        jsonParser.setCurrentTokenId(tokenId);
        result = nullifyingDeserializer.deserialize(jsonParser, deserializationContext);
        assertNull(result);
    }

    // Testing deserializeWithType() method
    @Test
    public void testDeserializeWithType() throws Exception {
        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        JsonParser jsonParser = new JsonParser(null, null);
        DeserializationContext deserializationContext = new DeserializationContext(null);
        TypeDeserializer typeDeserializer = new TypeDeserializer(null, null, null);
        
        // Test when JsonParser contains START_ARRAY token
        JsonTokenId tokenId = JsonTokenId.ID_START_ARRAY;
        jsonParser.setCurrentTokenId(tokenId);
        Object result = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        assertNull(result);
        
        // Test when JsonParser contains START_OBJECT token
        tokenId = JsonTokenId.ID_START_OBJECT;
        jsonParser.setCurrentTokenId(tokenId);
        result = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        assertNull(result);
        
        // Test when JsonParser contains FIELD_NAME token
        tokenId = JsonTokenId.ID_FIELD_NAME;
        jsonParser.setCurrentTokenId(tokenId);
        result = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        assertNotNull(result);
        
        // Test when JsonParser contains non-START_ARRAY, non-START_OBJECT, non-FIELD_NAME tokens
        tokenId = JsonTokenId.ID_STRING;
        jsonParser.setCurrentTokenId(tokenId);
        result = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        assertNull(result);
    }
}