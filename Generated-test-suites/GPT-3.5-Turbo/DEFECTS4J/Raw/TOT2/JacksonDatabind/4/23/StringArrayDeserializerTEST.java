import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;

@RunWith(MockitoJUnitRunner.class)
public class StringArrayDeserializerTest {

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    private StringArrayDeserializer stringArrayDeserializer;

    @Before
    public void setup() {
        stringArrayDeserializer = new StringArrayDeserializer();
    }

    @Test
    public void testDeserializeArrayWithStrings() throws IOException {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken())
                .thenReturn(JsonToken.VALUE_STRING)
                .thenReturn(JsonToken.VALUE_STRING)
                .thenReturn(JsonToken.END_ARRAY);

        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);

        assertArrayEquals(new String[]{"value1", "value2"}, result);
    }

    @Test
    public void testDeserializeArrayWithNulls() throws IOException {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken())
                .thenReturn(JsonToken.VALUE_NULL)
                .thenReturn(JsonToken.VALUE_NULL)
                .thenReturn(JsonToken.END_ARRAY);

        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);

        assertArrayEquals(new String[]{null, null}, result);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        Object result = stringArrayDeserializer.deserializeWithType(jsonParser, deserializationContext, null);

        assertNull(result);
    }

    @Test
    public void testHandleNonArrayWhenSingleValueAccepted() throws IOException {
        when(deserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(deserializationContext.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)).thenReturn(true);
        when(jsonParser.getText()).thenReturn("");

        String[] result = stringArrayDeserializer.handleNonArray(jsonParser, deserializationContext);

        assertArrayEquals(new String[]{null}, result);
    }

    @Test(expected = IOException.class)
    public void testHandleNonArrayWhenSingleValueNotAccepted() throws IOException {
        when(deserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(false);

        stringArrayDeserializer.handleNonArray(jsonParser, deserializationContext);
    }

    @Test
    public void testContextual() throws IOException, JsonMappingException {
        JsonDeserializer<?> deser = stringArrayDeserializer.createContextual(deserializationContext, null);

        assertNull(deser);
    }
}