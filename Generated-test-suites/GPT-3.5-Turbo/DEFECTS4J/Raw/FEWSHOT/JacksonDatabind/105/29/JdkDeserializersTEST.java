package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonDeserializer;

public class JdkDeserializersTest {

    private JdkDeserializers jdkDeserializers;

    @Before
    public void setUp() {
        jdkDeserializers = new JdkDeserializers();
    }
    
    @Test
    public void testFind() {
        JsonDeserializer<?> deserializer = jdkDeserializers.find(UUID.class, UUID.class.getName());
        assertEquals(UUIDDeserializer.class, deserializer.getClass());
        
        deserializer = jdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
        assertEquals(StackTraceElementDeserializer.class, deserializer.getClass());
        
        deserializer = jdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
        assertEquals(AtomicBooleanDeserializer.class, deserializer.getClass());
        
        deserializer = jdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
        assertEquals(ByteBufferDeserializer.class, deserializer.getClass());
        
        String nonExistingClassName = "com.test.NonExistingClass";
        deserializer = jdkDeserializers.find(UUID.class, nonExistingClassName);
        assertNull(deserializer);
    }
    
    @Test
    public void testFindWithNullClassName() {
        JsonDeserializer<?> deserializer = jdkDeserializers.find(UUID.class, null);
        assertNull(deserializer);
        
        deserializer = jdkDeserializers.find(StackTraceElement.class, null);
        assertNull(deserializer);
        
        deserializer = jdkDeserializers.find(AtomicBoolean.class, null);
        assertNull(deserializer);
        
        deserializer = jdkDeserializers.find(ByteBuffer.class, null);
        assertNull(deserializer);
    }
    
    @Test
    public void testFindWithNonJDKClass() {
        JsonDeserializer<?> deserializer = jdkDeserializers.find(String.class, String.class.getName());
        assertNull(deserializer);
    }
    
    @Test
    public void testFindWithMockedDeserializer() {
        JsonDeserializer<?> mockDeserializer = mock(JsonDeserializer.class);
        String className = "com.test.MockedClass";
        HashSet<String> classNames = mock(HashSet.class);
        when(classNames.contains(className)).thenReturn(true);
        when(classNames.contains("invalidClassName")).thenReturn(false);
        
        JdkDeserializers deserializers = new JdkDeserializers();
        deserializers._classNames = classNames;
        deserializers.FromStringDeserializer = mock(FromStringDeserializer.class);
        
        JsonDeserializer<?> deserializer = deserializers.find(MockedClass.class, className);
        assertNull(deserializer);
        
        deserializer = deserializers.find(MockedClass.class, className);
        assertNull(deserializer);
        
        deserializer = deserializers.find(MockedClass.class, "invalidClassName");
        assertNull(deserializer);
        
        deserializer = deserializers.find(MockedClass.class, null);
        assertNull(deserializer);
    }
}