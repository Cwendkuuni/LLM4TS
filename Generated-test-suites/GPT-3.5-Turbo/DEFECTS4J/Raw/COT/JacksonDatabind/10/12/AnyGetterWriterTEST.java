
package com.fasterxml.jackson.databind.ser;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

import java.util.HashMap;
import java.util.Map;

public class AnyGetterWriterTest {

    @Mock
    private BeanProperty mockBeanProperty;

    @Mock
    private AnnotatedMember mockAnnotatedMember;

    @Mock
    private MapSerializer mockMapSerializer;

    @Test
    public void testGetAndSerialize() throws Exception {
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(mockBeanProperty, mockAnnotatedMember, mockMapSerializer);
        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        Map<String, Object> mockMap = new HashMap<>();
        
        // Case when value is null
        anyGetterWriter.getAndSerialize(null, jsonGenerator, serializerProvider);
        
        // Case when value is not null and instance of Map
        anyGetterWriter.getAndSerialize(mockMap, jsonGenerator, serializerProvider);
        
        // Case when value is not null but not instance of Map
        try {
            anyGetterWriter.getAndSerialize(new Object(), jsonGenerator, serializerProvider);
            Assert.fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            // expected exception
        }
        
        Mockito.verifyZeroInteractions(jsonGenerator);
        Mockito.verifyZeroInteractions(serializerProvider);
    }

    @Test
    public void testGetAndFilter() throws Exception {
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(mockBeanProperty, mockAnnotatedMember, mockMapSerializer);
        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        PropertyFilter mockFilter = Mockito.mock(PropertyFilter.class);
        Map<String, Object> mockMap = new HashMap<>();
        
        // Case when value is null
        anyGetterWriter.getAndFilter(null, jsonGenerator, serializerProvider, mockFilter);
        
        // Case when value is not null and instance of Map
        anyGetterWriter.getAndFilter(mockMap, jsonGenerator, serializerProvider, mockFilter);
        
        // Case when value is not null but not instance of Map
        try {
            anyGetterWriter.getAndFilter(new Object(), jsonGenerator, serializerProvider, mockFilter);
            Assert.fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            // expected exception
        }
        
        Mockito.verifyZeroInteractions(jsonGenerator);
        Mockito.verifyZeroInteractions(serializerProvider);
        Mockito.verifyZeroInteractions(mockFilter);
    }

    @Test
    public void testResolve() throws JsonMappingException {
        AnyGetterWriter anyGetterWriter = new AnyGetterWriter(mockBeanProperty, mockAnnotatedMember, mockMapSerializer);
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

        anyGetterWriter.resolve(serializerProvider);

        Mockito.verify(serializerProvider).handlePrimaryContextualization(Mockito.any(), Mockito.eq(mockBeanProperty));
    }
}
