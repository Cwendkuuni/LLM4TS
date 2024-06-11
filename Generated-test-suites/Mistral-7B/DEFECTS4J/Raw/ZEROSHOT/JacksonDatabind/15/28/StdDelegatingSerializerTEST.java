package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class StdDelegatingSerializerTest {

 private Converter<Object, Object> converter;
 private JavaType delegateType;
 private JsonSerializer<Object> delegateSerializer;
 private StdDelegatingSerializer serializer;

 @Before
 public void setup() {
 converter = mock(Converter.class);
 delegateType = mock(JavaType.class);
 delegateSerializer = mock(JsonSerializer.class);
 serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
 }

 @Test
 public void testSerializationWithObject() throws JsonGenerationException, IOException {
 Object value = new Object();
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 TypeSerializer typeSerializer = mock(TypeSerializer.class);

 when(delegateSerializer.serialize(any(), eq(gen), eq(provider))).thenReturn(null);
 serializer.serialize(value, gen, provider);
 verify(delegateSerializer).serialize(eq(value), eq(gen), eq(provider));
 }

 @Test
 public void testSerializationWithType() throws JsonGenerationException, IOException {
 Object value = new Object();
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 Type type = mock(Type.class);
 TypeSerializer typeSerializer = mock(TypeSerializer.class);

 when(delegateSerializer.serializeWithType(any(), eq(gen), eq(provider), eq(typeSerializer))).thenReturn(null);
 serializer.serializeWithType(value, gen, provider, typeSerializer);
 verify(delegateSerializer).serializeWithType(eq(value), eq(gen), eq(provider), eq(typeSerializer));
 }

 @Test
 public void testResolve() throws JsonMappingException {
 SerializerProvider provider = mock(SerializerProvider.class);
 serializer.resolve(provider);
 verify(delegateSerializer).resolve(provider);
 }

 @Test
 public void testCreateContextual() throws JsonMappingException {
 BeanProperty property = mock(BeanProperty.class);
 serializer.createContextual(provider, property);
 verify(delegateSerializer).createContextual(provider, property);
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 SerializerProvider provider = mock(SerializerProvider.class);
 Type typeHint = mock(Type.class);
 serializer.getSchema(provider, typeHint);
 verify(delegateSerializer).getSchema(provider, typeHint);
 }

 @Test
 public void testGetSchemaWithOptional() throws JsonMappingException {
 SerializerProvider provider = mock(SerializerProvider.class);
 Type typeHint = mock(Type.class);
 boolean isOptional = true;
 serializer.getSchema(provider, typeHint, isOptional);
 verify(delegateSerializer).getSchema(provider, typeHint, isOptional);
 }

 @Test
 public void testAcceptJsonFormatVisitor() {
 JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
 JavaType typeHint = mock(JavaType.class);
 serializer.acceptJsonFormatVisitor(visitor, typeHint);
 verify(delegateSerializer).acceptJsonFormatVisitor(visitor, typeHint);
 }

 @Test
 public void testConvertValue() {
 Object value = new Object();
 Object delegateValue = serializer.convertValue(value);
 verify(converter).convert(eq(value));
 assertEquals(delegateValue, value);
 }
}