package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;

@RunWith(MockitoJUnitRunner.class)
public class StringArrayDeserializerTest {

    @Mock
    JsonParser jsonParser;
    
    @Mock
    DeserializationContext deserializationContext;

    @Test
    public void testDeserialize() throws IOException {
        StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer();
        
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        
        assertEquals(0, result.length);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer();

        Object result = stringArrayDeserializer.deserializeWithType(jsonParser, deserializationContext, null);
        
        assertNull(result);
    }

    @Test
    public void testHandleNonArray() {
        StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer();
        
        try {
          stringArrayDeserializer.handleNonArray(jsonParser, deserializationContext);
          fail("Expected IOException");
        } catch (IOException e) {
          // expected
        }
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer();
        
        JsonDeserializer<?> result = stringArrayDeserializer.createContextual(deserializationContext, null);
        
        assertNull(result);
    }
    
}