import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.core.JsonParser;
import java.util.Collection;
import com.fasterxml.jackson.core.JsonToken;

@RunWith(MockitoJUnitRunner.class)
public class StringCollectionDeserializerTest {

    @Mock
    private JsonParser jsonParser;
    
    @Mock
    private DeserializationContext deserializationContext;

    @Test
    public void testDeserializeWithDelegateDeserializer() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer();
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        Collection<String> result = deserializer.deserialize(jsonParser, deserializationContext);
        
        assertNotNull(result);
    }

    @Test
    public void testDeserializeUsingCustom() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer();
        Collection<String> result = deserializer.deserializeUsingCustom(jsonParser, deserializationContext, null, new CustomDeserializer());
        
        assertNotNull(result);
    }

    @Test
    public void testHandleNonArray() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer();
        Collection<String> result = deserializer.handleNonArray(jsonParser, deserializationContext, null);
        
        assertNotNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeException() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer();
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        doThrow(new JsonMappingException("Test Exception")).when(jsonParser).nextTextValue();
        
        deserializer.deserialize(jsonParser, deserializationContext);
    }

    class CustomDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) {
            return "Custom String";
        }
    }
}