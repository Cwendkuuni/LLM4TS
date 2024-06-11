import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ContextualDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.BeanProperty;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

public class StringArrayDeserializerTest {

    private StringArrayDeserializer deserializer;

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private TypeDeserializer typeDeserializer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        deserializer = new StringArrayDeserializer();
    }

    @Test
    public void testDeserialize() throws IOException {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING).thenReturn(JsonToken.END_ARRAY);
        when(jsonParser.getText()).thenReturn("testValue");

        String[] result = deserializer.deserialize(jsonParser, deserializationContext);

        assertEquals(1, result.length);
        assertEquals("testValue", result[0]);
    }

    @Test
    public void testDeserializeCustom() throws IOException {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING).thenReturn(JsonToken.END_ARRAY);
        when(jsonParser.getText()).thenReturn("testValue");

        ObjectBuffer objectBuffer = mock(ObjectBuffer.class);
        when(deserializationContext.leaseObjectBuffer()).thenReturn(objectBuffer);
        when(objectBuffer.resetAndStart()).thenReturn(new Object[1]);
        when(objectBuffer.appendCompletedChunk(any())).thenReturn(new Object[1]);
        when(objectBuffer.completeAndClearBuffer(any(), anyInt(), any())).thenReturn(new String[]{"testValue"});
        when(deserializationContext.returnObjectBuffer(objectBuffer)).thenReturn(null);

        String[] result = deserializer._deserializeCustom(jsonParser, deserializationContext);

        assertEquals(1, result.length);
        assertEquals("testValue", result[0]);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        Object result = deserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);

        assertNull(result);
    }

    @Test
    public void testHandleNonArray() throws IOException {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        String[] result = deserializer.handleNonArray(jsonParser, deserializationContext);

        assertEquals(1, result.length);
        assertNull(result[0]);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        BeanProperty beanProperty = mock(BeanProperty.class);
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        when(deserializationContext.findContextualValueDeserializer(any(), any())).thenReturn(valueDeserializer);
        when(deserializationContext.handleSecondaryContextualization(valueDeserializer, beanProperty)).thenReturn(valueDeserializer);
        when(deserializationContext.constructType(String.class)).thenReturn(null);
        when(deserializer.isDefaultDeserializer(valueDeserializer)).thenReturn(false);

        JsonDeserializer<?> result = deserializer.createContextual(deserializationContext, beanProperty);

        assertEquals(deserializer, result);
    }
}