import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class StringCollectionDeserializerTest {
    
    @Mock
    private JsonDeserializer<String> valueDeserializer;
    
    @Mock
    private ValueInstantiator valueInstantiator;
    
    @Mock
    private JsonDeserializer<Object> delegateDeserializer;
    
    @Mock
    private JsonParser jsonParser;
    
    @Mock
    private DeserializationContext deserializationContext;
    
    @Mock
    private NullValueProvider nullValueProvider;
    
    private Collection<String> result;
    
    private StringCollectionDeserializer deserializer;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        deserializer = new StringCollectionDeserializer(null, valueDeserializer, valueInstantiator);
        
        result = new ArrayList<>();
    }
    
    @Test
    public void testDeserialize() throws IOException {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY, JsonToken.VALUE_STRING, JsonToken.VALUE_NULL, JsonToken.END_ARRAY);
        when(jsonParser.nextTextValue()).thenReturn("foo", null);
        when(deserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(nullValueProvider.getNullValue(deserializationContext)).thenReturn(null);
        
        Collection<String> deserialized = deserializer.deserialize(jsonParser, deserializationContext, result);
        
        assertEquals(deserialized, result);
        verify(jsonParser, times(4)).nextTextValue();
        verify(deserializationContext, times(1)).handleUnexpectedToken(any(Class.class), any(JsonParser.class));
        verify(nullValueProvider, times(1)).getNullValue(deserializationContext);
    }
    
    @Test
    public void testDeserializeWithCustomDeserializer() throws IOException {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY, JsonToken.END_ARRAY);
        when(jsonParser.nextTextValue()).thenReturn("foo");
        when(valueDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("foo");
        
        deserializer = deserializer.withResolved(delegateDeserializer, valueDeserializer, nullValueProvider, false);
        
        Collection<String> deserialized = deserializer.deserialize(jsonParser, deserializationContext, result);
        
        assertEquals(deserialized, result);
        verify(jsonParser, times(2)).nextTextValue();
        verify(valueDeserializer, times(1)).deserialize(jsonParser, deserializationContext);
    }
    
    @Test
    public void testDeserializeWithType() throws IOException {
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        
        Object deserialized = deserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        
        assertNull(deserialized);
        verify(typeDeserializer, times(1)).deserializeTypedFromArray(jsonParser, deserializationContext);
    }
    
    @Test
    public void testIsCacheable() {
        assertFalse(deserializer.isCachable());
    }
    
    @Test
    public void testGetContentDeserializer() {
        assertSame(valueDeserializer, deserializer.getContentDeserializer());
    }
    
    @Test
    public void testGetValueInstantiator() {
        assertSame(valueInstantiator, deserializer.getValueInstantiator());
    }
    
    @Test
    public void testCreateContextual() throws JsonMappingException {
        JsonProperty property = mock(JsonProperty.class);
        JavaType collectionType = mock(JavaType.class);
        
        when(valueInstantiator.getDelegateCreator()).thenReturn(null);
        when(collectionType.getContentType()).thenReturn(mock(JavaType.class));

        JsonDeserializer<?> contextualDeserializer = deserializer.createContextual(deserializationContext, property);
        
        assertSame(contextualDeserializer, deserializer);
        verify(valueDeserializer, never()).deserialize(jsonParser, deserializationContext);
        verify(jsonParser, never()).nextTextValue();
        verify(deserializationContext, never()).handleUnexpectedToken(any(Class.class), any(JsonParser.class));
        verify(nullValueProvider, never()).getNullValue(deserializationContext);
    }
}