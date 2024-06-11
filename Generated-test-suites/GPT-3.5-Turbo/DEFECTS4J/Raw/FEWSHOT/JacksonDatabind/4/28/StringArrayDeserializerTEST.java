import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class StringArrayDeserializerTest {
    
    @Mock
    private JsonParser jsonParser;
    
    @Mock
    private DeserializationContext deserializationContext;
    
    @Mock
    private ObjectBuffer objectBuffer;
    
    @Mock
    private JsonDeserializer<String> elementDeserializer;
    
    private StringArrayDeserializer stringArrayDeserializer;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        stringArrayDeserializer = new StringArrayDeserializer(elementDeserializer);
    }
    
    @Test
    public void testDeserialize() throws Exception {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(jsonParser.getText()).thenReturn("test");
        when(objectBuffer.resetAndStart()).thenReturn(new Object[1]);
        when(objectBuffer.appendCompletedChunk(any())).thenReturn(new Object[1]);
        when(objectBuffer.completeAndClearBuffer(any(), anyInt(), any(Class.class))).thenReturn(new String[]{"test"});
        when(deserializationContext.leaseObjectBuffer()).thenReturn(objectBuffer);
        
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        
        assertEquals(1, result.length);
        assertEquals("test", result[0]);
        
        verify(jsonParser, times(2)).isExpectedStartArrayToken();
        verify(jsonParser, times(2)).nextToken();
        verify(jsonParser, times(1)).getText();
        verify(objectBuffer, times(1)).resetAndStart();
        verify(objectBuffer, times(1)).appendCompletedChunk(any());
        verify(objectBuffer, times(1)).completeAndClearBuffer(any(), anyInt(), any(Class.class));
        verify(deserializationContext, times(1)).leaseObjectBuffer();
    }
    
    @Test
    public void testDeserialize_CustomElementDeserializer() throws Exception {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(jsonParser.getText()).thenReturn("test");
        when(objectBuffer.resetAndStart()).thenReturn(new Object[1]);
        when(objectBuffer.appendCompletedChunk(any())).thenReturn(new Object[1]);
        when(objectBuffer.completeAndClearBuffer(any(), anyInt(), any(Class.class))).thenReturn(new String[]{"test"});
        when(deserializationContext.leaseObjectBuffer()).thenReturn(objectBuffer);
        
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        
        assertEquals(1, result.length);
        assertEquals("test", result[0]);
        
        verify(jsonParser, times(2)).isExpectedStartArrayToken();
        verify(jsonParser, times(2)).nextToken();
        verify(jsonParser, times(1)).getText();
        verify(objectBuffer, times(1)).resetAndStart();
        verify(objectBuffer, times(1)).appendCompletedChunk(any());
        verify(objectBuffer, times(1)).completeAndClearBuffer(any(), anyInt(), any(Class.class));
        verify(deserializationContext, times(1)).leaseObjectBuffer();
    }
    
    @Test
    public void testDeserializeWithType() throws Exception {
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        
        Object result = stringArrayDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        
        assertNull(result);
    }
    
    @Test
    public void testHandleNonArray_AcceptSingleValueAsArrayEnabled() throws Exception {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        when(deserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        
        String[] result = stringArrayDeserializer.handleNonArray(jsonParser, deserializationContext);
        
        assertEquals(1, result.length);
        assertNull(result[0]);
        
        verify(jsonParser, times(1)).getCurrentToken();
        verify(deserializationContext, times(1)).isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    }
    
    @Test(expected = JsonMappingException.class)
    public void testHandleNonArray_AcceptSingleValueAsArrayDisabled() throws Exception {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        when(deserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(false);
        
        stringArrayDeserializer.handleNonArray(jsonParser, deserializationContext);
    }
    
    @Test
    public void testCreateContextual() throws Exception {
        BeanProperty beanProperty = mock(BeanProperty.class);
        JsonDeserializer<?> contextualValueDeserializer = mock(JsonDeserializer.class);
        
        when(elementDeserializer.getNullValue()).thenReturn(null);
        when(contextualValueDeserializer.deserialize(eq(jsonParser), eq(deserializationContext))).thenReturn("test");
        when(contextualValueDeserializer.getNullValue()).thenReturn(null);
        when(beanProperty.getMember()).thenReturn(null);
        when(deserializationContext.isEnabled(any())).thenReturn(true);
        when(deserializationContext.constructType(eq(String.class))).thenReturn(mock(JavaType.class));
        when(deserializationContext.handleSecondaryContextualization(eq(contextualValueDeserializer), eq(beanProperty))).thenReturn(contextualValueDeserializer);
        
        JsonDeserializer<?> result = stringArrayDeserializer.createContextual(deserializationContext, beanProperty);
        
        assertNull(result);
        
        verify(elementDeserializer, times(1)).getNullValue();
        verify(contextualValueDeserializer, times(1)).deserialize(eq(jsonParser), eq(deserializationContext));
        verify(contextualValueDeserializer, times(1)).getNullValue();
        verify(beanProperty, times(1)).getMember();
        verify(deserializationContext, times(1)).isEnabled(any());
        verify(deserializationContext, times(1)).constructType(eq(String.class));
        verify(deserializationContext, times(1)).handleSecondaryContextualization(eq(contextualValueDeserializer), eq(beanProperty));
    }
}