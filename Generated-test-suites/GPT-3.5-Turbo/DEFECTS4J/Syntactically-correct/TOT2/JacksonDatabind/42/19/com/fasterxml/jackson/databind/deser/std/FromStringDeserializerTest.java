import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;

public class FromStringDeserializerTest {

    @Mock
    JsonParser jsonParserMock;
    
    @Mock
    DeserializationContext deserializationContextMock;
    
    @Test
    public void testDeserialize_emptyString_returnsNull() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 0);
        assertNull(deserializer.deserialize(jsonParserMock, deserializationContextMock));
    }

    @Test
    public void testDeserialize_validString_returnsValue() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 0);
        String value = "testString";
        when(jsonParserMock.getValueAsString()).thenReturn(value);
        assertEquals(value, deserializer.deserialize(jsonParserMock, deserializationContextMock));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_invalidString_throwsIllegalArgumentException() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 0);
        String invalidValue = "invalidString";
        when(jsonParserMock.getValueAsString()).thenReturn(invalidValue);
        deserializer.deserialize(jsonParserMock, deserializationContextMock);
    }

    @Test
    public void testDeserializeFromEmptyString_URI_returnsEmptyURI() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, 3);
        assertEquals(URI.create(""), deserializer.deserialize(jsonParserMock, deserializationContextMock));
    }
}