import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

@RunWith(MockitoJUnitRunner.class)
public class NullifyingDeserializerTest {

    @Mock
    JsonParser mockJsonParser;

    @Mock
    DeserializationContext mockDeserializationContext;

    @Mock
    TypeDeserializer mockTypeDeserializer;

    @Test
    public void testDeserialize_SkipChildren_ReturnsNull() throws IOException {
        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        
        Object result = nullifyingDeserializer.deserialize(mockJsonParser, mockDeserializationContext);
        
        assertNull(result);
        verify(mockJsonParser, times(1)).skipChildren();
    }

    @Test
    public void testDeserializeWithType_ArrayOrObject_ReturnsTypedObject() throws IOException {
        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        
        when(mockJsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT);
        
        Object result = nullifyingDeserializer.deserializeWithType(mockJsonParser, mockDeserializationContext, mockTypeDeserializer);
        
        assertNull(result);
        verify(mockTypeDeserializer, times(1)).deserializeTypedFromAny(mockJsonParser, mockDeserializationContext);
    }
}