package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class SettableAnyPropertyTest {

 private SettableAnyProperty settableAnyProperty;
 private ObjectInstanceFactory instanceFactory;
 private DeserializationContext context;
 private JsonParser parser;

 @Before
 public void setup() {
 instanceFactory = mock(ObjectInstanceFactory.class);
 context = mock(DeserializationContext.class);
 parser = mock(JsonParser.class);
 settableAnyProperty = new SettableAnyProperty(mock(BeanProperty.class), mock(AnnotatedMember.class),
         mock(JavaType.class), null, mock(JsonDeserializer.class), mock(TypeDeserializer.class));
 }

 @Test
 public void testDeserializeAndSet() throws JsonProcessingException, IOException {
 String propName = "testProp";
 Object key = new Object();
 Object value = "testValue";

 when(instanceFactory.createInstance(settableAnyProperty.getType())).thenReturn(new Object());

 settableAnyProperty.deserializeAndSet(parser, context, new Object(), propName);
 verify(instanceFactory).createInstance(settableAnyProperty.getType());
 verify(context).handleUnknownProperty(eq(parser), eq(new Object()), eq(propName), settableAnyProperty, any());
 verify(context).handleValueDeserialization(any(), eq(parser), eq(context), eq(value), any());

 settableAnyProperty.set(new Object(), key, value);
 verify(instanceFactory).findValueDeserializer(eq(key.getClass()), eq(settableAnyProperty._type), eq(context));
 verify(settableAnyProperty._setter).callOnWith(new Object(), propName, value);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeAndSetWithException() throws JsonProcessingException, IOException {
 String propName = "testProp";
 Object value = new Object();
 doThrow(new IllegalArgumentException("Test exception")).when(settableAnyProperty._setter).callOnWith(any(), eq(propName), any());

 settableAnyProperty.deserializeAndSet(parser, context, new Object(), propName);
 verify(instanceFactory).createInstance(settableAnyProperty.getType());
 verify(context).handleUnknownProperty(eq(parser), eq(new Object()), eq(propName), settableAnyProperty, any());
 verify(context).handleValueDeserialization(any(), eq(parser), eq(context), eq(value), any());
 verify(context).handleValueDeserialization(any(), eq(parser), eq(context), any(), any());
 }

 @Test
 public void testDeserialize() throws JsonProcessingException, IOException {
 Object value = "testValue";
 when(parser.nextValue()).thenReturn(value);
 Object deserializedValue = settableAnyProperty.deserialize(parser, context);
 assertEquals(value, deserializedValue);
 }

 @Test
 public void testSet() throws JsonProcessingException, IOException {
 Object instance = new Object();
 Object propName = "testProp";
 Object value = "testValue";
 settableAnyProperty.set(instance, propName, value);
 verify(settableAnyProperty._setter).callOnWith(instance, propName, value);
 }

 @Test(expected = JsonMappingException.class)
 public void testSetWithException() throws JsonProcessingException, IOException {
 Object instance = new Object();
 Object propName = "testProp";
 Object value = "testValue";
 doThrow(new IOException("Test exception")).when(settableAnyProperty._setter).callOnWith(instance, propName, value);
 settableAnyProperty.set(instance, propName, value);
 }
}