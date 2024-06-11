import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class NullifyingDeserializerTest {

    private NullifyingDeserializer nullifyingDeserializer;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;
    private TypeDeserializer typeDeserializer;
    
    @Before
    public void setup() {
        nullifyingDeserializer = NullifyingDeserializer.instance;
        jsonParser = mock(JsonParser.class);
        deserializationContext = mock(DeserializationContext.class);
        typeDeserializer = mock(TypeDeserializer.class);
    }
    
    @Test
    public void testDeserialize_WhenJsonParserIsEmpty_ShouldReturnNull() throws IOException {
        assertNull(nullifyingDeserializer.deserialize(jsonParser, deserializationContext));
    }
    
    @Test
    public void testDeserialize_WhenJsonParserSkipsChildren_ShouldReturnNull() throws IOException {
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT);
        
        assertNull(nullifyingDeserializer.deserialize(jsonParser, deserializationContext));
        
        verify(jsonParser, times(1)).skipChildren();
    }
    
    @Test
    public void testDeserializeWithType_WhenJsonParserIsArray_ShouldReturnObjectFromTypeDeserializer() throws IOException {
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY);
        when(typeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext)).thenReturn(new Object());
        
        Object result = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        
        assertEquals(Object.class, result.getClass());
        verify(typeDeserializer, times(1)).deserializeTypedFromAny(jsonParser, deserializationContext);
    }
    
    @Test
    public void testDeserializeWithType_WhenJsonParserIsObject_ShouldReturnObjectFromTypeDeserializer() throws IOException {
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT);
        when(typeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext)).thenReturn(new Object());
        
        Object result = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        
        assertEquals(Object.class, result.getClass());
        verify(typeDeserializer, times(1)).deserializeTypedFromAny(jsonParser, deserializationContext);
    }
    
    @Test
    public void testDeserializeWithType_WhenJsonParserIsFieldName_ShouldReturnObjectFromTypeDeserializer() throws IOException {
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_FIELD_NAME);
        when(typeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext)).thenReturn(new Object());
        
        Object result = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        
        assertEquals(Object.class, result.getClass());
        verify(typeDeserializer, times(1)).deserializeTypedFromAny(jsonParser, deserializationContext);
    }
    
    @Test
    public void testDeserializeWithType_WhenJsonParserIsOtherToken_ShouldReturnNull() throws IOException {
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_END_OBJECT);
        
        assertNull(nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer));
        
        verifyZeroInteractions(typeDeserializer);
    }
  
}