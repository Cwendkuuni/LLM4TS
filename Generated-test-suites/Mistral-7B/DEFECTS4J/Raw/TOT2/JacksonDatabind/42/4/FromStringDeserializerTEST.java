import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.junit.Test;

public class FromStringDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        Class<String> stringClass = String.class;
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        FromStringDeserializer<String> fromStringDeserializer = new FromStringDeserializer<String>(stringClass);

        when(jsonParser.getValueAsString()).thenReturn("Test String");
        when(deserializationContext.findValueSerializer(stringClass)).thenReturn(fromStringDeserializer);

        String result = fromStringDeserializer.deserialize(jsonParser, deserializationContext);

        assertEquals("Test String", result);
    }
}