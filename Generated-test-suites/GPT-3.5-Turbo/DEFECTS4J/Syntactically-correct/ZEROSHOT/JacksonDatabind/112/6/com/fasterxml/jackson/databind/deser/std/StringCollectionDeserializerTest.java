import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class StringCollectionDeserializerTest {

    private StringCollectionDeserializer deserializer;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;
    private String value1;
    private String value2;
    private String value3;

    @Before
    public void setUp() {
        deserializer = new StringCollectionDeserializer(null, null, null);
        jsonParser = mock(JsonParser.class);
        deserializationContext = mock(DeserializationContext.class);
        value1 = "value1";
        value2 = "value2";
        value3 = "value3";
    }

    @Test
    public void testDeserialize() throws IOException {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextTextValue()).thenReturn(value1, value2, null, value3, null);
        Collection<String> result = deserializer.deserialize(jsonParser, deserializationContext);
        assertEquals(3, result.size());
        assertTrue(result.contains(value1));
        assertTrue(result.contains(value2));
        assertTrue(result.contains(value3));
    }

    @Test
    public void testDeserializeUsingCustom() throws IOException {
        JsonDeserializer<String> valueDeserializer = mock(JsonDeserializer.class);
        deserializer = new StringCollectionDeserializer(null, null, null, valueDeserializer, null, null);
        when(jsonParser.nextTextValue()).thenReturn(null);
        when(valueDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(value1, value2, value3);
        Collection<String> result = deserializer.deserialize(jsonParser, deserializationContext, null);
        assertEquals(3, result.size());
        assertTrue(result.contains(value1));
        assertTrue(result.contains(value2));
        assertTrue(result.contains(value3));
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.deserializeTypedFromArray(jsonParser, deserializationContext)).thenReturn(new Object());
        Object result = deserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        assertNotNull(result);
    }

    @Test
    public void testHandleNonArrayCanWrap() throws IOException {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        deserializer = new StringCollectionDeserializer(null, null, null, null, null, Boolean.TRUE);
        Collection<String> result = deserializer.handleNonArray(jsonParser, deserializationContext, null);
        assertEquals(1, result.size());
        assertTrue(result.contains(null));
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleNonArrayCannotWrap() throws IOException {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        deserializer = new StringCollectionDeserializer(null, null, null, null, null, Boolean.FALSE);
        deserializer.handleNonArray(jsonParser, deserializationContext, null);
    }
}