import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ValueDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;

public class StringCollectionDeserializerTest {
    
    private StringCollectionDeserializer deserializer;
    private JsonDeserializer<?> valueDeserializer;
    private DeserializationContext context;
    private JavaType collectionType;
    private ArrayList<String> result;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        collectionType = mock(JavaType.class);
        valueDeserializer = mock(JsonDeserializer.class);
        context = mock(DeserializationContext.class);
        result = new ArrayList<String>();
        deserializer = new StringCollectionDeserializer(collectionType, valueDeserializer, null);
    }
    
    @Test
    public void testDeserialize_NullParser_ReturnsEmptyCollection() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        when(parser.isExpectedStartArrayToken()).thenReturn(false);
        
        Collection<String> actual = deserializer.deserialize(parser, context);
        
        assertEquals(0, actual.size());
    }
    
    @Test
    public void testDeserialize_WithNullValueDeserializer_NullValueProvider_ReturnsCollectionWithNull() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        when(parser.isExpectedStartArrayToken()).thenReturn(true);
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        when(context.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        
        Collection<String> actual = deserializer.deserialize(parser, context);
        
        assertEquals(1, actual.size());
        assertNull(actual.iterator().next());
    }
    
    @Test
    public void testDeserialize_WithNonNullValueDeserializer_NullValueProvider_ReturnsCollectionWithValue() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        when(parser.isExpectedStartArrayToken()).thenReturn(true);
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.nextTextValue()).thenReturn("test");
        when(context.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        
        Collection<String> actual = deserializer.deserialize(parser, context);
        
        assertEquals(1, actual.size());
        assertEquals("test", actual.iterator().next());
    }
    
    @Test
    public void testDeserialize_WithNonNullValueDeserializer_ContextValueDeserializer_ReturnsCollectionWithValue() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        when(parser.isExpectedStartArrayToken()).thenReturn(true);
        when(parser.nextTextValue()).thenReturn("test");
        when(context.findContextualValueDeserializer(any(JavaType.class), any())).thenReturn(valueDeserializer);
        
        Collection<String> actual = deserializer.deserialize(parser, context);
        
        assertEquals(1, actual.size());
        assertEquals("test", actual.iterator().next());
    }
    
    @Test
    public void testDeserialize_WithNonNullValueDeserializer_CustomValueDeserializer_ReturnsCollectionWithValue() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        when(parser.isExpectedStartArrayToken()).thenReturn(true);
        when(parser.nextTextValue()).thenReturn("test");
        
        Collection<String> actual = deserializer.deserialize(parser, context);
        
        assertEquals(1, actual.size());
        assertEquals("test", actual.iterator().next());
    }
    
    @Test
    public void testDeserialize_WithDelegateDeserializer_ReturnsCollectionWithValueFromDelegate() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        when(parser.isExpectedStartArrayToken()).thenReturn(true);
        when(context.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        
        JsonDeserializer<Object> delegate = mock(JsonDeserializer.class);
        when(delegate.deserialize(parser, context)).thenReturn(new ArrayList<String>());
        
        deserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, null);
        Collection<String> actual = deserializer.deserialize(parser, context);
        
        assertNotNull(actual);
        assertEquals(0, actual.size());
    }
    
    @Test
    public void testDeserialize_WithCustomDeserializer_ReturnsCollectionWithValueFromCustomDeserializer() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        when(parser.isExpectedStartArrayToken()).thenReturn(true);
        
        JsonDeserializer<String> valueDeserializer = mock(JsonDeserializer.class);
        when(valueDeserializer.deserialize(parser, context)).thenReturn("test");
        
        deserializer = new StringCollectionDeserializer(collectionType, null, valueDeserializer, null, null, null);
        Collection<String> actual = deserializer.deserialize(parser, context);
        
        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals("test", actual.iterator().next());
    }
    
}