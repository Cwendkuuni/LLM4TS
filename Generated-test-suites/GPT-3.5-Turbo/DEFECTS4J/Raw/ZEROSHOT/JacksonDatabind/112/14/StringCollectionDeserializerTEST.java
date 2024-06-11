import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;

public class StringCollectionDeserializerTest {
    
    @Test
    public void testDeserialize() throws IOException {
        // Given
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null);
        Collection<String> result = new ArrayList<String>();
        
        when(parser.isExpectedStartArrayToken()).thenReturn(true);
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(parser.nextTextValue()).thenReturn("value1", "value2", null);
        
        // When
        Collection<String> deserialized = deserializer.deserialize(parser, context, result);
        
        // Then
        assertEquals(2, deserialized.size());
        assertEquals("value1", deserialized.get(0));
        assertEquals("value2", deserialized.get(1));
    }
    
    @Test
    public void testDeserializeWithCustomDeserializer() throws IOException {
        // Given
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        JsonDeserializer<String> valueDeserializer = mock(JsonDeserializer.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null);
        Collection<String> result = new ArrayList<String>();
        
        when(parser.isExpectedStartArrayToken()).thenReturn(true);
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(parser.nextTextValue()).thenReturn("value1", "value2", null);
        when(valueDeserializer.deserialize(parser, context)).thenReturn("customValue");
        
        deserializer = deserializer.withResolved(null, valueDeserializer, null, null);
        
        // When
        Collection<String> deserialized = deserializer.deserialize(parser, context, result);
        
        // Then
        assertEquals(2, deserialized.size());
        assertEquals("customValue", deserialized.get(0));
        assertEquals("customValue", deserialized.get(1));
    }
    
    @Test
    public void testHandleNonArrayWhenCannotWrap() throws IOException {
        // Given
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null);
        Collection<String> result = new ArrayList<String>();
        
        when(parser.isExpectedStartArrayToken()).thenReturn(false);
        
        // When
        Collection<String> deserialized = deserializer.handleNonArray(parser, context, result);
        
        // Then
        assertEquals(result, deserialized);
        verify(context).handleUnexpectedToken(Mockito.eq(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY), 
                                              Mockito.eq(_containerType.getRawClass()), 
                                              Mockito.eq(parser));
    }
    
    @Test
    public void testHandleNonArrayWhenCanWrap() throws IOException {
        // Given
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        JsonDeserializer<String> valueDeserializer = mock(JsonDeserializer.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null);
        Collection<String> result = new ArrayList<String>();
        
        when(parser.isExpectedStartArrayToken()).thenReturn(false);
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.nextTextValue()).thenReturn("singleValue");
        when(valueDeserializer.deserialize(parser, context)).thenReturn("customValue");
        
        deserializer = deserializer.withResolved(null, valueDeserializer, null, Boolean.TRUE);
        
        // When
        Collection<String> deserialized = deserializer.handleNonArray(parser, context, result);
        
        // Then
        assertEquals(1, deserialized.size());
        assertEquals("customValue", deserialized.get(0));
    }
}