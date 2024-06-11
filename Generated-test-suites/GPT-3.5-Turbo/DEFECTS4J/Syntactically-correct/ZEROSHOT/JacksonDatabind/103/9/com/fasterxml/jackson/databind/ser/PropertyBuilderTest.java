package com.fasterxml.jackson.databind.ser;

import org.junit.Test;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PropertyBuilderTest {
  
  @Test
  public void testBuildWriter() throws JsonMappingException {
    // Create Mocks
    SerializationConfig config = mock(SerializationConfig.class);
    BeanDescription beanDesc = mock(BeanDescription.class);
    JsonSerializer<?> ser = mock(JsonSerializer.class);
    TypeSerializer typeSer = mock(TypeSerializer.class);
    TypeSerializer contentTypeSer = mock(TypeSerializer.class);
    AnnotatedMember am = mock(AnnotatedMember.class);
    
    // Create PropertyBuilder
    PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
    
    // Create BeanPropertyDefinition
    BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
    
    // Create JavaType
    JavaType type = mock(JavaType.class);
    
    // Create SerializerProvider
    SerializerProvider prov = mock(SerializerProvider.class);
    
    // Test buildWriter
    BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, type, ser, typeSer, contentTypeSer, am, true);
    
    // Verify bpw type
    assertEquals(BeanPropertyWriter.class, bpw.getClass());
    
    // Verify prov methods invocations
    verify(prov).reportBadDefinition(type, "Exception Message");
    verify(prov).reportBadPropertyDefinition(beanDesc, propDef, "Exception Message");
    
    // Verify am methods invocations
    verify(am).getRawType();
    verify(am).getGenericType();
    
    // Verify ser methods invocations
    verify(ser).getType();
    
    // Verify propertyBuilder methods invocations
    verify(propertyBuilder).findSerializationType(am, true, type);
    verify(propertyBuilder).getDefaultInclusion(ArgumentMatchers.<Class<?>>any(), ArgumentMatchers.<Class<?>>any(), ArgumentMatchers.<JsonInclude.Value>any());
    verify(propertyBuilder).findSerializationType(am, true, type);
    verify(propertyBuilder).findUnwrappingNameTransformer(am);
  }
}