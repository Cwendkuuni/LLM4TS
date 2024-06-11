import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FromStringDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        FromStringDeserializer<String> fromStringDeserializer = new FromStringDeserializer<String>(String.class);
        String expected = "test string";

        when(jsonParser.getValueAsString()).thenReturn(expected);
        when(deserializationContext.findValueSerializer(String.class)).thenReturn(fromStringDeserializer);

        String actual = fromStringDeserializer.deserialize(jsonParser, deserializationContext);

        assertEquals(expected, actual);
    }
}