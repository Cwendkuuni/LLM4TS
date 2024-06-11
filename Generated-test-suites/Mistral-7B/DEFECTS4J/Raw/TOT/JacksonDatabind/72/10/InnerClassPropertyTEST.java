package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.NestedPropertyConfiguration;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InnerClassPropertyTest {

 private static final String CLASS_NAME = InnerClassProperty.class.getName();

 @Mock
 private JsonParser mockJsonParser;

 @Mock
 private DeserializationContext mockContext;

 @Mock
 private Object mockBean;

 @Mock
 private AnnotatedClass mockAnnotatedClass;

 @Mock
 private AnnotatedConstructor mockAnnotatedConstructor;

 @Mock
 private BeanPropertyDefinition mockBeanPropertyDefinition;

 @Mock
 private NestedPropertyConfiguration mockNestedPropertyConfiguration;

 @Mock
 private JsonDeserializer<?> mockJsonDeserializer;

 @Mock
 private TypeDeserializer mockTypeDeserializer;

 private ObjectMapper objectMapper;

 private InnerClassProperty innerClassProperty;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 innerClassProperty = new InnerClassProperty(mockBeanPropertyDefinition, mockAnnotatedConstructor);
 }

 @Test
 public void testInnerClassProperty() throws Exception {
 // Test default constructor
 InnerClassProperty innerClassPropertyDefault = new InnerClassProperty(mockBeanPropertyDefinition, null);
 assertEquals(CLASS_NAME + "#default", innerClassPropertyDefault.getClass().getName());

 // Test constructor with AnnotatedConstructor
 AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
 when(mockAnnotatedClass.findMethod(anyString(), any(), any()))
     .thenReturn(annotatedMethod);
 when(annotatedMethod.getAnnotations()).thenReturn(new Annotation[0]);
 AnnotatedConstructor innerConstructor = mockAnnotatedConstructor;
 when(innerConstructor.getAnnotations()).thenReturn(new Annotation[0]);
 when(innerConstructor.getParameterTypes()).thenReturn(new Class[0]);
 InnerClassProperty innerClassPropertyAnnotated = new InnerClassProperty(mockBeanPropertyDefinition, innerConstructor);
 assertEquals(CLASS_NAME + "#annotated", innerClassPropertyAnnotated.getClass().getName());

 // Test deserializeAndSet method
 Object value = new Object();
 when(mockJsonParser.getCurrentName()).thenReturn("value");
 when(mockJsonParser.getValueAsString()).thenReturn("valueString");
 when(mockContext.findValueDeserializer(eq(mockAnnotatedClass), any())).thenReturn(mockJsonDeserializer);
 when(mockJsonDeserializer.deserialize(any(), eq(mockContext), any()))
     .thenReturn(value);
 innerClassProperty.deserializeAndSet(mockJsonParser, mockContext, mockBean);
 verify(mockBean).setValue(value);

 // Test deserializeSetAndReturn method
 when(mockJsonParser.getValueAsString()).thenReturn("valueString");
 when(mockBean.getValue()).thenReturn(null);
 Object deserializedValue = innerClassProperty.deserializeSetAndReturn(mockJsonParser, mockContext, mockBean);
 assertEquals(value, deserializedValue);

 // Test set method
 Object setValue = new Object();
 innerClassProperty.set(mockBean, setValue);
 assertEquals(setValue, mockBean.getValue());

 // Test setAndReturn method
 Object setAndReturnValue = new Object();
 when(mockBean.getValue()).thenReturn(null);
 Object returnValue = innerClassProperty.setAndReturn(mockBean, setAndReturnValue);
 assertEquals(setAndReturnValue, returnValue);

 // Test readResolve method
 innerClassProperty = (InnerClassProperty) innerClassProperty.readResolve();
 assertEquals(CLASS_NAME + "#readResolve", innerClassProperty.getClass().getName());

 // Test writeReplace method
 InnerClassProperty innerClassPropertyWrapped = new InnerClassProperty(innerClassProperty, mockAnnotatedConstructor);
 InnerClassProperty innerClassPropertyWrappedAnnotated = new InnerClassProperty(innerClassPropertyWrapped, mockAnnotatedConstructor);
 assertEquals(CLASS_NAME + "#writeReplace", innerClassPropertyWrappedAnnotated.getClass().getName());
 }
}