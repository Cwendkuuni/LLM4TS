import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StringCollectionDeserializerTest {

    private StringCollectionDeserializer stringCollectionDeserializer;

    @Test
    public void testDeserialize_shouldReturnEmptyCollection_whenJsonIsEmptyArray() throws IOException {
        String json = "[]";
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.getCurrentToken()).thenReturn(null);

        Collection<String> result = stringCollectionDeserializer.deserialize(jsonParser, deserializationContext);
        List<String> expected = new ArrayList<>();

        assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_shouldReturnCollectionOfStrings_whenJsonContainsStrings() throws IOException {
        String json = "[\"string1\", \"string2\", \"string3\"]";
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.getCurrentToken()).thenReturn(null);
        when(jsonParser.nextTextValue()).thenReturn("string1", "string2", "string3", null);

        Collection<String> result = stringCollectionDeserializer.deserialize(jsonParser, deserializationContext);
        List<String> expected = Arrays.asList("string1", "string2", "string3");

        assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_shouldIgnoreNullValues_whenSkipNullValuesEnabled() throws IOException {
        String json = "[\"string1\", null, \"string2\", null, \"string3\"]";
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.getCurrentToken()).thenReturn(null);
        when(jsonParser.nextTextValue()).thenReturn("string1", null, "string2", null, "string3", null);

        Collection<String> result = stringCollectionDeserializer.deserialize(jsonParser, deserializationContext);
        List<String> expected = Arrays.asList("string1", "string2", "string3");

        assertEquals(expected, result);
    }

    @Test(expected = JsonParseException.class)
    public void testDeserialize_shouldThrowJsonParseException_whenInvalidJson() throws IOException {
        String json = "[\"string1\", \"string2\", \"string3\"";
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.getCurrentToken()).thenReturn(null);

        stringCollectionDeserializer.deserialize(jsonParser, deserializationContext);
    }

    @Test
    public void testDeserializeWithType_shouldDeserializeArrayWithTypes() throws IOException {
        String json = "[\"string1\", \"string2\", \"string3\"]";
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.getCurrentToken()).thenReturn(null);

        Object result = stringCollectionDeserializer.deserializeWithType(jsonParser, deserializationContext, null);

        assertEquals(Arrays.asList("string1", "string2", "string3"), result);
    }

    @Test
    public void testIsCachable_shouldReturnFalse_whenCustomDeserializersViaAnnotationsAreInvolved() {
        boolean result = stringCollectionDeserializer.isCachable();

        assertEquals(false, result);
    }
}