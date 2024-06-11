import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class NullifyingDeserializerTest {

    private NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
    private JsonParser mockParser = Mockito.mock(JsonParser.class);
    private DeserializationContext mockContext = Mockito.mock(DeserializationContext.class);
    private TypeDeserializer mockTypeDeserializer = Mockito.mock(TypeDeserializer.class);

    @Test
    public void testDeserializeWithValidToken() throws Exception {
        assertNull(nullifyingDeserializer.deserializeWithType(mockParser, mockContext, mockTypeDeserializer));
    }

    @Test
    public void testDeserializeWithTypeWithStartArrayToken() throws Exception {
        Mockito.when(mockParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY);
        assertNull(nullifyingDeserializer.deserializeWithType(mockParser, mockContext, mockTypeDeserializer));
    }

    @Test
    public void testDeserializeWithTypeWithStartObjectToken() throws Exception {
        Mockito.when(mockParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT);
        assertNull(nullifyingDeserializer.deserializeWithType(mockParser, mockContext, mockTypeDeserializer));
    }

    @Test
    public void testDeserializeWithTypeWithFieldNameToken() throws Exception {
        Mockito.when(mockParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_FIELD_NAME);
        assertNull(nullifyingDeserializer.deserializeWithType(mockParser, mockContext, mockTypeDeserializer));
    }

    @Test
    public void testDeserializeWithTypeWithInvalidToken() throws Exception {
        assertNull(nullifyingDeserializer.deserializeWithType(mockParser, mockContext, mockTypeDeserializer));
    }
}