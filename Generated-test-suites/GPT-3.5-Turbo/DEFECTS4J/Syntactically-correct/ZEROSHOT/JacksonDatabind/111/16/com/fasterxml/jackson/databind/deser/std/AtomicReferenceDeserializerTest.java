package com.fasterxml.jackson.databind.deser.std;

import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AtomicReferenceDeserializerTest {

    @Mock
    private JavaType mockJavaType;
    
    @Mock
    private ValueInstantiator mockValueInstantiator;
    
    @Mock
    private TypeDeserializer mockTypeDeserializer;
    
    @Mock
    private JsonDeserializer<?> mockJsonDeserializer;
    
    private AtomicReferenceDeserializer atomicReferenceDeserializer;
    
    @Before
    public void setup() {
        atomicReferenceDeserializer = new AtomicReferenceDeserializer(mockJavaType, mockValueInstantiator, mockTypeDeserializer, mockJsonDeserializer);
    }

    @Test
    public void testWithResolved() {
        // TODO: Implement test case
    }
    
    @Test
    public void testGetNullValue() throws JsonMappingException {
        // TODO: Implement test case
    }
    
    @Test
    public void testGetEmptyValue() {
        // TODO: Implement test case
    }
    
    @Test
    public void testReferenceValue() {
        // TODO: Implement test case
    }
    
    @Test
    public void testGetReferenced() {
        // TODO: Implement test case
    }
    
    @Test
    public void testUpdateReference() {
        // TODO: Implement test case
    }
    
    @Test
    public void testSupportsUpdate() {
        // TODO: Implement test case
    }
}