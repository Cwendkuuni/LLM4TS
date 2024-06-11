import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.junit.Before;
import org.junit.Test;

public class StringCollectionDeserializerTest {

    private StringCollectionDeserializer deserializer;
    private JsonParser parser;
    private DeserializationContext ctxt;

    @Before
    public void setup() {
        deserializer = new StringCollectionDeserializer(null, null, null);
        parser = mock(JsonParser.class);
        ctxt = mock(DeserializationContext.class);
    }

    @Test
    public void testDeserialize() throws IOException {
        List<String> result = new ArrayList<>();
        when(parser.isExpectedStartArrayToken()).thenReturn(true);
        when(parser.nextTextValue()).thenReturn("one", "two", "three", null);
        deserializer.deserialize(parser, ctxt, result);
        assertEquals(3, result.size());
        assertEquals("one", result.get(0));
        assertEquals("two", result.get(1));
        assertEquals("three", result.get(2));
    }

    @Test
    public void testHandleNonArray() throws IOException {
        List<String> result = new ArrayList<>();
        when(ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getText()).thenReturn("singleValue");
        deserializer.handleNonArray(parser, ctxt, result);
        assertEquals(1, result.size());
        assertEquals("singleValue", result.get(0));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeException() throws IOException {
        List<String> result = new ArrayList<>();
        when(parser.isExpectedStartArrayToken()).thenReturn(true);
        when(parser.nextTextValue()).thenThrow(new IOException());
        deserializer.deserialize(parser, ctxt, result);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        Object result = deserializer.deserializeWithType(parser, ctxt, typeDeserializer);
        assertEquals(JsonToken.VALUE_STRING, result);
    }

    @Test
    public void testCreateContextual() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        JsonDeserializer<Object> delegate = mock(JsonDeserializer.class);
        JsonDeserializer<String> valueDeser = mock(JsonDeserializer.class);
        NullValueProvider nuller = mock(NullValueProvider.class);
        when(ctxt.findContextualValueDeserializer(any(JavaType.class), any(BeanProperty.class))).thenReturn(valueDeser);
        deserializer.createContextual(ctxt, property);
        assertNotNull(deserializer.withResolved(delegate, valueDeser, nuller, true));
    }

}