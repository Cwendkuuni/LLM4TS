package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;

public class StringArrayDeserializerTest {

    private StringArrayDeserializer stringArrayDeserializer;

    @Test
    public void testDeserialize() throws Exception {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        
        StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer();
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        
        assertNotNull(result);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        
        StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer();
        Object result = stringArrayDeserializer.deserializeWithType(jsonParser, deserializationContext, null);
        
        assertNotNull(result);
    }

    @Test
    public void testHandleNonArray() throws Exception {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        
        StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer();
        String[] result = stringArrayDeserializer.handleNonArray(jsonParser, deserializationContext);
        
        assertNotNull(result);
    }

    @Test
    public void testCreateContextual() throws Exception {
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        JsonMappingException jsonMappingException = new JsonMappingException("");
        
        StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer();
        JsonDeserializer<?> result = stringArrayDeserializer.createContextual(deserializationContext, null);
        
        assertNotNull(result);
    }
}