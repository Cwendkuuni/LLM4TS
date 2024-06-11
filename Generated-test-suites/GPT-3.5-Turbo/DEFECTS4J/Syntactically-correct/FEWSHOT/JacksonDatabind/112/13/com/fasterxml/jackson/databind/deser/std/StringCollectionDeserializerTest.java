
package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class StringCollectionDeserializerTest {
    
    @Test
    public void testDeserialize() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(JavaType.construct(Collection.class), new StringDeserializer(), null);
        
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Collection<String> result = new ArrayList<>();
        
        Mockito.when(parser.isExpectedStartArrayToken()).thenReturn(true);
        Mockito.when(parser.nextTextValue()).thenReturn("value1").thenReturn("value2").thenReturn(null);
        Mockito.when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL).thenReturn(JsonToken.END_ARRAY);
        Mockito.when(context.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        Mockito.when(parser.readValueAs(String.class)).thenReturn("value3");
        
        deserializer.deserialize(parser, context, result);
        
        assertEquals("value1", result.get(0));
        assertEquals("value2", result.get(1));
        assertEquals("value3", result.get(2));
        
        Mockito.verify(parser, times(3)).nextTextValue();
        Mockito.verify(parser, times(2)).getCurrentToken();
        Mockito.verify(parser, times(1)).readValueAs(String.class);
    }
    
    @Test
    public void testDeserializeWithType() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(JavaType.construct(Collection.class), new StringDeserializer(), null);
        
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);
        
        Mockito.when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(typeDeserializer.deserializeTypedFromArray(parser, context)).thenReturn("value");
        
        Object result = deserializer.deserializeWithType(parser, context, typeDeserializer);
        
        assertEquals("value", result);
        
        Mockito.verify(parser, times(1)).getCurrentToken();
        Mockito.verify(typeDeserializer, times(1)).deserializeTypedFromArray(parser, context);
    }
}
