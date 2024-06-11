
package com.fasterxml.jackson.databind.deser.std;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collection;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StringCollectionDeserializerTest {
    private StringCollectionDeserializer deserializer;
    private JsonDeserializer<String> valueDeserializer;
    private ValueInstantiator valueInstantiator;
    private JsonDeserializer<Object> delegateDeserializer;
    private JavaType collectionType;

    @Before
    public void setup(){
        collectionType = mock(JavaType.class);
        valueDeserializer = mock(JsonDeserializer.class);
        valueInstantiator = mock(ValueInstantiator.class);
        delegateDeserializer = mock(JsonDeserializer.class);
        deserializer = new StringCollectionDeserializer(collectionType, valueDeserializer, valueInstantiator);
    }

    @Test
    public void testDeserialize() throws Exception {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Collection<String> result = new ArrayList<>();
        result.add("test1");
        result.add("test2");
        
        when(parser.isExpectedStartArrayToken()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING).thenReturn(JsonToken.VALUE_STRING).thenReturn(JsonToken.END_ARRAY);
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL).thenReturn(JsonToken.END_ARRAY);
        when(parser.nextTextValue()).thenReturn("test1").thenReturn("test2").thenReturn(null);
        when(valueDeserializer.deserialize(parser, ctxt)).thenReturn("test1").thenReturn("test2");
        when(valueInstantiator.createUsingDelegate(ctxt, delegateDeserializer.deserialize(parser, ctxt))).thenReturn(result);
        when(valueInstantiator.createUsingDefault(ctxt)).thenReturn(result);
        when(valueDeserializer.deserialize(parser, ctxt)).thenReturn("test1").thenReturn("test2");
        when(valueDeserializer.deserialize(parser, ctxt)).thenReturn("test1").thenReturn("test2");
        when(valueDeserializer.deserialize(parser, ctxt)).thenReturn("test1").thenReturn("test2");

        Collection<String> actual = deserializer.deserialize(parser, ctxt, result);
        assertEquals(result, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithError() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Collection<String> result = new ArrayList<>();
        
        when(parser.isExpectedStartArrayToken()).thenReturn(true);
        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.nextTextValue()).thenReturn("test1");
        when(valueDeserializer.deserialize(parser, ctxt)).thenThrow(new IOException());

        deserializer.deserialize(parser, ctxt, result);
    }
}
