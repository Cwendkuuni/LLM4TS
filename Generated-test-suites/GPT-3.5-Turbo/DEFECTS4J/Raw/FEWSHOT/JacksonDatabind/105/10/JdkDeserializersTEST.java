
package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;

import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.*;
import java.util.Stack;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class JdkDeserializersTest {
    
    @Mock
    private JsonParser jsonParser;
    
    @Test
    public void testFind() throws IOException, JsonProcessingException {
        MockitoAnnotations.initMocks(this);
        
        JsonDeserializer<?> deserializer;
        
        // UUID test
        deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
        assertEquals(UUIDDeserializer.class, deserializer.getClass());
        
        // AtomicBoolean test
        deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
        assertEquals(AtomicBooleanDeserializer.class, deserializer.getClass());
        
        // StackTraceElement test
        deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
        assertEquals(StackTraceElementDeserializer.class, deserializer.getClass());
        
        // ByteBuffer test
        deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
        assertEquals(ByteBufferDeserializer.class, deserializer.getClass());
        
        // FromStringDeserializer tests
        List<Class<?>> fromStringClasses = FromStringDeserializer.types();
        for (Class<?> cls : fromStringClasses) {
            deserializer = JdkDeserializers.find(cls, cls.getName());
            JsonDeserializer<?> mockedDeserializer = Mockito.mock(JsonDeserializer.class);
            when(mockedDeserializer.deserialize(jsonParser, null)).thenReturn(null);
            doReturn(mockedDeserializer).when(deserializer);
  
            assertEquals(mockedDeserializer, deserializer);
        }
        
        // Non-JDK class test
        deserializer = JdkDeserializers.find(String.class, String.class.getName());
        assertNull(deserializer);
    }
}
