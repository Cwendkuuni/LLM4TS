
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StringCollectionDeserializerTest {

    private StringCollectionDeserializer deserializer;

    @Before
    public void setUp() {
        deserializer = new StringCollectionDeserializer(null, null, null);
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Collection<String> result = new ArrayList<>();

        when(parser.isExpectedStartArrayToken()).thenReturn(true);
        when(parser.nextTextValue()).thenReturn("Value1", "Value2", null);
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL, JsonToken.END_ARRAY);

        deserializer.deserialize(parser, context, result);

        assertEquals(2, result.size());
        assertEquals("Value1", result.get(0));
        assertEquals("Value2", result.get(1));
    }

    @Test
    public void testDeserializeUsingCustom() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Collection<String> result = new ArrayList<>();
        JsonDeserializer<String> valueDeserializer = mock(JsonDeserializer.class);

        when(parser.nextTextValue()).thenReturn("Value1", "Value2", null);
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL, JsonToken.END_ARRAY);
        when(valueDeserializer.deserialize(parser, context)).thenReturn("Value3");

        deserializer.deserializeUsingCustom(parser, context, result, valueDeserializer);

        assertEquals(2, result.size());
        assertEquals("Value3", result.get(0));
        assertEquals("Value3", result.get(1));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeError() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Collection<String> result = new ArrayList<>();

        when(parser.isExpectedStartArrayToken()).thenReturn(true);
        when(parser.nextTextValue()).thenThrow(new IOException());

        deserializer.deserialize(parser, context, result);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        when(typeDeserializer.deserializeTypedFromArray(parser, context)).thenReturn(new ArrayList<>());

        Object result = deserializer.deserializeWithType(parser, context, typeDeserializer);

        assertEquals(ArrayList.class, result.getClass());

    }
}
